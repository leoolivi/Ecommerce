package com.ecommerce.main.services;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.main.data.UserManagementDTO;
import com.ecommerce.main.exceptions.UserAlreadyExistException;
import com.ecommerce.main.models.AppUser;
import com.ecommerce.main.repositories.AppUserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserService {
    
    private final AppUserRepository repo;

    public List<AppUser> getUsers() {
        return repo.findAll();
    }

    public AppUser findUserByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not exists with email: " + email));
    }

    public AppUser findUserById(Long id) {
        return repo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not exists with id: " + id));
    }

    public AppUser createUser(UserManagementDTO request) throws UserAlreadyExistException {
        if (!repo.findByEmail(request.email()).isEmpty()) throw new UserAlreadyExistException("User already exists with email '" + request.email() + "'");
        AppUser user = new AppUser(request.email(), request.password(), request.role());
        return repo.save(user);
    }
    
    @Transactional
    public AppUser updateUser(UserManagementDTO request) {
        AppUser user = repo.findByEmail(request.email()).orElseThrow(() -> new UsernameNotFoundException("User not exists with email " + request.email()));
        if (!request.email().isEmpty()) user.setEmail(request.email());
        if (!request.password().isEmpty()) user.setPassword(request.password());
        if (request.role() != null) user.setRole(request.role());
        return repo.save(user);
    }

    public void deleteUser(Long id) {
        AppUser user = repo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not exists with id: " + id));
        repo.delete(user);
    }

}
