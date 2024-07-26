package com.bearsoft.pharmadepot.models.security;

import com.bearsoft.pharmadepot.models.domain.entities.Pharmacy;
import com.bearsoft.pharmadepot.models.domain.entities.Role;
import com.bearsoft.pharmadepot.models.domain.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@AllArgsConstructor
public class SecurityPharmacy implements UserDetails {

    private Pharmacy pharmacy;

    @Override
    public String getUsername() {
        return pharmacy.getName();
    }

    @Override
    public String getPassword() {
        return pharmacy.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : pharmacy.getRoles()) {
            RoleType roleType = role.getRoleType();

            authorities.add(new SimpleGrantedAuthority(roleType.name()));

        }
        return authorities;
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
