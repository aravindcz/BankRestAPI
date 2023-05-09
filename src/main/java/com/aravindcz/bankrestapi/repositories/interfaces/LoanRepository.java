package com.aravindcz.bankrestapi.repositories.interfaces;

import com.aravindcz.bankrestapi.models.entities.Loan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {

    public Optional<Loan> findByNumber(long number);
    @Transactional
    public void deleteByNumber(long number);

    public boolean existsByNumber(long number);

    public List<Loan> findByOffering_Id(long offeringId);
}
