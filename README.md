Overview
========

StarQL (Star Query Language) is a library build around a SQL-like query syntax designed to encourage simple intuitive data models for Restful APIs and Service Oriented Architectures. The library includes a parser which reads, validates, and interprets plain text queries. Lithium Technologies uses StarQL for implementing Lithium Query Language (LiQL). LiQL provides advanced searching, filtering, and sorting of data for our public facing REST API. It enables developers to create engaging and dynamic communities that leverage strategically defined domain objects and custom metadata to create new innovate user experiences with unbounded flexibility and a minimal learning curve for developers.

#### Maven
<pre><code>&lt;dependency>
    &lt;groupId>com.lithium&lt;/groupId>
    &lt;artifactId>dev-ldn-starql&lt;/artifactId>
    &lt;version>${starql.version}&lt;/version>
&lt;/dependency><code></pre>

#### Usage
Queries are parsed and return a POJO representing the query or throw an InvalidQueryException.

<pre><code>QueryMarkupManager queryManager = QueryMarkupManagerFactory.get();
String query = "SELECT id,size,cost FROM shoes WHERE color = 'black'";
QueryStatement statement = queryManager.parseQlSelect(query);
</code></pre>

#### More Information
See the [Wiki](https://github.com/lithiumtech/starql/wiki/Home) for more information and [examples](https://github.com/lithiumtech/starql/wiki/Examples).
