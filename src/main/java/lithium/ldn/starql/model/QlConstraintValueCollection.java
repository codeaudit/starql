/*
 * ConstraintValueCollection.java
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

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author Jake Scheps
 */
public class QlConstraintValueCollection<TypeT extends QlConstraintValue> extends QlConstraintValue implements Iterable<TypeT> {

	private final List<TypeT> value;

	public QlConstraintValueCollection(TypeT... values) {
		this.value = Lists.newArrayList(values);
	}
	
	public QlConstraintValueCollection(List<TypeT> value) {
		this.value = value;
	}
	
	public List<TypeT> getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public Iterator<TypeT> iterator() {
		return value.iterator();
	}

	@Override
	public Object get() {
		return getValue();
	}

}
