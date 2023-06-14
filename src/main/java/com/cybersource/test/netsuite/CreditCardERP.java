package com.cybersource.test.netsuite;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.cybersource.test.CommonTest;
import com.cybersource.test.util.CommonUtil;
import com.cybersource.test.util.Constants;
import com.cybersource.test.util.FileUtil;
import com.cybersource.test.util.RetryFailedTestCases;

@Listeners(com.cybersource.test.util.Listeners.class)
/**
 * CreditCardERP class which will have all functionality used to place order and do related services using Credit card ERP payment method
 * @author AD20243779
 *
 */
public class CreditCardERP extends CommonTest {
	String orderManagementType = null;
	String pnReferenceNumber = null;
	String authOrderId = null;
	String saleId = null;
	String screenShotPath = null;
	String screenShotModulePath = null;
	String userIndex = "1";
	Object itemDetails[][] = null;
	JavascriptExecutor executor = null;
	WebDriver driver = null;
	CommonUtil commonUtil = new CommonUtil();
	final static Logger logger = Logger.getLogger(CreditCardERP.class);

	@BeforeTest
	/**
	 * This method is used to initialize test environment before running a test
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @throws IOException Handles when an I/O exception of some sort has occurred
	 */
	public void init(ITestContext context) throws IOException {
		super.loadCommonConfig(context);
		if (null != context.getCurrentXmlTest().getParameter(Constants.USER_INDEX)) {
			userIndex = context.getCurrentXmlTest().getParameter(Constants.USER_INDEX);
		}
		commonUtil.executionSleepTimer(userIndex);
		screenShotModulePath = commonUtil.getScreenshotsModulePath(Constants.CREDIT_CARD_ERP);
	}

