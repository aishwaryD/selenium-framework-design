package com.bikroy.framework.utilities.textutilities;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bikroy.framework.globals.GlobalEnumerations.LogicalOperators;
import com.bikroy.framework.globals.GlobalEnumerations.TextComparators;
import com.bikroy.framework.globals.Logger;

public final class TextUtilities {
	// Initialize private logger object
	private static Logger logger = new Logger().getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/**
	 * The empty String <code>""</code>.
	 */
	public static final String EMPTY = "";

	/**
	 * <p>
	 * The maximum size to which the padding constant(s) can expand.
	 * </p>
	 */
	private static final int PAD_LIMIT = 8192;

	/**
	 * Compares value with other value based on TextComparator property
	 * 
	 * @param SourceValueToCompare
	 *            -> Source Value with which target values would be compared
	 * @param TargetValuesCompareWith
	 *            -> List of Target Values which needs to be compared in Source
	 *            value
	 * @param ignoreCase
	 *            -> Defines flag whether comparison should be case-insensitive
	 *            or not
	 * @param comparator
	 *            -> Defines Operator on basis of which values would be compared
	 * @param logicalOperator
	 *            -> Defines Logical Condition on basis of which all source
	 *            values would be compared in Target value
	 * @return <b>true :-> </b>Successful comparison<br>
	 *         <b>false:-> </b>Unsuccessful comparison
	 */
	public static synchronized final boolean compareValue(final String SourceValueToCompare, final List<String> TargetValuesCompareWith,
			final boolean ignoreCase, final TextComparators comparator, final LogicalOperators logicalOperator) {
		boolean valueMatched = true;

		if (TargetValuesCompareWith.isEmpty()) {
			if (TextUtilities.isEmpty(SourceValueToCompare)) {
				return valueMatched;
			}

			return false;
		}

		TextUtilities.logger.fullDebug("Performing Logical Operation: " + logicalOperator.toString());
		switch (logicalOperator) {
		case AND:
			// Consider value is matched for AND operation because
			// logical operation is calculated on basis of &&
			valueMatched = true;
			for (String targetValue : TargetValuesCompareWith) {
				valueMatched = valueMatched && TextUtilities.compareValue(SourceValueToCompare, targetValue, ignoreCase, comparator);
				if (valueMatched == false) {
					break;
				}
			}

			break;
		case OR:
			// Consider value is not matched for AND operation because
			// logical operation is calculated on basis of &&
			valueMatched = false;
			for (String targetValue : TargetValuesCompareWith) {
				valueMatched = valueMatched || TextUtilities.compareValue(SourceValueToCompare, targetValue, ignoreCase, comparator);
				if (valueMatched) {
					break;
				}
			}
			break;
		case NOT:
		case NA:
			break;
		}

		return valueMatched;
	}

