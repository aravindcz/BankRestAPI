package com.aravindcz.bankrestapi.services.implementations;

import com.aravindcz.bankrestapi.exceptions.OfferingDetailsAlreadyAddedException;
import com.aravindcz.bankrestapi.exceptions.OfferingNotFoundException;
import com.aravindcz.bankrestapi.models.dtos.LoanDTO;
import com.aravindcz.bankrestapi.models.dtos.LockerDTO;
import com.aravindcz.bankrestapi.models.dtos.OfferingDTO;
import com.aravindcz.bankrestapi.models.dtos.ResponseDTO;
import com.aravindcz.bankrestapi.models.entities.Customer;
import com.aravindcz.bankrestapi.models.entities.Loan;
import com.aravindcz.bankrestapi.models.entities.Locker;
import com.aravindcz.bankrestapi.models.entities.Offering;
import com.aravindcz.bankrestapi.repositories.interfaces.CustomerRepository;
import com.aravindcz.bankrestapi.repositories.interfaces.LoanRepository;
import com.aravindcz.bankrestapi.repositories.interfaces.LockerRepository;
import com.aravindcz.bankrestapi.repositories.interfaces.OfferingRepository;
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
 * OfferingService - provides methods which provides functionality for to and fro conversion of entity and dto , validation checks
 * and methods that talk with jpa repository to fetch necesary details from the database
 * @author Aravind C
 */
@Service
@AllArgsConstructor
public class OfferingService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OfferingRepository offeringRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private LockerRepository lockerRepository;
    @Autowired
    private LoanRepository loanRepository;


    /**
     * Method which converts the locker data transfer object to locker jpa entity format object and also manages the relationship it
     * has with offering object
     * @param offering - offering object which consists of all the services that the customer has access to
     * @param lockerDTO - locker data transfer object
     * @return - locker jpa entity format object
     */
    private Locker convertLockerDTOToLocker(Offering offering,LockerDTO lockerDTO){

        Locker locker = modelMapper.map(lockerDTO,Locker.class);
        locker.setOffering(offering);
        return locker;
    }


    /**
     * Method which converts the loan data transfer object to loan jpa entity format object and also manages the relationship it
     * has with offering object
     * @param offering - offering object which consists of all the services that the customer has access to
     * @param loanDTO - loan data transfer object
     * @return - loan jpa entity format object
     */
    private Loan convertLoanDTOToLoan(Offering offering, LoanDTO loanDTO){

        Loan loan = modelMapper.map(loanDTO,Loan.class);
        loan.setOffering(offering);
        return loan;
    }

    /**
     * Method to convert offering data transfer object to offering jpa entity format object and also manages the relationship it has
     * with customer object
     * @param customer - customer jpa entity format object
     * @param offeringDTO - offering data transfer object
     * @return offering jpa entity format object
     */
    private Offering convertOfferingDTOToOffering(Customer customer,OfferingDTO offeringDTO){

        Offering offering = modelMapper.map(offeringDTO,Offering.class);
        List<Locker> lockerList = offeringDTO.getLocker().stream()
                .map(lockerDTO -> convertLockerDTOToLocker(offering,lockerDTO))
                        .collect(Collectors.toList());

        List<Loan> loanList = offeringDTO.getLoan().stream()
                .map(loanDTO -> convertLoanDTOToLoan(offering,loanDTO))
                .collect(Collectors.toList());


        offering.setCustomer(customer);
        offering.setLoan(loanList);
        offering.setLocker(lockerList);
        customer.setOffering(offering);

        return offering;
    }


    /**
     * Method to convert offering jpa entity format object to offering data transfer object using model mapper
     * @param offering - offering jpa entity format object
     * @return - offering data transfer object
     */
    private OfferingDTO convertOfferingToOfferingDTO(Offering offering){
        OfferingDTO offeringDTO = modelMapper.map(offering,OfferingDTO.class);
        return offeringDTO;
    }

//    public void validateOffering(long customerId) throws Exception {
//
//        customerService.validateCustomer(customerId);
//        Optional<Customer> optionalCustomer;
//        try {
//            optionalCustomer = customerRepository.findById(customerId);
//        } catch (Exception e){
//            throw new Exception();
//        }
//    }


    /**
     * Method that saves the offering data transfer object to the database
     * @param customerId - customer id
     * @param offeringDTO - offering data transfer object
     * @return - customer response entity
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity save(long customerId,OfferingDTO offeringDTO) throws Exception {


       customerService.validateCustomer(customerId);
        Optional<Customer> optionalCustomer;
        try {
            optionalCustomer = customerRepository.findById(customerId);
        } catch (Exception e){
            throw new Exception();
        }


        Optional<Offering> optionalOffering;

        try {
            optionalOffering = offeringRepository.findByCustomer_Id(customerId);
        } catch (Exception e){
            throw new Exception();
        }

        if(optionalOffering.isPresent())
            throw  new OfferingDetailsAlreadyAddedException();

        try {
            Offering offering = convertOfferingDTOToOffering(optionalCustomer.get(),offeringDTO);
            customerRepository.save(customerRepository.findById(customerId).get());
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new Exception();
        }

        ResponseDTO responseDTO = new ResponseDTO(true,201,"Offering details successfully added",null);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.CREATED);

        return responseEntity;
    }


    /**
     * Method to return all the offerings associated with customer in the form of offering data transfer objects
     * @param customerId - customer id
     * @return - custom response entity consisting of offering data transfer objects
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */

    public ResponseEntity findAll(long customerId) throws Exception {

        customerService.validateCustomer(customerId);


        Optional<Offering> offering;

        try {
            offering = offeringRepository.findByCustomer_Id(customerId);
        } catch (Exception e){

            throw new Exception();
        }

        if(!offering.isPresent())
            throw new OfferingNotFoundException();

        OfferingDTO offeringDTO = convertOfferingToOfferingDTO(offering.get());
        ResponseDTO responseDTO = new ResponseDTO(true,200,"Offering details successfully retrieved",offeringDTO);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);

        return responseEntity;
    }


    /**
     * Method to update a specific offering based on the customer id
     * @param customerId -
     * @param offeringDTO - offering data transfer object
     * @return - custom response entity consisting of updated offering
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
//    public ResponseEntity update(long customerId,OfferingDTO offeringDTO) throws Exception {
//
//
//        customerService.validateCustomer(customerId);
//
//        Optional<Customer> optionalCustomer;
//
//        try {
//            optionalCustomer = customerRepository.findById(customerId);
//        } catch (Exception e){
//            throw new Exception();
//        }
//
//
//        try {
//            Offering offering = convertOfferingDTOToOffering(optionalCustomer.get(),offeringDTO);
//            offeringRepository.save(offering);
//        } catch (Exception e){
//            throw new Exception();
//        }
//
//        ResponseDTO responseDTO = new ResponseDTO(true,200,"Successfully updated offering data",null);
//        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);
//
//        return responseEntity;
//
//    }


}
