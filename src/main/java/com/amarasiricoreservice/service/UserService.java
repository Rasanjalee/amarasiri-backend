package com.amarasiricoreservice.service;

import com.amarasiricoreservice.Repository.UserRepository;
import com.amarasiricoreservice.entity.UserMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void saveUser(UserMaster userMaster){
        userRepository.save(userMaster);
    }

    public List<UserMaster> getUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(Integer userKey){
        userRepository.deleteById(userKey);
    }

    public UserMaster getUserById(Integer userId) {
        Optional<UserMaster> user = userRepository.findByUserKey(userId);
        return user.orElseGet(UserMaster::new);
    }
}
