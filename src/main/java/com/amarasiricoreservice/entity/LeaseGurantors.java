package com.amarasiricoreservice.entity;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name = "leasegurantors")
public class LeaseGurantors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guranterkey")
    private Integer guranterKey;

    @Column(name = "gurantyname")
    private String gurantorName;

    @Column(name = "identitinumber")
    private String identityNumber;

    @Column(name = "telephonenumber")
    private String telephoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "leasekey")
    private Integer leaseKey;
}
