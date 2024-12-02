package com.amarasiricoreservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
@Data
@Entity()
@Table(name ="reducingbalanceinterests")
public class ReducingBalanceInterests {
    @Column(name="leasekey")
    private Integer leaseKey;

    @Column(name = "principalamount")
    private Double principalAmount;

    @Column(name = "interestrate")
    private Double interestRate;

    @Column(name = "chargabledate")
    private Date chargableDate;

    @Column(name = "interestamount")
    private Double interestAmount;

    @Column(name = "paidamount")
    private Double paidAmount;

    @Column(name = "balanceamount")
    private Double balanceAmount;

    @Column(name = "ispartpayment")
    private Integer isPartPayment;

    @Column(name = "ispaymentdone")
    private Integer isPartPaymentDone;

    @Column(name = "transactiondatetime")
    private Date transactionDateTime;

    @Column(name = "transactionuserkey")
    private Integer transactionUserKey;

    @Column(name="numberofdaysinterestcharged")
    private Integer numberOfDaysInterestCharged;

    @Column(name="isdiscountpayment")
    private Integer isDiscountpayment;

    @Id
    private Integer id;

}
