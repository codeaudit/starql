package com.lithium.ldn.starql.validation;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.lithium.ldn.starql.exceptions.QueryValidationException;
import com.lithium.ldn.starql.models.QlSelectStatement;

public abstract class QlSelectStatementCompositeValidator implements QlSelectStatementValidator {

	protected final List<QlSelectStatementValidator> validators;
	
	protected QlSelectStatementCompositeValidator(QlSelectStatementValidator... validators) {
		this.validators = ImmutableList.copyOf(validators);
	}
	
	@Override
	public void validate(QlSelectStatement statement) throws QueryValidationException {
		for (QlSelectStatementValidator validator : validators) {
			validator.validate(statement);
		}
	}

}
