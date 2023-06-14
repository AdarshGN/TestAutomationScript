package com.cybersource.test.netsuite;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
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
 * NetSuiteCustomRoleConfiguration class which will have all functionality related to configuration of custom role to user
 * @author AD20243779
 *
 */
public class NetSuiteCustomRoleConfiguration extends CommonTest {
	CommonUtil commonUtil = new CommonUtil();
	static String rowNumber = null;
	static String mainWindow = null;
	static WebDriver driver = null;
	String userIndex = "1";
	int i = 2;
	int j = 2;
	static final Logger logger = Logger.getLogger(NetSuiteCustomRoleConfiguration.class);

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
	 * This method is used to get data from excel sheets about role name id
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @return Object containing rolename id data
	 */
	public Object[][] getRoleNameIdData(ITestContext context) {
		Object[][] data = null;
		data = FileUtil.getSheetData(XLSX_FILE_PATH, prop.getProperty(Constants.CUSTOM_ROLE_NAME_ID_SHEET_NAME));
		return data;
	}

	@DataProvider
	/**
	 * This method is used to get data from excel sheets about role assignee
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @return Object containing role assignee data
	 */
	public Object[][] getRoleAssigneeData(ITestContext context) {
		Object[][] data = null;
		data = FileUtil.getSheetData(XLSX_FILE_PATH, prop.getProperty(Constants.CUSTOM_ROLE_ASSIGNEE_SHEET_NAME));
		return data;
	}

	@DataProvider
	/**
	 * This method is used to get data from excel sheets about transaction permission
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @return Object containing transaction permission data
	 */
	public Object[][] getTransactionPermissionData(ITestContext context) {
		Object[][] data = null;
		data = FileUtil.getSheetData(XLSX_FILE_PATH,
				prop.getProperty(Constants.CUSTOM_ROLE_TRANSACTION_PERMISSION_SHEET_NAME));
		return data;
	}

	@DataProvider
	/**
	 * This method is used to get data from excel sheets about report permission
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @return Object containing report permission data
	 */
	public Object[][] getReportPermissionData(ITestContext context) {
		Object[][] data = null;
		data = FileUtil.getSheetData(XLSX_FILE_PATH,
				prop.getProperty(Constants.CUSTOM_ROLE_REPORT_PERMISSION_SHEET_NAME));
		return data;
	}

	@DataProvider
	/**
	 * This method is used to get data from excel sheets about permission data list
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @return Object containing permission data list
	 */
	public Object[][] getListsPermissionData(ITestContext context) {
		Object[][] data = null;
		data = FileUtil.getSheetData(XLSX_FILE_PATH,
				prop.getProperty(Constants.CUSTOM_ROLE_LISTS_PERMISSION_SHEET_NAME));
		return data;
	}

	@DataProvider
	/**
	 * This method is used to get data from excel sheets about setup permission data list
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @return Object containing setup permission data 
	 */
	public Object[][] getSetupPermissionData(ITestContext context) {
		Object[][] data = null;
		data = FileUtil.getSheetData(XLSX_FILE_PATH,
				prop.getProperty(Constants.CUSTOM_ROLE_SETUP_PERMISSION_SHEET_NAME));
		return data;
	}

	@DataProvider
	/**
	 * This method is used to get data from excel sheets about role and environment data 
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @return Object containing role and environment data 
	 */
	public Object[][] getRoleAndEnvironmentNameData(ITestContext context) {
		Object[][] data = null;
		rowNumber = context.getCurrentXmlTest().getParameter("rowNumber");
		data = FileUtil.getSheetData(XLSX_FILE_PATH, prop.getProperty(Constants.CUSTOM_ROLE_ENVIRONMENT_SHEET_NAME));
		return data;
	}

	@DataProvider
	/**
	 * This method is used to get data from excel sheets about fieldset data 
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @return Object containing fieldset data 
	 */
	public Object[][] getFieldsSetFieldData(ITestContext context) {
		String prefix = context.getCurrentXmlTest().getParameter(Constants.PREFIX);
		Object[][] data = null;
		data = FileUtil.getSheetData(XLSX_FILE_PATH, prop.getProperty(prefix + Constants._SHEET_NAME));
		return data;
	}

