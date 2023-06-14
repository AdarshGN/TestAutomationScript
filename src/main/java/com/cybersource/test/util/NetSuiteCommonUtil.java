package com.cybersource.test.util;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.cybersource.test.CommonTest;

/**
 * NetSuiteCommonUtil class which have all related functionality used to help Netsuite related methods
 * @author AD20243779
 *
 */
public class NetSuiteCommonUtil extends CommonTest {
	CommonUtil commonUtil = new CommonUtil();
	static ArrayList<String> tabs = null;
	public static Writer fileWriter = null;
	public static BufferedWriter bufferedWriter = null;
	public static PrintWriter printWriter = null;
	static JavascriptExecutor executor = null;
	String userIndex = "1";
	final static Logger logger = Logger.getLogger(NetSuiteCommonUtil.class);

	/**
	 * This method is used to set value to a checkbox field
	 * @param driver Webdriver object which will be used for further operations
	 * @param checkBoxValue String containing status value of checkbox
	 * @param checkBoxXpath xpath of the Checkbox webElement
	 */
	public void setCheckBoxFieldValue(WebDriver driver, String checkBoxValue, String checkBoxXpath) {
		WebElement checkboxElement = commonUtil.findElementByXPath(driver, prop, checkBoxXpath);
		if (checkboxElement != null) {
			String chkClass = checkboxElement.getAttribute("class");
			if ((chkClass.contains(Constants.CHECKBOX_UNCK) && checkBoxValue.equalsIgnoreCase(Constants.YES))
					|| chkClass.contains(Constants.CHECKBOX_CK) && !checkBoxValue.equalsIgnoreCase(Constants.YES)) {
				checkboxElement.click();
			}
		}
	}

