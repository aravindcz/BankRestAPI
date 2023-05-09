package com.aravindcz.bankrestapi.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Locker implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long number;

    private long accountNumber;

    private long branchCode;


    @ManyToOne
    @JoinColumn(name = "offering_id",referencedColumnName = "id")
    private Offering offering;

}
