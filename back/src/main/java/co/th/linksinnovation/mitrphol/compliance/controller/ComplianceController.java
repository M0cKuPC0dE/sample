/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.model.Category;
import co.th.linksinnovation.mitrphol.compliance.model.Compliance;
import co.th.linksinnovation.mitrphol.compliance.model.JsonViewer;
import co.th.linksinnovation.mitrphol.compliance.repository.CategoryRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.ComplianceRepository;
import com.fasterxml.jackson.annotation.JsonView;
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
    private CategoryRepository categoryRepository;

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
        return complianceRepository.save(compliance);
    }

    @PostMapping("/search")
    @JsonView(JsonViewer.ComplianceWithCategory.class)
    public List<Compliance> postSearch(@RequestBody Map<String, String> map) {
        if (!map.get("category").equals("null")) {
            Category findOne = categoryRepository.findOne(Long.parseLong(map.get("category")));
            return complianceRepository.searchWithCategory("%"+map.get("search")+"%","%"+ map.get("search")+"%", findOne);
        } else {
            return complianceRepository.searchWithoutCategory("%"+map.get("search")+"%", "%"+map.get("search")+"%");
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        Compliance findOne = complianceRepository.findOne(id);
        findOne.setDeleted(Boolean.TRUE);
        complianceRepository.save(findOne);
    }
}
