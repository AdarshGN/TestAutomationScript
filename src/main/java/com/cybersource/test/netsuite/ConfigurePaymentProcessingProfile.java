package com.cybersource.test.netsuite;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
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
 * ConfigurePaymentProcessingProfile class which will have all functionality related to creating payment processing profile, configuring and updating it
 * @author AD20243779
 *
 */
public class ConfigurePaymentProcessingProfile extends CommonTest {
	CommonUtil commonUtil = new CommonUtil();
	static WebDriver driver = null;
	String userIndex = "1";
	String screenShotModulePath = null;
	JavascriptExecutor executor = null;
	final static Logger logger = Logger.getLogger(ConfigurePaymentProcessingProfile.class);

	@BeforeTest
	/**
	 * This method is used to initialize test environment before running a test
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @throws IOException Handles when an I/O exception of some sort has occurred
	 */
	public void init(ITestContext context) throws IOException {
		super.loadCommonConfig(context);
		// screenShotModulePath =
		// commonUtil.getScreenshotsModulePath(Constants.CONFIGURE_PAYMENT_PROCESSING_PROFILE);
	}

	@Test
	/**
	 * This method is used to configure a payment profile
	 */
	public void configureProfile() {
		enablePaymentInstrument();
		enableGatewayRequestAuthorization(driver, Constants.PAYMENT_INTEGRATION);
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
		// String serviceType =
		// context.getCurrentXmlTest().getParameter(Constants.SERVICE_TYPE);
		data = FileUtil.getSheetData(XLSX_FILE_PATH,
				prop.getProperty(prefix + currentScenario + Constants._SHEET_NAME));
		logger.info("[CreditCardERP][getData] currentScenario:" + currentScenario);
		return data;
	}

