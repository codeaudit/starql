package com.lithium.ldn.starql.models;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * A node inside a tree which represents the {@link QlBooleanConstraintNode} from the WHERE clause 
 * in a StarQL SELECT Statement.
 *  
 * @author David Esposito
 */
public final class QlConstraintPair implements QlBooleanConstraintNode {

	private final QlBooleanConstraintNode leftHandSide;
	private final QlBooleanConstraintNode rightHandSide;
	private final QlConstraintPairOperator operation;

	/**
	 * @return Child of the current node. Never {@code null}.
	 */
	public QlBooleanConstraintNode getLeftHandSide() {
		return leftHandSide;
	}

	/**
	 * @return Child of the current node. Never {@code null}.
	 */
	public QlBooleanConstraintNode getRightHandSide() {
		return rightHandSide;
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}
	
	@Override
	public QlConstraintOperator getConstraintOperator() {
		return null;
	}
	
	@Override
	public QlConstraintPairOperator getConstraintPairOperator() {
		return operation;
	}

	public QlConstraintPair(QlBooleanConstraintNode leftHandSide, QlBooleanConstraintNode rightHandSide,
			QlConstraintPairOperator constraintPairOperator) {
		this.leftHandSide = leftHandSide;
		this.rightHandSide = rightHandSide;
		this.operation = constraintPairOperator;
	}

	@Override
	public String toString() {
		return "ConstraintPair [leftHandSide=" + leftHandSide + ", rightHandSide=" + rightHandSide + "]";
	}

	@Override
	public String getQueryString() {
		return "(" + leftHandSide.getQueryString() + " " + operation.getName() + " " 
				+ rightHandSide.getQueryString() + ")"; 
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
