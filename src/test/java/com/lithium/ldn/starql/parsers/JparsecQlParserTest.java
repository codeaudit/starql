/*
 * JparsecQlParserTest.java
 * Created on Jul 16, 2013
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

package com.lithium.ldn.starql.parsers;

import static com.lithium.ldn.starql.models.QlConstraintOperator.EQUALS;
import static com.lithium.ldn.starql.models.QlConstraintOperator.GREATER_THAN;
import static com.lithium.ldn.starql.models.QlConstraintOperator.GREATER_THAN_EQUAL;
import static com.lithium.ldn.starql.models.QlConstraintOperator.IN;
import static com.lithium.ldn.starql.models.QlConstraintOperator.LESS_THAN;
import static com.lithium.ldn.starql.models.QlConstraintOperator.LESS_THAN_EQUAL;
import static com.lithium.ldn.starql.models.QlConstraintOperator.NOT_EQUALS;
import static com.lithium.ldn.starql.models.QlConstraintOperator.MATCHES;
import static com.lithium.ldn.starql.models.QlConstraintOperator.LIKE;
import static com.lithium.ldn.starql.models.QlConstraintPairOperator.AND;
import static com.lithium.ldn.starql.models.QlConstraintPairOperator.OR;
import static com.lithium.ldn.starql.models.QlSortOrderType.ASC;
import static com.lithium.ldn.starql.models.QlSortOrderType.DESC;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.codehaus.jparsec.error.ParserException;
import org.junit.Assert;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.google.common.collect.Lists;
import com.lithium.ldn.starql.exceptions.InvalidQueryException;
import com.lithium.ldn.starql.exceptions.QueryValidationException;
import com.lithium.ldn.starql.models.QlBooleanConstraintNode;
import com.lithium.ldn.starql.models.QlConstraint;
import com.lithium.ldn.starql.models.QlConstraintOperator;
import com.lithium.ldn.starql.models.QlConstraintPair;
import com.lithium.ldn.starql.models.QlConstraintValue;
import com.lithium.ldn.starql.models.QlConstraintValueCollection;
import com.lithium.ldn.starql.models.QlConstraintValueDate;
import com.lithium.ldn.starql.models.QlConstraintValueNumber;
import com.lithium.ldn.starql.models.QlConstraintValueString;
import com.lithium.ldn.starql.models.QlField;
import com.lithium.ldn.starql.models.QlPageConstraints;
import com.lithium.ldn.starql.models.QlSelectStatement;
import com.lithium.ldn.starql.models.QlSortClause;
import com.lithium.ldn.starql.models.QlSortOrderType;
import com.lithium.ldn.starql.parsers.JparsecQueryMarkupManager;

/**
 * @author David Esposito
 */
public class JparsecQlParserTest extends TestCase {
	
	private static final JparsecQueryMarkupManager inst = new JparsecQueryMarkupManager();

	private final QlPageConstraints defaultLimit = new QlPageConstraints(10, -1);
	private final QlPageConstraints defaultOffset = new QlPageConstraints(-1, 100);
	private final QlPageConstraints defaultPage = new QlPageConstraints(10, 100);

	/*
	 * ===================================================
	 * 		WORD
	 * ===================================================
	 */
	public final void test_word() {
		try {
			String word = "";
			inst.alphaNumeric().parse(word);
			Assert.fail("Words cannot be blank");
		} catch (ParserException e) { }
	}
	
	public final void test_word1() {
		String word = "messages";
		Assert.assertEquals(word, inst.alphaNumeric().parse(word));
	}
	
	public final void test_word2() {
		String word = "users";
		Assert.assertEquals(word, inst.alphaNumeric().parse(word));
	}
	
	public final void test_word3() {
		String word = "uSeRsAvAtAr";
		Assert.assertEquals(word, inst.alphaNumeric().parse(word));
	}

	/*
	 * ===================================================
	 * 		FIELD
	 * ===================================================
	 */
	public final void test_field() {
		try {
			String word = "";
			inst.fieldParser().parse(word);
			Assert.fail("Field cannot be blank");
		} catch (ParserException e) { }
	}
	
	public final void test_field1() {
		runFieldTest("messages", "messages", false);
	}
	
	public final void test_field2() {
		runFieldTest("users", "users", false);
	}
	
	public final void test_field3() {
		runFieldTest("board.id", "board", false);
	}
	
	public final void test_field4() {
		runFieldTest("message.parent.author.uid", "message", false);
	}
	
	private final void runFieldTest(String word, String name, boolean isStar) {
		QlField qlField = inst.fieldParser().parse(word);
		Assert.assertNotNull(qlField);
		Assert.assertEquals(word, qlField.getQualifiedName());
		Assert.assertEquals(name, qlField.getName());
		Assert.assertEquals(isStar, qlField.isStar());
	}
	