	@DataProvider
	/**
	 * This method is used to get data from excel sheets about fieldset name and id data 
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @return Object containing fieldset name and id data 
	 */
	public Object[][] getFieldSetsNameAndIdData(ITestContext context) {
		Object[][] data = null;
		data = FileUtil.getSheetData(XLSX_FILE_PATH, prop.getProperty(Constants.FIELD_SETS_NAME_AND_ID_SHEET_NAME));
		return data;
	}

	@DataProvider
	/**
	 * This method is used to get data from excel sheets about credit card ppp data 
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @return Object containing credit card ppp data 
	 */
	public Object[][] getCreditCardPPPData(ITestContext context) {
		Object[][] data = null;
		data = FileUtil.getSheetData(XLSX_FILE_PATH, prop.getProperty(Constants.CREDIT_CARD_PPP_DATA_SHEET_NAME));
		return data;
	}

	@DataProvider
	/**
	 * This method is used to get data from excel sheets about secure acceptance payment processing profile data 
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @return Object containing secure acceptance ppp data 
	 */
	public Object[][] getSecureAcceptancePPPData(ITestContext context) {
		Object[][] data = null;
		String pppDataSheetPrefix = context.getCurrentXmlTest().getParameter(Constants.PPP_DATA_SHEET_PREFIX);
		data = FileUtil.getSheetData(XLSX_FILE_PATH, prop.getProperty(pppDataSheetPrefix + Constants._SHEET_NAME));
		return data;
	}

	@Test(dataProvider = "getRoleNameIdData")
	/**
	 * This method is used to create a custom role to an id
	 * @param RoleName String containing name of role
	 * @param RoleId String containing id of role
	 */
	public void createCustomRole(String RoleName, String RoleId) {
		logger.info("[NetSuiteCustomRoleConfiguration][createCustomRole] starts");
		driver = setupDriver(prop, Constants.CUSTOM_ROLE_URL);
		/*
		 * driver.manage().timeouts().implicitlyWait(10000, TimeUnit.SECONDS);
		 * commonUtil.clickElementByXPath(driver, prop, Constants.NEW_ROLE);
		 * commonUtil.findElementByXPath(driver, prop,
		 * Constants.ROLE_NAME).sendKeys(RoleName);
		 * commonUtil.findElementByXPath(driver, prop,
		 * Constants.ROLE_ID).sendKeys(RoleId);
		 */
		// commonUtil.clickElementByXPath(driver, prop, Constants.SAVE_BUTTON);
		commonUtil.clickElementByLinkText(driver, RoleName);
		commonUtil.clickElementByXPath(driver, prop, Constants.ROLE_EDIT);
		logger.info("[NetSuiteCustomRoleConfiguration][createCustomRole] ends");
	}

	@Test()
	/**
	 * This method is used to select transaction permission tab
	 */
	public void selectsetTransactionPermissionTab() {
		commonUtil.clickElementByXPath(driver, prop, Constants.PERMISSION_TRANSACTION_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
	}

	@Test(dataProvider = "getTransactionPermissionData")
	/**
	 * This method is used transaction permission for assigned fields and level
	 * @param PermissionField String containing Permission field
	 * @param PermissionLevel String containing permission level
	 */
	public void setTransactionPermission(String PermissionField, String PermissionLevel) {
		logger.info("[NetSuiteCustomRoleConfiguration][setTransactionPermission] starts");
		/*
		 * driver = setupDriver(prop, Constants.LOGIN_URL); commonUtil.sleep(3); driver
		 * =commonUtil.setupDriver(prop,Constants.CUSTOM_ROLE_URL);
		 * commonUtil.clickElementByXPath(driver, prop,
		 * Constants.PERMISSION_TRANSACTION_TAB);
		 * commonUtil.sleep(commonUtil.SLEEP_TIMER_1); commonUtil.sleep(5);
		 */
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTION_PERMISSION_FIELD);
		commonUtil.setElementValueByXPath(driver, prop, Constants.TRANSACTION_PERMISSION_FIELD, PermissionField);
		driver.findElement(By.cssSelector(".listinlinefocusedrowcell")).click();
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		driver.findElement(By.cssSelector(".listinlinefocusedrowcell")).click();
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		// commonUtil.clickElementByXPath(driver, prop,
		// Constants.TRANSACTION_PERMISSION_LEVEL_ID);
		commonUtil.setElementValueByXPath(driver, prop, Constants.TRANSACTION_PERMISSION_LEVEL_ID, PermissionLevel);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementByXPath(driver, prop, Constants.TRANSACTION_PERMISSION_ADD);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		logger.info("[NetSuiteCustomRoleConfiguration][setTransactionPermission] ends");
	}

