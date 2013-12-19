package com.lithium.ldn.starql.exceptions;

/**
 * @author David Esposito
 */
@SuppressWarnings("serial")
public class InvalidQueryException extends Exception {

	private final String query;
	
	public InvalidQueryException(String message, String query) {
		super(message  + " in: " + query);
		this.query = query;
	}
	
	public String getQuery() {
		return query;
	}
}