	public final void test_fieldStar() {
		try {
			String word = "";
			inst.fieldStarParser().parse(word);
			Assert.fail("FieldStar cannot be blank");
		} catch (ParserException e) { }
	}

	public final void test_fieldStar1() {
		String word = "*";
		List<QlField> qlFieldList = inst.fieldStarParser().parse(word);
		Assert.assertEquals(1, qlFieldList.size());
		QlField qlField = qlFieldList.get(0);
		Assert.assertNotNull(qlField);
		Assert.assertEquals(word, qlField.getName());
		Assert.assertTrue(qlField.isStar());
	}

	/*
	 * ===================================================
	 * 		FIELDS
	 * ===================================================
	 */
	public final void test_fields() {
		try {
			String word = "";
			inst.fieldsParser().parse(word);
			Assert.fail("Fields cannot be blank");
		} catch (ParserException e) { }
	}

	public final void test_fields1() {
		runFieldsTest("message", 1, false);
	}
	
	public final void test_fields2() {
		runFieldsTest("message, user", 2, false);
	}
	
	public final void test_fields3() {
		runFieldsTest("message,user,id", 3, false);
	}
	
	public final void test_fields4() {
		runFieldsTest("message,user, uid,       kudos", 4, false);
	}
	
	public final void test_fields5() {
		runFieldsTest("*", 1, true);
	}
	
	private final void runFieldsTest(String fieldsString, int count, boolean isStar) {
		List<QlField> fields = inst.fieldsParser().parse(fieldsString);
		Assert.assertNotNull(fields);
		Assert.assertEquals(count, fields.size());
		Assert.assertEquals(isStar, fields.get(0).isStar());
	}
	
	/*
	 * ===================================================
	 * 		ORDER BY TYPE
	 * ===================================================
	 */
	public final void test_sortOrderType() {
		try {
			String word = "";
			inst.sortOrderTypeParser().parse(word);
			Assert.fail("SortOrderType cannot be blank");
		} catch (ParserException e) { }
	}
	
	public final void test_sortOrderType1() {
		Assert.assertEquals(ASC, inst.sortOrderTypeParser().parse("ASC"));
	}
	
	public final void test_sortOrderType2() {
		Assert.assertEquals(ASC, inst.sortOrderTypeParser().parse("asc"));
	}
	
	public final void test_sortOrderType3() {
		Assert.assertEquals(DESC, inst.sortOrderTypeParser().parse("DESC"));
	}
	
	public final void test_sortOrderType4() {
		Assert.assertEquals(DESC, inst.sortOrderTypeParser().parse("desc"));
	}

	/*
	 * ===================================================
	 * 		ORDER BY
	 * ===================================================
	 */
	public final void test_sortOrder() {
		try {
			String word = "";
			inst.sortOrderTypeParser().parse(word);
			Assert.fail("SortOrder cannot be blank");
		} catch (ParserException e) { }
	}
	
	public final void test_sortOrder1() {
		runSortOrderTest("ORDER BY date ASC", getSortClause("date", ASC));
	}

	public final void test_sortOrder2() {
		runSortOrderTest("ORDER BY email DESC", getSortClause("email", DESC));
	}

	public final void test_sortOrder3() {
		runSortOrderTest("order by email desc", getSortClause("email", DESC));
	}
	
	private QlSortClause getSortClause(String field, QlSortOrderType order) {
		return new QlSortClause(new QlField(field), order);
	}
	
	private final void runSortOrderTest(String query, QlSortClause expected) {
		QlSortClause actual = inst.orderByParser().parse(query);
		Assert.assertNotNull(actual);
		Assert.assertEquals(expected, actual);
	}

	/*
	 * ===================================================
	 * 		PAGE CONSTRAINTS
	 * ===================================================
	 */
	public final void test_pageConstraints() {
		try {
			String word = "";
			QlPageConstraints pageConstraint = inst.pageConstraintParser().parse(word);
			assertEquals(-1, pageConstraint.getLimit());
			assertEquals(-1, pageConstraint.getOffset());
		} catch (ParserException e) {
			Assert.fail("PageConstraints can be blank");
		}
	}
	
	public final void test_pageConstraints1() {
		Assert.assertEquals(defaultLimit, 
				inst.pageConstraintParser().parse("LIMIT 10"));
	}
	
	public final void test_pageConstraints2() {
		Assert.assertEquals(defaultOffset, 
				inst.pageConstraintParser().parse("OFFSET 100"));
	}
	
	public final void test_pageConstraints3() {
		Assert.assertEquals(defaultPage, 
				inst.pageConstraintParser().parse("LIMIT 10 OFFSET 100"));
	}
	
