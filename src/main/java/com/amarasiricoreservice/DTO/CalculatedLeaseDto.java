package com.amarasiricoreservice.DTO;

import lombok.Data;

@Data
public class CalculatedLeaseDto {
    private Double installment;
    private Double interest;
    private Integer outDatedDaysCount;
    private Double penaltyAmount;

}