	@Test()
	/**
	 * This method is used to select report permission tab
	 */
	public void selectReportPermissionTab() {
		commonUtil.clickElementByXPath(driver, prop, Constants.PERMISSION_REPORTS_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
	}

	@Test(dataProvider = "getReportPermissionData")
	/**
	 * This method is used set Report permission
	 * @param PermissionField String containing Permission field
	 * @param PermissionLevel String containing permission level
	 */
	public void setReportPermission(String PermissionField, String PermissionLevel) {
		logger.info("[NetSuiteCustomRoleConfiguration][setReportPermission] starts");
		commonUtil.setElementValueByXPath(driver, prop, Constants.REPORT_PERMISSION_FIELD, PermissionField);
		if (Constants.VIEW.equals(PermissionLevel)) {
			commonUtil.findElementByXPath(driver, prop, Constants.REPORT_PERMISSION_LEVEL_ID).sendKeys(PermissionLevel);
		}
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.REPORT_PERMISSION_ADD);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		logger.info("[NetSuiteCustomRoleConfiguration][setReportPermission] ends");
	}

	@Test()
	/**
	 * This method is used to select lists permission tab
	 */
	public void selectListsPermissionTab() {
		commonUtil.clickElementByXPath(driver, prop, Constants.PERMISSION_LISTS_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
	}

	@Test(dataProvider = "getListsPermissionData")
	/**
	 * This method is used set List permission
	 * @param PermissionField String containing Permission field
	 * @param PermissionLevel String containing permission level
	 */
	public void setListsPermission(String PermissionField, String PermissionLevel) {
		logger.info("[NetSuiteCustomRoleConfiguration][setListsPermission] starts");
		commonUtil.setElementValueByXPath(driver, prop, Constants.LISTS_PERMISSION_FIELD, PermissionField);
		String s = prop.getProperty(Constants.LISTS_FIELD) + "/tbody/tr[" + j + "]/td[2]/div";
		logger.info("Value of string: " + s);
		driver.findElement(By.xpath(s)).click();
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		driver.findElement(By.xpath(s)).click();
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		/*
		 * commonUtil.clickElementByCssSelector(driver, prop,
		 * prop.getProperty(Constants.LISTS_PERMISSION_LEVEL));
		 * commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		 * commonUtil.clickElementByCssSelector(driver, prop,
		 * prop.getProperty(Constants.LISTS_PERMISSION_LEVEL));
		 * commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		 */
		commonUtil.findElementByXPath(driver, prop, Constants.LISTS_PERMISSION_LEVEL_ID).sendKeys(PermissionLevel);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_5);
		commonUtil.clickElementByXPath(driver, prop, Constants.LISTS_PERMISSION_ADD);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		j = j + 1;
		logger.info("[NetSuiteCustomRoleConfiguration][setListsPermission] ends");
	}

	@Test()
	/**
	 * This method is used to select setup permission tab
	 */
	public void selectSetupPermissionTab() {
		commonUtil.clickElementByXPath(driver, prop, Constants.PERMISSION_SETUP_TAB);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
	}

