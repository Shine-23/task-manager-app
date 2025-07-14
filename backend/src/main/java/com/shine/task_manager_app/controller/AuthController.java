package com.shine.task_manager_app.controller;

import com.shine.task_manager_app.dto.AuthResponse;
import com.shine.task_manager_app.dto.LoginRequest;
import com.shine.task_manager_app.dto.RegisterRequest;
import com.shine.task_manager_app.entity.ERole;
import com.shine.task_manager_app.entity.Role;
import com.shine.task_manager_app.entity.User;
import com.shine.task_manager_app.security.CustomUserDetails;
import com.shine.task_manager_app.security.JwtTokenProvider;
import com.shine.task_manager_app.service.CustomUserDetailsService;
import com.shine.task_manager_app.service.RoleService;
import com.shine.task_manager_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUsername());
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
            String jwt = jwtTokenProvider.generateToken(customUserDetails);

            return ResponseEntity.ok(new AuthResponse("Login successful", jwt));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String email = registerRequest.getEmail();

        if (userService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        if (userService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Role role = roleService.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role not found"));

        user.setRoles(Set.of(role));
        userService.saveUser(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        String jwt = jwtTokenProvider.generateToken(customUserDetails);

        return ResponseEntity.ok(new AuthResponse("User registered successfully", jwt));
    }
}
