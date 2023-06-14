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
import com.cybersource.test.util.RetryFailedTestCases;

@Listeners(com.cybersource.test.util.Listeners.class)
/**
 * CreditCardSiteBuilder class which will have all functionality used to place order and do related services using Credit card Site Builder
 * @author AD20243779
 *
 */
public class CreditCardSiteBuilder extends CommonTest {
	CommonUtil commonUtil = new CommonUtil();
	ConfigurePaymentProcessingProfile configurePaymentProcessingProfile = new ConfigurePaymentProcessingProfile();
	boolean isDirectCaptureService = false;
	boolean isDirectRefundService = false;
	boolean isDirectCustomerRefund = false;
	boolean isDirectVoid = false;
	String orderNumber = null;
	String pnReferenceNumber = null;
	String screenShotPath = null;
	String screenShotModulePath = null;
	WebDriver driver = null;
	String userIndex = "1";
	JavascriptExecutor executor = null;
	final static Logger logger = Logger.getLogger(CreditCardSiteBuilder.class);

	@BeforeTest
	/**
	 * This method is used to initialize test environment before running a test
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @throws IOException Handles when an I/O exception of some sort has occurred
	 */
	public void init(ITestContext context) throws IOException {
		super.loadCommonConfig(context);
		screenShotModulePath = commonUtil.getScreenshotsModulePath(Constants.CREDIT_CARD_SB);
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
		logger.info("[CreditCardSiteBuilder][getData] currentScenario:" + currentScenario);
		return data;
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute capture service after authorization
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param Item String containing item to be ordered
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CreditCardNumber String containing credit card number used for transaction
	 * @param ExpirationMonth String containing expiration month of credit card used for transaction
	 * @param ExpirationYear String containing expiration year of credit card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
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
	public void executeAuthCapture(String Email, String Password, String Item, String PaymentMethod,
			String CreditCardNumber, String ExpirationMonth, String ExpirationYear, String CardSecurityCode,
			String ShippingMethod, String Location, String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid,
			String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSiteBuilder][executeAuthCapture] starts");
		executeAuth(Email, Password, Item, PaymentMethod, CreditCardNumber, ExpirationMonth, ExpirationYear,
				CardSecurityCode, ShippingMethod, Location, AuthorderIdForDirectCapture, AuthorderIdForDirectVoid,
				CaptureIdForDirectRefund, CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		executeCapture(Email, Password, Item, PaymentMethod, CreditCardNumber, ExpirationMonth, ExpirationYear,
				CardSecurityCode, ShippingMethod, Location, AuthorderIdForDirectCapture, AuthorderIdForDirectVoid,
				CaptureIdForDirectRefund, CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		logger.info("[CreditCardSiteBuilder][executeAuthCapture] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund service after authorization and capture
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param Item String containing item to be ordered
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CreditCardNumber String containing credit card number used for transaction
	 * @param ExpirationMonth String containing expiration month of credit card used for transaction
	 * @param ExpirationYear String containing expiration year of credit card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
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
	public void executeAuthCaptureRefund(String Email, String Password, String Item, String PaymentMethod,
			String CreditCardNumber, String ExpirationMonth, String ExpirationYear, String CardSecurityCode,
			String ShippingMethod, String Location, String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid,
			String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSiteBuilder][executeAuthCaptureRefund] starts");
		executeAuth(Email, Password, Item, PaymentMethod, CreditCardNumber, ExpirationMonth, ExpirationYear,
				CardSecurityCode, ShippingMethod, Location, AuthorderIdForDirectCapture, AuthorderIdForDirectVoid,
				CaptureIdForDirectRefund, CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		executeCapture(Email, Password, Item, PaymentMethod, CreditCardNumber, ExpirationMonth, ExpirationYear,
				CardSecurityCode, ShippingMethod, Location, AuthorderIdForDirectCapture, AuthorderIdForDirectVoid,
				CaptureIdForDirectRefund, CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		executeRefund(Email, Password, Item, PaymentMethod, CreditCardNumber, ExpirationMonth, ExpirationYear,
				CardSecurityCode, ShippingMethod, Location, AuthorderIdForDirectCapture, AuthorderIdForDirectVoid,
				CaptureIdForDirectRefund, CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		logger.info("[CreditCardSiteBuilder][executeAuthCaptureRefund] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute customer refund service after authorization and capture
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param Item String containing item to be ordered
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CreditCardNumber String containing credit card number used for transaction
	 * @param ExpirationMonth String containing expiration month of credit card used for transaction
	 * @param ExpirationYear String containing expiration year of credit card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
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
	public void executeAuthCaptureCustomerRefund(String Email, String Password, String Item, String PaymentMethod,
			String CreditCardNumber, String ExpirationMonth, String ExpirationYear, String CardSecurityCode,
			String ShippingMethod, String Location, String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid,
			String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSiteBuilder][executeAuthCaptureCustomerRefund] starts");
		// ToDo: OpenIssueSDN: CustomerRefund has issue from NetSuite side
		// Assert.assertFalse("OpenIssueSDN: CustomerRefund has issue from NetSuite
		// side", true);

		executeAuth(Email, Password, Item, PaymentMethod, CreditCardNumber, ExpirationMonth, ExpirationYear,
				CardSecurityCode, ShippingMethod, Location, AuthorderIdForDirectCapture, AuthorderIdForDirectVoid,
				CaptureIdForDirectRefund, CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		executeCapture(Email, Password, Item, PaymentMethod, CreditCardNumber, ExpirationMonth, ExpirationYear,
				CardSecurityCode, ShippingMethod, Location, AuthorderIdForDirectCapture, AuthorderIdForDirectVoid,
				CaptureIdForDirectRefund, CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		executeCustomerRefund(Email, Password, Item, PaymentMethod, CreditCardNumber, ExpirationMonth, ExpirationYear,
				CardSecurityCode, ShippingMethod, Location, AuthorderIdForDirectCapture, AuthorderIdForDirectVoid,
				CaptureIdForDirectRefund, CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		logger.info("[CreditCardSiteBuilder][executeAuthCaptureCustomerRefund] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute void service after authorization service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param Item String containing item to be ordered
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CreditCardNumber String containing credit card number used for transaction
	 * @param ExpirationMonth String containing expiration month of credit card used for transaction
	 * @param ExpirationYear String containing expiration year of credit card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
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
	public void executeAuthVoid(String Email, String Password, String Item, String PaymentMethod,
			String CreditCardNumber, String ExpirationMonth, String ExpirationYear, String CardSecurityCode,
			String ShippingMethod, String Location, String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid,
			String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSiteBuilder][executeAuthVoid] starts");
		executeAuth(Email, Password, Item, PaymentMethod, CreditCardNumber, ExpirationMonth, ExpirationYear,
				CardSecurityCode, ShippingMethod, Location, AuthorderIdForDirectCapture, AuthorderIdForDirectVoid,
				CaptureIdForDirectRefund, CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		executeVoid(Email, Password, Item, PaymentMethod, CreditCardNumber, ExpirationMonth, ExpirationYear,
				CardSecurityCode, ShippingMethod, Location, AuthorderIdForDirectCapture, AuthorderIdForDirectVoid,
				CaptureIdForDirectRefund, CaptureIdForCustomerRefund, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario);
		logger.info("[CreditCardSiteBuilder][executeAuthVoid] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute authorization service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param Item String containing item to be ordered
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CreditCardNumber String containing credit card number used for transaction
	 * @param ExpirationMonth String containing expiration month of credit card used for transaction
	 * @param ExpirationYear String containing expiration year of credit card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
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
	public void executeAuth(String Email, String Password, String Item, String PaymentMethod, String CreditCardNumber,
			String ExpirationMonth, String ExpirationYear, String CardSecurityCode, String ShippingMethod,
			String Location, String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid,
			String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSiteBuilder][executeAuth] starts");
		String orderNumber = null;
		String childWindow = null;
		String mainWindow = null;
		String authOrderId = null;
		Set<String> allWindows = null;
		Iterator<String> itr = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_BUILDER_URL, userIndex);
		configurePaymentProcessingProfile.enableGatewayRequestAuthorization(driver, Constants.PAYMENT_INTEGRATION);
		executor = (JavascriptExecutor) driver;
		commonUtil.clickElementByXPath(driver, prop, Constants.SITE_BUILDER_WEBSITES_PREVIEW);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		mainWindow = driver.getWindowHandle();
		allWindows = driver.getWindowHandles();
		itr = allWindows.iterator();
		while (itr.hasNext()) {
			childWindow = null;
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				commonUtil.switchToWindow(driver, childWindow);
				commonUtil.maximizeWindow(driver);
				commonUtil.clickElementByXPath(driver, prop, Constants.SITE_BUILDER_SIGN_IN);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				if (Constants.ZERO == commonUtil
						.findElementById(driver, prop, Constants.SITE_BUILDER_SIGNIN_EMAIL_BOX_ID).getAttribute("value")
						.trim().length()) {
					commonUtil.setElementValueByXPath(driver, prop, Constants.SITE_BUILDER_CUSTOMER_SIGNIN_EMAIL,
							Email);
				}
				commonUtil.setElementValueByXPath(driver, prop, Constants.SITE_BUILDER_CUSTOMER_SIGNIN_PASSWORD,
						Password);
				executor.executeScript(prop.getProperty(Constants.SITE_BUILDER_CONTINUE));
				executor.executeScript("jQuery('.bgbutton').trigger('click');");
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.findElementByXPath(driver, prop, Constants.SITE_BUILDER_SEARCH_FOR).sendKeys(Item,
						Keys.ENTER);
				commonUtil.clickElementByXPath(driver, prop, Constants.SITE_BUILDER_ADD_TO_CART);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.CART_DETAILS);
				commonUtil.clickElementByXPath(driver, prop, Constants.SITE_BUILDER_PROCEED_TO_CHECKOUT);
				if (Constants.PAYMENT_INFORMATION_SUITEDREAMS.equals(driver.getTitle())) {
					commonUtil.setPaymentMethod(driver, PaymentMethod);
				} else if (Constants.REVIEW_AND_SUBMIT_ORDER_SUITEDREAMS.equals(driver.getTitle())) {
					executor.executeScript("document.getElementsByName('change')[2].click();");
					commonUtil.setPaymentMethod(driver, PaymentMethod);
				}
				commonUtil.setElementValueByXPath(driver, prop, Constants.SITE_BUILDER_EXPIRATION_MONTH,
						ExpirationMonth);
				commonUtil.setElementValueByXPath(driver, prop, Constants.SITE_BUILDER_EXPIRATION_YEAR, ExpirationYear);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.PAYMENT_METHOD);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				executor.executeScript(prop.getProperty(Constants.SITE_BUILDER_CONTINUE));
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				executor.executeScript(prop.getProperty(Constants.SITE_BUILDER_CONTINUE));
				WebElement orderConfirmationNumber = commonUtil.findElementByXPath(driver, prop,
						Constants.ORDER_CONFIRMATION_NUMBER);
				if (null != orderConfirmationNumber) {
					orderNumber = StringUtils.substringAfterLast(orderConfirmationNumber.getText(), Constants.HIPHEN);
				}
				commonUtil.clickElementByXPath(driver, prop, Constants.SITE_BUILDER_SIGN_OUT);
				commonUtil.closeCurrentWindow(driver);
			}
		}
		commonUtil.switchToWindow(driver, mainWindow);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber, Keys.ENTER);
		commonUtil.writeCurrentTransactionUrl(Constants.AUTH_ORDERID_URL, commonUtil.getCurrentUrl(driver));
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		authOrderId = commonUtil.getOrderId(driver, Constants.AUTHORIZATION);
		commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.AUTHORIZATION,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		// pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
		// prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH));
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, commonUtil.getCurrentUrl(driver), authOrderId);
		commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, pnReferenceNumber);
		logger.info("[CreditCardSiteBuilder][executeAuth] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute capture service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param Item String containing item to be ordered
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CreditCardNumber String containing credit card number used for transaction
	 * @param ExpirationMonth String containing expiration month of credit card used for transaction
	 * @param ExpirationYear String containing expiration year of credit card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
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
	public void executeCapture(String Email, String Password, String Item, String PaymentMethod,
			String CreditCardNumber, String ExpirationMonth, String ExpirationYear, String CardSecurityCode,
			String ShippingMethod, String Location, String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid,
			String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSiteBuilder][executeCapture] starts");
		String captureId = null;
		if (isDirectCaptureService) {
			driver = commonUtil.setupDriverDirectUrl(driver, prop, AuthorderIdForDirectCapture, null);
		}
		executor = (JavascriptExecutor) driver;
		executor.executeScript(prop.getProperty(Constants.SALES_ORDER_APPROVE));
		commonUtil.clickElementByXPath(driver, prop, Constants.OVERRIDE_PENDING_PAYMENT);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_FULFILL);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.ORDER_FULLFIL);
		commonUtil.setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, 1,
				Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
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
		// pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
		// prop.getProperty(Constants.PI_DISBLED_PN_REFERENCE_NUMBER));
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.CAPTURE, commonUtil.getCurrentUrl(driver), captureId);
		commonUtil.addLogToTestNGReport(Constants.CAPTURE, pnReferenceNumber);
		logger.info("[CreditCardSiteBuilder][executeCapture] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param Item String containing item to be ordered
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CreditCardNumber String containing credit card number used for transaction
	 * @param ExpirationMonth String containing expiration month of credit card used for transaction
	 * @param ExpirationYear String containing expiration year of credit card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
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
	public void executeRefund(String Email, String Password, String Item, String PaymentMethod, String CreditCardNumber,
			String ExpirationMonth, String ExpirationYear, String CardSecurityCode, String ShippingMethod,
			String Location, String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid,
			String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSiteBuilder][executeRefund] starts");
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.REFUND);
		String refundId = null;
		if (isDirectRefundService) {
			driver = commonUtil.setupDriverDirectUrl(driver, prop, CaptureIdForDirectRefund, null);
		}
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
		commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_LOCATION).sendKeys(Keys.DOWN, Keys.DOWN,
				Keys.ENTER);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.scrollToBottom(driver);
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_OK_BUTTON);
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		refundId = commonUtil.getOrderId(driver, Constants.REFUND);
		commonUtil.renameFolder(screenShotPath, Constants.REFUND, refundId);
		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.REFUND,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		// pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
		// prop.getProperty(Constants.PI_ENABLED_PN_REFERENCE_NUMBER));
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.REFUND, pnReferenceNumber);
		commonUtil.addLogToTestNGReport(Constants.REFUND, commonUtil.getCurrentUrl(driver), refundId);
		logger.info("[CreditCardSiteBuilder][executeRefund] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute customer refund service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param Item String containing item to be ordered
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CreditCardNumber String containing credit card number used for transaction
	 * @param ExpirationMonth String containing expiration month of credit card used for transaction
	 * @param ExpirationYear String containing expiration year of credit card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
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
	public void executeCustomerRefund(String Email, String Password, String Item, String PaymentMethod,
			String CreditCardNumber, String ExpirationMonth, String ExpirationYear, String CardSecurityCode,
			String ShippingMethod, String Location, String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid,
			String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSuiteCommerceAdvanced][executeCustomerRefund] starts");
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
		// pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
		// prop.getProperty(Constants.PI_DISBLED_PN_REFERENCE_NUMBER));
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.REFUND, pnReferenceNumber);
		commonUtil.addLogToTestNGReport(Constants.REFUND, commonUtil.getCurrentUrl(driver), refundId);
		logger.info("[CreditCardSuiteCommerceAdvanced][executeCustomerRefund] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute void service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param Item String containing item to be ordered
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CreditCardNumber String containing credit card number used for transaction
	 * @param ExpirationMonth String containing expiration month of credit card used for transaction
	 * @param ExpirationYear String containing expiration year of credit card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
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
	public void executeVoid(String Email, String Password, String Item, String PaymentMethod, String CreditCardNumber,
			String ExpirationMonth, String ExpirationYear, String CardSecurityCode, String ShippingMethod,
			String Location, String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid,
			String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSiteBuilder][executeCancel] starts");
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
		// pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
		// prop.getProperty(Constants.PI_DISBLED_PN_REFERENCE_NUMBER));
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.VOID, pnReferenceNumber);
		commonUtil.addLogToTestNGReport(Constants.VOID, commonUtil.getCurrentUrl(driver), voidId);
		logger.info("[CreditCardSiteBuilder][executeCancel] ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute sale service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param Item String containing item to be ordered
	 * @param PaymentMethod String containing payment method used for transaction
	 * @param CreditCardNumber String containing credit card number used for transaction
	 * @param ExpirationMonth String containing expiration month of credit card used for transaction
	 * @param ExpirationYear String containing expiration year of credit card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
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
	public void executeSale(String Email, String Password, String Item, String PaymentMethod, String CreditCardNumber,
			String ExpirationMonth, String ExpirationYear, String CardSecurityCode, String ShippingMethod,
			String Location, String AuthorderIdForDirectCapture, String AuthorderIdForDirectVoid,
			String CaptureIdForDirectRefund, String CaptureIdForCustomerRefund, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[CreditCardSiteBuilder][executeSale] starts");
		String orderNumber = null;
		String childWindow = null;
		String mainWindow = null;
		String saleId = null;
		Set<String> allWindows = null;
		Iterator<String> itr = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.STRING_SALE);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_BUILDER_URL, userIndex);
		configurePaymentProcessingProfile.disableGatewayRequestAuthorization(driver, Constants.PAYMENT_INTEGRATION);
		executor = (JavascriptExecutor) driver;
		commonUtil.clickElementByXPath(driver, prop, Constants.SITE_BUILDER_WEBSITES_PREVIEW);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		mainWindow = driver.getWindowHandle();
		allWindows = driver.getWindowHandles();
		itr = allWindows.iterator();
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				commonUtil.switchToWindow(driver, childWindow);
				commonUtil.maximizeWindow(driver);
				commonUtil.clickElementByXPath(driver, prop, Constants.SITE_BUILDER_SIGN_IN);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				if (0 == commonUtil.findElementById(driver, prop, Constants.SITE_BUILDER_SIGNIN_EMAIL_BOX_ID)
						.getAttribute(Constants.VALUE).trim().length()) {
					commonUtil.setElementValueByXPath(driver, prop, Constants.SITE_BUILDER_CUSTOMER_SIGNIN_EMAIL,
							Email);
				}
				commonUtil.setElementValueByXPath(driver, prop, Constants.SITE_BUILDER_CUSTOMER_SIGNIN_PASSWORD,
						Password);
				executor.executeScript(prop.getProperty(Constants.SITE_BUILDER_CONTINUE));
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.findElementByXPath(driver, prop, Constants.SITE_BUILDER_SEARCH_FOR).sendKeys(Item,
						Keys.ENTER);
				commonUtil.clickElementByXPath(driver, prop, Constants.SITE_BUILDER_ADD_TO_CART);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.CART_DETAILS);
				commonUtil.clickElementByXPath(driver, prop, Constants.SITE_BUILDER_PROCEED_TO_CHECKOUT);
				if (Constants.PAYMENT_INFORMATION_SUITEDREAMS.equals(driver.getTitle())) {
					commonUtil.setPaymentMethod(driver, PaymentMethod);
				} else if (Constants.REVIEW_AND_SUBMIT_ORDER_SUITEDREAMS.equals(driver.getTitle())) {
					executor.executeScript("document.getElementsByName('change')[2].click();");
					commonUtil.setPaymentMethod(driver, PaymentMethod);
				}
				commonUtil.setElementValueByXPath(driver, prop, Constants.SITE_BUILDER_EXPIRATION_MONTH,
						ExpirationMonth);
				commonUtil.setElementValueByXPath(driver, prop, Constants.SITE_BUILDER_EXPIRATION_YEAR, ExpirationYear);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.PAYMENT_METHOD);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				executor.executeScript(prop.getProperty(Constants.SITE_BUILDER_CONTINUE));
				commonUtil.setShippingMethod(driver, ShippingMethod);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				executor.executeScript(prop.getProperty(Constants.SITE_BUILDER_CONTINUE));
				WebElement orderConfirmationNumber = commonUtil.findElementByXPath(driver, prop,
						Constants.ORDER_CONFIRMATION_NUMBER);
				if (null != orderConfirmationNumber) {
					orderNumber = StringUtils.substringAfterLast(orderConfirmationNumber.getText(), Constants.HIPHEN);
				}
				commonUtil.clickElementByXPath(driver, prop, Constants.SITE_BUILDER_SIGN_OUT);
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
		// pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
		// prop.getProperty(Constants.PI_DISBLED_PN_REFERENCE_NUMBER));
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), saleId);
		logger.info("[CreditCardSiteBuilder][executeSale] ends");
	}
}