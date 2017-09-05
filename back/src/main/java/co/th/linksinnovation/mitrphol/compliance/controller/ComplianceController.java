/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.model.Category;
import co.th.linksinnovation.mitrphol.compliance.model.Compliance;
import co.th.linksinnovation.mitrphol.compliance.model.JsonViewer;
import co.th.linksinnovation.mitrphol.compliance.model.LegalDuty;
import co.th.linksinnovation.mitrphol.compliance.model.LegalFile;
import co.th.linksinnovation.mitrphol.compliance.model.Status;
import co.th.linksinnovation.mitrphol.compliance.repository.CategoryRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.ComplianceRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalDutyRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalFileRepository;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/api/compliance")
public class ComplianceController {

    @Autowired
    private ComplianceRepository complianceRepository;
    @Autowired
    private LegalDutyRepository legalDutyRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LegalFileRepository legalFileRepository;

    @GetMapping
    @JsonView(JsonViewer.ComplianceWithCategory.class)
    public List<Compliance> get() {
        return complianceRepository.findByDeletedIsFalse();
    }

    @GetMapping("/{id}")
    @JsonView(JsonViewer.ComplianceWithCategory.class)
    public Compliance get(@PathVariable("id") Long id) {
        return complianceRepository.findOne(id);
    }

    @GetMapping("/category/{id}")
    @JsonView(JsonViewer.ComplianceWithCategory.class)
    public List<Compliance> getByCategory(@PathVariable("id") Long id) {
        Category findOne = categoryRepository.findOne(id);
        return complianceRepository.findByDeletedIsFalseAndCategory(findOne);
    }

    @PostMapping
    @JsonView(JsonViewer.ComplianceWithCategory.class)
    public Compliance post(@RequestBody Compliance compliance) {
        Compliance save = complianceRepository.save(compliance);
        for (LegalFile lf : save.getLegalFiles()) {
            lf.setCompliance(null);
        }
        for (LegalDuty ld : save.getLegalDuties()) {
            ld.setCompliance(null);
        }
        for (LegalFile lf : compliance.getLegalFiles()) {
            lf.setCompliance(save);
            legalFileRepository.save(lf);
        }
        for (LegalDuty ld : compliance.getLegalDuties()) {
            ld.setCompliance(save);
            legalDutyRepository.save(ld);
        }

        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, 543);
        Date now = c.getTime();
        
        if (compliance.getEffectiveDate().after(now)) {
            compliance.setStatus(Status.INACTIVE);
        } else {
            compliance.setStatus(Status.ACTIVE);
        }
        return complianceRepository.save(compliance);
    }

    @PostMapping("/search")
    @JsonView(JsonViewer.ComplianceWithCategory.class)
    public List<Compliance> postSearch(@RequestBody Map<String, String> map) {
        if (!map.get("category").equals("null")) {
            Category findOne = categoryRepository.findOne(Long.parseLong(map.get("category")));
            return complianceRepository.searchWithCategory("%" + map.get("search") + "%", "%" + map.get("search") + "%", findOne);
        } else {
            return complianceRepository.searchWithoutCategory("%" + map.get("search") + "%", "%" + map.get("search") + "%");
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        Compliance findOne = complianceRepository.findOne(id);
        findOne.setDeleted(Boolean.TRUE);
        complianceRepository.save(findOne);
    }
}
