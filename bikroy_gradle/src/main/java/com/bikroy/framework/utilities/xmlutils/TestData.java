package com.bikroy.framework.utilities.xmlutils;

public class TestData {
	public static String read(String testDataFileName, String dataSetName, String keyName, String category) {
		try {
			XmlDataReader xr = new XmlDataReader(testDataFileName);

			return xr.readData(dataSetName, keyName, category);
		} catch (Exception e) {
			// eat exception
		}
		return null;
	}
}
