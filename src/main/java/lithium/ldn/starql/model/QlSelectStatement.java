/*
 * QlStatement.java
 * Created on May 20, 2013
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

import java.util.Collections;
import java.util.List;

import lithium.ldn.starql.QueryStatement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
public class QlSelectStatement implements QueryStatement{

	private final List<QlField> fields;
	private final String table;
	private final QlBooleanConstraintNode constraints;
	private final QlSortClause sortConstraint;
	private final String queryString;
	private final QlPageConstraints pageConstraints;

	public final List<QlField> getFields() {
		return fields;
	}
	
	public final boolean hasStarFields() {
		return fields.size() == 1 && fields.get(0).isStar();
	}

	public final String getCollection() {
		return table;
	}

	public final QlBooleanConstraintNode getConstraints() {
		return constraints;
	}
	
	public final boolean hasConstraints() {
		return constraints != null;
	}
	
	public final boolean hasSingleConstraint() {
		return hasConstraints() && constraints.isLeaf();
	}
	
	public final boolean hasSortConstraint() {
		return sortConstraint != null;
	}
	
	public final QlSortClause getSortConstraint() {
		return sortConstraint;
	}

	public String getQueryString() {
		return queryString;
	}
	
	public boolean hasLimitConstraint() {
		return pageConstraints != null && pageConstraints.getLimit() >= 0;
	}
	
	public int getLimitConstraint() {
		return hasLimitConstraint() ? pageConstraints.getLimit() : 25;
	}
	
	public boolean hasOffsetConstraint() {
		return pageConstraints != null && pageConstraints.getOffset() >= 0;
	}
	
	public int getOffsetConstraint() {
		return hasOffsetConstraint() ? pageConstraints.getOffset() : 0;
	}

	public QlSelectStatement(List<QlField> fields, String table, QlBooleanConstraintNode constraints,
			QlSortClause sortConstraint, QlPageConstraints pageConstraints) {
		this.fields = Collections.unmodifiableList(fields);
		this.table = table;
		this.constraints = constraints;
		this.sortConstraint = sortConstraint;
		this.queryString = initQueryString();
		this.pageConstraints = pageConstraints;
	}
	
	@Override
	public String toString() {
		return "QlSelectStatement [fields=" + fields + ", table=" + table + ", constraints=" + constraints
				+ ", sortConstraint=" + sortConstraint + ", queryString=" + queryString + ", pageConstraints="
				+ pageConstraints + "]";
	}

	private String initQueryString() {
		return "SELECT " + fieldsString() + " FROM " + table 
				+ " " + (hasConstraints() ? constraints.getQueryString() : "")
				+ " " + (hasSortConstraint() ? sortConstraint.getQueryString() : "")
				+ " " + (hasLimitConstraint() ? "LIMIT " + getLimitConstraint() : "")
				+ " " + (hasOffsetConstraint() ? "OFFSET " + getOffsetConstraint() : "");
		
	}
	
	private String fieldsString() {
		StringBuilder sb = new StringBuilder();
		for (QlField f : fields) {
			sb.append(f.getName()).append(",");
		}
		return sb.deleteCharAt(sb.length()-1).toString();
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
