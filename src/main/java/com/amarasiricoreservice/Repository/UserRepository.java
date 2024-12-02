package com.amarasiricoreservice.Repository;

import com.amarasiricoreservice.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserMaster,Integer> {
    Optional<UserMaster> findByLoginId(String integer);

    Boolean existsByLoginId(String loginId);

    Boolean existsByEmail(String email);

    Optional<UserMaster> findByUserKey(Integer userKey);
}
