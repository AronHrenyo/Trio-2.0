package com.wm.repository;

import com.wm.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Optional<Partner> findByPartnerEmail(String partnerEmail);

    List<Partner> findByPartnerNameContaining(String partnerName);
}