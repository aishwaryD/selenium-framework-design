package com.bikroy.framework.globals;

public class GlobalConsts {
	/**
	 * VerboseLogging stores the flag whether to log execution in detail or not
	 */
	private static boolean verboseLogging = false;

	public static final String PropertiesFileName = "config.properties";

	public static final String TestDataPropertiesFileName = "testData.properties";

	public static final String DirectoryPathTestData = "./src/test/resources/testdata/";

	public static final String ImageFileToBeUploaded = "\\src\\test\\resources\\testdata\\testImages\\";

	public static final String DefaultDataSet = "dataset_default";

	public static final String ActiveDataMode_TestData = "dataset_default";

	public static final int IMPLICIT_TIME_LIMIT = 30;

	public static final int LEAST_TIMEOUT_MILLISECONDS = 30000;

	public static final String TestEnvironment = "production";

	public static final String XPathToTextSeparator = "-->>";
	public static final String USE_EXTENDED_TEXT_TO_XPATH = "USE_EXTENDED_TEXT_TO_XPATH";
	public static final String PRE_POST_EXTENDED_TEXTS_MARKER = "{TXT}";

	public static boolean IsVerboseLogging() {
		return GlobalConsts.verboseLogging;
	}

	public static final String getProjectDir() {
		return System.getProperty("user.dir");
	}

	public static final String getScreenShotsFolderPath() {
		return GlobalConsts.getProjectDir() + "/results/screenshots/";
	}
}
