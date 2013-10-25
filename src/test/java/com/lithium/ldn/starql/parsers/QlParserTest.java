/*
 * QlParserTest.java
 * Created on Jul 8, 2013
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

import static com.lithium.ldn.starql.models.QlSortOrderType.ASC;
import static com.lithium.ldn.starql.models.QlSortOrderType.DESC;

import java.util.List;

import junit.framework.TestCase;

import com.google.common.collect.Lists;
import com.lithium.ldn.starql.exceptions.InvalidQueryException;
import com.lithium.ldn.starql.exceptions.QueryValidationException;
import com.lithium.ldn.starql.models.QlBooleanConstraintNode;
import com.lithium.ldn.starql.models.QlConstraint;
import com.lithium.ldn.starql.models.QlConstraintOperator;
import com.lithium.ldn.starql.models.QlConstraintPair;
import com.lithium.ldn.starql.models.QlConstraintPairOperator;
import com.lithium.ldn.starql.models.QlConstraintValueString;
import com.lithium.ldn.starql.models.QlField;
import com.lithium.ldn.starql.models.QlPageConstraints;
import com.lithium.ldn.starql.models.QlSelectStatement;
import com.lithium.ldn.starql.models.QlSortClause;
import com.lithium.ldn.starql.parsers.JparsecQueryMarkupManager;
import com.lithium.ldn.starql.parsers.QueryMarkupManager;

/**
 * @author David Esposito
 */
public class QlParserTest extends TestCase {
	
	private final QueryMarkupManager queryMarkupManager = new JparsecQueryMarkupManager();
	private final QlPageConstraints defaultPageConstraints = new QlPageConstraints(-1, -1);
	
	public final void test_QlParser_Simple1() throws InvalidQueryException, QueryValidationException {
		String query = "SELECT * FROM messages";
		QlSelectStatement actual = queryMarkupManager.parseQlSelect(query);
		List<QlField> fields = Lists.newArrayList(QlField.create("*"));
		QlBooleanConstraintNode constraints = null;
		QlSortClause sortClause = null;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection("messages")
				.setConstraints(constraints)
				.setSortConstraint(sortClause)
				.setPageConstraints(defaultPageConstraints)
				.build();
		assertEquals(query, expected.toString(), actual.toString());
	}
	
	public final void test_QlParser_Simple2() throws InvalidQueryException, QueryValidationException {
		String query = "SELECT id FROM messages";
		QlSelectStatement actual = queryMarkupManager.parseQlSelect(query);
		List<QlField> fields = Lists.newArrayList(QlField.create("id"));
		QlBooleanConstraintNode constraints = null;
		QlSortClause sortClause = null;
		QlSelectStatement expected = new QlSelectStatement.Builder()
			.setFields(fields)
			.setCollection("messages")
			.setConstraints(constraints)
			.setSortConstraint(sortClause)
			.setPageConstraints(defaultPageConstraints)
			.build();
		assertEquals(query, expected.toString(), actual.toString());
	}
	
	public final void test_QlParser_Simple3() throws InvalidQueryException, QueryValidationException {
		String query = "SELECT id,author,board FROM messages";
		QlSelectStatement actual = queryMarkupManager.parseQlSelect(query);
		List<QlField> fields = Lists.newArrayList(QlField.create("id"),
				QlField.create("author"),
				QlField.create("board"));
		QlBooleanConstraintNode constraints = null;
		QlSortClause sortClause = null;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection("messages")
				.setConstraints(constraints)
				.setSortConstraint(sortClause)
				.setPageConstraints(defaultPageConstraints)
				.build();
		assertEquals(query, expected.toString(), actual.toString());
	}
	
	public final void test_QlParser_WhereMatches() throws InvalidQueryException, QueryValidationException {
		String query = "SELECT * FROM messages WHERE body MATCHES 'asdf' AND subject LIKE 'fdsa'";
		QlSelectStatement actual = queryMarkupManager.parseQlSelect(query);
		List<QlField> fields = Lists.newArrayList(QlField.create("*"));
		QlBooleanConstraintNode matchesConstraint = new QlConstraint(QlField.create("body"),
				new QlConstraintValueString("asdf"), QlConstraintOperator.MATCHES);
		QlBooleanConstraintNode likeConstraint = new QlConstraint(QlField.create("subject"),
				new QlConstraintValueString("fdsa"), QlConstraintOperator.LIKE);
		QlBooleanConstraintNode constraints = new QlConstraintPair(matchesConstraint, likeConstraint,
				QlConstraintPairOperator.AND);
		QlSortClause sortClause = null;
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection("messages")
				.setConstraints(constraints)
				.setSortConstraint(sortClause)
				.setPageConstraints(defaultPageConstraints)
				.build();
		assertEquals(query, expected.toString(), actual.toString());
	}
	
