/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.model.Accord;
import co.th.linksinnovation.mitrphol.compliance.model.Authority;
import co.th.linksinnovation.mitrphol.compliance.model.JsonViewer;
import co.th.linksinnovation.mitrphol.compliance.model.LegalCategory;
import co.th.linksinnovation.mitrphol.compliance.model.LegalDuty;
import co.th.linksinnovation.mitrphol.compliance.model.LegalGroup;
import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import co.th.linksinnovation.mitrphol.compliance.model.authen.Authenticate;
import co.th.linksinnovation.mitrphol.compliance.repository.AccordRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalDutyRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalcategoryRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalgroupRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.UserDetailsRepository;
import co.th.linksinnovation.mitrphol.compliance.service.MailService;
import co.th.linksinnovation.mitrphol.compliance.service.RestService;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
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
@RequestMapping("/api/legalcategory")
public class LegalcategoryController {

    @Autowired
    private LegalcategoryRepository legalcategoryRepository;
    @Autowired
    private LegalgroupRepository legalgroupRepository;
    @Autowired
    private LegalDutyRepository legalDutyRepository;
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
    public List<LegalCategory> get(@AuthenticationPrincipal String username) {
        UserDetails findOne = userDetailsRepository.findOne(username);
        if (findOne.getAuthorities().contains(new Authority("Administrator"))) {
            return legalcategoryRepository.findAll();
        } else if (findOne.getAuthorities().contains(new Authority("Coordinator"))) {
            List<LegalGroup> findByCoordinatesIn = legalgroupRepository.findByCoordinatesIn(findOne);
            List<LegalCategory> legalCategories = new ArrayList<>();
            for (LegalGroup g : findByCoordinatesIn) {
                List<LegalCategory> findByLegalGroup = legalcategoryRepository.findByLegalGroup(g);
                legalCategories.addAll(findByLegalGroup);
            }
            return legalCategories;

        } else if (findOne.getAuthorities().contains(new Authority("Owner"))) {
            return legalcategoryRepository.findByOwnersIn(findOne);
        } else {
            return null;
        }
    }

    @GetMapping("/legalgroup/{id}")
    @JsonView(JsonViewer.ComplianceWithCategory.class)
    public List<LegalCategory> getByGroup(@PathVariable("id") Long id) {
        LegalGroup findOne = legalgroupRepository.findOne(id);
        return legalcategoryRepository.findByLegalGroup(findOne);
    }

    @GetMapping("/list")
    @JsonView(JsonViewer.LegalDutyWithCompliance.class)
    public List<LegalCategory> getList(@AuthenticationPrincipal String username,@CookieValue(value = "AU") String au) {
        UserDetails findOne = userDetailsRepository.findOne(username);
        if (au.equals("Administrator")) {
            return legalcategoryRepository.findAll();
        } else if (au.equals("Coordinator")) {
            List<LegalGroup> findByCoordinatesIn = legalgroupRepository.findByCoordinatesIn(findOne);
            List<LegalCategory> legalCategories = new ArrayList<>();
            for (LegalGroup g : findByCoordinatesIn) {
                List<LegalCategory> findByLegalGroup = legalcategoryRepository.findByLegalGroup(g);
                legalCategories.addAll(findByLegalGroup);
            }
            return legalCategories;

        } else if (au.equals("Owner")) {
            return legalcategoryRepository.findByOwnersIn(findOne);
        } else if (au.equals("Approver")) {
            return legalcategoryRepository.findByApproversIn(findOne);
        } else {
            return null;
        }
    }

    @GetMapping("/{id}")
    @JsonView(JsonViewer.ComplianceWithCategory.class)
    public LegalCategory get(@PathVariable("id") Long id) {
        return legalcategoryRepository.findOne(id);
    }

    @GetMapping("/view/{id}")
    @JsonView(JsonViewer.LegalDutyWithCompliance.class)
    public LegalCategory getView(@PathVariable("id") Long id) {
        return legalcategoryRepository.findOne(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        legalcategoryRepository.delete(id);
    }

    @PostMapping
    @JsonView(JsonViewer.ComplianceWithCategory.class)
    public LegalCategory post(@RequestBody LegalCategory legalCategory, @AuthenticationPrincipal String username) {
        Set<UserDetails> owners = new HashSet<>();
        Set<UserDetails> approvers = new HashSet<>();

        if (!legalCategory.getOwners().isEmpty()) {
            for (UserDetails u : legalCategory.getOwners()) {
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
                        ud.setUuid(UUID.randomUUID().toString());
                        Authority authority = new Authority();
                        authority.setAuthority("Owner");
                        ud.addAuthority(authority);
                        UserDetails save = userDetailsRepository.save(ud);
                        owners.add(save);
                    }
                } else {
                    UserDetails get = userdetails.get(0);
                    Authority authority = new Authority();
                    authority.setAuthority("Owner");
                    if (!get.getAuthorities().contains(authority)) {
                        get.addAuthority(authority);
                        get = userDetailsRepository.save(get);
                    }
                    owners.add(get);
                }
            }
        }

        if (!legalCategory.getApprovers().isEmpty()) {
            for (UserDetails u : legalCategory.getApprovers()) {
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
                        ud.setUuid(UUID.randomUUID().toString());
                        Authority authority = new Authority();
                        authority.setAuthority("Approver");
                        ud.addAuthority(authority);
                        UserDetails save = userDetailsRepository.save(ud);
                        approvers.add(save);
                    }
                } else {
                    UserDetails get = userdetails.get(0);
                    Authority authority = new Authority();
                    authority.setAuthority("Approver");
                    if (!get.getAuthorities().contains(authority)) {
                        get.addAuthority(authority);
                        get = userDetailsRepository.save(get);
                    }
                    approvers.add(get);
                }
            }
        }

        legalCategory.setOwners(owners);
        legalCategory.setApprovers(approvers);

        if (legalCategory.getId() != null) {
            LegalCategory findOne = legalcategoryRepository.findOne(legalCategory.getId());
            List<LegalDuty> legalDuties = findOne.getLegalDuties();
            legalDuties.removeAll(legalCategory.getLegalDuties());
            for (LegalDuty l : legalDuties) {
                Accord ac = accordRepository.findByLegalCategoryAndLegalDuty(legalCategory, l);
                if (ac != null) {
                    System.out.println("-----> " + ac.getId());
                    legalCategory.getAccords().removeIf(a -> a.getId().equals(ac.getId()));
                    System.out.println(legalCategory.getAccords().size());
                    accordRepository.delete(ac);
                }
            }
            System.out.println("in --------------> " + legalCategory.getAccords().size());
        }
        System.out.println("--------------> " + legalCategory.getAccords().size());
        legalcategoryRepository.save(legalCategory);

        if (!legalCategory.getLegalDuties().isEmpty()) {
            List<Accord> accords = new ArrayList<>();
            for (LegalDuty ld : legalCategory.getLegalDuties()) {
                Accord ac = accordRepository.findByLegalCategoryAndLegalDuty(legalCategory, ld);
                if (ac == null) {
                    ac = new Accord();
                    ac.setLegalCategory(legalCategory);
                    ac.setLegalDuty(ld);
                    ac = accordRepository.save(ac);
                    accords.add(ac);
                }
            }
            legalCategory.getAccords().clear();
            legalCategory.setAccords(accords);
        }

        mailService.send2Owner(legalCategory, username);

        return legalcategoryRepository.save(legalCategory);
    }
}
