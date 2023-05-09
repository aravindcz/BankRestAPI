package com.aravindcz.bankrestapi.models.dtos;

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
public class LoanDTO implements Serializable {


    @Positive
    private long number;
    @Positive
    private long customerId;
    @Positive
    private long amount;
}
