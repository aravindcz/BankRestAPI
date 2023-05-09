package com.aravindcz.bankrestapi.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Configuration class used to make a custom security filter chain and mention the password encoder that needs to be used
 * for basic authentication
 * @author Aravind C
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityFilterChainConfiguration  {

    /**
     * Method that returns http security object with custom security filter chain
     * @param httpSecurity - HttpSecurity.class instance
     * @return - SecurityFilterChain.class instance
     * @throws Exception - when any of the exceptions declared in the com.mindstix.bankrestapi.exceptions occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf().disable()
                .httpBasic().and()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/customers/register","/api/v1/employees/register").permitAll()
                .anyRequest().authenticated().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .build();
    }

    /**
     * Method that returns the password encoder instance that needs to be used for basic authentication
     * @return - PasswordEncoder.class instance
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

}