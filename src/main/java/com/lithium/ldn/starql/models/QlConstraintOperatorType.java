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
public enum QlConstraintOperatorType implements QlConstraintOperator {

	/**
	 * Checks Equality be between two objects.
	 */
	@SuppressWarnings("unchecked")
	EQUALS("=", getSet(QlConstraintValueString.class, QlConstraintValueNumber.class)),
	
	/**
	 * Checks non-equality between two objects.
	 */
	@SuppressWarnings("unchecked")
	NOT_EQUALS("!=", getSet(QlConstraintValueString.class, QlConstraintValueNumber.class)),
	
	/**
	 * Checks comparative values for ordered objects. Inclusive bound.
	 */
	@SuppressWarnings("unchecked")
	GREATER_THAN_EQUAL(">=", getSet(QlConstraintValueString.class, QlConstraintValueNumber.class)),
	
	/**
	 * Checks comparative values for ordered objects. Inclusive bound.
	 */
	@SuppressWarnings("unchecked")
	LESS_THAN_EQUAL("<=", getSet(QlConstraintValueString.class,QlConstraintValueNumber.class)),
	
	/**
	 * Checks comparative values for ordered objects. Exclusive bound.
	 */
	@SuppressWarnings("unchecked")
	GREATER_THAN(">", getSet(QlConstraintValueString.class, QlConstraintValueNumber.class)),
	
	/**
	 * Checks comparative values for ordered objects. Exclusive bound.
	 */
	@SuppressWarnings("unchecked")
	LESS_THAN("<", getSet(QlConstraintValueString.class, QlConstraintValueNumber.class)),
	
	/**
	 * Checks equality to at least one value in a collection. e.g.
	 * 
	 *   id IN (1,2,3)
	 *   
	 *   which is equivalient to
	 *   
	 *   id = 1 OR id = 2 OR id = 3
	 */
	@SuppressWarnings("unchecked")
	IN("IN", getSet(QlConstraintValueCollection.class)),

	/**
	 * Checks equality of collection to all values in a given collection. e.g.
	 *
	 *   collection has_all (1,2,3)
	 *
	 *   which is equivalient to
	 *
	 *   collection contains 1 AND collection contains 3 AND collection contains 3
	 */
	@SuppressWarnings("unchecked")
	HAS_ALL("HAS_ALL", getSet(QlConstraintValueCollection.class)),
	
	
	@SuppressWarnings("unchecked")
	MATCHES("MATCHES", getSet(QlConstraintValueString.class)),
	
	/**
	 * Used for analyzed string search.
	 */
	@SuppressWarnings("unchecked")
	LIKE("LIKE", getSet(QlConstraintValueString.class, QlConstraintValueCollection.class)),
	
	/**
	 * Used or substring search. Fuzzy bounds are defined defined by '%'. e.g.
	 * 
	 * subject LIKE '%Lithium%'
	 */
	@SuppressWarnings("unchecked")
	UNKNOWN("UNKNOWN", getSet());
	
	public static final QlConstraintOperatorType STATIC_ACCESS = UNKNOWN;
	private static final Map<String, QlConstraintOperatorType> map;
	static {
		Map<String,QlConstraintOperatorType> temp = Maps.newHashMap();
		temp.put(EQUALS.getName(), EQUALS);
		temp.put(NOT_EQUALS.getName(), NOT_EQUALS);
		temp.put(GREATER_THAN.getName(), GREATER_THAN);
		temp.put(LESS_THAN.getName(), LESS_THAN);
		temp.put(GREATER_THAN_EQUAL.getName(), GREATER_THAN_EQUAL);
		temp.put(LESS_THAN_EQUAL.getName(), LESS_THAN_EQUAL);
		temp.put(IN.getName(), IN);
		temp.put(HAS_ALL.getName(), HAS_ALL);
		temp.put(MATCHES.getName(), MATCHES);
		temp.put(LIKE.getName(), LIKE);
		map = Collections.unmodifiableMap(temp);
	}
	
	private static final Set<Class<? extends QlConstraintValue>> getSet(Class<? extends QlConstraintValue> ... classes) {
		return Collections.unmodifiableSet(Sets.newHashSet(classes));
	}
	
	private final String name;
	private final Set<Class<? extends QlConstraintValue>> expectedValueTypes;
	
	private QlConstraintOperatorType(String name, Set<Class<? extends QlConstraintValue>> expectedValueTypes) {
		this.name = name;
		this.expectedValueTypes = expectedValueTypes;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String getDisplayString() {
		return getName();
	}
	
	public Set<Class<? extends QlConstraintValue>> getExpectedValueTypes() {
		return expectedValueTypes;
	}

	/**
	 * Look up an operator enum by name.
	 * @param lookupName Name of desired operator enum.
	 * @return Desired enum if exists, {@code UNKNOWN} otherwise.
	 */
	public static QlConstraintOperatorType get(String lookupName) {
		lookupName = lookupName.toUpperCase();
		QlConstraintOperatorType result = map.get(lookupName);
		return result == null ? UNKNOWN : result;
	}
}
