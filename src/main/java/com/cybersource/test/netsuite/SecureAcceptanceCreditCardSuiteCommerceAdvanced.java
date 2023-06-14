package com.cybersource.test.netsuite;

import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
 * SecureAcceptanceCreditCardSuiteCommerceAdvanced class which will have all functionality used to place order and do related services using secure acceptance credit card in suite commerce advanced platform
 * @author AD20243779
 *
 */
public class SecureAcceptanceCreditCardSuiteCommerceAdvanced extends CommonTest {
	CommonUtil commonUtil = new CommonUtil();
	WebDriver driver = null;
	String screenShotPath = null;
	String screenShotModulePath = null;
	String service = null;
	String authOrderId = null;
	String captureId = null;
	String saleId = null;
	String refund = null;
	String pnReferenceNumber = null;
	String orderNumber = null;
	String errorMsg = null;
	String childWindow = null;
	String mainWindow = null;
	String userIndex = "1";
	String[] authCode = new String[2];
	Set<String> allWindows = null;
	Iterator<String> itr = null;
	Object itemDetails[][] = null;
	static int authProfileConfigFlag = 0;
	static int saleProfileConfigFlag = 0;
	JavascriptExecutor executor = null;
	final static Logger logger = Logger.getLogger(SecureAcceptanceCreditCardSuiteCommerceAdvanced.class);

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
		screenShotModulePath = commonUtil.getScreenshotsModulePath(Constants.CREDIT_CARD_SECUREACCEPTANCE_SCA);
	}

	@DataProvider
	/**
	 * This method is used to get data from excel sheets to execute test cases
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @return Object which contains data from sheet
	 */
	public Object[][] getData(ITestContext context) {
		String currentScenario = context.getCurrentXmlTest().getParameter(Constants.TEST_SCENARIO);
		String prefix = context.getCurrentXmlTest().getParameter(Constants.PREFIX);
		refund = context.getCurrentXmlTest().getParameter(Constants.REFUND);
		Object[][] data = FileUtil.getSheetData(XLSX_FILE_PATH,
				prop.getProperty(prefix + currentScenario + Constants._SHEET_NAME));
		logger.info("[getData] currentScenario: " + currentScenario);
		return data;
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute capture service after authorization
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param PaymentCardName String containing name of Card holder
	 * @param Location String containing order location
	 * @param CreditCardNumber String containing number of credit card used to place order
	 * @param ExpirationMonth String containing expiry month of credit card used to place order
	 * @param ExpirationYear String containing expiry year of credit card used to place order
	 * @param CardSecurityCode String containing cvN of credit card used to place order
	 * @param PaymentProcessingProfileConfig String containing configuration setup for payment processing profile 
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorSecretKeyString containing details of the processor secret key for transaction
	 * @param SAProfileID String containing secure acceptance profile id
	 * @param SAAccessKey String containing secure acceptance access key
	 * @param SASecretKey String containing secure acceptance secret key
	 * @param WebSiteName String containing name of website used to place order
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthCapture(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String PaymentCardName, String Location, String CreditCardNumber,
			String ExpirationMonth, String ExpirationYear, String CardSecurityCode,
			String PaymentProcessingProfileConfig, String PaymentProcessingProfile, String MerchantID,
			String ProcessorName, String ProcessorKey, String ProcessorSecretKey, String SAProfileID,
			String SAAccessKey, String SASecretKey, String WebSiteName, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthCapture] Starts");
		executeAuth(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, PaymentCardName, Location,
				CreditCardNumber, ExpirationMonth, ExpirationYear, CardSecurityCode, PaymentProcessingProfileConfig,
				PaymentProcessingProfile, MerchantID, ProcessorName, ProcessorKey, ProcessorSecretKey, SAProfileID,
				SAAccessKey, SASecretKey, WebSiteName, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeCapture(driver, Items, HasMultipleItem, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario, screenShotPath, screenShotModulePath);
		logger.info("[executeAuthCapture] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund service after authorization and capture
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param PaymentCardName String containing name of Card holder
	 * @param Location String containing order location
	 * @param CreditCardNumber String containing number of credit card used to place order
	 * @param ExpirationMonth String containing expiry month of credit card used to place order
	 * @param ExpirationYear String containing expiry year of credit card used to place order
	 * @param CardSecurityCode String containing cvN of credit card used to place order
	 * @param PaymentProcessingProfileConfig String containing configuration setup for payment processing profile 
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorSecretKeyString containing details of the processor secret key for transaction
	 * @param SAProfileID String containing secure acceptance profile id
	 * @param SAAccessKey String containing secure acceptance access key
	 * @param SASecretKey String containing secure acceptance secret key
	 * @param WebSiteName String containing name of website used to place order
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthCaptureRefund(String Email, String Password, String ItemName, String Items,
			String HasMultipleItem, String ShippingMethod, String PaymentCardName, String Location,
			String CreditCardNumber, String ExpirationMonth, String ExpirationYear, String CardSecurityCode,
			String PaymentProcessingProfileConfig, String PaymentProcessingProfile, String MerchantID,
			String ProcessorName, String ProcessorKey, String ProcessorSecretKey, String SAProfileID,
			String SAAccessKey, String SASecretKey, String WebSiteName, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthCaptureRefund] Starts");
		executeAuth(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, PaymentCardName, Location,
				CreditCardNumber, ExpirationMonth, ExpirationYear, CardSecurityCode, PaymentProcessingProfileConfig,
				PaymentProcessingProfile, MerchantID, ProcessorName, ProcessorKey, ProcessorSecretKey, SAProfileID,
				SAAccessKey, SASecretKey, WebSiteName, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeCapture(driver, Items, HasMultipleItem, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario, screenShotPath, screenShotModulePath);
		commonUtil.executeRefund(driver, Location, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario,
				screenShotPath, screenShotModulePath);
		logger.info("[executeAuthCaptureRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute customer refund service after authorization and capture
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param PaymentCardName String containing name of Card holder
	 * @param Location String containing order location
	 * @param CreditCardNumber String containing number of credit card used to place order
	 * @param ExpirationMonth String containing expiry month of credit card used to place order
	 * @param ExpirationYear String containing expiry year of credit card used to place order
	 * @param CardSecurityCode String containing cvN of credit card used to place order
	 * @param PaymentProcessingProfileConfig String containing configuration setup for payment processing profile 
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorSecretKeyString containing details of the processor secret key for transaction
	 * @param SAProfileID String containing secure acceptance profile id
	 * @param SAAccessKey String containing secure acceptance access key
	 * @param SASecretKey String containing secure acceptance secret key
	 * @param WebSiteName String containing name of website used to place order
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthCaptureCustomerRefund(String Email, String Password, String ItemName, String Items,
			String HasMultipleItem, String ShippingMethod, String PaymentCardName, String Location,
			String CreditCardNumber, String ExpirationMonth, String ExpirationYear, String CardSecurityCode,
			String PaymentProcessingProfileConfig, String PaymentProcessingProfile, String MerchantID,
			String ProcessorName, String ProcessorKey, String ProcessorSecretKey, String SAProfileID,
			String SAAccessKey, String SASecretKey, String WebSiteName, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthCaptureCustomerRefund] Starts");
		executeAuth(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, PaymentCardName, Location,
				CreditCardNumber, ExpirationMonth, ExpirationYear, CardSecurityCode, PaymentProcessingProfileConfig,
				PaymentProcessingProfile, MerchantID, ProcessorName, ProcessorKey, ProcessorSecretKey, SAProfileID,
				SAAccessKey, SASecretKey, WebSiteName, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeCapture(driver, Items, HasMultipleItem, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario, screenShotPath, screenShotModulePath);
		commonUtil.executeCustomerRefund(driver, Items, HasMultipleItem, Location, RequireEBCCheck, ReasonCode,
				DecisionMsg, TestScenario, screenShotPath, screenShotModulePath);
		logger.info("[executeAuthCaptureCustomerRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute void service after authorization
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param PaymentCardName String containing name of Card holder
	 * @param Location String containing order location
	 * @param CreditCardNumber String containing number of credit card used to place order
	 * @param ExpirationMonth String containing expiry month of credit card used to place order
	 * @param ExpirationYear String containing expiry year of credit card used to place order
	 * @param CardSecurityCode String containing cvN of credit card used to place order
	 * @param PaymentProcessingProfileConfig String containing configuration setup for payment processing profile 
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorSecretKeyString containing details of the processor secret key for transaction
	 * @param SAProfileID String containing secure acceptance profile id
	 * @param SAAccessKey String containing secure acceptance access key
	 * @param SASecretKey String containing secure acceptance secret key
	 * @param WebSiteName String containing name of website used to place order
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthVoid(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String PaymentCardName, String Location, String CreditCardNumber,
			String ExpirationMonth, String ExpirationYear, String CardSecurityCode,
			String PaymentProcessingProfileConfig, String PaymentProcessingProfile, String MerchantID,
			String ProcessorName, String ProcessorKey, String ProcessorSecretKey, String SAProfileID,
			String SAAccessKey, String SASecretKey, String WebSiteName, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthVoid] Starts");
		executeAuth(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, PaymentCardName, Location,
				CreditCardNumber, ExpirationMonth, ExpirationYear, CardSecurityCode, PaymentProcessingProfileConfig,
				PaymentProcessingProfile, MerchantID, ProcessorName, ProcessorKey, ProcessorSecretKey, SAProfileID,
				SAAccessKey, SASecretKey, WebSiteName, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeVoid(driver, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario, screenShotPath,
				screenShotModulePath);
		logger.info("[executeAuthVoid] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund service after authorization and capture for level 2,3 transaction
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param PaymentCardName String containing name of Card holder
	 * @param Location String containing order location
	 * @param CreditCardNumber String containing number of credit card used to place order
	 * @param ExpirationMonth String containing expiry month of credit card used to place order
	 * @param ExpirationYear String containing expiry year of credit card used to place order
	 * @param CardSecurityCode String containing cvv of credit card used to place order
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level of transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorSecretKeyString containing details of the processor secret key for transaction
	 * @param SAProfileID String containing secure acceptance profile id
	 * @param SAAccessKey String containing secure acceptance access key
	 * @param SASecretKey String containing secure acceptance secret key
	 * @param WebSiteName String containing name of website used to place order
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthCaptureRefundlv2lv3(String Email, String Password, String ItemName, String Items,
			String HasMultipleItem, String ShippingMethod, String PaymentCardName, String Location,
			String CreditCardNumber, String ExpirationMonth, String ExpirationYear, String CardSecurityCode,
			String PaymentProcessingProfile, String MerchantID, String ProcessorName, String ProcessorLevel,
			String ProcessorKey, String ProcessorSecretKey, String SAProfileID, String SAAccessKey, String SASecretKey,
			String WebSiteName, String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario)
			throws Exception {
		logger.info("[executeAuthCaptureRefundlv2lv3][executeAuthCaptureRefundlv2lv3] Starts");
		commonUtil.setPaymentProcessingProfile(driver, PaymentProcessingProfile, MerchantID, ProcessorKey,
				ProcessorName, ProcessorLevel);
		executeAuth(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, PaymentCardName, Location,
				CreditCardNumber, ExpirationMonth, ExpirationYear, CardSecurityCode, MerchantID,
				PaymentProcessingProfile, MerchantID, ProcessorName, ProcessorKey, ProcessorSecretKey, SAProfileID,
				SAAccessKey, SASecretKey, WebSiteName, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		executeCapture(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, PaymentCardName, Location,
				CreditCardNumber, ExpirationMonth, ExpirationYear, CardSecurityCode, PaymentProcessingProfile,
				WebSiteName, MerchantID, ProcessorKey, ProcessorName, ProcessorLevel, RequireEBCCheck, ReasonCode,
				DecisionMsg, TestScenario);
		executeRefund(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, PaymentCardName, Location,
				CreditCardNumber, ExpirationMonth, ExpirationYear, CardSecurityCode, PaymentProcessingProfile,
				WebSiteName, MerchantID, ProcessorKey, ProcessorName, ProcessorLevel, RequireEBCCheck, ReasonCode,
				DecisionMsg, TestScenario);
		logger.info("[executeAuthCaptureRefundlv2lv3][executeAuthCaptureRefundlv2lv3] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute authorization service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param PaymentCardName String containing name of Card holder
	 * @param Location String containing order location
	 * @param CreditCardNumber String containing number of credit card used to place order
	 * @param ExpirationMonth String containing expiry month of credit card used to place order
	 * @param ExpirationYear String containing expiry year of credit card used to place order
	 * @param CardSecurityCode String containing cvv of credit card used to place order
	 * @param PaymentProcessingProfileConfig String containing configuration setup for payment processing profile 
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorSecretKeyString containing details of the processor secret key for transaction
	 * @param SAProfileID String containing secure acceptance profile id
	 * @param SAAccessKey String containing secure acceptance access key
	 * @param SASecretKey String containing secure acceptance secret key
	 * @param WebSiteName String containing name of website used to place order
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuth(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String PaymentCardName, String Location, String CreditCardNumber,
			String ExpirationMonth, String ExpirationYear, String CardSecurityCode,
			String PaymentProcessingProfileConfig, String PaymentProcessingProfile, String MerchantID,
			String ProcessorName, String ProcessorKey, String ProcessorSecretKey, String SAProfileID,
			String SAAccessKey, String SASecretKey, String WebSiteName, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuth] Starts");
		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		driver = commonUtil.switchToMainWindow(driver);
		if ((Constants.YES.equalsIgnoreCase(PaymentProcessingProfileConfig)) && (authProfileConfigFlag == 0)) {
			authProfileConfigFlag = 1;
			commonUtil.setPaymentProcessingProfile(driver, userIndex, PaymentProcessingProfile, MerchantID,
					ProcessorName, ProcessorKey, ProcessorSecretKey, Constants.NO, Constants.CHARECTER_A,
					Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT,
					Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT,
					SAProfileID, SAAccessKey, SASecretKey);
		}
		if (Constants.POSITIVE.toUpperCase().equals(TestScenario.toUpperCase())) {
			webStoreOrder(driver, Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, PaymentCardName,
					Location, CreditCardNumber, ExpirationMonth, ExpirationYear, CardSecurityCode,
					PaymentProcessingProfile, MerchantID, WebSiteName, RequireEBCCheck, ReasonCode, DecisionMsg,
					TestScenario);

			executor = (JavascriptExecutor) driver;
			commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
			commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(Keys.DOWN, Keys.ENTER);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
			commonUtil.scrollToBottom(driver);

			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
			authOrderId = commonUtil.getOrderId(driver, Constants.AUTHORIZATION);
			commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, commonUtil.getCurrentUrl(driver), authOrderId);

			commonUtil.clickPaymentEvent(driver, Constants.AUTHORIZATION, Constants.SALES_ORDER,
					Constants.STRING_ACCEPT);
			commonUtil.clickPaymentEvent(driver, Constants.AUTHORIZATION, Constants.ORDER, Constants.STRING_ACCEPT);
			authCode = commonUtil.getAuthCode(driver, prop.getProperty(Constants.AUTH_CODE_SECURE_ACCEPTANCE),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH));

			pnReferenceNumber = authCode[1];
			if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
				commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
			}
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, pnReferenceNumber);
		}
		logger.info("[executeAuth] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute sale service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param PaymentCardName String containing name of Card holder
	 * @param Location String containing order location
	 * @param CreditCardNumber String containing number of credit card used to place order
	 * @param ExpirationMonth String containing expiry month of credit card used to place order
	 * @param ExpirationYear String containing expiry year of credit card used to place order
	 * @param CardSecurityCode String containing cvv of credit card used to place order
	 * @param PaymentProcessingProfileConfig String containing configuration setup for payment processing profile 
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorSecretKeyString containing details of the processor secret key for transaction
	 * @param SAProfileID String containing secure acceptance profile id
	 * @param SAAccessKey String containing secure acceptance access key
	 * @param SASecretKey String containing secure acceptance secret key
	 * @param WebSiteName String containing name of website used to place order
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeSale(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String PaymentCardName, String Location, String CreditCardNumber,
			String ExpirationMonth, String ExpirationYear, String CardSecurityCode,
			String PaymentProcessingProfileConfig, String PaymentProcessingProfile, String MerchantID,
			String ProcessorName, String ProcessorKey, String ProcessorSecretKey, String SAProfileID,
			String SAAccessKey, String SASecretKey, String WebSiteName, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeSale] Starts");
		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.STRING_SALE);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		driver = commonUtil.switchToMainWindow(driver);
		if ((Constants.YES.equalsIgnoreCase(PaymentProcessingProfileConfig)) && (saleProfileConfigFlag == 0)) {
			saleProfileConfigFlag = 1;
			commonUtil.setPaymentProcessingProfile(driver, userIndex, PaymentProcessingProfile, MerchantID,
					ProcessorName, ProcessorKey, ProcessorSecretKey, Constants.NO, Constants.CHARECTER_A,
					Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT,
					Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT,
					SAProfileID, SAAccessKey, SASecretKey);
		}
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		webStoreOrder(driver, Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, PaymentCardName,
				Location, CreditCardNumber, ExpirationMonth, ExpirationYear, CardSecurityCode, PaymentProcessingProfile,
				MerchantID, WebSiteName, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);

		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(Keys.DOWN, Keys.ENTER);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);

		commonUtil.scrollToBottom(driver);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		saleId = commonUtil.getOrderId(driver, Constants.STRING_SALE);
		commonUtil.renameFolder(screenShotPath, Constants.STRING_SALE, saleId);
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), saleId);

		commonUtil.clickPaymentEvent(driver, Constants.STRING_SALE, Constants.CUSTOMER_DEPOSIT,
				Constants.STRING_ACCEPT);
		authCode = commonUtil.getAuthCode(driver, prop.getProperty(Constants.AUTH_CODE_SECURE_ACCEPTANCE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH));
		pnReferenceNumber = authCode[1];
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);

		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		logger.info("[executeSale] Ends");
	}

	/**
	 * This method is used to place a webstore order
	 * @param driver Webdriver object which will be used for operations
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param PaymentCardName String containing name of Card holder
	 * @param Location String containing order location
	 * @param CreditCardNumber String containing number of credit card used to place order
	 * @param ExpirationMonth String containing expiry month of credit card used to place order
	 * @param ExpirationYear String containing expiry year of credit card used to place order
	 * @param CardSecurityCode String containing cvv of credit card used to place order
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param WebSiteName  String containing name of website used to place order
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void webStoreOrder(WebDriver driver, String Email, String Password, String ItemName, String Items,
			String HasMultipleItem, String ShippingMethod, String PaymentCardName, String Location,
			String CreditCardNumber, String ExpirationMonth, String ExpirationYear, String CardSecurityCode,
			String PaymentProcessingProfile, String MerchantID, String WebSiteName, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[webStoreOrder] Starts");
		int paymentTypeDropdownFlag = 0;
		String saExternalCheckoutPaymentTypeDropdown = prop
				.getProperty(Constants.SA_EXTERNAL_CHECKOUT_PAYMENT_TYPE_DROPDOWN);
		String[] saExternalCheckoutPaymentTypeDropdownValue = saExternalCheckoutPaymentTypeDropdown
				.split(Constants.COMMA);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		commonUtil.clickWebSitePreview(driver, WebSiteName);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		executor = (JavascriptExecutor) driver;
		mainWindow = driver.getWindowHandle();
		allWindows = driver.getWindowHandles();
		itr = allWindows.iterator();
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				commonUtil.switchToWindow(driver, childWindow);
				commonUtil.maximizeWindow(driver);
				wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_SHOP_NOW))));
				try {
					if (commonUtil.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN)
							.isDisplayed()) {
						logger.info("[webStoreOrder] Clicking login button");
					}
				} catch (Exception e) {
					logger.info("[webStoreOrder] Logging out previous user");
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WELCOME_USER);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SIGN_OUT);
				}

				wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_LOGIN))));
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);

				commonUtil.setElementValueByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN_EMAIL, Email);
				commonUtil.setElementValueByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN_PASSWORD,
						Password);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN_BUTTON);

				Helper.waitForElementToBeVisible(driver,
						commonUtil.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WISHLIST));
				commonUtil.clearCart(driver);
				if (Constants.YES.equals(HasMultipleItem.toUpperCase())) {
					itemDetails = commonUtil.getItemDetails(Items);
					// For multiple items need to loop through all the given item
					for (int i = 0; i < itemDetails.length; i++) {
						int j = 0;
						commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SHOP);
						commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
						commonUtil.refreshCurrentPage(driver);
						commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
						executor.executeScript(
								prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_SCROLL_WINDOW_TILL_ITEM_VISIBLE));
						commonUtil.clickElementByLinkText(driver, itemDetails[i][j].toString());
						commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
						commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_ADD_TO_CART);
						commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
						// Until we reach the last item continue shopping
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
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					commonUtil.refreshCurrentPage(driver);
					wait.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath(prop.getProperty(Constants.SA_PER_PAGE_DROPDOWN))));
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					commonUtil.clickElementByXPath(driver, prop, Constants.SA_PER_PAGE_DROPDOWN);
					commonUtil.clickElementByXPath(driver, prop, Constants.SA_PER_PAGE_OPTION_FOURTY_EIGHT);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
					commonUtil.clickElementByLinkText(driver, ItemName);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_ADD_TO_CART);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_4);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_VIEW_CART_CHECKOUT);
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.CART_DETAILS);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PROCEED_TO_CHECKOUT);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);

				commonUtil.setShippingMethodSCA(driver, ShippingMethod);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));

				commonUtil.clickElementByLinkText(driver, Constants.EXTERNAL_PAGE_PAYMENT_METHOD_OTHERS_TEXT);
				commonUtil.selectCard(driver, null, null, Constants.CREDIT_CARD_SECURE_ACCEPTANCE);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.PAYMENT_METHOD);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_EXTERNAL_PAYMENT);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				// checking payment type dropdown
				logger.info("[webStoreOrder] Value of Payment Type Dropdown: "
						+ saExternalCheckoutPaymentTypeDropdownValue.length);
				for (int i = 0; i < saExternalCheckoutPaymentTypeDropdownValue.length; i++) {
					if (saExternalCheckoutPaymentTypeDropdownValue[i].equalsIgnoreCase(MerchantID)) {
						paymentTypeDropdownFlag = 1;
					}
				}
				if (paymentTypeDropdownFlag != 0) {
					commonUtil.clickPaymentCardDropDown(driver, PaymentCardName);
				} else {
					commonUtil.selectPaymentCard(driver, PaymentCardName);
				}
				commonUtil.setElementValueByXPath(driver, prop, Constants.SECURE_ACCEPTANCE_CREDIT_CARD_NUMBER,
						CreditCardNumber);
				commonUtil.setElementValueByXPath(driver, prop, Constants.SECURE_ACCEPTANCE_EXPIRATION_MONTH,
						ExpirationMonth);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.setElementValueByXPath(driver, prop, Constants.SECURE_ACCEPTANCE_EXPIRATION_YEAR,
						ExpirationYear);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.setElementValueByXPath(driver, prop, Constants.SECURE_ACCEPTANCE_CARD_CVN, CardSecurityCode);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.clickElementByXPath(driver, prop, Constants.SECURE_ACCEPTANCE_NEXT);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				if (Constants.PAYMENT_ACCEPTANCE_REVIEW_PAGE_TITLE.equalsIgnoreCase(driver.getTitle())) {
					commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNAL_PAGE_PAY_BUTTON);
				}
				// Checks for any error in the transaction
				String error = "";
				commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
				String currentUrl = driver.getCurrentUrl();
				currentUrl = currentUrl.substring(currentUrl.length() - 4, currentUrl.length());
				logger.info("[webStoreOrder] Trasnaction Status: " + currentUrl);
				if (Constants.STRING_FAIL.equalsIgnoreCase(currentUrl)) {
					error = commonUtil.findElementByCssSelector(driver, prop, Constants.SA_WEB_STORE_ERROR).getText();
				}
				if (Constants.WEBSTORE_GENERAL_ERROR_MESSAGE.equalsIgnoreCase(error)) {
					logger.info("[webStoreOrder] Trying to switch window");
					try {
						commonUtil.switchToWindow(driver, mainWindow);
						logger.info("[webStoreOrder] Switch window successful");
					} catch (Exception e) {
						logger.info("[webStoreOrder] Switch window unsuccessful" + e);
					}
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					driver.get(prop.getProperty(Constants.PAYMENT_EVENTS_URL));
					logger.info("[webStoreOrder] Payment Events is loading");
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					orderNumber = commonUtil.getOrderIdFromPaymentEvents(driver, PaymentProcessingProfile);
					logger.info("[webStoreOrder] Order Number is: " + orderNumber);
					commonUtil.switchToWindow(driver, childWindow);
					commonUtil.clickElementByXPath(driver, prop, Constants.SA_HOME_PAGE_LOGO);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
				} else {
					WebElement orderConfirmationNumber = commonUtil.findElementByXPath(driver, prop,
							Constants.SUITE_COMMERCE_ADVANCED_ORDER_CONFIRMATION_NUMBER);
					orderNumber = StringUtils.substringAfterLast(orderConfirmationNumber.getText(), Constants.HASH);
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WELCOME_USER);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SIGN_OUT);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.closeCurrentWindow(driver);
				commonUtil.switchToWindow(driver, mainWindow);
				logger.info("[webStoreOrder] Ends");
			}
		}
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute capture service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param PaymentCardName String containing name of Card holder
	 * @param Location String containing order location
	 * @param CreditCardNumber String containing number of credit card used to place order
	 * @param ExpirationMonth String containing expiry month of credit card used to place order
	 * @param ExpirationYear String containing expiry year of credit card used to place order
	 * @param CardSecurityCode String containing cvv of credit card used to place order
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param WebSiteName String containing name of website used to place order
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeCapture(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String PaymentCardName, String Location, String CreditCardNumber,
			String ExpirationMonth, String ExpirationYear, String CardSecurityCode, String PaymentProcessingProfile,
			String WebSiteName, String MerchantID, String ProcessorKey, String ProcessorName, String ProcessorLevel,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeCapture] Starts");
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.CAPTURE);
		executor = (JavascriptExecutor) driver;
		executor.executeScript(prop.getProperty(Constants.SALES_ORDER_APPROVE));
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_APPROVE);
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
			commonUtil.setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, 2,
					Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
		}
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.ifAlertPresentAccept(driver);
		// Sales Order- Mark Packed button
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_PACKED);
		// Sales Order- Mark Shipped button
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_PACKED);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_BILL);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.ORDER_CAPTURE);
		commonUtil.writeCurrentTransactionUrl(Constants.CAPTURE_ORDERID_URL, commonUtil.getCurrentUrl(driver));
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		captureId = commonUtil.getOrderId(driver, Constants.CAPTURE_AUTHORIZATION);
		commonUtil.renameFolder(screenShotPath, Constants.CAPTURE, captureId);
		commonUtil.addLogToTestNGReport(Constants.CAPTURE, commonUtil.getCurrentUrl(driver), captureId);

		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.CAPTURE_AUTHORIZATION, ProcessorName,
				ProcessorLevel, prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		commonUtil.addLogToTestNGReport(Constants.CAPTURE, pnReferenceNumber);

		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		logger.info("[executeCapture] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param PaymentCardName String containing name of Card holder
	 * @param Location String containing order location
	 * @param CreditCardNumber String containing number of credit card used to place order
	 * @param ExpirationMonth String containing expiry month of credit card used to place order
	 * @param ExpirationYear String containing expiry year of credit card used to place order
	 * @param CardSecurityCode String containing cvv of credit card used to place order
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param WebSiteName String containing name of website used to place order
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeRefund(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String PaymentCardName, String Location, String CreditCardNumber,
			String ExpirationMonth, String ExpirationYear, String CardSecurityCode, String PaymentProcessingProfile,
			String WebSiteName, String MerchantID, String ProcessorKey, String ProcessorName, String ProcessorLevel,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeRefund] Starts");
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.REFUND);
		String refundId = null;
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
		commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_LOCATION).sendKeys(Keys.DOWN, Keys.DOWN);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		refundId = commonUtil.getOrderId(driver, Constants.REFUND);
		commonUtil.renameFolder(screenShotPath, Constants.REFUND, refundId);
		commonUtil.addLogToTestNGReport(Constants.REFUND, commonUtil.getCurrentUrl(driver), refundId);

		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.REFUND, ProcessorName, ProcessorLevel,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		commonUtil.addLogToTestNGReport(Constants.REFUND, pnReferenceNumber);

		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		logger.info("[executeRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/** 
	 * This method is used to execute AVS validation
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param PaymentCardName String containing name of Card holder
	 * @param Location String containing order location
	 * @param CreditCardNumber String containing number of credit card used to place order
	 * @param ExpirationMonth String containing expiry month of credit card used to place order
	 * @param ExpirationYear String containing expiry year of credit card used to place order
	 * @param CardSecurityCode String containing cvv of credit card used to place order
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorSecretKey String containing details of the processor secret key for transaction
	 * @param SAProfileID String containing secure acceptance profile id
	 * @param SAAccessKey String containing secure acceptance access key
	 * @param SASecretKey String containing secure acceptance secret key
	 * @param WebSiteName String containing name of website used to place order
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
	private void executeAVS(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String PaymentCardName, String Location, String CreditCardNumber,
			String ExpirationMonth, String ExpirationYear, String CardSecurityCode, String PaymentProcessingProfile,
			String MerchantID, String ProcessorName, String ProcessorKey, String ProcessorSecretKey, String SAProfileID,
			String SAAccessKey, String SASecretKey, String WebSiteName, String AVSConfiguration, String DeclineAVSFlag,
			String NoAVSMatch, String AVSServiceNotAvailable, String PartialAVSMatch, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String AVSCode, String TestScenario) throws Exception {
		logger.info("[executeAVS] Starts");
		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		driver = commonUtil.switchToMainWindow(driver);
		commonUtil.setPaymentProcessingProfile(driver, userIndex, PaymentProcessingProfile, MerchantID, ProcessorName,
				ProcessorKey, ProcessorSecretKey, Constants.YES, Constants.CHARECTER_A, NoAVSMatch,
				AVSServiceNotAvailable, PartialAVSMatch, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT,
				Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, SAProfileID, SAAccessKey,
				SASecretKey);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		if (Constants.POSITIVE.toUpperCase().equals(TestScenario.toUpperCase())) {
			webStoreOrder(driver, Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, PaymentCardName,
					Location, CreditCardNumber, ExpirationMonth, ExpirationYear, CardSecurityCode,
					PaymentProcessingProfile, MerchantID, WebSiteName, RequireEBCCheck, ReasonCode, DecisionMsg,
					TestScenario);

			executor = (JavascriptExecutor) driver;
			commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(Keys.DOWN, Keys.ENTER);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.scrollToBottom(driver);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
			authOrderId = commonUtil.getOrderId(driver, Constants.AUTHORIZATION);
			commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, commonUtil.getCurrentUrl(driver), authOrderId);

			commonUtil.clickPaymentEvent(driver, Constants.AUTHORIZATION, Constants.ORDER, Constants.PENDING);
			pnReferenceNumber = commonUtil.checkAvsActuals(driver, Constants.AUTHORIZATION,
					Constants.DECISION_MESSAGE_SA, prop.getProperty(Constants.REASON_CODE_SA),
					prop.getProperty(Constants.AVS_CODE_SA), prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH),
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
	 * This method is used to execute CVN validation
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param PaymentCardName String containing name of Card holder
	 * @param Location String containing order location
	 * @param CreditCardNumber String containing number of credit card used to place order
	 * @param ExpirationMonth String containing expiry month of credit card used to place order
	 * @param ExpirationYear String containing expiry year of credit card used to place order
	 * @param CardSecurityCode String containing cvv of credit card used to place order
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorSecretKey String containing details of the processor secret key for transaction
	 * @param SAProfileID String containing secure acceptance profile id
	 * @param SAAccessKey String containing secure acceptance access key
	 * @param SASecretKey String containing secure acceptance secret key
	 * @param WebSiteName String containing name of website used to place order
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
	private void executeCVN(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String PaymentCardName, String Location, String CreditCardNumber,
			String ExpirationMonth, String ExpirationYear, String CardSecurityCode, String PaymentProcessingProfile,
			String MerchantID, String ProcessorName, String ProcessorKey, String ProcessorSecretKey, String SAProfileID,
			String SAAccessKey, String SASecretKey, String WebSiteName, String CVNConfiguration, String CSCNotSubmitted,
			String CSCNotSubmittedByCardholderBank, String CSCServiceNotAvailable, String CSCCheckFailed,
			String NoCSCMatch, String RequireEBCCheck, String ReasonCode, String DecisionMsg, String CVNCode,
			String TestScenario) throws Exception {
		logger.info("[executeCVN] Starts");
		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		driver = commonUtil.switchToMainWindow(driver);
		commonUtil.setPaymentProcessingProfile(driver, userIndex, PaymentProcessingProfile, MerchantID, ProcessorName,
				ProcessorKey, ProcessorSecretKey, Constants.YES, Constants.CHARECTER_A, Constants.STRING_ACCEPT,
				Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, CSCNotSubmitted, CSCNotSubmittedByCardholderBank,
				CSCServiceNotAvailable, CSCCheckFailed, NoCSCMatch, SAProfileID, SAAccessKey, SASecretKey);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		if (Constants.POSITIVE.toUpperCase().equals(TestScenario.toUpperCase())) {
			webStoreOrder(driver, Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, PaymentCardName,
					Location, CreditCardNumber, ExpirationMonth, ExpirationYear, CardSecurityCode,
					PaymentProcessingProfile, MerchantID, WebSiteName, RequireEBCCheck, ReasonCode, DecisionMsg,
					TestScenario);
			executor = (JavascriptExecutor) driver;
			commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(Keys.DOWN, Keys.ENTER);
			commonUtil.scrollToBottom(driver);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
			authOrderId = commonUtil.getOrderId(driver, Constants.AUTHORIZATION);
			commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, commonUtil.getCurrentUrl(driver), authOrderId);

			commonUtil.clickPaymentEvent(driver, Constants.AUTHORIZATION, Constants.ORDER, Constants.PENDING);
			pnReferenceNumber = commonUtil.checkCvnActuals(driver, Constants.AUTHORIZATION,
					Constants.DECISION_MESSAGE_SA, prop.getProperty(Constants.REASON_CODE_SA_CVN),
					prop.getProperty(Constants.AVS_CODE_SA), prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH),
					ReasonCode, DecisionMsg, TestScenario, CVNCode);
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, pnReferenceNumber);

			if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
				commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
			}
		}
		screenShotPath = screenShotModulePath;
		logger.info("[SecureAcceptance[executeCVN] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute an order with decision manager validation
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param PaymentCardName String containing name of Card holder
	 * @param Location String containing order location
	 * @param CreditCardNumber String containing number of credit card used to place order
	 * @param ExpirationMonth String containing expiry month of credit card used to place order
	 * @param ExpirationYear String containing expiry year of credit card used to place order
	 * @param CardSecurityCode String containing cvv of credit card used to place order
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param WebSiteName String containing name of website used to place order
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param DecisionManagerReject String containing DM reject configuration setup
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	private void executeDM(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String PaymentCardName, String Location, String CreditCardNumber,
			String ExpirationMonth, String ExpirationYear, String CardSecurityCode, String PaymentProcessingProfile,
			String WebSiteName, String MerchantID, String ProcessorKey, String ProcessorLevel,
			String DecisionManagerReject, String RequireEBCCheck, String ReasonCode, String DecisionMsg,
			String TestScenario) throws Exception {
		logger.info("[executeDM] Starts");
		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		driver = commonUtil.switchToMainWindow(driver);
		commonUtil.setPaymentProcessingProfileDM(driver, userIndex, PaymentProcessingProfile, MerchantID, ProcessorKey,
				ProcessorLevel, DecisionManagerReject);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		if (Constants.POSITIVE.toUpperCase().equals(TestScenario.toUpperCase())) {
			webStoreOrder(driver, Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, PaymentCardName,
					Location, CreditCardNumber, ExpirationMonth, ExpirationYear, CardSecurityCode,
					PaymentProcessingProfile, MerchantID, WebSiteName, RequireEBCCheck, ReasonCode, DecisionMsg,
					TestScenario);

			executor = (JavascriptExecutor) driver;
			commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber, Keys.ENTER);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.scrollToBottom(driver);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
			authOrderId = commonUtil.getOrderId(driver, Constants.AUTHORIZATION);
			commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, commonUtil.getCurrentUrl(driver), authOrderId);

			commonUtil.clickPaymentEvent(driver, Constants.AUTHORIZATION, Constants.ORDER, Constants.PENDING);
			authCode = commonUtil.getAuthCode(driver, prop.getProperty(Constants.AUTH_CODE_SECURE_ACCEPTANCE),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH));
			pnReferenceNumber = commonUtil.checkDmActuals(driver, Constants.AUTHORIZATION,
					prop.getProperty(Constants.DECISION_MESSAGE_SA),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), prop.getProperty(Constants.DM_DECISION_SA),
					DecisionMsg, TestScenario);
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, pnReferenceNumber);

			if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
				commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
			}
		}
		screenShotPath = screenShotModulePath;
		logger.info("[SecureAcceptance[executeDM] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to validate AVS for a sale order
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param PaymentCardName String containing name of Card holder
	 * @param Location String containing order location
	 * @param CreditCardNumber String containing number of credit card used to place order
	 * @param ExpirationMonth String containing expiry month of credit card used to place order
	 * @param ExpirationYear String containing expiry year of credit card used to place order
	 * @param CardSecurityCode String containing cvv of credit card used to place order
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorSecretKey String containing details of the processor secret key for transaction
	 * @param SAProfileID String containing secure acceptance profile id
	 * @param SAAccessKey String containing secure acceptance access key
	 * @param SASecretKey String containing secure acceptance secret key
	 * @param WebSiteName String containing name of website used to place order
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
	private void executeAVSSale(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String PaymentCardName, String Location, String CreditCardNumber,
			String ExpirationMonth, String ExpirationYear, String CardSecurityCode, String PaymentProcessingProfile,
			String MerchantID, String ProcessorName, String ProcessorKey, String ProcessorSecretKey, String SAProfileID,
			String SAAccessKey, String SASecretKey, String WebSiteName, String AVSConfiguration, String DeclineAVSFlag,
			String NoAVSMatch, String AVSServiceNotAvailable, String PartialAVSMatch, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String AVSCode, String TestScenario) throws Exception {
		logger.info("[executeAVSSale] Starts");
		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.STRING_SALE);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		driver = commonUtil.switchToMainWindow(driver);
		commonUtil.setPaymentProcessingProfile(driver, userIndex, PaymentProcessingProfile, MerchantID, ProcessorName,
				ProcessorKey, ProcessorSecretKey, Constants.YES, Constants.CHARECTER_A, NoAVSMatch,
				AVSServiceNotAvailable, PartialAVSMatch, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT,
				Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, SAProfileID, SAAccessKey,
				SASecretKey);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		if (Constants.POSITIVE.toUpperCase().equals(TestScenario.toUpperCase())) {
			webStoreOrder(driver, Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, PaymentCardName,
					Location, CreditCardNumber, ExpirationMonth, ExpirationYear, CardSecurityCode,
					PaymentProcessingProfile, MerchantID, WebSiteName, RequireEBCCheck, ReasonCode, DecisionMsg,
					TestScenario);

			executor = (JavascriptExecutor) driver;
			commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber, Keys.ENTER);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.scrollToBottom(driver);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
			saleId = commonUtil.getOrderId(driver, Constants.STRING_SALE);
			commonUtil.renameFolder(screenShotPath, Constants.STRING_SALE, saleId);
			commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), saleId);

			commonUtil.clickPaymentEvent(driver, Constants.STRING_SALE, Constants.ORDER, Constants.PENDING);
			pnReferenceNumber = commonUtil.checkAvsActuals(driver, Constants.STRING_SALE, Constants.DECISION_MESSAGE_SA,
					prop.getProperty(Constants.REASON_CODE_SA), prop.getProperty(Constants.AVS_CODE_SA),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario,
					AVSCode);
			commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);

			if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
				commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
			}
		}
		screenShotPath = screenShotModulePath;
		logger.info("[executeAVSSale] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to validate CVN for a sale order
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param PaymentCardName String containing name of Card holder
	 * @param Location String containing order location
	 * @param CreditCardNumber String containing number of credit card used to place order
	 * @param ExpirationMonth String containing expiry month of credit card used to place order
	 * @param ExpirationYear String containing expiry year of credit card used to place order
	 * @param CardSecurityCode String containing cvv of credit card used to place order
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorSecretKey String containing details of the processor secret key for transaction
	 * @param SAProfileID String containing secure acceptance profile id
	 * @param SAAccessKey String containing secure acceptance access key
	 * @param SASecretKey String containing secure acceptance secret key
	 * @param WebSiteName String containing name of website used to place order
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
	private void executeCVNSale(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String PaymentCardName, String Location, String CreditCardNumber,
			String ExpirationMonth, String ExpirationYear, String CardSecurityCode, String PaymentProcessingProfile,
			String MerchantID, String ProcessorName, String ProcessorKey, String ProcessorSecretKey, String SAProfileID,
			String SAAccessKey, String SASecretKey, String WebSiteName, String CVNConfiguration, String CSCNotSubmitted,
			String CSCNotSubmittedByCardholderBank, String CSCServiceNotAvailable, String CSCCheckFailed,
			String NoCSCMatch, String RequireEBCCheck, String ReasonCode, String DecisionMsg, String CVNCode,
			String TestScenario) throws Exception {
		logger.info("[executeCVNSale] Starts");
		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		driver = commonUtil.switchToMainWindow(driver);
		commonUtil.setPaymentProcessingProfile(driver, userIndex, PaymentProcessingProfile, MerchantID, ProcessorName,
				ProcessorKey, ProcessorSecretKey, Constants.YES, Constants.CHARECTER_A, Constants.STRING_ACCEPT,
				Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, CSCNotSubmitted, CSCNotSubmittedByCardholderBank,
				CSCServiceNotAvailable, CSCCheckFailed, NoCSCMatch, SAProfileID, SAAccessKey, SASecretKey);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		if (Constants.POSITIVE.toUpperCase().equals(TestScenario.toUpperCase())) {
			webStoreOrder(driver, Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, PaymentCardName,
					Location, CreditCardNumber, ExpirationMonth, ExpirationYear, CardSecurityCode,
					PaymentProcessingProfile, MerchantID, WebSiteName, RequireEBCCheck, ReasonCode, DecisionMsg,
					TestScenario);
			executor = (JavascriptExecutor) driver;
			commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber, Keys.ENTER);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.scrollToBottom(driver);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
			saleId = commonUtil.getOrderId(driver, Constants.STRING_SALE);
			commonUtil.renameFolder(screenShotPath, Constants.STRING_SALE, saleId);
			commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), saleId);

			commonUtil.clickPaymentEvent(driver, Constants.STRING_SALE, Constants.ORDER, Constants.PENDING);
			pnReferenceNumber = commonUtil.checkCvnActuals(driver, Constants.STRING_SALE, Constants.DECISION_MESSAGE_SA,
					prop.getProperty(Constants.REASON_CODE_SA_CVN), prop.getProperty(Constants.AVS_CODE_SA),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario,
					CVNCode);
			commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);

			if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
				commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
			}
		}
		screenShotPath = screenShotModulePath;
		logger.info("[executeCVNSale] Ends");
	}

}
