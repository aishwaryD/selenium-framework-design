package com.bikroy.framework.utilities.xmlutils;

import com.bikroy.framework.globals.GlobalConsts;
import com.bikroy.framework.globals.Logger;

public class ConfigPropertiesReader extends PropertyFileReaderUpdater {
	// Initialize private logger object
	private static Logger logger = new Logger().getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public String readConfig(String key) {
		String values = this.readPropertyFromFile(key, GlobalConsts.PropertiesFileName);
		ConfigPropertiesReader.logger.info(":::::::::::::::::::::::::::::::" + values);
		return values;
	}

	public String readTestData(String key) {
		String values = this.readPropertyFromFile(key, GlobalConsts.TestDataPropertiesFileName);
		ConfigPropertiesReader.logger.info(":::::::::::::::::::::::::::::::" + values);
		return values;
	}
}
