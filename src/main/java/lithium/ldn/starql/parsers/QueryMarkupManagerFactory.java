package lithium.ldn.starql.parsers;

public abstract class QueryMarkupManagerFactory {

	private static final QueryMarkupManager QUERY_MARKUP_MANAGER = new JparsecQueryMarkupManager();
	
	private QueryMarkupManagerFactory() { }
	
	public static QueryMarkupManager get() {
		return QUERY_MARKUP_MANAGER;
	}
}