	public final void test_pageConstraints4() {
		Assert.assertEquals(defaultPage, 
				inst.pageConstraintParser().parse("limit 10 offset 100"));
	}

	/*
	 * ===================================================
	 * 		CONSTRAINT OPERATORS
	 * ===================================================
	 */
	public final void test_constraintOperator() {
		try {
			String word = "";
			inst.constraintOperatorParser().parse(word);
			Assert.fail("ConstraintOperator cannot be blank");
		} catch (ParserException e) { }
	}
	
	public final void test_constraintOperator1() {
		assertEquals(EQUALS, inst.constraintOperatorParser().parse("="));
	}

	public final void test_constraintOperator2() {
		assertEquals(NOT_EQUALS, inst.constraintOperatorParser().parse("!="));
	}

	public final void test_constraintOperator3() {
		assertEquals(GREATER_THAN, inst.constraintOperatorParser().parse(">"));
	}

	public final void test_constraintOperator4() {
		assertEquals(GREATER_THAN_EQUAL, inst.constraintOperatorParser().parse(">="));
	}

	public final void test_constraintOperator5() {
		assertEquals(LESS_THAN, inst.constraintOperatorParser().parse("<"));
	}

	public final void test_constraintOperator6() {
		assertEquals(LESS_THAN_EQUAL, inst.constraintOperatorParser().parse("<="));
	}
	
	public final void test_constraintOperator7() {
		assertEquals(IN, inst.constraintOperatorParser().parse("IN"));
	}
	
	public final void test_constraintOperator8() {
		assertEquals(IN, inst.constraintOperatorParser().parse("in"));
	}

	/*
	 * ===================================================
	 * 		CONSTRAINT PAIR OPERATORS
	 * ===================================================
	 */
	public final void test_constraintPairOperator() {
		try {
			String word = "";
			inst.constraintPairOperatorParser().parse(word);
			Assert.fail("ConstraintPairOperator cannot be blank");
		} catch (ParserException e) { }
	}
	
	public final void test_constraintPairOperator1() {
		assertEquals(AND, inst.constraintPairOperatorParser().parse("AND "));
	}
	
	public final void test_constraintPairOperator1a() {
		assertEquals(AND, inst.constraintPairOperatorParser().parse("and "));
	}
	
	public final void test_constraintPairOperator2() {
		assertEquals(OR, inst.constraintPairOperatorParser().parse("OR "));
	}
	
	public final void test_constraintPairOperator2a() {
		assertEquals(OR, inst.constraintPairOperatorParser().parse("or "));
	}

	/*
	 * ===================================================
	 * 		VARIABLE
	 * ===================================================
	 */
	public final void test_variable() {
		try {
			String word = "";
			inst.constraintValueParser().parse(word);
			Assert.fail("Variable cannot be blank");
		} catch (ParserException e) { }
	}
	
	public final void test_variable1() {
		String var = "25";
		Assert.assertEquals(25, inst.constraintValueParser().parse(var).asA(QlConstraintValueNumber.class).getValue());
	}
	
	public final void test_variable2() {
		String var = "3.1415";
		Assert.assertEquals(3.1415, inst.constraintValueParser().parse(var).asA(QlConstraintValueNumber.class).getValue());
	}
	
	public final void test_variable3() {
		String var = "'David''s'";
		String varExpected = "David's";
		Assert.assertEquals(varExpected, inst.constraintValueParser().parse(var).asA(QlConstraintValueString.class).getValue());
	}
	
	public final void test_variable4() {
		String var = "'Hello, World!'";
		String varExpected = "Hello, World!";
		Assert.assertEquals(varExpected, inst.constraintValueParser().parse(var).asA(QlConstraintValueString.class).getValue());
	}
	
	public final void test_variable5() {
		String var = "'Hello''s, World''!'''";
		String varExpected = "Hello's, World'!'";
		Assert.assertEquals(varExpected, inst.constraintValueParser().parse(var).asA(QlConstraintValueString.class).getValue());
	}
	
	public final void test_variable6() {
		String var = "()";
		QlConstraintValueCollection<QlConstraintValue> varExpected = new QlConstraintValueCollection<QlConstraintValue>();
		Assert.assertEquals(varExpected, inst.constraintValueParser().parse(var));
	}
	
	public final void test_variable7() {
		String var = "('a','b','c')";
		QlConstraintValueCollection<QlConstraintValue> varExpected = getConstraintValueCollection("a","b","c");
		Assert.assertEquals(varExpected,  inst.constraintValueParser().parse(var));
	}
	
	public final void test_variable8() {
		String var = "( 1 , 2.5 )";
		QlConstraintValueCollection<QlConstraintValue> varExpected = getConstraintValueCollectionNumber(1, 2.5);
		Assert.assertEquals(varExpected,  inst.constraintValueParser().parse(var));
	}
	
