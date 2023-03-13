/**
 * 
 */
package models;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Calendar;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.time.DateUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = { "id", "fecha", "custId", "slotId", "publisherId" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrintedAd {

	private Long id;

	private Date fecha;

	private Long custId;

	private Long slotId;

	private Long publisherId;

	private String strategy;

	private Date tms;

	public PrintedAd(Long id, Date tms, Long custId, Long slotId, Long publisherId) {
		this.id = id;
		this.tms = tms;
		this.fecha = truncateToDateOnly(tms);
		this.custId = custId;
		this.slotId = slotId;
		this.publisherId = publisherId;
	}

	public void setTms(Date tms) {
		this.tms = tms;
		setFecha(truncateToDateOnly(tms));
	}

	private Date truncateToDateOnly(Date tms) {
		if (tms == null) {
			return null;
		}
		return DateUtils.truncate(tms, Calendar.DATE);
	}

	public String getNavType() {
		if (strategy != null) {
			if (strategy.contains("KEYWORD")) {
				return "K";
			} else if (strategy.contains("MIX")) {
				return "M";
			} else if (strategy.contains("NAV")) {
				return "C";
			}
		}
		return EMPTY;
	}
}
