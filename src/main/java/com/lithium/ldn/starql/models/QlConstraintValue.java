package com.lithium.ldn.starql.models;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * A String value of a boolean constraint in the WHERE clause of a StarQL SELECT Statement.
 * 
 * @author Jake Scheps
 * @author David Esposito
 */
public abstract class QlConstraintValue {

	/**
	 * Test if the current object is assignable to a more specific implementation of QlConstraintValue.
	 * @param constraintValueClass The super type in question.
	 * @return True if this object can be cast to the provided class without throwing an exception. False
	 * otherwise.
	 */
	public boolean isA(Class<? extends QlConstraintValue> constraintValueClass) {
		return this.getClass().equals(constraintValueClass);
	}

	/**
	 * @param constraintValueT The desired type for casting the current object.
	 * @return The current object typed as the provided class if {@code asA(...)} return {@code true}, 
	 * {@code null} otherwise.
	 */
	@SuppressWarnings("unchecked")
	public <ConstraintValueT extends QlConstraintValue> ConstraintValueT asA(Class<ConstraintValueT> constraintValueT) {
		if (isA(constraintValueT)) {
			return (ConstraintValueT) this;
		} else {
			return null;
		}
	}
	
	/**
	 * @return The object represented by this POJO.
	 */
	public abstract Object get();

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
