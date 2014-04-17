package com.lithium.ldn.starql.parsers;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.google.common.collect.Lists;
import com.lithium.ldn.starql.models.QlConstraint;
import com.lithium.ldn.starql.models.QlConstraintOperatorType;
import com.lithium.ldn.starql.models.QlConstraintValue;
import com.lithium.ldn.starql.models.QlConstraintValueBoolean;
import com.lithium.ldn.starql.models.QlConstraintValueCollection;
import com.lithium.ldn.starql.models.QlConstraintValueDate;
import com.lithium.ldn.starql.models.QlConstraintValueNumber;
import com.lithium.ldn.starql.models.QlConstraintValueString;
import com.lithium.ldn.starql.models.QlField;
import com.lithium.ldn.starql.models.QlPageConstraints;
import com.lithium.ldn.starql.models.QlSortClause;
import com.lithium.ldn.starql.models.QlSortOrderType;

public abstract class JparsecTest {

	protected static final JparsecQueryMarkupManager inst = new JparsecQueryMarkupManager();
	
	protected final QlPageConstraints defaultLimit = new QlPageConstraints(10, -1);
	protected final QlPageConstraints defaultOffset = new QlPageConstraints(-1, 100);
	protected final QlPageConstraints defaultPage = new QlPageConstraints(10, 100);
	
	protected QlSortClause getSortClause(String field, QlSortOrderType order) {
		return new QlSortClause(QlField.create(field), order);
	}
	
	protected final QlConstraintValueCollection<QlConstraintValue> getConstraintValueCollection(String...values) {
		List<QlConstraintValue> vals = Lists.newArrayList();
		for (String value : values) {
			vals.add(new QlConstraintValueString(value));
		}
		return new QlConstraintValueCollection<QlConstraintValue>(vals);
	}
	
	protected final QlConstraintValueCollection<QlConstraintValue> getConstraintValueCollectionNumber(Number...values) {
		List<QlConstraintValue> vals = Lists.newArrayList();
		for (Number value : values) {
			vals.add(new QlConstraintValueNumber(value));
		}
		return new QlConstraintValueCollection<QlConstraintValue>(vals);
	}
	
	protected final QlConstraintValueCollection<QlConstraintValue> getConstraintValueCollectionBoolean(Boolean...values) {
		List<QlConstraintValue> vals = Lists.newArrayList();
		for (Boolean value : values) {
			vals.add(new QlConstraintValueBoolean(value));
		}
		return new QlConstraintValueCollection<QlConstraintValue>(vals);
	}
	
	//Use this one for recursive ql fields.
	protected final QlConstraint getStringConstraint(QlField field, String value, QlConstraintOperatorType op) {
		return new QlConstraint(field, new QlConstraintValueString(value), op);
	}
	
	//Use this one for recursive ql fields.
	protected final QlConstraint getNumberConstraint(QlField field, Number value, QlConstraintOperatorType op) {
		return new QlConstraint(field, new QlConstraintValueNumber(value), op);
	}
	
	protected final QlConstraint getStringConstraint(String key, String value, QlConstraintOperatorType op) {
		return new QlConstraint(QlField.create(key), new QlConstraintValueString(value), op);
	}
	
	protected final QlConstraint getNumberConstraint(String key, Number value, QlConstraintOperatorType op) {
		return new QlConstraint(QlField.create(key), new QlConstraintValueNumber(value), op);
	}
	
	protected final QlConstraint getDateConstraint(String key, DateTime value, QlConstraintOperatorType op) {
		return new QlConstraint(QlField.create(key), new QlConstraintValueDate(value), op);
	}
	
	protected final QlConstraint getBooleanConstraint(String key, Boolean value, QlConstraintOperatorType op) {
		return new QlConstraint(QlField.create(key), new QlConstraintValueBoolean(value), op);
	}
	
	protected final QlConstraint getCollectionConstraint(QlField field, QlConstraintOperatorType op) {
		return new QlConstraint(field, new QlConstraintValueCollection<QlConstraintValue>(), op);
	}
	
	protected final QlConstraint getStringCollectionConstraint(QlField field, QlConstraintOperatorType op, String...values) {
		return new QlConstraint(field, getConstraintValueCollection(values), op);
	}
	
	protected final QlConstraint getNumberCollectionConstraint(QlField field, QlConstraintOperatorType op, Number...values) {
		return new QlConstraint(field, getConstraintValueCollectionNumber(values), op);
	}
	
	protected final String getFormattedDateString(DateTime date) {
		return date.toString(ISODateTimeFormat.dateTime());
	}
}