	/**
	 * This method is used to enable token services for a profile
	 * @param paymentProcessingProfile String containing payment processing profile to be configured for tokenization
	 */
	public void enableTokenization(String paymentProcessingProfile) {
		logger.info("[ConfigurePaymentProcessingProfile][enableTokenization] starts");
		driver = commonUtil.setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, userIndex);
		commonUtil.clickEditPaymentProcessingProfile(driver, paymentProcessingProfile);
		if (!commonUtil.findElementByXPath(driver, prop, Constants.REPLACE_PAYMENT_CARD_BY_TOKEN).isSelected()) {
			commonUtil.clickElementByXPath(driver, prop, Constants.REPLACE_PAYMENT_CARD_BY_TOKEN);
			commonUtil.setElementValueByXPath(driver, prop, Constants.PAYMENT_CARD_TOKEN_PAYMENT_METHOD,
					Constants.VISA_TOKEN);
			commonUtil.clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		}
		logger.info("[ConfigurePaymentProcessingProfile][enableTokenization] ends");
	}

	/**
	 * This method is used to disable gateway request authorization for a profile
	 * @param driver Webdriver object which will be used for operations
	 * @param paymentProcessingProfile String containing payment processing profile to be configured
	 */
	public void disableGatewayRequestAuthorization(WebDriver driver, String paymentProcessingProfile) {
		logger.info("[ConfigurePaymentProcessingProfile][disableGatewayRequestAuthorization] starts");
		driver = commonUtil.setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, null);
		commonUtil.clickEditPaymentProcessingProfile(driver, paymentProcessingProfile);
		if (commonUtil
				.findElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_GATEWAY_REQUEST_AUTHORIZATIPON)
				.getAttribute("class").contains(Constants.CHECKBOX_CK)) {
			commonUtil.clickElementByXPath(driver, prop,
					Constants.PAYMENT_PROCESSING_PROFILES_GATEWAY_REQUEST_AUTHORIZATIPON);
			commonUtil.clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		}

		logger.info("[ConfigurePaymentProcessingProfile][disableGatewayRequestAuthorization] ends");

	}

	/**
	 * This method is used to enable gateway request authorization for a profile
	 * @param driver Webdriver object which will be used for operations
	 * @param paymentProcessingProfile String containing payment processing profile to be configured
	 */
	public void enableGatewayRequestAuthorization(WebDriver driver, String paymentProcessingProfile) {
		logger.info("[ConfigurePaymentProcessingProfile][enableGatewayRequestAuthorization] starts");
		driver = commonUtil.setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, null);
		commonUtil.clickEditPaymentProcessingProfile(driver, paymentProcessingProfile);
		if (commonUtil
				.findElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_GATEWAY_REQUEST_AUTHORIZATIPON)
				.getAttribute("class").contains(Constants.CHECKBOX_UNCK)) {
			commonUtil.clickElementByXPath(driver, prop,
					Constants.PAYMENT_PROCESSING_PROFILES_GATEWAY_REQUEST_AUTHORIZATIPON);
			commonUtil.clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		}
		logger.info("[ConfigurePaymentProcessingProfile][enableGatewayRequestAuthorization] ends");
	}

	/**
	 * This method is used to disable a payment instrument for a profile
	 */
	public void disablePaymentInstrument() {
		logger.info("[ConfigurePaymentProcessingProfile][disablePaymentInstrument] starts");
		driver = commonUtil.setupDriver(driver, prop, Constants.PAYMENT_INSTRUMENT_URL, userIndex);
		commonUtil.clickElementByXPath(driver, prop, Constants.ENABLE_FEATURES_TRANSACTIONS_TAB);
		logger.info(commonUtil.findElementByXPath(driver, prop, Constants.PAYMENT_INSTRUMENT_CHECKBOX)
				.getAttribute("class"));
		if (commonUtil.findElementByXPath(driver, prop, Constants.PAYMENT_INSTRUMENT_CHECKBOX).getAttribute("class")
				.contains(Constants.CHECKBOX_CK)) {
			commonUtil.clickElementByXPath(driver, prop, Constants.PAYMENT_INSTRUMENT_CHECKBOX);
			commonUtil.clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		}
		logger.info("[ConfigurePaymentProcessingProfile][disablePaymentInstrument] ends");
	}

	/**
	 * This method is used to enable a payment instrument for a profile
	 */
	public void enablePaymentInstrument() {
		logger.info("[ConfigurePaymentProcessingProfile][enablePaymentInstrument] starts");
		driver = commonUtil.setupDriver(driver, prop, Constants.PAYMENT_INSTRUMENT_URL, userIndex);
		commonUtil.clickElementByXPath(driver, prop, Constants.ENABLE_FEATURES_TRANSACTIONS_TAB);
		logger.info(commonUtil.findElementByXPath(driver, prop, Constants.PAYMENT_INSTRUMENT_CHECKBOX)
				.getAttribute("class"));
		if (commonUtil.findElementByXPath(driver, prop, Constants.PAYMENT_INSTRUMENT_CHECKBOX).getAttribute("class")
				.contains(Constants.CHECKBOX_UNCK)) {
			commonUtil.clickElementByXPath(driver, prop, Constants.PAYMENT_INSTRUMENT_CHECKBOX);
			commonUtil.clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		}
		logger.info("[ConfigurePaymentProcessingProfile][enablePaymentInstrument] ends");
	}

	/**
	 * This method is used to enable webstore view for a profile
	 * @param paymentProcessingProfile String containing payment processing profile to be configured
	 * @param WebStore String containing name of Webstore to be enabled
	 */
	public void enableWebStore(String paymentProcessingProfile, String WebStore) {
		logger.info("[ConfigurePaymentProcessingProfile][enableWebStore] starts");
		driver = commonUtil.setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, userIndex);
		commonUtil.clickEditPaymentProcessingProfile(driver, paymentProcessingProfile);
		commonUtil.clickElementByLinkText(driver, WebStore);
		commonUtil.clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		logger.info("[ConfigurePaymentProcessingProfile][enableWebStore] ends");
	}

	/**
	 * This method is used to disable webstore view for a profile
	 * @param paymentProcessingProfile String containing payment processing profile to be configured
	 * @param WebStore String containing name of Webstore to be disabled
	 */
	public void disableWebStore(String paymentProcessingProfile, String WebStore) {
		logger.info("[ConfigurePaymentProcessingProfile][disableWebStore] starts");
		driver = commonUtil.setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, userIndex);
		commonUtil.clickEditPaymentProcessingProfile(driver, paymentProcessingProfile);
		commonUtil.clickElementByLinkText(driver, WebStore);
		commonUtil.clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		logger.info("[ConfigurePaymentProcessingProfile][disableWebStore] ends");
	}

	/**
	 * This method is used to enable payer authentication service for a profile
	 * @param paymentProcessingProfile String containing payment processing profile to be configured
	 */
	public void enablePayerAuthentication(String paymentProcessingProfile) {
		logger.info("[ConfigurePaymentProcessingProfile][enablePayerAuthentication] starts");
		driver = commonUtil.setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, userIndex);
		commonUtil.clickEditPaymentProcessingProfile(driver, paymentProcessingProfile);
		if (!commonUtil.findElementByXPath(driver, prop, Constants.PAYER_AUTHENTICATION).isSelected()) {
			commonUtil.clickElementByXPath(driver, prop, Constants.PAYER_AUTHENTICATION);
			commonUtil.clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		}
		logger.info("[ConfigurePaymentProcessingProfile][enablePayerAuthentication] ends");

	}

	@Test(dataProvider = Constants.GET_DATA)
	/**
	 * This method is used to enable merchant defined data for a payment processing profile
	 * @param MerchantDefinedDataField1 String containing data for Merchant defined data field 1
	 * @param MerchantDefinedDataField2 String containing data for Merchant defined data field 2
	 * @param MerchantDefinedDataField3 String containing data for Merchant defined data field 3
	 * @param MerchantDefinedDataField4 String containing data for Merchant defined data field 4
	 * @param MerchantDefinedDataField5 String containing data for Merchant defined data field 5
	 * @param MerchantDefinedDataField6 String containing data for Merchant defined data field 6
	 * @param MerchantDefinedDataField7 String containing data for Merchant defined data field 7
	 * @param MerchantDefinedDataField8 String containing data for Merchant defined data field 8
	 * @param MerchantDefinedDataField9 String containing data for Merchant defined data field 9
	 * @param MerchantDefinedDataField10 String containing data for Merchant defined data field 10
	 * @param MerchantDefinedDataField11 String containing data for Merchant defined data field 11
	 * @param MerchantDefinedDataField12 String containing data for Merchant defined data field 12
	 * @param MerchantDefinedDataField13 String containing data for Merchant defined data field 13
	 * @param MerchantDefinedDataField14 String containing data for Merchant defined data field 14
	 * @param MerchantDefinedDataField15 String containing data for Merchant defined data field 15
	 * @param MerchantDefinedDataField16 String containing data for Merchant defined data field 16
	 * @param MerchantDefinedDataField17 String containing data for Merchant defined data field 17
	 * @param MerchantDefinedDataField18 String containing data for Merchant defined data field 18
	 * @param MerchantDefinedDataField19 String containing data for Merchant defined data field 19
	 * @param MerchantDefinedDataField20 String containing data for Merchant defined data field 20
	 * @param paymentProcessingProfile String containing payment processing profile to be configured
	 */
	public void enableMerchantDefinedData(String MerchantDefinedDataField1, String MerchantDefinedDataField2,
			String MerchantDefinedDataField3, String MerchantDefinedDataField4, String MerchantDefinedDataField5,
			String MerchantDefinedDataField6, String MerchantDefinedDataField7, String MerchantDefinedDataField8,
			String MerchantDefinedDataField9, String MerchantDefinedDataField10, String MerchantDefinedDataField11,
			String MerchantDefinedDataField12, String MerchantDefinedDataField13, String MerchantDefinedDataField14,
			String MerchantDefinedDataField15, String MerchantDefinedDataField16, String MerchantDefinedDataField17,
			String MerchantDefinedDataField18, String MerchantDefinedDataField19, String MerchantDefinedDataField20,
			String paymentProcessingProfile) {

		logger.info("[ConfigurePaymentProcessingProfile][enableMerchantDefinedData] starts");
		driver = commonUtil.setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, userIndex);
		commonUtil.clickEditPaymentProcessingProfile(driver, paymentProcessingProfile);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_1,
				MerchantDefinedDataField1);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_2,
				MerchantDefinedDataField2);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_3,
				MerchantDefinedDataField3);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_4,
				MerchantDefinedDataField4);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_5,
				MerchantDefinedDataField5);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_6,
				MerchantDefinedDataField6);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_7,
				MerchantDefinedDataField7);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_8,
				MerchantDefinedDataField8);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_9,
				MerchantDefinedDataField9);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_10,
				MerchantDefinedDataField10);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_11,
				MerchantDefinedDataField11);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_12,
				MerchantDefinedDataField12);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_13,
				MerchantDefinedDataField13);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_14,
				MerchantDefinedDataField14);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_15,
				MerchantDefinedDataField15);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_16,
				MerchantDefinedDataField16);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_17,
				MerchantDefinedDataField17);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_18,
				MerchantDefinedDataField18);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_19,
				MerchantDefinedDataField19);
		commonUtil.setElementValueByXPath(driver, prop, Constants.MERCHANT_DEFINED_DATA_FIELD_20,
				MerchantDefinedDataField20);
		commonUtil.clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		logger.info("[ConfigurePaymentProcessingProfile][enableMerchantDefinedData] starts");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to configure a payment processing profile
	 * @param PluginName String containing name of plugin in which profile is created
	 * @param Name String containing name of payment processing profile
	 * @param Subsidiary String containing subsidiary of payment processing profile
	 * @param SettlementCurrency String containing settlement currency for the payment processing profile
	 * @param SettlementBankAccount String containing settlement bank account  of payment processing profile
	 * @param MerchantID String containing Merchant id to be configured for payment processing profile
	 * @param SecurityKey String containing security key of profile
	 * @param ProcessorName String containing name of processor used for transaction
	 * @param ProcessorLevel String containing processor level for transaction
	 * @param DelayShipment String containing shipment delay status for profile
	 * @param AuthExpirationCutOffDays String containing Authorization expiration cutoff days of payment processing profile
	 * @param RefundExpirationCutOffDays String containing refund expiration cutoff days of payment processing profile
	 * @param AVSandCVNConfiguration String containing AVS and CVN configuration setup of payment processing profile
	 * @param DeclineAVSFlag String containing decline avs flag setup configuration of payment processing profile
	 * @param NoAVSMatch String containing configuration setup for AVS mismatch setup of payment processing profile
	 * @param AVSServiceNotAvailable String containing AVS service unavailability setup of payment processing profile
	 * @param PartialAVSMatch String containing AVS partial match setup of payment processing profile
	 * @param CSCNotSubmitted String containing CSC not submitted setup of payment processing profile
	 * @param CSCNotSubmittedByCardholderBank String containing CSC not submitted by card holder bank setup of payment processing profile
	 * @param CSCServiceNotAvailable String containing CSC service unavailable setup of payment processing profile
	 * @param CSCCheckFailed String containing CSC check failed setup of payment processing profile
	 * @param NoCSCMatch String containing CSC mismatch setup of payment processing profile
	 * @param FraudManagementSceanrio String containing Fraud management scenario setup of payment processing profile
	 * @param FraudManagement String containing Fraud management status of payment processing profile
	 * @param DecisionManagerReject String containing DM reject setup configuration of payment processing profile
	 * @param ProfileID String containing Profile ID for transactions
	 * @param AcccessKey String containing Access key for transactions
	 * @param SecertKey String containing Secret key for transactions
	 * @param SuiteletUrl String containing Suitelet url of payment processing profile
	 * @param WebstoreURL String containing Webstore url available with payment processing profile
	 * @param HASMultiplePaymentMethods String containing configuration setup for multiple payment methods of payment processing profile
	 * @param PaymentMethods String containing Payment methods available in payment processing profile
	 * @param HasTokenization String containing Configuration setup for token of payment processing profile
	 * @param TokenPaymentMethod String containing token payment method in payment processing profile
	 */
	public void PaymentProcessingProfileConfiguration(String PluginName, String Name, String Subsidiary,
			String SettlementCurrency, String SettlementBankAccount, String MerchantID, String SecurityKey,
			String ProcessorName, String ProcessorLevel, String DelayShipment, String AuthExpirationCutOffDays,
			String RefundExpirationCutOffDays, String AVSandCVNConfiguration, String DeclineAVSFlag, String NoAVSMatch,
			String AVSServiceNotAvailable, String PartialAVSMatch, String CSCNotSubmitted,
			String CSCNotSubmittedByCardholderBank, String CSCServiceNotAvailable, String CSCCheckFailed,
			String NoCSCMatch, String FraudManagementSceanrio, String FraudManagement, String DecisionManagerReject,
			String ProfileID, String AcccessKey, String SecertKey, String SuiteletUrl, String WebstoreURL,
			String HASMultiplePaymentMethods, String PaymentMethods, String HasTokenization,
			String TokenPaymentMethod) {
		logger.info("[PaymentProcessingProfileConfiguration]starts");
		driver = commonUtil.setupDriver(driver, prop, Constants.NEW_PAYMENT_PROCESSING_PROFILE_URL, userIndex);
		commonUtil.createNewPaymentProcessingProfile(driver, PluginName);
		commonUtil.setElementValueByXPath(driver, prop, Constants.NEW_PPP_NAME, Name);
		commonUtil.setElementValueByXPath(driver, prop, Constants.NEW_PPP_SUBSIDIARY, Subsidiary);
		commonUtil.setElementValueByXPath(driver, prop, Constants.NEW_PPP_SETTLEMENT_CURRENCY, SettlementCurrency);
		commonUtil.setElementValueByXPath(driver, prop, Constants.NEW_PPP_SETTLEMENT_BANK_ACCOUNT, SettlementBankAccount);
		driver.switchTo().alert().accept();
		executor = (JavascriptExecutor) driver;
		logger.info(commonUtil.findElementByXPath(driver, prop, Constants.NEW_PPP_SUPPORT_LINE_LEVEL_DATA_CHECKBOX)
				.getAttribute("class"));
		if (commonUtil.findElementByXPath(driver, prop, Constants.NEW_PPP_SUPPORT_LINE_LEVEL_DATA_CHECKBOX)
				.getAttribute("class").contains(Constants.CHECKBOX_UNCK)) {
			commonUtil.clickElementByXPath(driver, prop, Constants.NEW_PPP_SUPPORT_LINE_LEVEL_DATA_CHECKBOX);
		}
		logger.info(
				commonUtil.findElementByXPath(driver, prop, Constants.NEW_PPP_TESTMODE_CHECKBOX).getAttribute("class"));
		if (commonUtil.findElementByXPath(driver, prop, Constants.NEW_PPP_TESTMODE_CHECKBOX).getAttribute("class")
				.contains(Constants.CHECKBOX_UNCK)) {
			commonUtil.clickElementByXPath(driver, prop, Constants.NEW_PPP_TESTMODE_CHECKBOX);
		}
		commonUtil.setElementValueByXPath(driver, prop, Constants.NEW_PPP_MERCHANTID, MerchantID);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		commonUtil.findElementByXPath(driver, prop, Constants.NEW_PPP_SECURITYKEY).sendKeys(SecurityKey);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_4);
		commonUtil.findElementByXPath(driver, prop, Constants.PPP_SECURITY_KEY_FILE).click();
		commonUtil.setElementValueByXPath(driver, prop, Constants.PPP_PROCESSOR_NAME, ProcessorName);
		// commonUtil.setElementValueByXPath(driver, prop,
		// Constants.PPP_PROCESSOR_LEVEL, ProcessorLevel);
