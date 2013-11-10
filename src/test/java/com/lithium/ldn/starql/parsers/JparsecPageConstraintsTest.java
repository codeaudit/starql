package com.lithium.ldn.starql.parsers;

import static org.junit.Assert.assertEquals;

import org.codehaus.jparsec.error.ParserException;
import org.junit.Assert;
import org.junit.Test;

import com.lithium.ldn.starql.models.QlPageConstraints;

public class JparsecPageConstraintsTest extends JparsecTest {

	@Test
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

	@Test
	public final void test_pageConstraints1() {
		assertEquals(defaultLimit,
				inst.pageConstraintParser().parse("LIMIT 10"));
	}

	@Test
	public final void test_pageConstraints2() {
		assertEquals(defaultOffset,
				inst.pageConstraintParser().parse("OFFSET 100"));
	}

	@Test
	public final void test_pageConstraints3() {
		assertEquals(defaultPage,
				inst.pageConstraintParser().parse("LIMIT 10 OFFSET 100"));
	}

	@Test
	public final void test_pageConstraints4() {
		assertEquals(defaultPage,
				inst.pageConstraintParser().parse("limit 10 offset 100"));
	}
}
