package com.lithium.ldn.starql.models;

/**
 * A String value of a boolean constraint in the WHERE clause of a StarQL SELECT Statement.
 * 
 * @author Jake Scheps
 * @author David Esposito
 */
public final class QlConstraintValueString extends QlConstraintValue {
	
	private final String value;
	
	public QlConstraintValueString(String value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @return The string value of the boolean constraint. Surrounding quotes are removed and 
	 * all internal quotes are un-escaped.
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value != null ? "'" + value.toString() + "'" : "null";
	}

	@Override
	public Object get() {
		return getValue();
	}

}
