package com.lithium.ldn.starql.models;

import java.util.Collections;
import java.util.Map;

import org.codehaus.jparsec.functors.Binary;

import com.google.common.collect.Maps;

/**
 * Valid operators for combining {@link QlBooleanConstraintNode} in the WHERE clause of StarQL 
 * SELECT statements.
 * 
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
