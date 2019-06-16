package sk.thenoen.rasptemp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.thenoen.rasptemp.domain.TemperatureRecord;

import java.util.Date;
import java.util.List;

public interface TemperatureRecordRepository extends JpaRepository<TemperatureRecord, Long> {

	TemperatureRecord findOneByDateMeasured(Date dateMeasured);

	List<TemperatureRecord> findAllByDateMeasuredAfter(Date oldestDate);

//	TemperatureRecord findFirstByOrderByLastnameAsc();
	TemperatureRecord findFirstByOrderByDateMeasuredDesc();

}
