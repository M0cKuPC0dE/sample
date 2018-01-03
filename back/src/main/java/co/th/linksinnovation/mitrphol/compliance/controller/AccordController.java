/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.model.Accord;
import co.th.linksinnovation.mitrphol.compliance.model.AccordRemark;
import co.th.linksinnovation.mitrphol.compliance.model.Accorded;
import co.th.linksinnovation.mitrphol.compliance.model.EvidenceFile;
import co.th.linksinnovation.mitrphol.compliance.model.JsonViewer;
import co.th.linksinnovation.mitrphol.compliance.model.LegalCategory;
import co.th.linksinnovation.mitrphol.compliance.model.LegalDuty;
import co.th.linksinnovation.mitrphol.compliance.model.LicenseFile;
import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import co.th.linksinnovation.mitrphol.compliance.repository.AccordRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.EvidenceFileRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalDutyRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalcategoryRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LicenseFileRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.UserDetailsRepository;
import co.th.linksinnovation.mitrphol.compliance.service.MailService;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api/accord")
public class AccordController {

    @Autowired
    private AccordRepository accordRepository;
    @Autowired
    private LegalcategoryRepository legalcategoryRepository;
    @Autowired
    private LegalDutyRepository legalDutyRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private LicenseFileRepository licenseFileRepository;
    @Autowired
    private EvidenceFileRepository evidenceFileRepository;
    @Autowired
    private MailService mailService;

    @GetMapping("/{legalCategory}/{compliance}")
    @JsonView(JsonViewer.LegalDutyWithCompliance.class)
    public Accord get(@PathVariable("legalCategory") Long legalCategory, @PathVariable("compliance") Long compliance) {
        LegalCategory lc = legalcategoryRepository.findOne(legalCategory);
        LegalDuty c = legalDutyRepository.findOne(compliance);
        return accordRepository.findByLegalCategoryAndLegalDuty(lc, c);
    }

    @PostMapping
    @JsonView(JsonViewer.LegalDutyWithCompliance.class)
    public Accord post(@RequestBody Accord accord, @AuthenticationPrincipal String username) {
        UserDetails findOne = userDetailsRepository.findOne(username);
        accord.addRemark(new AccordRemark(accord.getRemark(), findOne, accord.getAccorded().toString()));
        accord.setRemark(null);
        accord.setApprove(null);
        Accord ac = accordRepository.save(accord);
        LegalCategory legalCategory = ac.getLegalCategory();
        legalcategoryRepository.save(legalCategory);
//        mailService.compliance(accord, username);
        return ac;
    }

    @PostMapping("/accept/{id}")
    public void accept(@PathVariable("id") Long id, @RequestBody Accord accord, @AuthenticationPrincipal String username) {
        Accord findOne = accordRepository.findOne(id);
        UserDetails ud = userDetailsRepository.findOne(username);
        findOne.addRemark(new AccordRemark(accord.getRemark(), ud, "ACCEPT"));
        findOne.setRemark(null);
        findOne.setApprove(null);
        findOne.setAccept(Boolean.TRUE);
        mailService.acceptNotification(findOne, username);
        accordRepository.save(findOne);
    }

    @PostMapping("/notaccept/{id}")
    public void notAccept(@PathVariable("id") Long id, @RequestBody Accord accord, @AuthenticationPrincipal String username) {
        Accord findOne = accordRepository.findOne(id);
        UserDetails ud = userDetailsRepository.findOne(username);
        findOne.addRemark(new AccordRemark(accord.getRemark(), ud, "NOT_ACCEPT"));
        findOne.setRemark(null);
        findOne.setApprove(null);
        findOne.setAccept(Boolean.FALSE);
        accordRepository.save(findOne);
    }

    @PostMapping("/approve/{id}")
    public void approve(@PathVariable("id") Long id, @RequestBody Accord accord, @AuthenticationPrincipal String username) {
        Accord findOne = accordRepository.findOne(id);
        UserDetails ud = userDetailsRepository.findOne(username);
        findOne.addRemark(new AccordRemark(accord.getRemark(), ud, "APPROVE"));
        findOne.setRemark(null);
        if (findOne.getAccorded().equals(Accorded.NOT_ACCORDED)) {
            findOne.setAccept(Boolean.FALSE);
            findOne.setApprove(null);
        } else {
            findOne.setApprove(Boolean.TRUE);
        }
        mailService.approveNotification(accord, username);
        accordRepository.save(findOne);
    }

    @PostMapping("/reject/{id}")
    public void reject(@PathVariable("id") Long id, @RequestBody Accord accord, @AuthenticationPrincipal String username) {
        Accord findOne = accordRepository.findOne(id);
        UserDetails ud = userDetailsRepository.findOne(username);
        findOne.addRemark(new AccordRemark(accord.getRemark(), ud, "NOT_APPROVE"));
        findOne.setRemark(null);
        findOne.setAccept(null);
        findOne.setAccorded(null);
        findOne.setApprove(Boolean.FALSE);
        mailService.approveNotification(accord, username);
        accordRepository.save(findOne);
    }

    @GetMapping("/approveall/{id}")
    public void approveAll(@PathVariable("id") Long id) {
        LegalCategory findOne = legalcategoryRepository.findOne(id);
        for (Accord accord : findOne.getAccords()) {
            if (accord.getAccept() != null && accord.getAccept() == true) {
                accord.setApprove(Boolean.TRUE);
                accordRepository.save(accord);
            }
        }
    }

    @GetMapping("/rejectall/{id}")
    public void rejectAll(@PathVariable("id") Long id) {
        LegalCategory findOne = legalcategoryRepository.findOne(id);
        for (Accord accord : findOne.getAccords()) {
            if (accord.getAccept() != null && accord.getAccept() == true) {
                accord.setAccept(null);
                accord.setApprove(Boolean.FALSE);
                accordRepository.save(accord);
            }
        }
    }

    @GetMapping("/reset/{id}")
    public void reset(@PathVariable("id") Long id) {
        accordRepository.reset(id);
    }

    @GetMapping("/resetall")
    public void resetAllLast() {
        List<Accord> findAll = accordRepository.findAll();
        for (Accord accord : findAll) {
            boolean flag = false;
            for (LicenseFile lf : accord.getLicenseFiles()) {
                if (lf != null && lf.getExpireDate() != null && (lf.getExpireDate().before(new Date()) || lf.getExpireDate().equals(new Date()))) {
                    flag = true;
                    break;
                }
            }
            for (EvidenceFile ef : accord.getEvidenceFiles()) {
                if (ef != null && ef.getExpireDate() != null &&  (ef.getExpireDate().before(new Date()) || ef.getExpireDate().equals(new Date()))) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                accord.setAccorded(null);
                accord.setAccept(null);
                accord.setApprove(null);
                accordRepository.save(accord);
            }
        }
    }
}
