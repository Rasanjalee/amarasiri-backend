package com.amarasiricoreservice.Repository;

import com.amarasiricoreservice.entity.LeaseDocuments;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaseDocumentRepository extends JpaRepository<LeaseDocuments,Integer> {


    void deleteAllByLeaseKey(Integer leaseKey);

    List<LeaseDocuments> findByLeaseKey(Integer leaseKey);
}
