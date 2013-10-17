package com.lithium.ldn.starql.exceptions;

@SuppressWarnings("serial")
public class CollectionNotDefinedException extends QueryValidationException {

	public CollectionNotDefinedException(String collectionName, String query) {
		super("Collection " + quote(collectionName) + " not defined.", query);
	}

}
