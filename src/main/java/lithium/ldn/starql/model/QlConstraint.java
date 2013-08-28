/*
 * Constraint.java
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
 * Defines a search constraint by requiring a field to related to a value in a 
 * specific way { =, !=, <, >, <=, >= }.
 * 
 * @author David Esposito
 */
public final class QlConstraint implements QlBooleanConstraintNode {

	private final QlField key;
	private final QlConstraintValue value;
	private final QlConstraintOperator operation;

	public QlField getKey() {
		return key;
	}

	public QlConstraintValue getValue() {
		return value;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public QlConstraintOperator getConstraintOperator() {
		return operation;
	}
	
	@Override
	public QlConstraintPairOperator getConstraintPairOperator() {
		return null;
	}

	public QlConstraint(QlField key, QlConstraintValue value, QlConstraintOperator operation) {
		this.key = key;
		this.value = value;
		this.operation = operation;
	}

	@Override
	public String toString() {
		return "Constraint [key=" + key + ", value=" + value + ", operation=" + operation + "]";
	}

	@Override
	public String getQueryString() {
		return key + operation.getName() + value;
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
