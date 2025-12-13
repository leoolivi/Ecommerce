// ========================================
// SettingServiceTest.java
// ========================================
package com.ecommerce.main.services;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.main.exceptions.SettingNotFoundException;
import com.ecommerce.main.models.Setting;
import com.ecommerce.main.repositories.SettingRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Setting Service Tests")
class SettingServiceTest {

    @Mock
    private SettingRepository settingRepository;

    @InjectMocks
    private SettingService settingService;

    private Setting testSetting;

    @BeforeEach
    void setUp() {
        testSetting = Setting.builder()
                .id(1L)
                .key("shipping_fee")
                .value("10")
                .build();
    }

    @Test
    @DisplayName("Should add setting successfully")
    void testAddSetting() {
        // Arrange
        when(settingRepository.save(any(Setting.class))).thenReturn(testSetting);

        // Act
        settingService.addSetting("shipping_fee", "10");

        // Assert
        verify(settingRepository, times(1)).save(any(Setting.class));
    }

    @Test
    @DisplayName("Should find setting by key")
    void testFindSettingByKey() throws SettingNotFoundException {
        // Arrange
        when(settingRepository.findByKey("shipping_fee")).thenReturn(Optional.of(testSetting));

        // Act
        Setting found = settingService.findSettingByKey("shipping_fee");

        // Assert
        assertNotNull(found);
        assertEquals("shipping_fee", found.getKey());
        assertEquals("10", found.getValue());
        verify(settingRepository, times(1)).findByKey("shipping_fee");
    }

    @Test
    @DisplayName("Should throw exception when setting not found")
    void testFindSettingByKeyNotFound() {
        // Arrange
        when(settingRepository.findByKey("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SettingNotFoundException.class, () -> {
            settingService.findSettingByKey("nonexistent");
        });
    }

    @Test
    @DisplayName("Should get value by key")
    void testGetValueByKey() throws SettingNotFoundException {
        // Arrange
        when(settingRepository.findByKey("shipping_fee")).thenReturn(Optional.of(testSetting));

        // Act
        String value = settingService.getValueByKey("shipping_fee");

        // Assert
        assertEquals("10", value);
        verify(settingRepository, times(1)).findByKey("shipping_fee");
    }

    @Test
    @DisplayName("Should throw exception when getting value of non-existent key")
    void testGetValueByKeyNotFound() {
        // Arrange
        when(settingRepository.findByKey("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SettingNotFoundException.class, () -> {
            settingService.getValueByKey("nonexistent");
        });
    }

    @Test
    @DisplayName("Should update setting successfully")
    void testUpdateSetting() throws SettingNotFoundException {
        // Arrange
        when(settingRepository.findByKey("shipping_fee")).thenReturn(Optional.of(testSetting));
        when(settingRepository.save(any(Setting.class))).thenReturn(testSetting);

        // Act
        settingService.updateSetting("shipping_fee", "15");

        // Assert
        verify(settingRepository, times(1)).save(any(Setting.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent setting")
    void testUpdateSettingNotFound() {
        // Arrange
        when(settingRepository.findByKey("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SettingNotFoundException.class, () -> {
            settingService.updateSetting("nonexistent", "value");
        });
    }

    @Test
    @DisplayName("Should remove setting successfully")
    void testRemoveSetting() throws SettingNotFoundException {
        // Arrange
        when(settingRepository.findByKey("shipping_fee")).thenReturn(Optional.of(testSetting));
        doNothing().when(settingRepository).deleteById(1L);

        // Act
        settingService.removeSetting("shipping_fee");

        // Assert
        verify(settingRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when removing non-existent setting")
    void testRemoveSettingNotFound() {
        // Arrange
        when(settingRepository.findByKey("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SettingNotFoundException.class, () -> {
            settingService.removeSetting("nonexistent");
        });
    }
}