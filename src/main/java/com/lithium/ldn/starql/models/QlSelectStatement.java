package com.lithium.ldn.starql.models;

import java.util.Collections;
import java.util.List;


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.lithium.ldn.starql.QueryStatement;

/**
 * An unmutable representation of a StarQL SELECT Statement.
 * 
 * @author David Esposito
 */
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
	
	/**
	 * If this SELECT statement provided "*" as the fields to be returned.
	 *  
	 * @return True if the statement only specifies one field and the only field is "*", false 
	 */
	public final boolean hasStarFields() {
		return fields.size() == 1 && fields.get(0).isStar();
	}

	/**
	 * Get the name of the collection from which resources are being queried.
	 * 
	 * @return Name of the collection. Never {@code null}.
	 */
	public final String getCollection() {
		return table;
	}

	/**
	 * Provides the root node of the boolean constraints from the WHERE clause.
	 * @return Root node from where clause. Could be {@code null}
	 */
	public final QlBooleanConstraintNode getConstraints() {
		return constraints;
	}
	
	/**
	 * 
	 * @return True if {@code getConstraints()!=null}, {@code false} otherwise.
	 */
	public final boolean hasConstraints() {
		return constraints != null;
	}
	
	/**
	 * 
	 * @return True if the root node of constraints is not {@code null} and is not the result of
	 * an AND/OR operation of two other boolean constraints.
	 */
	public final boolean hasSingleConstraint() {
		return hasConstraints() && constraints.isLeaf();
	}
	
	public final boolean hasSortConstraint() {
		return sortConstraint != null;
	}
	
	/**
	 * 
	 * @return Sort constraint. Could be null.
	 */
	public final QlSortClause getSortConstraint() {
		return sortConstraint;
	}

	/**
	 * String representation of this POJO as a StarQL SELECT statement.
	 * @return String representation of this object. Never {@code null}, never empty.
	 */
	public String getQueryString() {
		return queryString;
	}
	
	public boolean hasLimitConstraint() {
		return pageConstraints != null && pageConstraints.getLimit() >= 0;
	}
	
	/**
	 * 
	 * @return Maximum number of results to be returned if defined. Default is 25.
	 */
	public int getLimitConstraint() {
		return hasLimitConstraint() ? pageConstraints.getLimit() : 25;
	}
	
	/**
	 * 
	 * @return Starting index results to be returned if defined. Default is 25.
	 */
	public boolean hasOffsetConstraint() {
		return pageConstraints != null && pageConstraints.getOffset() >= 0;
	}
	
	public int getOffsetConstraint() {
		return hasOffsetConstraint() ? pageConstraints.getOffset() : 0;
	}

	private QlSelectStatement(List<QlField> fields, String table, QlBooleanConstraintNode constraints,
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
	
	public static class Builder {
		private List<QlField> fields;
		private String table;
		private QlBooleanConstraintNode constraints;
		private QlSortClause sortConstraint;
		private String queryString;
		private QlPageConstraints pageConstraints;
		public List<QlField> getFields() {
			return fields;
		}
		public Builder setFields(List<QlField> fields) {
			this.fields = fields;
			return this;
		}
		public String getCollection() {
			return table;
		}
		public Builder setCollection(String collection) {
			this.table = collection;
			return this;
		}
		public QlBooleanConstraintNode getConstraints() {
			return constraints;
		}
		public Builder setConstraints(QlBooleanConstraintNode constraints) {
			this.constraints = constraints;
			return this;
		}
		public QlSortClause getSortConstraint() {
			return sortConstraint;
		}
		public Builder setSortConstraint(QlSortClause sortConstraint) {
			this.sortConstraint = sortConstraint;
			return this;
		}
		public String getQueryString() {
			return queryString;
		}
		public Builder setQueryString(String queryString) {
			this.queryString = queryString;
			return this;
		}
		public QlPageConstraints getPageConstraints() {
			return pageConstraints;
		}
		public Builder setPageConstraints(QlPageConstraints pageConstraints) {
			this.pageConstraints = pageConstraints;
			return this;
		}
		/**
		 * 
		 * @return An unmutable representation of the current state of this builder. Never {@code null}.
		 */
		public QlSelectStatement build() {
			return new QlSelectStatement(fields, table, constraints, sortConstraint, pageConstraints);
		}
	}
}
