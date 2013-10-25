package com.lithium.ldn.starql.parsers;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.error.ParserException;
import org.codehaus.jparsec.functors.Map;
import org.codehaus.jparsec.functors.Pair;
import org.codehaus.jparsec.functors.Tuple3;
import org.codehaus.jparsec.functors.Tuple5;
import org.codehaus.jparsec.pattern.Patterns;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.google.common.collect.Lists;
import com.lithium.ldn.starql.exceptions.InvalidQueryException;
import com.lithium.ldn.starql.exceptions.QueryValidationException;
import com.lithium.ldn.starql.models.QlBooleanConstraintNode;
import com.lithium.ldn.starql.models.QlConstraint;
import com.lithium.ldn.starql.models.QlConstraintOperator;
import com.lithium.ldn.starql.models.QlConstraintPairOperator;
import com.lithium.ldn.starql.models.QlConstraintValue;
import com.lithium.ldn.starql.models.QlConstraintValueCollection;
import com.lithium.ldn.starql.models.QlConstraintValueDate;
import com.lithium.ldn.starql.models.QlConstraintValueNumber;
import com.lithium.ldn.starql.models.QlConstraintValueString;
import com.lithium.ldn.starql.models.QlField;
import com.lithium.ldn.starql.models.QlPageConstraints;
import com.lithium.ldn.starql.models.QlSelectStatement;
import com.lithium.ldn.starql.models.QlSortClause;
import com.lithium.ldn.starql.models.QlSortOrderType;
import com.lithium.ldn.starql.validation.NoOpValidator;
import com.lithium.ldn.starql.validation.QlSelectStatementValidator;

/**
 * JParsec implementation of QueryMarkupManger.
 * 
 * @author David Esposito
 * @testing When making changes to this file, make sure to run JparsecQlParserTest.
 */
public class JparsecQueryMarkupManager implements QueryMarkupManager {
	
	private static final NoOpValidator NO_OP_VALIDATOR = new NoOpValidator();
	
	@Override
	public QlSelectStatement parseQlSelect(String query) throws InvalidQueryException, 
			QueryValidationException {
		return parseQlSelect(query, NO_OP_VALIDATOR);
	}

	@Override
	public QlSelectStatement parseQlSelect(String query, QlSelectStatementValidator validator) 
			throws InvalidQueryException, QueryValidationException {
		if (query == null) {
			throw new IllegalArgumentException("query cannot be null");
		}
		if (validator == null) {
			throw new IllegalArgumentException("validator cannot be null");
		}
		try {
			QlSelectStatement selectStatement = qlSelectParser().parse(query);
			validator.validate(selectStatement);
			return selectStatement;
		} catch (ParserException e) {
			throw new InvalidQueryException(e.getMessage(), query);
		}
	}
	
	/*
	 * ====================================================
	 * 		SELECT STATEMENT
	 * ====================================================
	 */	
	protected Parser<QlSelectStatement> qlSelectParser() {
		return paddedRegex("SELECT", true, false)
			.next(Parsers.tuple(fieldsParser().followedBy(paddedRegex("FROM", true, false)), 
				alphaNumeric(), 
				padWithWhitespace(whereClauseParser().optional(), true), 
				padWithWhitespace(orderByParser().optional(), false),
				padWithWhitespace(pageConstraintParser().optional(), false))
			.map(new Map<Tuple5<List<QlField>, String, QlBooleanConstraintNode, QlSortClause, QlPageConstraints>, 
					QlSelectStatement>() {
						@Override
						public QlSelectStatement map(Tuple5<List<QlField>, String, QlBooleanConstraintNode, 
								QlSortClause, QlPageConstraints> arg0) {
							return new QlSelectStatement.Builder()
									.setFields(arg0.a)
									.setCollection(arg0.b)
									.setConstraints(arg0.c)
									.setSortConstraint(arg0.d)
									.setPageConstraints(arg0.e)
									.build();
						}
			}));
	}

	/*
	 * ====================================================
	 * 		FIELDS
	 * ====================================================
	 */
	protected Parser<List<QlField>> fieldsParser() {
		return Parsers.or(fieldStarParser(), fieldCollectionParser());
	}
	
	protected Parser<List<QlField>> fieldCollectionParser() {
		return fieldOrFunctionParser().sepBy1(padWithWhitespace(regex(",", true), false));
	}
	
