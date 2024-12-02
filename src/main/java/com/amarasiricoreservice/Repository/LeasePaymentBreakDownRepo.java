package com.amarasiricoreservice.Repository;

import com.amarasiricoreservice.entity.LeasePaymentBreakdown;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeasePaymentBreakDownRepo extends JpaRepository<LeasePaymentBreakdown,Integer> {

}
