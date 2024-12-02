package com.amarasiricoreservice.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "leaseinstallments")
public class LeaseInstallment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leaseinstallmentkey")
    private Integer leaseInstallmentId;

    @Column(name = "installmentindex")
    private Integer installmentIndex;

    @Column(name = "leasekey")
    private Integer leaseId;

    @Column(name = "paymentdate")
    private Date paymentDate;

    @Column(name = "beginingbalance")
    private Double beginingBalance = 0.0;

    @Column(name = "beginingleasecost")
    private Double beginingLeaseCost = 0.0;

    @Column(name = "endingbalance")
    private Double endingBalance = 0.0;

    @Column(name = "endingleasebalance")
    private Double endingLeaseBalance = 0.0;

    @Column(name = "installment")
    private Double installment = 0.0;

    @Column(name = "principal")
    private Double principal ;

    @Column(name = "interest")
    private Double interest = 0.0;

    @Column(name = "paiedamount")
    private Double paidAmount = 0.0;

    @Column(name = "paiedamountbalance")
    private Double paidAmountBalance = 0.0;

    @Column(name ="totalinterestpayied")
    private Double totalInterestPayied = 0.0;

    @Column(name ="totalcapitalpayied")
    private Double totalCapitalPayied = 0.0;

    @Column(name ="paieddatetime")
    private Date paiedDateTime;

    @Column(name ="receiveduserkey")
    private Integer receivedUserKey = 0;

    @Column(name ="remainingprincipalportion")
    private Double remainingPrincipalPortion = 0.0;

    @Column(name ="remaininginterestportion")
    private Double remainingInterestPortion = 0.0;

    @Column(name ="currentoutstandingbalance")
    private Double currentOutStandingBalance = 0.0;

    @Column(name ="penaltyduration")
    private Integer penaltyDuration = 0;

    @Column(name ="penaltyamount")
    private Double penaltyAmount = 0.0;

    @Column(name ="penaltyinterestrate")
    private Double penaltyInterestRate = 0.0;

    @Column(name ="modifieduserkey")
    private Integer modifiedUserKey = 0;

    @Column(name ="modifieddatetime")
    private Date modifiedDateTime = new Date();

    @Column(name ="ispaymentdone")
    private Integer isPaymentDone = 0;

    @Column(name ="islatepayment")
    private Integer isLatePayment = 0;

    @Column(name ="ispartpayment")
    private Integer isPartPayment = 0;

    @Column(name ="ispaymentoutdated")
    private Integer isPaymentOutDated = 0;

    @Column(name ="remarks")
    private String remarks;

    @Column(name ="statuscode")
    private Integer statusCode = 0;

    @Column(name ="numberofinstallments")
    private Integer numberOfInstallments = 0;

    @Column(name ="lastpenaltycalculateddatetime")
    private Date lastPenaltyCalculatedDateTime = new Date();




}
