package com.lithium.ldn.starql.parsers;

import static com.lithium.ldn.starql.models.QlConstraintOperatorType.EQUALS;
import static com.lithium.ldn.starql.models.QlConstraintOperatorType.GREATER_THAN;
import static com.lithium.ldn.starql.models.QlConstraintOperatorType.GREATER_THAN_EQUAL;
import static com.lithium.ldn.starql.models.QlConstraintOperatorType.IN;
import static com.lithium.ldn.starql.models.QlConstraintOperatorType.LESS_THAN;
import static com.lithium.ldn.starql.models.QlConstraintOperatorType.LESS_THAN_EQUAL;
import static com.lithium.ldn.starql.models.QlConstraintOperatorType.LIKE;
import static com.lithium.ldn.starql.models.QlConstraintOperatorType.MATCHES;
import static com.lithium.ldn.starql.models.QlConstraintOperatorType.NOT_EQUALS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.codehaus.jparsec.error.ParserException;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.lithium.ldn.starql.models.QlConstraintOperator;

public class JparsecConstraintOperatorTest extends JparsecTest {

	@Test(expected = ParserException.class)
	public final void test_constraintOperator() {
			String word = "";
			inst.constraintOperatorParser().parse(word);
			Assert.fail("ConstraintOperator cannot be blank");
	}

	@Test
	public final void test_constraintOperator1() {
		assertEquals(EQUALS, inst.constraintOperatorParser().parse("="));
	}

	@Test
	public final void test_constraintOperator2() {
		assertEquals(NOT_EQUALS, inst.constraintOperatorParser().parse("!="));
	}

	@Test
	public final void test_constraintOperator3() {
		assertEquals(GREATER_THAN, inst.constraintOperatorParser().parse(">"));
	}

	@Test
	public final void test_constraintOperator4() {
		assertEquals(GREATER_THAN_EQUAL, inst.constraintOperatorParser().parse(">="));
	}

	@Test
	public final void test_constraintOperator5() {
		assertEquals(LESS_THAN, inst.constraintOperatorParser().parse("<"));
	}

	@Test
	public final void test_constraintOperator6() {
		assertEquals(LESS_THAN_EQUAL, inst.constraintOperatorParser().parse("<="));
	}

	@Test
	public final void test_constraintOperator7() {
		assertEquals(IN, inst.constraintOperatorParser().parse("IN"));
	}

	@Test
	public final void test_constraintOperator8() {
		assertEquals(IN, inst.constraintOperatorParser().parse("in"));
	}
	
	@Test
	public final void test_constraintOperator9() {
		assertEquals(MATCHES, inst.constraintOperatorParser().parse("MATCHES"));
	}
	
	@Test
	public final void test_constraintOperator10() {
		assertEquals(MATCHES, inst.constraintOperatorParser().parse("matches"));
	}
	
	@Test
	public final void test_constraintOperator11() {
		assertEquals(LIKE, inst.constraintOperatorParser().parse("LIKE"));
	}
	
	@Test
	public final void test_constraintOperator12() {
		assertEquals(LIKE, inst.constraintOperatorParser().parse("like"));
	}
	
	/*
	 * ========================================================
	 * 				CUSTOM OPERATORS
	 * ========================================================
	 */
	private static final List<DummyOperator> CUSTOM_OPS = Lists.newArrayList();
	private static ConstraintOperatorSupport<DummyOperator> CUSTOM_OP_SUPPORT;
	private static final DummyOperator MY = new DummyOperator("MY");
	private static final DummyOperator NAME = new DummyOperator("NAME");
	private static final DummyOperator IS = new DummyOperator("IS");
	private static final DummyOperator DAVID = new DummyOperator("DAVID");
	static {
		CUSTOM_OPS.add(MY);
		CUSTOM_OPS.add(NAME);
		CUSTOM_OPS.add(IS);
		CUSTOM_OPS.add(DAVID);
		CUSTOM_OP_SUPPORT = new CustomConstraintOperatorSupport<DummyOperator>(CUSTOM_OPS, false);
	}
	
	@Test
	public final void test_customConstraintOperator() {
		assertEquals(MY, inst.constraintOperatorParser(CUSTOM_OP_SUPPORT).parse("MY"));
	}
	
	@Test
	public final void test_customConstraintOperator1() {
		assertEquals(MY, inst.constraintOperatorParser(CUSTOM_OP_SUPPORT).parse("my"));
	}
	
	@Test
	public final void test_customConstraintOperator2() {
		assertEquals(NAME , inst.constraintOperatorParser(CUSTOM_OP_SUPPORT).parse("NAME"));
	}
	
	@Test
	public final void test_customConstraintOperator3() {
		assertEquals(NAME, inst.constraintOperatorParser(CUSTOM_OP_SUPPORT).parse("name"));
	}
	
	@Test
	public final void test_customConstraintOperator4() {
		assertEquals(IS, inst.constraintOperatorParser(CUSTOM_OP_SUPPORT).parse("IS"));
	}
	
	@Test
	public final void test_customConstraintOperator5() {
		assertEquals(IS, inst.constraintOperatorParser(CUSTOM_OP_SUPPORT).parse("is"));
	}
	
	@Test
	public final void test_customConstraintOperator6() {
		assertEquals(DAVID, inst.constraintOperatorParser(CUSTOM_OP_SUPPORT).parse("DAVID"));
	}
	
	@Test
	public final void test_customConstraintOperator7() {
		assertEquals(DAVID, inst.constraintOperatorParser(CUSTOM_OP_SUPPORT).parse("david"));
	}
	
	@Test(expected = ParserException.class)
	public final void test_customConstraintOperator8() {
		assertNull(inst.constraintOperatorParser(CUSTOM_OP_SUPPORT).parse("="));
	}
	
	@Test(expected = ParserException.class)
	public final void test_customConstraintOperator9() {
		assertNull(inst.constraintOperatorParser(CUSTOM_OP_SUPPORT).parse(">="));
	}
	
	@Test(expected = ParserException.class)
	public final void test_customConstraintOperator10() {
		assertNull(inst.constraintOperatorParser(CUSTOM_OP_SUPPORT).parse("MATCHES"));
	}
	
	@Test(expected = ParserException.class)
	public final void test_customConstraintOperator11() {
		assertNull(inst.constraintOperatorParser(CUSTOM_OP_SUPPORT).parse("LIKE"));
	}
	
	public static class DummyOperator implements QlConstraintOperator {
		public final String dispalyString;
		public DummyOperator(String displayString) {
			this.dispalyString = displayString;
		}
		@Override
		public String getDisplayString() {
			return dispalyString;
		}
	}
}