	protected Parser<QlField> fieldOrFunctionParser() {
		Parser.Reference<QlField> fieldOrFuncParserRef = Parser.newReference();
		Parser<QlField> fieldOrFuncParser = 				
				Parsers.or(functionParser(fieldOrFuncParserRef), fieldParser(fieldOrFuncParserRef), simpleFieldParser()).map(
				new Map<Tuple3<String, QlField, Boolean>, QlField>() {
					@Override
					public QlField map(
							Tuple3<String, QlField, Boolean> fieldInfo) {
						return QlField.create(fieldInfo.a, fieldInfo.b,
								fieldInfo.c);
					}
				});
		fieldOrFuncParserRef.lazySet(fieldOrFuncParser);
		return fieldOrFuncParser;
	}
	
	protected Parser<Tuple3<String,QlField,Boolean>> functionParser(Parser.Reference<QlField> fieldOrFuncParserRef) {
		return Parsers.tuple(alphaNumeric().followedBy(padWithWhitespace(regex("\\(", true), false)),
							insideFunctionParser(fieldOrFuncParserRef),
							Parsers.constant(true));
	}
	
	protected Parser<QlField> insideFunctionParser(Parser.Reference<QlField> fieldOrFuncParserRef) {
		return Parsers.or(fieldSingleStarParser(), fieldOrFuncParserRef.lazy()).followedBy(
				padWithWhitespace(regex("\\)", true), true));
	}
	
	protected Parser<Tuple3<String, QlField, Boolean>> fieldParser(
			Parser.Reference<QlField> fieldOrFuncParserRef) {
		return Parsers.tuple(
				alphaNumeric().followedBy(padWithWhitespace(regex("\\.", true), false)), 
				fieldOrFuncParserRef.lazy(), 
				Parsers.constant(false));

	}
	
	protected Parser<Tuple3<String,QlField,Boolean>> simpleFieldParser() {
		return Parsers.tuple(alphaNumeric(),
				Parsers.constant((QlField)null),
				Parsers.constant(false));
	}
	
	protected Parser<QlField> fieldSingleStarParser() {
		return paddedRegex("\\*", true, true)
				.map(new Map<String, QlField>() {
					@Override
					public QlField map(String arg0) {
						return QlField.create(arg0);
					}
				});
	}
	
	protected Parser<List<QlField>> fieldStarParser() {
		return paddedRegex("\\*", true, true)
				.map(new Map<String, List<QlField>>() {
					@Override
					public List<QlField> map(String arg0) {
						return Lists.newArrayList(QlField.create(arg0));
					}
				});
	}
	
	/*
	 * ====================================================
	 * 		ORDER BY
	 * ====================================================
	 */
	protected Parser<QlSortClause> orderByParser() {
		return paddedRegex("ORDER BY", false, false)
				.next(Parsers.tuple(fieldOrFunctionParser(), sortOrderTypeParser()))
				.map(new Map<Pair<QlField, QlSortOrderType>, QlSortClause>() {
						@Override
						public QlSortClause map(Pair<QlField, QlSortOrderType> arg0) {
							return new QlSortClause(arg0.a, arg0.b);
						}
				});
	}
	
	protected Parser<QlSortOrderType> sortOrderTypeParser() {
		return paddedRegex("[a-z]+", true, false)
				.map(new Map<String, QlSortOrderType>() {
					@Override
					public QlSortOrderType map(String arg0) {
						return QlSortOrderType.get(arg0);
					}
				});
	}
	
	/*
	 * ====================================================
	 * 		LIMIT OFFSET
	 * ====================================================
	 */
	protected Parser<QlPageConstraints> pageConstraintParser() {
		return Parsers.tuple(padWithWhitespace(limitParser().optional(-1), false), 
				offsetParser().optional(-1))
				.map(new Map<Pair<Integer, Integer>, QlPageConstraints>() {
					@Override
					public QlPageConstraints map(Pair<Integer, Integer> arg0) {
						return new QlPageConstraints(arg0.a, arg0.b);
					}
				});
	}
	
	protected Parser<Integer> limitParser() {
		return regexIntegerPair("LIMIT", false);
	}
	
	protected Parser<Integer> offsetParser() {
		return regexIntegerPair("OFFSET", false);
	}
	
	/*
	 * ====================================================
	 * 		WHERE
	 * ====================================================
	 */
	protected Parser<QlBooleanConstraintNode> whereClauseParser() {
		return paddedRegex("WHERE", false, false).next(constraintsParser());
	}

