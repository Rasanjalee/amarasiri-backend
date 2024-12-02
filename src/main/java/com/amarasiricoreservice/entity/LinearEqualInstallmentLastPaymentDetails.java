package com.amarasiricoreservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;


@Data
@Entity
public class LinearEqualInstallmentLastPaymentDetails {

    @Id
    @Column(name = "lease_key")
    private Integer leaseKey;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "last_payment_date")
    private LocalDate lastPaymentDate;

    @Column(name = "remaining_capital")
    private Double remainingCapital;

    @Column(name = "month_rate")
    private Double monthInterestRate;
}
