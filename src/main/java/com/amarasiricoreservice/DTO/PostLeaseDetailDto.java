package com.amarasiricoreservice.DTO;

import com.amarasiricoreservice.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class PostLeaseDetailDto {

    private LeaseeMaster leaseeMaster;
    private LeaseMaster leaseMaster;
    private Vehicle vehicle;
    private List<LeaseGurantors>  leaseGurantors;
    private List<LeasePaymentHistory> leasePaymentHistories;
    private List<LeaseInstallment> installments;
    private List<LeaseDocuments> leaseDocuments;
}
