package com.lithium.ldn.starql.parsers;

import com.lithium.ldn.starql.exceptions.InvalidQueryException;
import com.lithium.ldn.starql.models.QlSelectStatement;


/**
 * Converts query strings into query markup specific data structures.
 * 
 * @author David Esposito
 */
public interface QueryMarkupManager {

	/**
	 * Converts a StarQL SELECT statement string into a POJO.
	 * @param query StarQL SELECT statement. Should not end in a semicolon. Cannot be null.
	 * @return The select statement representing the provided query. Will never be null.
	 * @throws InvalidQueryException Please see exception and fix query syntax.
	 */
	public QlSelectStatement parseQlSelect(String query) throws InvalidQueryException;
}
