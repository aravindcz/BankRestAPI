package com.aravindcz.bankrestapi.controllers.implementations;

import com.aravindcz.bankrestapi.models.dtos.OfferingDTO;
import com.aravindcz.bankrestapi.services.implementations.OfferingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 *
 * OfferingController - provides methods for handling creation and retrieval operations on the /api/v1/customers/{customerId}/offerings
 * endpoint, managing the authorization of the enpoints as well and all the methods return response entity following the uniform
 * response format
 * @author Aravind C
 */
@RestController
@RequestMapping("/api/v1/customers/{customerId}/offerings")
@AllArgsConstructor
public class OfferingController {

    @Autowired
    private OfferingService offeringService;


    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PostMapping(value = "",consumes = "application/json",produces = "application/json")
    public ResponseEntity save(@PathVariable long customerId,@Valid @RequestBody OfferingDTO offeringDTO) throws Exception {



        ResponseEntity responseEntity = offeringService.save(customerId,offeringDTO);

        return responseEntity;
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @GetMapping(value = "",produces = "application/json")
    public ResponseEntity findAll(@PathVariable long customerId) throws Exception {

        ResponseEntity responseEntity = offeringService.findAll(customerId);

        return responseEntity;
    }


//    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
//    @PutMapping(value = "",consumes = "application/json",produces = "application/json")
//    public ResponseEntity update(@PathVariable long customerId,@RequestBody OfferingDTO offeringDTO) throws Exception {
//
//        ResponseEntity responseEntity = offeringService.update(customerId,offeringDTO);
//
//        return responseEntity;
//    }



}