//		if (Constants.YES.equals(DelayShipment.toUpperCase())) {
//			logger.info(commonUtil.findElementByXPath(driver, prop, Constants.NEW_PPP_DELAYSHIPMENT_CHECKBOX)
//					.getAttribute("class"));
//			if (commonUtil.findElementByXPath(driver, prop, Constants.NEW_PPP_DELAYSHIPMENT_CHECKBOX)
//					.getAttribute("class").contains(Constants.CHECKBOX_UNCK)) {
//				commonUtil.clickElementByXPath(driver, prop, Constants.NEW_PPP_DELAYSHIPMENT_CHECKBOX);
//			}
//			commonUtil.clearElementByXPath(driver, prop, Constants.NEW_PPP_AUTH_EXPIRATION_CUT_OFF_DAYS);
//			commonUtil.setElementValueByXPath(driver, prop, Constants.NEW_PPP_AUTH_EXPIRATION_CUT_OFF_DAYS,
//					AuthExpirationCutOffDays);
//			commonUtil.clearElementByXPath(driver, prop, Constants.NEW_PPP_REFUND_EXPIRATION_CUT_OFF_DAYS);
//			commonUtil.setElementValueByXPath(driver, prop, Constants.NEW_PPP_REFUND_EXPIRATION_CUT_OFF_DAYS,
//					RefundExpirationCutOffDays);
//		}
		commonUtil.setAVSConfiguration(driver, AVSandCVNConfiguration, DeclineAVSFlag, NoAVSMatch,
				AVSServiceNotAvailable, PartialAVSMatch);
		commonUtil.setCVNConfiguration(driver, AVSandCVNConfiguration, CSCNotSubmitted, CSCNotSubmittedByCardholderBank,
				CSCServiceNotAvailable, CSCCheckFailed, NoCSCMatch);
		// logger.info(commonUtil.findElementByXPath(driver, prop,
		// Constants.NEW_PPP_INTERNALORDERS_CHECKBOX)
		// .getAttribute("class"));
