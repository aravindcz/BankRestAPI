package com.aravindcz.bankrestapi.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BranchDTO implements Serializable {
    @NotEmpty
    private String name;

    @Positive
    private long code;
    @NotEmpty
    private String ifsc;
}
