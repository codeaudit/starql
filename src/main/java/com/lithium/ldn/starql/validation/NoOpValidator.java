package com.lithium.ldn.starql.validation;

import com.lithium.ldn.starql.exceptions.QueryValidationException;
import com.lithium.ldn.starql.models.QlSelectStatement;

public class NoOpValidator implements QlSelectStatementValidator {

	@Override
	public void validate(QlSelectStatement statement)
			throws QueryValidationException {
		if (statement == null) {
			throw new IllegalArgumentException("statement must not be null");
		}
		// Do nothing
	}

}
