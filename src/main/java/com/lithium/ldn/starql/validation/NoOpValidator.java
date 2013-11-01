package com.lithium.ldn.starql.validation;

import com.lithium.ldn.starql.exceptions.QueryValidationException;
import com.lithium.ldn.starql.models.QlSelectStatement;
import com.lithium.ldn.starql.models.QlWhereClause;

public class NoOpValidator implements QlSelectStatementValidator, QlConstraintsClauseValidator {

	@Override
	public void validate(QlSelectStatement statement)
			throws QueryValidationException {
		if (statement == null) {
			throw new IllegalArgumentException("statement must not be null");
		}
		// Do nothing
	}

	@Override
	public void validate(QlWhereClause constraintsClause)
			throws QueryValidationException {
		if (constraintsClause == null) {
			throw new IllegalArgumentException("statement must not be null");
		}
		// Do nothing
	}

}
