package com.lithium.ldn.starql.parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.codehaus.jparsec.error.ParserException;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import com.lithium.ldn.starql.models.QlConstraintValue;
import com.lithium.ldn.starql.models.QlConstraintValueBoolean;
import com.lithium.ldn.starql.models.QlConstraintValueCollection;
import com.lithium.ldn.starql.models.QlConstraintValueDate;
import com.lithium.ldn.starql.models.QlConstraintValueNumber;
import com.lithium.ldn.starql.models.QlConstraintValueString;

public class JparsecConstraintVariableTest extends JparsecTest {

	@Test(expected = ParserException.class)
	public final void test_variable() {
			String word = "";
			inst.constraintValueParser().parse(word);
			Assert.fail("Variable cannot be blank");
	}

	@Test
	public final void test_variable1() {
		String var = "25";
		assertEquals(25, inst.constraintValueParser().parse(var).asA(QlConstraintValueNumber.class).getValue());
	}

	@Test
	public final void test_variable2() {
		String var = "3.1415";
		assertEquals(3.1415, inst.constraintValueParser().parse(var).asA(QlConstraintValueNumber.class).getValue());
	}

	@Test
	public final void test_variable3() {
		String var = "'David''s'";
		String varExpected = "David's";
		assertEquals(varExpected, inst.constraintValueParser().parse(var).asA(QlConstraintValueString.class).getValue());
	}

	@Test
	public final void test_variable4() {
		String var = "'Hello, World!'";
		String varExpected = "Hello, World!";
		assertEquals(varExpected, inst.constraintValueParser().parse(var).asA(QlConstraintValueString.class).getValue());
	}

	@Test
	public final void test_variable5() {
		String var = "'Hello''s, World''!'''";
		String varExpected = "Hello's, World'!'";
		assertEquals(varExpected, inst.constraintValueParser().parse(var).asA(QlConstraintValueString.class).getValue());
	}

	@Test
	public final void test_variable6() {
		String var = "()";
		QlConstraintValueCollection<QlConstraintValue> varExpected = new QlConstraintValueCollection<QlConstraintValue>();
		assertEquals(varExpected, inst.constraintValueParser().parse(var));
	}

	@Test
	public final void test_variable7() {
		String var = "('a','b','c')";
		QlConstraintValueCollection<QlConstraintValue> varExpected = getConstraintValueCollection("a","b","c");
		assertEquals(varExpected, inst.constraintValueParser().parse(var));
	}

	@Test
	public final void test_variable8() {
		String var = "( 1 , 2.5 )";
		QlConstraintValueCollection<QlConstraintValue> varExpected = getConstraintValueCollectionNumber(1, 2.5);
		assertEquals(varExpected, inst.constraintValueParser().parse(var));
	}

	@Test
	public final void test_variable9() {
		String var = "('a')";
		QlConstraintValueCollection<QlConstraintValue> varExpected = getConstraintValueCollection("a");
		assertEquals(varExpected, inst.constraintValueParser().parse(var));
	}

	@Test
	public final void test_variable10() {
		String var = "\"David's\"";
		String varExpected = "David's";
		assertEquals(varExpected, inst.constraintValueParser().parse(var).asA(QlConstraintValueString.class).getValue());
	}

	@Test
	public final void test_variable11() {
		String var = "\"David\\\"s\"";
		String varExpected = "David\"s";
		assertEquals(varExpected, inst.constraintValueParser().parse(var).asA(QlConstraintValueString.class).getValue());
	}

	@Test(expected = ParserException.class)
	public final void test_variable12() {
		String var = "\"Da\\vid's\"";
			inst.constraintValueParser().parse(var);
	}

	@Test(expected = ParserException.class)
	public final void test_variable13() {
		String var = "'David\"s''";
			inst.constraintValueParser().parse(var);
	}

	@Test
	public final void test_variable14() {
		String var = "'\"Hello, World!\"'";
		String varExpected = "\"Hello, World!\"";
		assertEquals(varExpected, inst.constraintValueParser().parse(var).asA(QlConstraintValueString.class).getValue());
	}