	/**
	 * Compares value with other value based on TextComparator property
	 * 
	 * @param SourceValueToCompare
	 *            -> Source Value which needs to be compared
	 * @param TargetValueCompareWith
	 *            -> Target Value with which source value would be compared
	 * @param ignoreCase
	 *            -> Defines flag whether comparison should be case-insensitive
	 *            or not
	 * @param comparator
	 *            -> Defines Operator on basis of which values would be compared
	 * @return <b>true :-> </b>Successful comparison<br>
	 *         <b>false:-> </b>Unsuccessful comparison
	 */
	public static synchronized final boolean compareValue(String SourceValueToCompare, String TargetValueCompareWith,
			final boolean ignoreCase, final TextComparators comparator) {
		boolean valueMatched = false;

		// Check if values provided are not null; if null, make value blank for
		// comparison
		SourceValueToCompare = TextUtilities.nullToBlank(SourceValueToCompare, true);
		TargetValueCompareWith = TextUtilities.nullToBlank(TargetValueCompareWith, true);

		// Check if values needs to be compared by ignoring case or not; if yes,
		// change case to UPPER case for comparison
		SourceValueToCompare = (ignoreCase) ? SourceValueToCompare.toUpperCase() : SourceValueToCompare;
		TargetValueCompareWith = (ignoreCase) ? TargetValueCompareWith.toUpperCase() : TargetValueCompareWith;

		// Perform comparison based on provided TextComparator
		switch (comparator) {
		case NA:
			break;
		case contains:
			valueMatched = TextUtilities.contains(TargetValueCompareWith, SourceValueToCompare);
			break;
		case equals:
			valueMatched = TextUtilities.equals(TargetValueCompareWith, SourceValueToCompare);
			break;
		case exists:
			valueMatched = TargetValueCompareWith.length() > 0;
			break;
		case missing:
			valueMatched = TargetValueCompareWith.length() <= 0;
			break;
		case notContains:
			valueMatched = TextUtilities.containsNot(TargetValueCompareWith, SourceValueToCompare);
			break;
		case notEquals:
			valueMatched = TextUtilities.equalsNot(TargetValueCompareWith, SourceValueToCompare);
			break;
		case endsWith:
			valueMatched = TextUtilities.endsWith(TargetValueCompareWith, SourceValueToCompare);
			break;
		case startsWith:
			valueMatched = TextUtilities.startsWith(TargetValueCompareWith, SourceValueToCompare);
			break;
		}

		return valueMatched;
	}

	/**
	 * <p>
	 * Checks if String contains a search String, handling <code>null</code>.
	 * This method uses {@link String#indexOf(String)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>false</code>.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.contains(null, *)     = false
	 * TextUtilities.contains(*, null)     = false
	 * TextUtilities.contains(&quot;&quot;, &quot;&quot;)      = true
	 * TextUtilities.contains(&quot;abc&quot;, &quot;&quot;)   = true
	 * TextUtilities.contains(&quot;abc&quot;, &quot;a&quot;)  = true
	 * TextUtilities.contains(&quot;abc&quot;, &quot;z&quot;)  = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @return true if the String contains the search String, false if not or
	 *         <code>null</code> string input
	 */
	public static synchronized final boolean contains(final String str, final String searchStr) {
		if ((str == null) || (searchStr == null)) {
			return false;
		}
		return str.indexOf(searchStr) >= 0;
	}

	/**
	 * <p>
	 * Checks if String does not contain search String, handling
	 * <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>false</code>.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.containsNot(null, *)     = true
	 * TextUtilities.containsNot(*, null)     = true
	 * TextUtilities.containsNot(&quot;&quot;, &quot;&quot;)      = false
	 * TextUtilities.containsNot(&quot;abc&quot;, &quot;&quot;)   = false
	 * TextUtilities.containsNot(&quot;abc&quot;, &quot;a&quot;)  = false
	 * TextUtilities.containsNot(&quot;abc&quot;, &quot;z&quot;)  = true
	 * TextUtilities.containsNot(&quot;aishwarya&quot;, &quot;asw&quot;)  = true
	 * TextUtilities.containsNot(&quot;aishwarya&quot;, &quot;asw&quot;)  = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @return true if the String does not contain the search String or
	 *         <code>null</code> string input, false if contains
	 */
	public static synchronized final boolean containsNot(final String str, final String searchStr) {
		if ((str == null) || (searchStr == null)) {
			return true;
		}

		return !TextUtilities.contains(str, searchStr);
	}

	/**
	 * <p>
	 * Check if a String ends with a specified suffix.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered to be equal. The comparison is case sensitive.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.endsWith(null, null)      = true
	 * TextUtilities.endsWith(null, &quot;def&quot;)     = false
	 * TextUtilities.endsWith(&quot;abcdef&quot;, null)  = false
	 * TextUtilities.endsWith(&quot;abcdef&quot;, &quot;def&quot;) = true
	 * TextUtilities.endsWith(&quot;ABCDEF&quot;, &quot;def&quot;) = false
	 * TextUtilities.endsWith(&quot;ABCDEF&quot;, &quot;cde&quot;) = false
	 * </pre>
	 * 
	 * @see java.lang.String#endsWith(String)
	 * @param str
	 *            the String to check, may be null
	 * @param suffix
	 *            the suffix to find, may be null
	 * @return <code>true</code> if the String ends with the suffix, case
	 *         sensitive, or both <code>null</code>
	 */
	public static synchronized final boolean endsWith(final String str, final String suffix) {
		return TextUtilities.endsWith(str, suffix, false);
	}

