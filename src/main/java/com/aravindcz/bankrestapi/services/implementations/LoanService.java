package com.aravindcz.bankrestapi.services.implementations;


import com.aravindcz.bankrestapi.exceptions.InconsistentDetailsException;
import com.aravindcz.bankrestapi.exceptions.LoanAlreadyPresentException;
import com.aravindcz.bankrestapi.exceptions.LoanNotFoundException;
import com.aravindcz.bankrestapi.exceptions.UnauthorizedCustomerException;
import com.mindstix.bankrestapi.exceptions.*;
import com.aravindcz.bankrestapi.models.dtos.LoanDTO;
import com.aravindcz.bankrestapi.models.dtos.ResponseDTO;
import com.aravindcz.bankrestapi.models.entities.Loan;
import com.aravindcz.bankrestapi.models.entities.Offering;
import com.aravindcz.bankrestapi.repositories.interfaces.CustomerRepository;
import com.aravindcz.bankrestapi.repositories.interfaces.LoanRepository;
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
 * LoanService - provides methods which provides functionality for to and fro conversion of entity and dto , validation checks
 * and methods that talk with jpa repository to fetch necesary details from the database
 * @author Aravind C
 */
@Service
@AllArgsConstructor
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private OfferingRepository offeringRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    ModelMapper modelMapper;


    /**
     * Method to convert loan data transfer object to loan jpa entity format object and also takes care of the relationship it has
     * with offering object
     * @param customerId
     * @param loanDTO - loan data transfer object
     * @return - loan jpa entity format object
     */
    private Loan convertLoanDTOToLoan(long customerId, LoanDTO loanDTO) {

        Loan loan = modelMapper.map(loanDTO,Loan.class);
        Optional<Offering> offering = offeringRepository.findByCustomer_Id(customerId);
        List<Loan> loanList = offering.get().getLoan();
        loanList.add(loan);

        loan.setOffering(offering.get());

        return loan;
    }


    /**
     * Method to convert loan jpa entity format object to loan data transfer object
     * @param loan - loan jpa entity format object
     * @return - loan data transfer object
     */
    private LoanDTO convertLoanToLoanDTO(Loan loan) {


        LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);

        return loanDTO;

    }

    /**
     * Method that validates loan , checks whether the current customer is authorized enough to do the operation on the current
     * resource
     * @param customerId
     * @param number - loan number
     * @throws UnauthorizedCustomerException - if the customer should not be allowed to access the resource
     * @throws LoanAlreadyPresentException - mainly when trying to create duplicate loans
     * @throws LoanNotFoundException - mainly when trying to perform operations on non existing loans
     */
    public void validateLoan(long customerId,long number) throws UnauthorizedCustomerException, LoanAlreadyPresentException, LoanNotFoundException {
        Optional<Offering> optionalOffering = offeringRepository.findByCustomer_Id(customerId);
        if(!optionalOffering.isPresent())
            throw new UnauthorizedCustomerException();

        Optional<Loan> optionalLoan = loanRepository.findByNumber(number);

        if((!optionalLoan.isPresent())||(optionalLoan.get().getOffering().getId()!=optionalOffering.get().getId()))
            throw new UnauthorizedCustomerException();

        if (!optionalLoan.isPresent())
            throw new LoanNotFoundException();
    }

    /**
     * Method to save the current loan to data base
     * @param customerId
     * @param loanDTO - loan data transfer object
     * @return - custom response entity consisting of saved loan details
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity save(long customerId, LoanDTO loanDTO) throws Exception {

        customerService.validateCustomer(customerId);

        if(loanRepository.existsByNumber(loanDTO.getNumber()))
            throw new InconsistentDetailsException();


        try {
            Loan loan = convertLoanDTOToLoan(customerId, loanDTO);
            offeringRepository.save(offeringRepository.findByCustomer_Id(customerId).get());
        } catch (Exception e) {
            throw new Exception();
        }

        ResponseDTO responseDTO = new ResponseDTO(true,201,"Loan details successfully added",loanDTO.getNumber());
        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.CREATED);

        return responseEntity;
    }


    /**
     * Method that returns all the loans associated with a customer
     * @param customerId
     * @return - list of loan data transfer objects associated with customer
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity findAll(long customerId) throws Exception {


        customerService.validateCustomer(customerId);

        Optional<Offering> offering = offeringRepository.findByCustomer_Id(customerId);

        if(!offering.isPresent())
            throw new UnauthorizedCustomerException();

        List<Loan> loanList = loanRepository.findByOffering_Id(offering.get().getId());


        List<LoanDTO> loanDTOList = loanList.stream()
                .map(loan -> convertLoanToLoanDTO(loan))
                .collect(Collectors.toList());

        ResponseDTO responseDTO = new ResponseDTO(true,200,"Loan details successfully retrieved",loanDTOList);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);

        return responseEntity;
    }

    /**
     * Method that finds a single loan associated with customer based on loan number
     * @param customerId
     * @param number
     * @return - customer response entity consisting of the loan data transfer object
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity findByNumber(long customerId,long number) throws Exception {

        customerService.validateCustomer(customerId);
        validateLoan(customerId,number);
        ResponseEntity responseEntity;

        try {
            Optional<Loan> optionalLoan = loanRepository.findByNumber(number);
            LoanDTO loanDTO = convertLoanToLoanDTO(optionalLoan.get());
            ResponseDTO responseDTO = new ResponseDTO(true,200,"Loan details successfully retrieved",loanDTO);
            responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);
        }catch (Exception e){
            throw new Exception();
        }


        return responseEntity;
    }


    /**
     * Method that updates the loan details of a specific customer
     * @param customerId
     * @param number - loan number
     * @param loanDTO - loan data transfer object
     * @return - custom response entity consisting of the updated loan data transfer object
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity update(long customerId,long number, LoanDTO loanDTO) throws Exception {

        if(number!=loanDTO.getNumber())
            throw new InconsistentDetailsException();

        customerService.validateCustomer(customerId);
        validateLoan(customerId,loanDTO.getNumber());

        try {
            Optional<Loan> optionalLoan = loanRepository.findByNumber(loanDTO.getNumber());
            Loan loan = optionalLoan.get();
            loan.setCustomerId(loanDTO.getCustomerId());
            loan.setAmount(loanDTO.getAmount());
            loanRepository.save(loan);
        }catch (Exception e){
            throw new Exception();
        }



        ResponseDTO responseDTO = new ResponseDTO(true,200,"Loan details successfully updated",loanDTO);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);

        return responseEntity;
    }

    /**
     * Method that deletes a loan based on the loan number provided
     * @param customerId
     * @param number - loan number
     * @return - custom reponse entity consisting of the status of operation
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity deleteByNumber(long customerId,long number) throws Exception {

        customerService.validateCustomer(customerId);
        validateLoan(customerId,number);


        try {
            loanRepository.deleteByNumber(number);
            ResponseDTO responseDTO = new ResponseDTO(true,200,"Loan details successfully removed",null);
            ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);

            return responseEntity;
        } catch (Exception e){
            throw new Exception();
        }


    }
}