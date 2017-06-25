package com.bikroy.framework.interfaces.impl.selenium;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.bikroy.framework.globals.GlobalConsts;
import com.bikroy.framework.globals.GlobalEnumerations.BrowsersTypes;
import com.bikroy.framework.globals.GlobalEnumerations.LogicalOperators;
import com.bikroy.framework.globals.GlobalEnumerations.TextComparators;
import com.bikroy.framework.globals.Logger;
import com.bikroy.framework.interfaces.ElementField;
import com.bikroy.framework.interfaces.IBrowser;
import com.bikroy.framework.interfaces.LocateBy;
import com.bikroy.framework.utilities.textutilities.ArrayUtilities;
import com.bikroy.framework.utilities.textutilities.TextUtilities;

/**
 * Class to implement all IBrowser methods. Every time, we need to change
 * automation tool, we just need to change this class with a new class.
 * 
 * @author Aishwarya Dwivedi
 * @since 1.0
 * 
 * @version 1.0
 */
public class SeleniumBrowser implements IBrowser {
	// Initialize private logger object
	private static Logger logger = new Logger().getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
	protected static WebDriverWait waitObj;
	private static boolean _waitEligibilityDeclaredByPreviousElementField = false;

	private static <T> T _instantiatePage(Class<T> pageClassToProxy) {
		try {
			try {
				Constructor<T> constructor = pageClassToProxy.getConstructor();
				return constructor.newInstance();
			} catch (NoSuchMethodException e) {
				return pageClassToProxy.newInstance();
			}
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	BrowsersTypes browserType = null;

	WebDriver driver = null;

	private String _extendedTextToXPath = "";

	public SeleniumBrowser() {
	}

	public SeleniumBrowser(BrowsersTypes brwType) {
		this.browserType = brwType;
	}

	private void _clickUsingJavaScript(WebElement we) {
		SeleniumBrowser.logger.enterMethod();
		((JavascriptExecutor) this._getDefaultIEBrowser()).executeScript("arguments[0].click();", we);
		SeleniumBrowser.logger.exitMethod();
	}

	private Object _executeJavaScript(String jsCode) {
		return ((JavascriptExecutor) this._getDefaultIEBrowser()).executeScript(jsCode);
	}

	/**
	 * 
	 * @param ef
	 * @return
	 * @throws Exception
	 */
	private WebElement _findElement(ElementField ef) throws Exception {
		if (ef == null) {
			return null;
		}

		WebElement we = null;
		try {
			we = this._getDefaultIEBrowser().findElement(this._getByLocator(ef));
		} catch (NoSuchElementException nse) {
			throw new Exception("Element not found for provided element field");
		}

		if (we == null) {
			throw new Exception("Element not found: " + ef.FieldId);
		}

		return we;
	}

	private WebDriver _getBrowserWebDriver(BrowsersTypes browserType) {
		SeleniumBrowser.logger.enterMethod();
		if (this.driver != null) {
			return this.driver;
		}

		DesiredCapabilities capabilities = null;

		switch (browserType) {
		case InternetExplorer:
			capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			System.setProperty("webdriver.ie.driver", "./resources/brwDrivers/IEDriverServer.exe");
			this.driver = new InternetExplorerDriver(capabilities);

			break;
		case Firefox:
			capabilities = DesiredCapabilities.firefox();
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			this.driver = new FirefoxDriver(capabilities);

			break;
		case Chrome:
			capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			System.setProperty("webdriver.ie.driver", "./resources/brwDrivers/chromedriver.exe");
			this.driver = new ChromeDriver(capabilities);
			break;
		}

		SeleniumBrowser.logger.exitMethod();
		return this.driver;
	}

	/**
	 * 
	 * @param ef
	 * @return
	 * @throws Exception
	 */
	private By _getByLocator(ElementField ef) throws Exception {
		SeleniumBrowser.logger.enterMethod();
		By biLocator = null;
		SeleniumBrowser.logger.info("Getting locator for " + ef.FieldId);

		try {
			if (TextUtilities.isBlank(ef.elementLocatorId) == false) {
				SeleniumBrowser.logger.info("Locator info found for ID attribute as: " + ef.elementLocatorId);
				biLocator = By.id(ef.elementLocatorId);
			} else if (TextUtilities.isBlank(ef.elementLocatorName) == false) {
				SeleniumBrowser.logger.info("Locator info found for NAME attribute as: " + ef.elementLocatorName);
				biLocator = By.name(ef.elementLocatorName);
			} else if (TextUtilities.isBlank(ef.elementLocatorXpath) == false) {
				SeleniumBrowser.logger.info("Locator info found for XPATH attribute as: " + ef.elementLocatorXpath);
				biLocator = this._parseElementOnXpath(ef);
			} else if (TextUtilities.isBlank(ef.elementLocatorCss) == false) {
				SeleniumBrowser.logger.info("Locator info found for CSS attribute as: " + ef.elementLocatorCss);
				biLocator = By.cssSelector(ef.elementLocatorCss);
			} else if ((TextUtilities.isBlank(ef.elementLocatorCustomAttr) == false)
					&& (TextUtilities.isBlank(ef.elementLocatorCustomAttrVal) == false)) {
				switch (ef.elementLocatorCustomAttr) {
				case "linktext":
					SeleniumBrowser.logger.info("Locator info found for linktext attribute as: " + ef.elementLocatorCustomAttrVal);
					biLocator = By.linkText(ef.elementLocatorCustomAttrVal);
					break;
				default:
					break;
				}
			} else {
				throw new Exception("No field information provided");
			}
		} catch (Exception e) {
			SeleniumBrowser.logger.error(e);
			throw e;
		}

		SeleniumBrowser.logger.exitMethod();
		return biLocator;
	}

	private WebDriver _getDefaultIEBrowser() {
		if (this.driver == null) {
			this.driver = this._getBrowserWebDriver(this.browserType == null ? BrowsersTypes.InternetExplorer : this.browserType);
		}

		this.driver.manage().timeouts().implicitlyWait(GlobalConsts.IMPLICIT_TIME_LIMIT, TimeUnit.SECONDS);
		return this.driver;
	}

	private String _getExtendedTextToXPath() {
		SeleniumBrowser.logger.info(this._extendedTextToXPath + " value asked");
		return this._extendedTextToXPath;
	}

	private WebElement _getListItem(ElementField ef, String targetValue) throws Exception {
		SeleniumBrowser.logger.enterMethod();
		this._setExtendedTextToXPath("");

		List<WebElement> liList = this._getDefaultIEBrowser().findElements(this._getByLocator(ef));

		// To use "USE_EXTENDED_TEXT_TO_XPATH", make sure _GetByLocator gets
		// called before its use
		if (targetValue == "USE_EXTENDED_TEXT_TO_XPATH") {
			targetValue = TextUtilities.replaceAllEscapeRegEx(this._getExtendedTextToXPath(), GlobalConsts.PRE_POST_EXTENDED_TEXTS_MARKER,
					"");
		}
		int targetValueIndex = 0;

		for (WebElement we : liList) {
			SeleniumBrowser.logger.info("targetValue to find in listbox: \"" + targetValue + "\"");
			SeleniumBrowser.logger.info("current element text by we.getText(): \"" + we.getText() + "\"");
			if (TextUtilities.compareValue(targetValue.trim(), we.getText().trim(), true, TextComparators.equals)) {
				break;
			}
			targetValueIndex++;
		}

		if (targetValueIndex >= liList.size()) {
			throw new Exception("No list item found with value \"" + targetValue + "\" in its list element");
		}
		SeleniumBrowser.logger.info("targetValue found at index: " + targetValueIndex);
		SeleniumBrowser.logger.info(liList.get(targetValueIndex).getText());
		WebElement targetElement = liList.get(targetValueIndex);

		SeleniumBrowser.logger.exitMethod();
		return targetElement;
	}

	private WebElement _getTable(ElementField ef) throws Exception {
		this._waitForElement(ef);

		WebElement tableElement = this._getDefaultIEBrowser().findElement(this._getByLocator(ef));

		return tableElement;
	}

	private WebElement _getTableCell(ElementField efTable, int row, int col) throws Exception {
		SeleniumBrowser.logger.enterMethod();
		WebElement table = this._getTable(efTable);
		List<WebElement> tableRows = this._getTableRows(table);

		if (tableRows.size() > 0) {
			WebElement firstRow = tableRows.get(tableRows.size() - 1);
			List<WebElement> tableCols = this._getTableRowCols(firstRow);

			if (tableCols.size() > 0) {
				if ((row >= 0) && (row <= tableRows.size()) && (col >= 0) && (col <= tableCols.size())) {
					WebElement rowEle = tableRows.get(row);
					return this._getTableRowCols(rowEle).get(col);
				}
			}
		}

		return null;
	}

	private List<WebElement> _getTableCells(ElementField efTable) throws Exception {
		SeleniumBrowser.logger.enterMethod();
		WebElement table = this._getTable(efTable);
		List<WebElement> tableCells = this._getTableCells(table);

		return tableCells;
	}

	private List<WebElement> _getTableCells(WebElement tableElement) throws Exception {
		Assert.assertTrue(tableElement.isDisplayed());
		Assert.assertTrue(tableElement.isEnabled());

		List<WebElement> cellElements = tableElement.findElements(By.tagName("td"));

		SeleniumBrowser.logger.info("Number of cells in table: " + cellElements.size());

		return cellElements;
	}

	private List<WebElement> _getTableRowCols(WebElement tableRow) {
		if (tableRow == null) {
			return null;
		}

		List<WebElement> cols = tableRow.findElements(By.tagName("td"));

		SeleniumBrowser.logger.info("Number of cols in table: " + cols.size());

		return cols;
	}

	private List<WebElement> _getTableRows(WebElement tableElement) throws Exception {
		Assert.assertTrue(tableElement.isDisplayed());
		Assert.assertTrue(tableElement.isEnabled());

		List<WebElement> rowsElements = tableElement.findElements(By.tagName("tr"));

		SeleniumBrowser.logger.info("Number of rows in table: " + rowsElements.size());

		return rowsElements;
	}

	private WebDriverWait _getWebDriverWait(int timeoutInSeconds) {
		SeleniumBrowser.waitObj = new WebDriverWait(this.getCurrentWebDriver(), timeoutInSeconds);

		return SeleniumBrowser.waitObj;
	}

	private By _parseElementOnXpath(ElementField ef) {
		if (ef == null) {
			return null;
		}
		SeleniumBrowser.logger.enterMethod();

		ef.elementLocatorXpath = TextUtilities.nullToBlank(ef.elementLocatorXpath, false);
		if (TextUtilities.contains(ef.elementLocatorXpath, GlobalConsts.XPathToTextSeparator)) {
			int lastIndex = TextUtilities.indexOf(ef.elementLocatorXpath, GlobalConsts.XPathToTextSeparator);
			String xpathString = TextUtilities.substring(ef.elementLocatorXpath, 0, lastIndex);
			String extendedDataToXpath = TextUtilities.substring(ef.elementLocatorXpath,
					lastIndex + GlobalConsts.XPathToTextSeparator.length(), ef.elementLocatorXpath.length());
			SeleniumBrowser.logger.info("Xpath: " + xpathString + " || Extended Text: " + extendedDataToXpath);
			this._setExtendedTextToXPath(extendedDataToXpath);

			ef.elementLocatorXpath = xpathString;
		}

		SeleniumBrowser.logger.info(ef.elementLocatorXpath + " is used");
		By loc = By.xpath(ef.elementLocatorXpath);

		SeleniumBrowser.logger.exitMethod();
		return loc;
	}

	private void _setExtendedTextToXPath(String str) {
		this._extendedTextToXPath = str;
		SeleniumBrowser.logger.info(this._extendedTextToXPath + " value set");
	}

	private void _setText(ElementField ef, String text, boolean trimText, boolean allCaps) throws Exception {
		List<String> dataList = new ArrayList<>();
		int attemptsCount = 1;
		while ((TextUtilities.compareValue(text, dataList, true, TextComparators.equals, LogicalOperators.OR) == false)
				&& (attemptsCount++ < 5)) {
			this.clearTextBox(ef);
			if (trimText) {
				text = text.trim();
			}
			if (allCaps) {
				text = text.toUpperCase();
			}
			this._findElement(ef).click();
			this._findElement(ef).sendKeys(text);

			dataList.clear();
			dataList.add(this.getAttribute(ef, "text"));
			dataList.add(this.getAttribute(ef, "value"));
		}
	}

	/**
	 * Wait for element enabled
	 * 
	 * @param element
	 * @param timeout
	 * @throws Exception
	 */
	private void _waitForAjaxElementVisibilityAndEnabled(By byEle, int timeoutInSeconds) throws Exception {
		if (byEle != null) {
			this._getWebDriverWait(timeoutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(byEle));
			this._getWebDriverWait(timeoutInSeconds).until(ExpectedConditions.visibilityOf(this.driver.findElement(byEle)));
		} else {

		}
	}

	/**
	 * Wait for element enabled
	 * 
	 * @param element
	 * @param timeout
	 * @throws Exception
	 */
	private void _waitForAjaxElementVisibilityAndEnabled(ElementField element, int timeoutInSeconds) throws Exception {
		By ele = null;
		try {
			ele = this._getByLocator(element);
		} catch (Exception e) {
			throw e;
		}

		if (ele != null) {
			this._waitForAjaxElementVisibilityAndEnabled(ele, timeoutInSeconds);
		}
	}

	private void _waitForElement(ElementField element) throws Exception {
		SeleniumBrowser.logger.enterMethod("Waiting for element: " + element.FieldId);
		this.waitForElement(element, GlobalConsts.LEAST_TIMEOUT_MILLISECONDS);
		SeleniumBrowser.logger.exitMethod();
	}

	private boolean _waitForJStoLoad() {
		SeleniumBrowser.logger.enterMethod();
		// wait for jQuery to load
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return ((Long) SeleniumBrowser.this._executeJavaScript("return jQuery.active") == 0);
				} catch (Exception e) {
					return true;
				}
			}
		};