	public final void test_variable9() {
		String var = "('a')";
		QlConstraintValueCollection<QlConstraintValue> varExpected = getConstraintValueCollection("a");
		Assert.assertEquals(varExpected,  inst.constraintValueParser().parse(var));
	}
	
	public final void test_variable10() {
		String var = "\"David's\"";
		String varExpected = "David's";
		Assert.assertEquals(varExpected, inst.constraintValueParser().parse(var).asA(QlConstraintValueString.class).getValue());
	}
	
	public final void test_variable11() {
		String var = "\"David\\\"s\"";
		String varExpected = "David\"s";
		Assert.assertEquals(varExpected, inst.constraintValueParser().parse(var).asA(QlConstraintValueString.class).getValue());
	}
	
	public final void test_variable12() {
		String var = "\"Da\\vid's\"";
		Exception expected = null;
		try {
			inst.constraintValueParser().parse(var);
		} catch (ParserException e) {
			expected = e;
		}
		Assert.assertNotNull(expected);
	}
	
	public final void test_variable13() {
		String var = "'David\"s''";
		Exception expected = null;
		try {
			inst.constraintValueParser().parse(var);
		} catch (ParserException e) {
			expected = e;
		}
		Assert.assertNotNull(expected);
	}
	
	public final void test_variable14() {
		String var = "'\"Hello, World!\"'";
		String varExpected = "\"Hello, World!\"";
		Assert.assertEquals(varExpected, inst.constraintValueParser().parse(var).asA(QlConstraintValueString.class).getValue());
	}
	
	public final void test_variable15() {
		String var = "\"'Hello, World!'\"";
		String varExpected = "'Hello, World!'";
		Assert.assertEquals(varExpected, inst.constraintValueParser().parse(var).asA(QlConstraintValueString.class).getValue());
	}
	
	public final void test_variable16() {
		String var = "'\"Hello, World!'\"";
		Exception expected = null;
		try {
			inst.constraintValueParser().parse(var);
		} catch (ParserException e) {
			expected = e;
		}
		Assert.assertNotNull(expected);
	}
	
	public final void test_variable17() {
		String var = "\"'Hello, World!\"'";
		Exception expected = null;
		try {
			inst.constraintValueParser().parse(var);
		} catch (ParserException e) {
			expected = e;
		}
		Assert.assertNotNull(expected);
	}
	
	public final void test_variable18() {
		Date now = new Date();
		String dateString = ISO8601Utils.format(now);
		QlConstraintValue var = inst.constraintValueParser().parse(dateString);
		assertTrue(var.isA(QlConstraintValueDate.class));
		QlConstraintValueDate dateVar = var.asA(QlConstraintValueDate.class);
		assertNotNull(dateVar);
		assertEquals(dateString, dateVar.getValueString());
	}

	public final void test_variable19() {
		String var = "0";
		Assert.assertEquals(0, inst.constraintValueParser().parse(var).asA(QlConstraintValueNumber.class).getValue());
	}

	public final void test_variable20() {
		String var = "0L";
		Assert.assertEquals(0L, inst.constraintValueParser().parse(var).asA(QlConstraintValueNumber.class).getValue());
	}

	private final QlConstraintValueCollection<QlConstraintValue> getConstraintValueCollection(String...values) {
		List<QlConstraintValue> vals = Lists.newArrayList();
		for (String value : values) {
			vals.add(new QlConstraintValueString(value));
		}
		return new QlConstraintValueCollection<QlConstraintValue>(vals);
	}
	
	private final QlConstraintValueCollection<QlConstraintValue> getConstraintValueCollectionNumber(Number...values) {
		List<QlConstraintValue> vals = Lists.newArrayList();
		for (Number value : values) {
			vals.add(new QlConstraintValueNumber(value));
		}
		return new QlConstraintValueCollection<QlConstraintValue>(vals);
	}
	
	/*
	 * ===================================================
	 * 		CONSTRAINT
	 * ===================================================
	 */
	public final void test_constraint() {
		try {
			String word = "";
			inst.constraintParser().parse(word);
			Assert.fail("Constraint cannot be blank");
		} catch (ParserException e) { }
	}
	
	public final void test_constraint1() {
		runConstraintTest("name = 'david'", getStringConstraint(new QlField("name"), "david", EQUALS));
	}
	
	public final void test_constraint2() {
		runConstraintTest("email!='david.esposito@lithium.com'", 
				getStringConstraint(new QlField("email"), "david.esposito@lithium.com", NOT_EQUALS));
	}
	
