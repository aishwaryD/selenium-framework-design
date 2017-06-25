package com.bikroy.framework.pageclasses;

import com.bikroy.framework.globals.GlobalController;
import com.bikroy.framework.interfaces.ElementField;
import com.bikroy.framework.interfaces.LocateBy;
import com.bikroy.framework.pageclasses.GlobalEnumsPage.SignUpField;
import com.bikroy.framework.utilities.xmlutils.TestData;

public class SignUpPage {

	@LocateBy(xpath = "//a[contains(@href,'/users/login')]")
	private ElementField HL_LogIn;

	@LocateBy(xpath = "//a[contains(@href,'/users/signup')]")
	private ElementField HL_SignUp;

	@LocateBy(id = "name")
	private ElementField TB_UserName;

	@LocateBy(id = "email")
	private ElementField TB_Email;

	@LocateBy(id = "password")
	private ElementField TB_Password;

	@LocateBy(id = "password-confirm")
	private ElementField TB_ConfirmPassword;

	@LocateBy(xpath = "//button[contains(@class,'ui-btn is-secondary has-busy is-auto is-island')]")
	private ElementField BT_SignUp;

	@LocateBy(xpath = "//a[contains(@class,'ui-btn is-secondary is-island')]")
	private ElementField BT_SignUpUsingEmail;

	/**
	 * This method will verify the Sign Up page
	 *
	 * @return
	 * @throws Exception
	 */
	public SignUpPage verifySignUpPage() throws Exception {
		GlobalController.brw.verifyUIComponent(this.HL_LogIn);
		return GlobalController.brw.initElements(SignUpPage.class);
	}

	/**
	 * This method will verify the UserName Text Field
	 *
	 * @return
	 * @throws Exception
	 */
	public SignUpPage verifyUserNameField() throws Exception {
		GlobalController.brw.verifyUIComponent(this.TB_UserName);
		return GlobalController.brw.initElements(SignUpPage.class);
	}

	/**
	 * This method will click on Sign Up Link
	 *
	 * @return
	 * @throws Exception
	 */
	public SignUpPage clickSignUpLink() throws Exception {
		GlobalController.brw.click(this.HL_SignUp);
		return GlobalController.brw.initElements(SignUpPage.class);
	}

	/**
	 * This method will click on Sign Up Button
	 *
	 * @return
	 * @throws Exception
	 */
	public SignUpPage clickSignUpBtn() throws Exception {
		GlobalController.brw.click(this.BT_SignUp);
		return GlobalController.brw.initElements(SignUpPage.class);
	}

	/**
	 * This method will set username on corresponding field
	 *
	 * @return
	 * @throws Exception
	 */
	public SignUpPage setUserName(String name) throws Exception {
		GlobalController.brw.setText(this.TB_UserName, name);
		return GlobalController.brw.initElements(SignUpPage.class);
	}

	/**
	 * This method will set emailId on corresponding field
	 *
	 * @return
	 * @throws Exception
	 */
	public SignUpPage setEmailId(String email) throws Exception {
		GlobalController.brw.setText(this.TB_Email, email);
		return GlobalController.brw.initElements(SignUpPage.class);
	}

	/**
	 * This method will set password on corresponding field
	 *
	 * @return
	 * @throws Exception
	 */
	public SignUpPage setPassword(String pswd) throws Exception {
		GlobalController.brw.setText(this.TB_Password, pswd);
		return GlobalController.brw.initElements(SignUpPage.class);
	}

	/**
	 * This method will set confirm password on corresponding field
	 *
	 * @return
	 * @throws Exception
	 */
	public SignUpPage setConfirmPassword(String pswd) throws Exception {
		GlobalController.brw.setText(this.TB_ConfirmPassword, pswd);
		return GlobalController.brw.initElements(SignUpPage.class);
	}

	/**
	 * This method will sign up with a random user with all possible scenarios
	 * and error conditions
	 *
	 * @param userName
	 * @param emailId
	 * @param password
	 * @return
	 * @throws Exception
	 */

	public String signUpWithNewUser(SignUpField signUpField, String testDataSetName, String category) throws Exception {
		String userName = TestData.read("SignUpPageData.xml", testDataSetName, "userName", category);
		String emailId = TestData.read("SignUpPageData.xml", testDataSetName, "emailId", category);
		String password = TestData.read("SignUpPageData.xml", testDataSetName, "password", category);
		String diffPassword = TestData.read("SignUpPageData.xml", testDataSetName, "diffPassword", category);

		switch (signUpField) {

		case USERNAME:
			this.setUserName(userName);
			break;

		case EMAILID:
			this.setEmailId(emailId);
			break;

		case PASSWORD:
			this.setPassword(password);
			break;

		case CONFIRMPSWD:
			this.setConfirmPassword(password);
			break;

		case INVALIDCONFIRMPSWD:
			this.setConfirmPassword(diffPassword);
			break;

		case EMPTY:
			this.setUserName("");
			this.setEmailId("");
			this.setPassword("");
			this.setConfirmPassword("");
			break;

		case ALL:
			this.setUserName(userName);
			this.setEmailId(emailId);
			this.setPassword(password);
			this.setConfirmPassword(password);
			break;

		default:
			throw new Exception("Invalid Step Provided. Not defined in enumeration");

		}
		this.clickSignUpBtn();

		return emailId;
	}
}
