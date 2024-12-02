package com.amarasiricoreservice.Repository;

import com.amarasiricoreservice.entity.ReducingBalanceInterests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReducingBalanceInterestRepo extends JpaRepository<ReducingBalanceInterests,Integer> {
    @Query("select RBI from ReducingBalanceInterests  RBI where RBI.leaseKey= :leaseKey order by RBI.chargableDate DESC ")
    ReducingBalanceInterests getTopByLeaseKeyOrdOrderByChargableDateDesc(Integer leaseKey);
}
