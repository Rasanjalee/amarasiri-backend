package com.amarasiricoreservice.Repository;

import com.amarasiricoreservice.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Integer> {

    Vehicle findByLeaseeVehicleKey(Integer vehicleId);


    List<Vehicle> findAllByVehicleNumber(String vehicleNo);

//    @Query("select v from Vehicle v where v.Leaseevehiclekey in (:vehicleIds)")
//    List<Vehicle> findByVehicleIds(@Param("vehicleIds") List<Integer> vehicleIds);
}
