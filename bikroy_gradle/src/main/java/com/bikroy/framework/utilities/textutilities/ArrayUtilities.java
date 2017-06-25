package com.bikroy.framework.utilities.textutilities;

public final class ArrayUtilities {
	/**
	 * An empty immutable <code>String</code> array.
	 */
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if an array of Objects is empty or <code>null</code>.
	 * </p>
	 * 
	 * @param array
	 *            the array to test
	 * @return <code>true</code> if the array is empty or <code>null</code>
	 */
	public static boolean isEmpty(final Object[] array) {
		if ((array == null) || (array.length == 0)) {
			return true;
		}
		return false;
	}
}
