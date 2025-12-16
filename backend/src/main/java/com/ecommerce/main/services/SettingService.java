package com.ecommerce.main.services;

import org.springframework.stereotype.Service;

import com.ecommerce.main.exceptions.SettingNotFoundException;
import com.ecommerce.main.models.Setting;
import com.ecommerce.main.repositories.SettingRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettingService {
    
    private final SettingRepository repo;

    @Transactional
    public void updateSetting(String key, String value) throws SettingNotFoundException {
        Setting setting = findSettingByKey(key);
        setting.setValue(value);
        repo.save(setting);
    }

    public void addSetting(String key, String value) {
        Setting newSetting = Setting.builder().key(key).value(value).build();
        repo.save(newSetting);
    }

    public Setting findSettingByKey(String key) throws SettingNotFoundException {
        return repo.findByKey(key).orElseThrow(() -> new SettingNotFoundException("Setting not found with key " + key));
    }

    public String getValueByKey(String key) throws SettingNotFoundException {
        Setting setting = findSettingByKey(key);
        return setting.getValue();
    }

    public void removeSetting(String key) throws SettingNotFoundException {
        var setting = findSettingByKey(key);
        repo.deleteById(setting.getId());
    }
}
