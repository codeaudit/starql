package com.lithium.ldn.starql.parsers;

import java.util.List;

public interface ConstraintOperatorSupport<OperatorT> {

	List<String> getOperatorDisplayStrings();
	
	OperatorT convert(String operatorDisplayString);
	
	boolean isCaseSensitive();
}