	@Test
	public final void test_variable15() {
		String var = "\"'Hello, World!'\"";
		String varExpected = "'Hello, World!'";
		assertEquals(varExpected, inst.constraintValueParser().parse(var).asA(QlConstraintValueString.class).getValue());
	}

	@Test(expected = ParserException.class)
	public final void test_variable16() {
		String var = "'\"Hello, World!'\"";
			inst.constraintValueParser().parse(var);
	}

	@Test(expected = ParserException.class)
	public final void test_variable17() {
		String var = "\"'Hello, World!\"'";
			inst.constraintValueParser().parse(var);
	}

	@Test
	public final void test_variable18() {
		DateTime now = new DateTime();
		String dateString = getFormattedDateString(now);
		
		QlConstraintValue var = inst.constraintValueParser().parse(dateString);
		assertTrue(var.isA(QlConstraintValueDate.class));
		QlConstraintValueDate dateVar = var.asA(QlConstraintValueDate.class);
		assertNotNull(dateVar);
		assertEquals(dateString, dateVar.getValueString());
	}

	@Test
	public final void test_variable19() {
		String var = "0";
		assertEquals(0, inst.constraintValueParser().parse(var).asA(QlConstraintValueNumber.class).getValue());
	}

	@Test
	public final void test_variable20() {
		String var = "0L";
		assertEquals(0L, inst.constraintValueParser().parse(var).asA(QlConstraintValueNumber.class).getValue());
	}

	@Test
	public final void test_variable21() {
		String var = "0f";
		assertEquals(0f, inst.constraintValueParser().parse(var).asA(QlConstraintValueNumber.class).getValue());
	}

	@Test(expected = ParserException.class)
	public final void test_variable22() {
		String var = "00";
		inst.constraintValueParser().parse(var);
	}

	@Test(expected = ParserException.class)
	public final void test_variable23() {
		String var = "000001";
		inst.constraintValueParser().parse(var);
	}

	@Test
	public final void test_variable24() {
		String var = "0.0f";
		assertEquals(0f, inst.constraintValueParser().parse(var).asA(QlConstraintValueNumber.class).getValue());
	}

	@Test
	public final void test_variable25() {
		String var = "0.0";
		assertEquals(0D, inst.constraintValueParser().parse(var).asA(QlConstraintValueNumber.class).getValue());
	}
	
	@Test
	public final void test_variable26() {
		String var = "true";
		assertEquals(true, inst.constraintValueParser().parse(var).asA(QlConstraintValueBoolean.class).getValue());
	}
	
	@Test
	public final void test_variable27() {
		String var = "'true'";
		assertEquals("true", inst.constraintValueParser().parse(var).asA(QlConstraintValueString.class).getValue());
	}
	
	@Test
	public final void test_variable28() {
		String var = "false";
		assertEquals(false, inst.constraintValueParser().parse(var).asA(QlConstraintValueBoolean.class).getValue());
	}
	
	@Test
	public final void test_variable29() {
		String var = "FAlse";
		assertEquals(false, inst.constraintValueParser().parse(var).asA(QlConstraintValueBoolean.class).getValue());
	}
	
	@Test
	public final void test_variable30() {
		String var = "TRUE";
		assertEquals(true, inst.constraintValueParser().parse(var).asA(QlConstraintValueBoolean.class).getValue());
	}
	
	@Test
	public final void test_variable31() {
		String var = "(true, false, tRUE)";
		QlConstraintValueCollection<QlConstraintValue> varExpected = getConstraintValueCollectionBoolean(true, false, true);
		assertEquals(varExpected, inst.constraintValueParser().parse(var).asA(QlConstraintValueCollection.class));
	}
	
	@Test
	public final void test_variable32() {
		String var = "-25";
		assertEquals(-25, inst.constraintValueParser().parse(var).asA(QlConstraintValueNumber.class).getValue());
	}
	
	@Test
	public final void test_variable33() {
		String var = "+25";
		assertEquals(25, inst.constraintValueParser().parse(var).asA(QlConstraintValueNumber.class).getValue());
	}
}
