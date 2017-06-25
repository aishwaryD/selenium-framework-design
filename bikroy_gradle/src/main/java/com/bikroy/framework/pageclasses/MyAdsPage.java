package com.bikroy.framework.pageclasses;

import org.testng.Assert;

import com.bikroy.framework.globals.GlobalController;
import com.bikroy.framework.interfaces.ElementField;
import com.bikroy.framework.interfaces.LocateBy;

public class MyAdsPage {

	@LocateBy(xpath = "//div[@class = 'item-content']/a")
	private ElementField TD_Item_Content;

	@LocateBy(xpath = "//a[contains(@href,'/users/logout')]")
	private ElementField HL_Logout;

	@LocateBy(xpath = "//h3[@class = 'is-section']/span")
	private ElementField TD_Ad_Section;

	@LocateBy(xpath = "//a[text() = 'English']")
	private ElementField LT_English;

	@LocateBy(xpath = "//a[contains(@href,'/post-ad')]")
	private ElementField HL_AdsLink;

	@LocateBy(xpath = "//a[contains(@href,'/post-ad/category?type=for_sale')]")
	private ElementField HL_SellItemServiceLink;

	@LocateBy(xpath = "//a[contains(@href,'/bn/my/settings')]")
	private ElementField BT_Settings;

	/**
	 * Validate Ads Under Review Count
	 *
	 * @return
	 * @throws Exception
	 */
	public MyAdsPage validateAdsUnderReviewCount() throws Exception {
		GlobalController.brw.verifyUIComponent(this.TD_Ad_Section);
		String val = GlobalController.brw.getText(this.TD_Ad_Section);
		int num = Integer.parseInt(val);
		if (num < 0) {
			Assert.fail();
		}
		return GlobalController.brw.initElements(MyAdsPage.class);
	}

	/**
	 * Validate Posted Item Content
	 *
	 * @return
	 * @throws Exception
	 */
	public MyAdsPage validateItemContent(String itemTitle) throws Exception {
		GlobalController.brw.validateSavedTestData(this.TD_Item_Content, itemTitle);
		return GlobalController.brw.initElements(MyAdsPage.class);
	}

	/**
	 * This method will click on Logout Button
	 *
	 * @throws Exception
	 */
	public HomePage clickLogoutLink() throws Exception {
		GlobalController.brw.clickLinkText(this.BT_Settings);
		GlobalController.brw.clickLinkText(this.HL_Logout);
		return GlobalController.brw.initElements(HomePage.class);
	}

	/**
	 * Click To Set English Language
	 *
	 * @return
	 * @throws Exception
	 */
	public MyAdsPage setEnglishLanguage() throws Exception {
		GlobalController.brw.click(this.LT_English);
		return GlobalController.brw.initElements(MyAdsPage.class);
	}

	/**
	 * Click Post Ad Button
	 *
	 * @return
	 * @throws Exception
	 */
	public MyAdsPage clickPostAdLink() throws Exception {
		GlobalController.brw.click(this.HL_AdsLink);
		return GlobalController.brw.initElements(MyAdsPage.class);
	}

	/**
	 * Click Sell An Item Service Link
	 *
	 * @return
	 * @throws Exception
	 */
	public CategoryPage clickSellAnItemServiceLink() throws Exception {
		GlobalController.brw.click(this.HL_SellItemServiceLink);
		return GlobalController.brw.initElements(CategoryPage.class);
	}

}