	public final void test_constraint3() {
		runConstraintTest("age>25", getNumberConstraint(new QlField("age"), 25, GREATER_THAN));
	}
	
	public final void test_constraint4() {
		runConstraintTest("average<=2.7182818f", 
				getNumberConstraint(new QlField("average"), new Float(2.7182818), LESS_THAN_EQUAL));
	}
	
	public final void test_constraint5() {
		runConstraintTest("board.id IN ('a','b','c')",
				getStringCollectionConstraint(new QlField("board", "id"), IN, "a", "b", "c"));
	}
	
	public final void test_constraint6() {
		runConstraintTest("user.uid IN (1,2,3)",
				getNumberCollectionConstraint(new QlField("user", "uid"), IN, 1, 2, 3));
	}
	
	public final void test_constraint7() {
		runConstraintTest("subject IN ()",
				getCollectionConstraint(new QlField("subject"), IN));
	}
	
	public final void test_constraint8() {
		runConstraintTest("age>25000000000000L", getNumberConstraint(new QlField("age"), Long.parseLong("25000000000000"), GREATER_THAN));
	}
	
	public final void test_constraint9() {
		runConstraintTest("body MATCHES 'asdf'", getStringConstraint(new QlField("body"), "asdf", MATCHES));
	}
	
	public final void test_constraint10() {
		runConstraintTest("body LIKE ('asdf', 'fdsa')", getStringCollectionConstraint(new QlField("body"), LIKE, "asdf", "fdsa"));
	}
	
	//Use this one for recursive ql fields.
	private final QlConstraint getStringConstraint(QlField field, String value, QlConstraintOperator op) {
		return new QlConstraint(field, new QlConstraintValueString(value), op);
	}
	
	//Use this one for recursive ql fields.
	private final QlConstraint getNumberConstraint(QlField field, Number value, QlConstraintOperator op) {
		return new QlConstraint(field, new QlConstraintValueNumber(value), op);
	}
	
	private final QlConstraint getStringConstraint(String key, String value, QlConstraintOperator op) {
		return new QlConstraint(new QlField(key), new QlConstraintValueString(value), op);
	}
	
	private final QlConstraint getNumberConstraint(String key, Number value, QlConstraintOperator op) {
		return new QlConstraint(new QlField(key), new QlConstraintValueNumber(value), op);
	}
	
	private final QlConstraint getDateConstraint(String key, Date value, QlConstraintOperator op) {
		return new QlConstraint(new QlField(key), new QlConstraintValueDate(value), op);
	}
	
	private final QlConstraint getCollectionConstraint(QlField field, QlConstraintOperator op) {
		return new QlConstraint(field, new QlConstraintValueCollection<QlConstraintValue>(), op);
	}
	
	private final QlConstraint getStringCollectionConstraint(QlField field, QlConstraintOperator op, String...values) {
		return new QlConstraint(field, getConstraintValueCollection(values), op);
	}
	
	private final QlConstraint getNumberCollectionConstraint(QlField field, QlConstraintOperator op, Number...values) {
		return new QlConstraint(field, getConstraintValueCollectionNumber(values), op);
	}
	
	private final void runConstraintTest(String query, QlConstraint expected) {
		QlBooleanConstraintNode actual = inst.constraintParser().parse(query);
		Assert.assertEquals(expected, actual);
	}
	
	/*
	 * ===================================================
	 * 		CONSTRAINTS
	 * ===================================================
	 */
	public final void test_constraints() {
		try {
			String word = "";
			inst.constraintsParser().parse(word);
			Assert.fail("Constraints cannot be blank");
		} catch (ParserException e) { }
	}
	
	public final void test_constraints1() {
		QlBooleanConstraintNode expected = getStringConstraint("email", "david.esposito@lithium.com", NOT_EQUALS);
		runConstraintsTest("email!='david.esposito@lithium.com'", expected);
	}

	public final void test_constraints2() {
		QlBooleanConstraintNode expected = getNumberConstraint("age", 25, GREATER_THAN);
		runConstraintsTest("age>25", expected);
	}

	public final void test_constraints2a() {
		Date date = new Date();
		QlBooleanConstraintNode expected = getDateConstraint("date", date, GREATER_THAN);
		runConstraintsTest("date>" + ISO8601Utils.format(date), expected);
	}

	public final void test_constraints2b() {
		Date date = new Date();
		QlBooleanConstraintNode expected = getDateConstraint("date", date, LESS_THAN);
		runConstraintsTest("date<" + ISO8601Utils.format(date), expected);
	}

	public final void test_constraints3() {
		QlBooleanConstraintNode expected = new QlConstraintPair(
				getNumberConstraint("a", 5, EQUALS), 
				getStringConstraint("b", "c", EQUALS), 
				AND);
		runConstraintsTest("a=5 AND b='c'", expected);
	}

