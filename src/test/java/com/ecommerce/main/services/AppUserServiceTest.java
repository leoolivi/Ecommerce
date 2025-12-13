// ========================================
// AppUserServiceTest.java
// ========================================
package com.ecommerce.main.services;

import java.util.Arrays;
import java.util.List;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ecommerce.main.data.UserManagementDTO;
import com.ecommerce.main.exceptions.UserAlreadyExistException;
import com.ecommerce.main.models.AppUser;
import com.ecommerce.main.models.enums.AppUserRole;
import com.ecommerce.main.repositories.AppUserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AppUser Service Tests")
class AppUserServiceTest {

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private AppUserService userService;

    private AppUser testUser;
    private UserManagementDTO userDTO;

    @BeforeEach
    void setUp() {
        testUser = AppUser.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .role(AppUserRole.CUSTOMER)
                .build();

        userDTO = new UserManagementDTO("test@example.com", "password", AppUserRole.CUSTOMER);
    }

    @Test
    @DisplayName("Should get all users")
    void testGetUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));

        // Act
        List<AppUser> users = userService.getUsers();

        // Assert
        assertNotNull(users);
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find user by email")
    void testFindUserByEmail() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Act
        AppUser found = userService.findUserByEmail("test@example.com");

        // Assert
        assertNotNull(found);
        assertEquals("test@example.com", found.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("Should throw exception when user not found by email")
    void testFindUserByEmailNotFound() {
        // Arrange
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.findUserByEmail("notfound@example.com");
        });
    }

    @Test
    @DisplayName("Should find user by ID")
    void testFindUserById() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        AppUser found = userService.findUserById(1L);

        // Assert
        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when user not found by ID")
    void testFindUserByIdNotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.findUserById(999L);
        });
    }

    @Test
    @DisplayName("Should create user successfully")
    void testCreateUser() throws UserAlreadyExistException {
        // Arrange
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(AppUser.class))).thenReturn(testUser);

        UserManagementDTO newUserDTO = new UserManagementDTO("new@example.com", "password", AppUserRole.CUSTOMER);

        // Act
        AppUser created = userService.createUser(newUserDTO);

        // Assert
        assertNotNull(created);
        verify(userRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    @DisplayName("Should throw exception when creating duplicate user")
    void testCreateUserDuplicate() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(UserAlreadyExistException.class, () -> {
            userService.createUser(userDTO);
        });
    }

    @Test
    @DisplayName("Should update user successfully")
    void testUpdateUser() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(AppUser.class))).thenReturn(testUser);

        UserManagementDTO updateDTO = new UserManagementDTO("test@example.com", "newpassword", AppUserRole.ADMIN);

        // Act
        AppUser updated = userService.updateUser(updateDTO);

        // Assert
        assertNotNull(updated);
        verify(userRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void testUpdateUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        UserManagementDTO updateDTO = new UserManagementDTO("notfound@example.com", "password", AppUserRole.CUSTOMER);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.updateUser(updateDTO);
        });
    }

    @Test
    @DisplayName("Should delete user successfully")
    void testDeleteUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent user")
    void testDeleteUserNotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.deleteUser(999L);
        });
    }
}