package com.bikroy.framework.pageclasses;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.bikroy.framework.globals.GlobalConsts;
import com.bikroy.framework.globals.GlobalController;
import com.bikroy.framework.interfaces.ElementField;
import com.bikroy.framework.interfaces.LocateBy;
import com.bikroy.framework.pageclasses.GlobalEnumsPage.ProvideItemDetails;
import com.bikroy.framework.utilities.xmlutils.TestData;

public class AdDetailsPage {

	@LocateBy(id = "name")
	private ElementField TB_Owner_Name;

	@LocateBy(id = "phone0")
	private ElementField TB_Phone_Number;

	@LocateBy(id = "email")
	private ElementField TB_Email_ID;

	@LocateBy(xpath = "//button[@name='post']")
	private ElementField BT_Ad_Post;

	@LocateBy(xpath = "//a[contains(@href,'/my/dashboard')]")
	private ElementField HL_MyAds;

	@LocateBy(id = "password")
	private ElementField TB_PSWRD;

	@LocateBy(id = "password-confirm")
	private ElementField TB_REPSWRD;

	@LocateBy(xpath = "//button[@class = 'ui-btn is-secondary has-busy is-auto']")
	private ElementField BT_SETPSWRD;

	@LocateBy(id = "fields-condition-value")
	private ElementField DD_COND_VALUE;

	@LocateBy(xpath = "//div[@class='post-upload']")
	private ElementField BT_UPLOAD_FILE;

	@LocateBy(id = "fields-brand-value")
	private ElementField DD_BRAND_VALUE;

	@LocateBy(id = "fields-model-value")
	private ElementField TB_MODEL_VALUE;

	@LocateBy(id = "fields-title-value")
	private ElementField TB_TITLE_VALUE;

	@LocateBy(id = "fields-description-value")
	private ElementField TB_Description_Value;

	@LocateBy(id = "fields-price-amount")
	private ElementField TB_Price_Value;

	/**
	 * Select Condition
	 *
	 * @param condition
	 * @return
	 */
	public AdDetailsPage selectConditionFromDropdown(String condition) throws Exception {
		GlobalController.brw.selectDropDown(this.DD_COND_VALUE, condition);
		return GlobalController.brw.initElements(AdDetailsPage.class);
	}

	/**
	 * Select Brand
	 *
	 * @param brand
	 * @return
	 */
	public AdDetailsPage selectBrandOption(String brand) throws Exception {
		GlobalController.brw.selectDropDown(this.DD_BRAND_VALUE, brand);
		return GlobalController.brw.initElements(AdDetailsPage.class);
	}

	/**
	 * Enter Model
	 *
	 * @param model
	 * @return
	 */
	public AdDetailsPage enterModel(String model) throws Exception {
		GlobalController.brw.setText(this.TB_MODEL_VALUE, model);
		return GlobalController.brw.initElements(AdDetailsPage.class);
	}

	/**
	 * Enter Title
	 *
	 * @param title
	 * @return
	 */
	public AdDetailsPage enterTitle(String title) throws Exception {
		GlobalController.brw.setText(this.TB_TITLE_VALUE, title);
		return GlobalController.brw.initElements(AdDetailsPage.class);
	}

	/**
	 * Enter Description
	 *
	 * @param description
	 * @return
	 */
	public AdDetailsPage entertDescription(String description) throws Exception {
		GlobalController.brw.setText(this.TB_Description_Value, description);
		return GlobalController.brw.initElements(AdDetailsPage.class);
	}

	/**
	 * Enter Price
	 *
	 * @param price
	 * @return
	 * @throws Exception
	 */
	public AdDetailsPage entertPrice(String price) throws Exception {
		GlobalController.brw.setText(this.TB_Price_Value, price);
		return GlobalController.brw.initElements(AdDetailsPage.class);
	}

	/**
	 * Enter Owner Email
	 *
	 * @param price
	 * @return
	 * @throws Exception
	 */
	public AdDetailsPage enterEmailId(String email) throws Exception {
		GlobalController.brw.setText(this.TB_Email_ID, email);
		return GlobalController.brw.initElements(AdDetailsPage.class);
	}

	/**
	 * Enter Owner Name
	 *
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public AdDetailsPage entertName(String name) throws Exception {
		GlobalController.brw.setText(this.TB_Owner_Name, name);
		return GlobalController.brw.initElements(AdDetailsPage.class);
	}

	/**
	 * Enter Owner Phone Number
	 *
	 * @param phone_number
	 * @return
	 * @throws Exception
	 */
	public AdDetailsPage enterPhoneNumber(String phone) throws Exception {
		GlobalController.brw.setText(this.TB_Phone_Number, phone);
		return GlobalController.brw.initElements(AdDetailsPage.class);
	}

	/**
	 * Click on Phone Adds button
	 *
	 * @param phone_number
	 * @return
	 * @throws Exception
	 */
	public AdDetailsPage clickAdPostBtn() throws Exception {
		GlobalController.brw.click(this.BT_Ad_Post);
		return GlobalController.brw.initElements(AdDetailsPage.class);
	}

	/**
	 * Fill Details And Click On Post Add Button
	 *
	 * @param phn
	 * @return
	 * @throws Exception
	 */
	public AdDetailsPage enterAboutYouDetails(String name, String phn, String email) throws Exception {
		this.entertName(name);
		this.enterPhoneNumber(phn);
		this.enterEmailId(email);
		this.clickAdPostBtn();
		return GlobalController.brw.initElements(AdDetailsPage.class);
	}

	/**
	 * Enter Value in Password Field
	 *
	 * @return
	 * @throws Exception
	 */
	public MyAdsPage enterValueInPswdField(String pswd) throws Exception {
		GlobalController.brw.setText(this.TB_PSWRD, pswd);
		return GlobalController.brw.initElements(MyAdsPage.class);
	}

