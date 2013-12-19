package com.lithium.ldn.starql.models;

import static com.lithium.ldn.starql.models.QlConstraintPairOperator.AND;
import static com.lithium.ldn.starql.models.QlConstraintPairOperator.OR;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Representation of Where clause in StarQL SELECT statement.
 * 
 * @author David Esposito
 *
 */
public class QlWhereClause {

	private final QlBooleanConstraintNode root;
	private final ImmutableList<QlConstraint> constraintsList;
	
	private QlWhereClause(QlBooleanConstraintNode root) {
		this.root = root;
		this.constraintsList = ImmutableList.copyOf(
				iterateConstraintsPrefix(new ArrayList<QlConstraint>(), root));
	}
	
	/**
	 * Get the root node of the boolean constraint tree.
	 * 
	 * @return Root node of the boolean constraint tree. Never null.
	 */
	public QlBooleanConstraintNode getRoot() {
		return root;
	}
	
	public List<QlConstraint> getConstraintsList() {
		return constraintsList;
	}
	
	/**
	 * Helper method for iterating through a constraint tree and compiling a list of QlConstraint is prefix order.
	 * Should possibly be in a different place, but must be called by getConstraintsPrefix().
	 * 
	 * @param constraintList
	 * @param constraintNode
	 */
	public static List<QlConstraint> iterateConstraintsPrefix(List<QlConstraint> constraintList,
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

	public static class Builder {
		private QlBooleanConstraintNode root;
		public QlBooleanConstraintNode getRoot() {
			return root;
		}
		/**
		 * Set the root node.
		 * @param root New root node.
		 * @return The current builder.
		 */
		public Builder setRoot(QlBooleanConstraintNode root) {
			this.root = root;
			return this;
		}
		/**
		 * AND the current root node with the supplied {@link QLConstraint} creating a new
		 * root node.
		 * @param rightHandSide The node to AND with the current root node.
		 * @return The current builder.
		 */
		public Builder and(QlConstraint rightHandSide) {
			if (root == null) {
				setRoot(rightHandSide);
			} else {
				setRoot(new QlConstraintPair(root, rightHandSide, AND));
			}
			return this;
		}
		/**
		 * OR the current root node with the supplied {@link QLConstraint} creating a new
		 * root node.
		 * @param rightHandSide The node to OR with the current root node.
		 * @return The current builder.
		 */
		public Builder or(QlConstraint rightHandSide) {
			if (root == null) {
				setRoot(rightHandSide);
			} else {
				setRoot(new QlConstraintPair(root, rightHandSide, OR));
			}
			return this;
		}
		/**
		 * Process the builder to return an unmutable POJO representation of the state of the 
		 * current builder.
		 * @return An unmutable representation of the state of the current builder.
		 */
		public QlWhereClause build() {
			return new QlWhereClause(root);
		}
	}
}
