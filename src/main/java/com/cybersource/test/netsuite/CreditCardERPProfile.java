package com.cybersource.test.netsuite;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
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
import com.cybersource.test.util.RetryFailedTestCases;

@Listeners(com.cybersource.test.util.Listeners.class)
/**
 * CreditCardERPProfile class which will have all functionality used to place order and do related services using Credit card ERP payment method which require configuration changes in Payment processing profile
 * @author AD20243779
 *
 */
public class CreditCardERPProfile extends CommonTest {
	CommonUtil commonUtil = new CommonUtil();
	String orderManagementType = null;
	String pnReferenceNumber = null;
	String authOrderId = null;
	String captureId = null;
	String userIndex = "1";
	String screenShotPath = null;
	String screenShotModulePath = null;
	String tokenId = null;
	String response[];
	Object itemDetails[][] = null;
	WebDriver driver = null;
	JavascriptExecutor executor = null;
	final static Logger logger = Logger.getLogger(CreditCardERPProfile.class);

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
		logger.info("[getData] currentScenario: " + currentScenario);
		return data;
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute capture service after completing a authorization service for token orders and processor level 2,3 transactions
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
	 * @param Token String containing details of token used for transaction
	 * @param TokenPaymentMethod String containing token payment methods
	 * @param PPPConfig String containing configuration setup for payment processing profile 
	 * @param PPP String containing payment processing profile used
	 * @param PaymentType String containing type of payment used
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
	public void executeAuthCapture(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String Token, String TokenPaymentMethod, String PPPConfig, String PPP,
			String PaymentType, String MerchantID, String ProcessorKey, String ProcessorName, String ProcessorLevel,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthCapture] Starts");
		executeAuth(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, Token, TokenPaymentMethod, PPPConfig, PPP, PaymentType, MerchantID,
				ProcessorKey, ProcessorName, ProcessorLevel, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		executeCapture(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, Token, TokenPaymentMethod, PPP, MerchantID, ProcessorKey, ProcessorName,
				ProcessorLevel, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		logger.info("[executeAuthCapture] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund service after completing a authorization and capture services for token orders and processor level 2,3 transactions
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
	 * @param Token String containing details of token used for transaction
	 * @param TokenPaymentMethod String containing token payment methods
	 * @param PPPConfig String containing configuration setup for payment processing profile 
	 * @param PPP String containing payment processing profile used
	 * @param PaymentType String containing type of payment used
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
	public void executeAuthCaptureRefund(String CustomerName, String Memo, String ItemName, String Quantity,
			String Rate, String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod,
			String CSC, String CardDetail, String Token, String TokenPaymentMethod, String PPPConfig, String PPP,
			String PaymentType, String MerchantID, String ProcessorKey, String ProcessorName, String ProcessorLevel,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthCaptureRefund] Starts");
		executeAuth(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, Token, TokenPaymentMethod, PPPConfig, PPP, PaymentType, MerchantID,
				ProcessorKey, ProcessorName, ProcessorLevel, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		executeCapture(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, Token, TokenPaymentMethod, PPP, MerchantID, ProcessorKey, ProcessorName,
				ProcessorLevel, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		executeRefund(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, Token, TokenPaymentMethod, PPP, MerchantID, ProcessorKey, ProcessorName,
				ProcessorLevel, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		logger.info("[executeAuthCaptureRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute customer refund service after completing a authorization and capture services for token orders and processor level 2,3 transactions
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
	 * @param Token String containing details of token used for transaction
	 * @param TokenPaymentMethod String containing token payment methods
	 * @param PPPConfig String containing configuration setup for payment processing profile 
	 * @param PPP String containing payment processing profile used
	 * @param PaymentType String containing type of payment used
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
	public void executeAuthCaptureCustomerRefund(String CustomerName, String Memo, String ItemName, String Quantity,
			String Rate, String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod,
			String CSC, String CardDetail, String Token, String TokenPaymentMethod, String PPPConfig, String PPP,
			String PaymentType, String MerchantID, String ProcessorKey, String ProcessorName, String ProcessorLevel,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuthCaptureCustomerRefund] Starts");
		executeAuth(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, Token, TokenPaymentMethod, PPPConfig, PPP, PaymentType, MerchantID,
				ProcessorKey, ProcessorName, ProcessorLevel, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		executeCapture(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, Token, TokenPaymentMethod, PPP, MerchantID, ProcessorKey, ProcessorName,
				ProcessorLevel, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		executeCustomerRefund(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, Token, TokenPaymentMethod, PPP, MerchantID, ProcessorKey, ProcessorName,
				ProcessorLevel, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		logger.info("[executeAuthCaptureCustomerRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund service after completing a sale service for token orders and processor level 2,3 transactions
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
	 * @param Token String containing details of token used for transaction
	 * @param TokenPaymentMethod String containing token payment methods
	 * @param PPPConfig String containing configuration setup for payment processing profile 
	 * @param PPP String containing payment processing profile used
	 * @param PaymentType String containing type of payment used
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
	public void executeSaleRefund(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String Token, String TokenPaymentMethod, String PPPConfig, String PPP,
			String PaymentType, String MerchantID, String ProcessorKey, String ProcessorName, String ProcessorLevel,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeSaleRefund] Starts");
		executeSale(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, Token, TokenPaymentMethod, PPPConfig, PPP, PaymentType, MerchantID,
				ProcessorKey, ProcessorName, ProcessorLevel, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		executeRefund(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, Token, TokenPaymentMethod, PPP, MerchantID, ProcessorKey, ProcessorName,
				ProcessorLevel, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		logger.info("[executeSaleRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute capture service using token after completing a auth service for token orders and processor level 2,3 transactions
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
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing processor key for transaction
	 * @param Token String to decide if token scenarios are to be handled
	 * @param TokenPaymentMethod String to set the token payment method
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeTokenAuthCapture(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String PPP, String MerchantID, String ProcessorKey, String Token,
			String TokenPaymentMethod, String RequireEBCCheck, String ReasonCode, String DecisionMsg,
			String TestScenario) throws Exception {
		logger.info("[executeTokenAuthCapture] Starts");
		executeTokenAuth(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, PPP, MerchantID, ProcessorKey, Token, TokenPaymentMethod,
				RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeCapture(driver, Items, HasMultipleItem, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario, screenShotPath, screenShotModulePath);
		logger.info("[executeTokenAuthCapture] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund service using token after completing a auth and capture services for token orders and processor level 2,3 transactions
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
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing processor key for transaction
	 * @param Token String to decide if token scenarios are to be handled
	 * @param TokenPaymentMethod String to set the token payment method
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeTokenAuthCaptureRefund(String CustomerName, String Memo, String ItemName, String Quantity,
			String Rate, String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod,
			String CSC, String CardDetail, String PPP, String MerchantID, String ProcessorKey, String Token,
			String TokenPaymentMethod, String RequireEBCCheck, String ReasonCode, String DecisionMsg,
			String TestScenario) throws Exception {
		logger.info("[executeTokenAuthCaptureRefund] Starts");
		executeTokenAuth(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, PPP, MerchantID, ProcessorKey, Token, TokenPaymentMethod,
				RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeCapture(driver, Items, HasMultipleItem, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario, screenShotPath, screenShotModulePath);
		commonUtil.executeRefund(driver, Location, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario,
				screenShotPath, screenShotModulePath);
		logger.info("[executeTokenAuthCaptureRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute customer refund service using token after completing a auth and capture services for token orders and processor level 2,3 transactions
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
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing processor key for transaction
	 * @param Token String to decide if token scenarios are to be handled
	 * @param TokenPaymentMethod String to set the token payment method
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeTokenAuthCaptureCustomerRefund(String CustomerName, String Memo, String ItemName,
			String Quantity, String Rate, String TaxCode, String Items, String HasMultipleItem, String Location,
			String PaymentMethod, String CSC, String CardDetail, String PPP, String MerchantID, String ProcessorKey,
			String Token, String TokenPaymentMethod, String RequireEBCCheck, String ReasonCode, String DecisionMsg,
			String TestScenario) throws Exception {
		logger.info("[executeTokenAuthCaptureCustomerRefund] Starts");
		executeTokenAuth(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, PPP, MerchantID, ProcessorKey, Token, TokenPaymentMethod,
				RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeCapture(driver, Items, HasMultipleItem, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario, screenShotPath, screenShotModulePath);
		commonUtil.executeCustomerRefund(driver, Items, HasMultipleItem, Location, RequireEBCCheck, ReasonCode,
				DecisionMsg, TestScenario, screenShotPath, screenShotModulePath);
		logger.info("[executeTokenAuthCaptureCustomerRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute void service using token after completing a auth service for token orders and processor level 2,3 transactions
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
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing processor key for transaction
	 * @param Token String to decide if token scenarios are to be handled
	 * @param TokenPaymentMethod String to set the token payment method
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeTokenAuthVoid(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String PPP, String MerchantID, String ProcessorKey, String Token,
			String TokenPaymentMethod, String RequireEBCCheck, String ReasonCode, String DecisionMsg,
			String TestScenario) throws Exception {
		logger.info("[executeTokenAuthVoid] Starts");
		executeTokenAuth(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, PPP, MerchantID, ProcessorKey, Token, TokenPaymentMethod,
				RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeVoid(driver, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario, screenShotPath,
				screenShotModulePath);
		logger.info("[executeTokenAuthVoid] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute refund service using token after completing a auth and capture service for token orders and processor level 2,3 transactions
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
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing processor key for transaction
	 * @param Token String to decide if token scenarios are to be handled
	 * @param TokenPaymentMethod String to set the token payment method
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthTokenCaptureRefund(String CustomerName, String Memo, String ItemName, String Quantity,
			String Rate, String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod,
			String CSC, String CardDetail, String PPP, String MerchantID, String ProcessorKey, String Token,
			String TokenPaymentMethod, String RequireEBCCheck, String ReasonCode, String DecisionMsg,
			String TestScenario) throws Exception {
		logger.info("[executeAuthTokenCaptureRefund] Starts");
		executeAuthToken(CustomerName, Memo, ItemName, Quantity, Rate, TaxCode, Items, HasMultipleItem, Location,
				PaymentMethod, CSC, CardDetail, PPP, MerchantID, ProcessorKey, Token, TokenPaymentMethod,
				RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario);
		commonUtil.executeCapture(driver, Items, HasMultipleItem, RequireEBCCheck, ReasonCode, DecisionMsg,
				TestScenario, screenShotPath, screenShotModulePath);
		commonUtil.executeRefund(driver, Location, RequireEBCCheck, ReasonCode, DecisionMsg, TestScenario,
				screenShotPath, screenShotModulePath);
		logger.info("[executeAuthTokenCaptureRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute Authorization service
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
	 * @param Token String to decide if token scenarios are to be handled
	 * @param TokenPaymentMethod String to set the token payment method
	 * @param PPPConfig String containing configuration setup payment processing profile
	 * @param PPP String containing payment processing profile used
	 * @param PaymentType String containing type of payment used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuth(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String Token, String TokenPaymentMethod, String PPPConfig, String PPP,
			String PaymentType, String MerchantID, String ProcessorKey, String ProcessorName, String ProcessorLevel,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeAuth] Starts");
		driver = null;
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALES_ORDER_URL, userIndex);
		if (commonUtil.isAlertPresent(driver)) {
			commonUtil.acceptAlert(driver);
		}
		if (Constants.YES.equalsIgnoreCase(PPPConfig)) {
			commonUtil.setPaymentProcessingProfile(driver, PPP, MerchantID, ProcessorKey, ProcessorName, ProcessorLevel,
					Token, TokenPaymentMethod);
		}
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALES_ORDER_URL, userIndex);
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
				if (commonUtil.isAlertPresent(driver)) {
					commonUtil.acceptAlert(driver);
				}
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
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_PROCESSING_PROFILE, PPP);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_BILL_CSC, CSC);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_LEVEL, ProcessorLevel);
			if (commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_TYPE).isDisplayed()) {
				commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_TYPE, PaymentType);
			}
			commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);

			commonUtil.scrollToBottom(driver);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
			authOrderId = commonUtil.getOrderId(driver, Constants.AUTHORIZATION);
			commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, commonUtil.getCurrentUrl(driver), authOrderId);

			response = commonUtil.checkActuals(driver, Constants.AUTHORIZATION, Token,
					prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
			pnReferenceNumber = response[2];
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, pnReferenceNumber);

			tokenId = response[3];
			if (!Constants.NULL.equalsIgnoreCase(tokenId)) {
				Reporter.log(Constants.TOKEN_ID + ": " + tokenId);
			}

			if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
				commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
			}
		}
		screenShotPath = screenShotModulePath;
		logger.info("[executeAuth] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute capture service
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
	 * @param Token String to decide if token scenarios are to be handled
	 * @param TokenPaymentMethod String to set the token payment method
	 * @param PPP String containing payment processing profile used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeCapture(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String Token, String TokenPaymentMethod, String PPP, String MerchantID,
			String ProcessorKey, String ProcessorName, String ProcessorLevel, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeCapture] Starts");
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.CAPTURE);
		executor = (JavascriptExecutor) driver;
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_APPROVE);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_FULFILL);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.ORDER_FULLFIL);
		commonUtil.setElementValueByXPath(driver, prop, Constants.ITEM_FULFILLMENT_STATUS,
				Constants.ORDER_STATUS_SHIPPED);
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
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.ORDER_FULLFIL);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		// Sales Order- Mark Packed button
		// commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_PACKED);
		// Sales Order- Mark Shipped button
		// commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_PACKED);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_BILL);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
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
	 * @param Token String to decide if token scenarios are to be handled
	 * @param TokenPaymentMethod String to set the token payment method
	 * @param PPP String containing payment processing profile used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeRefund(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String Token, String TokenPaymentMethod, String PPP, String MerchantID,
			String ProcessorKey, String ProcessorName, String ProcessorLevel, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeRefund] Starts");
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.REFUND);
		String refundId = null;
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
		commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_LOCATION, Location);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);

		commonUtil.scrollToBottom(driver);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

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
	 * This method is used to execute customer refund service
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
	 * @param Token String to decide if token scenarios are to be handled
	 * @param TokenPaymentMethod String to set the token payment method
	 * @param PPP String containing payment processing profile used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeCustomerRefund(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String Token, String TokenPaymentMethod, String PPP, String MerchantID,
			String ProcessorKey, String ProcessorName, String ProcessorLevel, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeCustomerRefund] Starts");
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.CUSTOMER_REF);
		int i = 0;
		String refundId = null;
		executor = (JavascriptExecutor) driver;
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_AUTHORIZE_RETURN);
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
		executor.executeScript(prop.getProperty(Constants.REFUND_CREATED_FROM));
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
		commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_LOCATION, Location);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);

		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_REFUND_METHOD_TAB);
		commonUtil.scrollToBottom(driver);
		commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);

		refundId = commonUtil.getOrderId(driver, Constants.REFUND);
		commonUtil.renameFolder(screenShotPath, Constants.CUSTOMER_REF, refundId);
		commonUtil.addLogToTestNGReport(Constants.REFUND, commonUtil.getCurrentUrl(driver), refundId);

		pnReferenceNumber = commonUtil.checkActuals(driver, Constants.REFUND,
				prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
				prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		commonUtil.addLogToTestNGReport(Constants.REFUND, pnReferenceNumber);

		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
		}
		logger.info("[executeCustomerRefund] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute Sale service
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
	 * @param Token String to decide if token scenarios are to be handled
	 * @param TokenPaymentMethod String to set the token payment method
	 * @param PPPConfig String containing configuration setup payment processing profile
	 * @param PPP String containing payment processing profile used
	 * @param PaymentType String containing type of payment used
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeSale(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String Token, String TokenPaymentMethod, String PPPConfig, String PPP,
			String PaymentType, String MerchantID, String ProcessorKey, String ProcessorName, String ProcessorLevel,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeSale] Starts");
		driver = null;
		String saleId = null;
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.STRING_SALE);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALE_URL, userIndex);
		if (commonUtil.isAlertPresent(driver)) {
			commonUtil.acceptAlert(driver);
		}
		if (Constants.YES.equalsIgnoreCase(PPPConfig)) {
			commonUtil.setPaymentProcessingProfile(driver, PPP, MerchantID, ProcessorKey, ProcessorName, ProcessorLevel,
					Token, TokenPaymentMethod);
		}
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALE_URL, userIndex);
		executor = (JavascriptExecutor) driver;
		if (Constants.POSITIVE.toUpperCase().equals(TestScenario.toUpperCase())) {
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER, CustomerName);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_5);

			if (Constants.NO.equalsIgnoreCase(prop.getProperty(Constants.CUSTOMER_ID_PRESENT))) {
				commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_CUSTOMER).sendKeys(Keys.DOWN,
						Keys.ENTER);
			}
			// wait until customer is set
			for (int i = 0; i < browserMaxTimeoutSeconds; i++) {
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
				if (commonUtil.isAlertPresent(driver)) {
					commonUtil.acceptAlert(driver);
				}
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
			commonUtil.setElementValueByXPath(driver, prop, Constants.CASH_SALE_PAYMENT_PROCESSING_PROFILE, PPP);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_BILL_CSC, CSC);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_LEVEL, ProcessorLevel);
			if (commonUtil.findElementByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_TYPE).isDisplayed()) {
				commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_TYPE, PaymentType);
			}
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.scrollToBottom(driver);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
			saleId = commonUtil.getOrderId(driver, Constants.STRING_SALE);
			commonUtil.renameFolder(screenShotPath, Constants.STRING_SALE, saleId);
			commonUtil.addLogToTestNGReport(Constants.STRING_SALE, commonUtil.getCurrentUrl(driver), saleId);

			response = commonUtil.checkActuals(driver, Constants.STRING_SALE, ProcessorName, ProcessorLevel, Token,
					prop.getProperty(Constants.SALE_DECISION_MESSAGE), prop.getProperty(Constants.SALE_REASON_CODE),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
			commonUtil.addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);

			pnReferenceNumber = response[2];
			tokenId = response[3];
			if (null != tokenId) {
				Reporter.log(Constants.TOKEN_ID + ": " + tokenId);
			}

			if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
				commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
			}
		}
		logger.info("[executeSale] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute authorization service with decision manager
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
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param FraudManagement String containing fraud manager setup for transaction
	 * @param DecisionManagerReject String containing decision manager reject setup for transaction
	 * @param PaymentType String containing type of payment used
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeDMAuth(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String PPP, String MerchantID, String ProcessorKey, String ProcessorLevel,
			String FraudManagement, String DecisionManagerReject, String PaymentType, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeDMAuth] Starts");
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		driver = null;
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALES_ORDER_URL, userIndex);
		if (commonUtil.isAlertPresent(driver)) {
			commonUtil.acceptAlert(driver);
		}
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.setPaymentProcessingProfileDM(driver, userIndex, PPP, MerchantID, ProcessorKey, ProcessorLevel,
				DecisionManagerReject);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALES_ORDER_URL, userIndex);
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
				if (commonUtil.isAlertPresent(driver)) {
					commonUtil.acceptAlert(driver);
				}
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
		}
		screenShotPath = screenShotModulePath;
		logger.info("[executeDMAuth] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute sale service with decision manager
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
	 * @param ProcessorKey String containing processor key for transaction
	 * @param ProcessorName String containing details of the processor name for transaction
	 * @param ProcessorLevel String containing details of the processor level for transaction
	 * @param FraudManagement String containing fraud manager setup for transaction
	 * @param DecisionManagerReject String containing decision manager reject setup for transaction
	 * @param PaymentType String containing type of payment used
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeDMSale(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String PPP, String MerchantID, String ProcessorKey, String ProcessorLevel,
			String FraudManagement, String DecisionManagerReject, String PaymentType, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario) throws Exception {
		logger.info("[executeDMSale] Starts");
		driver = null;
		String saleId = null;
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.STRING_SALE);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALE_URL, userIndex);
		if (commonUtil.isAlertPresent(driver)) {
			commonUtil.acceptAlert(driver);
		}
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.setPaymentProcessingProfileDM(driver, userIndex, PPP, MerchantID, ProcessorKey, ProcessorLevel,
				DecisionManagerReject);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
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
				logger.info("[executeDMSale] shipping cost value: "
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
				if (commonUtil.isAlertPresent(driver)) {
					commonUtil.acceptAlert(driver);
				}
				if (commonUtil.isAlertPresent(driver)) {
					commonUtil.acceptAlert(driver);
				}
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
		}
		logger.info("[executeDMSale] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute authorization service with tokenisation
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
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing processor key for transaction
	 * @param Token String to decide if token scenarios are to be handled
	 * @param TokenPaymentMethod String to set the token payment method
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeAuthToken(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String PPP, String MerchantID, String ProcessorKey, String Token,
			String TokenPaymentMethod, String RequireEBCCheck, String ReasonCode, String DecisionMsg,
			String TestScenario) throws Exception {
		logger.info("[executeAuthToken] Starts");
		driver = null;
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALES_ORDER_URL, userIndex);
		if (commonUtil.isAlertPresent(driver)) {
			commonUtil.acceptAlert(driver);
		}
		commonUtil.setPaymentProcessingProfile(driver, userIndex, PPP, MerchantID, ProcessorKey, TokenPaymentMethod);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALES_ORDER_URL, userIndex);
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
				if (commonUtil.isAlertPresent(driver)) {
					commonUtil.acceptAlert(driver);
				}
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
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_PROCESSING_PROFILE, PPP);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_BILL_CSC, CSC);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);

			commonUtil.scrollToBottom(driver);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

			commonUtil.takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
			authOrderId = commonUtil.getOrderId(driver, Constants.AUTHORIZATION);
			commonUtil.renameFolder(screenShotPath, Constants.AUTHORIZATION, authOrderId);
			response = commonUtil.checkActuals(driver, Constants.AUTHORIZATION, Token,
					prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
			pnReferenceNumber = response[2];
			tokenId = response[3];
			if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
				commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
			}
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, commonUtil.getCurrentUrl(driver), authOrderId);
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, pnReferenceNumber);
			if (!Constants.NULL.equalsIgnoreCase(tokenId)) {
				Reporter.log(Constants.TOKEN_ID + ": " + tokenId);
			}
		}
		screenShotPath = screenShotModulePath;
		logger.info("[executeAuthToken] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute authorization service with token
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
	 * @param MerchantID String containing merchant ID for transaction
	 * @param ProcessorKey String containing processor key for transaction
	 * @param Token String to decide if token scenarios are to be handled
	 * @param TokenPaymentMethod String to set the token payment method
	 * @param RequireEBCCheck String used to check if order need to be validated from ebc portal
	 * @param ReasonCode String containing reason code of transaction
	 * @param DecisionMsg String containing decision manager message
	 * @param TestScenario String used to define scenario used for executing the test
	 * @throws Exception Used to handle exceptions that may occur during runtime
	 */
	public void executeTokenAuth(String CustomerName, String Memo, String ItemName, String Quantity, String Rate,
			String TaxCode, String Items, String HasMultipleItem, String Location, String PaymentMethod, String CSC,
			String CardDetail, String PPP, String MerchantID, String ProcessorKey, String Token,
			String TokenPaymentMethod, String RequireEBCCheck, String ReasonCode, String DecisionMsg,
			String TestScenario) throws Exception {
		logger.info("[executeTokenAuth] Starts");
		driver = null;
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		screenShotPath = commonUtil.getScreenshotsPath(screenShotModulePath, Constants.AUTHORIZATION);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALES_ORDER_URL, userIndex);
		if (commonUtil.isAlertPresent(driver)) {
			commonUtil.acceptAlert(driver);
		}
		commonUtil.setPaymentProcessingProfile(driver, userIndex, PPP, MerchantID, ProcessorKey, TokenPaymentMethod);
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_SALES_ORDER_URL, userIndex);
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
				if (commonUtil.isAlertPresent(driver)) {
					commonUtil.acceptAlert(driver);
				}
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

			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_PAYMENT_PROCESSING_PROFILE, PPP);
			commonUtil.setElementValueByXPath(driver, prop, Constants.SALES_ORDER_BILL_CSC, CSC);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
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
			
			response = commonUtil.checkActuals(driver, Constants.AUTHORIZATION, Token,
					prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
			pnReferenceNumber = response[2];
			commonUtil.addLogToTestNGReport(Constants.AUTHORIZATION, pnReferenceNumber);
			
			tokenId = response[3];
			if (!Constants.NULL.equalsIgnoreCase(tokenId)) {
				Reporter.log(Constants.TOKEN_ID + ": " + tokenId);
			}
			
			if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
				commonUtil.checkTransactionInEBC(driver, pnReferenceNumber);
			}
		}
		screenShotPath = screenShotModulePath;
		logger.info("[executeTokenAuth] Ends");
	}
}
