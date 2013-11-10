Overview
========

StarQL (Star Query Language) is a library build around a SQL-like query syntax designed to encourage simple intuitive data models for Restful APIs and Service Oriented Architectures. The library includes a parser which reads, validates, and interprets plain text queries. Lithium Technologies uses StarQL for implementing Lithium Query Language (LiQL). LiQL provides advanced searching, filtering, and sorting of data for our public facing REST API. It enables developers to create engaging and dynamic communities that leverage strategically defined domain objects and custom metadata to create new innovate user experiences with unbounded flexibility and a minimal learning curve for developers. Paired with simple object models, LiQL provides the foundation for a market-leading REST API implementation.

Questies are parsed and return a POJO representing the query or throw an InvalidQueryException.

<pre><code>QueryMarkupManager queryManager = QueryMarkupManagerFactory.get();
String query = "SELECT id,size,cost FROM shoes WHERE color = 'black'";
QueryStatement statement = queryManager.parseQlSelect(query);
</code></pre>

## Paging
Paging is accomplished using <code>LIMIT</code> and <code>OFFSET</code> keywords.

### Example

Generate three queries to get the three pages of 10 users on each page starting with oldest registration date:

<pre><code>SELECT * FROM USERS ORDER BY registration_time ASC LIMIT 10
SELECT * FROM USERS ORDER BY registration_time ASC LIMIT 10 OFFSET 10
SELECT * FROM USERS ORDER BY registration_time ASC LIMIT 10 OFFSET 20
</code></pre>

## Filtering and Sorting
Define a <code>WHERE</code> clause to filter results. Constraints are defined be a field, operator and value. Supported operators include <code>=, !=, \<, \>, \<=, \>=, IN</code>. Values can be <code>Number, String, Date, Collections</code>.

Sorting is accomplished by the <code>ORDER BY</code> key words. A field and sort order must be provided. Supported sort orders are <code>ASC, DESC</code> meaning ascending and descending respectively.

### Examples
Get all messages with at least one like and from the most recently registered users on the site:

<pre><code>SELECT * FROM MESSAGES 
    WHERE likes.count \> 0 
    ORDER BY author.registration_time 
    DESC LIMIT 30</code></pre>

Get events for an activity stream that are related to the user with <code>id=123</code> from Christmas and New Years in 2013:

<pre><code>SELECT * FROM events 
    WHERE creation_time \>= 2013-12-24T00:00:00 
      AND ceation_time \<= 2013-01-01T23:59:59 
      AND creator.id = 123 
      OR related_users.id = 123 
    ORDER BY creation_time DESC 
    LIMIT 30</code></pre>

## Partial Responses
Partial repsonses can be defined by using comma seperated fields. Recursively defined sub-fields can be specified using period delimeters.

### Example
Get a mobile view of a user for display in a forum:

<code>SELECT id,name,rank,registration_time,avatar.url,avatar.size FROM users</code>

Versions
========
### 1.0
1. Parser Endpoints and Supported Clauses
  2. SELECT Statement
    3. Fields Clause
    4. Collection Clause
    5. WHERE Clause
    6. ORDER BY Clause
    7. LIMIT and OFFSET Clauses
8. Fields support simple field and unlimited number of sub-fields.
9. Where clause supportes all operators {=, !=, <, >, <=, >=, IN }
10. Order by accepts fields (simple or sub-fields).
11. Limit and Offset fully supported.

### 1.1
1. Fields supports functions.
2. Where clause supports functions. Added string search operators { LIKE, MATCHES }
3. Order by Clause supports functions.
4. Added support for query validation.

### 1.2
Skipped Version

### 1.3
1. New Parser Endpoints and Supported Clauses
  2. WHERE Clause
    3. Constraints Clause
2. Fields supports field qualifiers e.g. SELECT DISTINCT topic.id FROM messages
3. Where clause supports public endpoints with validation.
 
### 1.4 (Coming Soon)
1. Order by clause supports multiple, comma seperated, clauses.
2. Converted all Date instances to use JodaTime objects/parsers rather than java.util.Date.
