package org.mekkaoui.tp_jwt.controllers;


import org.mekkaoui.tp_jwt.repository.UserRepository;
import org.mekkaoui.tp_jwt.service.CustomerUserDetailsService;
import org.mekkaoui.tp_jwt.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private CustomerUserDetailsService customerUserDetailsService;




}
