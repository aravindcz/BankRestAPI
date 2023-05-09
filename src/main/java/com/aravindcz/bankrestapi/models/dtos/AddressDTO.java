package com.aravindcz.bankrestapi.models.dtos;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO implements Serializable {

    @NotEmpty
    private String street;
    @NotEmpty
    private String state;
    @NotEmpty
    private String city;
    @NotEmpty
    private String pin;

}
