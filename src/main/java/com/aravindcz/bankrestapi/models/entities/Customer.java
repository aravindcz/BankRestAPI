package com.aravindcz.bankrestapi.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Customer implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private long accountNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "branch_id")
    private Branch branch;
    private String accountType;
    private long contactNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="card_id")
    private Card card;
    private long panCardNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "offering_id",referencedColumnName = "id")
    private Offering offering;

    private String email;

    private String password;

    private String role;


}
