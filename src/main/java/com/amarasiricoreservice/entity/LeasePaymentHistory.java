package com.amarasiricoreservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "leasepaymenthistory")
public class LeasePaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentkey")
    private Integer paymentkey;

    @Column(name = "leasekey")
    private Integer leaseKey;

    @Column(name = "paymentdatetime")
    private Date paymentdateTime;

    @Column(name = "paymentamount")
    private Double paymentAmount;

    @Column(name = "receiveduserkey")
    private Integer receiveduserKey;

    @Column(name = "receiveddatetime")
    private Date receiveddatetime;

    @Column(name= "isdiscountpayment")
    private Integer isdiscountpayment = 0;

}
