package lithium.ldn.starql.model;

import static lithium.ldn.starql.model.QlConstraintPairOperator.AND;
import static lithium.ldn.starql.model.QlConstraintPairOperator.OR;


public class QlWhereClause {

	private final QlBooleanConstraintNode root;
	
	private QlWhereClause(QlBooleanConstraintNode root) {
		this.root = root;
	}
	
	public QlBooleanConstraintNode getRoot() {
		return root;
	}

	public static class Builder {
		private QlBooleanConstraintNode root;
		public QlBooleanConstraintNode getRoot() {
			return root;
		}
		public Builder setRoot(QlBooleanConstraintNode root) {
			this.root = root;
			return this;
		}
		public Builder and(QlConstraint rightHandSide) {
			if (root == null) {
				setRoot(rightHandSide);
			} else {
				setRoot(new QlConstraintPair(root, rightHandSide, AND));
			}
			return this;
		}
		public Builder or(QlConstraint rightHandSide) {
			if (root == null) {
				setRoot(rightHandSide);
			} else {
				setRoot(new QlConstraintPair(root, rightHandSide, OR));
			}
			return this;
		}
		public QlWhereClause build() {
			return new QlWhereClause(root);
		}
	}
}
