/*
 * ConStraintPairOperator.java
 * Created on Jul 15, 2013
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

import org.codehaus.jparsec.functors.Binary;

import com.google.common.collect.Maps;

/**
 * @author David Esposito
 */
public enum QlConstraintPairOperator implements Binary<QlBooleanConstraintNode> {

	AND("AND"),
	OR("OR"),
	UNKNOWN("UNKNOWN");
	
	private static final Map<String, QlConstraintPairOperator> map;
	
	static {
		Map<String, QlConstraintPairOperator> temp = Maps.newHashMap();
		temp.put(AND.getName(), AND);
		temp.put(OR.getName(), OR);
		map = Collections.unmodifiableMap(temp);
	}
	
	private final String name;
	
	private QlConstraintPairOperator(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public QlBooleanConstraintNode map(QlBooleanConstraintNode arg0, QlBooleanConstraintNode arg1) {
		if (arg0 == null || arg1 == null || this == UNKNOWN) {
			return null;
		}
		return new QlConstraintPair(arg0, arg1, this);
	}
	
	public static QlConstraintPairOperator get(String lookupName) {
		QlConstraintPairOperator result = map.get(lookupName);
		return result == null ? UNKNOWN : result;
	}
}
