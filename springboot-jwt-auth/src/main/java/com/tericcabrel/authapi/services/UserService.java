package com.tericcabrel.authapi.services;

import com.tericcabrel.authapi.dtos.RegisterUserDto;
import com.tericcabrel.authapi.entities.Role;
import com.tericcabrel.authapi.entities.RoleEnum;
import com.tericcabrel.authapi.entities.User;
import com.tericcabrel.authapi.repositories.RoleRepository;
import com.tericcabrel.authapi.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ShoppingCartService shoppingCartService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.shoppingCartService = shoppingCartService;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public User updateUser(Long id, RegisterUserDto input) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return null;
        }

        User existingUser = optionalUser.get();
        existingUser.setFullName(input.getFullName());
        existingUser.setEmail(input.getEmail());
        existingUser.setPassword(passwordEncoder.encode(input.getPassword()));
        existingUser.setUpdatedAt(new Date());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }
}
