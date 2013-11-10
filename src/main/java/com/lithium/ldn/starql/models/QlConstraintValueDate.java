package com.lithium.ldn.starql.models;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * A String value of a boolean constraint in the WHERE clause of a StarQL SELECT Statement.
 * Default date format. Implements ISO8601
 * 
 * @author David Esposito
 */
public class QlConstraintValueDate extends QlConstraintValue {

	private final DateTime date;
	private final String isoString;
	
	public QlConstraintValueDate(DateTime date) {
		this.date = date;
		this.isoString = date.toString(ISODateTimeFormat.dateTime());
	}
	
	/**
	 * 
	 * @return The date value of the boolean constraint. Never {@code null}.
	 */
	public DateTime getValue() {
		return date;
	}
	
	@Override
	public String toString() {
		return getValueString();
	}
	
	/**
	 * 
	 * @return String representation of the date in ISO-8601 format.
	 */
	public String getValueString() {
		return isoString;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((isoString == null) ? 0 : isoString.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		QlConstraintValueDate other = (QlConstraintValueDate) obj;
		if (isoString == null) {
			if (other.isoString != null)
				return false;
		} else if (!isoString.equals(other.isoString))
			return false;
		return true;
	}

	@Override
	public Object get() {
		return getValue();
	}
}
