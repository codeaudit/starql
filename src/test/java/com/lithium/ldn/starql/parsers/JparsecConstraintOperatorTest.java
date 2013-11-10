package com.lithium.ldn.starql.parsers;

import static com.lithium.ldn.starql.models.QlConstraintOperator.EQUALS;
import static com.lithium.ldn.starql.models.QlConstraintOperator.GREATER_THAN;
import static com.lithium.ldn.starql.models.QlConstraintOperator.GREATER_THAN_EQUAL;
import static com.lithium.ldn.starql.models.QlConstraintOperator.IN;
import static com.lithium.ldn.starql.models.QlConstraintOperator.LESS_THAN;
import static com.lithium.ldn.starql.models.QlConstraintOperator.LESS_THAN_EQUAL;
import static com.lithium.ldn.starql.models.QlConstraintOperator.LIKE;
import static com.lithium.ldn.starql.models.QlConstraintOperator.MATCHES;
import static com.lithium.ldn.starql.models.QlConstraintOperator.NOT_EQUALS;
import static org.junit.Assert.assertEquals;

import org.codehaus.jparsec.error.ParserException;
import org.junit.Assert;
import org.junit.Test;

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
}
