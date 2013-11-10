package com.lithium.ldn.starql.parsers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.codehaus.jparsec.error.ParserException;
import org.junit.Assert;
import org.junit.Test;

import com.lithium.ldn.starql.models.QlField;

public class JparsecFieldsTest extends JparsecTest {
	
	@Test(expected = ParserException.class)
	public final void test_fields() {
			String word = "";
			inst.fieldsParser().parse(word);
			Assert.fail("Fields cannot be blank");
	}

	@Test
	public final void test_fields1() {
		runFieldsTest("message", 1, false);
	}

	@Test
	public final void test_fields2() {
		runFieldsTest("message, user", 2, false);
	}

	@Test
	public final void test_fields3() {
		runFieldsTest("message,user,id", 3, false);
	}

	@Test
	public final void test_fields4() {
		runFieldsTest("message,user, uid,       kudos", 4, false);
	}

	@Test
	public final void test_fields5() {
		runFieldsTest("*", 1, true);
	}

	private final void runFieldsTest(String fieldsString, int count, boolean isStar) {
		List<QlField> fields = inst.fieldsParser().parse(fieldsString);
		assertNotNull(fields);
		assertEquals(count, fields.size());
		assertEquals(isStar, fields.get(0).isStar());
	}
}
