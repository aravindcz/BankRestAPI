package com.aravindcz.bankrestapi.repositories.interfaces;

import com.aravindcz.bankrestapi.models.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    public Optional<Customer> findByAccountNumber(long accountNumber);

    public Optional<Customer> findByEmail(String email);
}
