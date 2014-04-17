package com.lithium.ldn.starql.models;

public class QlConstraintValueBoolean extends QlConstraintValue{
	
	private final Boolean value;
	
	public QlConstraintValueBoolean(Boolean value) {
		this.value = value;
	}
	
	public Boolean getValue() {
		return value;
	}
	
	public String toString() {
		return value.toString();
	}
	
	public Object get() {
		return getValue();
	}

}
