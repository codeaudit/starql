/*
 * BooleanEvaluatable.java
 * Created on May 17, 2013
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
