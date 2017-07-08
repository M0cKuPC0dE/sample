/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.config;

import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import co.th.linksinnovation.mitrphol.compliance.repository.UserDetailsRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * 
 * @author Piyawut Chiradejnunt<pchiradejnunt@gmail.com>
 */
public abstract class AbstractCustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    protected UserDetailsRepository userDetailsRepository;

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
    protected abstract boolean isServiceAuthen(Authentication authentication);
    
    public Authentication validateAuthen(Authentication authentication) {
        if (isServiceAuthen(authentication)) {
            UserDetails findOne = userDetailsRepository.findOne(authentication.getName().toLowerCase());
            List<GrantedAuthority> grantedAuths;
            if (findOne != null) {
                grantedAuths = (List<GrantedAuthority>) findOne.getAuthorities();
            } else {
                grantedAuths = new ArrayList<>();
                grantedAuths.add(new SimpleGrantedAuthority("User"));
            }
            Authentication auth = new UsernamePasswordAuthenticationToken(authentication.getName().toLowerCase(), authentication.getCredentials().toString(), grantedAuths);
            return auth;
        }
        return null;
    }
}
