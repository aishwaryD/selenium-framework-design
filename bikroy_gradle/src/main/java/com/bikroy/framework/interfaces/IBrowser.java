package com.bikroy.framework.interfaces;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.bikroy.framework.globals.GlobalEnumerations.TextComparators;

/**
 * IBrowser interface is a parent interface of all the classes/interfaces in
 * Automation tool independence layer. This interface has all methods
 * declarations which could be valid for any automation tool. E.g. setText,
 * selectText, etc. All automation tool related objects would be created using
 * IBrowser interface only.
 * 
 * @author Aishwarya Dwivedi
 * @since 1.0
 * 
 * @version 1.0
 */
public interface IBrowser {
	public <T> T initElements(Class<T> pageClassToProxy) throws IllegalArgumentException, IllegalAccessException;

	public void check(ElementField ef) throws Exception;

	public void clearCookies() throws Exception;

	public void clearTextBox(ElementField ef) throws Exception;

	public void click(ElementField ef) throws Exception;

	void clickElementForElement(ElementField ef, ElementField efToAppear) throws Exception;

	void clickFirstTableRow(ElementField efTable) throws Exception;

	public void clickHiddenHref(ElementField ef) throws Exception;

	void clickLinkText(ElementField ef) throws Exception;

	void clickLinkText(ElementField ef, boolean optionallyWait) throws Exception;

	void clickListItem(ElementField ef, String targetValue) throws Exception;

	void clickRowWithText(ElementField ef, String text) throws Exception;

	void clickTableCell(ElementField efTable, int row, int col) throws Exception;

	void clickTableCellWithText(ElementField efTable, String text) throws Exception;

	void clickTableCellWithText(ElementField efTable, String text, boolean ignoreCase) throws Exception;

	void clickTableCellWithText(ElementField efTable, String text, boolean ignoreCase, TextComparators comparator) throws Exception;

	public void clickTableRow(ElementField efTable, int rowNum) throws Exception;

	public String getAttribute(ElementField ef, String attributeName) throws Exception;

	public WebDriver getCurrentWebDriver();

	public WebElement getListItem(ElementField ef, String targetValue) throws Exception;

	public String getText(ElementField ef) throws Exception;

	boolean isElementPresent(ElementField element) throws Exception;

	boolean isSelected(ElementField ef) throws Exception;

	public void maximize() throws Exception;

	public void navigateToUrl(String url) throws Exception;

	void openFileUsingOpenFileDialog(String fileName);

	public void pressEnter(ElementField ef) throws Exception;

	void pressTab(ElementField ef) throws Exception;

	public void selectDropDown(ElementField ef, String targetValue) throws Exception;

	public boolean isValuePresentInDropDown(ElementField ef, String valueToVerify) throws Exception;

	void selectListItem(ElementField ef, String targetValue) throws Exception;

	public void selectTableRowItem(ElementField elem, String data) throws Exception;

	public void selectText(ElementField ef, String text) throws Exception;

	public void setcurrentDate(ElementField el) throws Exception;

	public void setText(ElementField ef, String text) throws Exception;

	public void setText(ElementField ef, String text, boolean trimText) throws Exception;

	public void setText(ElementField ef, String text, boolean trimText, boolean allCaps) throws Exception;

	void takeScreenShot(String methodName);

	public void uncheck(ElementField ef) throws Exception;

	public void waitForAjaxElementNotPresent(ElementField locator, int timeoutInSeconds) throws Exception;

	void waitForElement(ElementField element, int timeout) throws Exception;

	void validateSavedTestDataInTable(ElementField efTable, String text, boolean ignoreCase, TextComparators comparator) throws Exception;

	void validateSavedTestDataInTable(ElementField efTable, String text, boolean ignoreCase) throws Exception;

	void validateSavedTestDataInTable(ElementField efTable, String text) throws Exception;

	void verifyUIComponent(ElementField ef) throws Exception;

	public void waitForWebElementPresent();

	public void quit();

	void acceptAlert();

	void pressEnterKey(ElementField ef) throws Exception;

	void validateSavedTestData(ElementField efTable, String text, boolean ignoreCase, TextComparators comparator) throws Exception;

	void validateSavedTestData(ElementField efTable, String text, boolean ignoreCase) throws Exception;

	void validateSavedTestData(ElementField efTable, String text) throws Exception;

}
