package com.lithium.ldn.starql.models;

/**
 * Used to build a constraint tree for *QL statements. Each node will be a pair 
 * of constraints or a constraint its self. The pair will be evaluated by an AND/OR
 * operator. This allows for nesting of search constraints:
 * 
 * a AND b OR c AND d
 * 
 *         AND
 *        /   \
 *      OR     d
 *     /  \
 *   AND   b
 *  /   \
 * a     b
 * 
 * @author David Esposito
 */
public interface QlBooleanConstraintNode {

	public boolean isLeaf();
	
	public QlConstraintOperator getConstraintOperator();
	
	public QlConstraintPairOperator getConstraintPairOperator();
	
	public String getQueryString();
}
