/*
 * SortOrderType.java
 * Created on May 28, 2013
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author David Esposito
 */
public enum QlSortOrderType {

	ASC,
	DESC,
	UNKNOWN;
	
	@SuppressWarnings("serial")
	public static final Map<String, QlSortOrderType> map = new HashMap<String, QlSortOrderType>() {
		{
			put("ASC",ASC);
			put("asc",ASC);
			put("ASCENDING",ASC);
			put("DESC",DESC);
			put("desc",DESC);
			put("DESCENDING",DESC);
		}
	};
	
	public static QlSortOrderType get(String lookupName) {
		QlSortOrderType sortOrderType = map.get(lookupName);
		return sortOrderType == null ? UNKNOWN : sortOrderType;
	}
}
