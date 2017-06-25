package com.bikroy.test;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.bikroy.framework.globals.Logger;
import com.bikroy.framework.pageclasses.GlobalEnumsPage.LoginField;
import com.bikroy.framework.pageclasses.GlobalEnumsPage.ProvideItemDetails;
import com.bikroy.framework.pageclasses.GlobalEnumsPage.SignUpField;
import com.bikroy.framework.utilities.browserutils.BrowserApp;
import com.bikroy.framework.utilities.xmlutils.ConfigPropertiesReader;

public class ValidateFlowForAdPostFunctionality extends BrowserApp {

	// Initialize private Logger object
	private static Logger logger = new Logger().getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	// Initialize property reader object
	ConfigPropertiesReader propReader = new ConfigPropertiesReader();

	// Initialize runTime variable stack
	HashMap<String, String> runTimeVariable = new HashMap<String, String>();

	// Please refer to below stack to use page variables in current class file

	/**
	 * APPLICATION CURRENT PAGE STACK $||$ LoginPage--> LoginPage; HomePage-->
	 * homePage; SignUpPage--> signUpPage; AddDetailsPage--> addDetailsPage;
	 * AddsPage--> addsPage; CategoryPage--> categoryPage; CityDivisionPage-->
	 * cityDivisionPage; HomePage--> homePage; MyAddsPage--> myAddsPage;
	 * SignUpPage--> signUpPage; FiltersPage--> filtersPage; MessagesPage-->
	 * msgsPage;
	 *
	 * @author Aishwarya Dwivedi
	 * @since 1.0
	 * @version 1.0
	 */

	@Test(description = "Test Case 1.0: Verify that users are able to login into the Bikroy App using signup credentials.")
	public void tc_LoginAfterSignUp() throws Exception {
		// Verify Bikroy LOGO Presence After Navigating To APP URL
		this.homePage = this.homePage.verifyAppLogo();
		ValidateFlowForAdPostFunctionality.logger.info("Verify Bikroy LOGO Presence After Navigating To APP URL");

		// Click Login Link On The Home Page
		this.loginPage = this.homePage.clickLoginLink();
		ValidateFlowForAdPostFunctionality.logger.info("Click Login Link On The Home Page");

		// Verify Presence Of Login UI Components
		this.loginPage = this.loginPage.verifyLoginComponentUI();
		ValidateFlowForAdPostFunctionality.logger.info("Verify Presence Of Login UI Components");

		// Click On Available SignUp Link
		this.signUpPage = this.loginPage.clickSignUpLink();
		ValidateFlowForAdPostFunctionality.logger.info("Click On Available SignUp Link");

		// Sign Up to the application with valid credentials
		String emailId = this.signUpPage.signUpWithNewUser(SignUpField.ALL, "signUpWithRandomUser", "suwru_1.0");
		this.runTimeVariable.put("SignUp_Email", emailId);
		ValidateFlowForAdPostFunctionality.logger.info("Sign Up to the application with valid credentials");

		// Logout From The Application
		this.homePage = this.myAddsPage.clickLogoutLink();
		ValidateFlowForAdPostFunctionality.logger.info("Logout From The Application");

		// Verify Home Page
		this.homePage = this.homePage.verifyAppLogo();
		ValidateFlowForAdPostFunctionality.logger.info("Verify Home Page");

		// Click on Login Button On Home Page
		this.loginPage = this.homePage.clickLoginLink();
		ValidateFlowForAdPostFunctionality.logger.info("Click on Login Button On Home Page");

		// Login To The Application With SignUp Credentials
		this.myAddsPage = this.loginPage.loginToApp(LoginField.ALL, "loginWithSignUpCredentials", "lwsuc_1.0",
				this.runTimeVariable.get("SignUp_Email"));
		ValidateFlowForAdPostFunctionality.logger.info("Login To The Application With SignUp Credentials");

	}

