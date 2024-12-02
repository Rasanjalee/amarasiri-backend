package com.amarasiricoreservice.DTO;

import lombok.Data;

import java.util.Date;
@Data
public class OutDatedPaymentResponse {

    private String  leaseID;
    private String leaseeName;
    private String contactNumber;
    private Double outstandingBalance;
    private String paymentDate;
    private Integer delay;
}
