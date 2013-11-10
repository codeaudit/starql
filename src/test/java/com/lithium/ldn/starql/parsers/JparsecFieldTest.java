package com.lithium.ldn.starql.parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.codehaus.jparsec.error.ParserException;
import org.junit.Assert;
import org.junit.Test;

import com.lithium.ldn.starql.models.QlField;

public class JparsecFieldTest extends JparsecTest {

	@Test(expected = ParserException.class)
	public final void test_field() {
			String word = "";
			inst.fieldOrFunctionParser().parse(word);
			Assert.fail("Field cannot be blank");
	}

	@Test
	public final void test_field1() {
		String word = "messages";
		runFieldTest(word, null, word, word, false);
	}
	
	@Test
	public final void test_field1_distinct() {
		String name = "messages";
		runFieldTest("distinct messages", "distinct", name, name, false);
	}

	@Test
	public final void test_field2() {
		String word = "users";
		runFieldTest(word, null, word, word, false);
	}
	
	@Test
	public final void test_field2_distinct() {
		String name = "users";
		runFieldTest("distinct users", "distinct", name, name, false);
	}

	@Test
	public final void test_field3() {
		String word = "board.id";
		runFieldTest(word, null, "board", word, false);
	}
	
	@Test
	public final void test_field3_distinct() {
		runFieldTest("DisTinCT board.id", "DisTinCT", "board", "board.id", false);
	}

	@Test
	public final void test_field4() {
		String word = "message.parent.author.uid";
		runFieldTest(word, null, "message", word, false);
	}

	@Test
	public final void test_field4_distinct() {
		runFieldTest("DISTINCT message.parent.author.uid", "DISTINCT", "message", 
				"message.parent.author.uid", false);
	}
	
	@Test
	public final void test_field5() {
		String word = "count(*)";
		runFieldTest(word, null, "count", word, false);
	}
	
	@Test
	public final void test_field5a() {
		runFieldTest("count( * )", null, "count", "count(*)", false);
	}
	
	@Test(expected = ParserException.class)
	public final void test_field5b() {
		String word = "count (*)";
		runFieldTest(word, null, "count", word, false);
		Assert.fail("Parser should not support white space after functions");
	}
	
	@Test
	public final void test_field6() {
		String word = "count(kudos)";
		runFieldTest(word, null, "count", word, false);
	}
	
	@Test
	public final void test_field7() {
		String word = "kudos.sum(weight)";
		runFieldTest(word, null, "kudos", word, false);
	}
	
	@Test
	public final void test_field8() {
		String word = "sum(kudos.sum(weight))";
		runFieldTest(word, null, "sum", word, false);
	}
	
	@Test
	public final void test_field9() {
		String word = "crazyFunction(crazyCollection.superFunction(unexpectedInnerField))";
		runFieldTest(word, null, "crazyFunction", word, false);
	}

	private final void runFieldTest(String word, String qualifier, String name, String expectedQualifiedName, 
			boolean isStar) {
		QlField qlField = inst.qualifiedFieldOrFunctionParser().parse(word);
		assertNotNull(qlField);
		assertEquals(expectedQualifiedName, qlField.getQualifiedName());
		assertEquals(name, qlField.getName());
		assertEquals(isStar, qlField.isStar());
		assertEquals(qualifier, qlField.getQualifier());
	}

	@Test(expected = ParserException.class)
	public final void test_fieldStar() {
			String word = "";
			inst.fieldStarParser().parse(word);
			Assert.fail("FieldStar cannot be blank");
	}

	@Test
	public final void test_fieldStar1() {
		String word = "*";
		List<QlField> qlFieldList = inst.fieldStarParser().parse(word);
		assertEquals(1, qlFieldList.size());
		QlField qlField = qlFieldList.get(0);
		assertNotNull(qlField);
		assertEquals(word, qlField.getName());
		assertTrue(qlField.isStar());
	}
}
