package com.ecommerce.main.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.main.models.Setting;

public interface SettingRepository extends JpaRepository<Setting, Long>{
    public Optional<Setting> findByKey(String key);
}
