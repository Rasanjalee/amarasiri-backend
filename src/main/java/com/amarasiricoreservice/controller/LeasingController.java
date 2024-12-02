package com.amarasiricoreservice.controller;

import com.amarasiricoreservice.DTO.*;
import com.amarasiricoreservice.Repository.UserGroupsRepository;
import com.amarasiricoreservice.Repository.UserRepository;
import com.amarasiricoreservice.Request.SignupRequest;
import com.amarasiricoreservice.Response.MessageResponse;
import com.amarasiricoreservice.entity.*;
import com.amarasiricoreservice.service.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*")
@RequestMapping("/leasing")
public class LeasingController {


    @Autowired
    UserRepository userRepository;
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private LeaseeMasterService leaseeMasterService;

    @Autowired
    private DocumentsUploadService documentsUploadService;


    @Autowired
    LeaseDocumentService leaseDocumentService;
    @Autowired
    UserGroupsRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private MainService mainService;

    @Autowired
    LeaseGurantorService guarantorService;

    @PreAuthorize("hasAuthority('User')or hasAuthority('Operator')")
    @RequestMapping(value = "/vehicles", method = RequestMethod.GET)
    @Operation(summary = "Get all the vehicles", description = "Return Vehicle Details", tags = {"Vehicle"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfull opertion"),
            @ApiResponse(responseCode = "400", description = "Vehcile record not found")
    })
    public Vehicle getAllVehicles(@RequestParam(name = "vehicle-id", value = "vehicle-id") int vehicleId) {

        try {

            Vehicle vehicleList = vehicleService.getVehicles(vehicleId);

            if (vehicleList != null) {
                return vehicleList;
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @Operation(summary = "Save", description = "Return saved Details", tags = {"LeaseeMaster"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successfull opertion"),
            @ApiResponse(responseCode = "400", description = "Vehcile record not found")
    })
    @PreAuthorize(" hasAnyAuthority('Operator','Administrator')")
    public LeaseMaster createNewLease(@RequestBody PostLeaseDetailDto postLeaseDetailDto) {
        return mainService.saveLease(postLeaseDetailDto);
    }

    @RequestMapping(value = "/lease/documents/save",method = RequestMethod.POST)
    public ResponseEntity<String> saveDocuments(@RequestBody DocumentDto documentDto){

        return mainService.saveDocuments(documentDto);
    }

    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = "/document-upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadDocumentsRegardingLease(@RequestParam("image") MultipartFile image) throws IOException {

        if (image.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded.");
        }

        // Explicitly get the path to CATALINA_HOME/webapps/uploads
        String catalinaHome = System.getProperty("catalina.home");
        String uploadFolder = catalinaHome + File.separator + "webapps" + File.separator + "uploads";

        try {
            // Pass the upload folder path to the service
            String imageUrl = documentsUploadService.uploadLeaseDocuments(image, uploadFolder);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the image.");
        }
    }

//    @RequestMapping(value = "/document-upload", method = RequestMethod.POST)
//    public ResponseEntity<String> uploadDocumentsRegardingLease(@RequestParam("image") MultipartFile image) throws IOException {
//
//        if (image.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded.");
//        }
//
//        try {
//            String imageUrl = documentsUploadService.uploadLeaseDocuments(image, "D:/image");
//            return ResponseEntity.ok(imageUrl);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the image.");
//        }
//


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('Operator','Administrator','User')")
    public List<PostLeaseDetailDto> getLeasingDetails(
            @RequestParam("open-status") Integer openStatus) {
        return mainService.getLeaseDetails(openStatus);
    }


    @RequestMapping(value = "/calculate-installment", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('Operator','Administrator','User')")
    public CalculatedLeaseDto manageLeasePayment(
            @RequestParam("lease-id") Integer leaseId,
            @RequestParam("payment-received-date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date payemtReceivedDate) {
        return mainService.calculateInterest(leaseId, payemtReceivedDate);
    }

    @RequestMapping(value = "/make-payment", method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('Operator','Administrator')")
    public ResponseEntity makePayment(
            @RequestParam(value = "lease-id" ,required = false) Integer leaseId,
            @RequestParam(value = "interest",required = false) Double interest,
            @RequestParam(value = "payment-date",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date paymentDate,
            @RequestParam(value = "user-id",required = false) Integer userId,
            @RequestParam(value = "installment",required = false) Double installment,
            @RequestParam(value = "penalty-duration",required = false) Integer penaltyDuration,
            @RequestParam(value = "penalty-amount",required = false ) Double penaltyAmount,
            @RequestParam(value = "amount",required = false) Double amount) {

//        LeaseMaster lease = mainService.makePayment(leaseId, installment, interest, paymentDate, userId, penaltyDuration, penaltyAmount);
        LeaseMaster lease =mainService.makePayment(leaseId,amount,userId);
        if (lease != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }


    @RequestMapping(value = "/all-users", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('Administrator','Operator')")
    public List<UserMaster> getAllUsers() {
        List<UserMaster> users = mainService.getAllUsers();
        return users;

    }

    @RequestMapping(value = "/user-group/add", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('Administrator','Operator')")
    public void saveUserGroup(@RequestParam("user-group-name") String userGroupName,
                              @RequestParam("user-key") Integer userKey) {
        mainService.addUserGroup(userGroupName, userKey);
    }

    @RequestMapping(value = "/user-group/all", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('Administrator','Operator')")
    public List<UserGroups> getAllUserGroups() {
        return mainService.getAllUserGroups();
    }

    @RequestMapping(value = "/user-group/edit", method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('Administrator')")
    public UserGroups editUserGroup(@RequestParam("user-group-name") String userGroupName,
                                    @RequestParam("user-group-key") Integer userGroupKey,
                                    @RequestParam("user-key") Integer userKey) {
        return mainService.editUserGroup(userGroupName, userGroupKey, userKey);
    }

    @RequestMapping(value = "/user-group/delete", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('Administrator','Operator')")
    public ResponseEntity deleteUserGroup(@RequestParam("user-group-key") Integer userGroupKey) {
        mainService.deleteUserGroup(userGroupKey);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('Administrator')")
    public void deleteUser(@RequestParam("user-key") Integer userKey) {
        mainService.deleteUser(userKey);
    }

    @RequestMapping(value = "/user/edit",method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('Administrator')")
    public void updateUser(@RequestBody UserMaster userMaster){
        mainService.updateUser(userMaster);
    }


    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('Administrator')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByLoginId(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        UserGroups userGroup = roleRepository.findByUserGroupKey(signUpRequest.getUserGroupKey()).get();

        UserMaster user = new UserMaster(signUpRequest.getFirstName(),
                signUpRequest.getLastName(), signUpRequest.getMobileNumber(),
                signUpRequest.getUserType(), signUpRequest.getProfileImagePath(),
                signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getIsEnable(), signUpRequest.getIsHidden(),
                signUpRequest.getAddress(), signUpRequest.getIdentificationNumber(),
                signUpRequest.getHomeNumber(), signUpRequest.getEmail(), userGroup);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @RequestMapping(value = "/lease-app-settings", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('Administrator')")
    public LeaseAppSettings getLeaseAppSettingsData() {
        return mainService.getLeaseAppSettings();
    }

    @RequestMapping(value = "/lease-app-settings", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('Administrator')")
    public ResponseEntity updateLeaseAppSettings(@RequestBody LeaseAppSettingResponse leaseAppSettingResponse) {
        System.out.println(leaseAppSettingResponse);
        LeaseAppSettings leaseAppSettings = mainService.updateLeseAppSettings(leaseAppSettingResponse);
        if (leaseAppSettings != null) {
            return ResponseEntity.ok("Successfully Updated");
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Not Updated"));
        }

    }

    @RequestMapping(value = "/report/income", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('Administrator')")
    public IncomeReportResponse getIncomeReport(@RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                @RequestParam("end-date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        return mainService.createIncomeReport(startDate, endDate);

    }

    @RequestMapping(value = "/report/income/summery", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('Administrator')")
    public List<LeaseMaster> getIncomeReportSummery(@RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                    @RequestParam("end-date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        return mainService.createIncomeReportSummery(startDate, endDate);

    }

    @RequestMapping(value = "/report/outdated-payments", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('Administrator')")
    public List<OutDatedPaymentResponse> getOutDatedPayments() {
        return mainService.getOutdatedPayments();

    }

    //    edit lease guarantors
    @RequestMapping(value = "/guarantors/edit", method = RequestMethod.PUT)
    @Operation(summary = "Edit", description = "Edit Lease Guarantors details", tags = {"LeaseGurantors"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "action performed success"),
            @ApiResponse(responseCode = "400", description = "unable to perform action")
    })
    @PreAuthorize(" hasAnyAuthority('Operator','Administrator')")
    public List<LeaseGurantors> editLeaseGuarantorsDetails(@RequestBody List<LeaseGurantors> guarantors) {
        return guarantorService.editLeaseGuarantor(guarantors);
    }

    @RequestMapping(value = "/vehicle",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('Operator','Administrator')")
    public List<Vehicle> searchVehicle(@RequestParam(name = "vehicle-no",required = false) String vehicleNo,
                                        @RequestParam(name = "chassis-no",required = false) String chassisNo,
                                        @RequestParam(name = "make",required = false)String make,
                                        @RequestParam(name = "model",required = false) String model){
        return vehicleService.getAllVehicles(vehicleNo);

    }

    @RequestMapping(value = "/leasee",method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('Operator','Administrator')")
    public Boolean searchLeasee(@RequestParam(name = "nic") String nic){
        return leaseeMasterService.getAllLeasee(nic);

    }

    @RequestMapping(value = "/delete",method = RequestMethod.PUT)
    @PreAuthorize(" hasAnyAuthority('Operator','Administrator')")
    public LeaseMaster deleteLease(@RequestParam(name = "id") Integer leaseId,
                                   @RequestParam(name = "status") Integer status){
       return this.mainService.deletelease(leaseId,status);
    }


    @RequestMapping(value = "/lease/personal_detail/edit",method = RequestMethod.PUT)
    @PreAuthorize(" hasAnyAuthority('Operator','Administrator')")
    public ResponseEntity editLeasePersonalDetails(@RequestParam(name = "lease-id") Integer leaseId,
                                                           @RequestParam(name = "land-number") String homeNumber,
                                                           @RequestParam(name = "email") String email){

         return this.mainService.updateLeaseeDeatils(leaseId,homeNumber,email);
    }



    @RequestMapping(value = "/documents",method = RequestMethod.GET)
    @PreAuthorize(" hasAnyAuthority('Operator','Administrator')")
    public ResponseEntity<List<String>> getDocuments(@RequestParam(value = "lease-id") Integer leaseId){
        List<String> fileNames = leaseDocumentService.listFiles(leaseId);
        return ResponseEntity.ok(fileNames);

    }



@RequestMapping(value ="/lease/active_log",method = RequestMethod.GET)
    public UserMaster getLoggedUser(@RequestParam(value = "id") Integer userId){
        return mainService.getLoggedUserById(userId);
    }

}
