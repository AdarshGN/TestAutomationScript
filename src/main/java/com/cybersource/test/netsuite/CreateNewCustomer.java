package com.cybersource.test.netsuite;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import com.cybersource.test.CommonTest;
import com.cybersource.test.util.CommonUtil;
import com.cybersource.test.util.Constants;
import com.cybersource.test.util.FileUtil;

/**
 * CreateNewCustomer class which will have all functionality related to creation of new customer
 * @author AD20243779
 *
 */
public class CreateNewCustomer extends CommonTest {
	CommonUtil commonUtil = new CommonUtil();
	String CustomerId = null;
	WebDriver driver = null;
	String userIndex = "1";
	final static Logger logger = Logger.getLogger(CreateNewCustomer.class);

	@BeforeTest
	/**
	 * This method is used to initialize test environment before running a test
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @throws IOException Handles when an I/O exception of some sort has occurred
	 */
	public void init(ITestContext context) throws IOException {
		super.loadCommonConfig(context);
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
		// Object[][] data = FileUtil.getSheetData(XLSX_FILE_PATH,
		// prop.getProperty( currentScenario + Constants._SHEET_NAME));
		Object[][] data = FileUtil.getSheetData(XLSX_FILE_PATH,
				prop.getProperty(prefix + currentScenario + Constants._SHEET_NAME));
		logger.info("[CreateNewCustomer][getData]  currentScenario:" + currentScenario);
		return data;
	}

	@SuppressWarnings("unused")
	@Test(dataProvider = Constants.GET_DATA)
	/**
	 * This method is used to create a new customer and to configure details of the customer
	 * @param CustomerFirstName String containing first name of the customer
	 * @param CustomerLastName String containing last name of the customer
	 * @param CustomerEmail String containing email of the customer
	 * @param Subsidiary String containing subsidiary of the customer
	 * @param Country String containing country of the customer
	 * @param AddressOne String containing Address of the customer
	 * @param City String containing city of the customer
	 * @param State String containing state of the customer
	 * @param Zip String containing zip code of the customer
	 * @param HasMultipleCards String containing setup for multiple cards of the customer
	 * @param Cards String containing cards of the customer
	 * @param PaymentMethod String containing payment method of the customer
	 * @param PaymentCardNumber String containing card number of the customer
	 * @param ExpirationDate String containing card expiration date of the customer
	 * @param CardBrand String containing card brand of the customer
	 * @param CardholderName String containing cardholder name of the customer
	 */
	public void createCustomer(String CustomerFirstName, String CustomerLastName, String CustomerEmail,
			String Subsidiary, String Country, String AddressOne, String City, String State, String Zip,
			String HasMultipleCards, String Cards, String PaymentMethod, String PaymentCardNumber,
			String ExpirationDate, String CardBrand, String CardholderName) {
		logger.info("[CreateNewCustomer][createCustomer] Starts");
		String childWindow = null;
		String mainWindow = null;
		Set<String> allWindows = null;
		Iterator<String> itr = null;
		JavascriptExecutor executor = null;
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_CUSTOMER_URL, userIndex);
		executor = (JavascriptExecutor) driver;

