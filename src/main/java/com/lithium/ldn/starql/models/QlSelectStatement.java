
package com.lithium.ldn.starql.models;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.lithium.ldn.starql.QueryStatement;

/**
 * An immutable representation of a StarQL SELECT Statement.
 * 
 * @author David Esposito
 */
public class QlSelectStatement implements QueryStatement{

	private final List<QlField> fields;
	private final String table;
	private final QlBooleanConstraintNode constraints;
	private final List<QlSortClause> sortConstraints;
	private final String queryString;
	private final QlPageConstraints pageConstraints;
	private final ImmutableList<QlConstraint> constraintsList;

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
	 * For getting a list of all QlConstraints. Does not includes QlConstraintPairOperators.
	 * 
	 * @return A list object containing the constraints in order from left to right according to how they appear in the
	 * query statement. Does not include constraint pair operators.
	 */
	public final List<QlConstraint> getConstraintsList() {
		return constraintsList;
	}
	
	/**
	 * Helper method for iterating through a constraint tree and compiling a list of QlConstraint is prefix order.
	 * Should possibly be in a different place, but must be called by getConstraintsPrefix().
	 * 
	 * @param constraintList
	 * @param constraintNode
	 */
	private List<QlConstraint> iterateConstraintsPrefix(List<QlConstraint> constraintList,
			QlBooleanConstraintNode constraintNode) {
		if (constraintNode != null) {
			if (constraintNode.isLeaf()) {
				constraintList.add((QlConstraint) constraintNode);
			}
			else {
				QlConstraintPair qlConstraintPair = (QlConstraintPair)constraintNode;
				iterateConstraintsPrefix(constraintList, qlConstraintPair.getLeftHandSide());
				iterateConstraintsPrefix(constraintList, qlConstraintPair.getRightHandSide());
			}
		}
		return constraintList;
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
	
	/**
	 * 
	 * @return true if there is one or more sort constraint.
	 */
	public final boolean hasSortConstraint() {
		return getSortConstraintCount() > 0;
	}
	
	/**
	 * return The number of sort constraints. 
	 */
	public final int getSortConstraintCount() {
		return sortConstraints.size();
	}
	
	/**
	 * 
	 * @return Sort constraint list in order defined. Could be empty.
	 */
	public final List<QlSortClause> getSortConstraints() {
		return sortConstraints;
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
			List<QlSortClause> sortConstraints, QlPageConstraints pageConstraints) {
		this.fields = ImmutableList.copyOf(fields);
		this.table = table;
		this.constraints = constraints;
		this.sortConstraints = ImmutableList.copyOf(sortConstraints);
		this.queryString = initQueryString();
		this.pageConstraints = pageConstraints;
		this.constraintsList = ImmutableList.copyOf(iterateConstraintsPrefix(new ArrayList<QlConstraint>(),
				constraints));
	}
	
	@Override
	public String toString() {
		return "QlSelectStatement [fields=" + fields + ", table=" + table + ", constraints=" + constraints
				+ ", sortConstraint=" + sortConstraints + ", queryString=" + queryString + ", pageConstraints="
				+ pageConstraints + "]";
	}

	private String initQueryString() {
		return "SELECT " + fieldsString() + " FROM " + table 
				+ " " + (hasConstraints() ? constraints.getQueryString() : "")
				+ " " + getSortString()
				+ " " + (hasLimitConstraint() ? "LIMIT " + getLimitConstraint() : "")
				+ " " + (hasOffsetConstraint() ? "OFFSET " + getOffsetConstraint() : "");
		
	}
	
	private String fieldsString() {
		StringBuilder sb = new StringBuilder();
		for (QlField f : fields) {
			sb.append((f.hasQualifier() ? f.getQualifier():"") + 
					f.getQualifiedName()).append(",");
		}
		return sb.deleteCharAt(sb.length()-1).toString();
	}
	
	private String getSortString() {
		StringBuilder sb = new StringBuilder();
		if (getSortConstraintCount() > 0) {
			sb.append("ORDER BY ");
			for (QlSortClause s : sortConstraints) {
				sb.append(s.getQueryString()).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static Builder builder(QlSelectStatement copy) {
		return new Builder(copy);
	}
	
	public static class Builder {
		private List<QlField> fields;
		private String table;
		private QlBooleanConstraintNode constraints;
		private List<QlSortClause> sortConstraints;
		private QlPageConstraints pageConstraints;
		private Builder() { 
			fields = Lists.newArrayList();
			sortConstraints = Lists.newArrayList();
		}
		private Builder(QlSelectStatement copy) {
			this.fields = copy.fields;
			this.table = copy.table;
			this.constraints = copy.constraints;
			this.sortConstraints = copy.sortConstraints;
			this.pageConstraints = copy.pageConstraints;
		}
		public List<QlField> getFields() {
			return fields;
		}
		public Builder setFields(List<QlField> fields) {
			this.fields = fields;
			return this;
		}
		/**
		 * Fields will be instantiated as an empty list upon creation of the Builder. If
		 * {@code setFields(null)} is called then this method will throw a NullPointerException. 
		 * @param field Fields to add.
		 * @return The current builder object for convenience.
		 */
		public Builder addField(QlField...field) {
			fields.addAll(Lists.newArrayList(field));
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
		public List<QlSortClause> getSortConstraints() {
			return sortConstraints;
		}
		public Builder setSortConstraints(List<QlSortClause> sortConstraints) {
			this.sortConstraints = sortConstraints == null 
					? new ArrayList<QlSortClause>() 
					:sortConstraints;
			return this;
		}
		/**
		 * Sort Constraints will be instantiated as an empty list upon creation of the Builder. If
		 * {@code setSortConstraints(null)} is called then this method will throw a NullPointerException. 
		 * @param sortConstraint Sort constraints to add.
		 * @return The current builder object for convenience.
		 */
		public Builder addSortConstraint(QlSortClause...sortConstraint) {
			sortConstraints.addAll(Lists.newArrayList(sortConstraint));
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
			return new QlSelectStatement(fields, table, constraints, sortConstraints, pageConstraints);
		}
	}
}
