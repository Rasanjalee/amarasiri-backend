package com.amarasiricoreservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "leasedocuments")
public class LeaseDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documentkey")
    private Integer documentKey;

    @Column(name = "documentname")
    private String documentName;

    @Column(name = "documentremarks")
    private String documentRemarks;

    @Column(name = "documentpath")
    private String documentPath;

    @Column(name = "leasekey")
    private Integer leaseKey;

    @Column(name = "uploadeduserkey")
    private Integer uploadedUserKey = 0;

    @Column(name = "uploadeddatetime")
    private Date uploadeddatetime = new Date();

    @Column(name = "status")
    private String status = "Active";

    @Column(name = "isdefaultdocument")
    private Integer isDefaultDocument = 0;

}