		// commonUtil.clickElementByXPath(driver, prop,
		// Constants.CUSTOMERS_NEW_CUSTOMER);
		commonUtil.clickElementByCssSelector(driver, prop, Constants.CUSTOMER_TYPE);
		commonUtil.setElementValueByXPath(driver, prop, Constants.CUSTOMER_FIRST_NAME, CustomerFirstName);
		commonUtil.setElementValueByXPath(driver, prop, Constants.CUSTOMER_LAST_NAME, CustomerLastName);
		commonUtil.setElementValueByXPath(driver, prop, Constants.CUSTOMER_EMAIL, CustomerEmail);
		// commonUtil.setValueByXPath(driver, prop,
		// Constants.CUSTOMER_SUBSIDIARY,Subsidiary);
		commonUtil.clickElementByXPath(driver, prop, Constants.CUSTOMER_ADDRESS_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.scrollToBottom(driver);

		commonUtil.clickElementByXPath(driver, prop, Constants.CUSTOMER_ADDRESS_EDIT);
		commonUtil.switchToFrame(driver, Constants.TWO);
		commonUtil.setElementValueByXPath(driver, prop, Constants.CUSTOMER_ADDRESS_COUNTRY, Country);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.setElementValueByXPath(driver, prop, Constants.CUSTOMER_ADDRESS_ADDRESSONE, AddressOne);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.setElementValueByXPath(driver, prop, Constants.CUSTOMER_ADDRESS_CITY, City);
		commonUtil.setElementValueByXPath(driver, prop, Constants.CUSTOMER_ADDRESS_STATE, State);
		commonUtil.setElementValueByXPath(driver, prop, Constants.CUSTOMER_ADDRESS_ZIP, Zip);
		commonUtil.clickElementByXPath(driver, prop, Constants.CUSTOMER_ADDRESS_OVERRIDE);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementByXPath(driver, prop, Constants.CUSTOMER_ADDRESS_OVERRIDE);
		commonUtil.clickElementByXPath(driver, prop, Constants.CUSTOMER_ADDRESS_OK);
		commonUtil.switchToDefaultContent(driver);
		commonUtil.clickElementByXPath(driver, prop, Constants.CUSTOMER_ADDRESS_ADD);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		commonUtil.clickElementByXPath(driver, prop, Constants.CUSTOMER_FINANCIAL_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_10);
		commonUtil.scrollToBottom(driver);
		commonUtil.clickLinkByXPath(driver, prop, Constants.CUSTOMER_PAYMENT_INSTRUMENT_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_10);

		if (Constants.YES.equals(HasMultipleCards.toUpperCase())) {
			Object paymentDetails[][] = null;
			paymentDetails = commonUtil.getpaymentCards(Cards);
			commonUtil.setPaymentCards(driver, paymentDetails);
		} else {
			commonUtil.clickElementByXPath(driver, prop, Constants.CUSTOMER_PAYMENT_INSTRUMENT_NEW);
			mainWindow = driver.getWindowHandle();
			allWindows = driver.getWindowHandles();
			itr = allWindows.iterator();
			while (itr.hasNext()) {
				childWindow = itr.next();
				if (!mainWindow.equals(childWindow)) {
					commonUtil.switchToWindow(driver, childWindow);
					// commonUtil.setFieldText(Constants.NLAPI_FIELD_TEXT_PREFIX_INSTRUMENT_TYPE,
					// "", Constants.NLAPI_FIELD_TEXT_SUFFIX_INSTRUMENT_TYPE);
					commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
					commonUtil.setElementValueByXPath(driver, prop,
							Constants.PAYMENT_INSTRUMENT_NEW_TYPE_PAYMENT_METHOD, PaymentMethod);
					commonUtil.setElementValueByXPath(driver, prop,
							Constants.PAYMENT_INSTRUMENT_NEW_TYPE_PAYMENT_CARD_NUMBER, PaymentCardNumber);
					commonUtil.setElementValueByXPath(driver, prop,
							Constants.PAYMENT_INSTRUMENT_NEW_TYPE_PAYMENT_CARD_EXPIRATIONDATE, ExpirationDate);
					commonUtil.setElementValueByXPath(driver, prop,
							Constants.PAYMENT_INSTRUMENT_NEW_TYPE_PAYMENT_CARD_CARDBRAND, CardBrand);
					commonUtil.clearElementByXPath(driver, prop,
							Constants.PAYMENT_INSTRUMENT_NEW_TYPE_PAYMENT_CARD_CARDHOLDERNAME);
					commonUtil.setElementValueByXPath(driver, prop,
							Constants.PAYMENT_INSTRUMENT_NEW_TYPE_PAYMENT_CARD_CARDHOLDERNAME, CardholderName);
					commonUtil.scrollToBottom(driver);
					commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
					commonUtil.closeCurrentWindow(driver);
				}
			}
			commonUtil.switchToWindow(driver, mainWindow);
		}
		// CustomerId = commonUtil.findElementByXPath(driver, prop,
		// Constants.NEW_CUSTOMER_ID).getText();
		logger.info("[CreateNewCustomer][createCustomer] ends");
	}
}