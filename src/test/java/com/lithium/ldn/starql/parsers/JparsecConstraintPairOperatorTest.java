package com.lithium.ldn.starql.parsers;

import static com.lithium.ldn.starql.models.QlConstraintPairOperator.AND;
import static com.lithium.ldn.starql.models.QlConstraintPairOperator.OR;
import static org.junit.Assert.assertEquals;

import org.codehaus.jparsec.error.ParserException;
import org.junit.Assert;
import org.junit.Test;

public class JparsecConstraintPairOperatorTest extends JparsecTest {

	@Test(expected = ParserException.class)
	public final void test_constraintPairOperator() {
			String word = "";
			inst.constraintPairOperatorParser().parse(word);
			Assert.fail("ConstraintPairOperator cannot be blank");
	}

	@Test
	public final void test_constraintPairOperator1() {
		assertEquals(AND, inst.constraintPairOperatorParser().parse("AND "));
	}

	@Test
	public final void test_constraintPairOperator1a() {
		assertEquals(AND, inst.constraintPairOperatorParser().parse("and "));
	}

	@Test
	public final void test_constraintPairOperator2() {
		assertEquals(OR, inst.constraintPairOperatorParser().parse("OR "));
	}

	@Test
	public final void test_constraintPairOperator2a() {
		assertEquals(OR, inst.constraintPairOperatorParser().parse("or "));
	}
}
