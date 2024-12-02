package com.amarasiricoreservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
@Data
@Entity
@Table(name = "leasemaster")
public class LeaseMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leasekey")
    private Integer leaseKey;

    @Column(name = "leaseid")
    private String leaseID;

    @Column(name = "Leaseekey")
    private Integer leaseeKey;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Leaseekey",insertable = false,updatable = false)
    private LeaseeMaster leaseeMaster;

    @Column(name = "leaseamount")
    private Double leaseAmount;
    @Column(name = "annualinterestrate")
    private Float annualInterestRate;

    @Column(name = "leaseduration")
    private Integer leaseDuration;
    @Column(name = "leasestartdate")
    private Date leaseStartDate;
    @Column(name = "installment")
    private Double installment;
    @Column(name = "totalinterest")
    private Double totalInterest;
    @Column(name = "remainingtotalinterestforlastpayment")
    private Double remainingTotalInterestForLastPayment;
    @Column(name = "totalleasecost")
    private Double totalLeaseCost;

    @Column(name = "remainingtotalleasecostforlastpayment")
    private Double remainingTotalLeaseCostForLastPayment;
    @Column(name = "cashoncustomerhand")
    private Double cashOnCustomerHand;
    @Column(name = "totalinterestcollected")
    private Double totalInterestCollected;
    @Column(name = "leasetypekey")
    private Integer leaseTypeKey;
    @Column(name = "documentcharge")
    private Double documentCharge;
    @Column(name = "visitcharge")
    private Double visitCharge;
    @Column(name = "panneltyduration")
    private Integer panneltyDuration;
    @Column(name = "panneltyforstartdate")
    private Double panneltyForStartDate;
    @Column(name = "ispaymentoutdated")
    private Integer isPaymentOutDated;
    @Column(name = "nextpaymentdate")
    private Date nextPaymentDate;
    @Column(name = "currentoutstandingbalance")
    private Double currentOutStandingBalance=0.0;
    @Column(name = "nextpaymentdateoutstandingbalance")
    private Double nextPaymentDateOutStandingBalance;
    @Column(name = "remainingleaseamount")
    private Double remainingLeaseAmount;
    @Column(name = "remainingcapial")
    private Double remainingCapial;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "hasoverriden")
    private Integer hasOverriden = 0;
    @Column(name = "leaseapproveduserkey")
    private Integer leaseApprovedUserKey;
    @Column(name = "leaseapproveddatetime")
    private Date leaseApprovedDateTime;
    @Column(name = "leaserating")
    private Float leaseRating;

    @Column(name = "leasevehiclekey")
    private Integer leaseVehicleKey;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leasevehiclekey",insertable = false,updatable = false)
    private Vehicle leaseVehicles;

    @Column(name = "leaseprocessuserkey")
    private Integer leaseProcessUserKey;
    @Column(name = "leaseprocessdatetime")
    private Date leaseProcessDateTime;
    @Column(name = "isleaseclosed")
    private Integer isLeaseClosed;
    @Column(name = "ishidden")
    private Integer isHidden = 0;
    @Column(name = "hiddenuserkey")
    private Integer hiddenUserKey;
    @Column(name = "hiddendatetime")
    private Date hiddenDateTime;
    @Column(name = "penaltydurationfornextpayment")
    private Integer penaltyDurationForNextPayment;
    @Column(name = "penaltyamountfornextpayment")
    private Double penaltyAmountForNextPayment;
    @Column(name = "lastpaidinstallmentindex")
    private Integer lastPaidInstallmentIndex;
    @Column(name = "remainingcapitalfortoday")
    private Double remainingCapitalForToday;
    @Column(name = "closeduserkey")
    private Integer closedUserKey;
    @Column(name = "closeddatetime")
    private Date closedDateTime;
    @Column(name = "closingamount")
    private Double closingAmount;
    @Column(name = "closinginterest")
    private Double closingInterest;
    @Column(name = "closingcalculatedinterestamount")
    private Double closingCalculatedInterestAmount;
    @Column(name = "closingcapitalamount")
    private Double closingCapitalAmount;
}
