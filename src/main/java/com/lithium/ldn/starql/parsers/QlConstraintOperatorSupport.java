package com.lithium.ldn.starql.parsers;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.lithium.ldn.starql.models.QlConstraintOperatorType;

public class QlConstraintOperatorSupport implements ConstraintOperatorSupport<QlConstraintOperatorType> {

	private List<String> displayStrings;
	
	public QlConstraintOperatorSupport() {
		List<String> temp = Lists.newArrayList();
		for (QlConstraintOperatorType op : QlConstraintOperatorType.values()) {
			temp.add(op.getName());
		}
		this.displayStrings = ImmutableList.copyOf(temp);
	}

	@Override
	public List<String> getOperatorDisplayStrings() {
		return displayStrings;
	}

	@Override
	public QlConstraintOperatorType convert(String operatorName) {
		return QlConstraintOperatorType.get(operatorName);
	}

	@Override
	public boolean isCaseSensitive() {
		return false;
	}

}