	protected Parser<QlBooleanConstraintNode> constraintsParser() {
		return padWithWhitespace(constraintParser(), false)
				.infixl(constraintPairOperatorParser());
	}
	
	protected Parser<QlConstraintPairOperator> constraintPairOperatorParser() {
		return regex("(AND|OR) ", false)
				.map(new Map<String, QlConstraintPairOperator>() {
					@Override
					public QlConstraintPairOperator map(String arg0) {
						return QlConstraintPairOperator.get(arg0.trim());
					}
				});
	}
	
	protected Parser<QlBooleanConstraintNode> constraintParser() {
		return Parsers.tuple(padWithWhitespace(fieldOrFunctionParser(), false), constraintOperatorParser(), 
				constraintValueParser())
				.map(new Map<Tuple3<QlField, QlConstraintOperator, QlConstraintValue>, QlBooleanConstraintNode>() {
					@Override
					public QlBooleanConstraintNode map(Tuple3<QlField, QlConstraintOperator, QlConstraintValue> arg0) {
						return new QlConstraint(arg0.a, arg0.c, arg0.b);
					}
				});
	}
	
	protected Parser<QlConstraintOperator> constraintOperatorParser() {
		return paddedRegex("(!=|=|<=|>=|<|>|IN|MATCHES|LIKE)", false, false)
				.map(new Map<String, QlConstraintOperator>() {
					@Override
					public QlConstraintOperator map(String arg0) {
						return QlConstraintOperator.get(arg0.toString());
					}
				});
	}

	/*
	 * ====================================================
	 * 		MISC
	 * ====================================================
	 */
	
	/**
	 * All added whitespace is optional. To require white space, you should add it to your parser inline.
	 * @param parser The parser to wrap in optional white space.
	 * @param leadingWhitespaces If optional white space should prepend the provided parser.
	 * @return The parser that was wrapped in optional white space.
	 */
	protected <T> Parser<T> padWithWhitespace(Parser<T> parser, boolean leadingWhitespaces) {
		return leadingWhitespaces 
				? Scanners.WHITESPACES.optional().next(parser).followedBy(Scanners.WHITESPACES.optional())
				: parser.followedBy(Scanners.WHITESPACES.optional());
	}
	
	/**
	 * This value is not padded with leading/trailing whitespace. Values can start with [a-zA-Z_].
	 * 
	 * @return The alpha-numberic string that was parsed.
	 */
	protected Parser<String> alphaNumeric() {
		return regex("[a-zA-Z_][a-zA-Z0-9_]*", false);
	}
	
	protected Parser<Integer> regexIntegerPair(String keyword, boolean isCaseSensitive) {
		return paddedRegex(keyword, false, isCaseSensitive).next(Scanners.INTEGER)
				.map(new Map<String, Integer>() {
					@Override
					public Integer map(String arg0) {
						return Integer.parseInt(arg0);
					}
				});
	}
	
	protected Parser<String> paddedRegex(String pattern, boolean leadingWhitespace, boolean isCaseSensitive) {
		return paddedRegex(pattern, "regexParser: " + pattern, leadingWhitespace, isCaseSensitive);
	}
	
	protected Parser<String> regex(String pattern, boolean isCaseSensitive) {
		return regex(pattern, "regexParser: " + pattern, isCaseSensitive);
	}
	
	protected Parser<String> paddedRegex(String pattern, String name, boolean leadingWhitespace, boolean isCaseSensitive) {
		return padWithWhitespace(regex(pattern, isCaseSensitive).source(), leadingWhitespace);
	}
	
	protected Parser<String> regex(String pattern, String name, boolean isCaseSensitive) {
		Pattern p = Pattern.compile(pattern, isCaseSensitive ? 0 : CASE_INSENSITIVE);
		return Scanners.pattern(Patterns.regex(p), name).source();
	}
	
	/**
	 * Parser for any type of constraint value.
	 * 
	 * @return The value, an integer, quoted string without the quotes, or collection.
	 */
	protected Parser<QlConstraintValue> constraintValueParser() {
		return Parsers.or(dateValueParser(),
				numericalValueParser(), 
				collectionValueParser(),
				stringValueParser()
				);
	}
	
