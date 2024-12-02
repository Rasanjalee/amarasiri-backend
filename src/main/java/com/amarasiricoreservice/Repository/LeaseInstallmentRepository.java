package com.amarasiricoreservice.Repository;

import com.amarasiricoreservice.entity.LeaseInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface LeaseInstallmentRepository extends JpaRepository<LeaseInstallment,Integer> {
    @Query("select li from LeaseInstallment li " +
            "where li.leaseId= :leaseId " +
            "and li.isPaymentDone= 0" +
            "and li.installmentIndex= " +
                "(select min(lI.installmentIndex)" +
                "  from LeaseInstallment lI " +
                "where lI.leaseId =: leaseId) ")
    LeaseInstallment getMaxLeaseInstallment(int leaseId);

    LeaseInstallment findByLeaseIdAndInstallmentIndex(Integer leaseId, Integer lastPaidInstallmentIndex);

    @Query("select li from LeaseInstallment li " +
            "where li.leaseId= :leaseId and " +
            " li.installmentIndex= " +
            "      (select min(lI.installmentIndex)" +
            "       from LeaseInstallment lI " +
            "       where lI.leaseId =: leaseId)")
    LeaseInstallment getLastInstallment(Integer leaseId);

    @Query("SELECT COALESCE(SUM(li.interest), 0)" +
            " FROM LeaseInstallment li " +
            "WHERE li.paymentDate BETWEEN :startDate AND :endDate")
    Double sumInterestByPaymentDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    List<LeaseInstallment> findByIsPaymentOutDated(Integer isOutDated);


    @Query("select li from LeaseInstallment li where li.leaseId = :leaseId \n" +
            "and  li.isPaymentDone=0 order by li.paymentDate")
    List<LeaseInstallment> findNotDoneByLeaseIdOrderByPaymentDate(Integer leaseId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value =
            "MERGE INTO LeaseInstallments S " +
                    "USING (SELECT ?1 AS LeaseKey) R ON " +
                    "S.IsPaymentDone = 0 AND S.LeaseKey = R.LeaseKey " +
                    "WHEN MATCHED THEN " +
                    "UPDATE SET " +
                    "S.PenaltyInterestRate = ?2, " +
                    "S.Principal = ?3, " +
                    "S.PenaltyDuration = ?4, " +
                    "S.PenaltyAmount = ?5 " +
                    "WHEN NOT MATCHED THEN " +
                    "INSERT (InstallmentIndex, LeaseKey, PaymentDate, BeginingBalance, BeginingLeaseCost, EndingBalance, EndingLeaseBalance, Installment, Principal, Interest, PaiedAmount, PaiedAmountBalance, TotalInterestPayied, TotalCapitalPayied, PaiedDateTime, ReceivedUserKey, RemainingPrincipalPortion, RemainingInterestPortion, CurrentOutStandingBalance, PenaltyDuration, PenaltyAmount, PenaltyInterestRate, ModifiedUserKey, ModifiedDateTime, IsPaymentDone, IsLatePayment, IsPartPayment, IsPaymentOutDated, Remarks, StatusCode, NumberOfInstallments, LastPenaltyCalculatedDateTime) " +
                    "VALUES (1, ?1, CURRENT_TIMESTAMP, ?3, ?6, ?3, ?6, 0, ?3, ?5, 0, 0, 0, 0, NULL, NULL, ?3, ?5, ?6, ?4, ?5, ?2, ?7, CURRENT_TIMESTAMP, 0, 0, 0, 0, NULL, 0, 0, CURRENT_TIMESTAMP);")
    void mergeLeaseInstallments(Integer leaseKey, Double interestRate, Double principalAmount, Integer numberOfDaysInterestCharged, Double interestForToday, Double leaseCostForToday, Integer loggedInUserKey);

    @Query(value = "SELECT LI.*, L.NextPaymentDate FROM LeaseInstallments LI, LeaseMaster L " +
            "WHERE L.LeaseKey = :leaseKey AND LI.LeaseKey = L.LeaseKey " +
            "AND IsPaymentDone = 0 AND LI.PaymentDate <= :startDate " +
            "ORDER BY PaymentDate", nativeQuery = true)
    List<LeaseInstallment> findInstallmentsByLeaseKeyAndStartDate(@Param("leaseKey") int leaseKey, @Param("startDate") Date startDate);


}
