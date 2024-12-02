package com.amarasiricoreservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@Table(name = "leaseidmaster")
public class LeaseId {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "iddate")
    private Date date;
    @Column(name = "count")
    private Integer count;
}