	/**
	 * Enter Value in Confirm Password Field
	 *
	 * @return
	 * @throws Exception
	 */
	public MyAdsPage enterValueInConfirmPswdField(String pswd) throws Exception {
		GlobalController.brw.setText(this.TB_REPSWRD, pswd);
		return GlobalController.brw.initElements(MyAdsPage.class);
	}

	/**
	 * Click on Set Password Btn
	 *
	 * @return
	 * @throws Exception
	 */
	public MyAdsPage clickSetPswdBtn() throws Exception {
		GlobalController.brw.click(this.BT_SETPSWRD);
		return GlobalController.brw.initElements(MyAdsPage.class);
	}

	/**
	 * Set Passwrd To Secure Your Add
	 *
	 * @return
	 * @throws Exception
	 */
	public MyAdsPage setPasswordToSecureAdd(String pswd) throws Exception {
		this.enterValueInPswdField(pswd);
		this.enterValueInConfirmPswdField(pswd);
		this.clickSetPswdBtn();
		return GlobalController.brw.initElements(MyAdsPage.class);
	}

	/**
	 * Set Password To Secure Your Add
	 *
	 * @param testDataSetName
	 * @param category
	 * @return page object
	 * @throws Exception
	 */
	public MyAdsPage setPasswordToSecureAdPost(String testDataSetName, String category) throws Exception {
		String password = TestData.read("AdsDetailsPageData.xml", testDataSetName, "password", category);
		this.enterValueInPswdField(password);
		this.enterValueInConfirmPswdField(password);
		this.clickSetPswdBtn();
		return GlobalController.brw.initElements(MyAdsPage.class);
	}

	/**
	 * Click on MyAdds Button
	 *
	 * @return
	 * @throws Exception
	 */
	public MyAdsPage clickMyAdsLink() throws Exception {
		GlobalController.brw.click(this.HL_MyAds);
		return GlobalController.brw.initElements(MyAdsPage.class);
	}

	/**
	 * Click on Upload file button
	 *
	 * @throws Exception
	 */
	public AdDetailsPage clickUploadFile() throws Exception {
		GlobalController.brw.click(this.BT_UPLOAD_FILE);
		return GlobalController.brw.initElements(AdDetailsPage.class);
	}

	/**
	 * Click on Browse button
	 *
	 * @throws Exception
	 */
	public AdDetailsPage browseForCsvFileToUpload(String imageFile) throws Exception {
		GlobalController.brw.openFileUsingOpenFileDialog(GlobalConsts.getProjectDir() + GlobalConsts.ImageFileToBeUploaded + imageFile);
		return GlobalController.brw.initElements(AdDetailsPage.class);
	}

	/**
	 * This wrapper method is designed to provide positive item details to post
	 * an ad. This maps the requirement to automate with valid set of data to
	 * achieve positive testing.
	 *
	 * @param testDataSetName
	 * @param category
	 * @return page object
	 * @throws Exception
	 */
	public String enterItemDetailsToPostAd(ProvideItemDetails itemDetails, String testDataSetName, String category) throws Exception {
		String condition = TestData.read("AdsDetailsPageData.xml", testDataSetName, "condition", category);
		String brand = TestData.read("AdsDetailsPageData.xml", testDataSetName, "brand", category);
		String model = TestData.read("AdsDetailsPageData.xml", testDataSetName, "model", category);
		String title = TestData.read("AdsDetailsPageData.xml", testDataSetName, "title", category);
		String description = TestData.read("AdsDetailsPageData.xml", testDataSetName, "description", category);
		String price = TestData.read("AdsDetailsPageData.xml", testDataSetName, "price", category);
		String name = TestData.read("AdsDetailsPageData.xml", testDataSetName, "name", category);
		String phn = TestData.read("AdsDetailsPageData.xml", testDataSetName, "phn", category);
		String email = TestData.read("AdsDetailsPageData.xml", testDataSetName, "email", category);
		String imageFile = TestData.read("AdsDetailsPageData.xml", testDataSetName, "imageFile", category);
		String password = TestData.read("AdsDetailsPageData.xml", testDataSetName, "password", category);

		switch (itemDetails) {

		case LOGGEDINUSER:
			this.clickUploadFile();
			this.browseForCsvFileToUpload(imageFile);
			// this.selectConditionFromDropdown(condition);
			this.selectBrandOption(brand);
			this.enterModel(model);
			this.enterTitle(title);
			this.entertDescription(description);
			this.entertPrice(price);
			// this.enterPhoneNumber(phn);
			// this.clickAdPostBtn();
			break;

		case NOLOGGEDINUSER:
			this.clickUploadFile();
			this.browseForCsvFileToUpload(imageFile);
			this.selectConditionFromDropdown(condition);
			this.selectBrandOption(brand);
			this.enterModel(model);
			this.enterTitle(title);
			this.entertDescription(description);
			this.entertPrice(price);
			this.entertName(name);
			this.enterPhoneNumber(phn);
			this.enterEmailId(email);
			this.clickAdPostBtn();
			this.enterValueInPswdField(password);
			this.enterValueInConfirmPswdField(password);
			this.clickSetPswdBtn();
			break;

		default:
			throw new Exception("Invalid Step Provided. Not defined in enumeration");
		}

		return title;
	}

	public String imageWrite(String testDataSetName, String category) throws Exception {
		BufferedImage image = null;
		File f = null;
		String imageName = TestData.read("AdsDetailsPageData.xml", testDataSetName, "imageName", category);

		// write image
		try {
			f = new File(GlobalConsts.getProjectDir() + GlobalConsts.ImageFileToBeUploaded + imageName);
			ImageIO.write(image, "jpg", f);
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}

		return imageName;

	}

}
