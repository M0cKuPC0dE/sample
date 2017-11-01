/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.model.Accord;
import co.th.linksinnovation.mitrphol.compliance.model.Authority;
import co.th.linksinnovation.mitrphol.compliance.model.Compliance;
import co.th.linksinnovation.mitrphol.compliance.model.JsonViewer;
import co.th.linksinnovation.mitrphol.compliance.model.LegalCategory;
import co.th.linksinnovation.mitrphol.compliance.model.LegalGroup;
import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import co.th.linksinnovation.mitrphol.compliance.model.authen.Authenticate;
import co.th.linksinnovation.mitrphol.compliance.repository.AccordRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalcategoryRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalgroupRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.UserDetailsRepository;
import co.th.linksinnovation.mitrphol.compliance.service.MailService;
import co.th.linksinnovation.mitrphol.compliance.service.RestService;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api/legalgroup")
public class LegalgroupController {

    @Autowired
    private LegalgroupRepository legalgroupRepository;
    @Autowired
    private LegalcategoryRepository legalcategoryRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private AccordRepository accordRepository;
    @Autowired
    private RestService restService;
    @Autowired
    private MailService mailService;

    @GetMapping
    @JsonView(JsonViewer.ComplianceWithCategory.class)
    public List<LegalGroup> get(@AuthenticationPrincipal String username) {
        UserDetails findOne = userDetailsRepository.findOne(username);
        if (findOne.getAuthorities().contains(new Authority("Administrator"))) {
            List<LegalGroup> findAll = legalgroupRepository.findAll();
            for (LegalGroup legalGroup : findAll) {
                List<LegalCategory> findByLegalGroup = legalcategoryRepository.findByLegalGroup(legalGroup);
                legalGroup.getLegalCategories().addAll(findByLegalGroup);
            }
            return findAll;
        } else if (findOne.getAuthorities().contains(new Authority("Coordinator"))) {
            List<LegalGroup> findAll = legalgroupRepository.findByCoordinatesIn(findOne);
            for (LegalGroup legalGroup : findAll) {
                List<LegalCategory> findByLegalGroup = legalcategoryRepository.findByLegalGroup(legalGroup);
                legalGroup.getLegalCategories().addAll(findByLegalGroup);
            }
            return findAll;
        } else {
            return null;
        }
    }

    @GetMapping("/{id}")
    @JsonView(JsonViewer.ComplianceWithCategory.class)
    public LegalGroup get(@PathVariable("id") Long id) {
        return legalgroupRepository.findOne(id);
    }

    @PostMapping
    @JsonView(JsonViewer.ComplianceWithCategory.class)
    public LegalGroup post(@RequestBody LegalGroup legalGroup,@AuthenticationPrincipal String username) {
        List<UserDetails> fillDetails = new ArrayList<>();
        if (!legalGroup.getCoordinates().isEmpty()) {
            for (UserDetails u : legalGroup.getCoordinates()) {
                List<UserDetails> userdetails = userDetailsRepository.findByUserId(u.getUserId());
                if (userdetails.isEmpty()) {
                    Authenticate findUserInfo = restService.findUserInfo(u.getUserId());
                    if (findUserInfo.getSuccess().getCode() == 200) {
                        UserDetails ud = new UserDetails();
                        ud.setUsername(findUserInfo.getSuccess().getData().getUserInfo().getUsername());
                        ud.setUserId(findUserInfo.getSuccess().getData().getUserInfo().getId());
                        ud.setNameEn(findUserInfo.getSuccess().getData().getUserInfo().getFullname().getEn());
                        ud.setNameTh(findUserInfo.getSuccess().getData().getUserInfo().getFullname().getTh());
                        ud.setEmail(findUserInfo.getSuccess().getData().getUserInfo().getEmail());
                        ud.setPhoto(findUserInfo.getSuccess().getData().getUserInfo().getPhoto());
                        Authority authority = new Authority();
                        authority.setAuthority("Coordinator");
                        ud.addAuthority(authority);
                        UserDetails save = userDetailsRepository.save(ud);
                        fillDetails.add(save);
                    }
                } else {
                    UserDetails get = userdetails.get(0);
                    Authority authority = new Authority();
                    authority.setAuthority("Coordinator");
                    if (!get.getAuthorities().contains(authority)) {
                        get.addAuthority(authority);
                        get = userDetailsRepository.save(get);
                    }
                    fillDetails.add(get);
                }
            }
        }
        legalGroup.setCoordinates(fillDetails);
        if (!legalGroup.getLegalDuties().isEmpty()) {
            mailService.send2Coordinator(legalGroup,username);
        }
        return legalgroupRepository.save(legalGroup);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LegalGroup findOne = legalgroupRepository.findOne(id);
        List<LegalCategory> findByLegalGroup = legalcategoryRepository.findByLegalGroup(findOne);
        for(LegalCategory le : findByLegalGroup){
            List<Accord> acs = accordRepository.findByLegalCategory(le);
            for(Accord ac : acs){
                accordRepository.delete(ac);
            }
            legalcategoryRepository.delete(le);
        }
        legalgroupRepository.delete(id);
    }
}
