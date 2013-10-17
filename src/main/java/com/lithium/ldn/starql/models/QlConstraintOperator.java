package com.lithium.ldn.starql.models;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * StarQL Search Query Operators. Used in {@link QlConstraint}.
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
	MATCHES("MATCHES", getSet(QlConstraintValueString.class)),
	@SuppressWarnings("unchecked")
	LIKE("LIKE", getSet(QlConstraintValueString.class, QlConstraintValueCollection.class)),
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
		temp.put(MATCHES.getName(), MATCHES);
		temp.put(LIKE.getName(), LIKE);
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

	/**
	 * Look up an operator enum by name.
	 * @param lookupName Name of desired operator enum.
	 * @return Desired enum if exists, {@code UNKNOWN} otherwise.
	 */
	public static QlConstraintOperator get(String lookupName) {
		lookupName = lookupName.toUpperCase();
		QlConstraintOperator result = map.get(lookupName);
		return result == null ? UNKNOWN : result;
	}
}