	@Test(description = "Test Case 1.1: Validate users are able to post their advertisements on Bikroy "
			+ "web portal as a Logged In User.", dependsOnMethods = { "tc_LoginAfterSignUp" })
	public void tc_ValidateFlowForAdPostFunctionalityWithLogin() throws Exception {

		// Set To English Language Mode
		this.myAddsPage = this.myAddsPage.setEnglishLanguage();
		ValidateFlowForAdPostFunctionality.logger.info("Set To English Language Mode");

		// Click On Available Post Ad Link
		this.myAddsPage = this.myAddsPage.clickPostAdLink();
		ValidateFlowForAdPostFunctionality.logger.info("Click On Available Post Ad Link");

		// Click On Sell An Item Or Service Link From Select Something Section
		this.categoryPage = this.myAddsPage.clickSellAnItemServiceLink();
		ValidateFlowForAdPostFunctionality.logger.info("Click On Sell An Item Or Service Link From Select Something Section");

		// Select Electronics Category From Available Links
		this.categoryPage = this.categoryPage.clickElectronicsCategory();
		ValidateFlowForAdPostFunctionality.logger.info("Select Electronics Category From Available Links");

		// Select TVs Link From Available Sub Category Section
		this.cityDivisionPage = this.categoryPage.clickTVsSubCategory();
		ValidateFlowForAdPostFunctionality.logger.info("Select TVs Link From Available Sub Category Section");

		// Select Dhaka From Available City Or Division Links
		this.cityDivisionPage = this.cityDivisionPage.clickDhakaCityLink();
		ValidateFlowForAdPostFunctionality.logger.info("Select Dhaka From Available City Or Division Links");

		// Select Badda Local area Within Dhaka
		this.addDetailsPage = this.cityDivisionPage.selectCityFromDropdown("selectCityFromDropdown", "scfdd_1.0");
		ValidateFlowForAdPostFunctionality.logger.info("Select Badda Local area Within Dhaka");

		// Enter positive item details to post an advertisement
		String itemTitle = this.addDetailsPage.enterItemDetailsToPostAd(ProvideItemDetails.LOGGEDINUSER, "itemDetailsToPost", "idtp_1.0");
		this.propReader.updatePropertyInFile("TC_1.1_ITEM_TITLE", itemTitle, "testData");
		ValidateFlowForAdPostFunctionality.logger.info("Enter positive item details to post an advertisement");

		// Verify Text: Your ad was successfully submitted for review
		// String textMessage =
		// this.propReader.readPropertyFromFile("AdPost_Success_Msg",
		// "testData");
		// this.msgsPage = this.msgsPage.verifyDisplayedMessageText(textMessage,
		// "", TextComparators.contains);
		// ValidateFlowForAdPostFunctionality.logger.info("Verify Text: Your ad
		// was successfully submitted for review");

		// Click On My Ads Link Available On Ad Details Page
		// this.myAddsPage = this.addDetailsPage.clickMyAdsLink();
		// ValidateFlowForAdPostFunctionality.logger.info("Click On My Ads Link
		// Available On Ad Details Page");

		// Validate Ads Under Review Count
		// this.myAddsPage = this.myAddsPage.validateAdsUnderReviewCount();
		// ValidateFlowForAdPostFunctionality.logger.info("Validate Ads Under
		// Review Count");

		// Validate Posted Item Content
		// String getItemTitle =
		// this.propReader.readTestData("TC_1.1_ITEM_TITLE");
		// this.myAddsPage = this.myAddsPage.validateItemContent(getItemTitle);
		// ValidateFlowForAdPostFunctionality.logger.info("Validate Posted Item
		// Content");

		// Logout From The Application
		// this.homePage = this.myAddsPage.clickLogoutLink();
		// ValidateFlowForAdPostFunctionality.logger.info("Logout From The
		// Application");

	}

