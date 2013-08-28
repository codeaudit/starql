/*
 * ConstraintPair.java
 * Created on May 17, 2013
 *
 * Copyright 2013 Lithium Technologies, Inc. 
 * Emeryville, California, U.S.A.  All Rights Reserved.
 *
 * This software is the  confidential and proprietary information
 * of  Lithium  Technologies,  Inc.  ("Confidential Information")
 * You shall not disclose such Confidential Information and shall 
 * use  it  only in  accordance  with  the terms of  the  license 
 * agreement you entered into with Lithium.
 */

package lithium.ldn.starql.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author David Esposito
 */
public final class QlConstraintPair implements QlBooleanConstraintNode {

	private final QlBooleanConstraintNode leftHandSide;
	private final QlBooleanConstraintNode rightHandSide;
	private final QlConstraintPairOperator operation;

	public QlBooleanConstraintNode getLeftHandSide() {
		return leftHandSide;
	}

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
