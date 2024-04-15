package sk.thenoen.rasptemp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

import sk.thenoen.rasptemp.domain.TemperatureRecord;

public interface TemperatureRecordRepository extends JpaRepository<TemperatureRecord, Long> {

	TemperatureRecord findOneByDateMeasured(Date dateMeasured);

	List<TemperatureRecord> findAllByDateMeasuredAfter(Date oldestDate);

	List<TemperatureRecord> findAllByDateMeasuredAfterAndAndDateMeasuredBefore(Date oldestDate, Date newestDate);

//	TemperatureRecord findFirstByOrderByLastnameAsc();
	TemperatureRecord findFirstByOrderByDateMeasuredDesc();

	// method to find records between two dates
	List<TemperatureRecord> findAllByDateMeasuredBetween(Date startDate, Date endDate);

}
