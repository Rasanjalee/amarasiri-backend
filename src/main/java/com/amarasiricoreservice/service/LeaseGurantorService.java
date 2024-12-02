package com.amarasiricoreservice.service;

import com.amarasiricoreservice.Repository.LeaseGurantorsRepository;
import com.amarasiricoreservice.entity.LeaseGurantors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaseGurantorService {
    @Autowired
    LeaseGurantorsRepository leaseGurantorsRepository;

    public List<LeaseGurantors> getLeaseGurantors (int leaseid){
        return leaseGurantorsRepository.findAllByLeaseKey(leaseid);
    }

    public void saveLeaseGuarantor(List<LeaseGurantors> leaseGuarantors) {
        leaseGurantorsRepository.saveAll(leaseGuarantors);
    }

    public List<LeaseGurantors> editLeaseGuarantor(List<LeaseGurantors> leaseGuarantors ) {
        leaseGurantorsRepository.saveAll(leaseGuarantors);
        return leaseGuarantors;
    }
}
