package com.lithium.ldn.starql.parsers;

import static com.lithium.ldn.starql.models.QlSortOrderType.ASC;
import static com.lithium.ldn.starql.models.QlSortOrderType.DESC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.codehaus.jparsec.error.ParserException;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.lithium.ldn.starql.models.QlSortClause;

public class JparsecOrderByTest extends JparsecTest {

	@Test(expected = ParserException.class)
	public final void test_sortOrderType() {
			String word = "";
			inst.sortOrderTypeParser().parse(word);
			Assert.fail("SortOrderType cannot be blank");
	}

	@Test
	public final void test_sortOrderType1() {
		assertEquals(ASC, inst.sortOrderTypeParser().parse("ASC"));
	}

	@Test
	public final void test_sortOrderType2() {
		assertEquals(ASC, inst.sortOrderTypeParser().parse("asc"));
	}

	@Test
	public final void test_sortOrderType3() {
		assertEquals(DESC, inst.sortOrderTypeParser().parse("DESC"));
	}

	@Test
	public final void test_sortOrderType4() {
		assertEquals(DESC, inst.sortOrderTypeParser().parse("desc"));
	}
	
	/*
	 * ===================================================
	 * 		ORDER BY
	 * ===================================================
	 */
	@Test(expected = ParserException.class)
	public final void test_sortOrder() {
			String word = "";
			inst.sortOrderTypeParser().parse(word);
			Assert.fail("SortOrder cannot be blank");
	}

	@Test
	public final void test_sortOrder1() {
		runSortOrderTest("ORDER BY date ASC", getSortClause("date", ASC));
	}

	@Test
	public final void test_sortOrder2() {
		runSortOrderTest("ORDER BY email DESC", getSortClause("email", DESC));
	}

	@Test
	public final void test_sortOrder3() {
		runSortOrderTest("order by email desc", getSortClause("email", DESC));
	}

	@Test
	public final void test_sortOrder4() {
		runSortOrderTest("ORDER BY date ASC, time ASC", getSortClause("date", ASC),
				getSortClause("time", ASC));
	}

	@Test
	public final void test_sortOrder5() {
		runSortOrderTest("ORDER BY email DESC, login ASC", getSortClause("email", DESC),
				getSortClause("login", ASC));
	}

	@Test
	public final void test_sortOrder6() {
		runSortOrderTest("order by email desc, login ASC, date desc", getSortClause("email", DESC),
				getSortClause("login", ASC), getSortClause("date", DESC));
	}
	
	protected final void runSortOrderTest(String query, QlSortClause...expected) {
		runSortOrderTest(query, Lists.newArrayList(expected));
	}
	
	protected final void runSortOrderTest(String query, List<QlSortClause> expected) {
		List<QlSortClause> actual = inst.orderByParser().parse(query);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
}
