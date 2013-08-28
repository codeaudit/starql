/*
 * QueryManager.java
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

package lithium.ldn.starql.parsers;

import lithium.ldn.starql.exceptions.InvalidQueryException;
import lithium.ldn.starql.model.QlSelectStatement;

/**
 * Converts query strings into query markup specific data structures.
 * 
 * @author David Esposito
 */
public interface QueryMarkupManager {

	public QlSelectStatement parseQlSelect(String query) throws InvalidQueryException;
}
