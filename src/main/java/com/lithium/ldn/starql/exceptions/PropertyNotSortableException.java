package com.lithium.ldn.starql.exceptions;

@SuppressWarnings("serial")
public class PropertyNotSortableException extends QueryValidationException {

	public PropertyNotSortableException(String propertyName, String collectionName, String query) {
		super("Property " + quote(propertyName) + " in collection " + quote(collectionName) + 
				" is not sortable.", query);
	}

}
