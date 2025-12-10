package com.ecommerce.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.main.models.AppUser;
import java.util.List;
import java.util.Optional;


public interface AppUserRepository extends JpaRepository<AppUser, Long>{
    public Optional<AppUser> findByEmail(String email);
}