//		if (commonUtil.findElementByXPath(driver, prop, Constants.NEW_PPP_INTERNALORDERS_CHECKBOX).getAttribute("class")
//				.contains(Constants.CHECKBOX_UNCK)) {
//			commonUtil.clickElementByXPath(driver, prop, Constants.NEW_PPP_INTERNALORDERS_CHECKBOX);
		// }
//		logger.info(commonUtil.findElementByXPath(driver, prop, Constants.NEW_PPP_WEBSTORREORDERS_CHECKBOX)
//				.getAttribute("class"));
//		if (commonUtil.findElementByXPath(driver, prop, Constants.NEW_PPP_WEBSTORREORDERS_CHECKBOX)
//				.getAttribute("class").contains(Constants.CHECKBOX_UNCK)) {
//			commonUtil.clickElementByXPath(driver, prop, Constants.NEW_PPP_WEBSTORREORDERS_CHECKBOX);
//		}
		// commonUtil.clearElementByXPath(driver, prop, Constants.PPP_FRAUD_MANAGEMENT);
		if (Constants.YES.equals(FraudManagementSceanrio.toUpperCase())) {
			logger.info(
					commonUtil.findElementByXPath(driver, prop, Constants.PPP_FRAUD_MANAGEMENT).getAttribute("class"));
			if (commonUtil.findElementByXPath(driver, prop, Constants.PPP_FRAUD_MANAGEMENT).getAttribute("class")
					.contains(Constants.CHECKBOX_UNCK)) {
				commonUtil.clickElementByXPath(driver, prop, Constants.PPP_FRAUD_MANAGEMENT);
			}
			commonUtil.clearElementByXPath(driver, prop, Constants.PPP_DECISION_MANAGER_REJECT);
			commonUtil.setElementValueByXPath(driver, prop, Constants.PPP_DECISION_MANAGER_REJECT,
					DecisionManagerReject);
		}
		commonUtil.setSecureAcceptanceConfiguration(driver, ProfileID, AcccessKey, SecertKey, SuiteletUrl, WebstoreURL);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		if (Constants.YES.equals(HASMultiplePaymentMethods.toUpperCase())) {
			Object PaymentMethod[][] = null;
			PaymentMethod = commonUtil.getPaymentMethods(PaymentMethods);
			commonUtil.setPaymentMethods(driver, PaymentMethod);
		}
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		if (Constants.YES.equals(HasTokenization.toUpperCase())) {
			logger.info(commonUtil.findElementByXPath(driver, prop, Constants.NEW_PPP_TOKENIZATION_CHECKBOX)
					.getAttribute("class"));
			if (commonUtil.findElementByXPath(driver, prop, Constants.NEW_PPP_TOKENIZATION_CHECKBOX)
					.getAttribute("class").contains(Constants.CHECKBOX_UNCK)) {
				commonUtil.clickElementByXPath(driver, prop, Constants.NEW_PPP_TOKENIZATION_CHECKBOX);
			}
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
			commonUtil.findElementByXPath(driver, prop, Constants.NEW_PPP_TOKENIZATION_PM).click();
			commonUtil.setElementValueByXPath(driver, prop, Constants.NEW_PPP_TOKENIZATION_PM, TokenPaymentMethod);
			commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		}
		commonUtil.clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_10);
		logger.info("[PaymentProcessingProfileConfiguration]ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to configure payment methods
	 * @param PaymentMethodName String containing name of payment method to be configured
	 * @param Type String containing payment type
	 * @param Deposit String containing deposit data
	 */
	public void PaymentMethodConfiguration(String PaymentMethodName, String Type, String Deposit) {
		logger.info("[PaymentMethodConfiguration]starts");
		driver = commonUtil.setupDriver(driver, prop, Constants.PAYMENT_METHOD_URL, userIndex);
		int count = commonUtil.checkPaymentMethod(driver, PaymentMethodName, Type, Deposit);
		if (count != 1) {
			commonUtil.clickElementByXPath(driver, prop, Constants.PAYMENT_METHOD_ADD_BUTTON);
		}
		commonUtil.setPaymentMethod(driver, PaymentMethodName, Type, Deposit);
		logger.info("[PaymentMethodConfiguration]ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to map payment method to payment method
	 * @param RecordName String containing name of record
	 * @param OMPMMappingMethod String containing mapping method between order management and payment method
	 */
	public void OrderManagementPaymentMethodMapping(String RecordName, String OMPMMappingMethod) {
		logger.info("[OrderManagementPaymentMethodMapping]starts");
		Object OMPMMapping[][] = null;
		driver = commonUtil.setupDriver(driver, prop, Constants.PAYMENT_METHOD_MAPPING_URL, userIndex);
		commonUtil.clickElementByLinkText(driver, RecordName);
		commonUtil.clickElementByXPath(driver, prop, Constants.OM_PM_MAPPING_MORE);
		commonUtil.clickElementByXPath(driver, prop, Constants.OM_PM_MAPPING_VIEW_RECORDS);
		OMPMMapping = commonUtil.getOMPMMappingMethod(OMPMMappingMethod);
		commonUtil.setOMPMMappingMethod(driver, OMPMMapping);
		logger.info("[OrderManagementPaymentMethodMapping]ends");
	}
}