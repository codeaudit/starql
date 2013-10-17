package com.lithium.ldn.starql.exceptions;

@SuppressWarnings("serial")
public class PropertyNotDefinedException extends QueryValidationException {

	public PropertyNotDefinedException(String propertyName, String collectionName, String query) {
		super("Property " + quote(propertyName) + " not defined for collection " + quote(collectionName) + 
				".", query);
	}

}
