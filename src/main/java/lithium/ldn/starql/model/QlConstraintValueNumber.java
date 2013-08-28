/*
 * ConstraintValueNumber.java
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

/**
 * @author Jake Scheps
 */
public class QlConstraintValueNumber extends QlConstraintValue {
	
	private final Number value;
	
	public QlConstraintValueNumber(Number value) {
		this.value = value;
	}
	
	public Number getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value != null ? value.toString() : null;
	}
}
