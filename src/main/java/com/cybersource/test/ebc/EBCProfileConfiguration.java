package com.cybersource.test.ebc;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.cybersource.test.CommonTest;
import com.cybersource.test.util.CommonUtil;
import com.cybersource.test.util.Constants;
import com.cybersource.test.util.FileUtil;

@Listeners(com.cybersource.test.util.Listeners.class)
/**
 * EBCProfileConfiguration class have all related functionality used to configure profile in EBC portal
 * @author AD20243779
 *
 */
public class EBCProfileConfiguration extends CommonTest {
	CommonUtil commonUtil = new CommonUtil();
	String netSuiteProfileDataSheetPrefix = null;
	static WebDriver driver = null;
	static final Logger logger = Logger.getLogger(EBCProfileConfiguration.class);

	@BeforeTest
	/**
	 * This method is used to initialize test environment before running a test
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @throws IOException Used to handle exceptions
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
	public Object[][] getProfileData(ITestContext context) {
		Object[][] data = null;
		String eBCProfileDataSheetPrefix = context.getCurrentXmlTest()
				.getParameter(Constants.EBC_PROFILE_DATA_SHEET_PREFIX);
		netSuiteProfileDataSheetPrefix = context.getCurrentXmlTest()
				.getParameter(Constants.NETSUITE_PROFILE_DATA_SHEET_PREFIX);
		data = FileUtil.getSheetData(XLSX_FILE_PATH,
				prop.getProperty(eBCProfileDataSheetPrefix + Constants._SHEET_NAME));
		return data;
	}

	@SuppressWarnings("unused")
	@Test(dataProvider = "getProfileData")
	/**
	 * This method is used to create secure acceptance profile in ebc portal
	 * @param ExistingProfileName String containing existing profile name in ebc
	 * @param NewProfileName String containing new profile name
	 * @param ProfileDescription String containing description for profile
	 * @param SecurityKeyName String containing profile security key
	 * @param SuitletUrl String containing Suitelet url
	 */
	public void createSecureAcceptanceProfile(String ExistingProfileName, String NewProfileName,
			String ProfileDescription, String SecurityKeyName, String SuitletUrl) {
		int end = 0;
		String profileId = null;
		String accessKeyElementContent = null;
		StringBuffer buf = null;
		WebElement accessKeyElement = null;
		driver = setupDriver(prop, Constants.EBC_SECURE_ACCEPTANCE_PROFILE_URL);
		setupDriver(prop, Constants.EBC_SECURE_ACCEPTANCE_PROFILE_URL);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_10);
		commonUtil.checkEBCProfile(driver, ExistingProfileName);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_12);
		commonUtil.clickElementByXPath(driver, prop, Constants.EBC_COPY_PROFILE);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementByCssSelector(driver, prop, Constants.EBC_PROFILE_COPY_CONFIRM);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		commonUtil.findElementByCssSelector(driver, prop, Constants.EBC_PROFILE_NAME).sendKeys(NewProfileName);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.findElementByCssSelector(driver, prop, Constants.EBC_PROFILE_DESCRIPTION)
				.sendKeys(ProfileDescription);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementByXPath(driver, prop, Constants.PROFILE_SUBMIT);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		profileId = commonUtil.findElementByXPath(driver, prop, Constants.PROFILE_ID).getText();
		commonUtil.clickElementByXPath(driver, prop, Constants.SECURITY_TAB);
		commonUtil.clickElementByXPath(driver, prop, Constants.CREATE_KEY);
		commonUtil.findElementByCssSelector(driver, prop, Constants.EBC_PROFILE_SECURITY_KEY_NAME)
				.sendKeys(SecurityKeyName);
		commonUtil.clickElementByXPath(driver, prop, Constants.CREATE_KEY_BUTTON);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.CREATE_KEY_CONFIRM);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		accessKeyElement = commonUtil.findElementByXPath(driver, prop, Constants.SECURITY_KEYS_PAGE);
		accessKeyElementContent = accessKeyElement.getAttribute(Constants.INNERHTML);
		if (accessKeyElementContent.contains(Constants.VALUE)) {
			buf = new StringBuffer(accessKeyElementContent);
			end = accessKeyElementContent.lastIndexOf(Constants.VALUE);
			accessKeyElementContent = buf.substring(end + Constants.SEVEN, end + Constants.THIRTY_NINE);
		}
	}

	/**
	 * This method is used to Setup driver
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param url String containing url which is to be set up in browser
	 * @return Webdriver element that is used for further operations
	 */
	public WebDriver setupDriver(Properties prop, String url) {
		if (null == driver) {
			System.setProperty(Constants.WEBDRIVER_CHROME_DRIVER, prop.getProperty(Constants.CHROMEDRIVER_PATH));
			ChromeOptions options = new ChromeOptions();
			driver = new ChromeDriver(options);
			commonUtil.maximizeWindow(driver);
			doEBCLogin(driver, prop, url);
		} else {
			driver.get(prop.getProperty(url));
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
		}
		return driver;
	}

	/**
	 * This method is used login to EBC portal
	 * @param driver Webdriver element that is used for further operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param url String containing url to set driver to ebc portal
	 */
	public void doEBCLogin(WebDriver driver, Properties prop, String url) {
		logger.info("[EBCProfileConfiguration][doEBCLogin] called");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
		driver.get(prop.getProperty(url));
		commonUtil.sleep(commonUtil.SLEEP_TIMER_40);
		commonUtil.setElementValueByXPath(driver, prop, Constants.EBC_ORGANIZATION_ID_ATTRIBUTE,
				prop.getProperty(Constants.EBC_ORGANIZATION_ID_ATTRIBUTE_VALUE));
		commonUtil.setElementValueByXPath(driver, prop, Constants.EBC_USERNAME_ATTRIBUTE,
				prop.getProperty(Constants.EBC_USERNAME_ATTRIBUTE_VALUE));
		commonUtil.setElementValueByXPath(driver, prop, Constants.EBC_PASSWORD_ATTRIBUTE,
				prop.getProperty(Constants.EBC_PASSWORD_ATTRIBUTE_VALUE));
		commonUtil.clickElementByCssSelector(driver, prop, Constants.EBC_LOGIN_SUBMIT_BUTTON);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
	}
}
