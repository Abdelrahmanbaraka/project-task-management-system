package com.abdelrahman.backend.service;

import com.abdelrahman.backend.dto.user.UserCreateRequest;
import com.abdelrahman.backend.entity.Role;
import com.abdelrahman.backend.entity.User;
import com.abdelrahman.backend.enums.RoleName;
import com.abdelrahman.backend.exception.BadRequestException;
import com.abdelrahman.backend.repository.RoleRepository;
import com.abdelrahman.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        UserRepository userRepository = mock(UserRepository.class);
        RoleRepository roleRepository = mock(RoleRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        UserService userService = new UserService(userRepository, roleRepository, passwordEncoder);

        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("admin");
        request.setEmail("new@example.com");
        request.setPassword("Test123!");
        request.setRoleName(RoleName.EMPLOYEE);

        when(userRepository.existsByUsername("admin")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.createUser(request));
    }

    @Test
    void shouldCreateUserSuccessfully() {
        UserRepository userRepository = mock(UserRepository.class);
        RoleRepository roleRepository = mock(RoleRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        UserService userService = new UserService(userRepository, roleRepository, passwordEncoder);

        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("mona");
        request.setEmail("mona@example.com");
        request.setPassword("Mona123!");
        request.setRoleName(RoleName.EMPLOYEE);

        Role role = Role.builder()
                .id(1L)
                .name(RoleName.EMPLOYEE)
                .build();

        User savedUser = User.builder()
                .id(10L)
                .username("mona")
                .email("mona@example.com")
                .password("encoded")
                .active(true)
                .role(role)
                .build();

        when(userRepository.existsByUsername("mona")).thenReturn(false);
        when(userRepository.existsByEmail("mona@example.com")).thenReturn(false);
        when(roleRepository.findByName(RoleName.EMPLOYEE)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("Mona123!")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        var response = userService.createUser(request);

        assertEquals("mona", response.getUsername());
        assertEquals("mona@example.com", response.getEmail());
        assertEquals(RoleName.EMPLOYEE, response.getRoleName());
        assertTrue(response.getActive());
    }
}