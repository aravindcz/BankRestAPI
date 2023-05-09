package com.aravindcz.bankrestapi.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Offering implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "offering")
    private List<Locker> locker;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "offering")
    private List<Loan> loan;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "offering")
    private Customer customer;

}
