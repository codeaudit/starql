package com.lithium.ldn.starql.models;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.collect.Lists;

/**
 * Defines a search constraint by relating a field to a value using {@link QlConstraintOperator}.
 * 
 * @author David Esposito
 */
public final class QlConstraint implements QlBooleanConstraintNode {

	private final QlField key;
	private final QlConstraintValue value;
	private final QlConstraintOperator operation;

	/**
	 * @return Key of constraint. Never {@code null}.
	 */
	public QlField getKey() {
		return key;
	}

	/**
	 * @return Value of constraint. Never {@code null}.
	 */
	public QlConstraintValue getValue() {
		return value;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public QlConstraintOperator getConstraintOperator() {
		return operation;
	}
	
	@Override
	public QlConstraintPairOperator getConstraintPairOperator() {
		return null;
	}

	public QlConstraint(QlField key, QlConstraintValue value, QlConstraintOperator operation) {
		this.key = key;
		this.value = value;
		this.operation = operation;
	}

	@Override
	public String toString() {
		return "Constraint [key=" + key + ", value=" + value + ", operation=" + operation + "]";
	}

	@Override
	public String getQueryString() {
		return key + operation.getName() + value;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	public static class Builder {
		private QlField key;
		private QlConstraintValue value;
		private QlConstraintOperator operation;
		public QlField getKey() {
			return key;
		}
		public Builder setKey(QlField key) {
			this.key = key;
			return this;
		}
		public Builder setKey(String name, QlField subObject, boolean isFunction) {
			return setKey(null, name, subObject, isFunction);
		}
		public Builder setKey(String qualifier, String name, QlField subObject, boolean isFunction) {
			this.key = QlField.create(qualifier, name, subObject, isFunction);
			return this;
		}
		public QlConstraintValue getValue() {
			return value;
		}
		public Builder setValue(QlConstraintValue value) {
			this.value = value;
			return this;
		}
		public Builder setValue(String value) {
			this.value = new QlConstraintValueString(value);
			return this;
		}
		public Builder setValue(Number value) {
			this.value = new QlConstraintValueNumber(value);
			return this;
		}
		public Builder setValue(String value, String...others) {
			List<QlConstraintValueString> list = Lists.newArrayList();
			list.add(new QlConstraintValueString(value));
			for (String s : others) {
				list.add(new QlConstraintValueString(s));
			}
			this.value = new QlConstraintValueCollection<QlConstraintValueString>(list);
			return this;
		}
		public Builder setValue(Number value, Number...others) {
			List<QlConstraintValueNumber> list = Lists.newArrayList();
			list.add(new QlConstraintValueNumber(value));
			for (Number s : others) {
				list.add(new QlConstraintValueNumber(s));
			}
			this.value = new QlConstraintValueCollection<QlConstraintValueNumber>(list);
			return this;
		}
		public QlConstraintOperator getOperation() {
			return operation;
		}
		public Builder setOperation(QlConstraintOperator operation) {
			this.operation = operation;
			return this;
		}
		public QlConstraint build() {
			return new QlConstraint(key, value, operation);
		}
	}
}
