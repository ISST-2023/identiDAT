package es.upm.etsit.dat.identi.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsit.dat.identi.persistence.model.Setting;

public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> findById(Integer id);
    Setting findBySettingKey(String settingKey);
}