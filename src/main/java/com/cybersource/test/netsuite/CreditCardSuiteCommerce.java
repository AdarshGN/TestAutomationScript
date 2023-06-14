package com.cybersource.test.netsuite;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.cybersource.test.CommonTest;
import com.cybersource.test.util.CommonUtil;
import com.cybersource.test.util.Constants;
import com.cybersource.test.util.FileUtil;
import com.cybersource.test.util.Helper;
import com.cybersource.test.util.RetryFailedTestCases;

@Listeners(com.cybersource.test.util.Listeners.class)
/**
 * CreditCardSuiteCommerce class which will have all functionality used to place order and do related services using Credit card suite commerce website
 * @author AD20243779
 *
 */
public class CreditCardSuiteCommerce extends CommonTest {
	CommonUtil commonUtil = new CommonUtil();
	ConfigurePaymentProcessingProfile configurePaymentProcessingProfile = new ConfigurePaymentProcessingProfile();
	boolean isDirectCaptureService = false;
	boolean isDirectRefundService = false;
	boolean isDirectVoid = false;
	boolean isDirectCustomerRefund = false;
	String orderNumber = null;
	String pnReferenceNumber = null;
	String screenShotPath = null;
	String screenShotModulePath = null;
	WebDriver driver = null;
	String userIndex = "1";
	JavascriptExecutor executor = null;
	final static Logger logger = Logger.getLogger(CreditCardSuiteCommerce.class);

