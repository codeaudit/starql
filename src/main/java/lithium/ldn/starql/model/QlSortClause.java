/*
 * SortConstraint.java
 * Created on May 22, 2013
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
 * Defines the sort order of results in a *QL statement.
 * 
 * @author David Esposito
 */
public class QlSortClause {

	private final QlField field;
	private final QlSortOrderType sortOrder;

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
	
}
