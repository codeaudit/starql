package com.lithium.ldn.starql.executables;

import com.lithium.ldn.starql.models.QlConstraint;
import com.lithium.ldn.starql.models.QlSelectStatement;
import com.lithium.ldn.starql.models.QlWhereClause;

public interface QlExecutableConstraintEvaluator {

	/**
	 * Evaluate the provided constraint. After this method completes, the constraint could 
	 * still be executable if there are multiple layers of executable complexity.
	 * 
	 * @param constraint
	 */
	void evaluate(QlConstraint constraint);
	
	/**
	 * Iterate over each constraint in the provided select statement and call evaluate on each 
	 * of the executable constraints.
	 * 
	 * @param selectStatement
	 */
	void evaluate(QlSelectStatement selectStatement);
	
	/**
	 * Iterate over each constraint in the provided WHERE clause and call evaluate on each 
	 * of the executable constraints.
	 * 
	 * @param whereClause
	 */
	void evaluate(QlWhereClause whereClause);
}
