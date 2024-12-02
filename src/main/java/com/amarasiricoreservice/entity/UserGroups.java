package com.amarasiricoreservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "usergroups")
public class UserGroups {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usergroupkey")
    private Integer userGroupKey;

    @Column(name = "usergroupname")
    private String userGroupName;

    @Column(name = "modifieddatetime")
    private Date  modifiedDateTime;

    @Column(name = "modifieduserkey")
    private Integer modifieduserkey;
}
