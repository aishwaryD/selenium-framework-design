package com.bikroy.framework.utilities.browserutils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import com.bikroy.framework.globals.GlobalController;
import com.bikroy.framework.globals.Logger;
import com.bikroy.framework.interfaces.impl.selenium.BrowserInst;
import com.bikroy.framework.pageclasses.AdDetailsPage;
import com.bikroy.framework.pageclasses.CategoryPage;
import com.bikroy.framework.pageclasses.CityDivisionPage;
import com.bikroy.framework.pageclasses.HomePage;
import com.bikroy.framework.pageclasses.LoginPage;
import com.bikroy.framework.pageclasses.MessagesPage;
import com.bikroy.framework.pageclasses.MyAdsPage;
import com.bikroy.framework.pageclasses.SignUpPage;
import com.bikroy.framework.utilities.xmlutils.ConfigPropertiesReader;
import com.bikroy.framework.utilities.xmlutils.PropertyFileReaderUpdater;

/**
 * Class to initialize all application page objects and manage WebDriver browser
 * object. Each and every test script class must extend this. This class does
 * not use any of the Selenium APIs directly, and adds support to make this
 * framework tool independent.
 * 
 * @author Aishwarya Dwivedi
 * @since 1.0
 * 
 * @version 1.0
 */

public abstract class BrowserApp {
	// Initialize private logger object
	private static Logger logger = new Logger().getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	ConfigPropertiesReader pr = new ConfigPropertiesReader();

	HashMap<String, String> runTimeVariables = new HashMap<String, String>();

	protected LoginPage loginPage;
	protected HomePage homePage;
	protected MessagesPage msgsPage;
	protected PropertyFileReaderUpdater propReader;
	protected AdDetailsPage addDetailsPage;
	protected CategoryPage categoryPage;
	protected CityDivisionPage cityDivisionPage;
	protected MyAdsPage myAddsPage;
	protected SignUpPage signUpPage;

	// Define objects
	private final Runtime runtime = Runtime.getRuntime();
	private static final long MEGABYTE = 1024L * 1024L;

	@BeforeClass
	public void Starter() throws Exception {
		BrowserApp.logger.enterMethod();
		Reporter.log("<br> BeforeClass Begins");

		this.runTimeVariables.clear();
		GlobalController.brw = new BrowserInst().getFirefoxSeleniumInstance();

		// Initialize Page Objects after Browser/Drivers object initialization
		this.loginPage = GlobalController.brw.initElements(LoginPage.class);
		this.homePage = GlobalController.brw.initElements(HomePage.class);
		this.msgsPage = GlobalController.brw.initElements(MessagesPage.class);
		this.addDetailsPage = GlobalController.brw.initElements(AdDetailsPage.class);
		this.categoryPage = GlobalController.brw.initElements(CategoryPage.class);
		this.cityDivisionPage = GlobalController.brw.initElements(CityDivisionPage.class);
		this.myAddsPage = GlobalController.brw.initElements(MyAdsPage.class);
		this.signUpPage = GlobalController.brw.initElements(SignUpPage.class);
		this.propReader = GlobalController.brw.initElements(PropertyFileReaderUpdater.class);
		this.runTimeVariables.put("ENVIRONMENT_UNDER_TEST", this.pr.readConfig("EnvironmentUnderTest"));
		this.runTimeVariables.put("ENVIRONMENT_URL_UNDER_TEST",
				this.pr.readConfig(this.runTimeVariables.get("ENVIRONMENT_UNDER_TEST") + "_URL"));

		// Navigate to the application url
		this.homePage = this.loginPage.navigateToURL(this.runTimeVariables.get("ENVIRONMENT_URL_UNDER_TEST"));
		BrowserApp.logger.debug("Navigate to the application url");

		Reporter.log("<br> BeforeClass Ends");
		BrowserApp.logger.exitMethod();
	}

	@AfterClass(alwaysRun = true)
	public void tearDownPrintRunTimeValues() throws IOException {

		Iterator<Entry<String, String>> it = this.runTimeVariables.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pair = it.next();

			BrowserApp.logger.info(pair.getKey() + " = " + pair.getValue());
			// it.remove(); // avoids a ConcurrentModificationException
		}

		BrowserApp.logger.debug("Disposing all page objects");
		this.loginPage = null;
		this.homePage = null;
		this.signUpPage = null;
		this.categoryPage = null;
		this.cityDivisionPage = null;
		this.addDetailsPage = null;
		this.myAddsPage = null;
		this.msgsPage = null;
		this.propReader = null;

		BrowserApp.logger.debug("Execution completed for test\n");
		BrowserApp.logger.debug("*****************************************************************************");

		BrowserApp.logger.debug("Memory after execution (in MB) " + (this.runtime.totalMemory() / BrowserApp.MEGABYTE));

		// Run the garbage collector
		this.runtime.gc();

		BrowserApp.logger.debug("Memory freed after execution (in MB) " + (this.runtime.freeMemory() / BrowserApp.MEGABYTE));

		// Calculate the used memory
		final long memory = this.runtime.totalMemory() - this.runtime.freeMemory();
		BrowserApp.logger.debug("Used memory (in MB) " + (memory / BrowserApp.MEGABYTE));

		try {

			if (!(GlobalController.brw == null)) {
				GlobalController.brw.quit();
			}
		} catch (final Exception e) {
			// this.runtime.exec("taskkill /F /IM firefox.exe");
		}

		// Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
		// this.runtime.exec("taskkill /F /IM firefox.exe");
		// System.out.println("After" + Runtime.getRuntime().freeMemory());

	}

	@AfterMethod(alwaysRun = true)
	public void captureScreenshot(ITestResult result) throws Exception {
		GlobalController.brw.takeScreenShot(result.getMethod().getMethodName());
	}

	public void clearMemory() {
		// Get the Java runtime
		final int MAXJVMMemoryUsage = 50;
		BrowserApp.logger.debug("Initial Memory consumed (in MB) "
				+ (((this.runtime.totalMemory() - this.runtime.freeMemory()) / this.runtime.totalMemory()) * 100));
		if ((((this.runtime.totalMemory() - this.runtime.freeMemory()) / this.runtime.totalMemory()) * 100) >= MAXJVMMemoryUsage) {
			this.runtime.gc();
			this.runtime.gc();
			BrowserApp.logger.debug("Memory Cleared");
		}
	}

}
