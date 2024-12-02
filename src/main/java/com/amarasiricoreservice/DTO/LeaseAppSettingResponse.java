package com.amarasiricoreservice.DTO;

import lombok.Data;

import java.util.Date;
@Data
public class LeaseAppSettingResponse {

    private Double documentCharge;
    private Double latePaymentInterest;
    private Double visitCharge;
    private Double  monthlyInterest;
    private Double earlierSettlementInterest;
    private String lastModifiedDateTime;
    private String userName;
    private Integer userKey;
    private Double belowBenchInterestCharge;
    private Double aboveBenchInterestCharge;
    private Double interestChangeDelayDuration;
    private Double benchMarkValue;
    private Double delayInterestCharge;

}
