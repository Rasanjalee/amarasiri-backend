package com.amarasiricoreservice.service;

import com.amarasiricoreservice.Repository.VehicleRepository;
import com.amarasiricoreservice.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle getVehicles(Integer vehicleId){

            return vehicleRepository.findByLeaseeVehicleKey(vehicleId);
    }

    public List<Vehicle> getAllVehicles(String vehicleNo){
        return vehicleRepository.findAllByVehicleNumber(vehicleNo);
    }

//    public Boolean exist
}
