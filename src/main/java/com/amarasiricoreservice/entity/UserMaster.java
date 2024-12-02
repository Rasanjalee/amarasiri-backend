package com.amarasiricoreservice.entity;

import lombok.Data;
import org.springframework.data.relational.core.sql.In;

import javax.persistence.*;
@Data
@Entity
@Table(name = "usermaster")
public class UserMaster {

    public UserMaster(){}
    public UserMaster(String username,String email,String password){
        this.email=email;
        this.loginId= username;
        this.password =password;
    }

    public UserMaster(String firstName, String lastName, String mobileNumber, Integer userTypeKey,
                      String progileImagePath, String loginId, String password, Integer isEnable,
                      Integer isHidden, String address, String identificationNumber, String homeNumber, String email,UserGroups userGroup) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.userTypeKey = userTypeKey;
        this.progileImagePath = progileImagePath;
        this.loginId = loginId;
        this.password = password;
        this.isEnable = isEnable;
        this.isHidden = isHidden;
        this.address = address;
        this.identificationNumber = identificationNumber;
        this.homeNumber = homeNumber;
        this.email = email;
        this.userGroup= userGroup;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userkey")
    private Integer userKey;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "mobilenumber")
    private String mobileNumber;

    @Column(name= "usertypekey")
    private Integer userTypeKey;

//    @Column(name = "usergroupkey")
//    private Integer userGroupKey;

    @Column(name = "profileimagepath")
    private String progileImagePath;

    @Column(name = "loginid")
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name = "isenable")
    private Integer isEnable;

    @Column(name = "ishidden")
    private Integer isHidden;

    @Column(name = "address")
    private String address;

    @Column(name = "identificationnumber")
    private String identificationNumber;

    @Column(name = "homenumber")
    private String homeNumber;

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "usergroupkey") // The column in UserMaster that references UserGroups
    private UserGroups userGroup;

}
