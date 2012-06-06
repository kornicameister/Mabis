package settings.io;

import java.text.DateFormat;
import java.util.Date;

/**
 * Prosta klasa bedaca kontenerem dla informacji o ostatnim uruchumieniu 
 * aplikacji MABIS. </br>
 * Zawiera nastepujace pola:
 * <ul>
 * <li>licznik uruchomien</li>
 * <li>status ostatniego uruchomienia</li>
 * <li>date ostatniego uruchomienia</li>
 * </ul>
 * 
 * @author tomasz
 * @see LastRunDescription#lastRunStatus
 */
public class LastRunDescription {
	final Integer lastRunId;
	/**
	 * <strong>true</strong> - jesli aplikacji zostala poprawnie zamknieta 
	 * </br> 
	 * <strong>false</strong> - w innym wypadku
	 */
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
