package com.aravindcz.bankrestapi.repositories.interfaces;

import com.aravindcz.bankrestapi.models.entities.Locker;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LockerRepository extends JpaRepository<Locker,Long> {
    public Optional<Locker> findByNumber(long number);
    @Transactional
    public void deleteByNumber(long number);

    public boolean existsByNumber(long number);

    public List<Locker> findByOffering_Id(long offeringId);
}
