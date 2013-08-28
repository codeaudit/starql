/*
 * ConstraintOperation.java
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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * *QL Search Query Operators.
 * 
 * @author David Esposito
 */
public enum QlConstraintOperator {

	// Constraint
	@SuppressWarnings("unchecked")
	EQUALS("=", getSet(QlConstraintValueString.class, QlConstraintValueNumber.class)),
	@SuppressWarnings("unchecked")
	NOT_EQUALS("!=", getSet(QlConstraintValueString.class, QlConstraintValueNumber.class)),
	@SuppressWarnings("unchecked")
	GREATER_THAN(">", getSet(QlConstraintValueString.class, QlConstraintValueNumber.class)),
	@SuppressWarnings("unchecked")
	LESS_THAN("<", getSet(QlConstraintValueString.class, QlConstraintValueNumber.class)),
	@SuppressWarnings("unchecked")
	GREATER_THAN_EQUAL(">=", getSet(QlConstraintValueString.class, QlConstraintValueNumber.class)),
	@SuppressWarnings("unchecked")
	LESS_THAN_EQUAL("<=", getSet(QlConstraintValueString.class,QlConstraintValueNumber.class)),
	@SuppressWarnings("unchecked")
	IN("IN", getSet(QlConstraintValueCollection.class)),
	@SuppressWarnings("unchecked")
	UNKNOWN("UNKNOWN", getSet());
	
	public static final QlConstraintOperator STATIC_ACCESS = UNKNOWN;
	private static final Map<String, QlConstraintOperator> map;
	static {
		Map<String,QlConstraintOperator> temp = Maps.newHashMap();
		temp.put(EQUALS.getName(), EQUALS);
		temp.put(NOT_EQUALS.getName(), NOT_EQUALS);
		temp.put(GREATER_THAN.getName(), GREATER_THAN);
		temp.put(LESS_THAN.getName(), LESS_THAN);
		temp.put(GREATER_THAN_EQUAL.getName(), GREATER_THAN_EQUAL);
		temp.put(LESS_THAN_EQUAL.getName(), LESS_THAN_EQUAL);
		temp.put(IN.getName(), IN);
		map = Collections.unmodifiableMap(temp);
	}
	
	private static final Set<Class<? extends QlConstraintValue>> getSet(Class<? extends QlConstraintValue> ... classes) {
		return Collections.unmodifiableSet(Sets.newHashSet(classes));
	}
	
	private final String name;
	private final Set<Class<? extends QlConstraintValue>> expectedValueTypes;
	
	private QlConstraintOperator(String name, Set<Class<? extends QlConstraintValue>> expectedValueTypes) {
		this.name = name;
		this.expectedValueTypes = expectedValueTypes;
	}

	public String getName() {
		return name;
	}
	
	public Set<Class<? extends QlConstraintValue>> getExpectedValueTypes() {
		return expectedValueTypes;
	}

	public static QlConstraintOperator get(String lookupName) {
		QlConstraintOperator result = map.get(lookupName);
		return result == null ? UNKNOWN : result;
	}
}
