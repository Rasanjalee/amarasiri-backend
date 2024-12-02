package com.amarasiricoreservice.service;

import com.amarasiricoreservice.Repository.LeasePaymentBreakDownRepo;
import com.amarasiricoreservice.entity.LeasePaymentBreakdown;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LeasePaymentBreakDownService {
    @Autowired
    LeasePaymentBreakDownRepo leasePaymentBreakDownRepo;

    public LeasePaymentBreakdown update(LeasePaymentBreakdown leasePaymentBreakdown){

        return leasePaymentBreakDownRepo.save(leasePaymentBreakdown);
    }

    public LeasePaymentBreakdown updateLeasePaymentBreakdown(Integer leaseInstallmentId, Double installment, Double paidAmountBalance, Double penaltyAmount, Integer penaltyDuration, Double penaltyInterestRate, Date lastPenaltyCalculatedDateTime, double interestPayid, double capitalPayid) {

        LeasePaymentBreakdown leasePaymentBreakdown = new LeasePaymentBreakdown();
        leasePaymentBreakdown.setInstallmentKey(leaseInstallmentId);
        leasePaymentBreakdown.setIsDiscountPayment(0);
        leasePaymentBreakdown.setBaseInstallment(installment);
        leasePaymentBreakdown.setDueinstalment(paidAmountBalance);
        leasePaymentBreakdown.setPenaltyAmount(penaltyAmount);
        leasePaymentBreakdown.setPenatyDuration(penaltyDuration);
        leasePaymentBreakdown.setInterestRate(penaltyInterestRate);
        leasePaymentBreakdown.setLastPenaltyCalculatedDateTime(lastPenaltyCalculatedDateTime);
        leasePaymentBreakdown.setTransactionDateTime(new Date());
        leasePaymentBreakdown.setInterestPortion(interestPayid);
        leasePaymentBreakdown.setCapitalPortion(capitalPayid);
        return leasePaymentBreakdown;
//        leasePaymentBreakDownRepo.save(leasePaymentBreakdown);

    }

    public void save(LeasePaymentBreakdown leasePaymentBreakDown) {
        leasePaymentBreakDownRepo.save(leasePaymentBreakDown);
    }
}
