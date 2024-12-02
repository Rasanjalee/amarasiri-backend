package com.amarasiricoreservice.Repository;

import com.amarasiricoreservice.entity.LeaseGurantors;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaseGurantorsRepository extends JpaRepository<LeaseGurantors, Integer> {
    List<LeaseGurantors> findAllByLeaseKey(int leaseKey);
}
