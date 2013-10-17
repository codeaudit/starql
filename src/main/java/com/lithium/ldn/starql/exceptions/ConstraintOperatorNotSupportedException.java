package com.lithium.ldn.starql.exceptions;

import com.lithium.ldn.starql.models.QlConstraintOperator;

@SuppressWarnings("serial")
public class ConstraintOperatorNotSupportedException extends QueryValidationException {

	public ConstraintOperatorNotSupportedException(String propertyName, String collectionName, 
			QlConstraintOperator op, String query) {
		super("Property " + quote(propertyName) + " in collection " + quote(collectionName) + 
				" does not support operator " + quote(op.getName()) + ".", query);
	}

}
