/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.config;

import co.th.linksinnovation.mitrphol.compliance.model.Authority;
import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 *
 * @author Piyawut Chiradejnunt<pchiradejnunt@gmail.com>
 */
@Profile("prod")
@Component
public class CustomAuthenticationProvider extends AbstractCustomAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return validateAuthen(authentication);
    }

    @Override
    protected boolean isServiceAuthen(Authentication authentication) {
        // TODO: implementing  the authentication at production
        return false;
    }
}
