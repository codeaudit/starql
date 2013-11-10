package com.lithium.ldn.starql.parsers;

import static org.junit.Assert.assertEquals;

import org.codehaus.jparsec.error.ParserException;
import org.junit.Assert;
import org.junit.Test;

public class JparsecWordTest extends JparsecTest {

	@Test(expected = ParserException.class)
	public final void test_word() {
		String word = "";
		inst.alphaNumeric().parse(word);
		Assert.fail("Words cannot be blank");
	}

	@Test
	public final void test_word1() {
		String word = "messages";
		assertEquals(word, inst.alphaNumeric().parse(word));
	}

	@Test
	public final void test_word2() {
		String word = "users";
		assertEquals(word, inst.alphaNumeric().parse(word));
	}

	@Test
	public final void test_word3() {
		String word = "uSeRsAvAtAr";
		assertEquals(word, inst.alphaNumeric().parse(word));
	}
}
