package com.lithium.ldn.starql.models;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Defines the LIMIT and OFFSET of a StarQL SELECT statement query, more generally,
 * defines the number of items to be returned and the starting index of the items to
 * be returned.
 * 
 * @author David Esposito
 */
public class QlPageConstraints {

	public static final QlPageConstraints ALL = new QlPageConstraints(-1, -1);
	
	private final int limit;
	private final int offset;

	/**
	 * 
	 * @return The maximum number of items to be return. Default -1 for limit not specified.
	 */
	public int getLimit() {
		return limit;
	}
	
	/**
	 * 
	 * @return The starting index of items to be returned. Default -1 for offset not specified.
	 */
	public int getOffset() {
		return offset;
	}

	public QlPageConstraints(int limit, int offset) {
		super();
		this.limit = limit;
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "PageConstraints [limit=" + limit + ", offset=" + offset + "]";
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
		private int limit = -1;
		private int offset = -1;
		public int getLimit() {
			return limit;
		}
		public Builder setLimit(int limit) {
			this.limit = limit;
			return this;
		}
		public int getOffset() {
			return offset;
		}
		public Builder setOffset(int offset) {
			this.offset = offset;
			return this;
		}
		/**
		 * 
		 * @return An immutable representation of the current state of this builder. Never {@code null}.
		 */
		public QlPageConstraints build() {
			return new QlPageConstraints(limit, offset);
		}
	}
}
