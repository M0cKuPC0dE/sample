/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.config;

import co.th.linksinnovation.mitrphol.compliance.model.Authority;
import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import org.springframework.context.annotation.Profile;

/**
 *
 * @author Piyawut Chiradejnunt<pchiradejnunt@gmail.com>
 */
@Profile("dev")
@Component
public class MockCustomAuthenticationProvider extends AbstractCustomAuthenticationProvider {
    
    private static final String ADMIN = "admin";
    private static final String COORDINATOR = "coordinator";
    private static final String OWNER = "owner";
    private static final String REVIEWER = "reviewer";
    private static final String APPROVER = "approver";
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return validateAuthen(authentication);
    }
    
    @Override
    protected boolean isServiceAuthen(Authentication authentication) {
        if (mockAuthen(authentication.getCredentials().toString())) {
            UserDetails userDetails = userDetailsRepository.findOne(authentication.getName().toLowerCase());
            if (userDetails == null) {
                userDetails = new UserDetails();
                userDetails.setUsername(authentication.getName().toLowerCase());
                userDetails.setPassword(authentication.getCredentials().toString());
                
                Authority authority = new Authority();
                authority.setAuthority("User");
                userDetails.addAuthority(authority);
                
            }
            userDetailsRepository.save(userDetails);
            return true;
        }
        
        return false;
    }

    /**
     *
     * @param password
     * @return
     *
     * Mock Password Remove it in production
     *
     */
    private boolean mockAuthen(String password) {
        return ADMIN.equals(password)
                || COORDINATOR.equals(password)
                || OWNER.equals(password)
                || REVIEWER.equals(password)
                || APPROVER.equals(password);
    }
}
