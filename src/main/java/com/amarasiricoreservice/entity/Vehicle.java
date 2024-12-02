package com.amarasiricoreservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Leaseevehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Leaseevehiclekey",columnDefinition = "int")
    private Integer leaseeVehicleKey;

    @Column(name="Vehiclenumber")
    private String vehicleNumber;

    @Column(name = "Chassisnumber")
    private String chassisNumber;

    @Column(name = "Vehiclemake")
    private String make;

    @Column(name = "Vehiclemodel")
    private String mode;

    @Column(name = "Vehiclemanufactureyear")
    private String manuYear;
    @Column(name = "Vehicleregistrationyear")
    private String regYear;

    @Column(name = "Numberofowners")
    private int numOwners;

    @Column(name = "Vehicleremarks")
    private String remarks;

    @Column(name = "Vehicleimage")
    private String VehicleImage;

    @Column(name = "Leaseekey")
    private Integer leaseeId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Leaseekey",insertable = false,updatable = false)
    private LeaseeMaster leaseeMasters;

    @Column(name = "Enginenumber")
    private String engineNumber;

//    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
//    @Fetch(FetchMode.SUBSELECT)
//    @JoinColumn(name = "leasevehiclekey")
//    private List<LeaseMaster> leaseMasters;
}
