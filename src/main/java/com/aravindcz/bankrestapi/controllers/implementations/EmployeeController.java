package com.aravindcz.bankrestapi.controllers.implementations;

import com.aravindcz.bankrestapi.models.dtos.EmployeeDTO;
import com.aravindcz.bankrestapi.models.dtos.UserDTO;
import com.aravindcz.bankrestapi.services.implementations.EmployeeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 *
 * EmployeeController - provides methods for handling all the crud operations on the /api/v1/employees endpoint, managing the authoriza
 * tion of the enpoints as well and all the methods return response entity following the uniform response format
 * @author Aravind C
 */
@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;



    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @PostMapping(value = "",consumes = "application/json",produces = "application/json")
    public ResponseEntity save(@Valid @RequestBody EmployeeDTO employeeDTO) throws Exception {

        ResponseEntity responseEntity = employeeService.save(employeeDTO);

        return responseEntity;
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping(value = "",produces = "application/json")
    public ResponseEntity findAll() throws Exception {

        ResponseEntity responseEntity = employeeService.findAll();

        return responseEntity;
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @GetMapping(value = "/{id}",produces = "application/json")
    public ResponseEntity findById(@PathVariable long id) throws Exception {

        ResponseEntity responseEntity = employeeService.findById(id);

        return responseEntity;
    }


    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @PutMapping(value = "/{id}",consumes = "application/json")
    public ResponseEntity update(@PathVariable long id,@Valid @RequestBody EmployeeDTO employeeDTO) throws Exception {

        ResponseEntity responseEntity = employeeService.update(id,employeeDTO);

        return responseEntity;
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @DeleteMapping(value = "/{id}",produces = "application/json")
    public ResponseEntity deleteById(@PathVariable long id) throws Exception {

        ResponseEntity responseEntity = employeeService.deleteById(id);

        return responseEntity;
    }



    @PostMapping(path = "/register",consumes = "application/json",produces = "application/json")
    public ResponseEntity createAccount(@Valid @RequestBody UserDTO userDTO) throws Exception {

        userDTO.setRole("ROLE_EMPLOYEE");

        ResponseEntity responseEntity = employeeService.createAccount(userDTO);

        return responseEntity;
    }



}
