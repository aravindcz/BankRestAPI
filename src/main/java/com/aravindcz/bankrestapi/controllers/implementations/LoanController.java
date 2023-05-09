package com.aravindcz.bankrestapi.controllers.implementations;

import com.aravindcz.bankrestapi.models.dtos.LoanDTO;
import com.aravindcz.bankrestapi.services.implementations.LoanService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 *
 * LoanController - provides methods for handling all the crud operations on the /api/v1/customers/{customerId}/offerings/loans endpoint,
 * managing the authorization of the enpoints as well and all the methods return response entity following the uniform response
 * format
 * @author Aravind C
 */
@RestController
@RequestMapping("/api/v1/customers/{customerId}/offerings/loans")
@AllArgsConstructor
public class LoanController {
    @Autowired
    private LoanService loanService;



    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping(value = "",consumes = "application/json",produces = "application/json")
    public ResponseEntity save(@PathVariable long customerId,@Valid @RequestBody LoanDTO loanDTO) throws Exception {

        ResponseEntity responseEntity = loanService.save(customerId,loanDTO);

        return responseEntity;
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @GetMapping(value = "",produces = "application/json")
    public ResponseEntity findAll(@PathVariable long customerId) throws Exception {

        ResponseEntity responseEntity = loanService.findAll(customerId);

        return responseEntity;
    }


    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @GetMapping(value = "/{number}",produces = "application/json")
    public ResponseEntity findByNumber(@PathVariable long customerId,@PathVariable long number) throws Exception {

        ResponseEntity responseEntity = loanService.findByNumber(customerId,number);

        return responseEntity;
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PutMapping(value = "/{number}",consumes = "application/json",produces = "application/json")
    public ResponseEntity update(@PathVariable long customerId,@PathVariable long number,@Valid @RequestBody LoanDTO loanDTO) throws Exception {

        ResponseEntity responseEntity = loanService.update(customerId,number,loanDTO);


        return responseEntity;
    }




    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @DeleteMapping(value = "/{number}",produces = "application/json")
    public ResponseEntity deleteByNumber(@PathVariable long customerId,@PathVariable long number) throws Exception {

        ResponseEntity responseEntity = loanService.deleteByNumber(customerId,number);

        return responseEntity;
    }




}
