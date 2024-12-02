package com.amarasiricoreservice.service;

import com.amarasiricoreservice.DTO.PostLeaseDetailDto;
import com.amarasiricoreservice.Repository.LeaseeMasterRepository;
import com.amarasiricoreservice.Repository.LeasemasterRepository;
import com.amarasiricoreservice.Repository.VehicleRepository;
import com.amarasiricoreservice.entity.LeaseMaster;
import com.amarasiricoreservice.entity.LeaseeMaster;
import com.amarasiricoreservice.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Transactional
public class LeaseeMasterService {
    @Autowired
    LeaseeMasterRepository leaseeMasterRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    LeasemasterRepository leasemasterRepository;

    public LeaseMaster saveLeaseeMaster(PostLeaseDetailDto postLeaseDetailDto) {

        List<Vehicle> vehicleList = new ArrayList<>();

        System.out.println(vehicleList);
        vehicleList = vehicleRepository.findAllByVehicleNumber(postLeaseDetailDto.getVehicle().getVehicleNumber());
        AtomicBoolean found = new AtomicBoolean(false);
        if (!vehicleList.isEmpty()) {
            vehicleList
                    .stream()
                    .forEach(vehicle -> {
                        leasemasterRepository.findAllByLeaseVehicleKey(vehicle.getLeaseeVehicleKey())
                                .stream()
                                .forEach(leaseMaster -> {
                                    if(leaseMaster.getIsLeaseClosed().equals(0)) {
                                        found.set(true);
                                    }
                                });
                    });
        }

//
        System.out.println(found.get());
        if(!found.get()){
//        if(postLeaseDetailDto!=null){
           new LeaseeMaster();
            Vehicle insertedVehicle = new Vehicle();
            LeaseMaster insertedLeaseMaster = new LeaseMaster();
            if(postLeaseDetailDto.getLeaseeMaster()!=null){
                System.out.println(postLeaseDetailDto.getLeaseeMaster().getFirstName());
                LeaseeMaster insertedLeaseeMaster= leaseeMasterRepository.save(postLeaseDetailDto.getLeaseeMaster());
                if(postLeaseDetailDto.getVehicle()!=null){
                    postLeaseDetailDto.getVehicle().setLeaseeId(insertedLeaseeMaster.getLeaseeId());
                    insertedVehicle = vehicleRepository.save(postLeaseDetailDto.getVehicle());
                }
                if (postLeaseDetailDto.getLeaseMaster()!=null){
                    postLeaseDetailDto.getLeaseMaster().setLeaseeKey(insertedLeaseeMaster.getLeaseeId());
                    postLeaseDetailDto.getLeaseMaster().setLeaseID(generateLeaseId(postLeaseDetailDto.getLeaseMaster().getLeaseStartDate()));
                    postLeaseDetailDto.getLeaseMaster().setLeaseVehicleKey(insertedVehicle.getLeaseeVehicleKey());
                    postLeaseDetailDto.getLeaseMaster().setIsHidden(0);
                    insertedLeaseMaster = leasemasterRepository.save(postLeaseDetailDto.getLeaseMaster());
                }
            }
            return  insertedLeaseMaster;
        } else {
            return new LeaseMaster();
        }

    }
    private String generateLeaseId(Date leaseStartDate) {
        System.out.println(leaseStartDate);
        LocalDate currentDate = leaseStartDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        String datePart = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Fetch the latest counter for the current date
        int incrementingNumber = leasemasterRepository.countByLeaseIdStartingWith("LS"+datePart);

        // Increment the counter for the current date
        incrementingNumber++;

        // Format the incrementing number as three digits
        String incrementingPart = String.format("%03d", incrementingNumber);

        // Concatenate the parts to create the custom ID
        return "LS" + datePart + incrementingPart;
    }

    public List<LeaseMaster> getLeasingDetails(Integer openStatus) {
        if (openStatus == 2) {
            return leasemasterRepository.findAll();
        } else {
            return leasemasterRepository.findByIsLeaseClosed(openStatus);
        }
    }

    public LeaseeMaster getLeaseeDetails(int LeaseeId){
        return leaseeMasterRepository.findByLeaseeId(LeaseeId);
    }



