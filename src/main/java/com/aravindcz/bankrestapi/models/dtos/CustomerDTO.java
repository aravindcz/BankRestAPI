package com.aravindcz.bankrestapi.models.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO implements Serializable {
    @Positive
    private long id;
    @NotEmpty
    private String name;
    @Positive
    private long accountNumber;

    @Valid
    private BranchDTO branch;
    @NotEmpty
    private String accountType;
    @Positive
    private long contactNumber;

    @Valid
    private CardDTO card;
    @Positive
    private long panCardNumber;

    @Valid
    private AddressDTO address;



}
