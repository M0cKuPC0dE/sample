/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.config;

import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import co.th.linksinnovation.mitrphol.compliance.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * OAuth2Configuration configuration Class
 *
 * @author Piyawut Chiradejnunt<pchiradejnunt@gmail.com>
 */
@Configuration
public class OAuth2Configuration {

    @Configuration
    @EnableResourceServer
    protected static class ResourceServer extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/hello")
                    .permitAll()
                    .antMatchers("/api/**")
                    .authenticated()
                    .anyRequest()
                    .permitAll();
        }

    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private UserDetailsRepository userDetailsRepository;

        @Bean
        public AccessTokenConverter converter() {
            return new JwtAccessTokenConverter() {
                @Override
                public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                    DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
                    UserDetails userDetails = userDetailsRepository.findOne(authentication.getPrincipal().toString());
                    defaultOAuth2AccessToken.getAdditionalInformation().put("name", userDetails.getNameTh());
                    return super.enhance(defaultOAuth2AccessToken, authentication);
                }

            };
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security.checkTokenAccess("permitAll()");
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.authenticationManager(authenticationManager).accessTokenConverter(converter());
        }
        
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("client")
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes("global")
                    .secret("secret");
        }
    }
}
