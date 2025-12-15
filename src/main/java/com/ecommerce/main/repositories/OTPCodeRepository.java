package com.ecommerce.main.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.main.models.OTPCode;


public interface OTPCodeRepository extends JpaRepository<OTPCode, Long> {
    public List<OTPCode> findAllByUserId(Long userId);
    public Optional<OTPCode> findByCode(char[] code);
}
