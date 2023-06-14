package com.cybersource.test.netsuite;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.cybersource.test.CommonTest;
import com.cybersource.test.util.CommonUtil;
import com.cybersource.test.util.Constants;
import com.cybersource.test.util.FileUtil;
import com.cybersource.test.util.RetryFailedTestCases;

/**
 * VisaCheckout class which will have all functionality used to place order and do related services using visa checkout payment method
 * @author AD20243779
 *
 */
public class VisaCheckout extends CommonTest {
	CommonUtil commonUtil = new CommonUtil();
	boolean isDirectCaptureService = false;
	boolean isDirectRefundService = false;
	boolean isDirectVoid = false;
	String pnReferenceNumber = null;
	String screenShotModulePath = null;
	String screenShotPath = null;
	String userIndex = "1";
	String message = null;
	WebDriver driver = null;
	JavascriptExecutor executor = null;
	final static Logger logger = Logger.getLogger(VisaCheckout.class);

	@BeforeTest(alwaysRun = true)
	/**
	 * This method is used to initialize test environment before running a test
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @throws IOException Handles when an I/O exception of some sort has occurred
	 * @throws InterruptedException Handles when a thread is waiting, sleeping, or otherwise occupied,and the thread is interrupted, either before or during the activity
	 * @throws AWTException Handles any Abstract Window Toolkit exception that can occur
	 */
	public void init(ITestContext context) throws IOException, InterruptedException, AWTException {
		super.loadCommonConfig(context);
		screenShotModulePath = commonUtil.getScreenshotsModulePath(Constants.VISA_CHECK_OUT);
		// executeCsvImport();
	}

	@DataProvider
	/**
	 * This method is used to get data from excel sheets to execute test cases
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @return Object which contains data from sheet
	 */
	public Object[][] getData(ITestContext context) {
		String prefix = context.getCurrentXmlTest().getParameter(Constants.PREFIX);
		String currentScenario = context.getCurrentXmlTest().getParameter(Constants.TEST_SCENARIO);
		Object[][] data = FileUtil.getSheetData(XLSX_FILE_PATH,
				prop.getProperty(prefix + currentScenario + Constants._SHEET_NAME));
		return data;
	}

