package com.lithium.ldn.starql.exceptions;

@SuppressWarnings("serial")
public class QueryValidationException extends Exception {

	private final String query;
	
	public QueryValidationException(String message, String query) {
		super(message + " Validation exception found: ");
		this.query = query;
	}
	
	public String getQuery() {
		return query;
	}
	
	protected static String quote(String text) {
		return "\"" + text + "\"";
	}
}
