package com.lithium.ldn.starql.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Sort orders accepted in the Star Query Language.
 * 
 * @author David Esposito
 */
public enum QlSortOrderType {

	ASC,
	DESC,
	DEFAULT;
	
	@SuppressWarnings("serial")
	public static final Map<String, QlSortOrderType> map = new HashMap<String, QlSortOrderType>() {
		{
			put("ASC",ASC);
			put("asc",ASC);
			put("ASCENDING",ASC);
			put("DESC",DESC);
			put("desc",DESC);
			put("DESCENDING",DESC);
			put("DEF", DEFAULT);
			put("def", DEFAULT);
			put("DEFAULT", DEFAULT);
		}
	};
	
	public static QlSortOrderType get(String lookupName) {
		QlSortOrderType sortOrderType = map.get(lookupName);
		return sortOrderType == null ? DEFAULT : sortOrderType;
	}
}
