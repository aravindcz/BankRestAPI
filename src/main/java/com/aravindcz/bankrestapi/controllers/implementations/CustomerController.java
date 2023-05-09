package com.aravindcz.bankrestapi.controllers.implementations;

import com.aravindcz.bankrestapi.models.dtos.CustomerDTO;
import com.aravindcz.bankrestapi.models.dtos.UserDTO;
import com.aravindcz.bankrestapi.services.implementations.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 *
 * CustomerController - provides methods for handling all the crud operations on the /api/v1/customers endpoint, managing the authoriza
 * tion of the enpoints as well and all the methods return response entity following the uniform response format
 * @author Aravind C
 */

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;



    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER','ROLE_EMPLOYEE')")
    @PostMapping(value = "",consumes = "application/json",produces = "application/json")
    public ResponseEntity save(@Valid @RequestBody CustomerDTO customerDTO) throws Exception {


        ResponseEntity responseEntity = customerService.save(customerDTO);

        return responseEntity;
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @GetMapping(value = "",produces = "application/json")
    public ResponseEntity findAll() throws Exception {

        ResponseEntity responseEntity = customerService.findAll();

        return responseEntity;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER','ROLE_EMPLOYEE')")
    @GetMapping(value = "/{id}",produces = "application/json")
    public ResponseEntity findById(@PathVariable long id) throws Exception {

        ResponseEntity responseEntity = customerService.findById(id);

        return responseEntity;
    }


    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER','ROLE_EMPLOYEE')")
    @PutMapping(value = "/{id}",consumes = "application/json",produces = "application/json")
    public ResponseEntity update(@PathVariable long id,@Valid @RequestBody CustomerDTO customerDTO) throws Exception {
        ResponseEntity responseEntity = customerService.update(id,customerDTO);

        return responseEntity;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER','ROLE_EMPLOYEE')")
    @DeleteMapping(value = "/{id}",produces = "application/json")
    public ResponseEntity deleteById(@PathVariable long id) throws Exception {

        ResponseEntity responseEntity = customerService.deleteById(id);

        return responseEntity;
    }


    @PostMapping(path = "/register",consumes = "application/json",produces = "application/json")
    public ResponseEntity createAccount(@Valid @RequestBody UserDTO userDTO) throws Exception {

        userDTO.setRole("ROLE_CUSTOMER");


        ResponseEntity responseEntity = customerService.createAccount(userDTO);

        return responseEntity;
    }


}


