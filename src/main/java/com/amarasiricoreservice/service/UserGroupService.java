package com.amarasiricoreservice.service;

import com.amarasiricoreservice.Repository.UserGroupsRepository;
import com.amarasiricoreservice.entity.UserGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserGroupService {

    @Autowired
    UserGroupsRepository userGroupsRepository;

    public UserGroups findUserGroup(Integer userGroupKey){
        return userGroupsRepository.findByUserGroupKey(userGroupKey).get();
    }
    public void add(String userGroupNme,Integer userKey){
        UserGroups userGroups = new UserGroups();
        userGroups.setUserGroupName(userGroupNme);
        userGroups.setModifiedDateTime(new Date());
        userGroups.setModifieduserkey(userKey);
        userGroupsRepository.save(userGroups);
    }

    public List<UserGroups> findAll(){
        System.out.println(userGroupsRepository.findAll());
       return userGroupsRepository.findAll();
    }

    public void deleteUserGroup(Integer userGroupKey){
        userGroupsRepository.deleteById(userGroupKey);
    }

    public UserGroups editUserGroup(String userGroupName, Integer userGroupKey,Integer userKey){

        UserGroups userGroups = findUserGroup(userGroupKey);
        userGroups.setUserGroupName(userGroupName);
        userGroups.setModifieduserkey(userKey);
        userGroups.setModifiedDateTime(new Date());
       return userGroupsRepository.save(userGroups);
    }
}

