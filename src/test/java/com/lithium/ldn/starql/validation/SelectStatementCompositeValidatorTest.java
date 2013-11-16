package com.lithium.ldn.starql.validation;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.lithium.ldn.starql.exceptions.QueryValidationException;
import com.lithium.ldn.starql.models.QlField;
import com.lithium.ldn.starql.models.QlSelectStatement;

public class SelectStatementCompositeValidatorTest {

	@Test
	public void compositeValidatorTest() throws QueryValidationException {
		DummyValidator validator1 = new DummyValidator(), validator2 = new DummyValidator();
		QlSelectStatementValidator composite = new DummyCompositeValidator(validator1, validator2);
		
		// Create a minimally functional select statement.
		composite.validate(QlSelectStatement.builder().addField(QlField.create("")).build());
		assertTrue(validator1.validationRun());
		assertTrue(validator2.validationRun());
	}
	
	private class DummyCompositeValidator extends QlSelectStatementCompositeValidator {
		
		private DummyCompositeValidator(QlSelectStatementValidator... validators) {
			super(validators);
		}
	}
	
	private class DummyValidator implements QlSelectStatementValidator {
		private boolean validationRun;
		
		private DummyValidator() {
			validationRun = false;
		}
		
		@Override
		public void validate(QlSelectStatement statement) {
			validationRun = true;
		}
		
		public boolean validationRun() {
			return validationRun;
		}
	}
}
