package com.cybersource.test.netsuite;

import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
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
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;
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
 * CreditCardSuiteCommerceAdvanced class which will have all functionality used to place order and do related services using Credit card suite commerce advanced website
 * @author AD20243779
 *
 */
public class CreditCardSuiteCommerceAdvanced extends CommonTest {
	CommonUtil commonUtil = new CommonUtil();
	String orderNumber = null;
	String pnReferenceNumber = null;
	String screenShotPath = null;
	String screenShotModulePath = null;
	String userIndex = "1";
	WebDriver driver = null;
	JavascriptExecutor executor = null;
	static int authProfileConfigFlag = 0;
	static int saleProfileConfigFlag = 0;
	final static Logger logger = Logger.getLogger(CreditCardSuiteCommerceAdvanced.class);

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
		screenShotModulePath = commonUtil.getScreenshotsModulePath(Constants.CREDIT_CARD_SCA);
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
		data = FileUtil.getSheetData(XLSX_FILE_PATH,
				prop.getProperty(prefix + currentScenario + Constants._SHEET_NAME));
		logger.info("[getData] currentScenario: " + currentScenario);
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
	 * @param WebsiteName String containing website used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardEnding String containing last four digits of card number used for transaction
	 * @param CardExpiry String containing expiration date of card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param CardType  String containing type of card used for transaction
	 * @param PPPConfig String containing configuration setup for payment processing profile
	 * @param PPP String containing payment processing profile used
	 * @param MID String containing merchant ID for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthCapture(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String WebsiteName, String ShippingMethod, String Location, String CardEnding, String CardExpiry,
			String CardSecurityCode, String CardType, String PPPConfig, String PPP, String MID, String ProcessorName,
			String ProcessorKey, String ProcessorSecretKey, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthCapture] Starts");
		executeAuth(Email, Password, ItemName, Items, HasMultipleItem, WebsiteName, ShippingMethod, Location,
				CardEnding, CardExpiry, CardSecurityCode, CardType, PPPConfig, PPP, MID, ProcessorName, ProcessorKey,
				ProcessorSecretKey, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeCapture(driver, Items, HasMultipleItem, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario, screenShotPath, screenShotModulePath);
		logger.info("[executeAuthCapture] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund service after authorization and capture services
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param WebsiteName String containing website used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardEnding String containing last four digits of card number used for transaction
	 * @param CardExpiry String containing expiration date of card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param CardType  String containing type of card used for transaction
	 * @param PPPConfig String containing configuration setup for payment processing profile
	 * @param PPP String containing payment processing profile used
	 * @param MID String containing merchant ID for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthCaptureRefund(String Email, String Password, String ItemName, String Items,
			String HasMultipleItem, String WebsiteName, String ShippingMethod, String Location, String CardEnding,
			String CardExpiry, String CardSecurityCode, String CardType, String PPPConfig, String PPP, String MID,
			String ProcessorName, String ProcessorKey, String ProcessorSecretKey, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthCaptureRefund] Starts");
		executeAuth(Email, Password, ItemName, Items, HasMultipleItem, WebsiteName, ShippingMethod, Location,
				CardEnding, CardExpiry, CardSecurityCode, CardType, PPPConfig, PPP, MID, ProcessorName, ProcessorKey,
				ProcessorSecretKey, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeCapture(driver, Items, HasMultipleItem, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario, screenShotPath, screenShotModulePath);
		commonUtil.executeRefund(driver, Location, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario,
				screenShotPath, screenShotModulePath);
		logger.info("[executeAuthCaptureRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute customer refund service after authorization and capture services
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param WebsiteName String containing website used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardEnding String containing last four digits of card number used for transaction
	 * @param CardExpiry String containing expiration date of card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param CardType  String containing type of card used for transaction
	 * @param PPPConfig String containing configuration setup for payment processing profile
	 * @param PPP String containing payment processing profile used
	 * @param MID String containing merchant ID for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthCaptureCustomerRefund(String Email, String Password, String ItemName, String Items,
			String HasMultipleItem, String WebsiteName, String ShippingMethod, String Location, String CardEnding,
			String CardExpiry, String CardSecurityCode, String CardType, String PPPConfig, String PPP, String MID,
			String ProcessorName, String ProcessorKey, String ProcessorSecretKey, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthCaptureCustomerRefund] Starts");
		executeAuth(Email, Password, ItemName, Items, HasMultipleItem, WebsiteName, ShippingMethod, Location,
				CardEnding, CardExpiry, CardSecurityCode, CardType, PPPConfig, PPP, MID, ProcessorName, ProcessorKey,
				ProcessorSecretKey, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeCapture(driver, Items, HasMultipleItem, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario, screenShotPath, screenShotModulePath);
		commonUtil.executeCustomerRefund(driver, Items, HasMultipleItem, Location, RequireEBCCheck, ReasonCode,
				DecisionMsg, TestScenario, screenShotPath, screenShotModulePath);
		logger.info("[executeAuthCaptureCustomerRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute void service after authorization service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param WebsiteName String containing website used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardEnding String containing last four digits of card number used for transaction
	 * @param CardExpiry String containing expiration date of card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param CardType  String containing type of card used for transaction
	 * @param PPPConfig String containing configuration setup for payment processing profile
	 * @param PPP String containing payment processing profile used
	 * @param MID String containing merchant ID for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthVoid(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String WebsiteName, String ShippingMethod, String Location, String CardEnding, String CardExpiry,
			String CardSecurityCode, String CardType, String PPPConfig, String PPP, String MID, String ProcessorName,
			String ProcessorKey, String ProcessorSecretKey, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthVoid] Starts");
		executeAuth(Email, Password, ItemName, Items, HasMultipleItem, WebsiteName, ShippingMethod, Location,
				CardEnding, CardExpiry, CardSecurityCode, CardType, PPPConfig, PPP, MID, ProcessorName, ProcessorKey,
				ProcessorSecretKey, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeVoid(driver, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario, screenShotPath,
				screenShotModulePath);
		logger.info("[executeAuthVoid] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund service after sale service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param WebsiteName String containing website used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardEnding String containing last four digits of card number used for transaction
	 * @param CardExpiry String containing expiration date of card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param CardType  String containing type of card used for transaction
	 * @param PPPConfig String containing configuration setup for payment processing profile
	 * @param PPP String containing payment processing profile used
	 * @param MID String containing merchant ID for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeSaleRefund(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String WebsiteName, String ShippingMethod, String Location, String CardEnding, String CardExpiry,
			String CardSecurityCode, String CardType, String PPPConfig, String PPP, String MID, String ProcessorName,
			String ProcessorKey, String ProcessorSecretKey, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeSaleRefund] Starts");
		executeSale(Email, Password, ItemName, Items, HasMultipleItem, WebsiteName, ShippingMethod, Location,
				CardEnding, CardExpiry, CardSecurityCode, CardType, PPPConfig, PPP, MID, ProcessorName, ProcessorKey,
				ProcessorSecretKey, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeRefund(driver, Location, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario,
				screenShotPath, screenShotModulePath);
		logger.info("[executeSaleRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute authorization service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param WebsiteName String containing website used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardEnding String containing last four digits of card number used for transaction
	 * @param CardExpiry String containing expiration date of card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param CardType  String containing type of card used for transaction
	 * @param PPPConfig String containing configuration setup for payment processing profile
	 * @param PPP String containing payment processing profile used
	 * @param MID String containing merchant ID for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuth(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String WebsiteName, String ShippingMethod, String Location, String CardEnding, String CardExpiry,
			String CardSecurityCode, String CardType, String PPPConfig, String PPP, String MID, String ProcessorName,
			String ProcessorKey, String ProcessorSecretKey, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuth] Starts");
		String authOrderId = null;
		String orderNumber = null;
		String childWindow = null;
		String mainWindow = null;
		Set<String> allWindows = null;
		Iterator<String> itr = null;
		Object itemDetails[][] = null;
		String currentURL = null;
		String error = null;
		List<WebElement> elements = null;
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));

		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		driver = commonUtil.switchToMainWindow(driver);
		if ((Constants.YES.equalsIgnoreCase(PPPConfig)) && (authProfileConfigFlag == 0)) {
			authProfileConfigFlag = 1;
			commonUtil.setPaymentProcessingProfile(driver, userIndex, PPP, MID, ProcessorName, ProcessorKey,
					ProcessorSecretKey, Constants.NO, Constants.CHARECTER_A, Constants.STRING_ACCEPT,
					Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT,
					Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.EMPTY_STRING,
					Constants.EMPTY_STRING, Constants.EMPTY_STRING);
		}
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		commonUtil.clickWebSitePreview(driver, WebsiteName);
		executor = (JavascriptExecutor) driver;
		mainWindow = driver.getWindowHandle();
		allWindows = driver.getWindowHandles();
		logger.info("[executeAuth] window size: " + allWindows.size());
		itr = allWindows.iterator();
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				commonUtil.switchToWindow(driver, childWindow);
				commonUtil.maximizeWindow(driver);
				commonUtil.refreshCurrentPage(driver);
				wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_SHOP_NOW))));
				logger.info("[executeAuth] Login Button Displayed: " + commonUtil
						.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN).isDisplayed());
				try {
					if (commonUtil.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN)
							.isDisplayed()) {
						logger.info("[executeAuth] Clicking login button");
					}
				} catch (Exception e) {
					logger.info("[executeAuth] Logging out previous user");
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
					// For multiple items need to loop through all the given item
					itemDetails = commonUtil.getItemDetails(Items);
					for (int i = 0; i < itemDetails.length; i++) {
						int j = 0;
						commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SHOP);
						commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
						executor.executeScript(
								prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_SCROLL_WINDOW_TILL_ITEM_VISIBLE));
						commonUtil.clickElementByLinkText(driver, itemDetails[i][j].toString());
						commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
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
							.visibilityOfElementLocated(By.xpath(prop.getProperty(Constants.TEST_AIR_PODS_XPATH))));
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					// commonUtil.clickElementByXPath(driver, prop, Constants.TEST_AIR_PODS_XPATH);
					commonUtil.clickElementByLinkText(driver, ItemName);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_ADD_TO_CART);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_4);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_VIEW_CART_CHECKOUT);
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.CART_DETAILS);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PROCEED_TO_CHECKOUT);
				commonUtil.setShippingMethodSCA(driver, ShippingMethod);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);

				commonUtil.clickElementByXPath(driver, prop, Constants.WEBSTORE_PAYMENT_METHOD_CREDIT_CARD);
				commonUtil.selectCard(driver, CardEnding, CardExpiry, CardType);

				commonUtil.setElementValueByXPath(driver, prop, Constants.WEBSTORE_CREDIT_CARD_CVN, CardSecurityCode);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.PAYMENT_METHOD);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PLACE_ORDER);
				// Checking for any error in the transaction
				for (int i = 0; i < browserMaxTimeoutSeconds; i++) {
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					currentURL = driver.getCurrentUrl();
					if ((ReasonCode.contains(Constants.STRING_REJECT))
							|| (DecisionMsg.contains(Constants.STRING_REJECT))) {
						elements = commonUtil.findElementsByXPath(driver, prop, Constants.WEB_STORE_ERROR);
					}
					if ((currentURL.contains(Constants.LAST_ORDER_ID)) || (elements != null)) {
						logger.info("[executeAuth] Checking Trasaction URL Status: " + currentURL);
						String[] orderState = currentURL.split("#");
						logger.info("[executeAuth] Transaction status: " + orderState[1]);
						if (Constants.STRING_REVIEW.equals(orderState[1])) {
							error = commonUtil.getElementTextByXPath(driver, prop, Constants.WEB_STORE_ERROR).trim();
							logger.info("[executeAuth] Error Message: " + error);
						}
						break;
					}
				}
				if (error != null) {
					logger.info("[executeAuth] Rejected transaction verification");
					try {
						commonUtil.switchToWindow(driver, mainWindow);
						logger.info("[executeAuth] Switch window successful");
					} catch (Exception e) {
						logger.info("[executeAuth] Switch window unsuccessful: " + e);
					}
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					driver.get(prop.getProperty(Constants.PAYMENT_EVENTS_URL));
					logger.info("[executeAuth] Payment Events is loading");
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					orderNumber = commonUtil.getOrderIdFromPaymentEvents(driver, PPP);
					logger.info("[executeAuth] Order Number is: " + orderNumber);
					commonUtil.switchToWindow(driver, childWindow);
					commonUtil.clickElementByXPath(driver, prop, Constants.SA_HOME_PAGE_LOGO);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
				} else {
					WebElement orderConfirmationNumber = commonUtil.findElementByXPath(driver, prop,
							Constants.SUITE_COMMERCE_ADVANCED_ORDER_CONFIRMATION_NUMBER);
					if (null != orderConfirmationNumber) {
						orderNumber = StringUtils.substringAfterLast(orderConfirmationNumber.getText(), Constants.HASH);
					}
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WELCOME_USER);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SIGN_OUT);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.closeCurrentWindow(driver);
			}
		}
		commonUtil.switchToWindow(driver, mainWindow);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(Keys.DOWN, Keys.ENTER);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.scrollToBottom(driver);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		authOrderId = commonUtil.getOrderId(driver, Constants.AUTHORIZATION);
		commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
		commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, commonUtil.getCurrentUrl(driver), authOrderId);

		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.AUTHORIZATION,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, pnReferenceNumber);

		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		logger.info("[executeAuth] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute sale service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param WebsiteName String containing website used for transaction
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardEnding String containing last four digits of card number used for transaction
	 * @param CardExpiry String containing expiration date of card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param CardType  String containing type of card used for transaction
	 * @param PPPConfig String containing configuration setup for payment processing profile
	 * @param PPP String containing payment processing profile used
	 * @param MID String containing merchant ID for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeSale(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String WebsiteName, String ShippingMethod, String Location, String CardEnding, String CardExpiry,
			String CardSecurityCode, String CardType, String PPPConfig, String PPP, String MID, String ProcessorName,
			String ProcessorKey, String ProcessorSecretKey, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeSale] Starts");
		String saleId = null;
		String orderNumber = null;
		String childWindow = null;
		String mainWindow = null;
		Set<String> allWindows = null;
		Iterator<String> itr = null;
		Object itemDetails[][] = null;
		String currentURL = null;
		String error = null;
		List<WebElement> elements = null;
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		driver = commonUtil.switchToMainWindow(driver);
		if ((Constants.YES.equalsIgnoreCase(PPPConfig)) && (saleProfileConfigFlag == 0)) {
			saleProfileConfigFlag = 1;
			commonUtil.setPaymentProcessingProfile(driver, userIndex, PPP, MID, ProcessorName, ProcessorKey,
					ProcessorSecretKey, Constants.NO, Constants.CHARECTER_A, Constants.STRING_ACCEPT,
					Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT,
					Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.EMPTY_STRING,
					Constants.EMPTY_STRING, Constants.EMPTY_STRING);
		}
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		commonUtil.clickWebSitePreview(driver, WebsiteName);
		executor = (JavascriptExecutor) driver;
		mainWindow = driver.getWindowHandle();
		allWindows = driver.getWindowHandles();
		itr = allWindows.iterator();
		childWindow = null;
		while (itr.hasNext()) {
			childWindow = null;
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				commonUtil.switchToWindow(driver, childWindow);
				commonUtil.maximizeWindow(driver);
				commonUtil.refreshCurrentPage(driver);
				wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_SHOP_NOW))));
				logger.info("[executeSale] Login Button Displayed: " + commonUtil
						.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN).isDisplayed());
				try {
					if (commonUtil.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN)
							.isDisplayed()) {
						logger.info("[executeSale] Clicking login button");
					}
				} catch (Exception e) {
					logger.info("[executeSale] Logging out previous user");
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
					// For multiple items need to loop through all the given item
					itemDetails = commonUtil.getItemDetails(Items);
					for (int i = 0; i < itemDetails.length; i++) {
						int j = 0;
						commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SHOP);
						commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
						executor.executeScript(
								prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_SCROLL_WINDOW_TILL_ITEM_VISIBLE));
						commonUtil.clickElementByLinkText(driver, itemDetails[i][j].toString());
						commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
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
					wait.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath(prop.getProperty(Constants.TEST_AIR_PODS_XPATH))));
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					// commonUtil.clickElementByXPath(driver, prop, Constants.TEST_AIR_PODS_XPATH);
					commonUtil.clickElementByLinkText(driver, ItemName);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_ADD_TO_CART);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_4);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_VIEW_CART_CHECKOUT);
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.CART_DETAILS);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PROCEED_TO_CHECKOUT);
				commonUtil.setShippingMethodSCA(driver, ShippingMethod);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.WEBSTORE_PAYMENT_METHOD_CREDIT_CARD);
				commonUtil.selectCard(driver, CardEnding, CardExpiry, CardType);
				commonUtil.setElementValueByXPath(driver, prop, Constants.WEBSTORE_CREDIT_CARD_CVN, CardSecurityCode);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.PAYMENT_METHOD);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PLACE_ORDER);
				// Checking for any error in the transaction
				for (int i = 0; i < browserMaxTimeoutSeconds; i++) {
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					currentURL = driver.getCurrentUrl();
					if ((ReasonCode.contains(Constants.STRING_REJECT))
							|| (DecisionMsg.contains(Constants.STRING_REJECT))) {
						elements = commonUtil.findElementsByXPath(driver, prop, Constants.WEB_STORE_ERROR);
					}
					if ((currentURL.contains(Constants.LAST_ORDER_ID)) || (elements != null)) {
						logger.info("[executeSale] Checking Trasaction URL Status: " + currentURL);
						String[] orderState = currentURL.split("#");
						logger.info("[executeSale] Transaction status: " + orderState[1]);
						if (Constants.STRING_REVIEW.equals(orderState[1])) {
							error = commonUtil.getElementTextByXPath(driver, prop, Constants.WEB_STORE_ERROR).trim();
							logger.info("[executeSale] Error Message: " + error);
						}
						break;
					}
				}
				if (error != null) {
					logger.info("[executeSale] Rejected transaction verification");
					try {
						commonUtil.switchToWindow(driver, mainWindow);
						logger.info("[executeSale] Switch window successful");
					} catch (Exception e) {
						logger.info("[executeSale] Switch window unsuccessful: " + e);
					}
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					driver.get(prop.getProperty(Constants.PAYMENT_EVENTS_URL));
					logger.info("[executeSale] Payment Events is loading");
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					orderNumber = commonUtil.getOrderIdFromPaymentEvents(driver, PPP);
					logger.info("[executeSale] Order Number is: " + orderNumber);
					commonUtil.switchToWindow(driver, childWindow);
					commonUtil.clickElementByXPath(driver, prop, Constants.SA_HOME_PAGE_LOGO);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
				} else {
					WebElement orderConfirmationNumber = commonUtil.findElementByXPath(driver, prop,
							Constants.SUITE_COMMERCE_ADVANCED_ORDER_CONFIRMATION_NUMBER);
					if (null != orderConfirmationNumber) {
						orderNumber = StringUtils.substringAfterLast(orderConfirmationNumber.getText(), Constants.HASH);
						logger.info("[executeSale] Order Number is: " + orderNumber);
					}
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WELCOME_USER);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SIGN_OUT);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.closeCurrentWindow(driver);
			}
		}
		commonUtil.switchToWindow(driver, mainWindow);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(Keys.DOWN, Keys.ENTER);

		commonUtil.scrollToBottom(driver);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		saleId = commonUtil.getOrderId(driver, Constants.STRING_SALE);
		commonUtil.renameFolder(screenShotPath, Constants.STRING_SALE, saleId);
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), saleId);

		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.STRING_SALE,
				prop.getProperty(Constants.SALE_DECISION_MESSAGE_XPATH),
				prop.getProperty(Constants.SALE_REASON_CODE_XPATH),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);

		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		logger.info("[executeSale] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute AVS validation
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardEnding String containing last four digits of card number used for transaction
	 * @param CardExpiry String containing expiration date of card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param CardType  String containing type of card used for transaction
	 * @param PPP String containing payment processing profile used
	 * @param MID String containing merchant ID for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorSecretKey  String containing details of the processor secret key for transaction
	 * @param WebsiteName String containing website used for transaction
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
			String ShippingMethod, String Location, String CardEnding, String CardExpiry, String CardSecurityCode,
			String CardType, String PPP, String MID, String ProcessorName, String ProcessorKey,
			String ProcessorSecretKey, String WebsiteName, String AVSConfiguration, String DeclineAVSFlag,
			String NoAVSMatch, String AVSServiceNotAvailable, String PartialAVSMatch, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String AVSCode, String TestScenario) throws Exception {
		logger.info("[executeAVS] Starts");
		String authOrderId = null;
		String orderNumber = null;
		String childWindow = null;
		String mainWindow = null;
		String error = null;
		String currentURL = null;
		List<WebElement> elements = null;
		Set<String> allWindows = null;
		Iterator<String> itr = null;
		Object itemDetails[][] = null;
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		driver = commonUtil.switchToMainWindow(driver);
		commonUtil.setPaymentProcessingProfile(driver, userIndex, PPP, MID, ProcessorName, ProcessorKey,
				ProcessorSecretKey, Constants.YES, Constants.CHARECTER_A, NoAVSMatch, AVSServiceNotAvailable,
				PartialAVSMatch, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT,
				Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.EMPTY_STRING, Constants.EMPTY_STRING,
				Constants.EMPTY_STRING);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		commonUtil.clickWebSitePreview(driver, WebsiteName);
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
						logger.info("[executeAVS] Clicking login button");
					}
				} catch (Exception e) {
					logger.info("[executeAVS] Logging out previous user");
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
					// For multiple items need to loop through all the given item
					itemDetails = commonUtil.getItemDetails(Items);
					for (int i = 0; i < itemDetails.length; i++) {
						int j = 0;
						commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SHOP);
						commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
						executor.executeScript(
								prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_SCROLL_WINDOW_TILL_ITEM_VISIBLE));
						commonUtil.clickElementByLinkText(driver, itemDetails[i][j].toString());
						commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
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
					wait.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath(prop.getProperty(Constants.TEST_AIR_PODS_XPATH))));
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					// commonUtil.clickElementByXPath(driver, prop, Constants.TEST_AIR_PODS_XPATH);
					commonUtil.clickElementByLinkText(driver, ItemName);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_ADD_TO_CART);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_4);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_VIEW_CART_CHECKOUT);
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.CART_DETAILS);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PROCEED_TO_CHECKOUT);
				commonUtil.setShippingMethodSCA(driver, ShippingMethod);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);

				commonUtil.clickElementByXPath(driver, prop, Constants.WEBSTORE_PAYMENT_METHOD_CREDIT_CARD);
				commonUtil.selectCard(driver, CardEnding, CardExpiry, CardType);
				commonUtil.setElementValueByXPath(driver, prop, Constants.WEBSTORE_CREDIT_CARD_CVN, CardSecurityCode);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.PAYMENT_METHOD);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PLACE_ORDER);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				// Checking for any error in the transaction
				for (int i = 0; i < browserMaxTimeoutSeconds; i++) {
					currentURL = driver.getCurrentUrl();
					if ((Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(NoAVSMatch))
							|| (Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(AVSServiceNotAvailable))
							|| (Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(PartialAVSMatch))) {
						elements = commonUtil.findElementsByXPath(driver, prop, Constants.WEB_STORE_ERROR);
					}
					if ((currentURL.contains(Constants.LAST_ORDER_ID)) || (elements != null)) {
						logger.info("[executeAVS] Checking Trasaction URL Status: " + currentURL);
						String[] orderState = currentURL.split("#");
						logger.info("[executeAVS] Transaction status: " + orderState[1]);
						if (Constants.STRING_REVIEW.equals(orderState[1])) {
							error = commonUtil.getElementTextByXPath(driver, prop, Constants.WEB_STORE_ERROR).trim();
							logger.info("[executeAVS] Error Message: " + error);
						}
						break;
					} else {
						commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					}
				}

				if (Constants.WEBSTORE_AVS_REJECT_MESSAGE.equalsIgnoreCase(error)) {
					logger.info("[executeAVS] Rejected transaction verification");
					try {
						commonUtil.switchToWindow(driver, mainWindow);
						logger.info("[executeAVS] Switch window successful");
					} catch (Exception e) {
						logger.info("[executeAVS] Switch window unsuccessful" + e);
					}
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					driver.get(prop.getProperty(Constants.PAYMENT_EVENTS_URL));
					logger.info("[executeAVS] Payment Events is loading");
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					orderNumber = commonUtil.getOrderIdFromPaymentEvents(driver, PPP);
					logger.info("[executeAVS] Order Number is: " + orderNumber);
					commonUtil.switchToWindow(driver, childWindow);
					commonUtil.clickElementByXPath(driver, prop, Constants.SA_HOME_PAGE_LOGO);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
				} else {
					WebElement orderConfirmationNumber = commonUtil.findElementByXPath(driver, prop,
							Constants.SUITE_COMMERCE_ADVANCED_ORDER_CONFIRMATION_NUMBER);
					if (null != orderConfirmationNumber) {
						orderNumber = StringUtils.substringAfterLast(orderConfirmationNumber.getText(), Constants.HASH);
					}
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WELCOME_USER);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SIGN_OUT);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.closeCurrentWindow(driver);
			}
		}
		commonUtil.switchToWindow(driver, mainWindow);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(Keys.DOWN, Keys.ENTER);
		commonUtil.scrollToBottom(driver);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		authOrderId = commonUtil.getOrderId(driver, Constants.AUTHORIZATION);
		commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
		commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, commonUtil.getCurrentUrl(driver), authOrderId);

		pnReferenceNumber = commonUtil.checkAvsActuals(driver, Constants.AUTHORIZATION,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.AVS_CODE), prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode,
				DecisionMsg, TestScenario, AVSCode);
		commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, pnReferenceNumber);

		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		screenShotPath = screenShotModulePath;
		logger.info("[executeAVS] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute CVN validation
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardEnding String containing last four digits of card number used for transaction
	 * @param CardExpiry String containing expiration date of card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param CardType  String containing type of card used for transaction
	 * @param PPP String containing payment processing profile used
	 * @param MID String containing merchant ID for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorSecretKey  String containing details of the processor secret key for transaction
	 * @param WebsiteName String containing website used for transaction
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
			String ShippingMethod, String Location, String CardEnding, String CardExpiry, String CardSecurityCode,
			String CardType, String PPP, String MID, String ProcessorName, String ProcessorKey,
			String ProcessorSecretKey, String WebsiteName, String CVNConfiguration, String CSCNotSubmitted,
			String CSCNotSubmittedByCardholderBank, String CSCServiceNotAvailable, String CSCCheckFailed,
			String NoCSCMatch, String RequireEBCCheck, String ReasonCode, String DecisionMsg, String CVNCode,
			String TestScenario) throws Exception {
		logger.info("[executeCVN] Starts");
		String authOrderId = null;
		String orderNumber = null;
		String childWindow = null;
		String mainWindow = null;
		String currentURL = null;
		String error = null;
		List<WebElement> elements = null;
		Set<String> allWindows = null;
		Iterator<String> itr = null;
		Object itemDetails[][] = null;
		driver = null;
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		driver = commonUtil.switchToMainWindow(driver);
		commonUtil.setPaymentProcessingProfile(driver, userIndex, PPP, MID, ProcessorName, ProcessorKey,
				ProcessorSecretKey, Constants.YES, Constants.CHARECTER_A, Constants.STRING_ACCEPT,
				Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, CSCNotSubmitted, CSCNotSubmittedByCardholderBank,
				CSCServiceNotAvailable, CSCCheckFailed, NoCSCMatch, Constants.EMPTY_STRING, Constants.EMPTY_STRING,
				Constants.EMPTY_STRING);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		commonUtil.clickWebSitePreview(driver, WebsiteName);
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
				logger.info("[executeCVN] Login Button Displayed: " + commonUtil
						.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN).isDisplayed());
				try {
					if (commonUtil.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN)
							.isDisplayed()) {
						logger.info("[WebStoreOrder] Clicking login button");
					}
				} catch (Exception e) {
					logger.info("[WebStoreOrder] Logging out previous user");
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
					// For multiple items need to loop through all the given item
					itemDetails = commonUtil.getItemDetails(Items);
					for (int i = 0; i < itemDetails.length; i++) {
						int j = 0;
						commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SHOP);
						commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
						executor.executeScript(
								prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_SCROLL_WINDOW_TILL_ITEM_VISIBLE));
						commonUtil.clickElementByLinkText(driver, itemDetails[i][j].toString());
						commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
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
					wait.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath(prop.getProperty(Constants.TEST_AIR_PODS_XPATH))));
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					// commonUtil.clickElementByXPath(driver, prop, Constants.TEST_AIR_PODS_XPATH);
					commonUtil.clickElementByLinkText(driver, ItemName);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_ADD_TO_CART);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_4);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_VIEW_CART_CHECKOUT);
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.CART_DETAILS);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PROCEED_TO_CHECKOUT);
				commonUtil.setShippingMethodSCA(driver, ShippingMethod);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.WEBSTORE_PAYMENT_METHOD_CREDIT_CARD);
				commonUtil.selectCard(driver, CardEnding, CardExpiry, CardType);
				commonUtil.setElementValueByXPath(driver, prop, Constants.WEBSTORE_CREDIT_CARD_CVN, CardSecurityCode);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.PAYMENT_METHOD);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PLACE_ORDER);
				// Checking for any error in the transaction
				for (int i = 0; i < browserMaxTimeoutSeconds; i++) {
					currentURL = driver.getCurrentUrl();
					if ((Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(CSCNotSubmitted))
							|| (Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(CSCNotSubmittedByCardholderBank))
							|| (Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(CSCServiceNotAvailable))
							|| (Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(CSCCheckFailed))
							|| (Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(NoCSCMatch))
							|| (DecisionMsg.contains(Constants.STRING_REJECT))) {
						elements = commonUtil.findElementsByXPath(driver, prop, Constants.WEB_STORE_ERROR);
					}
					if ((currentURL.contains(Constants.LAST_ORDER_ID)) || (elements != null)) {
						logger.info("[executeCVN] Checking Trasaction URL Status: " + currentURL);
						String[] orderState = currentURL.split("#");
						logger.info("[executeCVN] Transaction status: " + orderState[1]);
						if (Constants.STRING_REVIEW.equals(orderState[1])) {
							error = commonUtil.getElementTextByXPath(driver, prop, Constants.WEB_STORE_ERROR).trim();
							logger.info("[executeCVN] Error Message: " + error);
						}
						break;
					} else {
						commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					}
				}
				if (Constants.WEBSTORE_CVN_REJECT_MESSAGE.equalsIgnoreCase(error)
						|| Constants.WEBSTORE_CVN_SOFT_DECLINE_FLAGGED_MESSAGE.equals(error)
						|| Constants.WEBSTORE_CVN_SOFT_DECLINE_DECLINED_MESSAGE.equals(error)) {
					logger.info("[executeCVN] Trying to switch window");
					try {
						commonUtil.switchToWindow(driver, mainWindow);
						logger.info("[executeCVN] Switch window successful");
					} catch (Exception e) {
						logger.info("[executeCVN] Switch window unsuccessful" + e);
					}
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					driver.get(prop.getProperty(Constants.PAYMENT_EVENTS_URL));
					logger.info("[executeCVN] Payment Events is loading");
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					orderNumber = commonUtil.getOrderIdFromPaymentEvents(driver, PPP);
					logger.info("[executeCVN] Order Number is: " + orderNumber);
					commonUtil.switchToWindow(driver, childWindow);
					commonUtil.clickElementByXPath(driver, prop, Constants.SA_HOME_PAGE_LOGO);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
				} else {
					WebElement orderConfirmationNumber = commonUtil.findElementByXPath(driver, prop,
							Constants.SUITE_COMMERCE_ADVANCED_ORDER_CONFIRMATION_NUMBER);
					if (null != orderConfirmationNumber) {
						orderNumber = StringUtils.substringAfterLast(orderConfirmationNumber.getText(), Constants.HASH);
					}
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.refreshCurrentPage(driver);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WELCOME_USER);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SIGN_OUT);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.closeCurrentWindow(driver);
			}
		}
		commonUtil.switchToWindow(driver, mainWindow);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(Keys.DOWN, Keys.ENTER);
		commonUtil.scrollToBottom(driver);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);

		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		authOrderId = commonUtil.getOrderId(driver, Constants.AUTHORIZATION);
		commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
		commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, commonUtil.getCurrentUrl(driver), authOrderId);

		pnReferenceNumber = commonUtil.checkCvnActuals(driver, Constants.AUTHORIZATION,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.CVN_CODE), prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode,
				DecisionMsg, TestScenario, CVNCode);
		commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, pnReferenceNumber);

		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		screenShotPath = screenShotModulePath;
		logger.info("[executeCVN] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute sale service with AVS validation
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardEnding String containing last four digits of card number used for transaction
	 * @param CardExpiry String containing expiration date of card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param CardType  String containing type of card used for transaction
	 * @param PPP String containing payment processing profile used
	 * @param MID String containing merchant ID for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorSecretKey  String containing details of the processor secret key for transaction
	 * @param WebsiteName String containing website used for transaction
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
			String ShippingMethod, String Location, String CardEnding, String CardExpiry, String CardSecurityCode,
			String CardType, String PPP, String MID, String ProcessorName, String ProcessorKey,
			String ProcessorSecretKey, String WebsiteName, String AVSConfiguration, String DeclineAVSFlag,
			String NoAVSMatch, String AVSServiceNotAvailable, String PartialAVSMatch, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String AVSCode, String TestScenario) throws Exception {
		logger.info("[executeAVSSale] Starts");
		String authOrderId = null;
		String orderNumber = null;
		String childWindow = null;
		String mainWindow = null;
		Set<String> allWindows = null;
		Iterator<String> itr = null;
		Object itemDetails[][] = null;
		String currentURL = null;
		String error = null;
		List<WebElement> elements = null;
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		driver = commonUtil.switchToMainWindow(driver);
		commonUtil.setPaymentProcessingProfile(driver, userIndex, PPP, MID, ProcessorName, ProcessorKey,
				ProcessorSecretKey, Constants.YES, Constants.CHARECTER_A, NoAVSMatch, AVSServiceNotAvailable,
				PartialAVSMatch, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.STRING_ACCEPT,
				Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, Constants.EMPTY_STRING, Constants.EMPTY_STRING,
				Constants.EMPTY_STRING);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		commonUtil.clickWebSitePreview(driver, WebsiteName);
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
				logger.info("[executeAVS] Login Button Displayed: " + commonUtil
						.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN).isDisplayed());
				try {
					if (commonUtil.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN)
							.isDisplayed()) {
						logger.info("[executeAVSSale] Clicking login button");
					}
				} catch (Exception e) {
					logger.info("[executeAVSSale] Logging out previous user");
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
					// For multiple items need to loop through all the given item
					itemDetails = commonUtil.getItemDetails(Items);
					for (int i = 0; i < itemDetails.length; i++) {
						int j = 0;
						commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SHOP);
						commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
						executor.executeScript(
								prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_SCROLL_WINDOW_TILL_ITEM_VISIBLE));
						commonUtil.clickElementByLinkText(driver, itemDetails[i][j].toString());
						commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
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
					wait.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath(prop.getProperty(Constants.TEST_AIR_PODS_XPATH))));
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					// commonUtil.clickElementByXPath(driver, prop, Constants.TEST_AIR_PODS_XPATH);
					commonUtil.clickElementByLinkText(driver, ItemName);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_ADD_TO_CART);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_4);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_VIEW_CART_CHECKOUT);
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.CART_DETAILS);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PROCEED_TO_CHECKOUT);
				commonUtil.setShippingMethodSCA(driver, ShippingMethod);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.WEBSTORE_PAYMENT_METHOD_CREDIT_CARD);
				commonUtil.selectCard(driver, CardEnding, CardExpiry, CardType);
				commonUtil.setElementValueByXPath(driver, prop, Constants.WEBSTORE_CREDIT_CARD_CVN, CardSecurityCode);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.PAYMENT_METHOD);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PLACE_ORDER);
				// Checking for any error in the transaction
				for (int i = 0; i < browserMaxTimeoutSeconds; i++) {
					currentURL = driver.getCurrentUrl();
					if ((Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(NoAVSMatch))
							|| (Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(AVSServiceNotAvailable))
							|| (Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(PartialAVSMatch))) {
						elements = commonUtil.findElementsByXPath(driver, prop, Constants.WEB_STORE_ERROR);
					}
					if ((currentURL.contains(Constants.LAST_ORDER_ID)) || (elements != null)) {
						logger.info("[executeAVSSale] Checking Trasaction URL Status: " + currentURL);
						String[] orderState = currentURL.split("#");
						logger.info("[executeAVSSale] Transaction status: " + orderState[1]);
						if (Constants.STRING_REVIEW.equals(orderState[1])) {
							error = commonUtil.getElementTextByXPath(driver, prop, Constants.WEB_STORE_ERROR).trim();
							logger.info("[executeAVSSale] Error Message: " + error);
						}
						break;
					} else {
						commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					}
				}
				if (Constants.WEBSTORE_AVS_REJECT_MESSAGE.equalsIgnoreCase(error)) {
					logger.info("[executeAVSSale] Trying to switch window");
					try {
						commonUtil.switchToWindow(driver, mainWindow);
						logger.info("[executeAVSSale] Switch window successful");
					} catch (Exception e) {
						logger.info("[executeAVSSale] Switch window unsuccessful" + e);
					}
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					driver.get(prop.getProperty(Constants.PAYMENT_EVENTS_URL));
					logger.info("[executeAVSSale] Payment Events is loading");
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					orderNumber = commonUtil.getOrderIdFromPaymentEvents(driver, PPP);
					logger.info("[executeAVSSale] Order Number is: " + orderNumber);
					commonUtil.switchToWindow(driver, childWindow);
					commonUtil.clickElementByXPath(driver, prop, Constants.SA_HOME_PAGE_LOGO);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
				} else {
					WebElement orderConfirmationNumber = commonUtil.findElementByXPath(driver, prop,
							Constants.SUITE_COMMERCE_ADVANCED_ORDER_CONFIRMATION_NUMBER);
					if (null != orderConfirmationNumber) {
						orderNumber = StringUtils.substringAfterLast(orderConfirmationNumber.getText(), Constants.HASH);
					}
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.refreshCurrentPage(driver);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WELCOME_USER);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SIGN_OUT);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.closeCurrentWindow(driver);
			}
		}
		commonUtil.switchToWindow(driver, mainWindow);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(Keys.DOWN, Keys.ENTER);
		commonUtil.scrollToBottom(driver);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		// driver = commonUtil.setupDriver(prop, Constants.TEMP_TEST_URL);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		WebElement table = commonUtil.findElementById(driver, prop, Constants.PAYMENT_EVENT_TABLE_ID);
		List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
		List<WebElement> cells = null;
		for (WebElement row : allRows) {
			cells = row.findElements(By.tagName(Constants.TD));
			String paymentEvent = cells.get(2).getText();
			if (paymentEvent.equals("Sale")) {
				authOrderId = cells.get(1).getText().split("#")[1];
				logger.info("[executeAVSSale] order id: " + authOrderId);
				cells.get(8).findElement(By.id(prop.getProperty(Constants.PAYMENT_EVENT_TRANSACTION_COLUMN_ID)))
						.click();
				commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
				mainWindow = driver.getWindowHandle();
				allWindows = driver.getWindowHandles();
				itr = allWindows.iterator();
				while (itr.hasNext()) {
					childWindow = itr.next();
					if (!mainWindow.equals(childWindow)) {
						commonUtil.switchToWindow(driver, childWindow);
						commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
						if (!Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.PROCESS_TYPE_REST))) {
							String rawResponse = driver
									.findElement(By.xpath(
											"//*[@id='tr_fg_fldgrouprawresponse']/td/table/tbody/tr/td/div/span[2]"))
									.getText();
							String[] raw = rawResponse.split("\n");
							for (int i = 0; i < raw.length; i++) {
								if (raw[i].contains("c:decision:")) {
									logger.info("[executeAVSSale] Decision from xls " + DecisionMsg);
									logger.info("[executeAVSSale] Decision from web " + raw[i]);
									Assert.assertEquals(DecisionMsg, raw[i]);
								}
								if (raw[i].contains("c:reasonCode:")) {
									logger.info("[executeAVSSale] Reason code from xls " + ReasonCode);
									logger.info("[executeAVSSale] Reason code from web " + raw[i]);
									Assert.assertEquals(ReasonCode, raw[i]);
								}
								if (raw[i].contains("c:avsCode:")) {
									logger.info("[executeAVSSale] AVS code from xls " + AVSCode);
									logger.info("[executeAVSSale] AVS code from web " + raw[i]);
									Assert.assertEquals(AVSCode, raw[i]);
									String[] AVSCVNCode = raw[i].split(Constants.COLON);
									Reporter.log("AVS Code" + ": " + AVSCVNCode[AVSCVNCode.length - 1]);
								}
								if (raw[i].contains("c:requestID:")) {
									String[] authCode = raw[i].split(": ");
									logger.info(authCode[1]);
									pnReferenceNumber = authCode[1];
								}
							}
						} else {
							List<WebElement> pres = driver
									.findElements(By.xpath(prop.getProperty(Constants.REST_RESPONSE_XPATH)));
							String[] lines = pres.get(3).getText().trim().split("\\R");
							for (int i = 0; i < lines.length; i++) {
								if (lines[i].contains(prop.getProperty(Constants.REST_STATUS))) {
									logger.info("[executeAVSSale] Decision from xls " + DecisionMsg);
									logger.info("[executeAVSSale] Decision from web " + lines[i].trim());
									Assert.assertEquals(DecisionMsg, lines[i].trim());
								}
								if (lines[i].contains(prop.getProperty(Constants.REST_AVS))) {
									logger.info("[executeAVSSale] AVS code from xls " + AVSCode);
									logger.info("[executeAVSSale] AVS code from web " + lines[i + 1].trim());
									Assert.assertEquals(AVSCode, lines[i + 1].trim());
									Reporter.log("AVS Code" + ": " + lines[i + 1].trim());
								}
								if (lines[i].contains(prop.getProperty(Constants.REST_PN_REF))) {
									pnReferenceNumber = lines[i].split(":")[1].trim().replace("\"", "");
								}
							}
						}
					}
					commonUtil.closeCurrentWindow(driver);
					commonUtil.switchToWindow(driver, mainWindow);
				}
			}
		}
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), authOrderId);
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);
		screenShotPath = screenShotModulePath;
		logger.info("[executeAVSSale] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute sale order with CVN validation
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing item name to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param ShippingMethod String containing detail of shipping method for order
	 * @param Location String containing order location
	 * @param CardEnding String containing last four digits of card number used for transaction
	 * @param CardExpiry String containing expiration date of card used for transaction
	 * @param CardSecurityCode String containing security code of credit card used for transaction
	 * @param CardType  String containing type of card used for transaction
	 * @param PPP String containing payment processing profile used
	 * @param MID String containing merchant ID for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorKey String containing details of the processor key for transaction
	 * @param ProcessorSecretKey  String containing details of the processor secret key for transaction
	 * @param WebsiteName String containing website used for transaction
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
			String ShippingMethod, String Location, String CardEnding, String CardExpiry, String CardSecurityCode,
			String CardType, String PPP, String MID, String ProcessorName, String ProcessorKey,
			String ProcessorSecretKey, String WebsiteName, String CVNConfiguration, String CSCNotSubmitted,
			String CSCNotSubmittedByCardholderBank, String CSCServiceNotAvailable, String CSCCheckFailed,
			String NoCSCMatch, String RequireEBCCheck, String ReasonCode, String DecisionMsg, String CVNCode,
			String TestScenario) throws Exception {
		logger.info("[executeCVNSale] Starts");
		String authOrderId = null;
		String orderNumber = null;
		String childWindow = null;
		String mainWindow = null;
		Set<String> allWindows = null;
		Iterator<String> itr = null;
		Object itemDetails[][] = null;
		String currentURL = null;
		String error = null;
		List<WebElement> elements = null;
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		commonUtil.ifAlertPresentAccept(driver);
		driver = commonUtil.switchToMainWindow(driver);
		commonUtil.setPaymentProcessingProfile(driver, userIndex, PPP, MID, ProcessorName, ProcessorKey,
				ProcessorSecretKey, Constants.YES, Constants.CHARECTER_A, Constants.STRING_ACCEPT,
				Constants.STRING_ACCEPT, Constants.STRING_ACCEPT, CSCNotSubmitted, CSCNotSubmittedByCardholderBank,
				CSCServiceNotAvailable, CSCCheckFailed, NoCSCMatch, Constants.EMPTY_STRING, Constants.EMPTY_STRING,
				Constants.EMPTY_STRING);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		commonUtil.clickWebSitePreview(driver, WebsiteName);
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
				logger.info("[executeCVNSale] Login Button Displayed: " + commonUtil
						.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN).isDisplayed());
				try {
					if (commonUtil.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN)
							.isDisplayed()) {
						logger.info("[executeCVNSale] Clicking login button");
					}
				} catch (Exception e) {
					logger.info("[executeCVNSale] Logging out previous user");
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
					// For multiple items need to loop through all the given item
					itemDetails = commonUtil.getItemDetails(Items);
					for (int i = 0; i < itemDetails.length; i++) {
						int j = 0;
						commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SHOP);
						commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
						executor.executeScript(
								prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_SCROLL_WINDOW_TILL_ITEM_VISIBLE));
						commonUtil.clickElementByLinkText(driver, itemDetails[i][j].toString());
						commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
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
					wait.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath(prop.getProperty(Constants.TEST_AIR_PODS_XPATH))));
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					// commonUtil.clickElementByXPath(driver, prop, Constants.TEST_AIR_PODS_XPATH);
					commonUtil.clickElementByLinkText(driver, ItemName);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_ADD_TO_CART);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_4);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_VIEW_CART_CHECKOUT);
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.CART_DETAILS);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PROCEED_TO_CHECKOUT);
				commonUtil.setShippingMethodSCA(driver, ShippingMethod);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				// commonUtil.setElementValueByXPath(driver, prop,
				// "suite.commerce.advanced.security.code", CardSecurityCode); // DA not req //
				// DQA Req
				commonUtil.clickElementByXPath(driver, prop, Constants.WEBSTORE_PAYMENT_METHOD_CREDIT_CARD);
				commonUtil.selectCard(driver, CardEnding, CardExpiry, CardType);
				// commonUtil.clickElementByXPath(driver, prop,
				// Constants.WEBSTORE_PAYMENT_METHOD_CREDIT_CARD_SELECT);
				commonUtil.setElementValueByXPath(driver, prop, Constants.WEBSTORE_CREDIT_CARD_CVN, CardSecurityCode);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.PAYMENT_METHOD);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_PLACE_ORDER);
				// Checking for any error in the transaction
				for (int i = 0; i < browserMaxTimeoutSeconds; i++) {
					currentURL = driver.getCurrentUrl();
					if ((Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(CSCNotSubmitted))
							|| (Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(CSCNotSubmittedByCardholderBank))
							|| (Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(CSCServiceNotAvailable))
							|| (Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(CSCCheckFailed))
							|| (Constants.ORDER_STATUS_CANCEL_ORDER.equalsIgnoreCase(NoCSCMatch))
							|| (DecisionMsg.contains(Constants.STRING_REJECT))) {

						elements = commonUtil.findElementsByXPath(driver, prop, Constants.WEB_STORE_ERROR);
					}
					if ((currentURL.contains(Constants.LAST_ORDER_ID)) || (elements != null)) {
						logger.info("[executeCVNSale] Checking Trasaction URL Status: " + currentURL);
						String[] orderState = currentURL.split("#");
						logger.info("[executeCVNSale] Transaction status: " + orderState[1]);
						if (Constants.STRING_REVIEW.equals(orderState[1])) {
							error = commonUtil.getElementTextByXPath(driver, prop, Constants.WEB_STORE_ERROR).trim();
							logger.info("[executeCVNSale] Error Message: " + error);
						}
						break;
					} else {
						commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					}
				}
				if (Constants.WEBSTORE_CVN_REJECT_MESSAGE.equalsIgnoreCase(error)
						|| Constants.WEBSTORE_CVN_SOFT_DECLINE_FLAGGED_MESSAGE.equalsIgnoreCase(error)
						|| Constants.WEBSTORE_CVN_SOFT_DECLINE_DECLINED_MESSAGE.equalsIgnoreCase(error)) {
					logger.info("[executeCVNSale] switching window");
					try {
						commonUtil.switchToWindow(driver, mainWindow);
						logger.info("[executeCVNSale] Switch window successful");
					} catch (Exception e) {
						logger.info("[executeCVNSale] Switch window unsuccessful" + e);
					}
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					driver.get(prop.getProperty(Constants.PAYMENT_EVENTS_URL));
					logger.info("[executeCVNSale] Payment Events is loading");
					commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
					orderNumber = commonUtil.getOrderIdFromPaymentEvents(driver, PPP);
					logger.info("[executeCVNSale] Order Number is: " + orderNumber);
					commonUtil.switchToWindow(driver, childWindow);
					commonUtil.clickElementByXPath(driver, prop, Constants.SA_HOME_PAGE_LOGO);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
				} else {
					WebElement orderConfirmationNumber = commonUtil.findElementByXPath(driver, prop,
							Constants.SUITE_COMMERCE_ADVANCED_ORDER_CONFIRMATION_NUMBER);
					if (null != orderConfirmationNumber) {
						orderNumber = StringUtils.substringAfterLast(orderConfirmationNumber.getText(), Constants.HASH);
					}
				}
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.refreshCurrentPage(driver);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WELCOME_USER);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SIGN_OUT);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
				commonUtil.closeCurrentWindow(driver);
			}
		}
		commonUtil.switchToWindow(driver, mainWindow);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(Keys.DOWN, Keys.ENTER);
		commonUtil.scrollToBottom(driver);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		WebElement table = commonUtil.findElementById(driver, prop, Constants.PAYMENT_EVENT_TABLE_ID);
		List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
		List<WebElement> cells = null;
		for (WebElement row : allRows) {
			cells = row.findElements(By.tagName(Constants.TD));
			String paymentEvent = cells.get(2).getText();
			if (paymentEvent.equals("Sale")) {
				authOrderId = cells.get(1).getText().split("#")[1];
				logger.info("[executeCVNSale] auth order id: " + authOrderId);
				cells.get(8).findElement(By.id(prop.getProperty(Constants.PAYMENT_EVENT_TRANSACTION_COLUMN_ID)))
						.click();
				commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
				mainWindow = driver.getWindowHandle();
				allWindows = driver.getWindowHandles();
				itr = allWindows.iterator();
				while (itr.hasNext()) {
					childWindow = itr.next();
					if (!mainWindow.equals(childWindow)) {
						commonUtil.switchToWindow(driver, childWindow);
						commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
						if (!Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.PROCESS_TYPE_REST))) {
							String rawResponse = driver
									.findElement(By.xpath(
											"//*[@id='tr_fg_fldgrouprawresponse']/td/table/tbody/tr/td/div/span[2]"))
									.getText();
							String[] raw = rawResponse.split("\n");
							for (int i = 0; i < raw.length; i++) {
								if (raw[i].contains("c:decision:")) {
									logger.info("[executeCVNSale] Decision from xls " + DecisionMsg);
									logger.info("[executeCVNSale] Decision from web " + raw[i]);
									Assert.assertEquals(DecisionMsg, raw[i]);
								}
								if (raw[i].contains("c:reasonCode:")) {
									logger.info("[executeCVNSale] Reason code from xls " + ReasonCode);
									logger.info("[executeCVNSale] Reason code from web " + raw[i]);
									Assert.assertEquals(ReasonCode, raw[i]);
								}
								if (raw[i].contains("c:cvCode:")) {
									logger.info("[executeCVNSale] CVN code from xls " + CVNCode);
									logger.info("[executeCVNSale] CVN code from web " + raw[i]);
									Assert.assertEquals(CVNCode, raw[i]);
									String[] AVSCVNCode = raw[i].split(Constants.COLON);
									Reporter.log("AVS Code" + ": " + AVSCVNCode[AVSCVNCode.length - 1]);
								}
								if (raw[i].contains("c:requestID:")) {
									String[] authCode = raw[i].split(": ");
									logger.info(authCode[1]);
									pnReferenceNumber = authCode[1];
								}
							}
						} else {
							List<WebElement> pres = driver
									.findElements(By.xpath(prop.getProperty(Constants.REST_RESPONSE_XPATH)));
							String[] lines = pres.get(3).getText().trim().split("\\R");
							for (int i = 0; i < lines.length; i++) {
								if (lines[i].contains(prop.getProperty(Constants.REST_STATUS))) {
									logger.info("[executeCVNSale] Decision from xls " + DecisionMsg);
									logger.info("[executeCVNSale] Decision from web " + lines[i].trim());
									Assert.assertEquals(DecisionMsg, lines[i].trim());
								}
								if (lines[i].contains(prop.getProperty(Constants.REST_CVN))) {
									logger.info("[executeCVNSale] CVN code from xls " + CVNCode);
									logger.info("[executeCVNSale] CVN code from web " + lines[i].trim());
									Assert.assertEquals(CVNCode, lines[i].trim());
								}
								if (lines[i].contains(prop.getProperty(Constants.REST_PN_REF))) {
									pnReferenceNumber = lines[i].split(":")[1].trim().replace("\"", "");
								}
							}
						}
					}
					commonUtil.closeCurrentWindow(driver);
					commonUtil.switchToWindow(driver, mainWindow);
				}
			}
		}
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), authOrderId);
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);
		screenShotPath = screenShotModulePath;
		logger.info("[executeCVNSale] Ends");
	}
}
