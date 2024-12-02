package com.amarasiricoreservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "usergroupapproleap")
public class userGroupAppRoleMap {
    @Id
    @Column(name = "usergroupkey")
    private Integer userGroupKey;

    @Column(name = "approles")
    private String appRoles;

    @Column(name = "modifieddatetime")
    private Date modifiedDateTime;

    @Column(name = "modifieduser")
    private Integer modifiedUserKey;
}
