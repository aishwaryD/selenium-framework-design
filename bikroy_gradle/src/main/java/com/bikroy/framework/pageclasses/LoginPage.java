package com.bikroy.framework.pageclasses;

import com.bikroy.framework.globals.GlobalController;
import com.bikroy.framework.globals.Logger;
import com.bikroy.framework.interfaces.ElementField;
import com.bikroy.framework.interfaces.LocateBy;
import com.bikroy.framework.pageclasses.GlobalEnumsPage.LoginField;
import com.bikroy.framework.utilities.xmlutils.ConfigPropertiesReader;
import com.bikroy.framework.utilities.xmlutils.TestData;

public class LoginPage {
	// Initialize private logger object
	private static Logger logger = new Logger().getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	@LocateBy(xpath = "//input[@id='account']")
	private ElementField TB_Username;

	@LocateBy(xpath = "//input[@id='password']")
	private ElementField TB_PasswordField;

	@LocateBy(xpath = "//button[contains(@class,'ui-btn is-secondary has-busy is-auto is-island')]")
	private ElementField BT_Login;

	@LocateBy(xpath = "//a[contains(@href,'/users/logout')]")
	private ElementField HL_Logout;

	@LocateBy(xpath = "//a[contains(@href,'/users/login')]")
	private ElementField HL_LogIn;

	@LocateBy(xpath = "//a[contains(@href,'/bn/users/login-options')]")
	private ElementField HL_SignUp;

	@LocateBy(xpath = "//a[contains(@class,'ui-btn is-secondary is-island')]")
	private ElementField BT_SignUpUsingEmail;

	ConfigPropertiesReader propReader = new ConfigPropertiesReader();

	/**
	 * This method will set UserName
	 *
	 * @param userName
	 * @throws Exception
	 */
	public void setUserName(String userName) throws Exception {
		if (this.TB_Username == null) {
			System.exit(1);
		}
		GlobalController.brw.setText(this.TB_Username, userName);
	}

	/**
	 * This method will set Password
	 *
	 * @param password
	 * @throws Exception
	 */
	public void setPassword(String password) throws Exception {
		GlobalController.brw.setText(this.TB_PasswordField, password);
	}

	/**
	 * This method will click on Login Button
	 *
	 * @throws Exception
	 */
	public void clickLoginButton() throws Exception {
		GlobalController.brw.clickLinkText(this.BT_Login);
	}

	/**
	 * This method will click on Logout Button
	 *
	 * @throws Exception
	 */
	public void clickLogoutButton() throws Exception {
		GlobalController.brw.clickLinkText(this.HL_Logout);
	}

	/**
	 * This method will verify the login page
	 *
	 * @return
	 * @throws Exception
	 */
	public LoginPage verifyLoginComponentUI() throws Exception {
		GlobalController.brw.verifyUIComponent(this.HL_LogIn);
		GlobalController.brw.isElementPresent(this.BT_Login);
		return GlobalController.brw.initElements(LoginPage.class);
	}

	/**
	 * Navigate to Application URL
	 *
	 * @throws Exception
	 */
	public HomePage navigateToURL(String environment) throws Exception {
		LoginPage.logger.enterMethod();
		GlobalController.brw.navigateToUrl(environment);
		LoginPage.logger.exitMethod();
		return GlobalController.brw.initElements(HomePage.class);
	}

	/**
	 * This method will click on Sign Up Link
	 *
	 * @return
	 * @throws Exception
	 */
	public SignUpPage clickSignUpLink() throws Exception {
		GlobalController.brw.click(this.HL_SignUp);
		GlobalController.brw.click(this.BT_SignUpUsingEmail);
		return GlobalController.brw.initElements(SignUpPage.class);
	}

	public MyAdsPage loginToApp(LoginField loginField, String testDataSetName, String category, String userName) throws Exception {
		String password = TestData.read("LoginPageData.xml", testDataSetName, "password", category);

		switch (loginField) {
		case USERNAME:
			this.setUserName(userName);
			break;
		case PASSWORD:
			this.setPassword(password);
			break;
		case EMPTY:
			this.setUserName("");
			this.setPassword("");
			break;
		case ALL:
			this.setUserName(userName);
			this.setPassword(password);
			break;

		default:
			throw new Exception("Invalid Step Provided. Not defined in enumeration");

		}
		this.clickLoginButton();

		return GlobalController.brw.initElements(MyAdsPage.class);
	}
}
