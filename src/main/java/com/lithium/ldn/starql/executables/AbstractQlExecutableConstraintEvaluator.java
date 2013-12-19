package com.lithium.ldn.starql.executables;

import com.lithium.ldn.starql.models.QlConstraint;
import com.lithium.ldn.starql.models.QlConstraintValueExecutable;
import com.lithium.ldn.starql.models.QlSelectStatement;
import com.lithium.ldn.starql.models.QlWhereClause;

public abstract class AbstractQlExecutableConstraintEvaluator implements
		QlExecutableConstraintEvaluator {

	@Override
	public void evaluate(QlSelectStatement selectStatement) {
		if (selectStatement.hasConstraints()) {
			for (QlConstraint c : selectStatement.getConstraintsList()) {
				if (c.getValue().isA(QlConstraintValueExecutable.class)) {
					evaluate(c);
				}
			}
		}
	}

	@Override
	public void evaluate(QlWhereClause whereClause) {
		if (whereClause.getRoot() != null) {
			for (QlConstraint c : whereClause.getConstraintsList()) {
				if (c.getValue().isA(QlConstraintValueExecutable.class)) {
					evaluate(c);
				}
			}
		}
	}

}
