package com.amarasiricoreservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "leasepaymentbreakdown")
public class LeasePaymentBreakdown {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="historykey")
    private Integer historyKey;

    @Column(name = "paymentkey")
    private Integer paymentKey;

    @Column(name = "installmentkey")
    private Integer installmentKey;

    @Column(name = "baseinstallment")
    private Double baseInstallment;

    @Column(name = "dueinstallment")
    private Double dueinstalment;

    @Column(name = "interestrate")
    private Double interestRate;

    @Column(name = "penaltyduration")
    private Integer penatyDuration;

    @Column(name = "penaltyamount")
    private Double penaltyAmount;

    @Column(name = "lastpenaltycalculateddatetime")
    private Date  lastPenaltyCalculatedDateTime;

    @Column(name = "transactiondatetime")
    private Date transactionDateTime;

    @Column(name = "interestportion")
    private Double interestPortion;

    @Column(name = "capitalportion")
    private Double capitalPortion;

    @Column(name = "isdiscountpayment")
    private Integer isDiscountPayment;
}
