package com.amarasiricoreservice.Repository;

import com.amarasiricoreservice.entity.LeaseAppSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaseAppSettingsRepository extends JpaRepository<LeaseAppSettings, Integer> {
}
