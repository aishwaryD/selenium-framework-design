package com.bikroy.framework.interfaces.impl.selenium;

import java.lang.reflect.Constructor;

import com.bikroy.framework.globals.GlobalEnumerations.BrowsersTypes;
import com.bikroy.framework.globals.Logger;
import com.bikroy.framework.interfaces.IBrowser;

/**
 * Class to initialize browser drivers and provide browser instance as an
 * IBrowser interface object.
 * 
 * @author Aishwarya Dwivedi
 * @since 1.0
 * 
 * @version 1.0
 */
public class BrowserInst {
	// Initialize private logger object
	private static Logger logger = new Logger().getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	String tool = "x";
	IBrowser _brw = null;

	public BrowserInst() {
	}

	public IBrowser getFirefoxSeleniumInstance() throws Exception {
		return this.getBrowserInstance("selenium", BrowsersTypes.Firefox, "com.bikroy.framework.interfaces.impl.selenium",
				"SeleniumBrowser");
	}

	public IBrowser getBrowserInstance(String toolType, BrowsersTypes brwType, String packageName, String className) throws Exception {
		this.tool = toolType;
		BrowserInst.logger.info("Initializing class");
		Class<?> cls = Class.forName(packageName + "." + className);

		switch (this.tool) {
		case "selenium":
		default:
			BrowserInst.logger.info("Initializing constructor");
			Constructor<?> constructor = cls.getConstructor(BrowsersTypes.class);
			BrowserInst.logger.info("Constructor initialization done");

			this._brw = (IBrowser) constructor.newInstance(brwType);
			this._brw.maximize();
			this._brw.clearCookies();
			break;
		}
		return this._brw;
	}
}