	/**
	 * <p>
	 * Check if a String ends with a specified suffix (optionally case
	 * insensitive).
	 * </p>
	 * 
	 * @see java.lang.String#endsWith(String)
	 * @param str
	 *            the String to check, may be null
	 * @param suffix
	 *            the suffix to find, may be null
	 * @param ignoreCase
	 *            indicates whether the compare should ignore case (case
	 *            insensitive) or not.
	 * @return <code>true</code> if the String starts with the prefix or both
	 *         <code>null</code>
	 */
	private static boolean endsWith(final String str, final String suffix, final boolean ignoreCase) {
		if ((str == null) || (suffix == null)) {
			return ((str == null) && (suffix == null));
		}
		if (suffix.length() > str.length()) {
			return false;
		}
		int strOffset = str.length() - suffix.length();
		return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
	}

	// Equals
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Compares two Strings, returning <code>true</code> if they are equal.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered to be equal. The comparison is case sensitive.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.equals(null, null)   = true
	 * TextUtilities.equals(null, &quot;abc&quot;)  = false
	 * TextUtilities.equals(&quot;abc&quot;, null)  = false
	 * TextUtilities.equals(&quot;abc&quot;, &quot;abc&quot;) = true
	 * TextUtilities.equals(&quot;abc&quot;, &quot;ABC&quot;) = false
	 * </pre>
	 * 
	 * @see java.lang.String#equals(Object)
	 * @param str1
	 *            the first String, may be null
	 * @param str2
	 *            the second String, may be null
	 * @return <code>true</code> if the Strings are equal, case sensitive, or
	 *         both <code>null</code>
	 */
	public static synchronized final boolean equals(final String str1, final String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	/**
	 * <p>
	 * Compares two Strings, returning <code>true</code> if they are equal
	 * ignoring the case.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered equal. Comparison is case insensitive.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.equalsIgnoreCase(null, null)   = true
	 * TextUtilities.equalsIgnoreCase(null, &quot;abc&quot;)  = false
	 * TextUtilities.equalsIgnoreCase(&quot;abc&quot;, null)  = false
	 * TextUtilities.equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
	 * TextUtilities.equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
	 * </pre>
	 * 
	 * @see java.lang.String#equalsIgnoreCase(String)
	 * @param str1
	 *            the first String, may be null
	 * @param str2
	 *            the second String, may be null
	 * @return <code>true</code> if the Strings are equal, case insensitive, or
	 *         both <code>null</code>
	 */
	public static synchronized final boolean equalsIgnoreCase(final String str1, final String str2) {
		return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
	}

	/**
	 * <p>
	 * Compares two Strings, returning <code>true</code> if they are not equal.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered to be equal. The comparison is case sensitive.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.equals(null, null)   = false
	 * TextUtilities.equals(null, &quot;abc&quot;)  = true
	 * TextUtilities.equals(&quot;abc&quot;, null)  = true
	 * TextUtilities.equals(&quot;abc&quot;, &quot;abc&quot;) = false
	 * TextUtilities.equals(&quot;abc&quot;, &quot;ABC&quot;) = true
	 * </pre>
	 * 
	 * @param str1
	 *            the first String, may be null
	 * @param str2
	 *            the second String, may be null
	 * @return <code>false</code> if the Strings are equal, case sensitive, or
	 *         both <code>null</code>
	 */
	public static synchronized final boolean equalsNot(final String str1, final String str2) {
		return !TextUtilities.equals(str1, str2);
	}

	/**
	 * <p>
	 * Finds the first index within a String, handling <code>null</code>. This
	 * method uses {@link String#indexOf(String)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.indexOf(null, *)          = -1
	 * TextUtilities.indexOf(*, null)          = -1
	 * TextUtilities.indexOf(&quot;&quot;, &quot;&quot;)           = 0
	 * TextUtilities.indexOf(&quot;aabaabaa&quot;, &quot;a&quot;)  = 0
	 * TextUtilities.indexOf(&quot;aabaabaa&quot;, &quot;b&quot;)  = 2
	 * TextUtilities.indexOf(&quot;aabaabaa&quot;, &quot;ab&quot;) = 1
	 * TextUtilities.indexOf(&quot;aabaabaa&quot;, &quot;&quot;)   = 0
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchStr
	 *            the String to find, may be null
	 * @return the first index of the search String, -1 if no match or
	 *         <code>null</code> string input
	 */
	public static synchronized final int indexOf(final String str, final String searchStr) {
		if ((str == null) || (searchStr == null)) {
			return -1;
		}
		return str.indexOf(searchStr);
	}

	/**
	 * <p>
	 * Checks if a String is whitespace, empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.isBlank(null)      = true
	 * TextUtilities.isBlank(&quot;&quot;)        = true
	 * TextUtilities.isBlank(&quot; &quot;)       = true
	 * TextUtilities.isBlank(&quot;bob&quot;)     = false
	 * TextUtilities.isBlank(&quot;  bob  &quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 */
	public static synchronized final boolean isBlank(final String str) {
		int strLen;
		if ((str == null) || ((strLen = str.length()) == 0)) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	// Empty checks
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if a String is empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.isEmpty(null)      = true
	 * TextUtilities.isEmpty(&quot;&quot;)        = true
	 * TextUtilities.isEmpty(&quot; &quot;)       = false
	 * TextUtilities.isEmpty(&quot;bob&quot;)     = false
	 * TextUtilities.isEmpty(&quot;  bob  &quot;) = false
	 * </pre>
	 * 
	 * <p>
	 * NOTE: This method changed in Lang version 2.0. It no longer trims the
	 * String. That functionality is available in isBlank().
	 * </p>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is empty or null
	 */
	public static synchronized final boolean isEmpty(final String str) {
		return (str == null) || (str.length() == 0);
	}

	// LastIndexOf
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the last index within a String, handling <code>null</code>. This
	 * method uses {@link String#lastIndexOf(int)}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> or empty ("") String will return <code>-1</code>.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.lastIndexOf(null, *)         = -1
	 * TextUtilities.lastIndexOf(&quot;&quot;, *)           = -1
	 * TextUtilities.lastIndexOf(&quot;aabaabaa&quot;, 'a') = 7
	 * TextUtilities.lastIndexOf(&quot;aabaabaa&quot;, 'b') = 5
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @param searchChar
	 *            the character to find
	 * @return the last index of the search character, -1 if no match or
	 *         <code>null</code> string input
	 */
	public static synchronized final int lastIndexOf(final String str, final char searchChar) {
		if (TextUtilities.isEmpty(str)) {
			return -1;
		}
		return str.lastIndexOf(searchChar);
	}

	/**
	 * <p>
	 * Gets <code>len</code> characters from the middle of a String.
	 * </p>
	 * 
	 * <p>
	 * If <code>len</code> characters are not available, the remainder of the
	 * String will be returned without an exception. If the String is
	 * <code>null</code>, <code>null</code> will be returned. An exception is
	 * thrown if len is negative.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.mid(null, *, *)    = null
	 * TextUtilities.mid(*, *, -ve)     = &quot;&quot;
	 * TextUtilities.mid(&quot;&quot;, 0, *)      = &quot;&quot;
	 * TextUtilities.mid(&quot;abc&quot;, 0, 2)   = &quot;ab&quot;
	 * TextUtilities.mid(&quot;abc&quot;, 0, 4)   = &quot;abc&quot;
	 * TextUtilities.mid(&quot;abc&quot;, 2, 4)   = &quot;c&quot;
	 * TextUtilities.mid(&quot;abc&quot;, 4, 2)   = &quot;&quot;
	 * TextUtilities.mid(&quot;abc&quot;, -2, 2)  = &quot;ab&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get the characters from, may be null
	 * @param pos
	 *            the position to start from, negative treated as zero
	 * @param len
	 *            the length of the required String, must be zero or positive
	 * @return the middle characters, <code>null</code> if null String input
	 */
	public static synchronized final String mid(final String str, int pos, final int len) {
		if (str == null) {
			return null;
		}
		if ((len < 0) || (pos > str.length())) {
			return TextUtilities.EMPTY;
		}
		if (pos < 0) {
			pos = 0;
		}
		if (str.length() <= (pos + len)) {
			return str.substring(pos);
		}
		return str.substring(pos, pos + len);
	}

	/**
	 * <p>
	 * Returns Blank string if passed string is null; else returns the string
	 * provided
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.nullToBlank(null, true)	= &quot;&quot;
	 * TextUtilities.nullToBlank(null, false)	= &quot;&quot;
	 * TextUtilities.nullToBlank(&quot;abc&quot;, true) 	= &quot;abc&quot;
	 * TextUtilities.nullToBlank(&quot;abc&quot;, false) 	= &quot;abc&quot;
	 * TextUtilities.nullToBlank(&quot;  abc    &quot;, true) 	= &quot;abc&quot;
	 * TextUtilities.nullToBlank(&quot;  abc  &quot;, false) 	= &quot;  abc  &quot;
	 * </pre>
	 * 
	 * @param str
	 *            Specifies string which needs be checked
	 * @param trimStr
	 *            Specifies if string is not null, then to provide trimmed
	 *            string or not
	 * @return if string is null, return blank string<br>
	 *         if string is not null, returns string (trimmed/non-trimmed) as
	 *         specified
	 */
	public static synchronized final String nullToBlank(final String str, final boolean trimStr) {
		if ((str == null) || (str.trim().length() == 0)) {
			return "";
		}

		if (trimStr) {
			return str.trim();
		}

		return str;
	}

	/**
	 * <p>
	 * Returns padding using the specified delimiter repeated to a given length.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.padding(0, 'e')  = &quot;&quot;
	 * TextUtilities.padding(3, 'e')  = &quot;eee&quot;
	 * TextUtilities.padding(-2, 'e') = IndexOutOfBoundsException
	 * </pre>
	 * 
	 * <p>
	 * Note: this method doesn't not support padding with <a
	 * href="http://www.unicode.org/glossary/#supplementary_character"> Unicode
	 * Supplementary Characters</a> as they require a pair of <code>char</code>s
	 * to be represented. If you are needing to support full I18N of your
	 * applications consider using {@link #repeat(String, int)} instead.
	 * </p>
	 * 
	 * @param repeat
	 *            number of times to repeat delim
	 * @param padChar
	 *            character to repeat
	 * @return String with repeated character
	 * @throws IndexOutOfBoundsException
	 *             if <code>repeat &lt; 0</code>
	 * @see #repeat(String, int)
	 */
	private static String padding(final int repeat, final char padChar) throws IndexOutOfBoundsException {
		if (repeat < 0) {
			throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
		}
		final char[] buf = new char[repeat];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = padChar;
		}
		return new String(buf);
	}

	/**
	 * <p>
	 * Removes all occurrences of a character from within the source string.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> source string will return <code>null</code>. An empty
	 * ("") source string will return the empty string.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.remove(null, *)       = null
	 * TextUtilities.remove(&quot;&quot;, *)         = &quot;&quot;
	 * TextUtilities.remove(&quot;queued&quot;, 'u') = &quot;qeed&quot;
	 * TextUtilities.remove(&quot;queued&quot;, 'z') = &quot;queued&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the source String to search, may be null
	 * @param remove
	 *            the char to search for and remove, may be null
	 * @return the substring with the char removed if found, <code>null</code>
	 *         if null String input
	 */
	public static synchronized final String remove(final String str, final char remove) {
		if (TextUtilities.isEmpty(str) || (str.indexOf(remove) == -1)) {
			return str;
		}
		char[] chars = str.toCharArray();
		int pos = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != remove) {
				chars[pos++] = chars[i];
			}
		}
		return new String(chars, 0, pos);
	}