	public final void test_QlParser_SimpleFail1() {
		String query = "SELECT FROM messages";
		try {
			queryMarkupManager.parseQlSelect(query);
		} catch (InvalidQueryException e) {
			return;
		} catch (QueryValidationException e) {
			e.printStackTrace();
		}
		fail("Expected Invalid Query Exception: " + query);
	}
	
	public final void test_QlParser_SimpleFail2() {
		String query = "SELECT id,author,board FROM";
		try {
			queryMarkupManager.parseQlSelect(query);
		} catch (InvalidQueryException e) {
			return;
		} catch (QueryValidationException e) {
			e.printStackTrace();
		}
		fail("Expected Invalid Query Exception: " + query);
	}
	
	public final void test_QlParser_SimpleFail3() {
		String query = "INSERT * FROM messages";
		try {
			queryMarkupManager.parseQlSelect(query);
		} catch (InvalidQueryException e) {
			return;
		} catch (QueryValidationException e) {
			e.printStackTrace();
		}
		fail("Expected Invalid Query Exception: " + query);
	}
	
	public final void test_QlParser_Sorted1() throws InvalidQueryException, QueryValidationException {
		String query = "SELECT * FROM messages ORDER BY date ASC";
		QlSelectStatement actual = queryMarkupManager.parseQlSelect(query);
		List<QlField> fields = Lists.newArrayList(QlField.create("*"));
		QlBooleanConstraintNode constraints = null;
		QlSortClause sortClause = new QlSortClause(QlField.create("date"), ASC);
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection("messages")
				.setConstraints(constraints)
				.setSortConstraint(sortClause)
				.setPageConstraints(defaultPageConstraints)
				.build();
		assertEquals(query, expected.toString(), actual.toString());
	}
	
	public final void test_QlParser_Sorted2() throws InvalidQueryException, QueryValidationException {
		String query = "SELECT id FROM messages ORDER BY date DESC";
		QlSelectStatement actual = queryMarkupManager.parseQlSelect(query);
		List<QlField> fields = Lists.newArrayList(QlField.create("id"));
		QlBooleanConstraintNode constraints = null;
		QlSortClause sortClause = new QlSortClause(QlField.create("date"), DESC);
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection("messages")
				.setConstraints(constraints)
				.setSortConstraint(sortClause)
				.setPageConstraints(defaultPageConstraints)
				.build();
		assertEquals(query, expected.toString(), actual.toString());
	}
	
	public final void test_QlParser_Sorted3() throws InvalidQueryException, QueryValidationException {
		String query = "SELECT id,author,board FROM messages ORDER BY date ASC";
		QlSelectStatement actual = queryMarkupManager.parseQlSelect(query);
		List<QlField> fields = Lists.newArrayList(QlField.create("id"),
				QlField.create("author"),
				QlField.create("board"));
		QlBooleanConstraintNode constraints = null;
		QlSortClause sortClause = new QlSortClause(QlField.create("date"), ASC);
		QlSelectStatement expected = new QlSelectStatement.Builder()
				.setFields(fields)
				.setCollection("messages")
				.setConstraints(constraints)
				.setSortConstraint(sortClause)
				.setPageConstraints(defaultPageConstraints)
				.build();
		assertEquals(query, expected.toString(), actual.toString());
	}
	
	public final void test_QlParser_SortedFail1() {
		String query = "SELECT * FROM messages ORDERBY date ASC";
		try {
			queryMarkupManager.parseQlSelect(query);
		} catch (InvalidQueryException e) {
			return;
		} catch (QueryValidationException e) {
			e.printStackTrace();
		}
		fail("Expected Invalid Query Exception: " + query);
	}
	
	public final void test_QlParser_SortedFail2() {
		String query = "SELECT * FROM messages ORDER BY ASC";
		try {
			queryMarkupManager.parseQlSelect(query);
		} catch (InvalidQueryException e) {
			return;
		} catch (QueryValidationException e) {
			e.printStackTrace();
		}
		fail("Expected Invalid Query Exception: " + query);
	}
	
	public final void test_QlParser_SortedFail3() {
		String query = "SELECT * FROM messages SORT BY date ASCENDING";
		try {
			queryMarkupManager.parseQlSelect(query);
		} catch (InvalidQueryException e) {
			return;
		} catch (QueryValidationException e) {
			e.printStackTrace();
		}
		fail("Expected Invalid Query Exception: " + query);
	}
}