	// @Test(description = "Test Case 1.2: Validate users are able to post their
	// advertisements on Bikroy "
	// + "web portal Without SignUp/Login.", dependsOnMethods = {
	// "tc_ValidateFlowForAdPostFunctionalityWithLogin" })
	// public void tc_ValidateFlowForAdPostFunctionalityWithOutSignUpLogin()
	// throws Exception {
	//
	// // Verify Bikroy LOGO Presence After Navigating To APP URL
	// this.homePage = this.homePage.verifyAppLogo();
	// ValidateFlowForAdPostFunctionality.logger.info("Verify Bikroy LOGO
	// Presence After Navigating To APP URL");
	//
	// // Click On Available Post Ad Link
	// this.myAddsPage = this.homePage.clickPostAdLink();
	// ValidateFlowForAdPostFunctionality.logger.info("Click On Available Post
	// Ad Link");
	//
	// // Click On Sell An Item Or Service Link From Select Something Section
	// this.categoryPage = this.myAddsPage.clickSellAnItemServiceLink();
	// ValidateFlowForAdPostFunctionality.logger.info("Click On Sell An Item Or
	// Service Link From Select Something Section");
	//
	// // Select Electronics Category From Available Links
	// this.categoryPage = this.categoryPage.clickElectronicsCategory();
	// ValidateFlowForAdPostFunctionality.logger.info("Select Electronics
	// Category From Available Links");
	//
	// // Select TVs Link From Available Sub Category Section
	// this.cityDivisionPage = this.categoryPage.clickTVsSubCategory();
	// ValidateFlowForAdPostFunctionality.logger.info("Select TVs Link From
	// Available Sub Category Section");
	//
	// // Select Dhaka From Available City Or Division Links
	// this.cityDivisionPage = this.cityDivisionPage.clickDhakaCityLink();
	// ValidateFlowForAdPostFunctionality.logger.info("Select Dhaka From
	// Available City Or Division Links");
	//
	// // Select Badda Local area Within Dhaka
	// this.addDetailsPage =
	// this.cityDivisionPage.selectCityFromDropdown("selectCityFromDropdown",
	// "scfdd_1.0");
	// ValidateFlowForAdPostFunctionality.logger.info("Select Badda Local area
	// Within Dhaka");
	//
	// // Enter positive item details to post an advertisement
	// String itemTitle =
	// this.addDetailsPage.enterItemDetailsToPostAd(ProvideItemDetails.NOLOGGEDINUSER,
	// "itemDetailsToPost", "idtp_1.0");
	// this.propReader.updatePropertyInFile("TC_1.2_ITEM_TITLE", itemTitle,
	// "testData");
	// ValidateFlowForAdPostFunctionality.logger.info("Enter positive item
	// details to post an advertisement");
	//
	// // Verify Text: Your ad was successfully submitted for review
	// String textMessage =
	// this.propReader.readPropertyFromFile("AdPost_Success_Msg", "testData");
	// this.msgsPage = this.msgsPage.verifyDisplayedMessageText(textMessage, "",
	// TextComparators.contains);
	// ValidateFlowForAdPostFunctionality.logger.info("Verify Text: Your ad was
	// successfully submitted for review");
	//
	// // Click On My Ads Link Available On Ad Details Page
	// this.myAddsPage = this.addDetailsPage.clickMyAdsLink();
	// ValidateFlowForAdPostFunctionality.logger.info("Click On My Ads Link
	// Available On Ad Details Page");
	//
	// // Validate Ads Under Review Count
	// this.myAddsPage = this.myAddsPage.validateAdsUnderReviewCount();
	// ValidateFlowForAdPostFunctionality.logger.info("Validate Ads Under Review
	// Count");
	//
	// // Validate Posted Item Content
	// String getItemTitle = this.propReader.readTestData("TC_1.2_ITEM_TITLE");
	// this.myAddsPage = this.myAddsPage.validateItemContent(getItemTitle);
	// ValidateFlowForAdPostFunctionality.logger.info("Validate Posted Item
	// Content");
	//
	// // Logout From The Application
	// this.homePage = this.myAddsPage.clickLogoutLink();
	// ValidateFlowForAdPostFunctionality.logger.info("Logout From The
	// Application");
	//
	// }
}
