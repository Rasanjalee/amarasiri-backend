package com.amarasiricoreservice.service;

import com.amarasiricoreservice.DTO.*;
import com.amarasiricoreservice.Repository.LeasemasterRepository;
import com.amarasiricoreservice.Repository.VehicleRepository;
import com.amarasiricoreservice.Response.MessageResponse;
import com.amarasiricoreservice.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class MainService {
    @Autowired
    LeaseGurantorService leaseGurantorService;

    @Autowired
    LeasemasterRepository leasemasterRepository;

    @Autowired
    LeaseInstallmentService leaseInstallmentService;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    LeaseDocumentService  leaseDocumentService;

    @Autowired
    LeaseeMasterService leaseeMasterService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    LeasePaymentHistoryService leasePaymentHistoryService;

    @Autowired
    LeasePaymentBreakDownService leasePaymentBreakDownService;

    @Autowired
    UserService userMasterService;

    @Autowired
    ReducingBalanceInterestService reducingBalanceInterestService;

    @Autowired
    UserGroupService userGroupService;

    @Autowired
    LeaseAppSettingsService leaseAppSettingsService;

    public List<UserMaster> getAllUsers(){
       return  userMasterService.getUsers();
    }

    public void deleteUser(Integer userKey){
        userMasterService.deleteUser(userKey);
    }

    public void addUserGroup(String userGroupName,Integer userKey){
        System.out.println("add user group``");
        userGroupService.add(userGroupName,userKey);
    }
    public List<UserGroups> getAllUserGroups(){
        return userGroupService.findAll();
    }

    public void deleteUserGroup(Integer userGroupKey){
        userGroupService.deleteUserGroup(userGroupKey);
    }

    public UserGroups editUserGroup(String userGroupName,Integer userGroupKey,Integer userKey){
        return userGroupService.editUserGroup(userGroupName,userGroupKey,userKey);
    }
    public  List<PostLeaseDetailDto> getLeaseDetails(Integer openStatus){
        List<PostLeaseDetailDto> laseDetails = new ArrayList<>();
        leaseeMasterService.getLeasingDetails(openStatus).stream().forEach(
                leaseMaster -> {
                    PostLeaseDetailDto postLeaseDetailDto = new PostLeaseDetailDto();
                    postLeaseDetailDto.setLeaseMaster(leaseMaster);
                    postLeaseDetailDto.setLeasePaymentHistories(leasePaymentHistoryService.getLeasePaymentHistory(leaseMaster.getLeaseKey()));
                    postLeaseDetailDto.setLeaseGurantors(leaseGurantorService.getLeaseGurantors(leaseMaster.getLeaseKey()));
                    postLeaseDetailDto.setLeaseeMaster(leaseeMasterService.getLeaseeDetails(leaseMaster.getLeaseeKey()));
                    postLeaseDetailDto.setVehicle(vehicleService.getVehicles(leaseMaster.getLeaseVehicleKey()));
                    postLeaseDetailDto.setLeaseDocuments(leaseDocumentService.getDocs(leaseMaster.getLeaseKey()));
                    laseDetails.add(postLeaseDetailDto);

                }
        );
        return laseDetails;
    }

    public Map<String,Double> calculateReducingBalanceInterestRate(LeaseMaster lease,LeaseAppSettings leaseAppSettings){
        Map<String,Double> values = new HashMap<>();
        values.put("interestForTodayDB",Double.valueOf(-1));
        values.put("interestRateDB" ,Double.valueOf(0));
        values.put("numberOfInterestChargedDB ", Double.valueOf(0));
        values.put("principalAmountDB  ", Double.valueOf(0));
        values.put("statusDB ",Double.valueOf(0));
        try{
            double interestChangeDelayDuration = leaseAppSettings.getInterestChangeDelayDuration();
            double delayInterestCharge = leaseAppSettings.getDelayInterestCharge();


            Date leaseProccessedDateTime = lease.getLeaseProcessDateTime();
            Date interestCalcualtionStartDate = leaseProccessedDateTime;
            double interestRate = lease.getAnnualInterestRate();
            double remainingCapial = lease.getRemainingCapial();

            if(reducingBalanceInterestService.findTopReducingBalanceInterestByChargableDte(lease.getLeaseKey())!=null) {
                interestCalcualtionStartDate = reducingBalanceInterestService.findTopReducingBalanceInterestByChargableDte(lease.getLeaseKey()).getChargableDate();
            } // Convert Date to LocalDate

            double numberOfDays = (double) Math.ceil(Math.abs(ChronoUnit.DAYS.between(LocalDateTime.now(), interestCalcualtionStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(leaseProccessedDateTime);
            calendar.add(Calendar.DATE,30);
            if(leaseProccessedDateTime.before(new Date())&& calendar.getTime().after(new Date())){
                numberOfDays = (double) Math.ceil(Math.abs(ChronoUnit.DAYS.between(calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), interestCalcualtionStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())));
            }

            if (interestChangeDelayDuration <= numberOfDays)
            {
                interestRate = delayInterestCharge;
            }


            double interestForToday = ((remainingCapial * interestRate) / 3000) * numberOfDays;


            values.put("interestForTodayDB",interestForToday);
            values.put("interestRateDB" ,interestRate);
            values.put("numberOfInterestChargedDB ", numberOfDays);
            values.put("principalAmountDB  ", remainingCapial);
            values.put("statusDB ",Double.valueOf(1));
return values;
        }catch (Exception e){
return values;
        }


    }
    public CalculatedLeaseDto calculateInterest(Integer leaseId, Date paymentReceivedDate) {

        CalculatedLeaseDto calculatedLeaseDto= new CalculatedLeaseDto();
        Double installment = 0.0;
        LeaseMaster lease = leaseeMasterService.getLeaseDetail(leaseId);
        LeaseAppSettings leaseAppSettings = leaseAppSettingsService.getLeaseAppSettingsData();

        /**
         * lease.getLeaseTypeKey().equals(2) means standard equal ones
         */
        if (lease.getLeaseTypeKey().equals(2)) {

            long timeDifference = paymentReceivedDate.getTime() - lease.getNextPaymentDate().getTime();
            // Convert the time difference to days
            int outDatedDayCount = (int) TimeUnit.MILLISECONDS.toDays(timeDifference) + 1;

            Double penaltyAmount = 0.0;
            LeaseInstallment leaseInstallment = leaseInstallmentService.getMaxLeaseInstallmentForLeaseId(leaseId);

                if(outDatedDayCount>0){
                    penaltyAmount = leaseInstallment.getPrincipal()*0.1*outDatedDayCount;
                    installment = penaltyAmount + lease.getInstallment();
                }
                else {
                    installment = lease.getInstallment();
                }

                calculatedLeaseDto.setInterest(installment-leaseInstallment.getPrincipal());
                calculatedLeaseDto.setInstallment(installment);
                calculatedLeaseDto.setOutDatedDaysCount(outDatedDayCount);
                calculatedLeaseDto.setPenaltyAmount(penaltyAmount);
        }
        /**
         * lease.getLeaseTypeKey().equals(3) means linear equal ones
         */
        else if (lease.getLeaseTypeKey().equals(3)) {

            Map<String,Double> values = calculateReducingBalanceInterestRate(lease,leaseAppSettings);

            LocalDate paymentDate = Instant.ofEpochMilli(paymentReceivedDate.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LinearEqualInstallmentLastPaymentDetails lastPaymentDetails = leaseInstallmentService.lastPaymentDetails(lease.getLeaseKey());

            int daysDifference = (int)ChronoUnit.DAYS.between(lastPaymentDetails.getLastPaymentDate(), paymentDate);

            if (daysDifference <= 50) {
                installment = lastPaymentDetails.getRemainingCapital() * lastPaymentDetails.getMonthInterestRate() / (30 * 100) * daysDifference;
            } else {
                installment = lastPaymentDetails.getRemainingCapital() * daysDifference * 5 /100;
            }
            calculatedLeaseDto.setOutDatedDaysCount(daysDifference);
            calculatedLeaseDto.setInterest(installment);
            calculatedLeaseDto.setInstallment(1000.0+installment);
            calculatedLeaseDto.setOutDatedDaysCount(0);
            calculatedLeaseDto.setPenaltyAmount(0.0);

        } else {
            calculatedLeaseDto.setOutDatedDaysCount(0);
            calculatedLeaseDto.setInterest(0.0);
            calculatedLeaseDto.setInstallment(0.0);
            calculatedLeaseDto.setOutDatedDaysCount(0);
            calculatedLeaseDto.setPenaltyAmount(0.0);
        }

        return calculatedLeaseDto;






    }



//    public void calculateLease(double valuation, int leaseType, int duration, double monthlyInterest, double docCharge, double visitCharge, Date startDate, Date processDateTime){
//
//        Map<String, Object> docv = new HashMap<>();
//
//        // Convert string values to respective data types
//        double mInterest = monthlyInterest;
//        int numberOfMonths = duration;
//        double valuationAMT = valuation;
//        double dCharge = docCharge;
//        double vCharge = visitCharge;
//
//        // Initialize the HashMap with keys and empty values
//        docv.put("Installment", "");
//        docv.put("TotalLoanCost", "");
//        docv.put("TotalInterest", "");
//        docv.put("PanneltyOnStartDate", "");
//        docv.put("NumberOfPanneltyDays", "");
//        docv.put("CashOnHand", "");
//        docv.put("PaymentSchedule", "");
//
//        switch (leaseType){
//            case 1:
//                getStandardEqualInstallmentPayment(monthlyInterest, duration, valuation, startDate, processDateTime, docCharge, visitCharge);
//                break;
//            case 2:
//
//                Map<String, Object> doc = getLinearEqualInstallmentPayment(monthlyInterest, duration, valuation, startDate, processDateTime, docCharge, visitCharge);
//                docv = doc!=null?doc:docv;
//                break;
//            case 3:
//                getReducingBalanceInstallmentPayment(monthlyInterest, duration, valuation, startDate, processDateTime, docCharge, visitCharge);
//                break;
//            default:
//                break;
//        }
//
//    }

//    public static Map<String, Object> getLinearEqualInstallmentPayment(double monthlyInterest, int numberOfMonths,
//                                                                       double valuationAMT, Date startDate, Date processDateTime, double docCharge, double visitCharge) {
//        try {
//            Map<String, Object> docv = new HashMap<>();
//
//            double totalLeaseInterest = monthlyInterest * numberOfMonths;
//            double totalLeaseCost = Math.round(valuationAMT + (valuationAMT * (totalLeaseInterest / 100)));
//            double monthlyInstallment = Math.round(totalLeaseCost / numberOfMonths);
//            double monthlyPrincipal = Math.round(valuationAMT / numberOfMonths);
//            docv.put("Installment", monthlyInstallment);
//
//            double totalInterest = Math.round(totalLeaseCost - valuationAMT);
//            double numberOfPanneltyDays = (startDate.getTime() - processDateTime.getTime()) / (1000 * 60 * 60 * 24);
//            numberOfPanneltyDays = numberOfPanneltyDays < 0 ? 0 : numberOfPanneltyDays;
//            double numberOfLoanDays = (startDate.getMonth() + numberOfMonths - startDate.getMonth()) * 30;
//
//            double perdayInterest = Math.round(totalInterest / numberOfLoanDays);
//
//            double panneltyOnStartDate = Math.round(perdayInterest * numberOfPanneltyDays);
//            docv.put("PanneltyOnStartDate", panneltyOnStartDate);
//            docv.put("NumberOfPanneltyDays", numberOfPanneltyDays);
//            docv.put("CashOnHand",
//                    Math.round(valuationAMT - docCharge - visitCharge - (double) docv.get("PanneltyOnStartDate")));
//
//            docv.put("TotalLoanCost", totalLeaseCost + panneltyOnStartDate);
//            docv.put("TotalInterest", totalInterest + panneltyOnStartDate);
//
//            // Assuming you have a Java equivalent of GetLinearEqualInstallmentPaymentScheduleEx method
//            docv.put("PaymentSchedule", getLinearEqualInstallmentPaymentScheduleEx(panneltyOnStartDate, totalLeaseCost,
//                    valuationAMT, monthlyInstallment, monthlyPrincipal, numberOfMonths, startDate, 0));
//
//            return docv;
//        } catch (Exception ex) {
//            // Handle exception appropriately
//            ex.printStackTrace();
//            return null;
//        }
//    }

    // Define the method getLinearEqualInstallmentPaymentScheduleEx
    // Here you would implement its logic
//    public static Map<String, Object> getLinearEqualInstallmentPaymentScheduleEx(double panneltyOnStartDate,
//                                                                                 double totalLeaseCost, double valuationAMT, double monthlyInstallment, double monthlyPrincipal,
//                                                                                 int numberOfMonths, Date startDate, int startIndex) {
//        Map<String, Object> result = new HashMap<>();
//        result.put("PaymentDate", new Date());
//        result.put("BeginingLeaseCost", totalLeaseCost + panneltyOnStartDate);
//        result.put("BeginningBalance", valuationAMT);
//        result.put("Installment", panneltyOnStartDate);
//        result.put("Principal", 0);
//        result.put("Interest", panneltyOnStartDate);
//        result.put("EndingLeaseBalance", totalLeaseCost);
//        result.put("EndingBalance", valuationAMT);
//        result.put("InstallmentIndex", startIndex);
//
//        Calendar calendar = new GregorianCalendar();
//        calendar.setTime(startDate);
//
//        for (int i = 0; i < numberOfMonths; i++) {
//            double interest = Math.round((valuationAMT * monthlyInstallment) / 100);
//            double principal = monthlyInstallment - interest;
//            double endingBalance = valuationAMT - principal;
//
//            Map<String, Object> row = new HashMap<>();
//            row.put("PaymentDate", calendar.getTime());
//            row.put("BeginingLeaseCost", totalLeaseCost);
//            row.put("BeginningBalance", valuationAMT);
//            row.put("Installment", monthlyInstallment);
//            row.put("Principal", principal);
//            row.put("Interest", interest);
//            row.put("EndingLeaseBalance", totalLeaseCost - monthlyInstallment);
//            row.put("EndingBalance", endingBalance);
//            row.put("InstallmentIndex", startIndex + i + 1);
//
//            result.putAll(row);
//
//            calendar.add(Calendar.MONTH, 1);
//        }
//
//        return result;
//    }





    public LeaseMaster saveLease(PostLeaseDetailDto postLeaseDetailDto) {
        LeaseMaster leaseMaster = leaseeMasterService.saveLeaseeMaster(postLeaseDetailDto);
//        System.out.println(postLeaseDetailDto.getInstallments().get(0));
        if(leaseMaster!=null){
            postLeaseDetailDto.getInstallments()
                    .stream()
                    .forEach(leaseInstallment -> {
                        leaseInstallment.setLeaseId(leaseMaster.getLeaseKey());
                        leaseInstallment.setNumberOfInstallments(postLeaseDetailDto.getInstallments().size());
                        leaseInstallment.setRemainingPrincipalPortion(leaseInstallment.getPrincipal());
                        leaseInstallment.setRemainingInterestPortion(leaseInstallment.getInterest());
                    });
//            System.out.println();
            leaseInstallmentService.saveLeaseInstallments(postLeaseDetailDto.getInstallments());

            postLeaseDetailDto.getLeaseDocuments().stream().forEach(leaseDocuments -> leaseDocuments.setLeaseKey(leaseMaster.getLeaseKey()));
            leaseDocumentService.saveLeaseDocuments(postLeaseDetailDto.getLeaseDocuments());

            postLeaseDetailDto.getLeaseGurantors().stream().forEach(guarantor -> guarantor.setLeaseKey(leaseMaster.getLeaseKey()));
            leaseGurantorService.saveLeaseGuarantor(postLeaseDetailDto.getLeaseGurantors());
        }
        return  leaseMaster;
    }
    public LeaseMaster makePayment(Integer leaseId,Double amount,Integer userKey){
        LeaseMaster lease = leaseeMasterService.getLeaseDetail(leaseId);
        int discountPayment = 0;
        if(lease.getLeaseTypeKey().equals(3)){
            System.out.println("leaseType 3");
            payReducingBalanceLease(amount,leaseId,new Date(),discountPayment,userKey);
        }else{
            System.out.println("OrtherleaseType");
            payOtherLease(amount,leaseId,new Date(),userKey);
        }
        return lease;
    }
    public void payOtherLease(Double amount,Integer leaseId,Date date,Integer userKey){
        List<LeaseInstallment> leaseInstallments= leaseInstallmentService.getRemainLeaseInstallmentOrderByPaymentDate(leaseId);
        LeaseAppSettings leaseAppSettings = leaseAppSettingsService.getLeaseAppSettingsData();
        List<LeaseInstallment> updatedLeaseInstallmentList = new ArrayList<>();
        List<LeasePaymentBreakdown> updatedLeasePaymentBreakDownList= new ArrayList<>();
        double grandTotalCapitalPayied = 0;
        double grandTotalCapitalInterest = 0;
        int isPaymentOutDated = 0;
        int statusCode = 0;
        double balance = amount;

        for (LeaseInstallment leaseInstallment:leaseInstallments) {
            LeaseInstallment leaseInstallment1 = null;
            System.out.println("balance"+balance);
            if(balance<=0)break;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(leaseInstallment.getPaymentDate());
            calendar.add(Calendar.DATE,3);
            if(calendar.getTime().before(new Date())){

                System.out.println("payment Date +3 before the current date");
                isPaymentOutDated =1;
                statusCode = 3;
            }
            else {
                if(leaseInstallment.getPaymentDate().equals(new Date())|| leaseInstallment.getPaymentDate().before(new Date())){
                    statusCode =2;
                    System.out.println("ststus 2");
                }
                else if(leaseInstallment.getPaymentDate().after(new Date())){

                    statusCode=1;
                    System.out.println("ststus 1");

                }
            }
            double paybleAmt = leaseInstallment.getRemainingPrincipalPortion() + leaseInstallment.getRemainingInterestPortion() + leaseInstallment.getPenaltyAmount();
            System.out.println("payblemt"+paybleAmt);
            if(paybleAmt>balance){
                System.out.println("paybleAmt>balance");
                // This mean Leasee is not giving enough money to pay full payment
                Integer ReceivedUserKey = userKey;
                int isLatepayment = isPaymentOutDated == 1 ? 1 : 0;
                double remainingInterestPortion;
                double remainingPrincipalPortion;

                double totalInterestPayied;
                double totalCapitalPayied;
                double tempBalance = balance;
                double interestPayid = 0;
                double capitalPayid = 0;
                if (leaseInstallment.getRemainingInterestPortion() + leaseInstallment.getPenaltyAmount() < tempBalance)
                {
                    remainingInterestPortion = 0;
                    tempBalance = balance - (leaseInstallment.getRemainingInterestPortion() + leaseInstallment.getPenaltyAmount());
                    interestPayid = (leaseInstallment.getRemainingInterestPortion() + leaseInstallment.getPenaltyAmount());
                    remainingPrincipalPortion = leaseInstallment.getRemainingPrincipalPortion() - tempBalance;
                    grandTotalCapitalPayied += tempBalance;
                    capitalPayid = tempBalance;
                    totalInterestPayied = leaseInstallment.getTotalInterestPayied() + leaseInstallment.getRemainingInterestPortion() + leaseInstallment.getPenaltyAmount();
                    grandTotalCapitalInterest += (leaseInstallment.getRemainingInterestPortion() + leaseInstallment.getPenaltyAmount());
                    totalCapitalPayied = leaseInstallment.getTotalCapitalPayied() + tempBalance;

                } else if ((leaseInstallment.getRemainingInterestPortion() + leaseInstallment.getPenaltyAmount()) == tempBalance)
                {
                    remainingInterestPortion = 0;
                    interestPayid = (leaseInstallment.getRemainingInterestPortion()) + leaseInstallment.getPenaltyAmount();
                    grandTotalCapitalInterest += ((leaseInstallment.getRemainingInterestPortion()) + leaseInstallment.getPenaltyAmount());
                    remainingPrincipalPortion = leaseInstallment.getRemainingPrincipalPortion();
                    totalInterestPayied = (leaseInstallment.getTotalInterestPayied()) + (leaseInstallment.getRemainingInterestPortion() + leaseInstallment.getPenaltyAmount());
                    totalCapitalPayied = leaseInstallment.getTotalCapitalPayied() + 0;
                    capitalPayid = 0;
                }
                else
                {
                    grandTotalCapitalInterest += tempBalance;
                    remainingInterestPortion = leaseInstallment.getRemainingInterestPortion() + leaseInstallment.getPenaltyAmount() - tempBalance;
                    remainingPrincipalPortion = leaseInstallment.getRemainingPrincipalPortion();
                    totalInterestPayied = leaseInstallment.getTotalInterestPayied() + tempBalance;
                    totalCapitalPayied = leaseInstallment.getTotalCapitalPayied() + 0;
                    capitalPayid = 0;
                    interestPayid = tempBalance;
                }

                updatedLeaseInstallmentList.add( leaseInstallmentService.updatedInstallmentForOtherTypes( isPaymentOutDated,4,leaseInstallment.getPenaltyInterestRate(),leaseInstallment.getPenaltyAmount(),leaseInstallment.getPenaltyDuration(),0,leaseInstallment.getPaidAmount()+balance,paybleAmt-balance,leaseInstallment.getLeaseInstallmentId(),isLatepayment,1,paybleAmt-balance,totalInterestPayied,totalCapitalPayied,remainingInterestPortion,remainingPrincipalPortion,userKey));

                updatedLeasePaymentBreakDownList.add(leasePaymentBreakDownService.updateLeasePaymentBreakdown(leaseInstallment.getLeaseInstallmentId(),leaseInstallment.getInstallment(),leaseInstallment.getPaidAmountBalance(),leaseInstallment.getPenaltyAmount(),leaseInstallment.getPenaltyDuration(),leaseInstallment.getPenaltyInterestRate(),leaseInstallment.getLastPenaltyCalculatedDateTime(),interestPayid,capitalPayid));
            }
            else{
                System.out.println("balance"+paybleAmt);
                // This mean there is enough or extra money for payment
                balance = balance - paybleAmt; // This will get the rest of the balance from paying amount
                Integer ReceivedUserKey = userKey;
                int isLatepayment = isPaymentOutDated == 1 ? 1 : 0;

                double remainingInterestPortion = 0;
                double remainingPrincipalPortion = 0;
                double totalInterestPayied = 0;
                double totalCapitalPayied = 0;
                totalCapitalPayied = (leaseInstallment.getTotalCapitalPayied()) + leaseInstallment.getRemainingPrincipalPortion();
                grandTotalCapitalPayied += leaseInstallment.getRemainingPrincipalPortion();
                totalInterestPayied = leaseInstallment.getTotalInterestPayied() + leaseInstallment.getRemainingInterestPortion() + leaseInstallment.getPenaltyAmount();
                grandTotalCapitalInterest += leaseInstallment.getRemainingInterestPortion() + leaseInstallment.getPenaltyAmount();

               updatedLeaseInstallmentList.add( leaseInstallmentService.updatedInstallmentForOtherTypes(0,statusCode,leaseInstallment.getPenaltyInterestRate(),leaseInstallment.getPenaltyAmount(),leaseInstallment.getPenaltyDuration(),1,leaseInstallment.getPaidAmount()+paybleAmt,0,leaseInstallment.getLeaseInstallmentId(),isLatepayment,0,0.0,totalInterestPayied,totalCapitalPayied,remainingInterestPortion,remainingPrincipalPortion,userKey));
               updatedLeasePaymentBreakDownList.add(leasePaymentBreakDownService.updateLeasePaymentBreakdown(leaseInstallment.getLeaseInstallmentId(),leaseInstallment.getInstallment(),leaseInstallment.getPaidAmountBalance(),leaseInstallment.getPenaltyAmount(),leaseInstallment.getPenaltyDuration(),leaseInstallment.getPenaltyInterestRate(),leaseInstallment.getLastPenaltyCalculatedDateTime(),leaseInstallment.getRemainingInterestPortion()+leaseInstallment.getPenaltyAmount(),leaseInstallment.getRemainingPrincipalPortion()));

            }



        }
        for (LeaseInstallment leaseInstallment:updatedLeaseInstallmentList) {
            leaseInstallmentService.save(leaseInstallment);
        }
        LeasePaymentHistory leasePaymentHistory = leasePaymentHistoryService.savePaymentHistory(leaseId,new Date(),amount,userKey,new Date());


        for (LeasePaymentBreakdown leasePaymentBreakDown:updatedLeasePaymentBreakDownList) {
            leasePaymentBreakDown.setPaymentKey(leasePaymentHistory.getPaymentkey());
            leasePaymentBreakDownService.save(leasePaymentBreakDown);

        }
        LeaseMaster lease = leaseeMasterService.getLeaseDetail(leaseId);
        double remainingTotalLeaseCostForLastPayment = lease.getRemainingTotalLeaseCostForLastPayment();
        double remainingCapitalForToday=lease.getRemainingCapitalForToday();
        lease.setRemainingTotalInterestForLastPayment(lease.getRemainingTotalInterestForLastPayment()-grandTotalCapitalInterest);
        lease.setRemainingCapitalForToday(remainingCapitalForToday-grandTotalCapitalPayied);
        lease.setRemainingTotalLeaseCostForLastPayment(remainingTotalLeaseCostForLastPayment-amount);
        lease.setTotalInterestCollected(lease.getTotalInterestCollected()+grandTotalCapitalInterest);

        calculateNextPaymentAndPenaltyForScheduleForOtherLeaseTypes(leaseId,leaseAppSettings,userKey);





    }

    private void calculateNextPaymentAndPenaltyForScheduleForOtherLeaseTypes(Integer leaseId, LeaseAppSettings leaseAppSettings, Integer userKey) {


        try{
            double latepaymentInterest = leaseAppSettings.getLatePaymentInterest();
            LeaseInstallment leaseInstallment = leaseInstallmentService.getRemainLeaseInstallmentOrderByPaymentDate(leaseId).get(0);

            Date startDate = new Date();

            if(leaseInstallment!=null) {
                if (leaseInstallment.getPaymentDate().after(startDate))
                {
                    startDate = leaseInstallment.getPaymentDate();
                }
                else
                {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(startDate);
                    calendar.add(Calendar.MONTH,1);
                    startDate = calendar.getTime();
                }
            }else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                calendar.add(Calendar.MONTH,1);
                startDate = calendar.getTime();
            }
            List<LeaseInstallment> leaseInstallmentList = leaseInstallmentService.findLeaseInstallments(startDate,leaseId);

            double partPaymentBalance = 0;
            double nextInstallmentPenaltyDuration = 0;
            double nextInstallmentPenaltyAmount = 0;
            int isPaymentOutDated = 0;
            double nextPaymentDateOutStandingBalance = 0;
            double currentOutstandingBalance = 0;
            double remainingPrincipalAmount = 0;
            double remainingLeaseAmount = 0;
            Date nextPaymentDateTime = new Date();
            int c = 0;
            boolean isoVerPayOut = false;

            List<LeaseInstallment> leaseInstallmentList1 = new ArrayList<>();
            for (LeaseInstallment leaseInstallmentObj:leaseInstallmentList) {

                c++;
                Date paymentDateTime = leaseInstallmentObj.getPaymentDate();
                int statusCode = 0;
                double installmentPenaltyDuration = 0;
                double installmentPenaltyAmount = 0;

                if(leaseInstallmentObj.getIsPartPayment().equals(1)){
                    partPaymentBalance = leaseInstallmentObj.getPaidAmountBalance();
                    installmentPenaltyDuration = ChronoUnit.DAYS.between(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() , paymentDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

                    installmentPenaltyDuration = installmentPenaltyDuration < 0 ? 0 : installmentPenaltyDuration;
                    installmentPenaltyAmount = (partPaymentBalance * installmentPenaltyDuration * latepaymentInterest) / (100 * 30);
                    isPaymentOutDated = paymentDateTime.before( new Date()) ? 1 : 0;
                    nextPaymentDateOutStandingBalance += installmentPenaltyAmount + partPaymentBalance;
                    if (paymentDateTime.before(new Date()) || paymentDateTime.equals(new Date()))
                    {
                        currentOutstandingBalance = nextPaymentDateOutStandingBalance;
                    }
                    else
                    {
                        currentOutstandingBalance = 0;// nextPaymentDateOutStandingBalance - installmentPenaltyAmount;
                    }
                    nextPaymentDateTime = paymentDateTime;
                    nextInstallmentPenaltyAmount += installmentPenaltyAmount;
                    if (c == 1)
                    {
                        nextInstallmentPenaltyDuration = installmentPenaltyDuration;
                        remainingLeaseAmount = leaseInstallmentObj.getEndingLeaseBalance();
                        remainingPrincipalAmount = leaseInstallmentObj.getEndingBalance();
                    }
                    statusCode = 4;
                }else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(paymentDateTime);
                    calendar.add(Calendar.DATE,3);
                    // This is for up coming payments. As per the requirement 3 days are interest free
                    if (calendar.getTime().after(new Date()) || calendar.getTime().equals(new Date()))
                    {
                        nextPaymentDateOutStandingBalance += leaseInstallmentObj.getInstallment();
                        currentOutstandingBalance = nextPaymentDateOutStandingBalance - leaseInstallmentObj.getInstallment();
                        nextPaymentDateTime = paymentDateTime;
                        nextInstallmentPenaltyAmount += 0;
                        installmentPenaltyDuration = 0;
                        isPaymentOutDated = paymentDateTime.before(new Date())  ? 1 : 0;
                        installmentPenaltyAmount = 0;
                        statusCode = 5;
                        if (c == 1)
                        {
                            nextInstallmentPenaltyDuration = 0;
                            remainingLeaseAmount = leaseInstallmentObj.getBeginingLeaseCost();
                            remainingPrincipalAmount = leaseInstallmentObj.getBeginingBalance();
                        }
                    }else
                    {
                        // This mean payment is not done on time
                        double installment = leaseInstallmentObj.getInstallment();
                        installmentPenaltyDuration = ChronoUnit.DAYS.between(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),paymentDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                        nextInstallmentPenaltyAmount += (installment * latepaymentInterest * installmentPenaltyDuration) / (100 * 30);
                        //totalPanneltyCalculated += nextInstallmentPenaltyAmount;
                        nextPaymentDateOutStandingBalance += leaseInstallmentObj.getInstallment() + (installment * latepaymentInterest * installmentPenaltyDuration) / (100 * 30);
                        currentOutstandingBalance = nextPaymentDateOutStandingBalance;
                        nextPaymentDateTime = paymentDateTime;
                        isPaymentOutDated = 1;
                        installmentPenaltyAmount = (installment * latepaymentInterest * installmentPenaltyDuration) / (100 * 30);
                        statusCode = 3;
                        if (c == 1)
                        {
                            nextInstallmentPenaltyDuration = installmentPenaltyDuration;
                            remainingLeaseAmount = leaseInstallmentObj.getBeginingLeaseCost();
                            remainingPrincipalAmount = leaseInstallmentObj.getBeginingBalance();
                        }
                    }


                }

                if (isPaymentOutDated == 1) isoVerPayOut = true;
                leaseInstallmentList1.add(leaseInstallmentService.updatedInstallmentForOtherTypesWithPenalty(isPaymentOutDated,statusCode,latepaymentInterest,installmentPenaltyAmount,installmentPenaltyDuration,leaseInstallmentObj.getLeaseInstallmentId()));

                leaseeMasterService.updateLeaseMasterWithPenaltyPayment(leaseId,nextInstallmentPenaltyDuration,nextInstallmentPenaltyAmount,isoVerPayOut ? 1 : 0,nextPaymentDateTime,currentOutstandingBalance,remainingLeaseAmount,remainingPrincipalAmount,nextPaymentDateOutStandingBalance);


                for (LeaseInstallment leaseInstallment1:leaseInstallmentList1) {

                    leaseInstallmentService.save(leaseInstallment1);
                }

            }


        }
        catch (Exception e){
            System.out.println("Payment Schedule cannot be found, please contact administrator");

        }


    }

    public void payReducingBalanceLease(Double amount,Integer leaseId,Date date,Integer discountPayment,Integer userKey){
        LeaseMaster lease = leaseeMasterService.getLeaseDetail(leaseId);
        LeaseAppSettings leaseAppSettings = leaseAppSettingsService.getLeaseAppSettingsData();

        Map<String,Double> values = calculateReducingBalanceInterestRate(lease,leaseAppSettings);
        if(values.get("status").equals(0)){

        }

        double remainingCapitalAmount = values.get("PrincipalAmount");
        double interestAmountForToday = values.get("InterestForToday");

        double payingCapitalPortion = amount - interestAmountForToday > 0 ? amount - interestAmountForToday : 0;
        double payingInterestPortion = amount - interestAmountForToday > 0 ? interestAmountForToday : amount;
        remainingCapitalAmount = payingCapitalPortion > 0 ? remainingCapitalAmount - payingCapitalPortion : remainingCapitalAmount;

        recordLastPaymentDetailsForReducingBalance(leaseId, amount, payingInterestPortion, values,date, discountPayment,userKey);

        //update PaymentHistory table
        LeasePaymentHistory leasePaymentHistory = new LeasePaymentHistory();
        leasePaymentHistory.setLeaseKey(leaseId);
        leasePaymentHistory.setPaymentdateTime(new Date());
        leasePaymentHistory.setPaymentAmount(amount);
        leasePaymentHistory.setReceiveduserKey(userKey);
        leasePaymentHistory.setReceiveddatetime(new Date());
        leasePaymentHistory.setIsdiscountpayment(discountPayment);
        Integer paymentKey = leasePaymentHistoryService.update(leasePaymentHistory).getPaymentkey();

        //update paymentBreakdown

        LeasePaymentBreakdown leasePaymentBreakdown = new LeasePaymentBreakdown();
        leasePaymentBreakdown.setPaymentKey(paymentKey);
        leasePaymentBreakdown.setInstallmentKey(0);
        leasePaymentBreakdown.setBaseInstallment(0.0);
        leasePaymentBreakdown.setDueinstalment(0.0);
        leasePaymentBreakdown.setIsDiscountPayment(0);
        leasePaymentBreakdown.setPenaltyAmount(interestAmountForToday);
        leasePaymentBreakdown.setPenatyDuration(values.get("numberOfDaysInterestCharged").intValue());
        leasePaymentBreakdown.setInterestRate(values.get("interestRate"));
        leasePaymentBreakdown.setLastPenaltyCalculatedDateTime(new Date());
        leasePaymentBreakdown.setTransactionDateTime(new Date());
        leasePaymentBreakdown.setInterestPortion(payingInterestPortion);
        leasePaymentBreakdown.setCapitalPortion(payingCapitalPortion);
        leasePaymentBreakdown.setIsDiscountPayment(discountPayment);
        leasePaymentBreakDownService.update(leasePaymentBreakdown);

        double principalInterestAddion = payingCapitalPortion == 0 ? interestAmountForToday - amount : 0;

        if(amount==0){
            principalInterestAddion =0;
        }
        lease.setRemainingTotalInterestForLastPayment(0.0);
        lease.setRemainingCapitalForToday(remainingCapitalAmount+principalInterestAddion);
        lease.setRemainingTotalLeaseCostForLastPayment(remainingCapitalAmount+principalInterestAddion);
        lease.setTotalInterestCollected(lease.getTotalInterestCollected()+payingInterestPortion);
        lease.setRemainingCapial(remainingCapitalAmount+principalInterestAddion);
        leaseeMasterService.updateLease(lease);
        calculateNextPaymentAndInterestForRducingPattern(lease,leaseAppSettings,userKey);
    }
    public void calculateNextPaymentAndInterestForRducingPattern(LeaseMaster lease,LeaseAppSettings leaseAppSettings,Integer userKey){
        int isPaymentOutdated = 1;
        Map<String,Double> values = calculateReducingBalanceInterestRate(lease,leaseAppSettings);
        if(values.get("status").equals(0)){

        }
        double leaseCostForToday = values.get("interestForToday") + values.get("principalAmount"); // 5th index

        leaseInstallmentService.leaseInstallmentRepository.mergeLeaseInstallments(lease.getLeaseKey(),values.get("interestRate"),values.get("principalAmount"),values.get("numberOfDaysInterestCharged").intValue(),values.get("interestForToday"),leaseCostForToday,userKey);

        lease.setPenaltyDurationForNextPayment(values.get("numberOfDaysInterestCharged").intValue());
        lease.setPenaltyAmountForNextPayment(values.get("interestForToday"));
        lease.setCurrentOutStandingBalance(leaseCostForToday);
        lease.setNextPaymentDateOutStandingBalance(leaseCostForToday);
        lease.setNextPaymentDate(new Date());
        lease.setIsPaymentOutDated(isPaymentOutdated);
        lease.setLeaseID(lease.getLeaseID());
        leaseeMasterService.leasemasterRepository.save(lease);

    }

    public void  recordLastPaymentDetailsForReducingBalance(Integer leaseKey, Double payidAmount, Double payidInterestPortion, Map<String, Double> values, Date chargableDateTime, Integer isDiscountPayment,Integer userKey ){
        isDiscountPayment=0;
        ReducingBalanceInterests reducingBalanceInterests = new ReducingBalanceInterests();
        reducingBalanceInterests.setLeaseKey(leaseKey);
        reducingBalanceInterests.setPrincipalAmount(values.get("principalAmount"));
        reducingBalanceInterests.setInterestRate(values.get("interestRate"));
        reducingBalanceInterests.setChargableDate(chargableDateTime);
        reducingBalanceInterests.setInterestAmount(values.get("interestForToday"));
        reducingBalanceInterests.setNumberOfDaysInterestCharged(values.get("numberOfDaysInterestCharged").intValue());
        reducingBalanceInterests.setPaidAmount(payidAmount);
        Double balance= values.get("interestForToday")- payidInterestPortion;
        reducingBalanceInterests.setBalanceAmount(balance);
        reducingBalanceInterests.setIsPartPayment(balance>0?1:0);
        reducingBalanceInterests.setIsPartPaymentDone(1);
        reducingBalanceInterests.setTransactionDateTime(new Date());
        reducingBalanceInterests.setTransactionUserKey(userKey);
        reducingBalanceInterests.setIsDiscountpayment(isDiscountPayment);
        reducingBalanceInterestService.reducingBalanceInterestRepo.save(reducingBalanceInterests);
    }
//    public LeaseMaster makePayment(Integer leaseId, Double installment, Double interest, Date paymentDate, Integer userId,Integer penaltyDuration,Double penaltyAmount) {

//        LeaseMaster lease = leaseeMasterService.getLeaseDetail(leaseId);
//
//        int leaseType = lease.getLeaseTypeKey();
//        LeaseMaster updatedLease = new LeaseMaster();
//        leasePaymentHistoryService.savePaymentHistory(leaseId,paymentDate,installment,userId,new Date());
//        LocalDate nextPaymentDate = Instant.ofEpochMilli(lease.getNextPaymentDate().getTime())
//                .atZone(ZoneId.systemDefault())
//                .toLocalDate().plusMonths(1);
//        Double remainingCapital = lease.getRemainingCapial()-installment-interest;
//        Double remainingLeaseAmount = lease.getRemainingLeaseAmount() - installment-interest;
//        Double totalLeaseCost =  lease.getTotalLeaseCost() - installment - interest;
//        Double totalInterestCollected =  lease.getTotalInterestCollected()+interest;
//
//        Double remainingTotalLeaseCostForLastPayment = lease.getRemainingTotalLeaseCostForLastPayment() - installment - interest;
//        switch (leaseType){
//            case 1:
//                break;
//            case 2:
//                LeaseInstallment leaseInstallment = leaseInstallmentService.updatedInstallment(leaseId,lease.getLastPaidInstallmentIndex(),penaltyDuration,penaltyAmount,paymentDate,userId);
//                LeaseInstallment lastLeaseInstallment = leaseInstallmentService.getLastLeaseInstallmentForLeaseId(leaseId);
//
//                Double remaianigTotalInterestForLastPayment = lease.getRemainingTotalInterestForLastPayment() - interest;;
//                Integer duration =penaltyDuration;
//                Integer isPaymentOutDated = duration>0 ? 1:0;
//                Double currentOutStandingBalance = lease.getCurrentOutStandingBalance()- installment;
//                Double nextPaymentDateOutStandingBalance = lease.getNextPaymentDateOutStandingBalance() - installment;
//                Integer lastPaidInstallmentIndex = lastLeaseInstallment.getInstallmentIndex()>=lease.getLastPaidInstallmentIndex()+1?lease.getLastPaidInstallmentIndex()+1:lastLeaseInstallment.getInstallmentIndex();
//
//                Integer closedUserKey;
//                Double closingAmount;
//                Double closingInterest;
//                Double closingCalculatedInterestAmount;
//                Double closingCapitalAmount;
//                Date closedDateTime;
//                if (lastPaidInstallmentIndex.equals(lastLeaseInstallment.getInstallmentIndex())) {
//                    closingAmount = installment;
//                    closingInterest = interest;
//                    closedUserKey = userId;
//                    closingCapitalAmount = installment - interest;
//                    closingCalculatedInterestAmount = interest;
//                    closedDateTime = paymentDate;
//                } else {
//                    closingAmount = 0.0;
//                    closingInterest = 0.0;
//                    closedUserKey = null;
//                    closingCapitalAmount = 0.0;
//                    closingCalculatedInterestAmount = 0.0;
//                    closedDateTime = null;
//                }
//                updatedLease = leaseeMasterService.updatePayment(leaseId,
//                        nextPaymentDate,
//                        remainingCapital,
//                        remainingLeaseAmount,
//                        totalLeaseCost,
//                        totalInterestCollected,
//                        remainingTotalLeaseCostForLastPayment,
//                        remaianigTotalInterestForLastPayment,
//                        duration,
//                        isPaymentOutDated,
//                        currentOutStandingBalance,
//                        nextPaymentDateOutStandingBalance,
//                        lastPaidInstallmentIndex,
//                        closedUserKey,
//                        closingAmount,
//                        closingInterest,
//                        closingCalculatedInterestAmount,
//                        closingCapitalAmount,
//                        closedDateTime
//                        );
//
//
//                break;
//            case 3:
//                updatedLease = leaseeMasterService.updatePayment(leaseId,nextPaymentDate,remainingCapital,remainingLeaseAmount,totalLeaseCost,totalInterestCollected,0.0,0.0,0,0,0.0,0.0,0,null,0.0,0.0,0.0,0.0,null);
//
//                break;
//            default: break;
//        }
//        return updatedLease;
//    }

    public LeaseAppSettings getLeaseAppSettings() {
        return leaseAppSettingsService.getLeaseAppSettingsData();
    }

    public LeaseAppSettings updateLeseAppSettings(LeaseAppSettingResponse leaseAppSettingResponse){
        System.out.println(leaseAppSettingResponse);
        return leaseAppSettingsService.updateLeaseAppSettings(leaseAppSettingResponse);
    }


    public IncomeReportResponse createIncomeReport(Date startDate, Date endDate) {

        IncomeReportResponse incomeReportResponse = new IncomeReportResponse();

        //first Graph
        incomeReportResponse
                .setTotalSaleActual(leasemasterRepository.sumLeaseAmountByLeaseStartDateBetween(startDate,endDate));
        incomeReportResponse
                .setTotalInterestToCollect(leasemasterRepository.sumTotalInterestByLeaseStartDateBetween(startDate,endDate));

        //second graph
        incomeReportResponse
                .setTotalSaleCapital(leasemasterRepository.sumTotalLeaseCostByLeaseStartDateBetween(startDate,endDate));

        incomeReportResponse.setTotalCollection(leasePaymentHistoryService.getTotalCollection(startDate,endDate));
        incomeReportResponse.setTotalInterest(leasemasterRepository.getTotalInterst(startDate,endDate));

        return incomeReportResponse;
    }

    public List<LeaseMaster> createIncomeReportSummery(Date startDate, Date endDate) {

        return leasemasterRepository.findAllByLeaseStartDateBetween(startDate,endDate);
    }

    public List<OutDatedPaymentResponse> getOutdatedPayments() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        List<OutDatedPaymentResponse> outDatedPaymentResponseList = new ArrayList<>();
        leaseInstallmentService.getOutDatedPayments()
                .stream()
                .forEach(leaseInstallment -> {
                    OutDatedPaymentResponse outDatedPaymentResponse = new OutDatedPaymentResponse();
                    LeaseMaster leaseMaster = leasemasterRepository.findByLeaseKey(leaseInstallment.getLeaseId());
                    outDatedPaymentResponse.setLeaseID(leaseMaster.getLeaseID());
                    outDatedPaymentResponse.setLeaseeName(leaseMaster.getLeaseeMaster().getFirstName()+" "+leaseMaster.getLeaseeMaster().getLastName());
                    outDatedPaymentResponse.setContactNumber(leaseMaster.getLeaseeMaster().getMobileNumber());
                    outDatedPaymentResponse.setOutstandingBalance(leaseMaster.getCurrentOutStandingBalance());
                    outDatedPaymentResponse.setPaymentDate(dateFormat.format(leaseInstallment.getPaymentDate()));
                    long dateDifferenceInMillis = Math.abs(new Date().getTime() - leaseInstallment.getPaymentDate().getTime() );
                    outDatedPaymentResponse.setDelay((int) TimeUnit.DAYS.convert(dateDifferenceInMillis, TimeUnit.MILLISECONDS));

                    if(outDatedPaymentResponse!=null){
                        outDatedPaymentResponseList.add(outDatedPaymentResponse);
                    }

                });

        return outDatedPaymentResponseList;


    }

    public LeaseMaster deletelease(Integer leaseKey,Integer status){
        System.out.println(leaseKey);
        LeaseMaster lease = leaseeMasterService.getLeaseDetail(leaseKey);
        lease.setIsHidden(status);
        System.out.println(lease.getIsHidden());

        return leaseeMasterService.updateLease(lease);
    }

    public void updateUser(UserMaster userMaster) {
        userMasterService.saveUser(userMaster);
    }

    public ResponseEntity updateLeaseeDeatils(Integer leaseId, String homeNumber, String email) {
        LeaseMaster leaseMaster = this.leaseeMasterService.getLeaseDetail(leaseId);
        leaseMaster.getLeaseeMaster().setHomeNumber(homeNumber);
        leaseMaster.getLeaseeMaster().setEmail(email);
//        LeaseeMaster leaseeMaster = this.leaseeMasterService.getLeaseeDetails(leaseId);
//        leaseeMaster.setEmail(email);
//        leaseeMaster.setHomeNumber(homeNumber);

        LeaseMaster updatedLease= this.leaseeMasterService.updateLease(leaseMaster);

        if(updatedLease!=null){
            return ResponseEntity.ok("Successfully Updated");
        }else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Not Updated"));
        }

    }

    public ResponseEntity<String> saveDocuments(DocumentDto documentDto) {

        List<LeaseDocuments > documentsList = new ArrayList<>();

        documentDto.getDocList().stream().forEach(value->{
            LeaseDocuments leaseDocuments = new LeaseDocuments();
            leaseDocuments.setDocumentName(value.getDocumentName());
            leaseDocuments.setDocumentPath(value.getDocumentPath());
            leaseDocuments.setLeaseKey(documentDto.getLeaseKey());
            documentsList.add(leaseDocuments);
        });
        List<LeaseDocuments> savedDocs = leaseDocumentService.saveLeaseDocuments(documentsList);

        System.out.println(savedDocs.size());
        if (savedDocs.size() == documentDto.getDocList().size()){
            return ResponseEntity.ok("Successfully Saved");

        }
        else {
            return ResponseEntity.badRequest().body("Problem with saving");
        }
    }

    public UserMaster getLoggedUserById(Integer userId) {
        return userMasterService.getUserById(userId);
    }
}
