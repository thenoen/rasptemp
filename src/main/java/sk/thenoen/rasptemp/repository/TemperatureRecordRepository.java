package sk.thenoen.rasptemp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.thenoen.rasptemp.domain.TemperatureRecord;

public interface TemperatureRecordRepository extends JpaRepository<TemperatureRecord, Long> {

}