	/**
	 * This method is used to import csv file to test
	 * @throws InterruptedException Handles when a thread is waiting, sleeping, or otherwise occupied,and the thread is interrupted, either before or during the activity
	 * @throws AWTException Handles any Abstract Window Toolkit exception that can occur
	 */
	public void executeCsvImport() throws InterruptedException, AWTException {
		logger.info("[VisaCheckOut][executeCsvImport] starts");
		driver = commonUtil.setupDriver(driver, prop, Constants.CSV_IMPORT_URL, userIndex);
		Reporter.log("URL Loaded");
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.IMPORT_SELECTMAP);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		// WebElement element = commonUtil.findElementByXPath(driver,
		// prop,prop.getProperty(Constants.IMPORT_SELECTBUTTON));
		WebElement element = driver.findElement(By.xpath(prop.getProperty(Constants.IMPORT_SELECTBUTTON)));
		((JavascriptExecutor) driver).executeScript(Constants.ARGUMENTS_SCROLL_INTO_VIEW, element);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		Actions builder = new Actions(driver);
		builder.moveToElement(element).click().build().perform();
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		uploadFile(prop.getProperty(Constants.IMPORT_VISA_CHECKOUT_CSV_FILE_PATH));
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.IMPORT_FILEUPLOAD_NEXT);
		commonUtil.clickElementByXPath(driver, prop, Constants.IMPORT_ADD_OR_UPDATE_RADIO);
		commonUtil.clickElementByXPath(driver, prop, Constants.IMPORT_IMPORTOPTIONS_NEXT);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		commonUtil.clickElementByXPath(driver, prop, Constants.IMPORT_FIELD_MAP_NEXT);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clearElementByXPath(driver, prop, Constants.IMPORT_SAVE_AND_RUN_ID);
		commonUtil.clickElementByXPath(driver, prop, Constants.IMPORT_SAVE_AND_RUN);
		if (commonUtil.isAlertPresent(driver)) {
			commonUtil.acceptAlert(driver);
		}
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementByXPath(driver, prop, Constants.IMPORT_JOB_STATUS);
		do {
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
			driver.navigate().refresh();
			message = commonUtil.getElementTextByXPath(driver, prop, Constants.IMPORT_JOB_STATUS_FIRST_ROW);
		} while (null == message || message.equalsIgnoreCase(" "));
		logger.info("Import Status: " + message);
		Assert.assertEquals(message.charAt(0), message.charAt(5));
		logger.info("[VisaCheckOut][executeCsvImport] ends");
	}

	@Test(dataProvider = Constants.GET_DATA)
	/**
	 * This method is used to execute refund service after completing a capture service
	 * @param ExternalID String containing external id used to create an order
	 * @param Customer String containing name of customer placing order
	 * @param Location String containing location of order in which services are to be executed
	 * @param RequireCSVImport String to validate if csv file is to be imported
	 * @param RequireEBCCheck String to check if validation from ebc portal is required or not
	 * @param ReasonCode string containing reason code for the transaction
	 * @param DecisionMsg String containing decision manager message of the transaction
	 * @param TestScenario String to specify the scenario of the transaction happening
	 * @throws Exception Handles any exception that may happen
	 */
	public void executeCaptureRefund(String ExternalID, String Customer, String Location, String RequireCSVImport,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[VisaCheckOut][executeCaptureRefund] Starts");
		executeCapture(ExternalID, Customer, Location, RequireCSVImport, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		commonUtil.executeRefund(driver, Location, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario,
				screenShotPath, screenShotModulePath);
		logger.info("[VisaCheckOut][executeCaptureRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA)
	/**
	 * This method is used to execute a capture service
	 * @param ExternalID String containing external id used to create an order
	 * @param Customer String containing name of customer placing order
	 * @param Location String containing location of order in which services are to be executed
	 * @param RequireCSVImport String to validate if csv file is to be imported
	 * @param RequireEBCCheck String to check if validation from ebc portal is required or not
	 * @param ReasonCode string containing reason code for the transaction
	 * @param DecisionMsg String containing decision manager message of the transaction
	 * @param TestScenario String to specify the scenario of the transaction happening
	 * @throws Exception Handles any exception that may happen
	 */
	public void executeCapture(String ExternalID, String Customer, String Location, String RequireCSVImport,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[VisaCheckOut][executeCapture] starts");
		// ValidateImports(Customer);
		String captureId = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.CAPTURE);
		if (isDirectCaptureService) {
			// driver = commonUtil.setupDriverDirectUrl(prop, AuthorderIdForDirectCapture);
		}
		if (RequireCSVImport.equalsIgnoreCase(Constants.YES)) {
			executeCsvImport();
		}
		driver = commonUtil.setupDriver(driver, prop, Constants.EXTERNAL_ORDERID_SEARCH_URL, userIndex);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementByXPath(driver, prop, Constants.SEARCH_TAB);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNAL_ID_OPTION);
		driver.switchTo().frame(2);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_SEARCH_TEXT);
		commonUtil.setElementValueByXPath(driver, prop, Constants.EXTERNALID_SEARCH_TEXT, ExternalID);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		commonUtil.findElementByXPath(driver, prop, Constants.EXTERNALID_SEARCH_TEXT).sendKeys(Keys.ENTER);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_SET);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_SUBMIT);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_VIEW);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_APPROVE);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_FULFILL);
		commonUtil.setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, 1,
				Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		// Sales Order- Mark Packed button
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_PACKED);
		// Sales Order- Mark Shipped button
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_PACKED);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_BILL);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		// Checking payment status
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		captureId = commonUtil.getOrderId(driver, Constants.CAPTURE_AUTHORIZATION);
		commonUtil.renameFolder(screenShotPath, Constants.CAPTURE, captureId);
		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.CAPTURE_AUTHORIZATION,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.CAPTURE, commonUtil.getCurrentUrl(driver), captureId);
		commonUtil.addLogToTestNGReport(Constants.CAPTURE, pnReferenceNumber);
		logger.info("[VisaCheckOut][executeCapture] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to void a transaction
	 * @param ExternalID String containing external id used to create an order
	 * @param Customer String containing name of customer placing order
	 * @param Location String containing location of order in which services are to be executed
	 * @param RequireCSVImport String to validate if csv file is to be imported
	 * @param RequireEBCCheck String to check if validation from ebc portal is required or not
	 * @param ReasonCode string containing reason code for the transaction
	 * @param DecisionMsg String containing decision manager message of the transaction
	 * @param TestScenario String to specify the scenario of the transaction happening
	 * @throws Exception Handles any exception that may happen
	 */
	public void executeVoid(String ExternalID, String Customer, String Location, String RequireCSVImport,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[VisaCheckOut][executeVoid] starts");
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.VOID);
		String voidId = null;
		if (RequireCSVImport.equalsIgnoreCase(Constants.YES)) {
			executeCsvImport();
		}
		driver = commonUtil.setupDriver(driver, prop, Constants.EXTERNAL_ORDERID_SEARCH_URL, userIndex);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementByXPath(driver, prop, Constants.SEARCH_TAB);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNAL_ID_OPTION);
		driver.switchTo().frame(2);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_SEARCH_TEXT);
		commonUtil.setElementValueByXPath(driver, prop, Constants.EXTERNALID_SEARCH_TEXT, ExternalID);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		commonUtil.findElementByXPath(driver, prop, Constants.EXTERNALID_SEARCH_TEXT).sendKeys(Keys.ENTER);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_SET);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_SUBMIT);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_VIEW);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);

		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_CANCEL_ORDER);
		commonUtil.switchToFrame(driver, Constants.THREE);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_CANCEL_YES_BUTTON);
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		voidId = commonUtil.getOrderId(driver, Constants.VOID_AUTHORIZATION);
		commonUtil.renameFolder(screenShotPath, Constants.VOID, voidId);
		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.VOID_AUTHORIZATION,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		// pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
		// prop.getProperty(Constants.PI_ENABLED_PN_REFERENCE_NUMBER));
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.VOID, commonUtil.getCurrentUrl(driver), voidId);
		commonUtil.addLogToTestNGReport(Constants.VOID, pnReferenceNumber);
		logger.info("[VisaCheckOut][executeVoid] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to void a transaction
	 * @param ExternalID String containing external id used to create an order
	 * @param Customer String containing name of customer placing order
	 * @param Location String containing location of order in which services are to be executed
	 * @param Items String containing details of item to be placed
	 * @param HasMultipleItem String used to define if multiple items are placed or not
	 * @param RequireCSVImport String to validate if csv file is to be imported
	 * @param RequireEBCCheck String to check if validation from ebc portal is required or not
	 * @param ReasonCode string containing reason code for the transaction
	 * @param DecisionMsg String containing decision manager message of the transaction
	 * @param TestScenario String to specify the scenario of the transaction happening
	 * @throws Exception Handles any exception that may happen
	 */
	public void executeDelayedShipment(String ExternalID, String Customer, String Location, String Items,
			String HasMultipleItem, String RequireCSVImport, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[VisaCheckOut][executeDelayedShipment] starts");
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.DELAY_SHIPMENT);
		String saleId = null;
		if (isDirectCaptureService) {
			// driver = commonUtil.setupDriverDirectUrl(prop, AuthorderIdForDirectCapture);
		}
		if (RequireCSVImport.equalsIgnoreCase(Constants.YES)) {
			executeCsvImport();
		}
		driver = commonUtil.setupDriver(driver, prop, Constants.EXTERNAL_ORDERID_SEARCH_URL, userIndex);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementByXPath(driver, prop, Constants.SEARCH_TAB);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNAL_ID_OPTION);
		driver.switchTo().frame(2);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_SEARCH_TEXT);
		commonUtil.setElementValueByXPath(driver, prop, Constants.EXTERNALID_SEARCH_TEXT, ExternalID);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		commonUtil.findElementByXPath(driver, prop, Constants.EXTERNALID_SEARCH_TEXT).sendKeys(Keys.ENTER);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_SET);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_SUBMIT);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_VIEW);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);

		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_APPROVE);
		// commonUtil.clickElementByXPath(driver, prop,
		// Constants.OVERRIDE_PENDING_PAYMENT);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_FULFILL);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.ORDER_FULLFIL);
		Object itemDetails[][] = null;
		itemDetails = commonUtil.getItemDetails(Items);
		if (Constants.YES.equals(HasMultipleItem.toUpperCase())) {
			for (int i = 0; i < itemDetails.length; i++) {
				commonUtil.setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, (i + 1),
						Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
			}
		} else {
			commonUtil.setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, 1,
					Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
		}
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		// Sales Order- Mark Packed button
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_PACKED);
		// Sales Order- Mark Shipped button
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_PACKED);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_BILL);
		// Cash Sale Setting Date
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_DATE_FIELD);
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_DATE_FIELD_RIGHT, 3);
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_DATE_FIELD_SET);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);

		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		saleId = commonUtil.getOrderId(driver, Constants.STRING_SALE);
		commonUtil.renameFolder(screenShotPath, Constants.DELAY_SHIPMENT, saleId);
		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.STRING_SALE,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario); // pnReferenceNumber
		// commonUtil.getPNReferenceNumber(driver, //
		// prop.getProperty(Constants.PI_ENABLED_PN_REFERENCE_NUMBER));
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), saleId);
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);
		logger.info("[VisaCheckOut][executeDelayedShipment] ends");
	}

	@SuppressWarnings("unused")
	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute multiple capture transaction
	 * @param ExternalID String containing external id used to create an order
	 * @param Customer String containing name of customer placing order
	 * @param Location String containing location of order in which services are to be executed
	 * @param Items String containing details of item to be placed
	 * @param HasMultipleItem String used to define if multiple items are placed or not
	 * @param RequireCSVImport String to validate if csv file is to be imported
	 * @param RequireEBCCheck String to check if validation from ebc portal is required or not
	 * @param ReasonCode string containing reason code for the transaction
	 * @param DecisionMsg String containing decision manager message of the transaction
	 * @param TestScenario String to specify the scenario of the transaction happening
	 * @throws Exception Handles any exception that may happen
	 */
	public void executeMultiCapture(String ExternalID, String Customer, String Location, String Items,
			String HasMultipleItem, String RequireCSVImport, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[VisaCheckOut][executeMultiCapture] starts");
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.MULTI_CAPTURE);
		String saleId = null;
		String authOrderId = null;
		String captureId = null;
		Object itemDetails[][] = null;
		if (isDirectCaptureService) {
			// driver = commonUtil.setupDriverDirectUrl(prop, AuthorderIdForDirectCapture);
		}
		if (RequireCSVImport.equalsIgnoreCase(Constants.YES)) {
			executeCsvImport();
		}
		driver = commonUtil.setupDriver(driver, prop, Constants.EXTERNAL_ORDERID_SEARCH_URL, userIndex);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementByXPath(driver, prop, Constants.SEARCH_TAB);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNAL_ID_OPTION);
		driver.switchTo().frame(2);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_SEARCH_TEXT);
		commonUtil.setElementValueByXPath(driver, prop, Constants.EXTERNALID_SEARCH_TEXT, ExternalID);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		commonUtil.findElementByXPath(driver, prop, Constants.EXTERNALID_SEARCH_TEXT).sendKeys(Keys.ENTER);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_SET);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_SUBMIT);
		commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNALID_VIEW);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);

		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_APPROVE);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_FULFILL);
		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.ORDER_FULLFIL);
		itemDetails = commonUtil.getItemDetails(Items);
		if (Constants.YES.equals(HasMultipleItem.toUpperCase())) {
			commonUtil.setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, 1,
					Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
		}
		for (int i = 0; i < itemDetails.length - 1; i++) {
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_FULFILL_CHECKBOX_PREFIX, (i + 2),
					Constants.SALES_ORDER_FULFILL_CHECKBOX_SUFFIX);
		}
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		// Sales Order- Mark Packed button
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_PACKED);
		// Sales Order- Mark Shipped button
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_PACKED);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_BILL);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);

		commonUtil.scrollToBottom(driver);
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.ORDER_CAPTURE);
		authOrderId = commonUtil.getOrderId(driver, Constants.AUTHORIZATION);
		captureId = commonUtil.getOrderId(driver, Constants.CAPTURE_AUTHORIZATION);
		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.CAPTURE_AUTHORIZATION,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.CAPTURE, commonUtil.getCurrentUrl(driver), captureId);
		commonUtil.addLogToTestNGReport(Constants.CAPTURE, pnReferenceNumber);

		logger.info("[VisaCheckOut][executeMultiCapture] Test1--------------------------");
		// executor.executeScript(prop.getProperty(Constants.SALES_ORDER_CREATED_FROM));
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(authOrderId, Keys.ENTER);
		logger.info("[VisaCheckOut][executeMultiCapture] Test2--------------------------");

		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_FULFILL);
		for (int i = 0; i < itemDetails.length - 1; i++) {
			commonUtil.setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, (i + 1),
					Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
		}
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		// Sales Order- Mark Packed button
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_PACKED);
		// Sales Order- Mark Shipped button
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_PACKED);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_BILL);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.scrollToBottom(driver);
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		saleId = commonUtil.getOrderId(driver, Constants.STRING_SALE);
		commonUtil.renameFolder(screenShotPath, Constants.MULTI_CAPTURE, saleId);
		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.STRING_SALE,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), saleId);
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);
		logger.info("[VisaCheckOut][executeMultiCapture] ends");
	}

	/**
	 * This method is used to validate imports for a customer
	 * @param Customer String containing name of existing customer
	 */
	public void ValidateImports(String Customer) {
		int rowCount = 0;
		boolean notFound = true;
		WebElement name = null;
		driver = commonUtil.setupDriver(driver, prop, Constants.IMPORTS_VALIDATE_URL, userIndex);
		List<WebElement> names = driver.findElements(By.xpath(prop.getProperty(Constants.IMPORT_VALIDATE_NAMES)));
		List<WebElement> status = driver.findElements(By.xpath(prop.getProperty(Constants.IMPORTS_VALIDATE_STATUS)));
		List<WebElement> code = driver.findElements(By.xpath(prop.getProperty(Constants.IMPORTS_VALIDATE_CODE)));
		Iterator<WebElement> it = names.iterator();
		while (it.hasNext()) {
			rowCount++;
			name = (WebElement) it.next();
			if (name.getText().trim().equals(Customer)) {
				if (status.get(rowCount).getText().equals(Constants.PENDING_APPROVAL)) {
					code.get(rowCount - 1).click();
					notFound = false;
					break;
				}
			}
		}
		if (notFound) {
			Reporter.log(Constants.NO_MATCHES);
			Assert.assertFalse(true, Constants.NO_MATCHING_FOUND);
		}
	}

	/**
	 * This method is used to set data to clipboard
	 * @param string String containing data to be set to clipboard
	 */
	public static void setClipboardData(String string) {
		// StringSelection is a class that can be used for copy and paste operations.
		StringSelection stringSelection = new StringSelection(string);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}

	/**
	 * This method is used to upload a file to a folder location
	 * @param fileLocation String containing path directory where file is to be uploaded to
	 */
	public static void uploadFile(String fileLocation) {
		try {
			// Setting clip board with file location
			setClipboardData(fileLocation);
			// native key strokes for CTRL, V and ENTER keys
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_ENTER);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

}
