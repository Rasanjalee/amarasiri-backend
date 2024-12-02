package com.amarasiricoreservice.service;

import com.amarasiricoreservice.DTO.LeaseAppSettingResponse;
import com.amarasiricoreservice.Repository.LeaseAppSettingsRepository;
import com.amarasiricoreservice.Repository.UserRepository;
import com.amarasiricoreservice.entity.LeaseAppSettings;
import com.amarasiricoreservice.entity.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class LeaseAppSettingsService {

    @Autowired
    LeaseAppSettingsRepository leaseAppSettingsRepository;

    @Autowired
    UserRepository userRepository;
    public LeaseAppSettings getLeaseAppSettingsData() {
        LeaseAppSettings leaseAppSettings = leaseAppSettingsRepository.findAll().get(0);

//        LeaseAppSettingResponse leaseAppSettingResponse = new LeaseAppSettingResponse();
//
//        leaseAppSettingResponse.setDocumentCharge(leaseAppSettings.getDocumentChange());
//        leaseAppSettingResponse.setEarlierSettlementInterest(leaseAppSettings.getEarlySettlementInterest());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

//        leaseAppSettingResponse.setLastModifiedDateTime(dateFormat.format(leaseAppSettings.getModifiedDateTime()));
//        leaseAppSettingResponse.setMonthlyInterest(leaseAppSettings.getMonthlyInterest());
//        leaseAppSettingResponse.setVisitCharge(leaseAppSettings.getVisitCharge());
//        leaseAppSettingResponse.setLatePaymentInterest(leaseAppSettings.getLatePaymentInterest());
//        leaseAppSettingResponse.setModifiedUserName(leaseAppSettings.getUser().getFirstName()+" "+leaseAppSettings.getUser().getLastName());
//
//        return leaseAppSettingResponse;
        return leaseAppSettings;
    }

    public LeaseAppSettings updateLeaseAppSettings(LeaseAppSettingResponse leaseAppSettingResponse){
        LeaseAppSettings leaseAppSettings = leaseAppSettingsRepository.findAll().get(0);

        leaseAppSettings.setDocumentCharge(leaseAppSettingResponse.getDocumentCharge());
        leaseAppSettings.setEarlySettlementInterest(leaseAppSettingResponse.getEarlierSettlementInterest());
        leaseAppSettings.setModifiedDateTime(new Date());
        leaseAppSettings.setMonthlyInterest(leaseAppSettingResponse.getMonthlyInterest());
        leaseAppSettings.setVisitCharge(leaseAppSettingResponse.getVisitCharge());
        leaseAppSettings.setLatePaymentInterest(leaseAppSettingResponse.getLatePaymentInterest());
//        leaseAppSettings.
        UserMaster userMaster = userRepository.findByUserKey(leaseAppSettingResponse.getUserKey()).get();
        leaseAppSettings.setUser(userMaster);

        return leaseAppSettingsRepository.save(leaseAppSettings);

    }
}
