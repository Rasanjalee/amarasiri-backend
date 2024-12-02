package com.amarasiricoreservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "leaseappsettings")
public class LeaseAppSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leaseappkey")
    private Integer leaseAppKey;

    @Column(name = "documentcharge")
    private Double documentCharge;

    @Column(name = "visitcharge")
    private Double visitCharge;

    @OneToOne
    @JoinColumn(name = "modifieduserkey",referencedColumnName = "userkey")
    private UserMaster user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modifieddatetime")
    private Date modifiedDateTime = new Date();

    @Column(name = "monthlyinterest")
    private Double monthlyInterest;

    @Column(name = "latepaymentinterest")
    private Double latePaymentInterest;

    @Column(name = "belowbenchinterestcharge")
    private Double belowBenchInterestCharge;

    @Column(name = "abovebenchinterestcharge")
    private Double aboveBenchInterestCharge;

    @Column(name = "interestchangedelayduration")
    private Double interestChangeDelayDuration;

    @Column(name = "benchmarkvalue")
    private Double benchMarkValue;

    @Column(name = "earliersettlementinterest")
    private Double earlySettlementInterest;

    @Column(name = "delayinterestcharge")
    private Double delayInterestCharge;
}