	@Test(dataProvider = "getSetupPermissionData")
	/**
	 * This method is used set Setup permission
	 * @param PermissionField String containing Permission field
	 * @param PermissionLevel String containing permission level
	 */
	public void setSetupPermission(String PermissionField, String PermissionLevel) {
		logger.info("[NetSuiteCustomRoleConfiguration][setSetupPermission] starts");
		commonUtil.setElementValueByXPath(driver, prop, Constants.SETUP_PERMISSION_FIELD, PermissionField);
		String s1 = prop.getProperty(Constants.SETUP_FIELD) + "/tbody/tr[" + i + "]/td[2]/div";
		driver.findElement(By.xpath(s1)).click();
		logger.info("s" + s1);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		driver.findElement(By.xpath(s1)).click();
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		/*
		 * commonUtil.clickElementByCssSelector(driver, prop,
		 * Constants.SETUP_PERMISSION_LEVEL);
		 * commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		 * commonUtil.clickElementByCssSelector(driver, prop,
		 * Constants.SETUP_PERMISSION_LEVEL);
		 * commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		 */
		commonUtil.clickElementByXPath(driver, prop, Constants.SETUP_PERMISSION_LEVEL_ID);
		commonUtil.findElementByXPath(driver, prop, Constants.SETUP_PERMISSION_LEVEL_ID).sendKeys(PermissionLevel);
		commonUtil.clickElementByXPath(driver, prop, Constants.SETUP_PERMISSION_ADD);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		i = i + 1;
		logger.info("[NetSuiteCustomRoleConfiguration][setSetupPermission] ends");
	}

