package com.aravindcz.bankrestapi.services.implementations;

import com.aravindcz.bankrestapi.exceptions.*;
import com.aravindcz.bankrestapi.models.dtos.CustomerDTO;
import com.aravindcz.bankrestapi.models.dtos.ResponseDTO;
import com.aravindcz.bankrestapi.models.dtos.UserDTO;
import com.aravindcz.bankrestapi.models.entities.Customer;
import com.aravindcz.bankrestapi.repositories.interfaces.CustomerRepository;
import com.mindstix.bankrestapi.exceptions.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * CustomerService - provides methods which provides functionality for to and fro conversion of entity and dto , validation checks
 * and methods that talk with jpa repository to fetch necesary details from the database
 * @author Aravind C
 */
@Service
@Slf4j
@AllArgsConstructor
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    ModelMapper modelMapper;


    /**
     * Method to convert customer data transfer object to customer jpa entity format object using model mapper
     * @param customerDTO - customer data transfer object
     * @return customer jpa entity format object
     */
    private Customer convertCustomerDTOToCustomer(CustomerDTO customerDTO){
        Customer customer = modelMapper.map(customerDTO,Customer.class);
        return customer;
    }

    /**
     * Method to convert customer jpa entity format object to customer data transfer object using model mapper
     * @param customer - customer jpa entity format object
     * @return customer data transfer object
     */
    private CustomerDTO convertCustomerToCustomerDTO(Customer customer){
        CustomerDTO customerDTO = modelMapper.map(customer,CustomerDTO.class);
        return customerDTO;
    }


    /**
     * Method to convert user data transfer object to customer jpa entity format object using model mapper mainly used for registeration
     * of user
     * @param userDTO - user data transfer object
     * @return customer jpa entity format object
     */
    private Customer convertUserDTOToCustomer(UserDTO userDTO){

        Customer customer = modelMapper.map(userDTO,Customer.class);

        return customer;

    }


    /**
     * Method to ensure that the current logged in customer is authorized to access and perform operation on the resource
     * @param id - customer id
     * @throws  if user should be forbidden from doing operation on the resource
     */
    public void validateCustomer(long id) throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(userDetails.getAuthorities()
                .stream()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_EMPLOYEE")).count() > 0)
            return;

        Optional<Customer> optionalCustomer;

        try {
            optionalCustomer = customerRepository.findById(id);
        } catch (Exception e){
            throw  new Exception();
        }

        if((!optionalCustomer.isPresent()) || (!(userDetails.getUsername().equals(optionalCustomer.get().getEmail()))))
            throw new UnauthorizedCustomerException();
    }

    /**
     * Method to validate the email provided during registration
     * @param email - user supplied email address
     * @throws InvalidEmailAddressException - if the email address is not RFC regex complaint
     */
    private void validateEmail(String email) throws InvalidEmailAddressException {
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        boolean isValid = Pattern.compile(regexPattern)
                .matcher(email)
                .matches();

        if(!isValid)
            throw new InvalidEmailAddressException();
    }


    /**
     * Method to save the customer details to database
     * @param customerDTO - customer data transfer object
     * @return - custom response entity showing the result of evaluation
     * @throws Exception when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity save(CustomerDTO customerDTO) throws Exception {

        validateCustomer(customerDTO.getId());

        Optional<Customer> optionalCustomer;

        try {
            optionalCustomer = customerRepository.findById(customerDTO.getId());
        } catch (Exception e){
            throw new Exception();
        }

        //check if customer details are already added
        if(optionalCustomer.isPresent() && optionalCustomer.get().getName()!=null)
            throw new CustomerDetailsAlreadyAddedException();

        //adding customer details to the database
        try {
            Customer customer = convertCustomerDTOToCustomer(customerDTO);
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            customer.setEmail(userDetails.getUsername());
            customer.setPassword(userDetails.getPassword());
            customer.setRole("ROLE_CUSTOMER");
            customerRepository.save(customer);
        } catch (Exception e){
            throw new Exception();
        }

        ResponseDTO responseDTO = new ResponseDTO(true,201,"Customer details successfully added",customerDTO);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.CREATED);

        return responseEntity;
    }


    /**
     * Method to find all the customers from the database
     * @return list of CustomerDTOs
     * @throws Exception when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity findAll() throws Exception {


        List<Customer> customerList;

        try {
            customerList = customerRepository.findAll();
        } catch (Exception e){
            throw new Exception();
        }

        List<CustomerDTO> customerDTOList = customerList.stream()
                .map((customer) -> convertCustomerToCustomerDTO(customer))
                .collect(Collectors.toList());

        ResponseDTO responseDTO = new ResponseDTO(true,200,"Customer details successfully retrieved",customerDTOList);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);

        return responseEntity;
    }

    /**
     * Method to find each of the customer based on id
     * @param id - customer id
     * @return - custom response entity containing CustomerDTO
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity findById(long id) throws Exception {

        validateCustomer(id);

        Optional<Customer> optionalCustomer;

        try {
            optionalCustomer = customerRepository.findById(id);
        } catch (Exception e){
            throw  new Exception();
        }



        CustomerDTO customerDTO = convertCustomerToCustomerDTO(optionalCustomer.get());

        ResponseDTO responseDTO = new ResponseDTO(true,200,"Customer details successfully retrieved",customerDTO);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);

        return responseEntity;
    }

    /**
     * Method to update customer detials based on id
     * @param id - customerId
     * @param customerDTO - customer details input by user
     * @return - response entity consisting of the updated customer details
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity update(long id,CustomerDTO customerDTO) throws Exception {

        if(id!=customerDTO.getId())
            throw new InconsistentDetailsException();

        validateCustomer(id);

        Optional<Customer> optionalCustomer;



        try {
            optionalCustomer = customerRepository.findById(customerDTO.getId());
        } catch (Exception e){
            throw new Exception();
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();



        //updating the customer details in the database
        try {
            Customer customer = convertCustomerDTOToCustomer(customerDTO);
            customer.setEmail(userDetails.getUsername());
            customer.setPassword(userDetails.getPassword());
            customer.setRole("ROLE_CUSTOMER");
            customerRepository.save(customer);
        } catch (Exception e){
            throw new Exception();
        }

        ResponseDTO responseDTO = new ResponseDTO(true,200,"Customer details successfully updated",customerDTO);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);

        return responseEntity;
    }

    /**
     * Method to delete the customer based on the customer id supplied
     * @param id - customer id
     * @return - custom response entity
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity deleteById(long id) throws Exception {

        validateCustomer(id);

        Optional<Customer> optionalCustomer;


        try {
                customerRepository.deleteById(id);
                ResponseDTO responseDTO = new ResponseDTO(true,200,"Customer details successfully removed",null);
                ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);

                return responseEntity;
        } catch (Exception e){
            throw new Exception();
        }

    }


    /**
     * Method which creates a customer account
     * @param userDTO - user details like email and password input by the user
     * @return - custom response entity containing customer id
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity createAccount(UserDTO userDTO) throws Exception {

        validateEmail(userDTO.getEmail());

        Optional<Customer> optionalCustomer;
        long id;

        try {
            optionalCustomer = customerRepository.findByEmail(userDTO.getEmail());
        } catch (Exception e){
            throw new Exception();
        }

        if(optionalCustomer.isPresent())
            throw new EmailAlreadyRegisteredException();

        try {
            Customer customer = convertUserDTOToCustomer(userDTO);
            customerRepository.save(customer);
            id=customerRepository.findByEmail(userDTO.getEmail()).get().getId();
        } catch (Exception e){
            throw new Exception();
        }

        ResponseDTO responseDTO = new ResponseDTO(true,201,"Customer account successfully created",new Long(id));
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.CREATED);

        return responseEntity;

    }


}