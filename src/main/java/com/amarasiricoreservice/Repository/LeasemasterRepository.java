package com.amarasiricoreservice.Repository;

import com.amarasiricoreservice.entity.LeaseMaster;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LeasemasterRepository extends JpaRepository<LeaseMaster,Integer> {


    List<LeaseMaster> findByIsLeaseClosed(Integer isClosed);

    List<LeaseMaster>  findAllByLeaseVehicleKey(Integer vehicleKey);

    @Query("SELECT COALESCE(SUM(li.leaseAmount), 0)" +
            " FROM LeaseMaster li " +
            "WHERE li.leaseStartDate BETWEEN :startDate AND :endDate")
    Double sumLeaseAmountByLeaseStartDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query("SELECT COALESCE(SUM(li.cashOnCustomerHand), 0)" +
            " FROM LeaseMaster li " +
            "WHERE li.leaseStartDate BETWEEN :startDate AND :endDate")
    Double sumTotalLeaseCostByLeaseStartDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    @Query("SELECT COALESCE(SUM(li.totalInterestCollected), 0)" +
            " FROM LeaseMaster li " +
            "WHERE li.leaseStartDate BETWEEN :startDate AND :endDate")
    Double getTotalInterst(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT COALESCE(SUM(li.totalInterest), 0)" +
            " FROM LeaseMaster li " +
            "WHERE li.leaseStartDate BETWEEN :startDate AND :endDate")
    Double sumTotalInterestByLeaseStartDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT COUNT(e) FROM LeaseMaster e WHERE e.leaseID LIKE :datePart%")
    int countByLeaseIdStartingWith(String datePart);

    @Query("SELECT li" +
            " FROM LeaseMaster li " +
            "WHERE li.leaseStartDate BETWEEN :startDate AND :endDate")
    List<LeaseMaster> findAllByLeaseStartDateBetween(Date startDate, Date endDate);

    LeaseMaster findByLeaseKey(Integer leaseKey);
}
