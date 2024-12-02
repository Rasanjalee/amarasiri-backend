package com.amarasiricoreservice.Repository;

import com.amarasiricoreservice.entity.LeasePaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface LeasePaymentHisortyRepository extends JpaRepository<LeasePaymentHistory,Integer> {

    List<LeasePaymentHistory> findAllByLeaseKey(int leaseKey);

    @Query("SELECT COALESCE(SUM(li.paymentAmount), 0)" +
            " FROM LeasePaymentHistory li " +
            "WHERE li.paymentdateTime BETWEEN :startDate AND :endDate")
    Double sumPaymentAmountByPaymentDateTimeBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


}
