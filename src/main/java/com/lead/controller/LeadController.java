package com.lead.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lead.exception.LeadAlreadyExistsException;
import com.lead.exception.LeadNotFoundException;
import com.lead.model.Lead;
import com.lead.payload.response.ApiResponse;
import com.lead.payload.response.ErrorResponse;
import com.lead.payload.response.LeadResponse;
import com.lead.service.LeadService;

@RestController
@RequestMapping("/api/leads")
@Validated
public class LeadController {

    @Autowired
    private LeadService leadService;

    @PostMapping("/create")
    public ResponseEntity<Object> createLead(@Valid @RequestBody Lead lead, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> body = new HashMap<>();
            body.put("status", "error");
            body.put("message", "Validation error");

            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            fieldError -> fieldError.getField(),
                            fieldError -> fieldError.getDefaultMessage()));

            body.put("errors", errors);

            return ResponseEntity.badRequest().body(body);
        }

        try {
            leadService.createLead(lead);
            return ResponseEntity.ok().body(new ApiResponse("success", "Created Lead Successfully"));
        } catch (LeadAlreadyExistsException e) {
            List<String> errorMessages = List.of("Lead already exists with ID: " + lead.getLeadId());
            ErrorResponse errorResponse = new ErrorResponse("E10010", errorMessages);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error", errorResponse));
        } catch (ConstraintViolationException e) {
            Map<String, Object> body = new HashMap<>();
            body.put("status", "error");
            body.put("message", "Validation error");

            Map<String, String> errors = e.getConstraintViolations().stream()
                    .collect(Collectors.toMap(
                            violation -> violation.getPropertyPath().toString(),
                            violation -> violation.getMessage()));

            body.put("errors", errors);

            return ResponseEntity.badRequest().body(body);
        }
    }
    
    
    @GetMapping("/fetchByMobileNumber/{mobileNumber}")
    public ResponseEntity<Object> fetchLeadByMobileNumber(@PathVariable String mobileNumber) {
        try {
            List<Lead> leads = leadService.getLeadsByMobileNumber(mobileNumber);
            
            List<LeadResponse> leadResponses = leads.stream()
                    .map(lead -> new LeadResponse(lead.getLeadId(), lead.getFirstName(), lead.getMiddleName(),
                            lead.getLastName(), lead.getMobileNumber(), lead.getGender(), lead.getDob(), lead.getEmail()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(new ApiResponse("success", leadResponses));
        } catch (LeadNotFoundException e) {
            // No leads found with the given mobileNumber
            List<String> errorMessages = List.of("No Lead found with the Mobile Number " + mobileNumber);
            ErrorResponse errorResponse = new ErrorResponse("E10011", errorMessages);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", errorResponse));
        }
    }


}

