package com.aravindcz.bankrestapi.models.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferingDTO implements Serializable {
    @Valid
    @NotNull
    private List<LockerDTO> locker;

    @Valid
    @NotNull
    private List<LoanDTO> loan;


}
