package com.aravindcz.bankrestapi.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Loan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long number;

    private long customerId;

    private long amount;

    @ManyToOne
    @JoinColumn(name = "offering_id",referencedColumnName = "id")
    private Offering offering;

}
