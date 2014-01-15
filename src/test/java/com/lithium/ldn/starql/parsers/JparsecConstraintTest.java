package com.lithium.ldn.starql.parsers;

import static com.lithium.ldn.starql.models.QlConstraintOperatorType.EQUALS;
import static com.lithium.ldn.starql.models.QlConstraintOperatorType.GREATER_THAN;
import static com.lithium.ldn.starql.models.QlConstraintOperatorType.IN;
import static com.lithium.ldn.starql.models.QlConstraintOperatorType.LESS_THAN_EQUAL;
import static com.lithium.ldn.starql.models.QlConstraintOperatorType.LIKE;
import static com.lithium.ldn.starql.models.QlConstraintOperatorType.MATCHES;
import static com.lithium.ldn.starql.models.QlConstraintOperatorType.NOT_EQUALS;
import static org.junit.Assert.assertEquals;

import org.codehaus.jparsec.error.ParserException;
import org.junit.Assert;
import org.junit.Test;

import com.lithium.ldn.starql.models.QlBooleanConstraintNode;
import com.lithium.ldn.starql.models.QlConstraint;
import com.lithium.ldn.starql.models.QlField;

public class JparsecConstraintTest extends JparsecTest {

	@Test(expected = ParserException.class)
	public final void test_constraint() {
			String word = "";
			inst.constraintParser().parse(word);
			Assert.fail("Constraint cannot be blank");
	}

	@Test
	public final void test_constraint1() {
		runConstraintTest("name = 'david'", getStringConstraint(QlField.create("name"), "david", EQUALS));
	}

	@Test
	public final void test_constraint2() {
		runConstraintTest("email!='david.esposito@lithium.com'", 
				getStringConstraint(QlField.create("email"), "david.esposito@lithium.com", NOT_EQUALS));
	}

	@Test
	public final void test_constraint3() {
		runConstraintTest("age>25", getNumberConstraint(QlField.create("age"), 25, GREATER_THAN));
	}

	@Test
	public final void test_constraint4() {
		runConstraintTest("average<=2.7182818f", 
				getNumberConstraint(QlField.create("average"), new Float(2.7182818), LESS_THAN_EQUAL));
	}

	@Test
	public final void test_constraint5() {
		runConstraintTest("board.id IN ('a','b','c')",
				getStringCollectionConstraint(QlField.create(null, "board", QlField.create("id"), false), IN, "a", "b", "c"));
	}

	@Test
	public final void test_constraint6() {
		runConstraintTest("user.uid IN (1,2,3)",
				getNumberCollectionConstraint(QlField.create(null, "user", QlField.create("uid"), false), IN, 1, 2, 3));
	}

	@Test
	public final void test_constraint7() {
		runConstraintTest("subject IN ()",
				getCollectionConstraint(QlField.create("subject"), IN));
	}

	@Test
	public final void test_constraint8() {
		runConstraintTest("age>25000000000000L", getNumberConstraint(QlField.create("age"), Long.parseLong("25000000000000"), GREATER_THAN));
	}

	@Test
	public final void test_constraint9() {
		runConstraintTest("body MATCHES 'asdf'", getStringConstraint(QlField.create("body"), "asdf", MATCHES));
	}
	
	@Test
	public final void test_constraint10() {
		runConstraintTest("body LIKE 'fdsa'", getStringConstraint(QlField.create("body"), "fdsa", LIKE));
	}

	@Test
	public final void test_constraint11() {
		runConstraintTest("body LIKE ('asdf', 'fdsa')", getStringCollectionConstraint(QlField.create("body"), LIKE, "asdf", "fdsa"));
	}
	
	private final void runConstraintTest(String query, QlConstraint expected) {
		QlBooleanConstraintNode actual = inst.constraintParser().parse(query);
		assertEquals(expected, actual);
	}
}
