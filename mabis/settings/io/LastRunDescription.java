package settings.io;

import java.text.DateFormat;
import java.util.Date;

public class LastRunDescription {
	final Integer lastRunId;
	final Boolean lastRunStatus;
	final Date lastRunDate;

	public LastRunDescription(int id, boolean status, Date date) {
		this.lastRunDate = date;
		this.lastRunId = id;
		this.lastRunStatus = status;
	}

	public Integer getLastRunId() {
		return this.lastRunId;
	}

	public Boolean getLastRunStatus() {
		return this.lastRunStatus;
	}

	public Date getLastRunDate() {
		return this.lastRunDate;
	}

	@Override
	public String toString() {
		return "Program statistics:\n\tRun Count = " + this.lastRunId
				+ "\n\tLast run = "
				+ DateFormat.getDateInstance().format(this.lastRunDate);
	}

}