	/**
	 * This method is used to set value to a dropdown field
	 * @param driver Webdriver object which will be used for further operations
	 * @param xPath xpath of the dropdown webElement
	 * @param value String containing value which is to be selected from dropdown
	 */
	public void setDropDownFieldValue(WebDriver driver, String xPath, String value) {
		if (null != xPath) {
			WebElement element = commonUtil.findElementByXPath(driver, prop, xPath);
			if (null != element) {
				element.clear();
				element.sendKeys(value);
				if (!element.getAttribute("class").contains(prop.getProperty(Constants.DROPDOWN_CLASS_NAME))) {
					commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
					element.sendKeys(Keys.DOWN);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					element.sendKeys(Keys.ENTER);
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			}
		}
	}

	/**
	 * This method is used to setup Netsuite API
	 * @param netSuiteAPI String containing Netsuite API
	 * @param apiData String containing API data
	 */
	public static void setupNetSuiteApi(String netSuiteAPI, String apiData) {
		executor.executeScript(netSuiteAPI + Constants.SMALL_BRAKET_OPEN_KEYWORD + apiData
				+ Constants.SMALL_BRAKET_CLOSE_KEYWORD + Constants.SEMICOLON_KEYWORD);
	}

	/**
	 * This method is used setup Netsuite API using sublist name
	 * @param netSuiteAPI String containing Netsuite AP
	 * @param sublistName String containing sublist name to setup API
	 */
	public static void setupNetSuiteApiOneParam(String netSuiteAPI, String sublistName) {
		setupNetSuiteApi(netSuiteAPI, concatenateDoubleQuotes(sublistName));
	}

	/**
	 * This method is used setup Netsuite API using sublist name, field name and field id
	 * @param netSuiteAPI String containing Netsuite AP
	 * @param sublistName String containing sublist name to setup API
	 * @param fieldId String containing Field Id
	 * @param fieldValue String containing filed Value
	 */
	public static void setupNetSuiteApiThreeParam(String netSuiteAPI, String sublistName, String fieldId,
			String fieldValue) {
		setupNetSuiteApi(netSuiteAPI,
				concatenateDoubleQuotes(sublistName) + Constants.COMMA_KEYWORD + concatenateDoubleQuotes(fieldId)
						+ Constants.COMMA_KEYWORD + concatenateDoubleQuotes(fieldValue)
						+ Constants.NS_SET_CURRENT_LINE_SYNC);
	}

	/**
	 * This method is used to concatenate double quotes to a string
	 * @param actualString String containing actual text
	 * @return String after adding double quotes to actual text
	 */
	public static String concatenateDoubleQuotes(String actualString) {
		return Constants.DOUBLE_QUOTES_KEYWORD + actualString + Constants.DOUBLE_QUOTES_KEYWORD;
	}

	/**
	 * This method is used to update field values of Invoice
	 * @param driver Webdriver object which will be used for further operations
	 * @param CustomerName String containing name of the customer placing order
	 * @param Location String containing location of the existing order
	 * @param Memo String containing sales order memo
	 * @param ItemName String containing name ordered items
	 * @param Quantity String containing quantity of items ordered
	 * @param PriceLevel String containing price level of the order
	 * @param Rate String containing order rate
	 * @param ItemTaxCode String containing tax code of the item ordered
	 * @param DueDate String containing due date of the order
	 * @return Webdriver object which will be used for further operations
	 */
	public WebDriver updateInvoiceFieldsValue(WebDriver driver, String CustomerName, String Location, String Memo,
			String ItemName, String Quantity, String PriceLevel, String Rate, String ItemTaxCode, String DueDate) {
		logger.info("[updateInvoiceFieldsValue] Starts");
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER);
		commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.CONTROL + "a",
				Keys.BACK_SPACE);
		commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);

		if (Constants.NO.equalsIgnoreCase(prop.getProperty(Constants.CUSTOMER_ID_PRESENT))) {
			commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.DOWN, Keys.ENTER);
		}
		// wait until customer is set
		for (int i = 0; i < browserMaxTimeoutSeconds; i++) {
			if (commonUtil.getElementTextByXPath(driver, prop, Constants.SALES_ORDER_SUMMARY_SHIPPING_COST) != null) {
				break;
			} else {
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			}
		}
		commonUtil.setTextBoxFieldValue(driver, prop, Constants.INVOICING_CONFIGURATION_NEW_FIELD_DUEDATE, DueDate);
		commonUtil.clearElementByXPath(driver, prop, Constants.SALES_ORDER_MEMO);
		commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Memo);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);

		String splitSpecialCharacter = Constants.SEMICOLON_KEYWORD;
		String[] ItemNames = ItemName.split(splitSpecialCharacter);
		String[] Quantities = Quantity.split(splitSpecialCharacter);
		String[] PriceLevels = PriceLevel.split(splitSpecialCharacter);
		String[] Rates = Rate.split(splitSpecialCharacter);
		String[] ItemTaxCodes = ItemTaxCode.split(splitSpecialCharacter);

		if (ItemNames.length > 0) {
			commonUtil.clickElementByXPath(driver, prop, Constants.INVOICING_ITEM_CLEAR_ALL_LINES);
			logger.info("[updateInvoiceFieldsValue] items length: " + ItemNames.length);
			for (int i = 0; i < ItemNames.length; i++) {
				executor.executeScript(prop.getProperty(Constants.NLAPI_SELECT_NEW_LINE_ITEM));
				commonUtil.setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_PREFIX, ItemNames[i],
						Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				if (commonUtil.isAlertPresent(driver)) {
					commonUtil.acceptAlert(driver);
				}
				commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_QUANTITY_PREFIX,
						Quantities[i], Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				commonUtil.setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_PRICE_PREFIX,
						PriceLevels[i], Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_RATE_PREFIX, Rates[i],
						Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				commonUtil.setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_TAX_CODE_PREFIX,
						ItemTaxCodes[i], Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				executor.executeScript(prop.getProperty(Constants.NLAPI_COMMIT_LINE_ITEM));
			}
		}
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_MEMO);
		commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_LOCATION, Location);
		if (commonUtil.isAlertPresent(driver)) {
			commonUtil.acceptAlert(driver);
		}
		return driver;
	}

	/**
	 * This method is used to update the invoice record 
	 * @param driver Webdriver object which will be used for further operations
	 * @param CustomerName String containing name of the customer placing order
	 * @param CustomerLocation String containing location of the customer placing order
	 * @param Memo String containing sales order memo
	 * @param ItemName String containing name ordered items
	 * @param Quantity String containing quantity of items ordered
	 * @param PriceLevel String containing price level of the order
	 * @param Rate String containing order rate
	 * @param ItemTaxCode String containing tax code of the item ordered
	 * @param InvoiceAction String containing action to be taken on invoice
	 * @param DueDate String containing due date of the order
	 * @param TestScenario String containing test scenario of the test
	 * @param screenShotPath String containing path directory of the screenshot file
	 * @param screenShotModulePath string containing path to screenshot module folder
	 * @throws Exception Used to handle exceptions that may happen
	 */
	public void updateInvoiceRecord(WebDriver driver, String CustomerName, String CustomerLocation, String Memo,
			String ItemName, String Quantity, String PriceLevel, String Rate, String ItemTaxCode, String InvoiceAction,
			String DueDate, String TestScenario, String screenShotPath, String screenShotModulePath) throws Exception {
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.INVOICE);
		executor = (JavascriptExecutor) driver;
		driver = updateInvoiceFieldsValue(driver, CustomerName, CustomerLocation, Memo, ItemName, Quantity, PriceLevel,
				Rate, ItemTaxCode, DueDate);
		findCustomTabAndClick(driver, Constants.INVOICE_CYBERSOURCE_TAB_NAME);
		setDropDownFieldValue(driver, Constants.INVOICE_INVOICEACTION, InvoiceAction);
		SaveInvoiceAndGenerateReport(driver, screenShotPath, Constants.INVOICE_UPDATE);
	}

	/**
	 * This method is used to save the invoice and generate the report
	 * @param driver Webdriver object which will be used for further operations
	 * @param screenShotPath String containing path directory of the screenshot file
	 * @param screenShotModulePath string containing path to screenshot module folder
	 * @throws Exception Exception Used to handle exceptions that may happen
	 */
	private void SaveInvoiceAndGenerateReport(WebDriver driver, String screenShotPath, String screenShotType)
			throws Exception {
		commonUtil.clickElementByXPath(driver, prop, Constants.INVOICING_NEW_INVOICE_SAVE_BUTTON);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		if (commonUtil.isAlertPresent(driver)) {
			commonUtil.acceptAlert(driver);
		}
		String tranid = getNetSuiteFieldValue(driver, Constants.NS_INVOICE_FIELD_ID_TRANID);
		commonUtil.addLogToTestNGReportInvoicing(Constants.NS_INVOICE_REPORT_TITLE_ORDER_ID, tranid);
		commonUtil.addLogToTestNGReportInvoicing(Constants.NS_INVOICE_REPORT_TITLE_INVOICING_ID,
				getNetSuiteFieldValue(driver, Constants.NS_INVOICE_FIELD_ID_CUSTBODY_CS_PYMT_INV_INVOICEID));
		commonUtil.addLogToTestNGReportInvoicing(Constants.NS_INVOICE_REPORT_TITLE_STATUS,
				getNetSuiteFieldValue(driver, Constants.NS_INVOICE_FIELD_ID_CUSTBODY_CS_PYMT_INV_INVOICESTATUS));
		commonUtil.addLogToTestNGReportInvoicing(Constants.NS_INVOICE_REPORT_TITLE_ERROR,
				getNetSuiteFieldValue(driver, Constants.NS_INVOICE_FIELD_ID_CUSTBODY_CS_PYMT_INV_ERRORMESSAGE));

		if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.SCREEN_SHOT_REQUIRED))) {
			commonUtil.takeScreenShot(driver, screenShotPath, screenShotType + "_" + tranid);
			executor.executeScript(prop.getProperty(Constants.WINDOW_SCROLLHEIGHT_BOTTOM));
			commonUtil.takeScreenShot(driver, screenShotPath,
					screenShotType + "_" + tranid + Constants.INVOICE_ITEM_TAB);
			findCustomTabAndClick(driver, Constants.INVOICE_CYBERSOURCE_TAB_NAME);
			executor.executeScript(prop.getProperty(Constants.WINDOW_SCROLLHEIGHT_BOTTOM));
			commonUtil.takeScreenShot(driver, screenShotPath,
					screenShotType + "_" + tranid + Constants.INVOICE_CYBERSOURCE_TAB);
		}
	}

	/**
	 * This method is used to search and find a record in a list before editing it
	 * @param driver Webdriver object which will be used for further operations
	 * @param Name String containing title of column to be searched for
	 * @param tdIndex Integer containing index of column used to find record
	 * @return Boolean containing status of the operation of the method
	 */
	private boolean findRecordInListAndEdit(WebDriver driver, String Name, int tdIndex) {
		boolean flag = false;
		WebElement o_invoicing_list = driver
				.findElement(By.id(prop.getProperty(Constants.PAYMENT_PROCESSING_TABLE_ID)));
		if (commonUtil.isElementNotNull(o_invoicing_list)) {
			WebElement table = o_invoicing_list.findElement(By.xpath(Constants.CSS_PARENT));
			List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
			List<WebElement> cells = null;
			for (WebElement row : allRows) {
				cells = row.findElements(By.tagName(Constants.TD));
				if (cells.size() > 1) {
					if (cells.get(tdIndex).getText().equals(Name)) {
						logger.info("[findRecordInListAndEdit] Clicking on Edit Profile: " + Name);
						cells.get(0).findElement(By.linkText(Constants.EDIT)).click();
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}

	/**
	 * This method is used to find a tab using name and click it
	 * @param driver Webdriver object which will be used for further operations
	 * @param tabName String containing name of Tab to be clicked
	 */
	private static void findCustomTabAndClick(WebDriver driver, String tabName) {
		List<WebElement> cells = driver.findElements(By.className(Constants.INVOICE_FORM_TAB_TEXT));
		if (cells != null && cells.size() > 1) {
			for (WebElement row : cells) {
				if (row.getText().equalsIgnoreCase(tabName)) {
					row.click();
					logger.info("[findCustomTabAndClick] Clicked: " + row.getText());
					break;
				}
			}
		}
	}

	/**
	 * This method is used to get field value of Netsuite
	 * @param driver Webdriver object which will be used for further operations
	 * @param netsuiteFieldId String containing field id 
	 * @return String containing values of the netsuite field
	 */
	public String getNetSuiteFieldValue(WebDriver driver, String netsuiteFieldId) {
		String fieldValue = "";
		WebElement nsFieldElement = driver
				.findElement(By.cssSelector(createCssSelectorForNetSuiteField(netsuiteFieldId)));
		if (commonUtil.isElementNotNull(nsFieldElement)) {
			fieldValue = ((String) ((JavascriptExecutor) driver).executeScript("return arguments[0].textContent;",
					nsFieldElement)).replaceAll("\\s", "");
		}
		return fieldValue;
	}

	/**
	 * This method is used to create css selector for Netsuite fields
	 * @param netsuiteFieldId String containing field id 
	 * @return String after creating css selector with field id
	 */
	public static String createCssSelectorForNetSuiteField(String netsuiteFieldId) {
		return Constants.CSS_SEARCH_BY_ID_KEYWORD + netsuiteFieldId + Constants.NS_FIELD_ID_SUFFIX;
	}

	/**
	 * This method is used to configure invoice setup and to create an invoice
	 * @param driver Webdriver object which will be used for further operations
	 * @param userIndex Index number of user to specify the user of the instance
	 * @param CustomerName String containing name of the customer placing order
	 * @param CustomerLocation String containing location of the customer placing order
	 * @param Memo String containing sales order memo
	 * @param ItemName String containing name ordered items
	 * @param Quantity String containing quantity of items ordered
	 * @param PriceLevel String containing price level of the order
	 * @param Rate String containing order rate
	 * @param ItemTaxCode String containing tax code of the item ordered
	 * @param MidAccount String containing Merchant ID account
	 * @param CreateInvoice String to set create invoice value from dropdown
	 * @param InvoiceId String containing invoice Id
	 * @param PartialAllowedAmount String containing partial allowed amount
	 * @param DueDate String containing due date of the order
	 * @param UpdateConfiguration String to configure updation status
	 * @param Name String containing invoicing configuration field name
	 * @param TestMode String configuring test mode setup
	 * @param MerchantID String containing invoicing configuration merchant id
	 * @param Key String containing invoicing configuration field key
	 * @param SecretKey String containing invoicing configuration secret key
	 * @param ImportEBCInvoices String containing invoicing configuration EBC import invoices
	 * @param OnDemandScheduler String containing invoicing configuration ondemand scheduler
	 * @param Subsidiary String containing invoicing configuration subsidary
	 * @param Location String containing invoicing configuration location
	 * @param DefaultCustomer String containing invoicing configuration default customer
	 * @param Item String containing invoicing configuration item
	 * @param ShippingItem String containing invoicing configuration shipping item
	 * @param TaxItem String containing invoicing configuration tax item
	 * @param DiscountItem String containing invoicing configuration discount item
	 * @param TaxCode String containing invoicing configuration tax code
	 * @param DepositAccount String containing invoicing configuration deposit account
	 * @param TestScenario String containing order test scenario
	 * @param screenShotPath String containing path directory of the screenshot file
	 * @param screenShotModulePath string containing path to screenshot module folder
	 * @return Webdriver object which will be used for further operations
	 * @throws Exception Used to handle exceptions that may happen
	 */
	public WebDriver SetupInvoicingConfigurationAndCreateInvoice(WebDriver driver, String userIndex,
			String CustomerName, String CustomerLocation, String Memo, String ItemName, String Quantity,
			String PriceLevel, String Rate, String ItemTaxCode, String MidAccount, String CreateInvoice,
			String InvoiceId, String PartialAllowedAmount, String DueDate, String UpdateConfiguration, String Name,
			String TestMode, String MerchantID, String Key, String SecretKey, String ImportEBCInvoices,
			String OnDemandScheduler, String Subsidiary, String Location, String DefaultCustomer, String Item,
			String ShippingItem, String TaxItem, String DiscountItem, String TaxCode, String DepositAccount,
			String TestScenario, String screenShotPath, String screenShotModulePath) throws Exception {
		logger.info("[SetupInvoicingConfigurationAndCreateInvoice] UpdateConfiguration" + UpdateConfiguration);
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.INVOICE);
		if (commonUtil.isValueNotNull(UpdateConfiguration) && UpdateConfiguration.toUpperCase().equals(Constants.YES)) {
			logger.info("[SetupInvoicingConfigurationAndCreateInvoice] Invoicing Profile setup is being start");
			driver = commonUtil.setupDriver(driver, prop, Constants.INVOICING_CONFIGURATION_LIST_URL, userIndex);
			executor = (JavascriptExecutor) driver;
			if (!findRecordInListAndEdit(driver, Name, Constants.NS_INVOICING_LIST_NAME_INDEX)) {
				logger.info("[SetupInvoicingConfigurationAndCreateInvoice] New Invoicing Profile being setup");
				driver = commonUtil.setupDriver(driver, prop, Constants.INVOICING_CONFIGURATION_CREATE_URL, userIndex);
			}
			commonUtil.setTextBoxFieldValue(driver, prop, Constants.INVOICING_CONFIGURATION_NEW_FIELD_NAME, Name);
			commonUtil.setTextBoxFieldValue(driver, prop, Constants.INVOICING_CONFIGURATION_NEW_FIELD_MERCHANTID,
					MerchantID);
			commonUtil.setTextBoxFieldValue(driver, prop, Constants.INVOICING_CONFIGURATION_NEW_FIELD_SECRETKEY,
					SecretKey);
			commonUtil.setTextBoxFieldValue(driver, prop, Constants.INVOICING_CONFIGURATION_NEW_FIELD_KEY, Key);

			setCheckBoxFieldValue(driver, TestMode, Constants.INVOICING_CONFIGURATION_TESTMODE);
			setCheckBoxFieldValue(driver, ImportEBCInvoices, Constants.INVOICING_CONFIGURATION_EBCIMPORT);
			setCheckBoxFieldValue(driver, OnDemandScheduler, Constants.INVOICING_CONFIGURATION_DEMANDSCHEDULER);
			setDropDownFieldValue(driver, Constants.INVOICING_CONFIGURATION_SUBSIDIARY, Subsidiary);
			setDropDownFieldValue(driver, Constants.INVOICING_CONFIGURATION_LOCATION, Location);
			setDropDownFieldValue(driver, Constants.INVOICING_CONFIGURATION_CUSTOMER, DefaultCustomer);
			setDropDownFieldValue(driver, Constants.INVOICING_CONFIGURATION_ITEM, Item);
			setDropDownFieldValue(driver, Constants.INVOICING_CONFIGURATION_SHIPPINGITEM, ShippingItem);
			setDropDownFieldValue(driver, Constants.INVOICING_CONFIGURATION_TAXITEM, TaxItem);
			setDropDownFieldValue(driver, Constants.INVOICING_CONFIGURATION_DISCOUNTITEM, DiscountItem);
			setDropDownFieldValue(driver, Constants.INVOICING_CONFIGURATION_TAXCODE, TaxCode);
			setDropDownFieldValue(driver, Constants.INVOICING_CONFIGURATION_DEPOSITACCOUNT, DepositAccount);

			commonUtil.clickElementByXPath(driver, prop, Constants.INVOICING_CONFIGURATION_SAVE_BUTTON);
			logger.info("[SetInvoicingProfile] InvoicingProfile is configured");
		}
		logger.info("[SetInvoicingProfile] Invoice is getting created");
		driver = commonUtil.setupDriver(driver, prop, Constants.INVOICING_NEW_INVOICE_URL, userIndex);
		executor = (JavascriptExecutor) driver;
		updateInvoiceFieldsValue(driver, CustomerName, CustomerLocation, Memo, ItemName, Quantity, PriceLevel, Rate,
				ItemTaxCode, DueDate);
		findCustomTabAndClick(driver, Constants.INVOICE_CYBERSOURCE_TAB_NAME);
		setDropDownFieldValue(driver, Constants.INVOICE_MIDACCOUNT, MidAccount);
		setDropDownFieldValue(driver, Constants.INVOICE_CREATEINVOICE, CreateInvoice);
		commonUtil.setTextBoxFieldValue(driver, prop, Constants.INVOICE_INVOICEID, InvoiceId);
		commonUtil.setTextBoxFieldValue(driver, prop, Constants.INVOICE_PARTIALAMOUNT, PartialAllowedAmount);
		SaveInvoiceAndGenerateReport(driver, screenShotPath, Constants.NEW_INVOICE);
		return driver;
	}

	/**
	 * This method is used to find an invoice and to update it
	 * @param driver Webdriver object which will be used for further operations
	 * @param userIndex Index number of user to specify the user of the instance
	 * @param InvoiceNumber String containing invoice number of the order
	 * @param CustomerName String containing name of the customer placing order
	 * @param CustomerLocation String containing location of the customer placing order
	 * @param Memo String containing sales order memo
	 * @param ItemName String containing name ordered items
	 * @param Quantity String containing quantity of items ordered
	 * @param PriceLevel String containing price level of the order
	 * @param Rate String containing order rate
	 * @param ItemTaxCode String containing tax code of the item ordered
	 * @param InvoiceAction String containing detail of invoice action to be done
	 * @param DueDate String containing due date of the order
	 * @param TestScenario String containing order test scenario
	 * @param screenShotPath String containing path directory of the screenshot file
	 * @param screenShotModulePath string containing path to screenshot module folder
	 * @throws Exception Used to handle exceptions that may happen
	 */
	public void findInvoiceAndUpdate(WebDriver driver, String userIndex, String InvoiceNumber, String CustomerName,
			String CustomerLocation, String Memo, String ItemName, String Quantity, String PriceLevel, String Rate,
			String ItemTaxCode, String InvoiceAction, String DueDate, String TestScenario, String screenShotPath,
			String screenShotModulePath) throws Exception {
		driver = commonUtil.configureDriver(driver, prop, userIndex);
		logger.info("[findInvoiceAndUpdate] Start work on Invoice :" + InvoiceNumber);
		if (commonUtil.isValueNotNull(InvoiceNumber)) {
			WebElement globelObj = commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR);
			globelObj.clear();
			try {
				globelObj.sendKeys(InvoiceNumber);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				WebElement element = commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR_EDIT);// driver.findElement(By.xpath(Constants.SEARCH_BAR_VIEW));
				Actions actions = new Actions(driver);
				actions.moveToElement(element).build().perform();
				commonUtil.clickElementByXPath(driver, prop, Constants.SEARCH_BAR_EDIT);
				commonUtil.ifAlertPresentAccept(driver);
			} catch (Exception ex) {
				globelObj.sendKeys(InvoiceNumber, Keys.ENTER);
				commonUtil.clickElementByXPath(driver, prop, Constants.SEARCH_BAR_EDIT);
			}
			logger.info("[findInvoiceAndUpdate] Invoice page open :" + InvoiceNumber);
			updateInvoiceRecord(driver, CustomerName, CustomerLocation, Memo, ItemName, Quantity, PriceLevel, Rate,
					ItemTaxCode, InvoiceAction, DueDate, TestScenario, screenShotPath, screenShotModulePath);
		}
		// return driver;
	}

	/**
	 * This method is used to create an invoice and to update it
	 * @param driver Webdriver object which will be used for further operations
	 * @param userIndex Index number of user to specify the user of the instance
	 * @param CustomerName String containing name of the customer placing order
	 * @param CustomerLocation String containing location of the customer placing order
	 * @param Memo String containing sales order memo
	 * @param ItemName String containing name ordered items
	 * @param Quantity String containing quantity of items ordered
	 * @param PriceLevel String containing price level of the order
	 * @param Rate String containing order rate
	 * @param ItemTaxCode String containing tax code of the item ordered
	 * @param MidAccount String containing Merchant ID account
	 * @param CreateInvoice String to set create invoice value from dropdown
	 * @param InvoiceId String containing invoice Id
	 * @param PartialAllowedAmount String containing partial allowed amount
	 * @param DueDate String containing due date of the order
	 * @param UpdateConfiguration String to configure updation status
	 * @param Name String containing invoicing configuration field name
	 * @param TestMode String configuring test mode setup
	 * @param MerchantID String containing invoicing configuration merchant id
	 * @param Key String containing invoicing configuration field key
	 * @param SecretKey String containing invoicing configuration secret key
	 * @param ImportEBCInvoices String containing invoicing configuration EBC import invoices
	 * @param OnDemandScheduler String containing invoicing configuration ondemand scheduler
	 * @param Subsidiary String containing invoicing configuration subsidary
	 * @param Location String containing invoicing configuration location
	 * @param DefaultCustomer String containing invoicing configuration default customer
	 * @param Item String containing invoicing configuration item
	 * @param ShippingItem String containing invoicing configuration shipping item
	 * @param TaxItem String containing invoicing configuration tax item
	 * @param DiscountItem String containing invoicing configuration discount item
	 * @param TaxCode String containing invoicing configuration tax code
	 * @param DepositAccount String containing invoicing configuration deposit account
	 * @param UpdateCustomerName String containing customer name to be updated with
	 * @param UpdateCustomerLocation String containing customer location to be updated with
	 * @param UpdateMemo String containing memo to be updated with
	 * @param UpdateItemName String containing item name to be updated with
	 * @param UpdateQuantity String containing quantity to be updated with
	 * @param UpdatePriceLevel String containing price level to be updated with
	 * @param UpdateRate String containing rate to be updated with
	 * @param UpdateItemTaxCode String containing item tax code to be updated with
	 * @param UpdateInvoiceAction String containing invoice action to be updated with
	 * @param UpdateDueDate String containing due date to be updated with
	 * @param TestScenario String containing order test scenario
	 * @param screenShotPath String containing path directory of the screenshot file
	 * @param screenShotModulePath string containing path to screenshot module folder
	 * @throws Exception Used to handle exceptions that may happen
	 */
	public void InvoicingCreateAndUpdate(WebDriver driver, String userIndex, String CustomerName,
			String CustomerLocation, String Memo, String ItemName, String Quantity, String PriceLevel, String Rate,
			String ItemTaxCode, String MidAccount, String CreateInvoice, String InvoiceId, String PartialAllowedAmount,
			String DueDate, String UpdateConfiguration, String Name, String TestMode, String MerchantID, String Key,
			String SecretKey, String ImportEBCInvoices, String OnDemandScheduler, String Subsidiary, String Location,
			String DefaultCustomer, String Item, String ShippingItem, String TaxItem, String DiscountItem,
			String TaxCode, String DepositAccount, String UpdateCustomerName, String UpdateCustomerLocation,
			String UpdateMemo, String UpdateItemName, String UpdateQuantity, String UpdatePriceLevel, String UpdateRate,
			String UpdateItemTaxCode, String UpdateInvoiceAction, String UpdateDueDate, String TestScenario,
			String screenShotPath, String screenShotModulePath) throws Exception {
		logger.info("[InvoicingCreateAndUpdate] Starts");
		driver = SetupInvoicingConfigurationAndCreateInvoice(driver, userIndex, CustomerName, CustomerLocation, Memo,
				ItemName, Quantity, PriceLevel, Rate, ItemTaxCode, MidAccount, CreateInvoice, InvoiceId,
				PartialAllowedAmount, DueDate, UpdateConfiguration, Name, TestMode, MerchantID, Key, SecretKey,
				ImportEBCInvoices, OnDemandScheduler, Subsidiary, Location, DefaultCustomer, Item, ShippingItem,
				TaxItem, DiscountItem, TaxCode, DepositAccount, TestScenario, screenShotPath, screenShotModulePath);
		commonUtil.scrollToBottom(driver);
		commonUtil.clickElementByXPath(driver, prop, Constants.INVOICING_EDIT_BUTTON);
		updateInvoiceRecord(driver, UpdateCustomerName, UpdateCustomerLocation, UpdateMemo, UpdateItemName,
				UpdateQuantity, UpdatePriceLevel, UpdateRate, UpdateItemTaxCode, UpdateInvoiceAction, UpdateDueDate,
				TestScenario, screenShotPath, screenShotModulePath);
		logger.info("[InvoicingCreateAndUpdate] Ends");
	}
}