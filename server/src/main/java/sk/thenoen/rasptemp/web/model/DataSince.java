package sk.thenoen.rasptemp.web.model;

import java.time.LocalDateTime;

public class DataSince {

	private long hours;
	private LocalDateTime since;
	private int groupSize;

	public long getHours() {
		return hours;
	}

	public void setHours(long hours) {
		this.hours = hours;
	}

	public LocalDateTime getSince() {
		return since;
	}

	public void setSince(LocalDateTime since) {
		this.since = since;
	}

	public int getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}
}
