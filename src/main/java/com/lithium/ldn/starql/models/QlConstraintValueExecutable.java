package com.lithium.ldn.starql.models;

/**
 * @author Venk Subramanian
 */
public class QlConstraintValueExecutable extends QlConstraintValue {
	private QlConstraintValue delegate;
	private final String executableStatement;

	public QlConstraintValueExecutable(String executableStatement) {
		super();
		this.executableStatement = executableStatement;
	}

	public String getExecutableStatement() {
		return executableStatement;
	}

	public QlConstraintValue getDelegate() {
		return delegate;
	}

	public void setDelegate(QlConstraintValue delegate) {
		this.delegate = delegate;
	}

	@Override
	public boolean isA(Class<? extends QlConstraintValue> constraintValueClass) {
		if (delegate == null) {
			return super.isA(constraintValueClass);
		}

		return delegate.isA(constraintValueClass);
	}

	@Override
	public <ConstraintValueT extends QlConstraintValue> ConstraintValueT asA(
			Class<ConstraintValueT> constraintValueT) {
		if (delegate == null) {
			return super.asA(constraintValueT);
		}

		return delegate.asA(constraintValueT);
	}

	@Override
	public Object get() {
		if (delegate == null) {
			return null;
		}
		
		return delegate.get();
	}
}
