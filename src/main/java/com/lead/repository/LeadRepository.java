package com.lead.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lead.model.Lead;

public interface LeadRepository extends JpaRepository<Lead, Long> {

    boolean existsByLeadId(Long leadId);
    
    List<Lead> findByMobileNumber(String mobileNumber);
    
   

}
