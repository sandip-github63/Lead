package com.lead.service;


import java.util.List;

import com.lead.exception.LeadAlreadyExistsException;
import com.lead.exception.LeadNotFoundException;
import com.lead.model.Lead;

public interface LeadService {
	String createLead(Lead lead) throws LeadAlreadyExistsException;
	
	List<Lead> getLeadsByMobileNumber(String mobileNumber) throws LeadNotFoundException;
	
}
