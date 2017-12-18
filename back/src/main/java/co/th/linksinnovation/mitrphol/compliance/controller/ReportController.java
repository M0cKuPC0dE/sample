/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.model.JsonViewer;
import co.th.linksinnovation.mitrphol.compliance.model.LegalCategory;
import co.th.linksinnovation.mitrphol.compliance.model.LegalGroup;
import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalcategoryRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalgroupRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.UserDetailsRepository;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jirawong
 */
@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private LegalcategoryRepository legalcategoryRepository;
    @Autowired
    private LegalgroupRepository legalgroupRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @GetMapping("/checklist/{authority}")
    @JsonView(JsonViewer.LegalDutyWithCompliance.class)
    public List<LegalGroup> checklist(@PathVariable("authority") String authority, @AuthenticationPrincipal String username) {
        UserDetails findOne = userDetailsRepository.findOne(username);
        if (authority.equals("Coordinator")) {
            List<LegalGroup> findAll = legalgroupRepository.findByCoordinatesIn(findOne);
            for (LegalGroup legalGroup : findAll) {
                List<LegalCategory> findByLegalGroup = legalcategoryRepository.findByLegalGroup(legalGroup);
                legalGroup.getLegalCategories().addAll(findByLegalGroup);
            }
            return findAll;
        } else {
            List<LegalGroup> findAll = new ArrayList<>();
            LegalGroup legalGroup = new LegalGroup();
            List<LegalCategory> findByOwnersIn = legalcategoryRepository.findByOwnersIn(findOne);
            legalGroup.setLegalCategories(findByOwnersIn);
            findAll.add(legalGroup);
            return findAll;
        }
    }

}
