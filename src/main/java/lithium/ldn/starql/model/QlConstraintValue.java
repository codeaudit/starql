/*
 * ConstraintValue.java
 * Created on Aug 21, 2013
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
 * @author Jake Scheps
 */
public abstract class QlConstraintValue {

	public boolean isA(Class<? extends QlConstraintValue> constraintValueClass) {
		return this.getClass().equals(constraintValueClass);
	}

	@SuppressWarnings("unchecked")
	public <ConstraintValueT extends QlConstraintValue> ConstraintValueT asA(Class<ConstraintValueT> constraintValueT) {
		if (isA(constraintValueT)) {
			return (ConstraintValueT) this;
		} else {
			return null;
		}
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
