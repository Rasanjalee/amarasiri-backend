package com.amarasiricoreservice.Repository;

import com.amarasiricoreservice.entity.LinearEqualInstallmentLastPaymentDetails;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class LinearEqualInstallmentLastPaymentRepo {
    private EntityManager entityManager;

    public LinearEqualInstallmentLastPaymentRepo(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    public LinearEqualInstallmentLastPaymentDetails getLastPaymentDetailsOfLease(int leaseId) {
        LinearEqualInstallmentLastPaymentDetails paymentDetails = new LinearEqualInstallmentLastPaymentDetails();

        String query = String.format( "SELECT TOP 1 lm.LeaseKey                             AS lease_key,\n" +
                "             CONVERT(DATE, IIF(lph.PaymentDateTime IS NULL, lm.LeaseStartDate,\n" +
                "                               lph.PaymentDateTime)) AS last_payment_date,\n" +
                "             lm.RemainingCapial                      as remaining_capital,\n" +
                "             lm.AnnualInterestRate                   as month_rate\n" +
                "FROM LeaseMaster lm\n" +
                "         LEFT JOIN\n" +
                "     LeasePaymentHistory lph\n" +
                "     ON\n" +
                "         lm.LeaseKey = lph.LeaseKey\n" +
                "WHERE lm.LeaseKey = %d\n" +
                "ORDER BY lph.PaymentKey DESC;", leaseId);

        try {
            paymentDetails =
                    ( LinearEqualInstallmentLastPaymentDetails )
                            entityManager.createNativeQuery( query, LinearEqualInstallmentLastPaymentDetails.class).getSingleResult();

            return paymentDetails;
        } catch (Exception e) {
            return null;
        }
    }

}
