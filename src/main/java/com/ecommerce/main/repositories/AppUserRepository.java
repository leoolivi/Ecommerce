package com.ecommerce.main.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.main.models.AppUser;


public interface AppUserRepository extends JpaRepository<AppUser, Long>{
    public Optional<AppUser> findByEmail(String email);
}
