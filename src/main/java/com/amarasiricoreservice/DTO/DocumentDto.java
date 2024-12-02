package com.amarasiricoreservice.DTO;

import lombok.Data;

import java.util.List;

@Data
public class DocumentDto {
    Integer leaseKey;
    List<NewLeaseDoc> docList;
}
