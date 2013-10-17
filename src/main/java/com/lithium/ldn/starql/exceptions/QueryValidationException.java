package com.lithium.ldn.starql.exceptions;

@SuppressWarnings("serial")
public class QueryValidationException extends Exception {

	public QueryValidationException(String message, String query) {
		super(message + " Validation exception found in query: " + quote(query));
	}
	
	protected static String quote(String text) {
		return "\"" + text + "\"";
	}
}