	@DataProvider
	/**
	 * This method is used to get data from excel sheets to execute test cases
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @return Object which contains data from sheet
	 */
	public Object[][] getData(ITestContext context) {
		Object[][] data = null;
		String prefix = context.getCurrentXmlTest().getParameter(Constants.PREFIX);
		String currentScenario = context.getCurrentXmlTest().getParameter(Constants.TEST_SCENARIO);
		orderManagementType = context.getCurrentXmlTest().getParameter(Constants.ORDER_MANAGEMENT_TYPE);
		data = FileUtil.getSheetData(XLSX_FILE_PATH,
				prop.getProperty(prefix + currentScenario + Constants._SHEET_NAME));
		logger.info("[getData] currentScenario:" + currentScenario);
		return data;
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute capture service after completing a authorization service
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
	 * @param CardSecurityCode String containing security code of card used for transaction
	 * @param CardDetail String containing details of card used for transaction
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param PaymentType String containing type of payment used
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthCapture(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod,
			String CardSecurityCode, String CardDetail, String PaymentProcessingProfile, String PaymentType,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthCapture] Starts");
		executeAuth(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CardSecurityCode, CardDetail, PaymentProcessingProfile, PaymentType, RequireEBCCheck,
				ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeCapture(driver, Items, HasMultipleItem, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario, screenShotPath, screenShotModulePath);
		logger.info("[executeAuthCapture] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund service after completing a authorization and capture services
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
	 * @param CardSecurityCode String containing security code of card used for transaction
	 * @param CardDetail String containing details of card used for transaction
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param PaymentType String containing type of payment used
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthCaptureRefund(String CustomerName, String Memo, String ItemName, String Quantity,
			String Rate, String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod,
			String CardSecurityCode, String CardDetail, String PaymentProcessingProfile, String PaymentType,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthCaptureRefund] Starts");
		executeAuth(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CardSecurityCode, CardDetail, PaymentProcessingProfile, PaymentType, RequireEBCCheck,
				ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeCapture(driver, Items, HasMultipleItem, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario, screenShotPath, screenShotModulePath);
		commonUtil.executeRefund(driver, Location, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario,
				screenShotPath, screenShotModulePath);
		logger.info("[executeAuthCaptureRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute customer refund service after completing a authorization and capture services
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
	 * @param CardSecurityCode String containing security code of card used for transaction
	 * @param CardDetail String containing details of card used for transaction
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param PaymentType String containing type of payment used
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthCaptureCustomerRefund(String CustomerName, String Memo, String ItemName, String Quantity,
			String Rate, String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod,
			String CSC, String CardDetail, String PPP, String PaymentType, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthCaptureCustomerRefund] Starts");
		executeAuth(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, PPP, PaymentType, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		commonUtil.executeCapture(driver, Items, HasMultipleItem, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario, screenShotPath, screenShotModulePath);
		commonUtil.executeCustomerRefund(driver, Items, HasMultipleItem, Location, RequireEBCCheck, ReasonCode,
				DecisionMsg, TestScenario, screenShotPath, screenShotModulePath);
		logger.info("[executeAuthCaptureCustomerRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute void service after completing a authorization service
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
	 * @param CardSecurityCode String containing security code of card used for transaction
	 * @param CardDetail String containing details of card used for transaction
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param PaymentType String containing type of payment used
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthVoid(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String PPP, String PaymentType, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthVoid] Starts");
		executeAuth(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, PPP, PaymentType, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		commonUtil.executeVoid(driver, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario, screenShotPath,
				screenShotModulePath);
		logger.info("[executeAuthVoid] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute delayed shipment service after completing a authorization service
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
	 * @param CSC String containing security code of card used for transaction
	 * @param CardDetail String containing details of card used for transaction
	 * @param PPP String containing payment processing profile used
	 * @param PaymentType String containing type of payment used
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthDelayedShipment(String CustomerName, String Memo, String ItemName, String Quantity,
			String Rate, String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod,
			String CSC, String CardDetail, String PPP, String PaymentType, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthDelayedShipment] Starts");
		executeAuth(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, PPP, PaymentType, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		commonUtil.executeDelayedShipment(driver, Items, HasMultipleItem, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario, screenShotPath, screenShotModulePath);
		logger.info("[executeAuthDelayedShipment] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute multiple capture service after completing a authorization service
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
	 * @param CSC String containing security code of card used for transaction
	 * @param CardDetail String containing details of card used for transaction
	 * @param PPP String containing payment processing profile used
	 * @param PaymentType String containing type of payment used
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthMultipleCapture(String CustomerName, String Memo, String ItemName, String Quantity,
			String Rate, String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod,
			String CSC, String CardDetail, String PPP, String PaymentType, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeMultipleCapture] Starts");
		executeAuth(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, PPP, PaymentType, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		commonUtil.executeMultipleCapture(driver, Items, HasMultipleItem, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario, screenShotPath, screenShotModulePath);
		logger.info("[executeMultipleCapture] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund service after completing a sale service
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
	 * @param CSC String containing security code of card used for transaction
	 * @param CardDetail String containing details of card used for transaction
	 * @param PPP String containing payment processing profile used
	 * @param PaymentType String containing type of payment used
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeSaleRefund(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String PPP, String PaymentType, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeSaleRefund] Starts");
		executeSale(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, PPP, PaymentType, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		commonUtil.executeRefund(driver, Location, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario,
				screenShotPath, screenShotModulePath);
		logger.info("[executeSaleRefund] Ends");
	}

	@Test
	/**
	 * This method is used to clear all cache in the active web browser
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void clearBrowserCache() throws Exception {
		logger.info("[clearBrowserCache] Starts");
		commonUtil.clearBrowserCache(driver, userIndex);
		logger.info("[clearBrowserCache] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute an authorization service
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
	 * @param CSC String containing security code of card used for transaction
	 * @param CardDetail String containing details of card used for transaction
	 * @param PPP String containing payment processing profile used
	 * @param PaymentType String containing type of payment used
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuth(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String PPP, String PaymentType, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuth] Starts");
		logger.info("[executeAuth] Excel Data: " + CustomerName + Constants.COMMA_SPACE + Memo + Constants.COMMA_SPACE
				+ ItemName + Constants.COMMA_SPACE + Quantity + Constants.COMMA_SPACE + Rate + Constants.COMMA_SPACE
				+ TaxCode + Constants.COMMA_SPACE + Items + Constants.COMMA_SPACE + HasMultipleItem
				+ Constants.COMMA_SPACE + Location + Constants.COMMA_SPACE + PaymentMethod + Constants.COMMA_SPACE + CSC
				+ Constants.COMMA_SPACE + CardDetail + Constants.COMMA_SPACE + PPP + Constants.COMMA_SPACE + PaymentType
				+ Constants.COMMA_SPACE + RequireEBCCheck + Constants.COMMA_SPACE + ReasonCode + Constants.COMMA_SPACE
				+ DecisionMsg + Constants.COMMA_SPACE + TestScenario);
		int csc = CSC.length();
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALES_ORDER_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		executor = (JavascriptExecutor) driver;
		if (Constants.POSITIVE.toUpperCase().equals(TestScenario.toUpperCase())) {
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);

			if (Constants.NO.equalsIgnoreCase(prop.getProperty(Constants.CUSTOMER_ID_PRESENT))) {
				commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.DOWN,
						Keys.ENTER);
			}
			// wait until customer is set
			for (int i = 0; i < browserMaxTimeoutSeconds; i++) {
				logger.info("[executeAuth] shipping cost value: "
						+ commonUtil.getElementTextByXPath(driver, prop, Constants.SALES_ORDER_SUMMARY_SHIPPING_COST));
				if (commonUtil.getElementTextByXPath(driver, prop,
						Constants.SALES_ORDER_SUMMARY_SHIPPING_COST) != null) {
					break;
				} else {
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				}
			}
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Memo);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.takeScreenShot(driver, screenShotPath, Constants.CUSTOMER_DETAILS_ENTRY);
			if (Constants.YES.equals(HasMultipleItem.toUpperCase())) {
				itemDetails = commonUtil.getItemDetails(Items);
				commonUtil.setItemDetails(driver, itemDetails);
			} else {
				commonUtil.setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_PREFIX, ItemName,
						Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.ifAlertPresentAccept(driver);
				commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_QUANTITY_PREFIX, Quantity,
						Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_RATE_PREFIX, Rate,
						Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				executor.executeScript(prop.getProperty(Constants.NLAPI_COMMIT_LINE_ITEM));
			}
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Constants.EMPTY_STRING);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.setFieldText(driver, Constants.NLAPI_FIELD_TEXT_PREFIX_PAYMENTOPTION, PaymentMethod,
					Constants.NLAPI_FIELD_TEXT_SUFFIX);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_PROCESSING_PROFILE, PPP);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_BILL_CSC, CSC);
			if (commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_TYPE).isDisplayed()) {
				commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_TYPE, PaymentType);
			}
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);

			commonUtil.scrollToBottom(driver);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
			authOrderId = commonUtil.getOrderId(driver, Constants.AUTHORIZATION);
			commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, commonUtil.getCurrentUrl(driver), authOrderId);

			if (Constants.MOTO.equalsIgnoreCase(PaymentType)) {
				pnReferenceNumber = commonUtil.checkActuals(driver, Constants.AUTHORIZATION,
						prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
						prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
			} else {
				pnReferenceNumber = commonUtil.checkMITActuals(driver, Constants.AUTHORIZATION, PaymentType,
						prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
						prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
			}
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, pnReferenceNumber);

			if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
				commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
			}
		} else if (Constants.NULL.equals(TestScenario.toUpperCase())) {
			// If CustomerName available then proceed.
			if (Constants.ZERO < CustomerName.trim().length()) {
				// If Item is not there, alert come, we need to asset against that.
				if (Constants.ZERO < ItemName.trim().length()) {
					commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
					commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.ENTER);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				}
			}
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			if (commonUtil.isAlertPresent(driver)) {
				Assert.assertEquals(Constants.TRUE, commonUtil.isAlertPresent(driver));
				commonUtil.acceptAlert(driver);
			}
		} else if (Constants.NULL_CUSTOMER_ADDRESS_FIELD.toUpperCase().equals(TestScenario.toUpperCase())) {
			// If Item and customer available but customer record don't have street/city/ZIP
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.ENTER);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Memo);
			commonUtil.setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_PREFIX, ItemName,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_QUANTITY_PREFIX, Quantity,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_RATE_PREFIX, Rate,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			executor.executeScript(prop.getProperty(Constants.NLAPI_COMMIT_LINE_ITEM));
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.setFieldText(driver, Constants.NLAPI_FIELD_TEXT_PREFIX_PAYMENTOPTION, PaymentMethod,
					Constants.NLAPI_FIELD_TEXT_SUFFIX);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_PROCESSING_PROFILE, PPP);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_BILL_CSC, CSC);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			if (Constants.TRANSACTION_SUCCESSFULLY_SAVED.toUpperCase().equals(commonUtil
					.findElementByXPath(driver, prop, Constants.SALES_ORDER_STATUS).getText().toUpperCase())) {
				Assert.assertTrue(false,
						"Defect:: Customer billing Address is incorrect. Still tracsaction is successfully placed.");
			} else {
				Assert.assertTrue(true);
			}
		} else if (Constants.INVALID.equals(TestScenario)) {
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_PREFIX, ItemName,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_QUANTITY_PREFIX, Quantity,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			String cardDetailStr = CardDetail.trim().toUpperCase();
			if (!commonUtil.isNumeric(Quantity) || Constants.QUANTITY < Long.parseLong(Quantity)) {
				commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
				if (commonUtil.isAlertPresent(driver)) {
					Assert.assertEquals(Constants.TRUE, commonUtil.isAlertPresent(driver));
					commonUtil.acceptAlert(driver);
				}
			} else if (Constants.VISA.equals(cardDetailStr) && Constants.THREE != csc
					|| Constants.AMEX.equals(cardDetailStr) && Constants.FOUR != csc
					|| Constants.MASTERCARD.equals(cardDetailStr) && Constants.THREE != csc) {
				executor.executeScript(prop.getProperty(Constants.NLAPI_COMMIT_LINE_ITEM));
				commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
				commonUtil.setFieldText(driver, Constants.NLAPI_FIELD_TEXT_PREFIX_PAYMENTOPTION, PaymentMethod,
						Constants.NLAPI_FIELD_TEXT_SUFFIX);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_BILL_CSC, CSC);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
				if (Constants.TRANSACTION_SUCCESSFULLY_SAVED.toUpperCase().equals(commonUtil
						.findElementByXPath(driver, prop, Constants.SALES_ORDER_STATUS).getText().toUpperCase())) {
					Assert.assertTrue(false, "Defect:: Invalid CVN number. Still tracsaction is successfully placed.");
				} else {
					Assert.assertTrue(true);
				}
			}
		}
		screenShotPath = screenShotModulePath;
		logger.info("[executeAuth] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute a sale service
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
	 * @param CSC String containing security code of card used for transaction
	 * @param CardDetail String containing details of card used for transaction
	 * @param PPP String containing payment processing profile used
	 * @param PaymentType String containing type of payment used
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeSale(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String PPP, String PaymentType, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeSale] Starts");
		logger.info("[executeSale] Excel Data: " + CustomerName + Constants.COMMA_SPACE + Memo + Constants.COMMA_SPACE
				+ ItemName + Constants.COMMA_SPACE + Quantity + Constants.COMMA_SPACE + Rate + Constants.COMMA_SPACE
				+ TaxCode + Constants.COMMA_SPACE + Items + Constants.COMMA_SPACE + HasMultipleItem
				+ Constants.COMMA_SPACE + Location + Constants.COMMA_SPACE + PaymentMethod + Constants.COMMA_SPACE + CSC
				+ Constants.COMMA_SPACE + CardDetail + Constants.COMMA_SPACE + PPP + Constants.COMMA_SPACE + PaymentType
				+ Constants.COMMA_SPACE + RequireEBCCheck + Constants.COMMA_SPACE + ReasonCode + Constants.COMMA_SPACE
				+ DecisionMsg + Constants.COMMA_SPACE + TestScenario);
		driver = null;
		int csc = CSC.length();
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.STRING_SALE);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALE_URL, userIndex);
		if (commonUtil.isAlertPresent(driver)) {
			commonUtil.acceptAlert(driver);
		}
		executor = (JavascriptExecutor) driver;
		if (Constants.POSITIVE.toUpperCase().equals(TestScenario.toUpperCase())) {
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);

			if (Constants.NO.equalsIgnoreCase(prop.getProperty(Constants.CUSTOMER_ID_PRESENT))) {
				commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.DOWN,
						Keys.ENTER);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			}
			// wait until customer is set
			for (int i = 0; i < browserMaxTimeoutSeconds; i++) {
				logger.info("[executeSale] shipping cost value: "
						+ commonUtil.getElementTextByXPath(driver, prop, Constants.SALES_ORDER_SUMMARY_SHIPPING_COST));
				if (commonUtil.getElementTextByXPath(driver, prop,
						Constants.SALES_ORDER_SUMMARY_SHIPPING_COST) != null) {
					break;
				} else {
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				}
			}
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Memo);
			commonUtil.takeScreenShot(driver, screenShotPath, Constants.CUSTOMER_DETAILS_ENTRY);
			if (Constants.YES.equals(HasMultipleItem.toUpperCase())) {
				itemDetails = commonUtil.getItemDetails(Items);
				commonUtil.setItemDetails(driver, itemDetails);
			} else {
				commonUtil.setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_PREFIX, ItemName,
						Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.ifAlertPresentAccept(driver);
				commonUtil.ifAlertPresentAccept(driver);
				commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_QUANTITY_PREFIX, Quantity,
						Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_RATE_PREFIX, Rate,
						Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				executor.executeScript(prop.getProperty(Constants.NLAPI_COMMIT_LINE_ITEM));
			}
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.takeScreenShot(driver, screenShotPath, Constants.ITEM_DETAILS_ENTRY);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_LOCATION, Location);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);

			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.setFieldText(driver, Constants.NLAPI_FIELD_TEXT_PREFIX_PAYMENTOPTION, PaymentMethod,
					Constants.NLAPI_FIELD_TEXT_SUFFIX);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_PROCESSING_PROFILE, PPP);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_BILL_CSC, CSC);
			if (commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_TYPE).isDisplayed()) {
				commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_TYPE, PaymentType);
			}
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);

			commonUtil.scrollToBottom(driver);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
			saleId = commonUtil.getOrderId(driver, Constants.STRING_SALE);
			commonUtil.renameFolder(screenShotPath, Constants.STRING_SALE, saleId);
			commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), saleId);

			if (Constants.MOTO.equalsIgnoreCase(PaymentType)) {
				pnReferenceNumber = commonUtil.checkActuals(driver, Constants.STRING_SALE,
						prop.getProperty(Constants.SALE_DECISION_MESSAGE), prop.getProperty(Constants.SALE_REASON_CODE),
						prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
			} else {
				pnReferenceNumber = commonUtil.checkMITActuals(driver, Constants.STRING_SALE, PaymentType,
						prop.getProperty(Constants.SALE_DECISION_MESSAGE), prop.getProperty(Constants.SALE_REASON_CODE),
						prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
			}
			commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);

			if (Constants.YES.equalsIgnoreCase(RequireEBCCheck)) {
				commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
			}
		} else if (Constants.NULL.equals(TestScenario.toUpperCase())) {
			// If CustomerName available then proceed.
			if (Constants.ZERO < CustomerName.trim().length()) {
				// If Item is not there, alert come, we need to asset against that.
				if (Constants.ZERO < ItemName.trim().length()) {
					commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
					commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.ENTER);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				}
			}
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			if (commonUtil.isAlertPresent(driver)) {
				Assert.assertEquals(Constants.TRUE, commonUtil.isAlertPresent(driver));
				commonUtil.acceptAlert(driver);
			}
		} else if (Constants.NULL_CUSTOMER_ADDRESS_FIELD.toUpperCase().equals(TestScenario.toUpperCase())) {
			// If Item and customer available but customer record don't have street/city/ZIP
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
			commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.ENTER);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Memo);
			commonUtil.setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_PREFIX, ItemName,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_QUANTITY_PREFIX, Quantity,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_RATE_PREFIX, Rate,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			executor.executeScript(prop.getProperty(Constants.NLAPI_COMMIT_LINE_ITEM));

			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.setFieldText(driver, Constants.NLAPI_FIELD_TEXT_PREFIX_PAYMENTOPTION, PaymentMethod,
					Constants.NLAPI_FIELD_TEXT_SUFFIX);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_BILL_CSC, CSC);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			if (Constants.TRANSACTION_SUCCESSFULLY_SAVED.toUpperCase().equals(commonUtil
					.findElementByXPath(driver, prop, Constants.SALES_ORDER_STATUS).getText().toUpperCase())) {
				Assert.assertTrue(false,
						"Defect:: Customer billing Address is incorrect. Still tracsaction is successfully placed.");
			} else {
				Assert.assertTrue(true);
			}
		} else if (Constants.INVALID.equalsIgnoreCase(TestScenario)) {
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
			commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.ENTER);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_PREFIX, ItemName,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_QUANTITY_PREFIX, Quantity,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			String cardDetailStr = CardDetail.trim().toUpperCase();
			if (!commonUtil.isNumeric(Quantity) || Constants.QUANTITY < Long.parseLong(Quantity)) {
				commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
				if (commonUtil.isAlertPresent(driver)) {
					Assert.assertEquals(Constants.TRUE, commonUtil.isAlertPresent(driver));
					commonUtil.acceptAlert(driver);
				}
			} else if (Constants.VISA.equals(cardDetailStr) && Constants.THREE != csc
					|| Constants.AMEX.equals(cardDetailStr) && Constants.FOUR != csc
					|| Constants.MASTERCARD.equals(cardDetailStr) && Constants.THREE != csc) {
				executor.executeScript(prop.getProperty(Constants.NLAPI_COMMIT_LINE_ITEM));
				commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
				commonUtil.setFieldText(driver, Constants.NLAPI_FIELD_TEXT_PREFIX_PAYMENTOPTION, PaymentMethod,
						Constants.NLAPI_FIELD_TEXT_SUFFIX);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_BILL_CSC, CSC);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
				if (Constants.TRANSACTION_SUCCESSFULLY_SAVED.toUpperCase().equals(commonUtil
						.findElementByXPath(driver, prop, Constants.SALES_ORDER_STATUS).getText().toUpperCase())) {
					Assert.assertTrue(false, "Defect:: Invalid CVN number. Still tracsaction is successfully placed.");
				} else {
					Assert.assertTrue(true);
				}
			}
		}
		screenShotPath = screenShotModulePath;
		logger.info("[executeSale] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute a refund service after an ACH sale service
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
	 * @param SecCode String containing ACH security code
	 * @param PPP String containing payment processing profile used
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
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeACHSaleRefund(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String SecCode,
			String PPP, String CardDetail, String PurchaseTotalsCurrency, String MerchantReferenceCode,
			String MerchantID, String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid,
			String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeACHSaleRefund] Starts");
		String saleId = null;
		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.STRING_SALE);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALE_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		executor = (JavascriptExecutor) driver;
		if (Constants.POSITIVE.toUpperCase().equals(TestScenario.toUpperCase())) {
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);

			if (Constants.NO.equalsIgnoreCase(prop.getProperty(Constants.CUSTOMER_ID_PRESENT))) {
				commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.DOWN,
						Keys.ENTER);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			}

			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Memo);
			commonUtil.takeScreenShot(driver, screenShotPath, Constants.CUSTOMER_DETAILS_ENTRY);
			commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_LOCATION).sendKeys(Keys.DOWN);
			if (Constants.YES.equals(HasMultipleItem.toUpperCase())) {
				itemDetails = commonUtil.getItemDetails(Items);
				commonUtil.setItemDetails(driver, itemDetails);
			} else {
				commonUtil.setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_PREFIX, ItemName,
						Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.ifAlertPresentAccept(driver);
				commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_QUANTITY_PREFIX, Quantity,
						Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_RATE_PREFIX, Rate,
						Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				executor.executeScript(prop.getProperty(Constants.NLAPI_COMMIT_LINE_ITEM));
			}
			commonUtil.takeScreenShot(driver, screenShotPath, Constants.ITEM_DETAILS_ENTRY);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Constants.EMPTY_STRING);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.setFieldText(driver, Constants.NLAPI_FIELD_TEXT_PREFIX_PAYMENTOPTION, PaymentMethod,
					Constants.NLAPI_FIELD_TEXT_SUFFIX);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.setElementValueByXPath(driver, prop, Constants.CASH_SALE_PAYMENT_PROCESSING_PROFILE, PPP);
			commonUtil.setElementValueByXPath(driver, prop, Constants.CASH_SALE_ACH_SEC_CODE, SecCode);
			// commonUtil.clickElementByXPath(driver, prop,
			// Constants.CASH_SALE_ACH_PAYMENT_OPERATION);
			// commonUtil.clickElementByXPath(driver, prop,
			// Constants.CASH_SALE_ACH_SEC_CODE);
			commonUtil.scrollToBottom(driver);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_10);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			if (commonUtil.isAlertPresent(driver)) {
				commonUtil.acceptAlert(driver);
			}
			commonUtil.scrollToBottom(driver);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
			saleId = commonUtil.getOrderId(driver, Constants.STRING_SALE);
			commonUtil.renameFolder(screenShotPath, Constants.STRING_SALE, saleId);
			if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.PROCESS_TYPE_REST))) {
				pnReferenceNumber = commonUtil.checkRestActuals(driver, Constants.STRING_SALE, ReasonCode,
						TestScenario);
			} else {
				pnReferenceNumber = commonUtil.checkActuals(driver, Constants.STRING_SALE,
						prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
						prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
			}
			commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), saleId);
			commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);
		}
		// refund
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.REFUND);
		String refundId = null;
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
		commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_LOCATION).sendKeys(Keys.DOWN);
		commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_LOCATION, Location);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_10);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);

		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.scrollToBottom(driver);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		refundId = commonUtil.getOrderId(driver, Constants.REFUND);
		commonUtil.renameFolder(screenShotPath, Constants.REFUND, refundId);
		commonUtil.addLogToTestNGReport(Constants.REFUND, commonUtil.getCurrentUrl(driver), refundId);

		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.REFUND,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		commonUtil.addLogToTestNGReport(Constants.REFUND, pnReferenceNumber);

		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		logger.info("[executeACHSaleRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute a customer refund service after an ACH sale service
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
	 * @param SecCode String containing ACH security code
	 * @param PPP String containing payment processing profile used
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
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeACHSaleCustomerRefund(String CustomerName, String Memo, String ItemName, String Quantity,
			String Rate, String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod,
			String SecCode, String PPP, String CardDetail, String PurchaseTotalsCurrency, String MerchantReferenceCode,
			String MerchantID, String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid,
			String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeACHSaleCustomerRefund] Starts");
		String saleId = null;
		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.STRING_SALE);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALE_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		executor = (JavascriptExecutor) driver;
		if (Constants.POSITIVE.toUpperCase().equals(TestScenario.toUpperCase())) {
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);

			if (Constants.NO.equalsIgnoreCase(prop.getProperty(Constants.CUSTOMER_ID_PRESENT))) {
				commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.DOWN,
						Keys.ENTER);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			}
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Memo);
			commonUtil.takeScreenShot(driver, screenShotPath, Constants.CUSTOMER_DETAILS_ENTRY);
			commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_LOCATION).sendKeys(Keys.DOWN);
			if (Constants.YES.equals(HasMultipleItem.toUpperCase())) {
				itemDetails = commonUtil.getItemDetails(Items);
				commonUtil.setItemDetails(driver, itemDetails);
			} else {
				commonUtil.setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_PREFIX, ItemName,
						Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.ifAlertPresentAccept(driver);
				commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_QUANTITY_PREFIX, Quantity,
						Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_RATE_PREFIX, Rate,
						Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
				executor.executeScript(prop.getProperty(Constants.NLAPI_COMMIT_LINE_ITEM));
			}
			commonUtil.takeScreenShot(driver, screenShotPath, Constants.ITEM_DETAILS_ENTRY);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Constants.EMPTY_STRING);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.setFieldText(driver, Constants.NLAPI_FIELD_TEXT_PREFIX_PAYMENTOPTION, PaymentMethod,
					Constants.NLAPI_FIELD_TEXT_SUFFIX);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.setElementValueByXPath(driver, prop, Constants.CASH_SALE_PAYMENT_PROCESSING_PROFILE, PPP);
			commonUtil.setElementValueByXPath(driver, prop, Constants.CASH_SALE_ACH_SEC_CODE, SecCode);
			// commonUtil.clickElementByXPath(driver, prop,
			// Constants.CASH_SALE_ACH_PAYMENT_OPERATION);
			// commonUtil.clickElementByXPath(driver, prop,
			// Constants.CASH_SALE_ACH_SEC_CODE);
			commonUtil.scrollToBottom(driver);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_10);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			if (commonUtil.isAlertPresent(driver)) {
				commonUtil.acceptAlert(driver);
			}
			commonUtil.scrollToBottom(driver);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
			saleId = commonUtil.getOrderId(driver, Constants.STRING_SALE);
			commonUtil.renameFolder(screenShotPath, Constants.STRING_SALE, saleId);
			if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.PROCESS_TYPE_REST))) {
				pnReferenceNumber = commonUtil.checkRestActuals(driver, Constants.STRING_SALE, ReasonCode,
						TestScenario);
			} else {
				pnReferenceNumber = commonUtil.checkActuals(driver, Constants.STRING_SALE,
						prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
						prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
			}
			commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), saleId);
			commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);
		}
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.CUSTOMER_REF);
		int i = 0;
		String refundId = null;

		executor = (JavascriptExecutor) driver;
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_AUTHORIZE_RETURN);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_APPROVE_RETURN);
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_RECEIVE_RETURN);
		itemDetails = commonUtil.getItemDetails(Items);
		if (Constants.YES.equals(HasMultipleItem.toUpperCase())) {
			for (i = 0; i < itemDetails.length; i++) {
				commonUtil.setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, (i + 1),
						Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
			}
		} else {
			commonUtil.setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, 1,
					Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
		}
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.checkUrlTitle(driver, prop, Constants.ITEM_RECEIPT_PAGE_TITLE);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.REFUND_CREATED_FROM);

		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
		commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_LOCATION, Location);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
		commonUtil.checkUrlTitle(driver, prop, Constants.CUSTOMER_REFUND_PAGE_TITLE);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_10);
		if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.VIRTUAL_MACHINE_RUN))) {
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		}
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_10);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_REFUND_METHOD_TAB);

		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		refundId = commonUtil.getOrderId(driver, Constants.STRING_SALE);
		commonUtil.renameFolder(screenShotPath, Constants.CUSTOMER_REF, refundId);
		commonUtil.addLogToTestNGReport(Constants.REFUND, commonUtil.getCurrentUrl(driver), refundId);

		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.REFUND,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		commonUtil.addLogToTestNGReport(Constants.REFUND, pnReferenceNumber);

		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		logger.info("[executeACHSaleCustomerRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute a refund service after an ACH sale service
	 * @param CustomerName String containing name of customer placing order
	 * @param Memo String containing memo of the order
	 * @param ItemName String containing item name to be ordered
	 * @param Quantity String containing quantity of item to be ordered
	 * @param Amount String containing rate amount of item to be ordered
	 * @param Taxcode String containing tax code of item to be ordered
	 * @param Location String containing order location
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CSC String containing security code of card used for transaction
	 * @param PPP String containing payment processing profile used
	 * @param AVSConfiguration String containing AVS configuration setup
	 * @param DeclineAVSFlag String containing AVS decline configuration setup
	 * @param NoAVSMatch String containing AVS not match configuration setup
	 * @param AVSServiceNotAvailable String containing AVS service unavailable configuration setup
	 * @param PartialAVSMatch String containing AVS partial match configuration setup
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param AVSCode String containing AVS code of transaction
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	private void executeAVS(String CustomerName, String Memo, String ItemName, String Quantity, String Amount,
			String Taxcode, String Location, String PaymentMethod, String CSC, String PPP, String AVSConfiguration,
			String DeclineAVSFlag, String NoAVSMatch, String AVSServiceNotAvailable, String PartialAVSMatch,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String AVSCode, String TestScenario)
			throws Exception {
		logger.info("[executeAVS] Starts");
		driver = null;
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALES_ORDER_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		commonUtil.setPaymentProcessingProfileAVS(driver, userIndex, PPP, AVSConfiguration, DeclineAVSFlag, NoAVSMatch,
				AVSServiceNotAvailable, PartialAVSMatch);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALES_ORDER_URL, userIndex);
		executor = (JavascriptExecutor) driver;
		if (Constants.POSITIVE.toUpperCase().equals(TestScenario.toUpperCase())) {
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);

			if (Constants.NO.equalsIgnoreCase(prop.getProperty(Constants.CUSTOMER_ID_PRESENT))) {
				commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.DOWN,
						Keys.ENTER);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			}
			// wait until customer is set
			for (int i = 0; i < browserMaxTimeoutSeconds; i++) {
				logger.info("[executeAVS] shipping cost value: "
						+ commonUtil.getElementTextByXPath(driver, prop, Constants.SALES_ORDER_SUMMARY_SHIPPING_COST));
				if (commonUtil.getElementTextByXPath(driver, prop,
						Constants.SALES_ORDER_SUMMARY_SHIPPING_COST) != null) {
					break;
				} else {
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				}
			}
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Memo);
			commonUtil.takeScreenShot(driver, screenShotPath, Constants.CUSTOMER_DETAILS_ENTRY);

			commonUtil.setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_PREFIX, ItemName,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.ifAlertPresentAccept(driver);
			commonUtil.ifAlertPresentAccept(driver);
			commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_QUANTITY_PREFIX, Quantity,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_RATE_PREFIX, Amount,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			executor.executeScript(prop.getProperty(Constants.NLAPI_COMMIT_LINE_ITEM));
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.ITEM_DETAILS_ENTRY);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Constants.EMPTY_STRING);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.setFieldText(driver, Constants.NLAPI_FIELD_TEXT_PREFIX_PAYMENTOPTION, PaymentMethod,
					Constants.NLAPI_FIELD_TEXT_SUFFIX);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_PROCESSING_PROFILE, PPP);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_BILL_CSC, CSC);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			commonUtil.ifAlertPresentAccept(driver);
			commonUtil.scrollToBottom(driver);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);
			commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);

			authOrderId = commonUtil.getOrderId(driver, Constants.AUTHORIZATION);
			commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, commonUtil.getCurrentUrl(driver), authOrderId);

			pnReferenceNumber = commonUtil.checkAvsActuals(driver, Constants.AUTHORIZATION,
					prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
					prop.getProperty(Constants.AVS_CODE), prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH),
					ReasonCode, DecisionMsg, TestScenario, AVSCode);
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, pnReferenceNumber);

			if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
				commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
			}
		}
		screenShotPath = screenShotModulePath;
		logger.info("[executeAVS] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute CVN validation after a transaction
	 * @param CustomerName String containing name of customer placing order
	 * @param Memo String containing memo of the order
	 * @param ItemName String containing item name to be ordered
	 * @param Quantity String containing quantity of item to be ordered
	 * @param Amount String containing rate amount of item to be ordered
	 * @param Taxcode String containing tax code of item to be ordered
	 * @param Location String containing order location
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CSC String containing security code of card used for transaction
	 * @param PPP String containing payment processing profile used
	 * @param CVNConfiguration String containing CVN configuration setup
	 * @param CSCNotSubmitted String containing CSC not submitted configuration setup
	 * @param CSCNotSubmittedByCardholderBank String containing CSC not submitted by cardholder bank configuration setup
	 * @param CSCServiceNotAvailable String containing CSC service not available configuration setup
	 * @param CSCCheckFailed String containing CSC check failed configuration setup
	 * @param NoCSCMatch String containing CSC not matching configuration setup
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param CVNCode String containing CVN code of the transaction
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	private void executeCVN(String CustomerName, String Memo, String ItemName, String Quantity, String Amount,
			String Taxcode, String Location, String PaymentMethod, String CSC, String PPP, String CVNConfiguration,
			String CSCNotSubmitted, String CSCNotSubmittedByCardholderBank, String CSCServiceNotAvailable,
			String CSCCheckFailed, String NoCSCMatch, String RequireEBCCheck, String ReasonCode, String DecisionMsg,
			String CVNCode, String TestScenario) throws Exception {
		logger.info("[executeCVN] Starts");
		driver = null;
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALES_ORDER_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		commonUtil.setPaymentProcessingProfileCVN(driver, userIndex, PPP, CVNConfiguration, CSCNotSubmitted,
				CSCNotSubmittedByCardholderBank, CSCServiceNotAvailable, CSCCheckFailed, NoCSCMatch);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALES_ORDER_URL, userIndex);
		executor = (JavascriptExecutor) driver;
		if (Constants.POSITIVE.toUpperCase().equals(TestScenario.toUpperCase())) {
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);

			if (Constants.NO.equalsIgnoreCase(prop.getProperty(Constants.CUSTOMER_ID_PRESENT))) {
				commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.DOWN,
						Keys.ENTER);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			}
			// wait until customer is set
			for (int i = 0; i < browserMaxTimeoutSeconds; i++) {
				logger.info("[executeCVN] shipping cost value: "
						+ commonUtil.getElementTextByXPath(driver, prop, Constants.SALES_ORDER_SUMMARY_SHIPPING_COST));
				if (commonUtil.getElementTextByXPath(driver, prop,
						Constants.SALES_ORDER_SUMMARY_SHIPPING_COST) != null) {
					break;
				} else {
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				}
			}
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Memo);
			commonUtil.takeScreenShot(driver, screenShotPath, Constants.CUSTOMER_DETAILS_ENTRY);

			commonUtil.setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_PREFIX, ItemName,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.ifAlertPresentAccept(driver);
			commonUtil.ifAlertPresentAccept(driver);
			commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_QUANTITY_PREFIX, Quantity,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_RATE_PREFIX, Amount,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			executor.executeScript(prop.getProperty(Constants.NLAPI_COMMIT_LINE_ITEM));
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.ITEM_DETAILS_ENTRY);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Constants.EMPTY_STRING);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.setFieldText(driver, Constants.NLAPI_FIELD_TEXT_PREFIX_PAYMENTOPTION, PaymentMethod,
					Constants.NLAPI_FIELD_TEXT_SUFFIX);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_PROCESSING_PROFILE, PPP);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_BILL_CSC, CSC);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			commonUtil.ifAlertPresentAccept(driver);

			commonUtil.scrollToBottom(driver);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);
			commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);

			authOrderId = commonUtil.getOrderId(driver, Constants.AUTHORIZATION);
			commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, commonUtil.getCurrentUrl(driver), authOrderId);

			pnReferenceNumber = commonUtil.checkCvnActuals(driver, Constants.AUTHORIZATION,
					prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
					prop.getProperty(Constants.CVN_CODE), prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH),
					ReasonCode, DecisionMsg, TestScenario, CVNCode);
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, pnReferenceNumber);

			if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
				commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
			}
		}
		screenShotPath = screenShotModulePath;
		logger.info("[executeCVN] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This methods is used to validate AVS code for a sale transaction
	 * @param CustomerName String containing name of customer placing order
	 * @param Memo String containing memo of the order
	 * @param ItemName String containing item name to be ordered
	 * @param Quantity String containing quantity of item to be ordered
	 * @param Amount String containing rate amount of item to be ordered
	 * @param Taxcode String containing tax code of item to be ordered
	 * @param Location String containing order location
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CSC String containing security code of card used for transaction
	 * @param PPP String containing payment processing profile used
	 * @param AVSConfiguration String containing AVS configuration setup
	 * @param DeclineAVSFlag String containing AVS decline configuration setup
	 * @param NoAVSMatch String containing AVS not match configuration setup
	 * @param AVSServiceNotAvailable String containing AVS service unavailable configuration setup
	 * @param PartialAVSMatch String containing AVS partial match configuration setup
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param AVSCode String containing AVS code of transaction
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	private void executeAVSSale(String CustomerName, String Memo, String ItemName, String Quantity, String Amount,
			String Taxcode, String Location, String PaymentMethod, String CSC, String PPP, String AVSConfiguration,
			String DeclineAVSFlag, String NoAVSMatch, String AVSServiceNotAvailable, String PartialAVSMatch,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String AVSCode, String TestScenario)
			throws Exception {
		logger.info("[executeAVSSale] Starts");
		driver = null;
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALE_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		commonUtil.setPaymentProcessingProfileAVS(driver, userIndex, PPP, AVSConfiguration, DeclineAVSFlag, NoAVSMatch,
				AVSServiceNotAvailable, PartialAVSMatch);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALE_URL, userIndex);
		executor = (JavascriptExecutor) driver;
		if (Constants.POSITIVE.toUpperCase().equals(TestScenario.toUpperCase())) {
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);

			if (Constants.NO.equalsIgnoreCase(prop.getProperty(Constants.CUSTOMER_ID_PRESENT))) {
				commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.DOWN,
						Keys.ENTER);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			}
			// wait until customer is set
			for (int i = 0; i < browserMaxTimeoutSeconds; i++) {
				logger.info("[executeAVSSale] shipping cost value: "
						+ commonUtil.getElementTextByXPath(driver, prop, Constants.SALES_ORDER_SUMMARY_SHIPPING_COST));
				if (commonUtil.getElementTextByXPath(driver, prop,
						Constants.SALES_ORDER_SUMMARY_SHIPPING_COST) != null) {
					break;
				} else {
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				}
			}
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Memo);
			commonUtil.takeScreenShot(driver, screenShotPath, Constants.CUSTOMER_DETAILS_ENTRY);

			commonUtil.setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_PREFIX, ItemName,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.ifAlertPresentAccept(driver);
			commonUtil.ifAlertPresentAccept(driver);
			commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_QUANTITY_PREFIX, Quantity,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_RATE_PREFIX, Amount,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			executor.executeScript(prop.getProperty(Constants.NLAPI_COMMIT_LINE_ITEM));
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.ITEM_DETAILS_ENTRY);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_LOCATION, Location);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);

			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.setFieldText(driver, Constants.NLAPI_FIELD_TEXT_PREFIX_PAYMENTOPTION, PaymentMethod,
					Constants.NLAPI_FIELD_TEXT_SUFFIX);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_PROCESSING_PROFILE, PPP);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_BILL_CSC, CSC);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_10);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			commonUtil.ifAlertPresentAccept(driver);

			// checking for error
			if (commonUtil.getElementTextByXPath(driver, prop, Constants.CASH_SALE_ERROR)
					.equals(Constants.STRING_ERROR)) {
				driver = commonUtil.setupDriver(driver, prop, Constants.PAYMENT_EVENTS_URL, userIndex);
				WebElement div = commonUtil.findElementById(driver, prop, Constants.PAYMENT_EVENTS_TABLE_ID);
				WebElement table = div.findElement(By.xpath(".//*"));
				List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
				List<WebElement> cells = null;
				String paymentReason = null;
				String paymentProfile = null;
				for (WebElement row : allRows) {
					cells = row.findElements(By.tagName(Constants.TD));
					paymentProfile = cells.get(5).getText();
					paymentReason = cells.get(9).getText();
					if ((paymentProfile.equalsIgnoreCase(PPP))
							&& ((paymentReason.equalsIgnoreCase(Constants.STRING_VERIFICATION_REJECTION))
									|| (paymentReason.equalsIgnoreCase(Constants.STRING_GENERAL_REJECT)))) {
						cells.get(0).click();
						break;
					}
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
				if (!Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.PROCESS_TYPE_REST))) {
					WebElement element = commonUtil.findElementByXPath(driver, prop, Constants.RAW_RESPONSE_TEXT_FIELD);
					String[] lines = element.getText().trim().split("\\R");
					logger.info("[executeAVSSale] response length: " + lines.length);
					for (int i = 0; i < lines.length; i++) {
						if ((lines[i].contains(prop.getProperty(Constants.SOAP_DECISION_MESSAGE)))
								|| (lines[i].contains(prop.getProperty(Constants.SOAP_SA_DECISION_MESSAGE)))) {
							Assert.assertEquals(lines[i], DecisionMsg);
						}
						if ((lines[i].contains(prop.getProperty(Constants.SOAP_REASON_CODE)))
								|| (lines[i].contains(prop.getProperty(Constants.SOAP_SA_REASON_CODE)))) {
							Assert.assertEquals(lines[i], ReasonCode);
						}
					}
					pnReferenceNumber = commonUtil.getElementTextByXPath(driver, prop,
							Constants.PNREFERENCE_NUMBER_XPATH);
					commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);
				}
			} else {
				commonUtil.scrollToBottom(driver);
				commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
				commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);

				saleId = commonUtil.getOrderId(driver, Constants.STRING_SALE);
				commonUtil.renameFolder(screenShotPath, Constants.STRING_SALE, saleId);
				commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), saleId);

				pnReferenceNumber = commonUtil.checkAvsActuals(driver, Constants.STRING_SALE,
						prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
						prop.getProperty(Constants.AVS_CODE), prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH),
						ReasonCode, DecisionMsg, TestScenario, AVSCode);
				commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);

				if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
					commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
				}
			}
		}
		screenShotPath = screenShotModulePath;
		logger.info("[executeAVSSale] Ends");

	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This methods is used to validate CVN code for a sale transaction
	 * @param CustomerName String containing name of customer placing order
	 * @param Memo String containing memo of the order
	 * @param ItemName String containing item name to be ordered
	 * @param Quantity String containing quantity of item to be ordered
	 * @param Amount String containing rate amount of item to be ordered
	 * @param Taxcode String containing tax code of item to be ordered
	 * @param Location String containing order location
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CardSecurityCode String containing security code of card used for transaction
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param CVNConfiguration String containing CVN configuration setup
	 * @param CSCNotSubmitted String containing CSC not submitted configuration setup
	 * @param CSCNotSubmittedByCardholderBank String containing CSC not submitted by cardholder bank configuration setup
	 * @param CSCServiceNotAvailable String containing CSC service not available configuration setup
	 * @param CSCCheckFailed String containing CSC check failed configuration setup
	 * @param NoCSCMatch String containing CSC not matching configuration setup
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param CVNCode String containing CVN code of the transaction
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	private void executeCVNSale(String CustomerName, String Memo, String ItemName, String Quantity, String Amount,
			String Taxcode, String Location, String PaymentMethod, String CardSecurityCode,
			String PaymentProcessingProfile, String CVNConfiguration, String CSCNotSubmitted,
			String CSCNotSubmittedByCardholderBank, String CSCServiceNotAvailable, String CSCCheckFailed,
			String NoCSCMatch, String RequireEBCCheck, String ReasonCode, String DecisionMsg, String CVNCode,
			String TestScenario) throws Exception {
		logger.info("[executeCVNSale] Starts");
		driver = null;
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALE_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		commonUtil.setPaymentProcessingProfileCVN(driver, userIndex, PaymentProcessingProfile, CVNConfiguration,
				CSCNotSubmitted, CSCNotSubmittedByCardholderBank, CSCServiceNotAvailable, CSCCheckFailed, NoCSCMatch);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALE_URL, userIndex);
		executor = (JavascriptExecutor) driver;
		if (Constants.POSITIVE.toUpperCase().equals(TestScenario.toUpperCase())) {
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);

			if (Constants.NO.equalsIgnoreCase(prop.getProperty(Constants.CUSTOMER_ID_PRESENT))) {
				commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.DOWN,
						Keys.ENTER);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			}
			// wait until customer is set
			for (int i = 0; i < browserMaxTimeoutSeconds; i++) {
				logger.info("[executeCVNSale] shipping cost value: "
						+ commonUtil.getElementTextByXPath(driver, prop, Constants.SALES_ORDER_SUMMARY_SHIPPING_COST));
				if (commonUtil.getElementTextByXPath(driver, prop,
						Constants.SALES_ORDER_SUMMARY_SHIPPING_COST) != null) {
					break;
				} else {
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				}
			}
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_MEMO, Memo);
			commonUtil.takeScreenShot(driver, screenShotPath, Constants.CUSTOMER_DETAILS_ENTRY);

			commonUtil.setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_PREFIX, ItemName,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.ifAlertPresentAccept(driver);
			commonUtil.ifAlertPresentAccept(driver);
			commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_QUANTITY_PREFIX, Quantity,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			commonUtil.setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_RATE_PREFIX, Amount,
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			executor.executeScript(prop.getProperty(Constants.NLAPI_COMMIT_LINE_ITEM));
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.ITEM_DETAILS_ENTRY);
			commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_LOCATION).sendKeys(Keys.DOWN);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_LOCATION, Location);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);

			commonUtil.setFieldText(driver, Constants.NLAPI_FIELD_TEXT_PREFIX_PAYMENTOPTION, PaymentMethod,
					Constants.NLAPI_FIELD_TEXT_SUFFIX);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_PROCESSING_PROFILE,
					PaymentProcessingProfile);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_BILL_CSC, CardSecurityCode);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			commonUtil.ifAlertPresentAccept(driver);
			// checking for error
			if (commonUtil.getElementTextByXPath(driver, prop, Constants.CASH_SALE_ERROR)
					.equals(Constants.STRING_ERROR)) {
				driver = commonUtil.setupDriver(driver, prop, Constants.PAYMENT_EVENTS_URL, userIndex);
				WebElement div = commonUtil.findElementById(driver, prop, Constants.PAYMENT_EVENTS_TABLE_ID);
				WebElement table = div.findElement(By.xpath(".//*"));
				List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
				List<WebElement> cells = null;
				String paymentReason = null;
				String paymentProfile = null;
				for (WebElement row : allRows) {
					cells = row.findElements(By.tagName(Constants.TD));
					paymentProfile = cells.get(5).getText();
					paymentReason = cells.get(9).getText();
					if ((paymentProfile.equalsIgnoreCase(PaymentProcessingProfile))
							&& ((paymentReason.equalsIgnoreCase(Constants.STRING_VERIFICATION_REJECTION))
									|| (paymentReason.equalsIgnoreCase(Constants.STRING_GENERAL_REJECT)))) {
						cells.get(0).click();
						break;
					}
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
				if (!Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.PROCESS_TYPE_REST))) {
					WebElement element = commonUtil.findElementByXPath(driver, prop, Constants.RAW_RESPONSE_TEXT_FIELD);
					String[] lines = element.getText().trim().split("\\R");
					logger.info("[executeCVNSale] response length: " + lines.length);
					for (int i = 0; i < lines.length; i++) {
						if ((lines[i].contains(prop.getProperty(Constants.SOAP_DECISION_MESSAGE)))
								|| (lines[i].contains(prop.getProperty(Constants.SOAP_SA_DECISION_MESSAGE)))) {
							Assert.assertEquals(lines[i], DecisionMsg);
						}
						if ((lines[i].contains(prop.getProperty(Constants.SOAP_REASON_CODE)))
								|| (lines[i].contains(prop.getProperty(Constants.SOAP_SA_REASON_CODE)))) {
							Assert.assertEquals(lines[i], ReasonCode);
						}
					}
					pnReferenceNumber = commonUtil.getElementTextByXPath(driver, prop,
							Constants.PNREFERENCE_NUMBER_XPATH);
					commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);
				}
			} else {
				commonUtil.scrollToBottom(driver);
				commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
				commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);

				saleId = commonUtil.getOrderId(driver, Constants.STRING_SALE);
				commonUtil.renameFolder(screenShotPath, Constants.STRING_SALE, saleId);
				commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), saleId);

				pnReferenceNumber = commonUtil.checkAvsActuals(driver, Constants.STRING_SALE,
						prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
						prop.getProperty(Constants.AVS_CODE), prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH),
						ReasonCode, DecisionMsg, TestScenario, CVNCode);
				commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);

				if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
					commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
				}
			}
		}
		screenShotPath = screenShotModulePath;
		logger.info("[executeCVNSale] Ends");
	}

}