	// Padding
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Repeat a String <code>repeat</code> times to form a new String.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.repeat(null, 2) = null
	 * TextUtilities.repeat(&quot;&quot;, 0)   = &quot;&quot;
	 * TextUtilities.repeat(&quot;&quot;, 2)   = &quot;&quot;
	 * TextUtilities.repeat(&quot;a&quot;, 3)  = &quot;aaa&quot;
	 * TextUtilities.repeat(&quot;ab&quot;, 2) = &quot;abab&quot;
	 * TextUtilities.repeat(&quot;a&quot;, -2) = &quot;&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to repeat, may be null
	 * @param repeat
	 *            number of times to repeat str, negative treated as zero
	 * @return a new String consisting of the original String repeated,
	 *         <code>null</code> if null String input
	 */
	public static synchronized final String repeat(final String str, final int repeat) {
		// Performance tuned for 2.0 (JDK1.4)

		if (str == null) {
			return null;
		}
		if (repeat <= 0) {
			return TextUtilities.EMPTY;
		}
		int inputLength = str.length();
		if ((repeat == 1) || (inputLength == 0)) {
			return str;
		}
		if ((inputLength == 1) && (repeat <= TextUtilities.PAD_LIMIT)) {
			return TextUtilities.padding(repeat, str.charAt(0));
		}

		int outputLength = inputLength * repeat;
		switch (inputLength) {
		case 1:
			char ch = str.charAt(0);
			char[] output1 = new char[outputLength];
			for (int i = repeat - 1; i >= 0; i--) {
				output1[i] = ch;
			}
			return new String(output1);
		case 2:
			char ch0 = str.charAt(0);
			char ch1 = str.charAt(1);
			char[] output2 = new char[outputLength];
			for (int i = (repeat * 2) - 2; i >= 0; i--, i--) {
				output2[i] = ch0;
				output2[i + 1] = ch1;
			}
			return new String(output2);
		default:
			StringBuffer buf = new StringBuffer(outputLength);
			for (int i = 0; i < repeat; i++) {
				buf.append(str);
			}
			return buf.toString();
		}
	}

