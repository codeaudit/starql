package com.lithium.ldn.starql.parsers;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lithium.ldn.starql.models.QlConstraintOperator;

public class CustomConstraintOperatorSupport<OperatorT extends QlConstraintOperator> implements ConstraintOperatorSupport<OperatorT> {

	private final Map<String, OperatorT> opMap;
	private final List<String> displayStrings;
	private final boolean caseSensitive;
	
	public CustomConstraintOperatorSupport(List<OperatorT> ops, boolean caseSensitive) {
		Map<String, OperatorT> temp = Maps.newHashMap();
		List<String> tempList = Lists.newArrayList();
		for (OperatorT op : ops) {
			String key = caseSensitive ? op.getDisplayString() : op.getDisplayString().toLowerCase();
			temp.put(key, op);
			tempList.add(key);
		}
		this.opMap = ImmutableMap.copyOf(temp);
		this.displayStrings = ImmutableList.copyOf(tempList);
		this.caseSensitive = caseSensitive;
	}

	@Override
	public List<String> getOperatorDisplayStrings() {
		return displayStrings;
	}

	@Override
	public OperatorT convert(String operatorDisplayString) {
		if (!caseSensitive) {
			operatorDisplayString = operatorDisplayString.toLowerCase();
		}
		return opMap.get(operatorDisplayString);
	}

	@Override
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

}
