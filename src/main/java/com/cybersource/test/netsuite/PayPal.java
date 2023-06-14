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

/**
 * PayPal class which will have all functionality used to place order and do related services using PayPal payment method
 * @author AD20243779
 *
 */
public class PayPal extends CommonTest {
	CommonUtil commonUtil = new CommonUtil();
	boolean isDirectCaptureService = false;
	boolean isDirectRefundService = false;
	boolean isDirectVoid = false;
	String pnReferenceNumber = null;
	WebDriver driver = null;
	String userIndex = "1";
	JavascriptExecutor executor = null;
	final static Logger logger = Logger.getLogger(PayPal.class);

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
		executeCsvImport();
	}

	@DataProvider
	/**
	 * This method is used to get data from excel sheets to execute test cases
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @return Object which contains data from sheet
	 */
	public Object[][] getdata(ITestContext context) {
		String currentScenario = context.getCurrentXmlTest().getParameter(Constants.TEST_SCENARIO);
		Object[][] data = FileUtil.getSheetData(XLSX_FILE_PATH,
				prop.getProperty(currentScenario + Constants._SHEET_NAME));
		return data;
	}

	/**
	 * This method is used to import csv file to test
	 * @throws InterruptedException Handles when a thread is waiting, sleeping, or otherwise occupied,and the thread is interrupted, either before or during the activity
	 * @throws AWTException Handles any Abstract Window Toolkit exception that can occur
	 */
	public void executeCsvImport() throws InterruptedException, AWTException {
		logger.info("[PayPal][executeCsvImport] starts");
		driver = commonUtil.setupDriver(driver, prop, Constants.CSV_IMPORT_URL, userIndex);
		Reporter.log("URL Loaded");
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		commonUtil.clickElementByXPath(driver, prop, Constants.IMPORT_SELECTMAP);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		// Scrolling into view
		WebElement element = driver.findElement(By.xpath(prop.getProperty(Constants.IMPORT_SELECTBUTTON)));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		Actions builder = new Actions(driver);
		builder.moveToElement(element).click().build().perform();
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		uploadFile(prop.getProperty(Constants.IMPORT_CSV_FILE_PATH));
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.IMPORT_FILEUPLOAD_NEXT);
		commonUtil.clickElementByXPath(driver, prop, Constants.IMPORT_ADD_OR_UPDATE_RADIO);
		commonUtil.clickElementByXPath(driver, prop, Constants.IMPORT_IMPORTOPTIONS_NEXT);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.IMPORT_FIELD_MAP_NEXT);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clearElementByXPath(driver, prop, Constants.IMPORT_SAVE_AND_RUN_ID);
		commonUtil.clickElementByXPath(driver, prop, Constants.IMPORT_SAVE_AND_RUN);
		if (commonUtil.isAlertPresent(driver))
			commonUtil.acceptAlert(driver);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.IMPORT_JOB_STATUS);
		String message = commonUtil.getElementTextByXPath(driver, prop, Constants.IMPORT_JOB_STATUS_LAST_ROW);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_10);
		commonUtil.refreshCurrentPage(driver);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_10);
		logger.info(message.charAt(0));
		logger.info(message.charAt(5));
		char actual = message.charAt(0);
		char expected = message.charAt(5);
		Assert.assertEquals(actual, expected);
		logger.info("[PayPal][executeCsvImport] ends");
	}

	@Test(dataProvider = Constants.GET_DATA)
	/**
	 * This method is used to execute a refund service after a capture service
	 * @param CustomerName String containing name of customer placing order
	 * @param Memo String containing memo of the order
	 * @param ItemName String containing item name to be ordered
	 * @param Quantity String containing quantity of item to be ordered
	 * @param Rate String containing rate of item to be ordered
	 * @param TaxCode String containing tax code of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param Location String containing order location
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CSC String containing CSC details for transaction
	 * @param ChangeDue String containing change due detail for transaction
	 * @param CardDetail String containing details of card used for transaction
	 * @param PurchaseTotalsCurrency String containing currency in which purchase total is to be displayed
	 * @param MerchantReferenceCode String containing merchant reference code
	 * @param MerchantID String containing merchant ID for transaction
	 * @param AuthorderIdForDirectCapture String contain auth order id for direct capture
	 * @param AuthorderIdForDirectVoid String contain auth order id for direct void
	 * @param CaptureIdForDirectRefund String contain auth order id for direct refund
	 * @param CaptureIdForCustomerRefund String containing capture id to be used for customer refund service
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 */
	public void executeCaptureRefund(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String ChangeDue, String CardDetail, String PurchaseTotalsCurrency, String MerchantReferenceCode,
			String MerchantID, String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid,
			String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) {
		logger.info("[PayPal][executeCaptureRefund] starts");
		executeCapture(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, ChangeDue, CardDetail, PurchaseTotalsCurrency, MerchantReferenceCode, MerchantID,
				AuthorderIdForDirectCapture, AuthorderIdForDirectVoid, CaptureIdForDirectRefund,
				CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		executeRefund(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, ChangeDue, CardDetail, PurchaseTotalsCurrency, MerchantReferenceCode, MerchantID,
				AuthorderIdForDirectCapture, AuthorderIdForDirectVoid, CaptureIdForDirectRefund,
				CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		logger.info("[PayPal][executeCaptureRefund] ends");
	}

	@Test(dataProvider = "getdata")
	/**
	 * This method is used to execute a capture service
	 * @param CustomerName String containing name of customer placing order
	 * @param Memo String containing memo of the order
	 * @param ItemName String containing item name to be ordered
	 * @param Quantity String containing quantity of item to be ordered
	 * @param Rate String containing rate of item to be ordered
	 * @param TaxCode String containing tax code of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param Location String containing order location
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CSC String containing CSC details for transaction
	 * @param ChangeDue String containing change due detail for transaction
	 * @param CardDetail String containing details of card used for transaction
	 * @param PurchaseTotalsCurrency String containing currency in which purchase total is to be displayed
	 * @param MerchantReferenceCode String containing merchant reference code
	 * @param MerchantID String containing merchant ID for transaction
	 * @param AuthorderIdForDirectCapture String contain auth order id for direct capture
	 * @param AuthorderIdForDirectVoid String contain auth order id for direct void
	 * @param CaptureIdForDirectRefund String contain auth order id for direct refund
	 * @param CaptureIdForCustomerRefund String containing capture id to be used for customer refund service
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 */
	public void executeCapture(String Customer, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String ChangeDue, String CardDetail, String PurchaseTotalsCurrency, String MerchantReferenceCode,
			String MerchantID, String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid,
			String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) {
		ValidateImports(Customer);
		logger.info("[PayPal][executeCapture] starts");
		if (isDirectCaptureService) {
			driver = commonUtil.setupDriverDirectUrl(driver, prop, AuthorderIdForDirectCapture, userIndex);
		}
		executor = (JavascriptExecutor) driver;
		executor.executeScript(prop.getProperty(Constants.SALES_ORDER_APPROVE));
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
		commonUtil.writeCurrentTransactionUrl(Constants.CAPTURE_ORDERID_URL, commonUtil.getCurrentUrl(driver));
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.CAPTURE_AUTHORIZATION,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty("reason.code"),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		String PNReferenceNumber = commonUtil.getPNReferenceNumber(driver,
				prop.getProperty(Constants.PI_ENABLED_PN_REFERENCE_NUMBER));
		if (Constants.YES.equals(RequireEBCCheck)) {
			commonUtil.checkTransactionInEBC(driver, PNReferenceNumber);
		}
		logger.info("[PayPal][executeCapture] ends");
	}

	@Test(dataProvider = Constants.GET_DATA)
	/**
	 * This method is used to execute a refund service
	 * @param CustomerName String containing name of customer placing order
	 * @param Memo String containing memo of the order
	 * @param ItemName String containing item name to be ordered
	 * @param Quantity String containing quantity of item to be ordered
	 * @param Rate String containing rate of item to be ordered
	 * @param TaxCode String containing tax code of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param Location String containing order location
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CSC String containing CSC details for transaction
	 * @param ChangeDue String containing change due detail for transaction
	 * @param CardDetail String containing details of card used for transaction
	 * @param PurchaseTotalsCurrency String containing currency in which purchase total is to be displayed
	 * @param MerchantReferenceCode String containing merchant reference code
	 * @param MerchantID String containing merchant ID for transaction
	 * @param AuthorderIdForDirectCapture String contain auth order id for direct capture
	 * @param AuthorderIdForDirectVoid String contain auth order id for direct void
	 * @param CaptureIdForDirectRefund String contain auth order id for direct refund
	 * @param CaptureIdForCustomerRefund String containing capture id to be used for customer refund service
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 */
	public void executeRefund(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String ChangeDue, String CardDetail, String PurchaseTotalsCurrency, String MerchantReferenceCode,
			String MerchantID, String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid,
			String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) {
		logger.info("[PayPal][executeRefund] starts");
		String refundId = null;
		if (isDirectRefundService) {
			driver = commonUtil.setupDriverDirectUrl(driver, prop, CaptureIdForDirectRefund, userIndex);
		}
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
		commonUtil.findElementByXPath(driver, prop, Constants.CASH_SALE_LOCATION_NINE).sendKeys(Keys.DOWN, Keys.DOWN,
				Keys.ENTER);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.REFUND,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
				prop.getProperty(Constants.PI_ENABLED_PN_REFERENCE_NUMBER));
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		refundId = commonUtil.getOrderId(driver, Constants.REFUND);
		commonUtil.addLogToTestNGReport(Constants.REFUND, pnReferenceNumber);
		commonUtil.addLogToTestNGReport(Constants.REFUND, commonUtil.getCurrentUrl(driver), refundId);
		logger.info("[PayPal][executeRefund] ends");
	}

	@SuppressWarnings("rawtypes")
	/**
	 * This method is used to validate imports for a customer
	 * @param Customer String containing name of existing customer
	 */
	public void ValidateImports(String Customer) {
		int rowCount = 0;
		driver = commonUtil.setupDriver(driver, prop, Constants.IMPORTS_VALIDATE_URL, userIndex);
		List<WebElement> names = driver.findElements(By.xpath(prop.getProperty(Constants.IMPORT_VALIDATE_NAMES)));
		List<WebElement> status = driver.findElements(By.xpath(prop.getProperty(Constants.IMPORTS_VALIDATE_STATUS)));
		List<WebElement> code = driver.findElements(By.xpath(prop.getProperty(Constants.IMPORTS_VALIDATE_CODE)));
		Iterator it = names.iterator();
		boolean notFound = true;
		logger.info(Customer);
		while (it.hasNext()) {
			rowCount++;
			WebElement name = (WebElement) it.next();
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
			// Setting clipboard with file location
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
