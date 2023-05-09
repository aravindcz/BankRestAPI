package com.aravindcz.bankrestapi.services.implementations;

import com.aravindcz.bankrestapi.exceptions.InconsistentDetailsException;
import com.aravindcz.bankrestapi.exceptions.LockerAlreadyPresentException;
import com.aravindcz.bankrestapi.exceptions.LockerNotFoundException;
import com.aravindcz.bankrestapi.exceptions.UnauthorizedCustomerException;
import com.aravindcz.bankrestapi.models.dtos.LockerDTO;
import com.aravindcz.bankrestapi.models.dtos.ResponseDTO;
import com.aravindcz.bankrestapi.models.entities.Locker;
import com.aravindcz.bankrestapi.models.entities.Offering;
import com.aravindcz.bankrestapi.repositories.interfaces.CustomerRepository;
import com.aravindcz.bankrestapi.repositories.interfaces.LockerRepository;
import com.aravindcz.bankrestapi.repositories.interfaces.OfferingRepository;
import com.mindstix.bankrestapi.exceptions.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * LockerService - provides methods which provides functionality for to and fro conversion of entity and dto , validation checks
 * and methods that talk with jpa repository to fetch necesary details from the database
 * @author Aravind C
 */
@Service
@AllArgsConstructor
public class LockerService {

    @Autowired
    private LockerRepository lockerRepository;
    @Autowired
    private OfferingRepository offeringRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    ModelMapper modelMapper;


    /**
     * Method to convert locker data transfer object to locker jpa entity format object and also takes care of the relationship it has
     * with offering object
     * @param customerId
     * @param lockerDTO - locker data transfer object
     * @return - locker jpa entity format object
     */
    private Locker convertLockerDTOToLocker(long customerId, LockerDTO lockerDTO) {

        Locker locker = modelMapper.map(lockerDTO,Locker.class);
        Optional<Offering> offering = offeringRepository.findByCustomer_Id(customerId);
        List<Locker> lockerList = offering.get().getLocker();
        lockerList.add(locker);

        locker.setOffering(offering.get());

        return locker;
    }


    /**
     * Method to convert locker jpa entity format object to locker data transfer object
     * @param locker - locker jpa entity format object
     * @return - locker data transfer object
     */
    private LockerDTO convertLockerToLockerDTO(Locker locker) {


        LockerDTO lockerDTO = modelMapper.map(locker, LockerDTO.class);

        return lockerDTO;

    }

    /**
     * Method that validates locker , checks whether the current customer is authorized enough to do the operation on the current
     * resource
     * @param customerId
     * @param number - locker number
     * @throws UnauthorizedCustomerException - if the customer should not be allowed to access the resource
     * @throws LockerAlreadyPresentException - mainly when trying to create duplicate lockers
     * @throws LockerNotFoundException - mainly when trying to perform operations on non existing lockers
     */
    public void validateLocker(long customerId,long number) throws UnauthorizedCustomerException, LockerAlreadyPresentException, LockerNotFoundException {
        Optional<Offering> optionalOffering = offeringRepository.findByCustomer_Id(customerId);
        if(!optionalOffering.isPresent())
            throw new UnauthorizedCustomerException();

        Optional<Locker> optionalLocker = lockerRepository.findByNumber(number);

        if((!optionalLocker.isPresent())||(optionalLocker.get().getOffering().getId()!=optionalOffering.get().getId()))
            throw new UnauthorizedCustomerException();

        if (!optionalLocker.isPresent())
            throw new LockerNotFoundException();
    }

    /**
     * Method to save the current locker to data base
     * @param customerId
     * @param lockerDTO - locker data transfer object
     * @return - custom response entity consisting of saved locker details
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity save(long customerId, LockerDTO lockerDTO) throws Exception {

        customerService.validateCustomer(customerId);

        if(lockerRepository.existsByNumber(lockerDTO.getNumber()))
            throw new InconsistentDetailsException();


        try {
            Locker locker = convertLockerDTOToLocker(customerId, lockerDTO);
            offeringRepository.save(offeringRepository.findByCustomer_Id(customerId).get());
        } catch (Exception e) {
            throw new Exception();
        }

        ResponseDTO responseDTO = new ResponseDTO(true,201,"Locker details successfully added",lockerDTO.getNumber());
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.CREATED);

        return responseEntity;
    }


    /**
     * Method that returns all the lockers associated with a customer
     * @param customerId
     * @return - list of locker data transfer objects associated with customer
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity findAll(long customerId) throws Exception {


        customerService.validateCustomer(customerId);

        Optional<Offering> offering = offeringRepository.findByCustomer_Id(customerId);

        if(!offering.isPresent())
            throw new UnauthorizedCustomerException();

        List<Locker> lockerList = lockerRepository.findByOffering_Id(offering.get().getId());


        List<LockerDTO> lockerDTOList = lockerList.stream()
                .map(locker -> convertLockerToLockerDTO(locker))
                .collect(Collectors.toList());

        ResponseDTO responseDTO = new ResponseDTO(true,200,"Locker details successfully retrieved",lockerDTOList);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);

        return responseEntity;
    }

    /**
     * Method that finds a single locker associated with customer based on locker number
     * @param customerId
     * @param number
     * @return - customer response entity consisting of the locker data transfer object
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity findByNumber(long customerId,long number) throws Exception {

        customerService.validateCustomer(customerId);
        validateLocker(customerId,number);
        ResponseEntity responseEntity;

        try {
            Optional<Locker> optionalLocker = lockerRepository.findByNumber(number);
            LockerDTO lockerDTO = convertLockerToLockerDTO(optionalLocker.get());
            ResponseDTO responseDTO = new ResponseDTO(true,200,"Locker details successfully retrieved",lockerDTO);
            responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);
        }catch (Exception e){
            throw new Exception();
        }


        return responseEntity;
    }


    /**
     * Method that updates the locker details of a specific customer
     * @param customerId
     * @param number - locker number
     * @param lockerDTO - locker data transfer object
     * @return - custom response entity consisting of the updated locker data transfer object
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity update(long customerId,long number, LockerDTO lockerDTO) throws Exception {

        if(number!=lockerDTO.getNumber())
            throw new InconsistentDetailsException();

        customerService.validateCustomer(customerId);
        validateLocker(customerId,lockerDTO.getNumber());

        try {
            Optional<Locker> optionalLocker = lockerRepository.findByNumber(lockerDTO.getNumber());
            Locker locker = optionalLocker.get();
            locker.setAccountNumber(lockerDTO.getAccountNumber());
            locker.setBranchCode(lockerDTO.getBranchCode());
            lockerRepository.save(locker);
        }catch (Exception e){
            throw new Exception();
        }



        ResponseDTO responseDTO = new ResponseDTO(true,200,"Locker details successfully updated",lockerDTO);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);

        return responseEntity;
    }

    /**
     * Method that deletes a locker based on the locker number provided
     * @param customerId
     * @param number - locker number
     * @return - custom reponse entity consisting of the status of operation
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity deleteByNumber(long customerId,long number) throws Exception {

        customerService.validateCustomer(customerId);
        validateLocker(customerId,number);


        try {
                lockerRepository.deleteByNumber(number);
                ResponseDTO responseDTO = new ResponseDTO(true,200,"Locker details successfully removed",null);
                ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);

                return responseEntity;
        } catch (Exception e){
            throw new Exception();
        }


    }
}
