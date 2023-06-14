package com.cybersource.test.ebc;

import java.io.IOException;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.cybersource.test.CommonTest;
import com.cybersource.test.util.CommonUtil;
import com.cybersource.test.util.Constants;

/**
 * EBCTransactionCheck class contains all functionality to do validation operations in EBC portal
 * @author AD20243779
 *
 */
public class EBCTransactionCheck extends CommonTest {
	WebDriver driver = null;
	String userIndex = "1";
	CommonUtil commonUtil = new CommonUtil();

	@BeforeTest
	/**
	 * This method is used to initialize test environment before running a test
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @throws IOException Used to handle exceptions
	 */
	public void init(ITestContext context) throws IOException {
		super.loadCommonConfig(context);
	}

	@Test()
	@Parameters({ "transactionID" })
	/**
	 * This method is used to search for a transaction using Transaction ID
	 * @param transactionID string containing transaction id which is to be searched in ebc portal
	 */
	public void executeTransactionSearch(String transactionID) {
		driver = commonUtil.setupDriver(driver, prop, null, "ebc", userIndex);
		commonUtil.clickElementByCssSelector(driver, prop, "ebc.transaction.management.cssPath");
		commonUtil.clickElementByLinkText(driver, Constants.TRANSACTIONS);
		commonUtil.clickElementByCssSelector(driver, prop, "ebc.transactions.request.id.filter");
		commonUtil.findElementByCssSelector(driver, prop, "ebc.transactions.request.id.filter").sendKeys(transactionID);
		commonUtil.findElementByCssSelector(driver, prop, "ebc.transactions.request.id.filter").sendKeys(Keys.ENTER);
		commonUtil.clickElementByLinkText(driver, transactionID);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
	}
}