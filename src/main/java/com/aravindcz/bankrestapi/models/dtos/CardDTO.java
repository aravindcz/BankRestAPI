package com.aravindcz.bankrestapi.models.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO implements Serializable {

    @Positive
    private long cardNumber;
    @Positive
    private long creditLimit;
    @Temporal(TemporalType.DATE)
    @Future
    private Date expiryDate;
}
