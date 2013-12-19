package com.lithium.ldn.starql.exceptions;

@SuppressWarnings("serial")
public class QueryValidationException extends InvalidQueryException {

	public QueryValidationException(String message, String query) {
		super(message, query);
	}
	
	protected static String quote(String text) {
		return "\"" + text + "\"";
	}
}
