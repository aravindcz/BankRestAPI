package com.aravindcz.bankrestapi.configurations;

import com.aravindcz.bankrestapi.models.dtos.UserDTO;
import com.aravindcz.bankrestapi.models.entities.Customer;
import com.aravindcz.bankrestapi.models.entities.Employee;

import com.aravindcz.bankrestapi.repositories.interfaces.CustomerRepository;
import com.aravindcz.bankrestapi.repositories.interfaces.EmployeeRepository;
import lombok.AllArgsConstructor;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * UserDetailsServiceImplementation - provides custome UserDetailsService implementation to be configured with the basic authentication
 * @author Aravind C
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ModelMapper modelMapper;


    private UserDTO convertEmployeeToUserDTO(Employee employee){

        UserDTO userDTO = modelMapper.map(employee, UserDTO.class);

        return userDTO;

    }

    private UserDTO convertCustomerToUserDTO(Customer customer){

        UserDTO userDTO = modelMapper.map(customer, UserDTO.class);

        return userDTO;

    }

    /**
     * Method that returns the user details object that needs to be compared with user provided credentials for authentication
     * @param username - username provided by the user during authentication
     * @return - user details that need to be compared with user provided credentials
     * @throws UsernameNotFoundException - when the credentials are incorrect
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Optional<Customer> optionalCustomer = customerRepository.findByEmail(username);
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(username);

        if(optionalEmployee.isPresent()){
            return new UserDetailsImplementation(convertEmployeeToUserDTO(optionalEmployee.get()));
        }

        if(optionalCustomer.isPresent()){
            return new UserDetailsImplementation(convertCustomerToUserDTO(optionalCustomer.get()));
        }

        throw new UsernameNotFoundException("No account exists with this username");


    }

}