	@Test()
	/**
	 * This method is used to save custom role assigned to the user
	 */
	public void saveCustomRole() {
		commonUtil.clickElementByXPath(driver, prop, Constants.SAVE_BUTTON);
		WebElement userButton = commonUtil.findElementByXPath(driver, prop, Constants.USER_BUTTON);
		Actions builder = new Actions(driver);
		builder.moveToElement(userButton).perform();
		commonUtil.clickElementByLinkText(driver, Constants.LOG_OUT);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_3);
		driver.quit();
	}

	@Test(dataProvider = "getRoleAssigneeData")
	/**
	 * This method is used to assign custom role to an existing employee
	 * @param RoleName String containing name of role to be assigned
	 * @param EmployeeName String containing name of the employee
	 * @param EmployeeEmail String containing email of employee
	 */
	public void assignCustomRole(String RoleName, String EmployeeName, String EmployeeEmail) {
		driver = setupDriver(prop, Constants.EMPLOYEES_URL);
		commonUtil.clickEmployee(driver, EmployeeName, EmployeeEmail);
		commonUtil.clickElementByXPath(driver, prop, Constants.EMPLOYEE_ACCESS_TAB);
		commonUtil.setElementValueByXPath(driver, prop, Constants.EMPLOYEE_ROLE, RoleName);
		commonUtil.clickElementByXPath(driver, prop, Constants.EMPLOYEE_ROLE_ADD);
		commonUtil.clickElementByXPath(driver, prop, Constants.SAVE_BUTTON);
	}

	@Test(dataProvider = "getRoleAndEnvironmentNameData")
	/**
	 * This method is used to switch to new role for a user
	 * @param RoleName String containing name of role to be switched to
	 * @param EnvironmentName String containing name of environment used by the employee
	 */
	public void switchToCustomRole(String RoleName, String EnvironmentName) {
		WebElement userButton = commonUtil.findElementByXPath(driver, prop, Constants.USER_BUTTON);
		Actions builder = new Actions(driver);
		builder.moveToElement(userButton).perform();
		commonUtil.clickElementByLinkText(driver, Constants.VIEW_ALL_ROLES);
		commonUtil.clickRole(driver, RoleName, EnvironmentName);
	}

	@Test()
	/**
	 * This method is used to navigate to SCA field sets
	 */
	public void navigateToSCAFieldSets() {
		commonUtil.setupDriver(driver, prop, Constants.SETUP_WEBSITE_URL, userIndex);
		commonUtil.clickElementByXPath(driver, prop, Constants.SCA_EDIT);
		commonUtil.clickElementByXPath(driver, prop, Constants.WEBSITE_FIELD_SETS);
	}

	@Test()
	/**
	 * This method is used to navigate to SC field sets
	 */
	public void navigateToSCFieldSets() {
		commonUtil.setupDriver(driver, prop, Constants.SETUP_WEBSITE_URL, userIndex);
		commonUtil.clickElementByXPath(driver, prop, Constants.SC_EDIT);
		commonUtil.clickElementByXPath(driver, prop, Constants.WEBSITE_FIELD_SETS);
	}

	@Test(dataProvider = "getRoleAndEnvironmentNameData")
	/**
	 * This method is used to select field set
	 * @param RoleName String containing name of role to be switched to
	 * @param EnvironmentName String containing name of environment used by the employee
	 */
	public void clickFieldSethl(String RoleName, String EnvironmentName) {
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		driver.findElement(By.cssSelector("#fieldset_row_" + rowNumber + " > .listtexthl:nth-child(5)")).click();
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementById(driver, Constants.WEBSITE_FIELD_SETS_HELPER_OPTION);
		driver = switchToFieldSetWindow(driver);
	}

	@Test(dataProvider = "getRoleAndEnvironmentNameData")
	/**
	 * This method is used to select field set
	 * @param RoleName String containing name of role to be switched to
	 * @param EnvironmentName String containing name of environment used by the employee
	 */
	public void clickFieldSet(String RoleName, String EnvironmentName) {
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		driver.findElement(By.cssSelector("#fieldset_row_" + rowNumber + " > .listtext:nth-child(5)")).click();
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementById(driver, Constants.WEBSITE_FIELD_SETS_HELPER_OPTION);

		driver = switchToFieldSetWindow(driver);
	}

	@Test(dataProvider = "getFieldSetsNameAndIdData")
	/**
	 * This method is used to set value to name and id field
	 * @param FieldName String containing value to set to name
	 * @param FieldSetId String containing value to set to id
	 */
	public void setFieldSetsNameAndId(String FieldName, String FieldSetId) {
		commonUtil.setElementValueByXPath(driver, prop, Constants.FIELD_SET_NAME, FieldName);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementByCssSelector(driver, prop, Constants.FIELD_SET_ID_SELECTOR);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_1);
		commonUtil.clickElementByCssSelector(driver, prop, Constants.FIELD_SET_ID_SELECTOR);
		commonUtil.setElementValueByXPath(driver, prop, Constants.FIELD_SET_ID, FieldSetId);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.FIELDS_SET_ADD);
	}

	@Test()
	/**
	 * This method is used to save data in set fields
	 */
	public void saveFieldsSetData() {
		commonUtil.clickElementByXPath(driver, prop, Constants.SAVE_BUTTON);
	}

	@Test()
	/**
	 * This method is used to save field data
	 */
	public void saveFieldsData() {
		commonUtil.maximizeWindow(driver);
		commonUtil.clickElementByXPath(driver, prop, Constants.FIELDS_SET_SUBMIT);
		commonUtil.switchToWindow(driver, mainWindow);
		commonUtil.clickElementByXPath(driver, prop, Constants.FIELDS_SET_ADD);
	}

	@Test(dataProvider = "getFieldsSetFieldData")
	/**
	 * This method is used to set value to fields for fieldset
	 * @param FieldsForFieldSet String containing value for fieldset
	 */
	public void setFieldsSetField(String FieldsForFieldSet) {
		commonUtil.setElementValueByXPath(driver, prop, Constants.FIELDS_SET_FIELD_NAME, FieldsForFieldSet);
		commonUtil.sleep(commonUtil.SLEEP_TIMER_2);
		commonUtil.clickElementByXPath(driver, prop, Constants.FIELDS_ADD);
	}

	@Test(dataProvider = "getCreditCardPPPData")
	/**
	 * This method is used to create payment processing profile for creditcard
	 * @param ScriptId String containing script id
	 * @param ProfileName String containing name for PPP
	 * @param MerchantId String containing merchant id used for transaction
	 * @param SecurityKey Sting containing transaction secret key
	 */
	public void createCreditCardPPP(String ScriptId, String ProfileName, String MerchantId, String SecurityKey) {
		commonUtil.setupDriver(driver, prop, Constants.NEW_PAYMENT_PROCESSING_PROFILE_URL, userIndex);
		commonUtil.clickAddProfile(driver, ScriptId);
		commonUtil.setElementValueByXPath(driver, prop, Constants.PPP_NAME, ProfileName);
		commonUtil.setElementValueByXPath(driver, prop, Constants.PPP_MERCHANT_ID, MerchantId);
		commonUtil.clickElementByXPath(driver, prop, Constants.SAVE_BUTTON);
	}

	@Test(dataProvider = "getSecureAcceptancePPPData")
	/**
	 * This method is used to create payment processing profile for secure
	 * @param ScriptId String containing script id
	 * @param ProfileName String containing name for PPP
	 * @param MerchantId String containing merchant id used for transaction
	 * @param DeveloperID String containing the developer ID
	 * @param WebStoreUrl String containing Webstore url to do the transaction
	 * @param SuitletUrl String containing suitlet url
	 * @param ProfileId String containing profile id to do transaction
	 * @param AccessKey String containing Access key for transaction
	 * @param SecretKey String containing secret key for transaction
	 */
	public void createSecureAcceptancePPP(String ScriptId, String ProfileName, String MerchantId, String DeveloperID,
			String WebStoreUrl, String SuitletUrl, String ProfileId, String AccessKey, String SecretKey) {
		commonUtil.setupDriver(driver, prop, Constants.NEW_PAYMENT_PROCESSING_PROFILE_URL, userIndex);
		commonUtil.clickAddProfile(driver, ScriptId);
		commonUtil.setElementValueByXPath(driver, prop, Constants.PPP_NAME, ProfileName);
		commonUtil.setElementValueByXPath(driver, prop, Constants.PPP_PROFILE_ID, ProfileId);
		commonUtil.setElementValueByXPath(driver, prop, Constants.PPP_ACCESS_KEY, AccessKey);
		commonUtil.setElementValueByXPath(driver, prop, Constants.PPP_SECRET_KEY, SecretKey);
		commonUtil.setElementValueByXPath(driver, prop, Constants.PPP_SUITLET_URL, SuitletUrl);
		commonUtil.setElementValueByXPath(driver, prop, Constants.PPP_DEVELOPER_ID, DeveloperID);
		commonUtil.setElementValueByXPath(driver, prop, Constants.PPP_WEBSTORE_URL, WebStoreUrl);
		commonUtil.setElementValueByXPath(driver, prop, Constants.PPP_MERCHANT_ID, MerchantId);
		commonUtil.clickElementByXPath(driver, prop, Constants.SAVE_BUTTON);
	}

	@SuppressWarnings("deprecation")
	/**
	 * This method is used to setup webdriver with the given url
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param url the url with which the webdriver is to be setup with
	 * @return WebDriver element that is used for further operations
	 */
	public WebDriver setupDriver(Properties prop, String url) {
		if (null == driver) {
			System.setProperty(Constants.WEBDRIVER_CHROME_DRIVER, prop.getProperty(Constants.CHROMEDRIVER_PATH));
			ChromeOptions options = new ChromeOptions();
			driver = new ChromeDriver(options);
			commonUtil.maximizeWindow(driver);
			doLogin(prop, url);
		} else {
			driver.get(prop.getProperty(url));
			driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		}
		return driver;
	}

	@SuppressWarnings("deprecation")
	/**
	 * This method is used to do login operation to the netsuite portal
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param url String containing url for netsuite instance
	 */
	public void doLogin(Properties prop, String url) {
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get(prop.getProperty(url));
		commonUtil.setElementValueByXPath(driver, prop, Constants.LOGIN_USERNAME_ATTRIBUTE,
				prop.getProperty(Constants.LOGIN_USERNAME_ATTRIBUTE_VALUE));
		commonUtil.setElementValueByXPath(driver, prop, Constants.LOGIN_PASSWORD_ATTRIBUTE,
				prop.getProperty(Constants.LOGIN_PASSWORD_ATTRIBUTE_VALUE));
		commonUtil.findElementByXPath(driver, prop, Constants.LOGIN_SUBMIT_BUTTON).submit();
		commonUtil.sleep(35000);
	}

	/**
	 * This method is used to switch driver to fieldset window
	 * @param driver WebDriver element that is used for further operations
	 * @return WebDriver element after switching to fieldset window
	 */
	public WebDriver switchToFieldSetWindow(WebDriver driver) {
		mainWindow = driver.getWindowHandle();
		String childWindow = null;
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				commonUtil.switchToWindow(driver, childWindow);
				commonUtil.maximizeWindow(driver);
			}
		}
		return driver;
	}
}
