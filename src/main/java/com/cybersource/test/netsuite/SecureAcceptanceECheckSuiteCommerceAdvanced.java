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
 * SecureAcceptanceECheckSuiteCommerceAdvanced class which will have all functionality used to place order and do related services using secure acceptance echeck in suite commerce advanced platform
 * @author AD20243779
 *
 */
public class SecureAcceptanceECheckSuiteCommerceAdvanced extends CommonTest {
	CommonUtil commonUtil = new CommonUtil();
	boolean isDirectRefundService = false;
	String refund = null;
	String service = null;
	String pnReferenceNumber = null;
	String screenShotPath = null;
	String screenShotModulePath = null;
	String PNRef = null;
	String saleId = null;
	String userIndex = "1";
	WebDriver driver = null;
	String orderNumber = null;
	String childWindow = null;
	String mainWindow = null;
	String authOrderId = null;
	String[] authCode = new String[2];
	Set<String> allWindows = null;
	Iterator<String> itr = null;
	Object itemDetails[][] = null;
	JavascriptExecutor executor = null;
	final static Logger logger = Logger.getLogger(SecureAcceptanceECheckSuiteCommerceAdvanced.class);

	@BeforeTest
	/**
	 * This method is used to initialize test environment before running a test
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @throws IOException Handles when an I/O exception of some sort has occurred
	 */
	public void init(ITestContext context) throws IOException {
		super.loadCommonConfig(context);
		screenShotModulePath = commonUtil.getScreenshotsModulePath(Constants.ECHECK_SECUREACCEPTANCE_SCA);
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
		String serviceType = context.getCurrentXmlTest().getParameter(Constants.SERVICE_TYPE);
		refund = context.getCurrentXmlTest().getParameter(Constants.REFUND);
		if (Constants.DIRECT_REFUND.equals(serviceType)) {
			isDirectRefundService = true;
		}
		Object[][] data = FileUtil.getSheetData(XLSX_FILE_PATH,
				prop.getProperty(prefix + currentScenario + Constants._SHEET_NAME));
		logger.info("[eCheckSecureAcceptanceSuiteCommerceAdvanced][getData]  currentScenario:" + currentScenario);
		return data;
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
	 * @param Location String containing order location
	 * @param RoutingNumber String containing routing number
	 * @param AccountNumber String containing account number
	 * @param AccountType String to define type of account used to do transaction
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param WebSiteName 
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeSale(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String Location, String RoutingNumber, String AccountNumber, String AccountType,
			String PaymentProcessingProfile, String WebSiteName, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[SecureAcceptanceECheckSuiteCommerceAdvanced][executeSale] Starts");
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.STRING_SALE);
		commonUtil.setPaymentProcessingProfileECheck(driver, PaymentProcessingProfile);
		WebStoreOrder(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, Location, RoutingNumber,
				AccountNumber, AccountType, PaymentProcessingProfile, WebSiteName, RequireEBCCheck, ReasonCode,
				DecisionMsg, TestScenario);

		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(orderNumber);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		commonUtil.findElementByXPath(driver, prop, Constants.SEARCH_BAR).sendKeys(Keys.DOWN, Keys.ENTER);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.scrollToBottom(driver);

		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickLinkByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		saleId = commonUtil.getOrderId(driver, Constants.STRING_SALE);
		commonUtil.renameFolder(screenShotPath, Constants.STRING_SALE, saleId);
//		commonUtil.clickPaymentEvent(driver, Constants.SALE, Constants.CUSTOMER_DEPOSIT, Constants.ACCEPT);
//		authCode = commonUtil.getAuthCode(driver, prop.getProperty(Constants.AUTH_CODE_SECURE_ACCEPTANCE),
//				prop.getProperty(Constants.PN_REF_NUMBER_SECURE_ACCEPTANCE));
//		logger.info(authCode[0]+" "+authCode[1]);
//		pnReferenceNumber = authCode[1];
//		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
//			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
//		}
//		commonUtil.addLogToTestNGReport(Constants.SALE, commonUtil.getCurrentUrl(driver), saleId);
//		commonUtil.addLogToTestNGReport(Constants.SALE, pnReferenceNumber);

		commonUtil.clickPaymentEvent(driver, Constants.STRING_SALE, Constants.CUSTOMER_DEPOSIT, Constants.PENDING);
		authCode = commonUtil.getAuthCode(driver, prop.getProperty(Constants.AUTH_CODE_SECURE_ACCEPTANCE),
				prop.getProperty(Constants.PN_REF_NUMBER_SECURE_ACCEPTANCE));
		pnReferenceNumber = authCode[1];
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), saleId);
		commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);
		logger.info("[SecureAcceptanceECheckSuiteCommerceAdvanced][executeSale] Ends");
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
	 * @param Location String containing order location
	 * @param RoutingNumber String containing routing number
	 * @param AccountNumber String containing account number
	 * @param AccountType String to define type of account used to do transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 */
	public void executeRefund(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String Location, String RoutingNumber, String AccountNumber, String AccountType,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) {
		logger.info("[SecureAcceptanceECheckSuiteCommerceAdvanced][executeRefund] Starts");
		String refundId = null;
		Object itemDetails[][] = null;
		executor = (JavascriptExecutor) driver;
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_APPROVE);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_FULFILL);
		itemDetails = commonUtil.getItemDetails(ItemName);
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
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		if (Constants.CREDIT.equals(refund)) {
			commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_CREDIT);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_LOCATION, Location);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_OK_BUTTON);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_REFUND_METHOD_TAB);
			pnReferenceNumber = commonUtil.checkActuals(driver, Constants.REFUND,
					prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
			pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
					prop.getProperty(Constants.PI_DISBLED_PN_REFERENCE_NUMBER));
			if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
				commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
			}
			refundId = commonUtil.getOrderId(driver, Constants.REFUND);
			commonUtil.addLogToTestNGReport(Constants.REFUND, pnReferenceNumber);
			commonUtil.addLogToTestNGReport(Constants.REFUND, commonUtil.getCurrentUrl(driver), refundId);
		}
		if (Constants.CUSTOMER_REFUND.equals(refund)) {
			commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_AUTHORIZE_RETURN);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_APPROVE_RETURN);
			commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_RECEIVE_RETURN);
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
			executor.executeScript(prop.getProperty(Constants.REFUND_CREATED_FROM));
			commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_LOCATION, Location);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_OK_BUTTON);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_REFUND_METHOD_TAB);
			pnReferenceNumber = commonUtil.checkActuals(driver, Constants.REFUND,
					prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
			pnReferenceNumber = commonUtil.getPNReferenceNumber(driver,
					prop.getProperty(Constants.PI_DISBLED_PN_REFERENCE_NUMBER));
			if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
				commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
			}
			refundId = commonUtil.getOrderId(driver, Constants.REFUND);
			commonUtil.addLogToTestNGReport(Constants.REFUND, pnReferenceNumber);
			commonUtil.addLogToTestNGReport(Constants.REFUND, commonUtil.getCurrentUrl(driver), refundId);
		}
		logger.info("[SecureAcceptanceECheckSuiteCommerceAdvanced][executeRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund after sale service
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param Location String containing order location
	 * @param RoutingNumber String containing routing number
	 * @param AccountNumber String containing account number
	 * @param AccountType String to define type of account used to do transaction
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param WebSiteName String containing name of website used to place transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeSaleRefund(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String Location, String RoutingNumber, String AccountNumber, String AccountType,
			String PaymentProcessingProfile, String WebSiteName, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {

		logger.info("[SecureAcceptanceECheckSuiteCommerceAdvanced][executeSaleRefund] Starts");
		executeSale(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, Location, RoutingNumber,
				AccountNumber, AccountType, PaymentProcessingProfile, WebSiteName, RequireEBCCheck, ReasonCode,
				DecisionMsg, TestScenario);
		executeRefund(Email, Password, ItemName, Items, HasMultipleItem, ShippingMethod, Location, RoutingNumber,
				AccountNumber, AccountType, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		logger.info("[SecureAcceptanceECheckSuiteCommerceAdvanced][executeSaleRefund] Ends");
	}

	/**
	 * This method is used to execute webstore order
	 * @param Email This string contains email id of the registered user
	 * @param Password This string contains password of the registered user
	 * @param ItemName String containing name of item to be ordered
	 * @param Items String containing items to be ordered
	 * @param HasMultipleItem String to check if multiple items are to be ordered
	 * @param ShippingMethod String containing shipping method of order
	 * @param Location String containing order location
	 * @param RoutingNumber String containing routing number
	 * @param AccountNumber String containing account number
	 * @param AccountType String to define type of account used to do transaction
	 * @param PaymentProcessingProfile String containing payment processing profile used
	 * @param WebSiteName String containing name of website used to place transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void WebStoreOrder(String Email, String Password, String ItemName, String Items, String HasMultipleItem,
			String ShippingMethod, String Location, String RoutingNumber, String AccountNumber, String AccountType,
			String PaymentProcessingProfile, String WebSiteName, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[SecureAcceptanceECheckSuiteCommerceAdvanced][WebStoreOrder] Starts");
		driver = commonUtil.setupDriver(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_URL, userIndex);
		commonUtil.clickWebSitePreview(driver, WebSiteName);
		// WebDriverWait wait = new WebDriverWait(driver, 300);

		executor = (JavascriptExecutor) driver;
		mainWindow = driver.getWindowHandle();
		allWindows = driver.getWindowHandles();
		itr = allWindows.iterator();
		while (itr.hasNext()) {
			childWindow = null;
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				commonUtil.switchToWindow(driver, childWindow);
				commonUtil.maximizeWindow(driver);
				commonUtil.refreshCurrentPage(driver);
				if (Constants.WELCOME_TO_THE_STORE.equals(driver.getTitle()) && commonUtil
						.findElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_LOGIN).isDisplayed()) {
					// wait.until(ExpectedConditions.visibilityOfElementLocated(
					// By.xpath(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_WELCOME_USER))));
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_WELCOME_USER);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					commonUtil.clickElementByXPath(driver, prop, Constants.SUITE_COMMERCE_ADVANCED_SIGN_OUT);
				}
				// wait.until(ExpectedConditions.visibilityOfElementLocated(
				// By.xpath(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_LOGIN))));
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
						commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
						executor.executeScript(
								prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_SCROLL_WINDOW_TILL_ITEM_VISIBLE));
						commonUtil.clickElementByLinkText(driver, itemDetails[i][j].toString());
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

				commonUtil.clickElementByLinkText(driver, Constants.EXTERNAL_PAGE_PAYMENT_METHOD_OTHERS_TEXT);
				commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNAL_PAGE_PAYMENT_METHOD_ECHECK);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
				commonUtil.takeScreenShot(driver, screenShotPath, Constants.PAYMENT_METHOD);
				executor.executeScript(prop.getProperty(Constants.SUITE_COMMERCE_ADVANCED_CONTINUE));
				commonUtil.clickElementByCssSelector(driver, prop,
						Constants.SUITE_COMMERCE_ADVANCED_CONTINUE_EXTERNAL_CHECKOUT);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_5);

				commonUtil.setElementValueByXPath(driver, prop, Constants.ECHECK_ROUTING_NUMBER, RoutingNumber);
				commonUtil.setElementValueByXPath(driver, prop, Constants.ECHECK_ACCOUNT_NUMBER, AccountNumber);
				commonUtil.clickAccountType(driver, prop, AccountType);
				boolean isPay = commonUtil.isExists(driver, Constants.SECURE_ACCEPTANCE_NEXT,
						Constants.WEBSTORE_IS_PAY_BUTTON_VALUE);
				commonUtil.clickElementByXPath(driver, prop, Constants.SECURE_ACCEPTANCE_NEXT);
				commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
				if (isPay == false) {
					commonUtil.clickElementByXPath(driver, prop, Constants.EXTERNAL_PAGE_PAY_BUTTON);
				}
				WebElement orderConfirmationNumber = commonUtil.findElementByXPath(driver, prop,
						Constants.SUITE_COMMERCE_ADVANCED_ORDER_CONFIRMATION_NUMBER);
				if (null != orderConfirmationNumber) {
					orderNumber = StringUtils.substringAfterLast(orderConfirmationNumber.getText(), Constants.HASH);
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
		logger.info("[SecureAcceptanceECheckSuiteCommerceAdvanced][WebStoreOrder] Ends");
	}

}
