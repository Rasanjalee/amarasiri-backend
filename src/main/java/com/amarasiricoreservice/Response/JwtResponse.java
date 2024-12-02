package com.amarasiricoreservice.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private Integer id;
    private String username;
    private String email;
    private List<String> roles;
    private String firstName;
    private String lastName;
}
