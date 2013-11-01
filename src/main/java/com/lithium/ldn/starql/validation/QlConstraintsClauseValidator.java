package com.lithium.ldn.starql.validation;

import com.lithium.ldn.starql.exceptions.QueryValidationException;
import com.lithium.ldn.starql.models.QlWhereClause;

public interface QlConstraintsClauseValidator {

	/**
	 * Performs validation on the provided clause. Note that validators might do varying levels
	 * of validation for performance reasons. Using a validator on a constraints clause does not
	 * guarentee that the statement is valid, but only for the cases that the validators is designed
	 * to check. See documentation of implementations for cases checked by the validator.
	 * 
	 * @param constraintsClause The clause to be validated. Must not be {@code null}.
	 * @throws QueryValidationException Thrown if an invalid state exists in the query.
	 */
	void validate(QlWhereClause constraintsClause) throws QueryValidationException;
}
