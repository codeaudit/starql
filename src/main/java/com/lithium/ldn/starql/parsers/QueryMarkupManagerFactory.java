package com.lithium.ldn.starql.parsers;

/**
 * Factory to provide implementations of {@link QueryMarkupManager}.
 * @author David Esposito
 *
 */
public abstract class QueryMarkupManagerFactory {

	private static final QueryMarkupManager QUERY_MARKUP_MANAGER = new JparsecQueryMarkupManager();
	
	private QueryMarkupManagerFactory() { }
	
	public static QueryMarkupManager get() {
		return QUERY_MARKUP_MANAGER;
	}
}
