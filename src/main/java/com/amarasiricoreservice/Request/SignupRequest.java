package com.amarasiricoreservice.Request;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class SignupRequest {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String  lastName;
    private String mobileNumber;
    private Integer userType;
    private String profileImagePath;
    private Integer isEnable;
    private Integer isHidden;
    private String address;
    private String identificationNumber;
    private String homeNumber;
    private Integer userGroupKey;
}