	public static synchronized final String replaceAllEscapeRegEx(final String sourceString, final String toReplace,
			final String replaceWith) {
		TextUtilities.logger.enterMethod();

		if ((sourceString == null) || (toReplace == null) || (replaceWith == null)) {
			TextUtilities.logger.debug("A null parameter encountered. Returning source string as it is");
			return sourceString;
		}

		String finalString = sourceString.replaceAll(Pattern.quote(toReplace), Matcher.quoteReplacement(replaceWith));

		TextUtilities.logger.debug("String Replaced");
		TextUtilities.logger.debug("String before replacement: " + sourceString);
		TextUtilities.logger.debug("String after replacement: " + finalString);

		TextUtilities.logger.exitMethod();
		return finalString;
	}

	// endsWith
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Check if a String starts with a specified prefix.
	 * </p>
	 * 
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered to be equal. The comparison is case sensitive.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.startsWith(null, null)      = true
	 * TextUtilities.startsWith(null, &quot;abc&quot;)     = false
	 * TextUtilities.startsWith(&quot;abcdef&quot;, null)  = false
	 * TextUtilities.startsWith(&quot;abcdef&quot;, &quot;abc&quot;) = true
	 * TextUtilities.startsWith(&quot;ABCDEF&quot;, &quot;abc&quot;) = false
	 * </pre>
	 * 
	 * @see java.lang.String#startsWith(String)
	 * @param str
	 *            the String to check, may be null
	 * @param prefix
	 *            the prefix to find, may be null
	 * @return <code>true</code> if the String starts with the prefix, case
	 *         sensitive, or both <code>null</code>
	 */
	public static synchronized final boolean startsWith(final String str, final String prefix) {
		return TextUtilities.startsWith(str, prefix, false);
	}

