/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Piyawut Chiradejnunt<pchiradejnunt@gmail.com>
 */
public class WebSecurityConfiguration {
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private AbstractCustomAuthenticationProvider customAuthenticationProvider;
    
    @Bean
    public BCryptPasswordEncoder bcryptEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider).userDetailsService(userDetailsService).passwordEncoder(bcryptEncoder());
    }  
}