    public LeaseMaster getLeaseDetail(int leaseId){

        LeaseMaster leaseMaster = leasemasterRepository.findById(leaseId).get();
        return leaseMaster;
    }


    public LeaseMaster updatePayment(Integer leaseId, LocalDate nextPaymentDate, Double remainingCapital, Double remainingLeaseAmount, Double totalLeaseCost, Double totalInterestCollected,
                                     Double remainingTotalLeaseCostForLastPayment,
                                     Double remaianigTotalInterestForLastPayment,
                                     Integer duration,
                                     Integer isPaymentOutDated,
                                     Double currentOutStandingBalance,
                                     Double nextPaymentDateOutStandingBalance,
                                     Integer lastPaidInstallmentIndex,
                                     Integer closedUserKey,
                                     Double closingAmount,
                                     Double closingInterest,
                                     Double closingCalculatedInterestAmount,
                                     Double closingCapitalAmount,
                                     Date closedDateTime) {

        Date nextPayDate = java.util.Date.from(nextPaymentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());


        LeaseMaster lease = getLeaseDetail(leaseId);

        lease.setRemainingCapial(remainingCapital);
        lease.setNextPaymentDate(nextPayDate);
        lease.setRemainingLeaseAmount(remainingLeaseAmount);
        lease.setTotalLeaseCost(totalLeaseCost);
        lease.setTotalInterestCollected(totalInterestCollected);
        lease.setRemainingTotalLeaseCostForLastPayment(remainingTotalLeaseCostForLastPayment);
        lease.setRemainingTotalInterestForLastPayment(remaianigTotalInterestForLastPayment);
        lease.setPanneltyDuration(duration);
        lease.setIsPaymentOutDated(isPaymentOutDated);
        lease.setCurrentOutStandingBalance(currentOutStandingBalance);
        lease.setNextPaymentDateOutStandingBalance(nextPaymentDateOutStandingBalance);
        lease.setLastPaidInstallmentIndex(lastPaidInstallmentIndex);
        lease.setClosedUserKey(closedUserKey);
        lease.setClosingAmount(closingAmount);
        lease.setClosingInterest(closingInterest);
        lease.setClosingCalculatedInterestAmount(closingCalculatedInterestAmount);
        lease.setClosingAmount(closingCapitalAmount);
        lease.setClosedDateTime(closedDateTime);
        return leasemasterRepository.save(lease);

    }

    public Boolean getAllLeasee(String nic) {
        return leaseeMasterRepository.existsByCondition(nic)==1?Boolean.TRUE:Boolean.FALSE;

    }

    public LeaseMaster updateLease(LeaseMaster lease){
        return leasemasterRepository.save(lease);
    }


//    public LeaseeMaster updateLeaseeMaster(LeaseeMaster leaseeMaster){
//         return leaseeMasterRepository.save(leaseeMaster);
//    }
    public void updateLeaseMasterWithPenaltyPayment(int leaseId,double nextInstallmentPenaltyDuration, double nextInstallmentPenaltyAmount, int isPaymentOutDated, Date nextPaymentDateTime, double currentOutstandingBalance, double remainingLeaseAmount, double remainingPrincipalAmount, double nextPaymentDateOutStandingBalance) {

        LeaseMaster leaseMaster = leasemasterRepository.findByLeaseKey(leaseId);
        leaseMaster.setPenaltyDurationForNextPayment((int)nextInstallmentPenaltyDuration);
        leaseMaster.setPenaltyAmountForNextPayment(nextInstallmentPenaltyAmount);
        leaseMaster.setIsPaymentOutDated(isPaymentOutDated);
        leaseMaster.setNextPaymentDate(nextPaymentDateTime);
        leaseMaster.setCurrentOutStandingBalance(currentOutstandingBalance);

        leaseMaster.setRemainingLeaseAmount(remainingLeaseAmount);

        leaseMaster.setRemainingCapial(remainingPrincipalAmount);
        leaseMaster.setNextPaymentDateOutStandingBalance(nextPaymentDateOutStandingBalance);
        leasemasterRepository.save(leaseMaster);
    }
}
