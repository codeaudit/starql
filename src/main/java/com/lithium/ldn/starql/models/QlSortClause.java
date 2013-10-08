package com.lithium.ldn.starql.models;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * Defines the sort order of results in a StarQL statement.
 * 
 * @author David Esposito
 */
public class QlSortClause {

	private final QlField field;
	private final QlSortOrderType sortOrder;

	/**
	 * 
	 * @return The field to sort on. Never {@code null}.
	 */
	public QlField getField() {
		return field;
	}

	public final QlSortOrderType getSortOrder() {
		return sortOrder;
	}

	public QlSortClause(String field, QlSortOrderType sortOrder) {
		this(new QlField(field), sortOrder);
	}

	public QlSortClause(QlField field, QlSortOrderType sortOrder) {
		this.field = field;
		this.sortOrder = sortOrder;
	}

	@Override
	public String toString() {
		return "SortConstraint [field=" + field + ", sortOrder=" + sortOrder + "]";
	}

	/**
	 * Returns a string representation of the current object as it would appear in
	 * a StarQL SELECT statement.
	 * @return A string representation of the current object.
	 */
	public String getQueryString() {
		return "SORTBY " + field.getName() + " " + sortOrder.name();
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
		private QlField field;
		private QlSortOrderType order;
		public QlField getField() {
			return field;
		}
		public Builder setField(QlField field) {
			this.field = field;
			return this;
		}
		public Builder setField(String name, String...subFieldNames) {
			this.field = new QlField(name, subFieldNames);
			return this;
		}
		public QlSortOrderType getOrder() {
			return order;
		}
		public Builder setOrder(QlSortOrderType order) {
			this.order = order;
			return this;
		}
		public Builder setOrder(String lookupName) {
			this.order = QlSortOrderType.get(lookupName);
			return this;
		}
		/**
		 * @return An unmutable representation of the current state of this builder.
		 */
		public QlSortClause build() {
			return new QlSortClause(field, order);
		}
	}
}
