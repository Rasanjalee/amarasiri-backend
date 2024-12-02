package com.amarasiricoreservice.entity;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "Leaseemaster")
public class LeaseeMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="leaseekey")
    private Integer leaseeId;

    @Column(name="firstname")
    private String firstName;

    @Column(name="lastname")
    private String lastName;

    @Column(name="identificationnumber")
    private String nic;

    @Column(name="mobilenumber")
    private String mobileNumber;

    @Column(name="homenumber")
    private String homeNumber;

    @Column(name="address")
    private String address;

    @Column(name="googlecoordinates")
    private String googleCords;

    @Column(name="loyaltystatus")
    private int loyaltyStatus;

    @Column(name="leaseeremarks")
    private String remarks;

    @Column(name="profileimagepath")
    private String profimagePath;

    @Column(name="rating")
    private Float rating;

    @Column(name="email")
    private String email;

    @Column(name="ishidden")
    private int isHidden;

//    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
//    @Fetch(FetchMode.SUBSELECT)
//    @JoinColumn(name = "Leaseekey")
//    private List<Vehicle> vehicles;

//    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
//    @Fetch(FetchMode.SUBSELECT)
//    @JoinColumn(name = "Leaseekey")
//    private List<LeaseMaster> leaseMasters;

    }