	@BeforeTest
	/**
	 * This method is used to initialize test environment before running a test
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @throws IOException Handles when an I/O exception of some sort has occurred
	 */
	public void init(ITestContext context) throws IOException {
		super.loadCommonConfig(context);
		screenShotModulePath = commonUtil.getScreenshotsModulePath(Constants.CREDIT_CARD_SC);
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
		String serviceType = context.getCurrentXmlTest().getParameter(Constants.SERVICE_TYPE);
		if (Constants.DIRECT_CAPTURE.equals(serviceType)) {
			isDirectCaptureService = true;
		} else if (Constants.DIRECT_REFUND.equals(serviceType)) {
			isDirectRefundService = true;
		} else if (Constants.DIRECT_CUSTOMER_REFUND.equals(serviceType)) {
			isDirectCustomerRefund = true;
		} else if (Constants.DIRECT_VOID.equals(serviceType)) {
			isDirectVoid = true;
		}
		data = FileUtil.getSheetData(XLSX_FILE_PATH,
				prop.getProperty(prefix + currentScenario + Constants._SHEET_NAME));
		logger.info("[CreditCardSuiteCommerce][getData]  currentScenario:" + currentScenario);
		return data;
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute capture service after authorization
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardSecurityCode String containing security code of credit card used for transaction
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
	public void executeAuthCapture(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String Location, String CardSecurityCode, String AuthorderIdForDirectCapture,
			String AuthorderIdForDirectVoid, String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSuiteCommerce][executeAuthCapture] starts");
		executeAuth(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, Location, CardSecurityCode,
				AuthorderIdForDirectCapture, AuthorderIdForDirectVoid, CaptureIdForDirectRefund,
				CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		executeCapture(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, Location, CardSecurityCode,
				AuthorderIdForDirectCapture, AuthorderIdForDirectVoid, CaptureIdForDirectRefund,
				CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		logger.info("[CreditCardSuiteCommerce][executeAuthCapture] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund service after authorization and capture services
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardSecurityCode String containing security code of credit card used for transaction
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
	public void executeAuthCaptureRefund(String Email, String Password, String ItemName, String Items,
			String HasMultipleItem, String ShippingMethod, String Location, String CardSecurityCode,
			String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid, String CaptureIdForDirectRefund,
			String CaptureIdForCustomerRefund, String RequireEBCCheck, String ReasonCode, String DecisionMsg,
			String TestScenario) throws Exception {
		logger.info("[CreditCardSuiteCommerce][executeAuthCaptureRefund] starts");
		executeAuth(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, Location, CardSecurityCode,
				AuthorderIdForDirectCapture, AuthorderIdForDirectVoid, CaptureIdForDirectRefund,
				CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		executeCapture(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, Location, CardSecurityCode,
				AuthorderIdForDirectCapture, AuthorderIdForDirectVoid, CaptureIdForDirectRefund,
				CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		executeRefund(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, Location, CardSecurityCode,
				AuthorderIdForDirectCapture, AuthorderIdForDirectVoid, CaptureIdForDirectRefund,
				CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		logger.info("[CreditCardSiteBuilder][executeAuthCaptureRefund] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute customer refund service after authorization and capture services
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardSecurityCode String containing security code of credit card used for transaction
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
	public void executeAuthCaptureCustomerRefund(String Email, String Password, String ItemName, String Items,
			String HasMultipleItem, String ShippingMethod, String Location, String CardSecurityCode,
			String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid, String CaptureIdForDirectRefund,
			String CaptureIdForCustomerRefund, String RequireEBCCheck, String ReasonCode, String DecisionMsg,
			String TestScenario) throws Exception {
		logger.info("[CreditCardSuiteCommerce][executeAuthCaptureCustomerRefund] starts");
		// ToDo: OpenIssueSDN: CustomerRefund has issue from NetSuite side
		// Assert.assertFalse("OpenIssueSDN: CustomerRefund has issue from NetSuite
		// side", true);
		executeAuth(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, Location, CardSecurityCode,
				AuthorderIdForDirectCapture, AuthorderIdForDirectVoid, CaptureIdForDirectRefund,
				CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		executeCapture(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, Location, CardSecurityCode,
				AuthorderIdForDirectCapture, AuthorderIdForDirectVoid, CaptureIdForDirectRefund,
				CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		executeCustomerRefund(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, Location,
				CardSecurityCode, AuthorderIdForDirectCapture, AuthorderIdForDirectVoid, CaptureIdForDirectRefund,
				CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		logger.info("[CreditCardSiteBuilder][executeAuthCaptureCustomerRefund] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute void service after authorization
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardSecurityCode String containing security code of credit card used for transaction
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
	public void executeAuthVoid(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String Location, String CardSecurityCode, String AuthorderIdForDirectCapture,
			String AuthorderIdForDirectVoid, String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSiteBuilder][executeAuthVoid] starts");
		executeAuth(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, Location, CardSecurityCode,
				AuthorderIdForDirectCapture, AuthorderIdForDirectVoid, CaptureIdForDirectRefund,
				CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		executeVoid(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, Location, CardSecurityCode,
				AuthorderIdForDirectCapture, AuthorderIdForDirectVoid, CaptureIdForDirectRefund,
				CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		logger.info("[CreditCardSiteBuilder][executeAuthVoid] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute authorization service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardSecurityCode String containing security code of credit card used for transaction
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
	public void executeAuth(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String Location, String CardSecurityCode, String AuthorderIdForDirectCapture,
			String AuthorderIdForDirectVoid, String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSuiteCommerce][executeAuth] starts");
		String authOrderId = null;
		String orderNumber = null;
		String childWindow = null;
		String mainWindow = null;
		Set<String> allWindows = null;
		Iterator<String> itr = null;
		Object itemDetails[][] = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		configurePaymentProcessingProfile.enableGatewayRequestAuthorization(driver, Constants.PAYMENT_INTEGRATION);
		commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_WEBSITES_PREVIEW);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		executor = (JavascriptExecutor) driver;
		mainWindow = driver.getWindowHandle();
		allWindows = driver.getWindowHandles();
		itr = allWindows.iterator();
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				commonUtil.switchToWindow(driver, childWindow);
				commonUtil.maximizeWindow(driver);
				commonUtil.refreshCurrentPage(driver);
				if (Constants.WELCOME_TO_THE_STORE.equals(driver.getTitle()) && commonUtil
						.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN).isDisplayed()) {
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WELCOME_USER);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SIGN_OUT);
				}
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN);
				commonUtil.setElementValueByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN_EMAIL, Email);
				commonUtil.setElementValueByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN_PASSWORD,
						Password);
				executor.executeScript("jQuery('.login-register-login-submit').trigger('click');");
				Helper.waitForElementToBeVisible(driver,
						commonUtil.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WISHLIST));
				if (Constants.YES.equals(HasMultipleItem.toUpperCase())) {
					itemDetails = commonUtil.getItemDetails(Items);
					for (int i = 0; i < itemDetails.length; i++) {
						int j = 0;
						commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SHOP);
						commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
						commonUtil.clickElementByLinkText(driver, itemDetails[i][j].toString());
						commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_ADD_TO_CART);
						commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
						if (i != (itemDetails.length - 1)) {
							commonUtil.clickElementByXPath(driver, prop,
									Constants.SUITE_COMMERCE_ADVANCED_CONTINUE_SHOPPING);
						} else {
							commonUtil.clickElementByXPath(driver, prop,
									Constants.SUITE_COMMERCE_ADVANCED_VIEW_CART_CHECKOUT);
						}
					}
				} else {
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SHOP);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
					commonUtil.clickElementByLinkText(driver, ItemName);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_ADD_TO_CART);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_VIEW_CART_CHECKOUT);
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.CART_DETAILS);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PROCEED_TO_CHECKOUT);
				commonUtil.setShippingMethodSCA(driver, ShippingMethod);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
				commonUtil.clickElementByLinkText(driver, Constants.CREDIT_DEBIT_CARD);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.PAYMENT_METHOD);
				executor.executeScript("jQuery('.creditcard-selector-option').trigger('click')");
				commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
				// continue button is not clicked in SCA Auth After selecting SHipping Method
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PLACE_ORDER);
				WebElement orderConfirmationNumber = commonUtil.findElementByXPath(driver, prop,
						Constants.SUITE_COMMERCE_ADVANCED_ORDER_CONFIRMATION_NUMBER);
				if (null != orderConfirmationNumber) {
					orderNumber = StringUtils.substringAfterLast(orderConfirmationNumber.getText(), Constants.HASH);
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.refreshCurrentPage(driver);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WELCOME_USER);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SIGN_OUT);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.closeCurrentWindow(driver);
			}
		}
		commonUtil.switchToWindow(driver, mainWindow);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber, Keys.ENTER);
		commonUtil.writeCurrentTransactionUrl(Constants.AUTH_ORDERID_URL, commonUtil.getCurrentUrl(driver));
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.scrollToBottom(driver);
		commonUtil.clickLinkByXPath(driver, prop, Constants.PAYMENT_EVENT_TAB);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		authOrderId = commonUtil.getOrderId(driver, Constants.AUTHORIZATION);
		commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.AUTHORIZATION,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		// pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
		// prop.getProperty(Constants.PI_DISBLED_PN_REFERENCE_NUMBER));
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, pnReferenceNumber);
		commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, commonUtil.getCurrentUrl(driver), authOrderId);
		logger.info("[CreditCardSuiteCommerce][executeAuth] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute capture service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardSecurityCode String containing security code of credit card used for transaction
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
	public void executeCapture(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String Location, String CardSecurityCode, String AuthorderIdForDirectCapture,
			String AuthorderIdForDirectVoid, String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSuiteCommerce][executeCapture] starts");
		String captureId = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.CAPTURE);
		if (isDirectCaptureService) {
			driver = commonUtil.setupDriverDirectUrl(driver, prop, AuthorderIdForDirectCapture, null);
		}
		executor = (JavascriptExecutor) driver;
		executor.executeScript(prop.getProperty(Constants.SALES_ORDER_APPROVE));
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_FULFILL);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.ORDER_FULLFIL);
		Object itemDetails[][] = null;
		if (Constants.YES.equals(HasMultipleItem.toUpperCase())) {
			itemDetails = commonUtil.getItemDetails(Items);
			for (int i = 0; i < itemDetails.length; i++) {
				commonUtil.setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, (i + 1),
						Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
			}
		} else {
			commonUtil.setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, 1,
					Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		}
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		// Sales Order- Mark Packed button
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_PACKED);
		// Sales Order- Mark Shipped button
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_PACKED);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_BILL);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_OK_BUTTON);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.ORDER_CAPTURE);
		commonUtil.writeCurrentTransactionUrl(Constants.CAPTURE_ORDERID_URL, commonUtil.getCurrentUrl(driver));
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		captureId = commonUtil.getOrderId(driver, Constants.CAPTURE_AUTHORIZATION);
		commonUtil.renameFolder(screenShotPath, Constants.CAPTURE, captureId);
		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.CAPTURE_AUTHORIZATION,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
				prop.getProperty(Constants.PI_DISBLED_PN_REFERENCE_NUMBER));
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.CAPTURE, pnReferenceNumber);
		commonUtil.addLogToTestNGReport(Constants.CAPTURE, commonUtil.getCurrentUrl(driver), captureId);
		logger.info("[CreditCardSuiteCommerce][executeCapture] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardSecurityCode String containing security code of credit card used for transaction
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
	public void executeRefund(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String Location, String CardSecurityCode, String AuthorderIdForDirectCapture,
			String AuthorderIdForDirectVoid, String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSuiteCommerce][executeRefund] starts");
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.REFUND);
		String refundId = null;
		if (isDirectRefundService) {
			driver = commonUtil.setupDriverDirectUrl(driver, prop, CaptureIdForDirectRefund, null);
		}
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
		commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_LOCATION).sendKeys(Keys.DOWN, Keys.DOWN,
				Keys.ENTER);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_OK_BUTTON);
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		refundId = commonUtil.getOrderId(driver, Constants.REFUND);
		commonUtil.renameFolder(screenShotPath, Constants.REFUND, refundId);
		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.REFUND,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
				prop.getProperty(Constants.PI_ENABLED_PN_REFERENCE_NUMBER));
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.REFUND, pnReferenceNumber);
		commonUtil.addLogToTestNGReport(Constants.REFUND, commonUtil.getCurrentUrl(driver), refundId);
		logger.info("[CreditCardSuiteCommerce][executeRefund] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute customer refund service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardSecurityCode String containing security code of credit card used for transaction
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
	public void executeCustomerRefund(String Email, String Password, String ItemName, String Items,
			String HasMultipleItem, String ShippingMethod, String Location, String CardSecurityCode,
			String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid, String CaptureIdForDirectRefund,
			String CaptureIdForCustomerRefund, String RequireEBCCheck, String ReasonCode, String DecisionMsg,
			String TestScenario) throws Exception {
		logger.info("[CreditCardSuiteCommerce][executeCustomerRefund] starts");
		String refundId = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.CUSTOMER_REF);
		if (isDirectCustomerRefund) {
			driver = commonUtil.setupDriverDirectUrl(driver, prop, CaptureIdForCustomerRefund, null);
		}
		executor = (JavascriptExecutor) driver;
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_AUTHORIZE_RETURN);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_APPROVE_RETURN);
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_RECEIVE_RETURN);
		commonUtil.setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, 1,
				Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		executor.executeScript(prop.getProperty(Constants.REFUND_CREATED_FROM));
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
		commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_LOCATION, Location);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_OK_BUTTON);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_REFUND_METHOD_TAB);
		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		refundId = commonUtil.getOrderId(driver, Constants.REFUND);
		commonUtil.renameFolder(screenShotPath, Constants.CUSTOMER_REF, refundId);
		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.REFUND,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
				prop.getProperty(Constants.PI_DISBLED_PN_REFERENCE_NUMBER));
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.REFUND, pnReferenceNumber);
		commonUtil.addLogToTestNGReport(Constants.REFUND, commonUtil.getCurrentUrl(driver), refundId);
		logger.info("[CreditCardSuiteCommerce][executeCustomerRefund] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute void service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param AuthorderIdForDirectCapture String contain auth order id for direct capture
	 * @param AuthorderIdForDirectVoid String contain auth order id for direct void
	 * @param CaptureIdForDirectRefund String contain auth order id for direct refund
	 * @param CaptureIdForCustomerRefund String containing capture id to be used for customer refund service
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 * @throws Exception
	 */
	public void executeVoid(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String Location, String CardSecurityCode, String AuthorderIdForDirectCapture,
			String AuthorderIdForDirectVoid, String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSuiteCommerce][executeCancel] starts");
		String voidId = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.VOID);
		if (isDirectVoid) {
			driver = commonUtil.setupDriverDirectUrl(driver, prop, AuthorderIdForDirectVoid, null);
		}
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_CANCEL_ORDER);
		commonUtil.switchToFrame(driver, Constants.THREE);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_CANCEL_YES_BUTTON);
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		voidId = commonUtil.getOrderId(driver, Constants.VOID_AUTHORIZATION);
		commonUtil.renameFolder(screenShotPath, Constants.VOID, voidId);
		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.VOID_AUTHORIZATION,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
				prop.getProperty(Constants.PI_DISBLED_PN_REFERENCE_NUMBER));
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.VOID, pnReferenceNumber);
		commonUtil.addLogToTestNGReport(Constants.VOID, commonUtil.getCurrentUrl(driver), voidId);
		logger.info("[CreditCardSuiteCommerce][executeCancel] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute sale service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardSecurityCode String containing security code of credit card used for transaction
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
	public void executeSale(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String Location, String CardSecurityCode, String AuthorderIdForDirectCapture,
			String AuthorderIdForDirectVoid, String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSuiteCommerce][executeSale] ends");
		String orderNumber = null;
		String childWindow = null;
		String mainWindow = null;
		String saleId = null;
		Set<String> allWindows = null;
		Iterator<String> itr = null;
		Object itemDetails[][] = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.STRING_SALE);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		configurePaymentProcessingProfile.disableGatewayRequestAuthorization(driver, Constants.PAYMENT_INTEGRATION);
		commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_WEBSITES_PREVIEW);
		executor = (JavascriptExecutor) driver;
		mainWindow = driver.getWindowHandle();
		allWindows = driver.getWindowHandles();
		itr = allWindows.iterator();
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				commonUtil.switchToWindow(driver, childWindow);
				commonUtil.maximizeWindow(driver);
				commonUtil.refreshCurrentPage(driver);
				if (Constants.WELCOME_TO_THE_STORE.equals(driver.getTitle()) && commonUtil
						.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN).isDisplayed()) {
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WELCOME_USER);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SIGN_OUT);
				}
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN);
				commonUtil.setElementValueByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN_EMAIL, Email);
				commonUtil.setElementValueByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN_PASSWORD,
						Password);
				executor.executeScript("jQuery('.login-register-login-submit').trigger('click');");
				Helper.waitForElementToBeVisible(driver,
						commonUtil.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WISHLIST));
				if (Constants.YES.equals(HasMultipleItem.toUpperCase())) {
					itemDetails = commonUtil.getItemDetails(Items);
					for (int i = 0; i < itemDetails.length; i++) {
						int j = 0;
						commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SHOP);
						commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
						commonUtil.clickElementByLinkText(driver, itemDetails[i][j].toString());
						commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_ADD_TO_CART);
						commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
						if (i != (itemDetails.length - 1)) {
							commonUtil.clickElementByXPath(driver, prop,
									Constants.SUITE_COMMERCE_ADVANCED_CONTINUE_SHOPPING);
						} else {
							commonUtil.clickElementByXPath(driver, prop,
									Constants.SUITE_COMMERCE_ADVANCED_VIEW_CART_CHECKOUT);
						}
					}
				} else {
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SHOP);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
					commonUtil.clickElementByLinkText(driver, ItemName);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_ADD_TO_CART);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_VIEW_CART_CHECKOUT);
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.CART_DETAILS);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PROCEED_TO_CHECKOUT);
				commonUtil.setShippingMethodSCA(driver, ShippingMethod);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
				commonUtil.clickElementByLinkText(driver, Constants.CREDIT_DEBIT_CARD);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.PAYMENT_METHOD);
				executor.executeScript("jQuery('.creditcard-selector-option').trigger('click')");
				commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
				// continue button is not clicked in SCA Auth After selecting SHipping Method
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PLACE_ORDER);
				WebElement orderConfirmationNumber = commonUtil.findElementByXPath(driver, prop,
						Constants.SUITE_COMMERCE_ADVANCED_ORDER_CONFIRMATION_NUMBER);
				if (null != orderConfirmationNumber) {
					orderNumber = StringUtils.substringAfterLast(orderConfirmationNumber.getText(), Constants.HASH);
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.refreshCurrentPage(driver);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WELCOME_USER);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SIGN_OUT);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.closeCurrentWindow(driver);
			}
		}
		commonUtil.switchToWindow(driver, mainWindow);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber, Keys.ENTER);
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.scrollToBottom(driver);
		commonUtil.clickLinkByXPath(driver, prop, Constants.PAYMENT_EVENT_TAB);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		saleId = commonUtil.getOrderId(driver, Constants.STRING_SALE);
		commonUtil.renameFolder(screenShotPath, Constants.STRING_SALE, saleId);
		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.STRING_SALE,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
				prop.getProperty(Constants.PI_DISBLED_PN_REFERENCE_NUMBER));
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), saleId);
		logger.info("[CreditCardSuiteCommerce][executeSale] ends");
	}

}
