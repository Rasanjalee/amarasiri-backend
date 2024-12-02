package com.amarasiricoreservice.DTO;

import lombok.Data;

@Data
public class IncomeReportResponse {
    private Double totalSaleActual;
    private Double totalInterestToCollect;
    private Double totalSaleCapital;
    private Double totalCollection;
    private Double totalInterest;
}
