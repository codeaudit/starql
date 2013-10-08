package com.lithium.ldn.starql.exceptions;

/**
 * @author David Esposito
 */
public class InvalidQueryException extends Exception {

	private static final long serialVersionUID = 1L;

	private final String query;
	
	public String getQuery() {
		return query;
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + " in: " + getQuery();
	}

	public InvalidQueryException(String message, String query) {
		super(message);
		this.query = query;
	}
}
