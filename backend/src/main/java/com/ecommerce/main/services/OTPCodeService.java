package com.ecommerce.main.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.main.exceptions.OTPCodeNotFoundException;
import com.ecommerce.main.models.OTPCode;
import com.ecommerce.main.repositories.OTPCodeRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OTPCodeService {
    
    private final OTPCodeRepository repo;

    public List<OTPCode> getCodes() {
        return repo.findAll();
    }

    public OTPCode getCodeById(Long id) throws OTPCodeNotFoundException {
        return repo.findById(id).orElseThrow(() -> new OTPCodeNotFoundException("OTP code not found"));
    }

    public OTPCode getCodeByCode(char[] code) throws OTPCodeNotFoundException{
        return repo.findByCode(code).orElseThrow(() -> new OTPCodeNotFoundException("OTP code not found"));
    }

    public OTPCode createCode(OTPCode code) {
        return repo.save(code);
    }

    public boolean isCodeValid(OTPCode code) throws OTPCodeNotFoundException {
        OTPCode matchCode = repo.findById(code.getId()).orElseThrow(() -> new OTPCodeNotFoundException("Code not found"));
        return matchCode.equals(code) && 
                !code.isExpired();
    }
}
