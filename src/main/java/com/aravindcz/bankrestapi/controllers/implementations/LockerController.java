package com.aravindcz.bankrestapi.controllers.implementations;

import com.aravindcz.bankrestapi.models.dtos.LockerDTO;
import com.aravindcz.bankrestapi.services.implementations.LockerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 *
 * LockerController - provides methods for handling all the crud operations on the /api/v1/customers/{customerId}/offerings/lockers endpoint,
 * managing the authorization of the enpoints as well and all the methods return response entity following the uniform response
 * format
 * @author Aravind C
 */
@RestController
@RequestMapping("/api/v1/customers/{customerId}/offerings/lockers")
@AllArgsConstructor
public class LockerController {
    @Autowired
    private LockerService lockerService;



    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping(value = "",consumes = "application/json",produces = "application/json")
    public ResponseEntity save(@PathVariable long customerId,@Valid  @RequestBody LockerDTO lockerDTO) throws Exception {

        ResponseEntity responseEntity = lockerService.save(customerId,lockerDTO);

        return responseEntity;
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @GetMapping(value = "",produces = "application/json")
    public ResponseEntity findAll(@PathVariable long customerId) throws Exception {

        ResponseEntity responseEntity = lockerService.findAll(customerId);

        return responseEntity;
    }


    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @GetMapping(value = "/{number}",produces = "application/json")
    public ResponseEntity findByNumber(@PathVariable long customerId,@PathVariable long number) throws Exception {

        ResponseEntity responseEntity = lockerService.findByNumber(customerId,number);

        return responseEntity;
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PutMapping(value = "/{number}",consumes = "application/json",produces = "application/json")
    public ResponseEntity update(@PathVariable long customerId,@PathVariable long number,@Valid @RequestBody LockerDTO lockerDTO) throws Exception {

        ResponseEntity responseEntity = lockerService.update(customerId,number,lockerDTO);


        return responseEntity;
    }




    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @DeleteMapping(value = "/{number}",produces = "application/json")
    public ResponseEntity deleteByNumber(@PathVariable long customerId,@PathVariable long number) throws Exception {

        ResponseEntity responseEntity = lockerService.deleteByNumber(customerId,number);

        return responseEntity;
    }




}
