package com.dindeMarket.api.controller;

import com.dindeMarket.api.payload.AuthRequest;
import com.dindeMarket.api.payload.LoginResponse;
import com.dindeMarket.config.jwt.JwtTokenUtil;
import com.dindeMarket.db.entity.UserEntity;
import com.dindeMarket.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;


    private final JwtTokenUtil jwtTokenUtil;


    private final UserDetailsService jwtUserDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        return ResponseEntity.ok(new LoginResponse(token,roles));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            System.out.println("Authenticating user: " + username); // Добавьте логирование
            System.out.println("Password provided: " + password); // Добавьте логирование
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new Exception("Ошибка аутентификации: неверные учетные данные", e);
    }
    }

    @GetMapping("/me")
    public ResponseEntity<UserEntity> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        // Извлекаем токен из заголовка
        String token = authorizationHeader.replace("Bearer ", "");

        // Находим пользователя по токену
        UserEntity user = userService.findUserByToken(token);

        return ResponseEntity.ok(user);
    }

}
