/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.model.Authority;
import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import co.th.linksinnovation.mitrphol.compliance.model.authen.Authenticate;
import co.th.linksinnovation.mitrphol.compliance.repository.UserDetailsRepository;
import co.th.linksinnovation.mitrphol.compliance.service.RestService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jirawong
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private RestService restService;

    @GetMapping("/administrator")
    public List<UserDetails> getUserAdmin() {
        List<String> authorities = new ArrayList<>();
        authorities.add("Administrator");
        return userDetailsRepository.findUserByRole(authorities);
    }

    @PostMapping("/administrator")
    public void addAdmin(@RequestBody UserDetails u) {
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
                authority.setAuthority("Administrator");
                ud.addAuthority(authority);
                UserDetails save = userDetailsRepository.save(ud);
            }
        } else {
            UserDetails get = userdetails.get(0);
            Authority authority = new Authority();
            authority.setAuthority("Administrator");
            get.addAuthority(authority);
            UserDetails save = userDetailsRepository.save(get);
        }
    }

    @PostMapping("/administrator/delete")
    public void removeAdmin(@RequestBody UserDetails u) {
        List<UserDetails> userdetails = userDetailsRepository.findByUserId(u.getUserId());
        UserDetails get = userdetails.get(0);
        get.removeAuthority(new Authority("Administrator"));
        userDetailsRepository.save(get);
    }

}
