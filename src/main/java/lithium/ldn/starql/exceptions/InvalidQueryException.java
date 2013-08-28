/*
 * InvalidQueryException.java
 * Created on May 20, 2013
 *
 * Copyright 2013 Lithium Technologies, Inc. 
 * Emeryville, California, U.S.A.  All Rights Reserved.
 *
 * This software is the  confidential and proprietary information
 * of  Lithium  Technologies,  Inc.  ("Confidential Information")
 * You shall not disclose such Confidential Information and shall 
 * use  it  only in  accordance  with  the terms of  the  license 
 * agreement you entered into with Lithium.
 */

package lithium.ldn.starql.exceptions;

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
