package com.aravindcz.bankrestapi.models.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
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
public class EmployeeDTO implements Serializable {

    @Positive
    private long id;
    @NotEmpty
    private String name;
    @Positive
    private int salary;
    @NotEmpty
    private String title;

    @Valid
    private AddressDTO address;

    @PastOrPresent
    private Date joiningDate;


}
