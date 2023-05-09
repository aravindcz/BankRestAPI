package com.aravindcz.bankrestapi.repositories.interfaces;

import com.aravindcz.bankrestapi.models.entities.Offering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfferingRepository extends JpaRepository<Offering,Long> {

   public Optional<Offering> findByCustomer_Id(long id);
}
