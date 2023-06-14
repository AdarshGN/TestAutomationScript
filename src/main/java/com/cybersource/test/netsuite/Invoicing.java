package com.cybersource.test.netsuite;

import java.io.IOException;

import org.apache.log4j.Logger;
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
import com.cybersource.test.util.NetSuiteCommonUtil;
import com.cybersource.test.util.RetryFailedTestCases;

@Listeners(com.cybersource.test.util.Listeners.class)
/**
 * Invoicing class which will have all functionality related to creation and updation of invoice
 * @author AD20243779
 *
 */
public class Invoicing extends CommonTest {
	CommonUtil commonUtil = new CommonUtil();
	NetSuiteCommonUtil netSuiteCommonUtil = new NetSuiteCommonUtil();
	String screenShotModulePath = null;
	String screenShotPath = null;
	WebDriver driver = null;
	String userIndex = "1";
	final static Logger logger = Logger.getLogger(Invoicing.class);

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
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		screenShotModulePath = commonUtil.getScreenshotsModulePath(Constants.INVOICING);
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
		logger.info("[Invoicing][getData] currentScenario:" + currentScenario);
		return data;
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute invoicing
	 * @param CustomerName String containing name of customer placing order
	 * @param CustomerLocation String containing location of customer placing order
	 * @param Memo String containing memo of the order
	 * @param ItemName String containing item name to be ordered
	 * @param Quantity String containing quantity of item to be ordered
	 * @param PriceLevel String containing price level of item
	 * @param Rate String containing rate of item
	 * @param ItemTaxCode String containing tax code of item
	 * @param MidAccount String containing Merchant ID of account
	 * @param CreateInvoice String to set create invoice value from dropdown
	 * @param InvoiceId String containing id of invoice
	 * @param PartialAllowedAmount String containing partial amount allowed
	 * @param DueDate String containing due date of the order
	 * @param UpdateConfiguration String containing configuration update status
	 * @param Name String containing invoicing configuration field name
	 * @param TestMode String configuring test mode setup
	 * @param MerchantID String containing invoicing configuration merchant id
	 * @param Key String containing invoicing configuration field key
	 * @param SecretKey String containing invoicing configuration secret key
	 * @param ImportEBCInvoices String containing invoicing configuration EBC import invoices
	 * @param OnDemandScheduler String containing invoicing configuration ondemand scheduler
	 * @param Subsidiary String containing invoicing configuration subsidiary
	 * @param Location String containing invoicing configuration location
	 * @param DefaultCustomer String containing invoicing configuration default customer
	 * @param Item String containing invoicing configuration item
	 * @param ShippingItem String containing invoicing configuration shipping item
	 * @param TaxItem String containing invoicing configuration tax item
	 * @param DiscountItem String containing invoicing configuration discount item
	 * @param TaxCode String containing invoicing configuration tax code
	 * @param DepositAccount String containing invoicing configuration deposit account
	 * @param TestScenario String containing order test scenario
	 * @throws Exception Used to handle exceptions that may happen
	 */
	public void executeInvoicing(String CustomerName, String CustomerLocation, String Memo, String ItemName,
			String Quantity, String PriceLevel, String Rate, String ItemTaxCode, String MidAccount,
			String CreateInvoice, String InvoiceId, String PartialAllowedAmount, String DueDate,
			String UpdateConfiguration, String Name, String TestMode, String MerchantID, String Key, String SecretKey,
			String ImportEBCInvoices, String OnDemandScheduler, String Subsidiary, String Location,
			String DefaultCustomer, String Item, String ShippingItem, String TaxItem, String DiscountItem,
			String TaxCode, String DepositAccount, String TestScenario) throws Exception {
		logger.info("[Invoicing][executeInvoicing] Starts");
		driver = null;
		netSuiteCommonUtil.SetupInvoicingConfigurationAndCreateInvoice(driver, userIndex, CustomerName,
				CustomerLocation, Memo, ItemName, Quantity, PriceLevel, Rate, ItemTaxCode, MidAccount, CreateInvoice,
				InvoiceId, PartialAllowedAmount, DueDate, UpdateConfiguration, Name, TestMode, MerchantID, Key,
				SecretKey, ImportEBCInvoices, OnDemandScheduler, Subsidiary, Location, DefaultCustomer, Item,
				ShippingItem, TaxItem, DiscountItem, TaxCode, DepositAccount, TestScenario, screenShotPath,
				screenShotModulePath);
		logger.info("[Invoicing][executeInvoicing] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to execute invoice creation and updation
	 * @param CustomerName String containing name of customer placing order
	 * @param CustomerLocation String containing location of customer placing order
	 * @param Memo String containing memo of the order
	 * @param ItemName String containing item name to be ordered
	 * @param Quantity String containing quantity of item to be ordered
	 * @param PriceLevel String containing price level of item
	 * @param Rate String containing rate of item
	 * @param ItemTaxCode String containing tax code of item
	 * @param MidAccount String containing Merchant ID of account
	 * @param CreateInvoice String to set create invoice value from dropdown
	 * @param InvoiceId String containing id of invoice
	 * @param PartialAllowedAmount String containing partial amount allowed
	 * @param DueDate String containing due date of the order
	 * @param UpdateConfiguration String containing configuration update status
	 * @param Name String containing invoicing configuration field name
	 * @param TestMode String configuring test mode setup
	 * @param MerchantID String containing invoicing configuration merchant id
	 * @param Key String containing invoicing configuration field key
	 * @param SecretKey String containing invoicing configuration secret key
	 * @param ImportEBCInvoices String containing invoicing configuration EBC import invoices
	 * @param OnDemandScheduler String containing invoicing configuration ondemand scheduler
	 * @param Subsidiary String containing invoicing configuration subsidiary
	 * @param Location String containing invoicing configuration location
	 * @param DefaultCustomer String containing invoicing configuration default customer
	 * @param Item String containing invoicing configuration item
	 * @param ShippingItem String containing invoicing configuration shipping item
	 * @param TaxItem String containing invoicing configuration tax item
	 * @param DiscountItem String containing invoicing configuration discount item
	 * @param TaxCode String containing invoicing configuration tax code
	 * @param DepositAccount String containing invoicing configuration deposit account
	 * @param UpdateCustomerName String containing customer name to be updated with
	 * @param UpdateCustomerLocation String containing customer location to be updated with
	 * @param UpdateMemo String containing memo to be updated with
	 * @param UpdateItemName String containing item name to be updated with
	 * @param UpdateQuantity String containing quantity to be updated with
	 * @param UpdatePriceLevel String containing price level to be updated with
	 * @param UpdateRate String containing rate to be updated with
	 * @param UpdateItemTaxCode String containing item tax code to be updated with
	 * @param UpdateInvoiceAction String containing invoice action to be updated with
	 * @param UpdateDueDate String containing due date to be updated with
	 * @param TestScenario String containing order test scenario
	 * @throws Exception Used to handle exceptions that may happen
	 */
	public void executeInvoicingCreateAndUpdate(String CustomerName, String CustomerLocation, String Memo,
			String ItemName, String Quantity, String PriceLevel, String Rate, String ItemTaxCode, String MidAccount,
			String CreateInvoice, String InvoiceId, String PartialAllowedAmount, String DueDate,
			String UpdateConfiguration, String Name, String TestMode, String MerchantID, String Key, String SecretKey,
			String ImportEBCInvoices, String OnDemandScheduler, String Subsidiary, String Location,
			String DefaultCustomer, String Item, String ShippingItem, String TaxItem, String DiscountItem,
			String TaxCode, String DepositAccount, String UpdateCustomerName, String UpdateCustomerLocation,
			String UpdateMemo, String UpdateItemName, String UpdateQuantity, String UpdatePriceLevel, String UpdateRate,
			String UpdateItemTaxCode, String UpdateInvoiceAction, String UpdateDueDate, String TestScenario)
			throws Exception {
		logger.info("[InvoicingCreateAndUpdate][executeInvoicingCreateAndUpdate] Starts");
		driver = null;
		netSuiteCommonUtil.InvoicingCreateAndUpdate(driver, userIndex, CustomerName, CustomerLocation, Memo, ItemName,
				Quantity, PriceLevel, Rate, ItemTaxCode, MidAccount, CreateInvoice, InvoiceId, PartialAllowedAmount,
				DueDate, UpdateConfiguration, Name, TestMode, MerchantID, Key, SecretKey, ImportEBCInvoices,
				OnDemandScheduler, Subsidiary, Location, DefaultCustomer, Item, ShippingItem, TaxItem, DiscountItem,
				TaxCode, DepositAccount, UpdateCustomerName, UpdateCustomerLocation, UpdateMemo, UpdateItemName,
				UpdateQuantity, UpdatePriceLevel, UpdateRate, UpdateItemTaxCode, UpdateInvoiceAction, UpdateDueDate,
				TestScenario, screenShotPath, screenShotModulePath);
		logger.info("[InvoicingCreateAndUpdate][executeInvoicingCreateAndUpdate] Ends");
	}

	@Test(dataProvider = Constants.GET_DATA, retryAnalyzer = RetryFailedTestCases.class)
	/**
	 * This method is used to update invoice
	 * @param InvoiceNumber String containing invoice number of the order
	 * @param CustomerName String containing name of the customer placing order
	 * @param CustomerLocation String containing location of the customer placing order
	 * @param Memo String containing sales order memo
	 * @param ItemName String containing name ordered items
	 * @param Quantity String containing quantity of items ordered
	 * @param PriceLevel String containing price level of the order
	 * @param Rate String containing order rate
	 * @param ItemTaxCode String containing tax code of the item ordered
	 * @param InvoiceAction String containing detail of invoice action to be done
	 * @param DueDate String containing due date of the order
	 * @param TestScenario String containing order test scenario
	 * @throws Exception Used to handle exceptions that may happen
	 */
	public void executeInvoiceUpdate(String InvoiceNumber, String CustomerName, String CustomerLocation, String Memo,
			String ItemName, String Quantity, String PriceLevel, String Rate, String ItemTaxCode, String InvoiceAction,
			String DueDate, String TestScenario) throws Exception {
		logger.info("[InvoiceUpdate][executeInvoiceUpdate] Starts");
		driver = null;
		netSuiteCommonUtil.findInvoiceAndUpdate(driver, userIndex, InvoiceNumber, CustomerName, CustomerLocation, Memo,
				ItemName, Quantity, PriceLevel, Rate, ItemTaxCode, InvoiceAction, DueDate, TestScenario, screenShotPath,
				screenShotModulePath);
		logger.info("[InvoiceUpdate][executeInvoiceUpdate] Ends");
	}
}
