package com.lithium.ldn.starql.exceptions;


@SuppressWarnings("serial")
public class QlExecutableConstraintEvaluatorException extends QueryValidationException {

	public QlExecutableConstraintEvaluatorException(String message, String query) {
		super(message, query);
	}

}
