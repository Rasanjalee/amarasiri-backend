package com.amarasiricoreservice.controller;

import com.amarasiricoreservice.Repository.UserGroupsRepository;
import com.amarasiricoreservice.Repository.UserRepository;
import com.amarasiricoreservice.Request.LoginRequest;
import com.amarasiricoreservice.Request.SignupRequest;
import com.amarasiricoreservice.Response.JwtResponse;
import com.amarasiricoreservice.Response.MessageResponse;
import com.amarasiricoreservice.entity.UserGroups;
import com.amarasiricoreservice.entity.UserMaster;
import com.amarasiricoreservice.jwt.JwtUtils;
import com.amarasiricoreservice.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserGroupsRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        if (jwt != null) {
            return new ResponseEntity<>(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles ,userDetails.getFirstName(), userDetails.getLastName()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("login-failed", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam("token") String token) {
        if (jwtUtils.validateJwtToken(token)) {
            if (!jwtUtils.isTokenExpired(token)) {
                return new ResponseEntity<>("Token is valid", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Token is expired", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }
    }
}


