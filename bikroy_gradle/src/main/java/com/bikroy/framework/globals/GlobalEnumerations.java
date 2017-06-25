package com.bikroy.framework.globals;

/**
 * This class defines the enumerations related to the Web Objects and Any
 * Automation tool used in the whole automation suite. This class does have all
 * static properties/methods/enumerations.
 * 
 * @author Aishwarya Dwivedi
 * @since 1.0
 * @version 1.0
 */
public final class GlobalEnumerations {

	/**
	 * Enumeration to know web browser types
	 * 
	 * @author Aishwarya Dwivedi
	 * @since 1.0
	 * @version 1.0
	 */
	public enum BrowsersTypes {
		InternetExplorer, Chrome, Firefox
	};

	/**
	 * Logical Operations.
	 * 
	 * @author Aishwarya Dwivedi
	 * @since 1.0
	 * @version 1.0
	 */
	public static enum LogicalOperators {
		AND, OR, NOT, NA;
	}

	/**
	 * Specifies all Date Masks supported for test data
	 * 
	 * @author Aishwarya Dwivedi
	 * @since 1.0
	 * @version 1.0
	 */
	public static enum TextComparators {
		NA(""), contains("Contains"), notContains("Does not contain"), equals("Equals"),

		notEquals("Does not equal"), missing("Missing"), exists("Exists"),

		startsWith("Starts With"), endsWith("Ends With");

		public static TextComparators GetKey(final String Value) {
			for (TextComparators tc : GlobalEnumerations.TextComparators.values()) {
				if (tc.GetValue().equals(Value)) {
					return tc;
				}
			}
			return null;
		}

		private final String indexLabel;

		private TextComparators(final String labelText) {
			this.indexLabel = labelText;
		}

		public String GetValue() {
			return this.indexLabel;
		}
	}
}