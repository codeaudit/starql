package com.lithium.ldn.starql.parsers;

import com.lithium.ldn.starql.exceptions.InvalidQueryException;
import com.lithium.ldn.starql.exceptions.QueryValidationException;
import com.lithium.ldn.starql.models.QlSelectStatement;
import com.lithium.ldn.starql.validation.QlSelectStatementValidator;


/**
 * Converts query strings into query markup specific data structures.
 * 
 * @author David Esposito
 */
public interface QueryMarkupManager {

	/**
	 * Converts a StarQL SELECT statement string into a POJO. A no-op validator will be used.
	 * @param query StarQL SELECT statement. Should not end in a semicolon. Must not be {@code null}
	 * @return The select statement representing the provided query. Will never be null.
	 * @throws InvalidQueryException Please see exception and fix query syntax.
	 */
	public QlSelectStatement parseQlSelect(String query) throws InvalidQueryException, QueryValidationException;
	
	/**
	 * Converts a StarQL SELECT statement string into a POJO.
	 * @param query StarQL SELECT statement. Should not end in a semicolon. Must not be {@code null}
	 * @param validator The validator to be used once the query is successfully parsed. Must not be {@code null}
	 * @return The select statement representing the provided query. Will never be null.
	 * @throws InvalidQueryException Please see exception and fix query syntax.
	 */
	public QlSelectStatement parseQlSelect(String query, QlSelectStatementValidator validator) 
			throws InvalidQueryException, QueryValidationException;
}
