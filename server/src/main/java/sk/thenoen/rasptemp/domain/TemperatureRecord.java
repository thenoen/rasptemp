package sk.thenoen.rasptemp.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class TemperatureRecord {

	@Id
	@GeneratedValue
	private Long id;

	private Double degrees;

	private Date dateMeasured;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getDegrees() {
		return degrees;
	}

	public void setDegrees(Double degrees) {
		this.degrees = degrees;
	}

	public Date getDateMeasured() {
		return dateMeasured;
	}

	public void setDateMeasured(Date dateMeasured) {
		this.dateMeasured = dateMeasured;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Date: ").append(dateMeasured);
		sb.append(" - ");
		sb.append("Temperature: ").append(degrees);
		return sb.toString();
	}
}
