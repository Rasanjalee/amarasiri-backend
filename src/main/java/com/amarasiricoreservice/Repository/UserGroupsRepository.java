package com.amarasiricoreservice.Repository;

import com.amarasiricoreservice.entity.UserGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserGroupsRepository extends JpaRepository<UserGroups,Integer> {

    Optional<UserGroups> findByUserGroupKey(Integer integer);

    Optional<UserGroups> findByUserGroupName(String userGroupName);
}
