package com.lithium.ldn.starql.postprocessors;

 /*
 *  QlPostProcessor
 * Created on  1/31/14
 *
 * Copyright 2014 Lithium Technologies, Inc. 
 * Emeryville, California, U.S.A.  All Rights Reserved.
 *
 * This software is the  confidential and proprietary information
 * of  Lithium  Technologies,  Inc.  ("Confidential Information")
 * You shall not disclose such Confidential Information and shall 
 * use  it  only in  accordance  with  the terms of  the  license 
 * agreement you entered into with Lithium.
 */

import com.lithium.ldn.starql.QueryStatement;

public interface QlPostProcessor {
	/**
	 * This method is used to do any sort of post processing on a query statement
	 * @param statement the statement to parse
	 * @return processed query statement
	 */
	public <QueryT extends QueryStatement> QueryT processQueryStatement(QueryT statement);
}
