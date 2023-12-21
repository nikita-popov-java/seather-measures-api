package ru.nikitapopov.weathermeasuresapi.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.nikitapopov.weathermeasuresapi.models.ApiUser;

import java.util.Collection;
import java.util.Collections;

public class ApiUserDetails implements UserDetails {

    private final ApiUser apiUser;

    public ApiUserDetails(ApiUser apiUser) {
        this.apiUser = apiUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.apiUser.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return this.apiUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.apiUser.getUsername();
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
