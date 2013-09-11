package lithium.ldn.starql.model;

import java.util.Date;

import com.fasterxml.jackson.databind.util.ISO8601Utils;

public class QlConstraintValueDate extends QlConstraintValue {

	private final Date date;
	private final String isoString;
	
	public QlConstraintValueDate(Date date) {
		this.date = date;
		this.isoString = ISO8601Utils.format(date);
	}
	
	public Date getValue() {
		return date;
	}
	
	public String getValueString() {
		return isoString;
	}
}
