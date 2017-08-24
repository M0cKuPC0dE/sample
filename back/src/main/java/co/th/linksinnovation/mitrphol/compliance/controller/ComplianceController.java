/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.model.Compliance;
import co.th.linksinnovation.mitrphol.compliance.repository.ComplianceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/compliance")
public class ComplianceController {

    @Autowired
    private ComplianceRepository complianceRepository;

    @GetMapping
    public List<Compliance> get() {
        return complianceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Compliance get(@PathVariable("id") Long id) {
        return complianceRepository.findOne(id);
    }

    @PostMapping
    public Compliance post(@RequestBody Compliance compliance) {
        return complianceRepository.save(compliance);
    }
}
