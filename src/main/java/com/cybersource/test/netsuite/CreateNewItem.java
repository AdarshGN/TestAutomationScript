package com.cybersource.test.netsuite;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.cybersource.test.CommonTest;
import com.cybersource.test.util.CommonUtil;
import com.cybersource.test.util.Constants;
import com.cybersource.test.util.FileUtil;

/**
 * CreateNewItem class which contains all functionalities related to creating a new item to inventory to be ordered in future
 * @author AD20243779
 *
 */
public class CreateNewItem extends CommonTest {
	CommonUtil commonUtil = new CommonUtil();
	String CustomerId = null;
	WebDriver driver = null;
	String userIndex = "1";
	final static Logger logger = Logger.getLogger(CreateNewItem.class);

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

	@SuppressWarnings("unused")
	@Test(dataProvider = Constants.GET_DATA)
	/**
	 * This method is used to create a new item to the inventory
	 * @param ItemName String containing name of the item
	 * @param ItemUpccode String containing UPC code of the item
	 * @param ItemDisplayName String containing display name of the item
	 * @param ItemBasePrice String containing base price of the item
	 * @param ItemOnlinePrice String containing online price of the item
	 * @param Itemtax String containing tax of the item
	 * @param ItemWebsiteName String containing website name of item
	 * @param ItemWebsitePage String containing website page of item
	 */
	public void createItem(String ItemName, String ItemUpccode, String ItemDisplayName, String ItemBasePrice,
			String ItemOnlinePrice, String Itemtax, String ItemWebsiteName, String ItemWebsitePage) {
		logger.info("[CreateNewItem][createItem] starts");
		JavascriptExecutor executor = null;
		driver = commonUtil.setupDriver(driver, prop, Constants.CREATE_ITEM_URL, userIndex);
		executor = (JavascriptExecutor) driver;
		commonUtil.setElementValueByXPath(driver, prop, Constants.ITEM_NAME, ItemName);
		// commonUtil.setElementValueByXPath(driver, prop, Constants.ITEM_UPCCODE,
		// ItemUpccode);
		commonUtil.setElementValueByXPath(driver, prop, Constants.ITEM_DISPLAYNAME, ItemDisplayName);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.findElementByXPath(driver, prop, Constants.ITEM_ACCOUNTING_SECTION).click();
		commonUtil.setElementValueByXPath(driver, prop, Constants.ITEM_TAX, Itemtax);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementByXPath(driver, prop, Constants.ITEM_WEB_SECTION);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		if (commonUtil.findElementByXPath(driver, prop, Constants.ITEM_WEBSTORE_CHECKBOX).getAttribute("class")
				.contains(Constants.CHECKBOX_UNCK)) {
			commonUtil.clickElementByXPath(driver, prop, Constants.ITEM_WEBSTORE_CHECKBOX);
		}
		commonUtil.clickElementByXPath(driver, prop, Constants.ITEM_WEBSITE_CATEGORIES_SECTION);
		commonUtil.setElementValueByXPath(driver, prop, Constants.ITEM_WEBSITE_NAME, ItemWebsiteName);
		commonUtil.clickElementByXPath(driver, prop, Constants.ITEM_WEBSITE_PAGE_ID);
		commonUtil.clickElementByXPath(driver, prop, Constants.ITEM_WEBSITE_PAGE_ID);
		commonUtil.clickElementByXPath(driver, prop, Constants.ITEM_WEBSITE_PAGE);
		commonUtil.setElementValueByXPath(driver, prop, Constants.ITEM_WEBSITE_PAGE, ItemWebsitePage);
		commonUtil.clickElementByXPath(driver, prop, Constants.ITEM_WEBSITE_EDIT);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.ITEM_WEBSITE_EDIT);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.setElementValueByXPath(driver, prop, Constants.ITEM_WEBSTORE_NAME, ItemName);
		commonUtil.scroll100Up(driver);
		commonUtil.clickElementByXPath(driver, prop, Constants.ITEM_SALES_SECTION);
		commonUtil.setElementValueByXPath(driver, prop, Constants.ITEM_BASE_PRICE, ItemBasePrice);
		commonUtil.clearElementByXPath(driver, prop, Constants.ITEM_ONLINE_PRICE);
		commonUtil.setElementValueByXPath(driver, prop, Constants.ITEM_ONLINE_PRICE, ItemOnlinePrice);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		logger.info("[CreateNewItem][createItem] ends");
	}
}