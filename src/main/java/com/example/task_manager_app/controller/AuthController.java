package com.example.task_manager_app.controller;

import com.example.task_manager_app.entity.ERole;
import com.example.task_manager_app.entity.Role;
import com.example.task_manager_app.entity.User;
import com.example.task_manager_app.security.JwtTokenProvider;
import com.example.task_manager_app.service.CustomUserDetailsService;
import com.example.task_manager_app.service.RoleService;
import com.example.task_manager_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
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
    public ResponseEntity<?> authenticUser(@RequestBody Map<String, String> loginRequest ){
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            String jwt = jwtTokenProvider.generateToken(userDetails);

            return ResponseEntity.ok(Map.of("token",jwt));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> registerRequest ){
        String username = registerRequest.get("username");
        String password = registerRequest.get("password");
        String email = registerRequest.get("email");

        if (userService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        if (userService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        // Assign ROLE_USER by default
        Role role = roleService.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role not found"));
        user.setRoles(Set.of(role));

        userService.saveUser(user);

        // Optionally, return JWT after registration
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        String jwt = jwtTokenProvider.generateToken(userDetails);

        return ResponseEntity.ok(Map.of("message", "User registered successfully", "token", jwt));

    }
}
