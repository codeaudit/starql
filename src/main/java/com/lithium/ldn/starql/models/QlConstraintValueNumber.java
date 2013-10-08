package com.lithium.ldn.starql.models;

/**
 * A number value of a boolean constraint in the WHERE clause of a StarQL SELECT Statement.
 * 
 * @author Jake Scheps
 * @author David Esposito
 */
public class QlConstraintValueNumber extends QlConstraintValue {
	
	private final Number value;
	
	public QlConstraintValueNumber(Number value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @return The number value of the boolean constraint. Could be an {@code int}, {@code long}, 
	 * {@code float} or {@code double}.
	 */
	public Number getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value != null ? value.toString() : null;
	}

	@Override
	public Object get() {
		return getValue();
	}
}
