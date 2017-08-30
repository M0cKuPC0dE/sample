/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.model.Authority;
import co.th.linksinnovation.mitrphol.compliance.model.LegalGroup;
import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import co.th.linksinnovation.mitrphol.compliance.model.authen.Authenticate;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalgroupRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.UserDetailsRepository;
import co.th.linksinnovation.mitrphol.compliance.service.RestService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private RestService restService;

    @GetMapping
    public List<LegalGroup> get() {
        return legalgroupRepository.findAll();
    }

    @GetMapping("/{id}")
    public LegalGroup get(@PathVariable("id") Long id) {
        return legalgroupRepository.findOne(id);
    }

    @PostMapping
    public LegalGroup post(@RequestBody LegalGroup legalGroup) {
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
                }
            }
        }
        legalGroup.setCoordinates(fillDetails);
        return legalgroupRepository.save(legalGroup);
    }
}
