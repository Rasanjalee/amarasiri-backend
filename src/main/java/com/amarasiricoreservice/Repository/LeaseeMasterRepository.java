package com.amarasiricoreservice.Repository;

import com.amarasiricoreservice.entity.LeaseeMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeaseeMasterRepository extends JpaRepository<LeaseeMaster,Integer> {
    LeaseeMaster findByLeaseeId(Integer leaseeId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
            "FROM LeaseeMaster LM " +
            "JOIN LeaseMaster L ON LM.LeaseeKey = L.LeaseeKey " +
            "WHERE LM.IdentificationNumber = :identificationNumber " +
            "AND L.IsLeaseClosed = 0", nativeQuery = true)
    Integer existsByCondition(@Param("identificationNumber") String identificationNumber);


//    LeaseeMaster findBy
}
