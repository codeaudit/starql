/*
 * PageConstraints.java
 * Created on Jul 2, 2013
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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author David Esposito
 */
public class QlPageConstraints {

	public static final QlPageConstraints ALL = new QlPageConstraints(-1, -1);
	
	private final int limit;
	private final int offset;

	public int getLimit() {
		return limit;
	}
	
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
		public QlPageConstraints build() {
			return new QlPageConstraints(limit, offset);
		}
	}
}
