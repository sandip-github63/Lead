package com.lead.serviceImpl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lead.exception.LeadAlreadyExistsException;
import com.lead.exception.LeadNotFoundException;
import com.lead.model.Lead;
import com.lead.repository.LeadRepository;
import com.lead.service.LeadService;

@Service
public class LeadServiceImpl implements LeadService {

    @Autowired
    private LeadRepository leadRepository;

    @Override
    public String createLead(Lead lead) throws LeadAlreadyExistsException {
        if (leadRepository.existsByLeadId(lead.getLeadId())) {
            throw new LeadAlreadyExistsException("Lead already exists with ID: " + lead.getLeadId());
        }
        
        leadRepository.save(lead);
        return "Created Lead Successfully";
    }

    @Override
    public List<Lead> getLeadsByMobileNumber(String mobileNumber) throws LeadNotFoundException {
        List<Lead> leads = leadRepository.findByMobileNumber(mobileNumber);
        if (leads.isEmpty()) {
            throw new LeadNotFoundException("No lead found with mobile number: " + mobileNumber);
        }
        return leads;
    }

}