	/**
	 * <p>
	 * Check if a String starts with a specified prefix (optionally case
	 * insensitive).
	 * </p>
	 * 
	 * @see java.lang.String#startsWith(String)
	 * @param str
	 *            the String to check, may be null
	 * @param prefix
	 *            the prefix to find, may be null
	 * @param ignoreCase
	 *            inidicates whether the compare should ignore case (case
	 *            insensitive) or not.
	 * @return <code>true</code> if the String starts with the prefix or both
	 *         <code>null</code>
	 */
	private static boolean startsWith(final String str, final String prefix, final boolean ignoreCase) {
		if ((str == null) || (prefix == null)) {
			return ((str == null) && (prefix == null));
		}
		if (prefix.length() > str.length()) {
			return false;
		}
		return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length());
	}

	// Substring
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 * 
	 * <p>
	 * A negative start position can be used to start <code>n</code> characters
	 * from the end of the String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> String will return <code>null</code>. An empty ("")
	 * String will return "".
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.substring(null, *)   = null
	 * TextUtilities.substring(&quot;&quot;, *)     = &quot;&quot;
	 * TextUtilities.substring(&quot;abc&quot;, 0)  = &quot;abc&quot;
	 * TextUtilities.substring(&quot;abc&quot;, 2)  = &quot;c&quot;
	 * TextUtilities.substring(&quot;abc&quot;, 4)  = &quot;&quot;
	 * TextUtilities.substring(&quot;abc&quot;, -2) = &quot;bc&quot;
	 * TextUtilities.substring(&quot;abc&quot;, -4) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get the substring from, may be null
	 * @param start
	 *            the position to start from, negative means count back from the
	 *            end of the String by this many characters
	 * @return substring from start position, <code>null</code> if null String
	 *         input
	 */
	public static synchronized final String substring(final String str, int start) {
		if (str == null) {
			return null;
		}

		// handle negatives, which means last n characters
		if (start < 0) {
			start = str.length() + start; // remember start is negative
		}

		if (start < 0) {
			start = 0;
		}
		if (start > str.length()) {
			return TextUtilities.EMPTY;
		}

		return str.substring(start);
	}

	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 * 
	 * <p>
	 * A negative start position can be used to start/end <code>n</code>
	 * characters from the end of the String.
	 * </p>
	 * 
	 * <p>
	 * The returned substring starts with the character in the
	 * <code>start</code> position and ends before the <code>end</code>
	 * position. All position counting is zero-based -- i.e., to start at the
	 * beginning of the string use <code>start = 0</code>. Negative start and
	 * end positions can be used to specify offsets relative to the end of the
	 * String.
	 * </p>
	 * 
	 * <p>
	 * If <code>start</code> is not strictly to the left of <code>end</code>, ""
	 * is returned.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.substring(null, *, *)    = null
	 * TextUtilities.substring(&quot;&quot;, * ,  *)    = &quot;&quot;;
	 * TextUtilities.substring(&quot;abc&quot;, 0, 2)   = &quot;ab&quot;
	 * TextUtilities.substring(&quot;abc&quot;, 2, 0)   = &quot;&quot;
	 * TextUtilities.substring(&quot;abc&quot;, 2, 4)   = &quot;c&quot;
	 * TextUtilities.substring(&quot;abc&quot;, 4, 6)   = &quot;&quot;
	 * TextUtilities.substring(&quot;abc&quot;, 2, 2)   = &quot;&quot;
	 * TextUtilities.substring(&quot;abc&quot;, -2, -1) = &quot;b&quot;
	 * TextUtilities.substring(&quot;abc&quot;, -4, 2)  = &quot;ab&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to get the substring from, may be null
	 * @param start
	 *            the position to start from, negative means count back from the
	 *            end of the String by this many characters
	 * @param end
	 *            the position to end at (exclusive), negative means count back
	 *            from the end of the String by this many characters
	 * @return substring from start position to end position, <code>null</code>
	 *         if null String input
	 */
	public static synchronized final String substring(final String str, int start, int end) {
		if (str == null) {
			return null;
		}

		// handle negatives
		if (end < 0) {
			end = str.length() + end; // remember end is negative
		}
		if (start < 0) {
			start = str.length() + start; // remember start is negative
		}

		// check length next
		if (end > str.length()) {
			end = str.length();
		}

		// if start is greater than end, return ""
		if (start > end) {
			return TextUtilities.EMPTY;
		}

		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = 0;
		}

		return str.substring(start, end);
	}

	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String,
	 * handling <code>null</code> by returning <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * The String is trimmed using {@link String#trim()}. Trim removes start and
	 * end characters &lt;= 32. To strip whitespace use {@link #strip(String)}.
	 * </p>
	 * 
	 * <p>
	 * To trim your choice of characters, use the {@link #strip(String, String)}
	 * methods.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.trim(null)          = null
	 * TextUtilities.trim(&quot;&quot;)            = &quot;&quot;
	 * TextUtilities.trim(&quot;     &quot;)       = &quot;&quot;
	 * TextUtilities.trim(&quot;abc&quot;)         = &quot;abc&quot;
	 * TextUtilities.trim(&quot;    abc    &quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str
	 *            the String to be trimmed, may be null
	 * @return the trimmed string, <code>null</code> if null String input
	 */
	public static synchronized final String trim(final String str) {
		return str == null ? null : str.trim();
	}

	// Case conversion
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts a String to upper case as per {@link String#toUpperCase()}.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * TextUtilities.upperCase(null)  = null
	 * TextUtilities.upperCase(&quot;&quot;)    = &quot;&quot;
	 * TextUtilities.upperCase(&quot;aBc&quot;) = &quot;ABC&quot;
	 * </pre>
	 * 
	 * <p>
	 * <strong>Note:</strong> As described in the documentation for
	 * {@link String#toUpperCase()}, the result of this method is affected by
	 * the current locale. For platform-independent case transformations, the
	 * method {@link #lowerCase(String, Locale)} should be used with a specific
	 * locale (e.g. {@link Locale#ENGLISH}).
	 * </p>
	 * 
	 * @param str
	 *            the String to upper case, may be null
	 * @return the upper cased String, <code>null</code> if null String input
	 */
	public static synchronized final String upperCase(final String str) {
		if (str == null) {
			return null;
		}
		return str.toUpperCase();
	}

	/**
	 * Get random string
	 * 
	 * @param len
	 * @return
	 */
	public static synchronized final String getRandomString(final int len) {
		final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final Random rnd = new Random();
		final StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		}
		return sb.toString();
	}

	/**
	 * Get random number
	 * 
	 * @param len
	 * @return
	 */
	public static synchronized final int getRandomNumber(final int len) {
		int randomInt = 0;
		final Random rnd = new Random();
		for (int i = 0; i < len; i++) {
			randomInt = rnd.nextInt(100);
		}
		return randomInt;

	}

	/**
	 * Get random integer within defined range
	 * 
	 * @param len
	 * @return
	 */

	// Get random integer within defined range
	public static synchronized final int getRandomInteger(final long aStart, final long aEnd) {
		final Random aRandom = new Random();
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start value cannot exceed End Value.");
		}
		// get the range, casting to long to avoid overflow problems
		final long range = (aEnd - aStart) + 1;
		// compute a fraction of the range, 0 <= frac < range
		final long fraction = (long) (range * aRandom.nextDouble());
		final int randomNumber = (int) (fraction + aStart);
		return randomNumber;
	}
}
