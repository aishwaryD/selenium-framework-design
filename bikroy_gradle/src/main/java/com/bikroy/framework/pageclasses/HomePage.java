package com.bikroy.framework.pageclasses;

import com.bikroy.framework.globals.GlobalController;
import com.bikroy.framework.interfaces.ElementField;
import com.bikroy.framework.interfaces.LocateBy;

public class HomePage {

	@LocateBy(xpath = "//a[contains(@href, '/users/login')]")
	private ElementField HL_Login;

	@LocateBy(xpath = "//a[contains(@href,'/post-ad')]")
	private ElementField HL_AdsLink;

	@LocateBy(xpath = "//div[@class = 'ui-logo']")
	private ElementField LT_Logo;

	/**
	 * This method is used to verify the home page
	 * 
	 * @return
	 * @throws Exception
	 */
	public HomePage verifyAppLogo() throws Exception {
		GlobalController.brw.verifyUIComponent(this.LT_Logo);
		return GlobalController.brw.initElements(HomePage.class);
	}

	/**
	 * This method is for clicking on Login button
	 * 
	 * @return
	 * @throws Exception
	 */
	public LoginPage clickLoginLink() throws Exception {
		GlobalController.brw.click(this.HL_Login);
		return GlobalController.brw.initElements(LoginPage.class);
	}

	/**
	 * Click Ad Post Link
	 * 
	 * @return
	 * @throws Exception
	 */
	public MyAdsPage clickPostAdLink() throws Exception {
		GlobalController.brw.click(this.HL_AdsLink);
		return GlobalController.brw.initElements(MyAdsPage.class);
	}

}
