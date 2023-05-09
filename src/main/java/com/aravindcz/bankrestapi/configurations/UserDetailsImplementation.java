package com.aravindcz.bankrestapi.configurations;

import com.aravindcz.bankrestapi.models.dtos.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * UserDetailsImplementation - Custom user details implementation that connects the user details with the user data transfer object
 * @author Aravind C
 */
@AllArgsConstructor
public class UserDetailsImplementation implements UserDetails {

    private UserDTO userDTO;

    //private BCryptPasswordEncoder bCryptPasswordEncoder;




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(()->userDTO.getRole());
    }

    @Override
    public String getPassword() {
        return userDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return userDTO.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
