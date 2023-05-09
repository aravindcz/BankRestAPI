package com.aravindcz.bankrestapi.services.implementations;

import com.aravindcz.bankrestapi.exceptions.*;
import com.mindstix.bankrestapi.exceptions.*;
import com.aravindcz.bankrestapi.models.dtos.EmployeeDTO;
import com.aravindcz.bankrestapi.models.dtos.ResponseDTO;
import com.aravindcz.bankrestapi.models.dtos.UserDTO;
import com.aravindcz.bankrestapi.models.entities.Employee;
import com.aravindcz.bankrestapi.repositories.interfaces.EmployeeRepository;
import lombok.AllArgsConstructor;
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
 * EmployeeService - provides methods which provides functionality for to and fro conversion of entity and dto , validation checks
 * and methods that talk with jpa repository to fetch necesary details from the database
 * @author Aravind C
 */
@Service
@AllArgsConstructor
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    ModelMapper modelMapper;


    /**
     * Method to convert employee data transfer object to employee jpa entity format object
     * @param employeeDTO - employee data transfer object
     * @return - employee jpa entity format object
     */
    private Employee convertEmployeeDTOToEmployee(EmployeeDTO employeeDTO){
        Employee employee = modelMapper.map(employeeDTO,Employee.class);
        return employee;
    }

    /**
     * Method to convert employee jpa entity format object to employee data transfer object
     * @param employee - employee jpa entity format
     * @return - employee data transfer object
     */
    private EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO = modelMapper.map(employee,EmployeeDTO.class);
        return employeeDTO;
    }

    /**
     * Method to convert user data tranfer object mainly provided during authentication to employee jpa entity format object
     * @param userDTO - user data transfer object
     * @return  - employee jpa entity format object
     */
    private Employee convertUserDTOToEmployee(UserDTO userDTO){

        Employee employee = modelMapper.map(userDTO,Employee.class);

        return employee;

    }

    /**
     * Method to validate if the id is assoicated with this employee so as to verify that the employee is authorized to perform
     * the operation on the resource
     * @param id - employee id
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    private void validateEmployee(long id) throws Exception {
        Optional<Employee> optionalEmployee;

        try {
            optionalEmployee = employeeRepository.findById(id);
        } catch (Exception e){
            throw  new Exception();
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if((!optionalEmployee.isPresent()) || (!(userDetails.getUsername().equals(optionalEmployee.get().getEmail()))))
            throw new UnauthorizedEmployeeException();
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
     * Method to save the employee details to the database
     * @param employeeDTO - employee data transfer object
     * @return - custom response entity consisting of employee id which corresponds to the employee saved in the database
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity save(EmployeeDTO employeeDTO) throws Exception {

        validateEmployee(employeeDTO.getId());

        Optional<Employee> optionalEmployee;

        try {
            optionalEmployee = employeeRepository.findById(employeeDTO.getId());
        } catch (Exception e){
            throw new Exception();
        }

        if(optionalEmployee.isPresent() && optionalEmployee.get().getName()!=null)
            throw new EmployeeDetailsAlreadyAddedException();

        try {
            Employee employee = convertEmployeeDTOToEmployee(employeeDTO);
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            employee.setEmail(userDetails.getUsername());
            employee.setPassword(userDetails.getPassword());
            employee.setRole("ROLE_EMPLOYEE");
            employeeRepository.save(employee);
        } catch (Exception e){
            throw new Exception();
        }

        ResponseDTO responseDTO = new ResponseDTO(true,201,"Employee details successfully added",employeeDTO);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.CREATED);

        return responseEntity;
    }


    /**
     * Method that returns all the employees in the database
     * @return - custom response entity consisting of the entire list of employee data transfer objects
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity findAll() throws Exception {
        List<Employee> employeeList;

        try {
            employeeList = employeeRepository.findAll();
        } catch (Exception e){
            throw new Exception();
        }

        List<EmployeeDTO> employeeDTOList = employeeList.stream()
                .map((employee) -> convertEmployeeToEmployeeDTO(employee))
                .collect(Collectors.toList());

        return new ResponseEntity(employeeDTOList,HttpStatus.OK);
    }

    /**
     * Method to find a specific employee details based on the employee id provided
     * @param id - employee id
     * @return - custom response entity consisting of the employee data transfer object
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity findById(long id) throws Exception {

        validateEmployee(id);

        Optional<Employee> optionalEmployee;

        try {
            optionalEmployee = employeeRepository.findById(id);
        } catch (Exception e){
            throw new Exception();
        }

        if(!optionalEmployee.isPresent())
            throw new EmployeeNotFoundException();



        EmployeeDTO employeeDTO = convertEmployeeToEmployeeDTO(optionalEmployee.get());

        ResponseDTO responseDTO = new ResponseDTO(true,200,"Employee details successfully retrieved",employeeDTO);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);

        return responseEntity;
    }

    /**
     * Method that updates the employee details to the database
     * @param id - employee id
     * @param employeeDTO - employee data transfer object
     * @return - custom response entity consisting of the updated employee data transfer object
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity update(long id,EmployeeDTO employeeDTO) throws Exception {

        if(id!=employeeDTO.getId())
            throw new InconsistentDetailsException();

        validateEmployee(id);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        try {
            Employee employee = convertEmployeeDTOToEmployee(employeeDTO);
            employee.setEmail(userDetails.getUsername());
            employee.setPassword(userDetails.getPassword());
            employee.setRole("ROLE_EMPLOYEE");
            employeeRepository.save(employee);
        } catch (Exception e){
            throw new Exception();
        }

        ResponseDTO responseDTO = new ResponseDTO(true,200,"Employee details successfully updated",employeeDTO);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);

        return responseEntity;
    }

    /**
     * Method that deletes employee details based on the id provided
     * @param id - employee id
     * @return - custom response entity consisting of the status of operation
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity deleteById(long id) throws Exception {

        validateEmployee(id);

        Optional<Employee> optionalEmployee;

        try {
            if(!employeeRepository.existsById(id))
                throw new EmployeeNotFoundException();
            else {
                employeeRepository.deleteById(id);
                ResponseDTO responseDTO = new ResponseDTO(true,200,"Employee details successfully removed",null);
                ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.OK);
                return responseEntity;
            }
        } catch (Exception e){
            throw new Exception();
        }

    }


    /**
     * Method used to register an employee based on email and password
     * @param userDTO - user data tranfer object
     * @return - custom response entity that returns the employee id
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    public ResponseEntity createAccount(UserDTO userDTO) throws Exception {

        validateEmail(userDTO.getEmail());

        Optional<Employee> optionalEmployee;
        long id;

        try {
            optionalEmployee = employeeRepository.findByEmail(userDTO.getEmail());
        } catch (Exception e){
            throw new Exception();
        }

        if(optionalEmployee.isPresent())
            throw new EmailAlreadyRegisteredException();


        try {
            Employee employee = convertUserDTOToEmployee(userDTO);
            employeeRepository.save(employee);
            id=employeeRepository.findByEmail(userDTO.getEmail()).get().getId();
        } catch (Exception e){
            throw new Exception();
        }

        ResponseDTO responseDTO = new ResponseDTO(true,201,"Employee account successfully created",new Long(id));
        ResponseEntity responseEntity = new ResponseEntity(responseDTO,HttpStatus.CREATED);

        return responseEntity;
    }


}