	public final void test_constraints4() {
		QlBooleanConstraintNode temp = new QlConstraintPair(
				getNumberConstraint("a", 5, EQUALS), 
				getStringConstraint("b", "c", EQUALS),
				AND);
		QlBooleanConstraintNode expected = new QlConstraintPair(
				temp, 
				getNumberConstraint("d", 18, EQUALS), 
				AND);
		runConstraintsTest("a=5 AND b='c' AND d=18", expected);
	}

	public final void test_constraints5() {
		QlBooleanConstraintNode temp = new QlConstraintPair(
				getNumberConstraint("a", 5, EQUALS), 
				getStringConstraint("b", "cdef", EQUALS),
				AND);
		QlBooleanConstraintNode expected = new QlConstraintPair(
				temp, 
				getNumberConstraint("g", 18, EQUALS), 
				OR);
		runConstraintsTest("a=5 AND b='cdef' OR g=18", expected);
	}

	public final void test_constraints6() {
		QlBooleanConstraintNode temp1 = new QlConstraintPair(
				getNumberConstraint("a", 5, EQUALS), 
				getStringConstraint("b", "c", EQUALS),
				AND);
		QlBooleanConstraintNode temp2 = new QlConstraintPair(
				temp1, 
				getNumberConstraint("d", 18, EQUALS),
				OR);
		QlBooleanConstraintNode temp3 = new QlConstraintPair(
				temp2, 
				getNumberConstraint("w", 5, EQUALS),
				AND);
		QlBooleanConstraintNode temp4 = new QlConstraintPair(
				temp3, 
				getStringConstraint("x", "y", EQUALS),
				AND);
		QlBooleanConstraintNode expected = new QlConstraintPair(
				temp4, 
				getNumberConstraint("z", 18, EQUALS), 
				OR);
		runConstraintsTest("a=5 AND b='c' OR d=18 AND w=5 AND x='y' OR z=18", expected);
	}
	
	private final void runConstraintsTest(String query, QlBooleanConstraintNode expected) {
		QlBooleanConstraintNode actual = inst.constraintsParser().parse(query);
		Assert.assertEquals(expected, actual);
	}

	/*
	 * ===================================================
	 * 		*QL SELECT STATEMENTS
	 * ===================================================
	 */
	public final void test_qlSelectStatement() {
		try {
			String word = "";
			inst.qlSelectParser().parse(word);
			Assert.fail("*QL SelectStatement cannot be blank");
		} catch (ParserException e) { }
	}
	
