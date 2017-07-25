/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.config;

import co.th.linksinnovation.mitrphol.compliance.model.Authority;
import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import co.th.linksinnovation.mitrphol.compliance.model.authen.Authenticate;
import co.th.linksinnovation.mitrphol.compliance.model.authen.UserInfo;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Piyawut Chiradejnunt<pchiradejnunt@gmail.com>
 */
@Profile("prod")
@Component
public class CustomAuthenticationProvider extends AbstractCustomAuthenticationProvider {

    private static final String APIKEY = "App-Key";
    private static final String APIKEY_VALUE = "blySsonddG3ZDLU1d2V9hCEUyKt09iDdCKrndyFxAewAY4ny6xU4OOprDoJl5IWa.IqK3VvSVoQ6O4vt3-0kxA";

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private static final String LOGIN_URL = "https://api.mitrphol.com:3001/authentication/login";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return validateAuthen(authentication);
    }

    @Override
    protected boolean isServiceAuthen(Authentication authentication) {
        ResponseEntity<Authenticate> responseEntity = null;

        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(APIKEY, APIKEY_VALUE);
        
        final String userName = authentication.getName().trim().toLowerCase();
        
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(USERNAME, userName);
        map.add(PASSWORD, authentication.getCredentials().toString());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        responseEntity = rest.exchange(LOGIN_URL, HttpMethod.POST, request, Authenticate.class);

        if (responseEntity.getBody().getSuccess() != null) {
            UserDetails userDetails = userDetailsRepository.findOne(userName);
            if (userDetails == null) {
                userDetails = new UserDetails();
                userDetails.setUsername(userName);
                userDetails.setPassword(authentication.getCredentials().toString());
                
                final UserInfo userInfo = responseEntity.getBody().getSuccess().getData().getUserInfo();
                userDetails.setNameEn(userInfo.getFullname().getEn());
                userDetails.setNameTh(userInfo.getFullname().getTh());
                userDetails.setUserId(userInfo.getId());
                userDetails.setPhoto(userInfo.getPhoto());
                userDetails.setCitizenId(userInfo.getCitizenId());
                userDetails.setEmail(userInfo.getEmail());
                
                Authority authority = new Authority();
                authority.setAuthority("User");
                userDetails.addAuthority(authority);
                
            }
            userDetailsRepository.save(userDetails);
            return true;

        }

        return false;
    }
}