		// wait for JavaScript to load
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				Object rsltJs = SeleniumBrowser.this._executeJavaScript("return document.readyState");
				if (rsltJs == null) {
					rsltJs = "";
				}
				return rsltJs.toString().equals("complete") || rsltJs.toString().equals("loaded");
			}
		};

		WebDriverWait wait = this._getWebDriverWait(GlobalConsts.LEAST_TIMEOUT_MILLISECONDS);
		boolean waitDone = wait.until(jQueryLoad) && wait.until(jsLoad);

		SeleniumBrowser.logger.exitMethod("DOM load wait done: " + waitDone);
		return waitDone;
	}

	@Override
	public void check(ElementField ef) throws Exception {
		this._waitForElement(ef);

		WebElement ele = this._findElement(ef);
		if (ele.isSelected() == false) {
			ele.click();
		}
	}

	@Override
	public void clearCookies() throws Exception {
		this._getDefaultIEBrowser().manage().deleteAllCookies();
	}

	@Override
	public void clearTextBox(ElementField ef) throws Exception {
		this._waitForElement(ef);

		this._findElement(ef).clear();
	}

	@Override
	public void click(ElementField ef) throws Exception {
		this._waitForElement(ef);

		this._findElement(ef).click();
	}

	@Override
	public void clickElementForElement(ElementField ef, ElementField efToAppear) throws Exception {
		this._waitForElement(ef);
		this._waitForJStoLoad();

		int attempts = 0;
		boolean eleFound = false;
		while ((eleFound == false) && (attempts <= 5)) {
			this._findElement(ef).click();
			this._waitForJStoLoad();

			SeleniumBrowser.logger.info(efToAppear.FieldId + " field appearance check");
			WebElement we = this._findElement(efToAppear);
			eleFound = we.isDisplayed() && we.isEnabled();
			attempts++;
		}
	}

	@Override
	public void clickFirstTableRow(ElementField efTable) throws Exception {
		List<WebElement> tableRows = this._getTableRows(this._getTable(efTable));

		SeleniumBrowser.logger.info("Number of rows in table: " + tableRows.size());
		if (tableRows.size() > 0) {
			WebElement firstRow = tableRows.get(0);

			Assert.assertTrue(firstRow.isDisplayed());
			Assert.assertTrue(firstRow.isEnabled());
			// this._clickUsingJavaScript(firstRow);
			firstRow.click();
		}
	}

	@Override
	public void clickHiddenHref(ElementField ef) throws Exception {
		// NOTE: Don't implement waitForElement here as it is supposed to be
		// hidden and waitForElement checks for visibility

		WebElement we = this._findElement(ef);
		this._clickUsingJavaScript(we);
	}

	@Override
	public void clickLinkText(ElementField ef) throws Exception {
		this.clickLinkText(ef, false);
	}

	@Override
	public void clickLinkText(ElementField ef, boolean optionallyWait) throws Exception {
		SeleniumBrowser.logger.enterMethod();
		if (optionallyWait) {
			try {
				this._waitForElement(ef);
			} catch (Exception e) {
			}
		} else {
			this._waitForElement(ef);
		}

		this.clickHiddenHref(ef);
		SeleniumBrowser.logger.exitMethod();
	}

	@Override
	public void clickListItem(ElementField ef, String targetValue) throws Exception {
		this._waitForElement(ef);

		WebElement we = this.getListItem(ef, targetValue);
		Assert.assertTrue(we.isDisplayed());
		Assert.assertTrue(we.isEnabled());
		we.click();
	}

	@Override
	public void clickRowWithText(ElementField ef, String text) throws Exception {
		List<WebElement> rows = this._getTableRows(this._getTable(ef));
		for (WebElement row : rows) {
			String textAttrVal = row.getAttribute("text");
			if (TextUtilities.compareValue(textAttrVal, text, true, TextComparators.equals)) {
				row.click();
				break;
			}
		}
	}

	@Override
	public void clickTableCell(ElementField efTable, int row, int col) throws Exception {
		SeleniumBrowser.logger.enterMethod();
		WebElement cell = this._getTableCell(efTable, row, col);
		cell.click();
		SeleniumBrowser.logger.exitMethod();
	}

	@Override
	public void clickTableCellWithText(ElementField efTable, String text) throws Exception {
		this.clickTableCellWithText(efTable, text, true, TextComparators.contains);
	}

	@Override
	public void clickTableCellWithText(ElementField efTable, String text, boolean ignoreCase) throws Exception {
		this.clickTableCellWithText(efTable, text, ignoreCase, TextComparators.contains);
	}

	@Override
	public void clickTableCellWithText(ElementField efTable, String text, boolean ignoreCase, TextComparators comparator) throws Exception {
		SeleniumBrowser.logger.enterMethod();
		SeleniumBrowser.logger.info("Finding text: " + text);
		List<WebElement> cells = this._getTableCells(efTable);
		for (WebElement cell : cells) {
			SeleniumBrowser.logger.info(cell.getText() + " -- " + text);
			if (TextUtilities.compareValue(text, cell.getText(), true, comparator)) {
				// cell.click();
				this._clickUsingJavaScript(cell);
				break;
			} else {
				WebElement cellHavingText = cell.findElement(By.xpath("//strong[contains(text(), '" + text + "')]"));
				if (cellHavingText != null) {
					SeleniumBrowser.logger.info("eleme found");
					cellHavingText.click();
				}
			}
		}
		SeleniumBrowser.logger.exitMethod();
	}

	@Override
	public void clickTableRow(ElementField efTable, int rowNum) throws Exception {
		List<WebElement> tableRows = this._getTableRows(this._getTable(efTable));

		for (int rn = 0; rn < tableRows.size(); rn++) {
			if (rn == rowNum) {
				tableRows.get(rn).click();
			}
		}
	}

	@Override
	public String getAttribute(ElementField ef, String attributeName) throws Exception {
		this._waitForElement(ef);

		String attributeValue = this._findElement(ef).getAttribute(attributeName);
		return TextUtilities.nullToBlank(attributeValue, false);
	}

	@Override
	public WebDriver getCurrentWebDriver() {
		return this.driver;
	}

	@Override
	public WebElement getListItem(ElementField ef, String targetValue) throws Exception {
		SeleniumBrowser.logger.enterMethod();
		this._setExtendedTextToXPath("");
		WebElement targetElement = this._getListItem(ef, targetValue);

		SeleniumBrowser.logger.exitMethod();
		return targetElement;
	}

	@Override
	public String getText(ElementField ef) throws Exception {
		this._waitForElement(ef);

		String text = this._findElement(ef).getText();
		return text;
	}

	@Override
	public <T> T initElements(Class<T> pageClassToProxy) throws IllegalArgumentException, IllegalAccessException {
		SeleniumBrowser.logger.enterMethod();
		T page = SeleniumBrowser._instantiatePage(pageClassToProxy);

		Field[] fld = page.getClass().getDeclaredFields();
		if (ArrayUtilities.isEmpty(fld) == false) {
			for (Field f : fld) {
				if (f.isAnnotationPresent(LocateBy.class)) {
					LocateBy l = f.getAnnotation(LocateBy.class);

					ElementField ef = new ElementField();
					SeleniumBrowser.logger.info("ID set to field: " + l.id());
					if (TextUtilities.isBlank(l.id()) == false) {
						ef.elementLocatorId = l.id();
					}
					SeleniumBrowser.logger.info("Name set to field: " + l.name());
					if (TextUtilities.isBlank(l.name()) == false) {
						ef.elementLocatorName = l.name();
					}
					SeleniumBrowser.logger.info("CSS set to field: " + l.css());
					if (TextUtilities.isBlank(l.css()) == false) {
						ef.elementLocatorCss = l.css();
					}
					SeleniumBrowser.logger.info("XPath set to field: " + l.xpath());
					if (TextUtilities.isBlank(l.xpath()) == false) {
						ef.elementLocatorXpath = l.xpath();
					}

					f.setAccessible(true);
					f.set(page, ef);
				}
			}
		}
		SeleniumBrowser.logger.exitMethod();
		return page;
	}

	/**
	 * Verify Element is Present
	 * 
	 * @param locator
	 * @throws Exception
	 */
	@Override
	public boolean isElementPresent(ElementField ef) throws Exception {
		boolean elePresent = true;

		try {
			this._waitForElement(ef);
		} catch (Exception e) {
			return false;
		}

		return elePresent;
	}

	@Override
	public boolean isSelected(ElementField ef) throws Exception {
		this._waitForElement(ef);

		WebElement ele = this._findElement(ef);
		return ele.isSelected();
	}

	@Override
	public void maximize() throws Exception {
		this._getDefaultIEBrowser().manage().window().maximize();
	}

	@Override
	public void navigateToUrl(String url) throws Exception {
		this._getDefaultIEBrowser().get(url);
	}

	@Override
	public void openFileUsingOpenFileDialog(String fileName) {
		try {
			StringSelection stringSelection = new StringSelection(fileName);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, stringSelection);

			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.delay(5000);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (Exception e) {
			// eat exception
		}
	}

	@Override
	public void pressEnter(ElementField ef) throws Exception {
		this._waitForElement(ef);

		WebElement el = this._findElement(ef);
		el.sendKeys(Keys.ENTER);
	}

	@Override
	public void pressTab(ElementField ef) throws Exception {
		this._waitForElement(ef);

		WebElement el = this._findElement(ef);
		el.sendKeys(Keys.TAB);
	}

	@Override
	public void selectDropDown(ElementField ef, String targetValue) throws Exception {
		this._setExtendedTextToXPath("");
		this._waitForElement(ef);

		WebElement element = this._findElement(ef);
		Select se = new Select(element);
		if (targetValue == "USE_EXTENDED_TEXT_TO_XPATH") {
			targetValue = TextUtilities.replaceAllEscapeRegEx(this._getExtendedTextToXPath(), GlobalConsts.PRE_POST_EXTENDED_TEXTS_MARKER,
					"");
		}
		se.selectByVisibleText(targetValue);
	}

	@Override
	public boolean isValuePresentInDropDown(ElementField ef, String valueToVerify) throws Exception {
		this._waitForElement(ef);
		WebElement element = this._findElement(ef);

		Select se = new Select(element);
		List<WebElement> efValues = se.getOptions();
		for (int i = 0; i < efValues.size(); i++) {
			String optionText = efValues.get(i).getText();
			if (TextUtilities.contains(optionText.toLowerCase(), valueToVerify.toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void selectListItem(ElementField ef, String targetValue) throws Exception {
		SeleniumBrowser.logger.enterMethod();
		SeleniumBrowser.logger.info(targetValue + " to find");
		if (SeleniumBrowser._waitEligibilityDeclaredByPreviousElementField) {
			this._waitForJStoLoad();
		}

		WebElement we = this._getListItem(ef, targetValue);

		Actions builder = new Actions(this._getDefaultIEBrowser());
		builder.moveToElement(we);

		builder.click().build().perform();
		SeleniumBrowser.logger.exitMethod();
	}

	@Override
	public void selectTableRowItem(ElementField ef, String data) throws Exception {
		this._waitForElement(ef);
		WebElement table = this._findElement(ef);
		List<WebElement> cells = null;
		String text;
		// Now get all the TR elements from the table
		List<WebElement> allRows = table.findElements(By.xpath("/thead/tbody/tr"));
		// And iterate over them, getting the cells
		for (WebElement row : allRows) {
			cells = row.findElements(By.xpath("/td[1]"));
		}
		for (WebElement cell : cells) {
			text = cell.getText();
			SeleniumBrowser.logger.info("content >>:::::::::::::::   " + cell.getText());
			Assert.assertTrue(text.equals(data));
			WebElement webElem = this._getDefaultIEBrowser().findElement(By.xpath("//strong[contains(text(),'" + text + "')]"));
			((JavascriptExecutor) this._getDefaultIEBrowser()).executeScript("arguments[0].click();", webElem);
		}
	}

	@Override
	public void selectText(ElementField ef, String text) throws Exception {
		this._waitForElement(ef);

		Select sel = new Select(this._getDefaultIEBrowser().findElement(this._getByLocator(ef)));
		sel.deselectAll();
		sel.selectByVisibleText(text);
	}

	@Override
	public void setcurrentDate(ElementField el) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		String dates = dateFormat.format(date);
		this.setText(el, dates);
	}

	@Override
	public void setText(ElementField ef, String text) throws Exception {
		this._waitForElement(ef);

		this.setText(ef, text, false);
	}

	@Override
	public void setText(ElementField ef, String text, boolean trimText) throws Exception {
		this._waitForElement(ef);
		this.setText(ef, text, trimText, false);
	}

	@Override
	public void setText(ElementField ef, String text, boolean trimText, boolean allCaps) throws Exception {
		this._waitForElement(ef);
		this._setText(ef, text, trimText, allCaps);
	}

	@Override
	public void takeScreenShot(String methodName) {
		File scrFile = ((TakesScreenshot) this.getCurrentWebDriver()).getScreenshotAs(OutputType.FILE);
		// The below method will save the screen shot in drive with test method
		// name
		try {
			String fileNameWithPath = GlobalConsts.getScreenShotsFolderPath() + methodName + ".png";
			FileUtils.copyFile(scrFile, new File(fileNameWithPath));
			SeleniumBrowser.logger.info("***Placed screen shot in [" + fileNameWithPath + "] ***");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void uncheck(ElementField ef) throws Exception {
		this._waitForElement(ef);
		WebElement ele = this._findElement(ef);
		if (ele.isSelected()) {
			ele.click();
		}
	}

	/**
	 * Wait for element not present
	 * 
	 * @param locator
	 * @param timeout
	 * @throws Exception
	 */
	@Override
	public void waitForAjaxElementNotPresent(ElementField element, int timeoutInSeconds) throws Exception {
		SeleniumBrowser.logger.enterMethod();
		this._getWebDriverWait(timeoutInSeconds).until(ExpectedConditions.invisibilityOfElementLocated(this._getByLocator(element)));
		SeleniumBrowser.logger.exitMethod();
	}

	@Override
	public void waitForElement(ElementField element, int timeoutInMilliSeconds) throws Exception {
		SeleniumBrowser.logger.enterMethod();
		boolean elementFound = false;

		long maxTime = System.currentTimeMillis() + timeoutInMilliSeconds;
		WebElement we = null;
		while ((elementFound == false) && (System.currentTimeMillis() < maxTime)) {
			we = this._findElement(element);
			if (we != null) {
				elementFound = we.isDisplayed();
			}
		}

		this._waitForAjaxElementVisibilityAndEnabled(element, timeoutInMilliSeconds / 1000);

		if (SeleniumBrowser._waitEligibilityDeclaredByPreviousElementField) {
			SeleniumBrowser.logger
					.info("element wait eligibility found: " + SeleniumBrowser._waitEligibilityDeclaredByPreviousElementField);
			this._waitForJStoLoad();
		}

		SeleniumBrowser._waitEligibilityDeclaredByPreviousElementField = element.elementLocatorWaitEligible;
		SeleniumBrowser.logger.info("element wait eligibility set: " + SeleniumBrowser._waitEligibilityDeclaredByPreviousElementField);

		SeleniumBrowser.logger.exitMethod();
	}

	@Override
	public void validateSavedTestDataInTable(ElementField efTable, String text, boolean ignoreCase, TextComparators comparator)
			throws Exception {
		SeleniumBrowser.logger.enterMethod();
		SeleniumBrowser.logger.info("Finding text: " + text);
		List<WebElement> cells = this._getTableCells(efTable);
		for (WebElement cell : cells) {
			SeleniumBrowser.logger.info(cell.getText() + " -- " + text);
			if (TextUtilities.compareValue(cell.getText(), text, true, comparator)) {
				break;
			} else {
				WebElement cellHavingText = cell.findElement(By.xpath("//*[contains(text(), '" + text + "')]"));
				if (cellHavingText != null) {
					SeleniumBrowser.logger.info("eleme found");
					Assert.assertTrue(cellHavingText.isDisplayed());
				}
			}
		}

		SeleniumBrowser.logger.exitMethod();
	}

	@Override
	public void validateSavedTestDataInTable(ElementField efTable, String text, boolean ignoreCase) throws Exception {
		this.validateSavedTestDataInTable(efTable, text, ignoreCase, TextComparators.contains);
	}

	@Override
	public void validateSavedTestDataInTable(ElementField efTable, String text) throws Exception {
		this.validateSavedTestDataInTable(efTable, text, true, TextComparators.contains);
	}

	@Override
	public void validateSavedTestData(ElementField ef, String text, boolean ignoreCase, TextComparators comparator) throws Exception {
		SeleniumBrowser.logger.enterMethod();
		SeleniumBrowser.logger.info("Finding text: " + text);
		this._waitForElement(ef);
		WebElement ele = this._findElement(ef);
		SeleniumBrowser.logger.info(ele.getText() + " -- " + text);
		Assert.assertTrue(TextUtilities.compareValue(ele.getText(), text, true, comparator));
		SeleniumBrowser.logger.exitMethod();
	}

	@Override
	public void validateSavedTestData(ElementField efTable, String text, boolean ignoreCase) throws Exception {
		this.validateSavedTestData(efTable, text, ignoreCase, TextComparators.contains);
	}

	@Override
	public void validateSavedTestData(ElementField efTable, String text) throws Exception {
		this.validateSavedTestData(efTable, text, true, TextComparators.contains);
	}

	@Override
	public void verifyUIComponent(ElementField ef) throws Exception {
		this._waitForElement(ef);
		WebElement ele = this._findElement(ef);
		Assert.assertTrue(ele.isDisplayed());
	}

	// Wait for element not present
	@Override
	public void waitForWebElementPresent() {
		try {
			Thread.sleep(10000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void quit() {
		this._getDefaultIEBrowser().quit();
	}

	@Override
	public void acceptAlert() {

		final Alert alert = this._getDefaultIEBrowser().switchTo().alert();
		alert.accept();

		SeleniumBrowser.logger.debug("Accept the Alert Pop up");
	}

	@Override
	public void pressEnterKey(ElementField ef) throws Exception {

		WebElement ele = this._findElement(ef);
		ele.sendKeys(Keys.ENTER);
		SeleniumBrowser.logger.debug("Press Enter Key");
	}
}
