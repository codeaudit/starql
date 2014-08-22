package com.lithium.ldn.starql.models;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.ImmutableList;

/**
 * A collection of {@link QlConstraintValue} for a boolean constraint in the WHERE clause of 
 * a StarQL SELECT Statement.
 * 
 * @author Jake Scheps
 * @author David Esposito
 */
public final class QlConstraintValueCollection<TypeT extends QlConstraintValue> extends QlConstraintValue implements Iterable<TypeT> {

	private final ImmutableList<TypeT> value;

	public QlConstraintValueCollection(TypeT... values) {
		this.value = ImmutableList.copyOf(values);
	}
	
	public QlConstraintValueCollection(List<TypeT> value) {
		this.value = ImmutableList.copyOf(value);
	}
	
	public List<TypeT> getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "(" + StringUtils.join(value.toArray(), ", ") + ")";
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
