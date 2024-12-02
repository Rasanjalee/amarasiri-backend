package com.amarasiricoreservice.service;

import com.amarasiricoreservice.Repository.ReducingBalanceInterestRepo;
import com.amarasiricoreservice.entity.ReducingBalanceInterests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReducingBalanceInterestService {
    @Autowired
    ReducingBalanceInterestRepo reducingBalanceInterestRepo;

    public ReducingBalanceInterests findTopReducingBalanceInterestByChargableDte(Integer leaseKey){
        return reducingBalanceInterestRepo.getTopByLeaseKeyOrdOrderByChargableDateDesc(leaseKey);
    }
}