	public final void test_qlSelectStatement_simple() {
		String query = "SELECT * FROM users";
		List<QlField> fields = getFields();
		String table = "users";
		QlBooleanConstraintNode constraints = null;
		QlSortClause sortConstraint = null;
		QlPageConstraints pageConstraints = QlPageConstraints.ALL;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	public final void test_qlSelectStatement_simple1() {
		String query = "select * from users";
		List<QlField> fields = getFields();
		String table = "users";
		QlBooleanConstraintNode constraints = null;
		QlSortClause sortConstraint = null;
		QlPageConstraints pageConstraints = QlPageConstraints.ALL;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	private List<QlField> getFields(String...fieldNames) {
		if (fieldNames.length == 0) {
			return Lists.newArrayList(new QlField("*"));
		}
		List<QlField> fields = Lists.newArrayList();
		for (String name : fieldNames) {
			fields.add(new QlField(name));
		}
		return fields;
	}
	
	public final void test_qlSelectStatement_fields() {
		String query = "SELECT uid,login, email,firstName FROM users";
		List<QlField> fields = getFields("uid", "login", "email", "firstName");
		String table = "users";
		QlBooleanConstraintNode constraints = null;
		QlSortClause sortConstraint = null;
		QlPageConstraints pageConstraints = QlPageConstraints.ALL;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	/*
	 * ===================================================
	 * 		SORTED *QL SELECT STATEMENTS
	 * ===================================================
	 */
	public final void test_qlSelectStatement_simpleOrderBy() {
		String query = "SELECT * FROM users ORDER BY date ASC";
		List<QlField> fields = getFields();
		String table = "users";
		QlBooleanConstraintNode constraints = null;
		QlSortClause sortConstraint = getSortClause("date", ASC);
		QlPageConstraints pageConstraints = QlPageConstraints.ALL;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	public final void test_qlSelectStatement_fieldsOrderBy() {
		String query = "SELECT uid,login, email,firstName FROM users ORDER BY email DESC";
		List<QlField> fields = getFields("uid", "login", "email", "firstName");
		String table = "users";
		QlBooleanConstraintNode constraints = null;
		QlSortClause sortConstraint = getSortClause("email", DESC);
		QlPageConstraints pageConstraints = QlPageConstraints.ALL;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}

	/*
	 * ===================================================
	 * 		*QL SELECT STATEMENTS WITH LIMIT/OFFSET
	 * ===================================================
	 */
	public final void test_qlSelectStatement_simpleLimit() {
		String query = "SELECT * FROM users LIMIT 10";
		List<QlField> fields = getFields();
		String table = "users";
		QlBooleanConstraintNode constraints = null;
		QlSortClause sortConstraint = null;
		QlPageConstraints pageConstraints = defaultLimit;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	public final void test_qlSelectStatement_fieldsLimitOffset() {
		String query = "SELECT uid,login, email,firstName FROM users LIMIT 30 OFFSET 10";
		List<QlField> fields = getFields("uid", "login", "email", "firstName");
		String table = "users";
		QlBooleanConstraintNode constraints = null;
		QlSortClause sortConstraint = null;
		QlPageConstraints pageConstraints = new QlPageConstraints(30, 10);
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	public final void test_qlSelectStatement_simpleSortLimit() {
		String query = "SELECT * FROM users ORDER BY date ASC LIMIT 100";
		List<QlField> fields = getFields();
		String table = "users";
		QlBooleanConstraintNode constraints = null;
		QlSortClause sortConstraint = getSortClause("date", ASC);
		QlPageConstraints pageConstraints = new QlPageConstraints(100, -1);
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	public final void test_qlSelectStatement_fieldsSortLimitOffset() {
		String query = "SELECT uid,login, email,firstName FROM users ORDER BY email DESC LIMIT 100 OFFSET 1000";
		List<QlField> fields = getFields("uid", "login", "email", "firstName");
		String table = "users";
		QlBooleanConstraintNode constraints = null;
		QlSortClause sortConstraint = getSortClause("email", DESC);
		QlPageConstraints pageConstraints = new QlPageConstraints(100, 1000);
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	/*
	 * ===================================================
	 * 		*QL SELECT STATEMENTS WITH BOOLEAN CONSTRAINTS
	 * ===================================================
	 */
	public final void test_qlSelectStatement_simpleSingleWhere() {
		String query = "SELECT * FROM users WHERE email='david.esposito@lithium.com'";
		List<QlField> fields = getFields();
		String table = "users";
		QlBooleanConstraintNode constraints = getStringConstraint("email", "david.esposito@lithium.com", EQUALS);
		QlSortClause sortConstraint = null;
		QlPageConstraints pageConstraints = QlPageConstraints.ALL;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	public final void test_qlSelectStatement_fieldsTwoWhere() {
		String query = "SELECT uid,login, email,firstName FROM users WHERE name='david' AND age<50";
		List<QlField> fields = getFields("uid", "login", "email", "firstName");
		String table = "users";
		QlBooleanConstraintNode constraints = new QlConstraintPair(
				getStringConstraint("name", "david", EQUALS),
				getNumberConstraint("age", 50, LESS_THAN),
				AND);
		QlSortClause sortConstraint = null;
		QlPageConstraints pageConstraints = QlPageConstraints.ALL;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	public final void test_qlSelectStatement_simpleDateWhere() {
		Date start = new Date(new Date().getTime() - (60 * 60 * 24));
		Date end = new Date();
		String startString = ISO8601Utils.format(start);
		String endString = ISO8601Utils.format(end);
		String query = String.format("SELECT * FROM users WHERE date >= %1$s AND date <= %2$s", 
				startString, endString);
		List<QlField> fields = getFields();
		String table = "users";
		QlBooleanConstraintNode constraints = new QlConstraintPair(
				getDateConstraint("date", start, GREATER_THAN_EQUAL),
				getDateConstraint("date", end, LESS_THAN_EQUAL),
				AND);
		QlSortClause sortConstraint = null;
		QlPageConstraints pageConstraints = QlPageConstraints.ALL;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			QlSelectStatement parseQlSelect = inst.parseQlSelect(query);
			Assert.assertEquals(expected, parseQlSelect);
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	public final void test_qlSelectStatement_fieldsDateWhere() {
		Date start = new Date(new Date().getTime() - (60 * 60 * 24));
		Date end = new Date();
		String startString = ISO8601Utils.format(start);
		String endString = ISO8601Utils.format(end);
		String query = String.format("SELECT uid,login, email,firstName FROM users WHERE registerDate >= %1$s AND registerDate <= %2$s", 
				startString, endString);
		List<QlField> fields = getFields("uid", "login", "email", "firstName");
		String table = "users";
		QlBooleanConstraintNode constraints = new QlConstraintPair(
				getDateConstraint("registerDate", start, GREATER_THAN_EQUAL),
				getDateConstraint("registerDate", end, LESS_THAN_EQUAL),
				AND);
		QlSortClause sortConstraint = null;
		QlPageConstraints pageConstraints = QlPageConstraints.ALL;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			QlSelectStatement parseQlSelect = inst.parseQlSelect(query);
			Assert.assertEquals(expected, parseQlSelect);
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	// HERE
	public final void test_qlSelectStatement_simpleSingleWhereSort() {
		String query = "SELECT * FROM users WHERE email='david.esposito@lithium.com' ORDER BY date ASC";
		List<QlField> fields = getFields();
		String table = "users";
		QlBooleanConstraintNode constraints = getStringConstraint("email", "david.esposito@lithium.com", EQUALS);
		QlSortClause sortConstraint = getSortClause("date", ASC);
		QlPageConstraints pageConstraints = QlPageConstraints.ALL;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	// HERE
	public final void test_qlSelectStatement_fieldsTwoWhereSort() {
		String query = "SELECT uid,login, email,firstName FROM users WHERE name='david' AND age<50 ORDER BY email DESC";
		List<QlField> fields = getFields("uid", "login", "email", "firstName");
		String table = "users";
		QlBooleanConstraintNode constraints = new QlConstraintPair(
				getStringConstraint("name", "david", EQUALS),
				getNumberConstraint("age", 50, LESS_THAN),
				AND);
		QlSortClause sortConstraint = getSortClause("email", DESC);
		QlPageConstraints pageConstraints = QlPageConstraints.ALL;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	public final void test_qlSelectStatement_fieldsTwoWhereLimit() {
		String query = "SELECT uid,login, email,firstName FROM users WHERE name='david' AND age<50 LIMIT 50";
		List<QlField> fields = getFields("uid", "login", "email", "firstName");
		String table = "users";
		QlBooleanConstraintNode constraints = new QlConstraintPair(
				getStringConstraint("name", "david", EQUALS),
				getNumberConstraint("age", 50, LESS_THAN),
				AND);
		QlSortClause sortConstraint = null;
		QlPageConstraints pageConstraints = new QlPageConstraints(50, -1);
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	public final void test_qlSelectStatement_simpleThreeWhereLimit() {
		String query = "SELECT * FROM users WHERE name='david' AND age<50 OR login!='davidE' LIMIT 10";
		List<QlField> fields = getFields();
		String table = "users";
		QlBooleanConstraintNode constraints = new QlConstraintPair(
				new QlConstraintPair(
						getStringConstraint("name", "david", EQUALS), 
						getNumberConstraint("age", 50, LESS_THAN), 
						AND), 
				getStringConstraint("login", "davidE", NOT_EQUALS), 
				OR);
		QlSortClause sortConstraint = null;
		QlPageConstraints pageConstraints = new QlPageConstraints(10, -1);
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	public final void test_qlSelectStatement_fieldsThreeLimitOffset() {
		String query = "SELECT uid,login, email,firstName FROM users WHERE name='david' AND age<50 OR login!='davidE' LIMIT 30 OFFSET 10";
		List<QlField> fields = getFields("uid", "login", "email", "firstName");
		String table = "users";
		QlBooleanConstraintNode constraints = new QlConstraintPair(
				new QlConstraintPair(
						getStringConstraint("name", "david", EQUALS), 
						getNumberConstraint("age", 50, LESS_THAN), 
						AND), 
				getStringConstraint("login", "davidE", NOT_EQUALS), 
				OR);
		QlSortClause sortConstraint = null;
		QlPageConstraints pageConstraints = new QlPageConstraints(30, 10);
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	// HERE
	public final void test_qlSelectStatement_fieldsThreeSort() {
		String query = "SELECT uid,login, email,firstName FROM users WHERE name='david' AND age<50 OR login!='davidE' ORDER BY age DESC";
		List<QlField> fields = getFields("uid", "login", "email", "firstName");
		String table = "users";
		QlBooleanConstraintNode constraints = new QlConstraintPair(
				new QlConstraintPair(
						getStringConstraint("name", "david", EQUALS), 
						getNumberConstraint("age", 50, LESS_THAN), 
						AND), 
				getStringConstraint("login", "davidE", NOT_EQUALS), 
				OR);
		QlSortClause sortConstraint = getSortClause("age", DESC);
		QlPageConstraints pageConstraints = QlPageConstraints.ALL;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection(table)
				.setConstraints(constraints)
				.setSortConstraint(sortConstraint)
				.setPageConstraints(pageConstraints)
				.build();
		try {
			Assert.assertEquals(expected, inst.parseQlSelect(query));
		} catch (InvalidQueryException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		} catch (QueryValidationException e) {
			Assert.fail(e.getMessage() + "\n" + e.getStackTrace());
		}
	}
}
