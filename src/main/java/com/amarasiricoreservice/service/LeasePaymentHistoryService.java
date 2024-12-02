package com.amarasiricoreservice.service;

import com.amarasiricoreservice.Repository.LeasePaymentHisortyRepository;
import com.amarasiricoreservice.entity.LeasePaymentHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LeasePaymentHistoryService {
    @Autowired
    LeasePaymentHisortyRepository  leasePaymentHisortyRepository;

    public LeasePaymentHistory update(LeasePaymentHistory leasePaymentHistory){
       return leasePaymentHisortyRepository.save(leasePaymentHistory);
    }

    public List<LeasePaymentHistory> getLeasePaymentHistory(int leaseKey){
        return leasePaymentHisortyRepository.findAllByLeaseKey(leaseKey);
    }

    public LeasePaymentHistory savePaymentHistory(int leaseId, Date paymentDateTime,Double paymentAmount,int receivedUserKey,Date receivedDateTime){
        LeasePaymentHistory leasePaymentHistory = new LeasePaymentHistory();
        leasePaymentHistory.setLeaseKey(leaseId);
        leasePaymentHistory.setPaymentAmount(paymentAmount);
        leasePaymentHistory.setPaymentdateTime(paymentDateTime);
        leasePaymentHistory.setReceiveddatetime(receivedDateTime);
        leasePaymentHistory.setReceiveduserKey(receivedUserKey);
        return leasePaymentHisortyRepository.save(leasePaymentHistory);
    }

    public Double getTotalCollection(Date startDate, Date endDate) {
       return leasePaymentHisortyRepository.sumPaymentAmountByPaymentDateTimeBetween(startDate,endDate);
    }

//    public void savePaymentHistory(Integer leaseId, Double amount, Integer userKey) {
//    }
}
