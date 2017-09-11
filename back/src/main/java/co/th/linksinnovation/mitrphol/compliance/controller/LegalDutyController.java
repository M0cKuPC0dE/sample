/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.model.JsonViewer;
import co.th.linksinnovation.mitrphol.compliance.model.LegalDuty;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalDutyRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jirawong
 */
@RestController
@RequestMapping("/api/legalduty")
public class LegalDutyController {
    
    @Autowired
    private LegalDutyRepository legalDutyRepository;
    
    @GetMapping("/{id}")
    @JsonView(JsonViewer.LegalDutyWithCompliance.class)
    public LegalDuty get(@PathVariable("id") Long id){
        return legalDutyRepository.findOne(id);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        LegalDuty findOne = legalDutyRepository.findOne(id);
        findOne.setDeleted(Boolean.TRUE);
        legalDutyRepository.save(findOne);
    }
    
    @PostMapping
    public void post(@RequestBody LegalDuty legalDuty){
        legalDutyRepository.save(legalDuty);
    }
}
