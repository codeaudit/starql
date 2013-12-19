package com.lithium.ldn.starql.executables;

import com.lithium.ldn.starql.models.QlConstraint;

public class NoOpEvaluator extends AbstractQlExecutableConstraintEvaluator {

	@Override
	public void evaluate(QlConstraint constraint) { }

}
