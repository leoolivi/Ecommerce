package com.ecommerce.main.models;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.main.models.enums.AppUserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class AppUser implements UserDetails {
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @NonNull
    private String email;

    @NonNull
    private String password;

    @Enumerated(EnumType.STRING) @NonNull
    private AppUserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public String getUsername() {
        return getEmail();
    }
    
}