	/**
	 * String variables may be in single or double quotes (regardless, quotes on either end must match).
	 * Single quoted strings have single quotes escaped by repeating, so use two single quotes ('').
	 * Double quoted strings are escaped with the character '\'.  Only the double quote character needs escaping.
	 * 
	 * @return A quoted string without the surrounding single quotes.
	 */
	protected Parser<QlConstraintValueString> stringValueParser() {
		return Parsers.or(Scanners.DOUBLE_QUOTE_STRING
				.map(new Map<String, QlConstraintValueString>() {
					@Override
					public QlConstraintValueString map(String arg0) {
						String ret = arg0;
						// Remove beginning and ending double quote.
						ret = ret.substring(1);
						ret = ret.substring(0, ret.length()-1);
						// Unescape double quotes, ie. "\"" becomes """, "\\"" becomes "\"".
						Pattern p = Pattern.compile("(\\\\(.))");
						Matcher m = p.matcher(ret);
						StringBuffer sb = new StringBuffer();
						while(m.find()) {
							if (m.group(2).equals("\"")) {
								m.appendReplacement(sb, "\"");
							} else if (m.group(2).equals("\\")) {
								m.appendReplacement(sb, "\\");
							} else {
								throw new ParserException(null, "stringValueParser", null);
							}
						}
						m.appendTail(sb);
						ret = sb.toString();
						return new QlConstraintValueString(ret);
					}
				}),Scanners.SINGLE_QUOTE_STRING
				.map(new Map<String, QlConstraintValueString>(){
					@Override
					public QlConstraintValueString map(String arg0) {
						String ret = arg0;
						// Remove beginning and ending single quote.
						ret = ret.substring(1);
						ret = ret.substring(0, ret.length()-1);
						// Unescape single quotes, ie. "''" becomes "'".
						ret = ret.replaceAll("''", "'");
						return new QlConstraintValueString(ret);
					}
				})
				);
	}
	
	/**
	 * A collection is a constraint value consisting of a pair of parentheses around a comma-separated
	 * list of constraint values, as defined above excluding collections, so it is not recursive.
	 * 
	 * @return ConstraintValueCollection containing ConstraintValues *MAY BE OF DIFFERENT TYPES*
	 */
	protected Parser<QlConstraintValueCollection<? extends QlConstraintValue>> collectionValueParser() {
		return padWithWhitespace(Parsers.or(stringValueParser(), numericalValueParser()), false).sepBy(padWithWhitespace(regex(",", true), false))
				.between(padWithWhitespace(regex("\\(", true), false), padWithWhitespace(regex("\\)", true), false))
				.map(new Map<List<QlConstraintValue>, QlConstraintValueCollection<? extends QlConstraintValue>>(){

					@Override
					public QlConstraintValueCollection<? extends QlConstraintValue> map(List<QlConstraintValue> arg0) {
						return new QlConstraintValueCollection<QlConstraintValue>(arg0);
					}
				});
	}
	
	/**
	 * Supports 
	 *   - int: [1-9][0-9]*, 0
	 *   - long: [1-9][0-9]*L, 0L
	 *   - double: [0-9]+\.[0-9]+
	 *   - float: [0-9]+\.[0-9]+f, 0f
	 * 
	 * @return The matched string
	 */
	protected Parser<QlConstraintValueNumber> numericalValueParser() {
		return regex("([0-9]+\\.[0-9]+f?|[1-9][0-9]*L?|0[fL]?)", false)
				.map(new Map<String, QlConstraintValueNumber>(){
					@Override
					public QlConstraintValueNumber map(String arg0) {
						if (arg0.contains("f")) {
							return new QlConstraintValueNumber(Float.parseFloat(arg0));
						}
						if (arg0.contains(".")) {
							return new QlConstraintValueNumber(Double.parseDouble(arg0));
						}
						if (arg0.contains("L")) {
							return new QlConstraintValueNumber(Long.parseLong(arg0.substring(0, arg0.length()-1)));
						}
						try {
							return new QlConstraintValueNumber(Integer.parseInt(arg0));
						}
						catch(NumberFormatException e) {
							return new QlConstraintValueNumber(Long.parseLong(arg0.substring(0, arg0.length())));
						}
					}
				});
	}
	
	
	protected Parser<QlConstraintValueDate> dateValueParser() {
		return regex("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}(Z|(\\+|-)[0-9]{2}:[0-9]{2})?", true)
				.map(new Map<String, QlConstraintValueDate>(){
					@Override
					public QlConstraintValueDate map(String arg0) {
						return new QlConstraintValueDate(ISO8601Utils.parse(arg0));
					}
				});
	}
}
