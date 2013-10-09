package com.lithium.ldn.starql.models;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * A collection of {@link QlConstraintValue} for a boolean constraint in the WHERE clause of 
 * a StarQL SELECT Statement.
 * 
 * @author Jake Scheps
 * @author David Esposito
 */
public final class QlConstraintValueCollection<TypeT extends QlConstraintValue> extends QlConstraintValue implements Iterable<TypeT> {

	private final List<TypeT> value;

	public QlConstraintValueCollection(TypeT... values) {
		this.value = Collections.unmodifiableList(Lists.newArrayList(values));
	}
	
	public QlConstraintValueCollection(List<TypeT> value) {
		this.value = Collections.unmodifiableList(Lists.newArrayList(value));
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
