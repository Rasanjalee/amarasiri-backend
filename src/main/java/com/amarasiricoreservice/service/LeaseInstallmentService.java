package com.amarasiricoreservice.service;

import com.amarasiricoreservice.DTO.OutDatedPaymentResponse;
import com.amarasiricoreservice.Repository.LeaseInstallmentRepository;
import com.amarasiricoreservice.Repository.LinearEqualInstallmentLastPaymentRepo;
import com.amarasiricoreservice.entity.LeaseInstallment;
import com.amarasiricoreservice.entity.LinearEqualInstallmentLastPaymentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LeaseInstallmentService {

    @Autowired
    LeaseInstallmentRepository leaseInstallmentRepository;

    @Autowired
    LinearEqualInstallmentLastPaymentRepo lastPaymentRepository;

    public LeaseInstallment getMaxLeaseInstallmentForLeaseId(int leaseId){
        return leaseInstallmentRepository.getMaxLeaseInstallment(leaseId);
    }

    public List<LeaseInstallment> saveLeaseInstallments(List<LeaseInstallment> installments) {
        System.out.println("save Installments");
        System.out.println(installments.get(0));
        return leaseInstallmentRepository.saveAll(installments);
    }
    public List<LeaseInstallment> getRemainLeaseInstallmentOrderByPaymentDate(Integer leaseId){
        List<LeaseInstallment> leaseInstallment = leaseInstallmentRepository.findNotDoneByLeaseIdOrderByPaymentDate(leaseId);
        return leaseInstallment.size()==0?new ArrayList<>() :leaseInstallment;
    }


    public LinearEqualInstallmentLastPaymentDetails lastPaymentDetails(int leaseId) {
        return lastPaymentRepository.getLastPaymentDetailsOfLease(leaseId);
    }

    public LeaseInstallment updatedInstallment(Integer leaseId, Integer lastPaidInstallmentIndex, Integer penaltyDuration, Double penaltyAmount, Date paymentDate, Integer userId) {

        LeaseInstallment leaseInstallment = leaseInstallmentRepository.findByLeaseIdAndInstallmentIndex(leaseId,lastPaidInstallmentIndex);

        leaseInstallment.setPenaltyAmount(penaltyAmount);
        leaseInstallment.setPenaltyDuration(penaltyDuration);
        leaseInstallment.setReceivedUserKey(userId);
        leaseInstallment.setPaiedDateTime(paymentDate);
        leaseInstallment.setIsLatePayment(penaltyDuration>0?1:0);
        leaseInstallment.setIsPaymentDone(1);
        leaseInstallment.setIsPaymentOutDated(penaltyDuration>0?1:0);
        leaseInstallment.setTotalInterestPayied(leaseInstallment.getTotalInterestPayied()+penaltyAmount);
        return leaseInstallmentRepository.save(leaseInstallment);

    }

    public LeaseInstallment getLastLeaseInstallmentForLeaseId(Integer leaseId) {
        return leaseInstallmentRepository.getLastInstallment(leaseId);
    }

//    public Double getTotalInterst(Date startDate, Date endDate) {
//        return leaseInstallmentRepository.sumInterestByPaymentDateBetween(startDate,endDate);
//    }

    public List<LeaseInstallment> getOutDatedPayments() {

       return leaseInstallmentRepository.findByIsPaymentOutDated(1);


    }

    public LeaseInstallment updatedInstallmentForOtherTypes(int isPaymentOutDated, int statusCode, Double penaltyInterestRate, Double penaltyAmount, Integer penaltyDuration, int isPaymentDone, double paidAmount, double paidAMountBalancec, Integer leaseInstallmentId, int isLatepayment, int isPartPayment, double currentOutstndingBalance, double totalInterestPayied, double totalCapitalPayied, double remainingInterestPortion, double remainingPrincipalPortion, Integer userKey) {

        LeaseInstallment leaseInstallment = leaseInstallmentRepository.findById(leaseInstallmentId).get();
        leaseInstallment.setIsPaymentOutDated(isPaymentOutDated);
        leaseInstallment.setStatusCode(statusCode);
        leaseInstallment.setPenaltyInterestRate(penaltyInterestRate);
        leaseInstallment.setPenaltyAmount(penaltyAmount);
        leaseInstallment.setPenaltyDuration(penaltyDuration);
        leaseInstallment.setIsPaymentDone(isPaymentDone);
        leaseInstallment.setPaidAmount(paidAmount);
        leaseInstallment.setPaidAmountBalance(paidAMountBalancec);
        leaseInstallment.setPaiedDateTime(new Date());
        leaseInstallment.setIsLatePayment(isLatepayment);
        leaseInstallment.setIsPartPayment(isPartPayment);
        leaseInstallment.setCurrentOutStandingBalance(currentOutstndingBalance);
        leaseInstallment.setTotalInterestPayied(totalInterestPayied);
        leaseInstallment.setTotalCapitalPayied(totalCapitalPayied);
        leaseInstallment.setRemainingInterestPortion(remainingInterestPortion);
        leaseInstallment.setRemainingPrincipalPortion(remainingPrincipalPortion);
        leaseInstallment.setReceivedUserKey(userKey);
        return leaseInstallment;
//        return leaseInstallmentRepository.save(leaseInstallment);



    }

    public void save(LeaseInstallment leaseInstallment) {
        leaseInstallmentRepository.save(leaseInstallment);
    }

    public List<LeaseInstallment> findLeaseInstallments(Date startDate, Integer leaseId) {
        return leaseInstallmentRepository.findInstallmentsByLeaseKeyAndStartDate(leaseId,startDate);
    }

    public LeaseInstallment updatedInstallmentForOtherTypesWithPenalty(int isPaymentOutDated, int statusCode, double latepaymentInterest, double installmentPenaltyAmount, double installmentPenaltyDuration, Integer leaseInstallmentId) {
        LeaseInstallment leaseInstallment=leaseInstallmentRepository.findById(leaseInstallmentId).get();

        leaseInstallment.setIsPaymentOutDated(isPaymentOutDated);
        leaseInstallment.setStatusCode(statusCode);
        leaseInstallment.setPenaltyInterestRate(latepaymentInterest);
        leaseInstallment.setPenaltyAmount(installmentPenaltyAmount);
        leaseInstallment.setPenaltyDuration((int)installmentPenaltyDuration);
        return leaseInstallment;
    }
}
