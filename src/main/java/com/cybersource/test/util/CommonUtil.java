package com.cybersource.test.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.Reporter;

import com.cybersource.test.CommonTest;

/**
 * Common Utility class which have all related functionality used to help Selenium related methods, it act as wrapper method for actual selenium default functions.
 * @author AD20243779
 *
 */
public class CommonUtil extends CommonTest {

	// time are in mill i seconds.
	public int SLEEP_TIMER_1 = 1000;
	public int SLEEP_TIMER_2 = 2000;
	public int SLEEP_TIMER_3 = 3000;
	public int SLEEP_TIMER_4 = 4000;
	public int SLEEP_TIMER_5 = 5000;
	public int SLEEP_TIMER_8 = 8000;
	public int SLEEP_TIMER_10 = 10000;
	public int SLEEP_TIMER_12 = 12000;
	public int SLEEP_TIMER_15 = 15000;
	public int SLEEP_TIMER_20 = 20000;
	public int SLEEP_TIMER_40 = 40000;
	ArrayList<String> tabs = null;
	public Writer fileWriter = null;
	public BufferedWriter bufferedWriter = null;
	public PrintWriter printWriter = null;
	JavascriptExecutor executor = null;
	String pnReferenceNumber = null;
	WebDriver driverInstance = null;
	static ArrayList<WebDriver> driverIndex = new ArrayList<>();
	final Logger logger = Logger.getLogger(CommonUtil.class);

	/**
	 * This method is used to define browser specific driver based on the user preference
	 * @param prop properties file which is configured for all xpath, user related data
	 * @return Webdriver object which will be used for further operations
	 */
	public WebDriver setupBrowserSpecificDriver(Properties prop) {
		logger.info("[setupBrowserSpecificDriver] Starts");
		String browser = null;
		WebDriver driver = null;
		browser = prop.getProperty("browser.type");
		browser = (null != browser) ? browser.trim() : Constants.CHROME;
		logger.info("[setupBrowserSpecificDriver] browser=" + browser);
		logger.info("[setupBrowserSpecificDriver] firefox browser=" + browser.equalsIgnoreCase(Constants.FIREFOX));
		logger.info("[setupBrowserSpecificDriver] chrome browser=" + browser.equalsIgnoreCase(Constants.CHROME));
		if (browser.equalsIgnoreCase(Constants.FIREFOX)) {
			logger.info("[setupBrowserSpecificDriver] FIREFOX browser=" + browser);
			System.setProperty(Constants.WEBDRIVER_CHROME_DRIVER, prop.getProperty(Constants.FIREFOXDRIVER_PATH));
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase(Constants.CHROME)) {
			logger.info("[setupBrowserSpecificDriver] CHROME browser=" + browser);
			System.setProperty(Constants.WEBDRIVER_CHROME_DRIVER, prop.getProperty(Constants.CHROMEDRIVER_PATH));
			driver = new ChromeDriver();
		}
		logger.info("[setupBrowserSpecificDriver] driver=" + driver);
		logger.info("[setupBrowserSpecificDriver] Ends");
		return driver;
	}

	/**
	 * This method is used to check value of a dropdown field defined by a xpath
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param xPath xPath of the dropdown field
	 */
	public void checkDropDownValueByXPath(WebDriver driver, Properties prop, String xPath) {
		logger.info("[checkDropDownValueByXPath] Starts");
		int j = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		String data = null;
		boolean flag = false;
		logger.info("[checkDropDownValueByXPath] xPath=" + xPath);
		for (int i = 0; i < j; i++) {
			logger.info("[checkDropDownValueByXPath] for loop iteration: " + (i + 1));
			sleep(SLEEP_TIMER_1);
			if (findElementsByXPath(driver, prop, xPath).size() != 0) {
				data = getElementTextByXPath(driver, prop, xPath);
				if (!data.equals(null)) {
					flag = true;
					break;
				}
			}
		}
		if (flag == true) {
			logger.info("[checkDropDownValueByXPath] Dropdown is not empty=" + data);
		} else {
			logger.info("[checkDropDownValueByXPath] Dropdown is empty=" + data);
		}
		logger.info("[checkDropDownValueByXPath] Ends");
	}

	/**
	 * This method is used to find an element using it's xpath
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param xPath xPath of the element to be found
	 * @return Webelement having the given xpath
	 */
	public WebElement findElementByXPath(WebDriver driver, Properties prop, String xPath) {
		logger.info("[findElementByXPath] Starts");
		String xPathInProp = null;
		WebElement element = null;
		logger.info("[findElementByXPath] xPath=" + xPath);
		if (null != xPath) {
			xPathInProp = prop.getProperty(xPath);
			logger.info("[findElementByXPath] xPathInProp=" + xPathInProp);
			if (null != xPathInProp) {
				try {
					logger.info("[findElementByXPath] By.xpath(xPathInProp)=" + By.xpath(xPathInProp));
					if (driver.findElements(By.xpath(xPathInProp)).size() != 0) {
						try {
							element = driver.findElement(By.xpath(xPathInProp));
						} catch (Exception e) {
							e.printStackTrace();
						}
						logger.info("[findElementByXPath] element.isDisplayed()=" + element.isDisplayed());
					}
				} catch (Throwable e) {
					logger.info("[findElementByXPath] No Element found for given xPathInProp:" + xPathInProp);
					e.printStackTrace();
				} finally {
					logger.info("[findElementByXPath] finally called");
				}
			} else {
				logger.info("[findElementByXPath] xPathInProp is Null:" + xPathInProp);
			}
		} else {
			logger.info("[findElementByXPath] xPath is Null");
		}
		logger.info("[findElementByXPath] Ends");
		return element;
	}

	/**
	 * This method is used to find an element using it's xpath prefix and suffix
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param xPathPrefix xPath prefix of the element to be found
	 * @param positionNumber position number of element between xpath prefix and suffix
	 * @param xPathSuffix xPath suffix of the element to be found
	 * @return Webelement having the given xpath
	 */
	public WebElement findElementByXPath(WebDriver driver, Properties prop, String xPathPrefix, int positionNumber,
			String xPathSuffix) {
		logger.info("[findElementByXPath] Starts");
		String xPathPrefixInProp = null;
		String xPathSuffixInProp = null;
		WebElement element = null;
		logger.info("[findElementByXPath] xPath=" + xPathPrefixInProp + positionNumber + xPathSuffixInProp);
		if (null != xPathPrefix && null != xPathSuffix) {
			xPathPrefixInProp = prop.getProperty(xPathPrefix);
			xPathSuffixInProp = prop.getProperty(xPathSuffix);
			logger.info("[findElementByXPath] xPathPrefixInProp=" + xPathPrefixInProp + ":: xPathSuffixInProp="
					+ xPathSuffixInProp);
			if (null != xPathPrefixInProp && null != xPathSuffixInProp) {
				try {
					logger.info("[findElementByXPath] By.xpath(xPathInProp)="
							+ By.xpath(xPathPrefixInProp + positionNumber + xPathSuffixInProp));
					if (driver.findElements(By.xpath(xPathPrefixInProp + positionNumber + xPathSuffixInProp))
							.size() != 0) {
						element = driver.findElement(By.xpath(xPathPrefixInProp + positionNumber + xPathSuffixInProp));
						logger.info("[findElementByXPath] element.isDisplayed()=" + element.isDisplayed());
					}
				} catch (Throwable e) {
					logger.info("[findElementByXPath] No Element found for given xPathInProp:" + xPathPrefixInProp
							+ positionNumber + xPathSuffixInProp);
					e.printStackTrace();
				} finally {
					logger.info("[findElementByXPath] finally called");
				}
			} else {
				logger.info("[findElementByXPath] xPathInProp is Null:" + xPathPrefixInProp + positionNumber
						+ xPathSuffixInProp);
			}
		} else {
			logger.info("[findElementByXPath] xPath is Null");
		}
		logger.info("[findElementByXPath] Ends");
		return element;
	}

	/**
	 * This method is used to find all possible instances of an xpath element
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param xPath xpath of the elements to be found
	 * @return Webelements having given xpath
	 */
	public List<WebElement> findElementsByXPath(WebDriver driver, Properties prop, String xPath) {
		logger.info("[findElementsByXPath] Starts");
		String xPathInProp = null;
		List<WebElement> elements = null;
		logger.info("[findElementsByXPath] xPath=" + xPath);
		if (null != xPath) {
			xPathInProp = prop.getProperty(xPath);
			logger.info("[findElementsByXPath] xPathInProp=" + xPathInProp);
			if (null != xPathInProp) {
				try {
					elements = driver.findElements(By.xpath(xPathInProp));
				} catch (Throwable e) {
					logger.info("[findElementsByXPath] No Element found for given xPathInProp:" + xPathInProp);
					e.printStackTrace();
				} finally {
					logger.info("[findElementsByXPath] finally called");
				}
			} else {
				logger.info("[findElementsByXPath] xPathInProp is Null:" + xPathInProp);
			}
		} else {
			logger.info("[findElementsByXPath] xPath is Null");
		}
		logger.info("[findElementsByXPath] Ends");
		return elements;
	}

	/**
	 * This method is used to fetch text inside an element having given xpath
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param xPath xPath of the element from which text is to be fetched
	 * @return String which is contained in the webElement having given xpath
	 */
	public String getElementTextByXPath(WebDriver driver, Properties prop, String xPath) {
		logger.info("[getElementTextByXPath] Starts");
		String elementText = null;
		WebElement element = null;
		logger.info("[getElementTextByXPath] xPath=" + xPath);
		if (null != xPath) {
			element = findElementByXPath(driver, prop, xPath);
			if (null != element) {
				elementText = element.getText();
				logger.info("[getElementTextByXPath] elementText: " + elementText);
			} else {
				logger.info("[getElementTextByXPath] element is Null");
			}
		} else {
			logger.info("[getElementTextByXPath] element is null for given xPath:" + xPath);
		}
		logger.info("[getElementTextByXPath] Ends");
		return elementText;
	}

	/**
	 * This method is used to set value to a webelement with given xpath
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param xPath xpath of the element which is to be assigned a value
	 * @param value The value which is to be assigned to the webElement
	 * @return WebElement to which value is assigned
	 */
	public WebElement setElementValueByXPath(WebDriver driver, Properties prop, String xPath, String value) {
		logger.info("[setElementValueByXPath] Starts");
		WebElement element = null;
		logger.info("[setElementValueByXPath] value=" + value + ", xPath=" + xPath);
		if (null != xPath) {
			element = findElementByXPath(driver, prop, xPath);
			if (null != element) {
				try {
					element.sendKeys(value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				logger.info("[setElementValueByXPath] Not able to set the value, element is Null for xPath=" + xPath);
			}
		} else {
			logger.info("[setElementValueByXPath] value is Null for given xPath:" + xPath);
		}
		logger.info("[setElementValueByXPath] Ends");
		return element;
	}

	/**
	 * This method is used to do single click on a webElement having given xpath
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param xPath xpath of the webElement that is to be clicked
	 */
	public void clickElementByXPath(WebDriver driver, Properties prop, String xPath) {
		logger.info("[clickElementByXPath] Starts");
		WebElement element = null;
		logger.info("[clickElementByXPath] xPath=" + xPath);
		if (null != xPath) {
			element = findElementByXPath(driver, prop, xPath);
			if (null != element) {
				logger.info("[clickElementByXPath] before click xPath=" + xPath);
				try {
					element.click();
				} catch (ElementClickInterceptedException e) {
					logger.info("[clickElementByXPath] Element click intercepted: " + e.getMessage());
					e.printStackTrace();
				}
				logger.info("[clickElementByXPath] after click xPath=" + xPath);
			} else {
				logger.info("[clickElementByXPath] Not able to set the element, value is Null for xPath=" + xPath);
			}
		} else {
			logger.info("[clickElementByXPath] xPath is Null for given xPath:" + xPath);
		}
		logger.info("[clickElementByXPath] Ends");
	}

	/**
	 * This method is used to single click a webElement more than one time
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param xPath xpath of the webElement to be clicked
	 * @param iterations Number of iterations to click the given webElement
	 */
	public void clickElementByXPath(WebDriver driver, Properties prop, String xPath, int iterations) {
		logger.info("[clickElementByXPath] Starts");
		WebElement element = null;
		for (int i = 0; i < iterations; i++) {
			logger.info("[clickElementByXPath] xPath=" + xPath);
			if (null != xPath) {
				element = findElementByXPath(driver, prop, xPath);
				if (null != element) {
					logger.info("[clickElementByXPath] before click xPath=" + xPath);
					element.click();
					logger.info("[clickElementByXPath] after click xPath=" + xPath);
				} else {
					logger.info("[clickElementByXPath] Not able to set the value element is Null for xPath=" + xPath);
				}
			} else {
				logger.info("[clickElementByXPath] xPath is Null for given xPath:" + xPath);
			}
		}
		logger.info("[clickElementByXPath] iteration: " + iterations);
		logger.info("[clickElementByXPath] Ends");
	}

	/**
	 * This method is used click a webelement fetched using xpath prefix and suffix
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param xPathPrefix prefix of xpath of the webelement to be clicked
	 * @param positionNumber position number of webelement between xpath prefix and suffix
	 * @param xPathSuffix suffix of the xpath of the webelement to be clicked
	 */
	public void clickElementByXPath(WebDriver driver, Properties prop, String xPathPrefix, int positionNumber,
			String xPathSuffix) {
		logger.info("[clickElementByXPath] Starts");
		WebElement element = null;
		logger.info("[clickElementByXPath] xPathPrefix=" + xPathPrefix + ", positionNumber=" + positionNumber
				+ ", xPathSuffix=" + xPathSuffix);
		if (null != (xPathPrefix + positionNumber + xPathSuffix)) {
			element = findElementByXPath(driver, prop, xPathPrefix, positionNumber, xPathSuffix);
			if (null != element) {
				logger.info("[clickElementByXPath] before click xPath=" + xPathPrefix + positionNumber + xPathSuffix);
				element.click();
				logger.info("[clickElementByXPath] after click xPath=" + xPathPrefix + positionNumber + xPathSuffix);
			} else {
				logger.info("[clickElementByXPath] Not able to set the value element is Null for xPath=" + xPathPrefix
						+ positionNumber + xPathSuffix);
			}
		}
		logger.info("[clickElementByXPath] Ends");
	}

	/**
	 * This method is used to switch window to check transactions in EBC window
	 * @param driver Webdriver object which will be used for operations
	 * @param PNReferenceNumber PN reference number of the transaction to be checked in EBC
	 */
	public void checkTransactionInEBC(WebDriver driver, String PNReferenceNumber) {
		logger.info("[checkTransactionInEBC] Starts");
		driver = setupDriver(driver, prop, null, Constants.EBC, null);
		switchToWindow(driver, tabs.get(0));
		logger.info("[checkTransactionInEBC] Ends");

	}

	/**
	 * This method is used to single click a webElement having a particular name
	 * @param driver Webdriver object which will be used for operations
	 * @param Name The name of the webElement to be clicked
	 */
	public void clickElementByName(WebDriver driver, String Name) {
		logger.info("[clickElementByName] Starts");
		logger.info("[clickElementByName] Name=" + Name);
		if (null != Name) {
			logger.info("[clickElementByName] before click Name=" + Name);
			driver.findElement(By.name(Name)).click();
			logger.info("[clickElementByXPath] after click Name=" + Name);
		} else {
			logger.info("[clickElementByName] Name is Null: " + Name);
		}
		logger.info("[clickElementByName] Ends");
	}

	/**
	 * This method is used to find a webElement having a particular CSS selector
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param cssPath The CSS selector path of the webElement to be be found
	 * @return WebElement having the given CSS selector
	 */
	public WebElement findElementByCssSelector(WebDriver driver, Properties prop, String cssPath) {
		logger.info("[findElementByCssSelector] Starts");
		String cssPathInProp = null;
		WebElement element = null;
		logger.info("[findElementByCssSelector] cssPath=" + cssPath);
		if (null != cssPath) {
			cssPathInProp = prop.getProperty(cssPath);
			logger.info("[findElementByCssSelector] cssPathInProp=" + cssPathInProp);
			if (null != cssPathInProp) {
				try {
					logger.info("[findElementByCssSelector] By.cssSelector(cssPathInProp)="
							+ By.cssSelector(cssPathInProp));
					if (Constants.ZERO != driver.findElements(By.cssSelector(cssPathInProp)).size()) {
						element = driver.findElement(By.cssSelector(cssPathInProp));
						logger.info("[findElementByCssSelector] element.isDisplayed()=" + element.isDisplayed());
					}
				} catch (Throwable e) {
					System.out.println(
							"[findElementByCssSelector] No Element found for given cssPathInProp:" + cssPathInProp);
					e.printStackTrace();
				} finally {
					logger.info("[findElementByCssSelector] finally called");
				}
			} else {
				logger.info("[findElementByCssSelector] cssPathInProp is Null:" + cssPathInProp);
			}
		} else {
			logger.info("[findElementByCssSelector] cssPath is Null");
		}
		logger.info("[findElementByCssSelector] Ends");
		return element;
	}

	/**
	 * THis method is used to single click a webElement having a given CSS selector
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param cssPath The CSS selector path of the webElement that is to be clicked
	 */
	public void clickElementByCssSelector(WebDriver driver, Properties prop, String cssPath) {
		logger.info("[clickElementByCssSelector] Starts");
		WebElement element = null;
		logger.info("[clickElementByCssSelector] cssPath=" + cssPath);
		if (null != cssPath) {
			element = findElementByCssSelector(driver, prop, cssPath);
			if (null != element) {
				logger.info("[clickElementByCssSelector] before click cssPath=" + cssPath);
				element.click();
				logger.info("[clickElementByCssSelector] after click cssPath=" + cssPath);
			} else {
				logger.info(
						"[clickElementByCssSelector] Not able to set the value element is Null for cssPath=" + cssPath);
			}
		} else {
			logger.info("[clickElementByCssSelector] cssPath is Null for given cssPath:" + cssPath);
		}
		logger.info("[clickElementByCssSelector] Ends");
	}

	/**
	 * This method is used to convert a String to lower cases and without special characters
	 * @param value The String value that is to be converted
	 * @return String in lowercase without special characters
	 */
	public String getLowerCaseWithoutSpecialChar(String value) {
		logger.info("[getLowerCaseWithoutSpecialChar] Starts");
		if (null != value) {
			value = value.replaceAll(Constants.COLON, Constants.EMPTY_STRING);
			value = value.trim().toLowerCase();
		}
		logger.info("[getLowerCaseWithoutSpecialChar] Ends");
		return value;
	}

	/**
	 * This method is used to check title of the active url page
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param value Expected title of the url page that is to be checked
	 */
	public void checkUrlTitle(WebDriver driver, Properties prop, String value) {
		logger.info("[checkUrlTitle] Starts");
		String UrlTitle = null;
		int j = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		for (int i = 0; i < j; i++) {
			UrlTitle = driver.getTitle();
			if (UrlTitle.contains(value)) {
				logger.info("[checkUrlTitle] url page title is not null: " + UrlTitle);
				break;
			} else {
				sleep(SLEEP_TIMER_1);
				driver.navigate().refresh();
			}

		}
		logger.info("[checkUrlTitle] Ends");
	}

	/**
	 * This method is used to Login to the Home page
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param userIndex Index number of user to specify the user trying to login
	 */
	public void doLogin(WebDriver driver, Properties prop, String userIndex) {
		logger.info("[doLogin] Starts");
		logger.info(
				"[doLogin] userIndex value: " + Constants.LOGIN_USERNAME_ATTRIBUTE_VALUE + Constants.DOT + userIndex);
		driver.get(prop.getProperty(Constants.LOGIN_URL));
		// waiting for login page to load
		checkUrlTitle(driver, prop, Constants.LOGIN_PAGE_TITLE);

		setElementValueByXPath(driver, prop, Constants.LOGIN_USERNAME_ATTRIBUTE,
				prop.getProperty(Constants.LOGIN_USERNAME_ATTRIBUTE_VALUE + Constants.DOT + userIndex));
		setElementValueByXPath(driver, prop, Constants.LOGIN_PASSWORD_ATTRIBUTE,
				prop.getProperty(Constants.LOGIN_PASSWORD_ATTRIBUTE_VALUE + Constants.DOT + userIndex));
		clickElementByXPath(driver, prop, Constants.LOGIN_SUBMIT_BUTTON);

		setElementValueByXPath(driver, prop, Constants.SECURITY_ANSWER_ATTRIBUTE,
				prop.getProperty(Constants.SECURITY_ANSWER + Constants.DOT + userIndex));
		clickElementByXPath(driver, prop, Constants.SECURITY_ANSWER_SUBMIT_BUTTON);
		logger.info("[doLogin] Ends");
	}

	/**
	 * This method is used to get the browser driver based on the userIndex
	 * @param userIndex Used to specify the user logging in to the browser
	 * @return WebDriver element that will be used for further operations
	 */
	public WebDriver getDriver(String userIndex) {
		logger.info("[getDriver] Starts");
		WebDriver driver = null;
		int userIndexInt = Integer.parseInt(userIndex);
		if (driverIndex != null) {
			if (driverIndex.size() >= userIndexInt) {
				driver = driverIndex.get(userIndexInt - 1);
				logger.info("[getDriver] driver is set: " + driverIndex.get(userIndexInt - 1));
			}
		}
		logger.info("[getDriver] Ends");
		return driver;
	}

	/**
	 * This method is used to setup webdriver with the given url
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param url the url with which the webdriver is to be setup with
	 * @param userIndex Used to specify user logged in to the browser
	 * @return WebDriver element that is used for further operations
	 */
	public WebDriver setupDriver(WebDriver driver, Properties prop, String url, String userIndex) {
		logger.info("[setupDriver] Starts");
		WebDriver driverValue = configureDriver(driver, prop, userIndex);
		logger.info("[setupDriver] prop url=" + url);
		logger.info("[setupDriver] driver=" + driverValue);
		driverValue.get(prop.getProperty(url));
		logger.info("[setupDriver] Ends");
		return driverValue;
	}

	/**
	 * This method is used to setup webdriver with direct url
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param url The url with which the webdriver is to be setted up with
	 * @param userIndex Used to specify user logged into the browser
	 * @return WebDriver Element that is used for further operations
	 */
	public WebDriver setupDriverDirectUrl(WebDriver driver, Properties prop, String url, String userIndex) {
		logger.info("[setupDriverDirectUrl] Starts");
		WebDriver driverValue = configureDriver(driver, prop, userIndex);
		logger.info("[setupDriverDirectUrl] url = " + url);
		driverValue.get(url);
		logger.info("[setupDriverDirectUrl] Ends");
		return driverValue;
	}

	/**
	 * This method is used to configure webDriver as per the user
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param userIndex Used to specify user logged into the browser
	 * @return Webdriver element that is used for further operations
	 */
	public WebDriver configureDriver(WebDriver driver, Properties prop, String userIndex) {
		logger.info("[configureDriver] Starts");
		String prefix = CommonTest.prefix;
		int userIndexInt = Integer.parseInt(userIndex);
		int browserMaxTimeoutSeconds = Integer.parseInt(prop.getProperty(Constants.BROWSER_MAX_TIMEOUT_SECONDS));
		logger.info("[configureDriver] driverIndex: " + driverIndex);
		// checks and sets if driver is already present for the user
		if (driverIndex != null) {
			if (driverIndex.size() >= userIndexInt) {
				driver = driverIndex.get(userIndexInt - 1);
				logger.info("[configureDriver] driver is set: " + driverIndex.get(userIndexInt - 1));
			}
		}
		logger.info("[configureDriver] driver: " + driver);
		// creates a new driver instance if driver is null
		if (null == driver) {
			System.setProperty("webdriver.chrome.driver", prop.getProperty(Constants.CHROMEDRIVER_PATH));
			ChromeOptions options = new ChromeOptions();
			if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.HEADLESS_MODE))) {
				logger.info("[configureDriver] prefix: " + prefix);
				if ((!prefix.contains(Constants.PREFIX_SCA)) || (!prefix.contains(Constants.PREFIX_SC))) {
					logger.info("[configureDriver] setting headless option");
					options.addArguments("--headless");
					options.addArguments("--disable-gpu");
					options.addArguments("--window-size=1366,768");
				}
			}
			options.addArguments("--remote-allow-origins=*");
			driver = new ChromeDriver(options);
			logger.info("[configureDriver] driver after setup=" + driver);
			maximizeWindow(driver);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(browserMaxTimeoutSeconds));
			doLogin(driver, prop, userIndex);
			// checks driver index and adds new driver instance
			if (driverIndex != null) {
				if (driverIndex.size() < userIndexInt) {
					driverIndex.add(driver);
					logger.info("[configureDriver] driver is added");
				}
			}
		}
		driverInstance = driver;
		logger.info("[configureDriver] Ends");
		return driver;
	}

	/**
	 * This method is used to Setup driver for future operations
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param url The url to which browser is to be configured
	 * @param domainName Domain name of the webdriver browser
	 * @param userIndex Used to specify user logged into the browser
	 * @return Webdriver element that is used for further operations
	 */
	public WebDriver setupDriver(WebDriver driver, Properties prop, String url, String domainName, String userIndex) {
		logger.info("[setupDriver] Starts");
		driver = configureDriver(driver, prop, url);
		executor = (JavascriptExecutor) driver;
		if (null == tabs) {
			executor.executeScript("window.open();");
			tabs = new ArrayList<String>(driver.getWindowHandles());
		}
		switchToWindow(driver, tabs.get(1));

		if (null != domainName && domainName.contains(Constants.EBC)) {
			doEBCLogin(driver, prop);
		}
		logger.info("[setupDriver] Ends");
		return driver;
	}

	/**
	 * This method is used to lof=gin to EBC portal
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 */
	public void doEBCLogin(WebDriver driver, Properties prop) {
		logger.info("[doEBCLogin] Starts");
		driver.get(prop.getProperty(Constants.EBC_LOGIN_URL));
		// WebDriverWait wait = new WebDriverWait(driver, Constants.THIRTY);
		// wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(Constants.ORG_ID)));
		setElementValueByXPath(driver, prop, Constants.EBC_ORGANIZATION_ID_ATTRIBUTE,
				prop.getProperty(Constants.EBC_ORGANIZATION_ID_ATTRIBUTE_VALUE));
		setElementValueByXPath(driver, prop, Constants.EBC_USERNAME_ATTRIBUTE,
				prop.getProperty(Constants.EBC_USERNAME_ATTRIBUTE_VALUE));
		setElementValueByXPath(driver, prop, Constants.EBC_PASSWORD_ATTRIBUTE,
				prop.getProperty(Constants.EBC_PASSWORD_ATTRIBUTE_VALUE));
		findElementByCssSelector(driver, prop, Constants.EBC_LOGIN_SUBMIT_BUTTON);
		logger.info("[doEBCLogin] Ends");
	}

	/**
	 * This method is used to find an element in browser using link text
	 * @param driver Webdriver object which will be used for operations
	 * @param string The link text used to fetch the element
	 * @return WebElement having the given link text
	 */
	public WebElement findElementByLinkText(WebDriver driver, String string) {
		logger.info("[findElementByLinkText] Starts");
		WebElement element = null;
		if (null != string) {
			logger.info("[findElementByLinkText] Value is not null=" + string);
			element = driver.findElement(By.linkText(string));
		} else {
			logger.info("[findElementByLinkText] Value is null=" + string);
		}
		logger.info("[findElementByLinkText] Ends");
		return element;
	}

	/**
	 * This method is used to single click an element having a given link text
	 * @param driver Webdriver object which will be used for operations
	 * @param LinkText The link text used to fetch the webelement 
	 * @return WebElement having the given link text
	 */
	public WebElement clickElementByLinkText(WebDriver driver, String LinkText) {
		logger.info("[clickElementByLinkText] Starts");
		WebElement element = null;
		if (null != LinkText) {
			logger.info("[clickElementByLinkText] before click LinkText=" + LinkText);
			element = findElementByLinkText(driver, LinkText);
			element.click();
			logger.info("[clickElementByLinkText] after click LinkText=" + LinkText);
		} else {
			logger.info("[clickElementByLinkText] LinkText is Null :" + LinkText);
		}
		logger.info("[clickElementByLinkText] Ends");
		return element;
	}

	/**
	 * This function is used to pause the run for specified time duration
	 * @param time Time (in milliseconds) to pause the test run
	 */
	@SuppressWarnings("static-access")
	public void sleep(int time) {
		logger.info("[sleep] Starts");
		if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.VIRTUAL_MACHINE_RUN))) {
			time = SLEEP_TIMER_2;
		}
		logger.info("[sleep] for " + time + " ms");
		try {
			Thread.currentThread().sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("[sleep] Ends");
	}

	/**
	 * This method is used to single click a link using xpath
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param string xpath of the link to be clicked
	 */
	public void clickLinkByXPath(WebDriver driver, Properties prop, String string) {
		logger.info("[clickLinkByXpath] Starts");
		if (string != null) {
			logger.info("[clickLinkByXpath] Value is not null=" + string);
			WebElement element = null;
			executor = (JavascriptExecutor) driver;
			for (int i = 0; i < 2; i++) {
				element = findElementByXPath(driver, prop, string);
				executor.executeScript("arguments[0].click();", element);
			}
		} else {
			logger.info("[clickLinkByXpath] Value is null=" + string);
		}
		logger.info("[clickLinkByXpath] Ends");
	}

	/**
	 * This method is used to validate details of Test scenario, Decision Message, Reason code
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param reasonCodeXpath The xpath of the element containing Reason code of current transaction
	 * @param pnReferenceNumberXpath The xpath of element containing PN reference number of the current transaction
	 * @param ReasonCode The string that defines Reason code of current transaction
	 * @param DecisionMsg The string that defines the decision message
	 * @param TestScenario The string that defines the current test scenario
	 * @return String that contains PN reference number of the transaction
	 */
	public String checkActuals(WebDriver driver, String PaymentEvent, String decisionXpath, String reasonCodeXpath,
			String pnReferenceNumberXpath, String ReasonCode, String DecisionMsg, String TestScenario) {
		logger.info("[checkActuals] Starts");
		logger.info("[checkActuals] from excel TestScenario=" + TestScenario);
		logger.info("[checkActuals] from excel DecisionMsg=" + DecisionMsg);
		logger.info("[checkActuals] from excel ReasonCode=" + ReasonCode);

		String response[] = getResponseFieldValue(driver, PaymentEvent, decisionXpath, reasonCodeXpath,
				pnReferenceNumberXpath);
		if ((response[0].contains(Constants.REST_STATUS_VALUE))) {
			if ((Constants.AUTHORIZATION.equalsIgnoreCase(PaymentEvent))
					|| (Constants.STRING_SALE.equalsIgnoreCase(PaymentEvent))) {
				logger.info("[checkActuals] web status=" + response[0]);
				logger.info("[checkActuals] xls status=" + ReasonCode);
				Assert.assertEquals(response[0], ReasonCode);
			} else {
				logger.info("[checkActuals] web status=" + response[0]);
				logger.info("[checkActuals] xls status=" + DecisionMsg);
				Assert.assertEquals(response[0], DecisionMsg);
			}
		} else {
			logger.info("[checkActuals] web decision=" + response[0]);
			logger.info("[checkActuals] xls DecisionMsg=" + DecisionMsg);
			Assert.assertEquals(response[0], DecisionMsg);

			logger.info("[checkActuals] web reasonCode=" + response[1]);
			logger.info("[checkActuals] xls ReasonCode=" + ReasonCode);
			Assert.assertEquals(response[1], ReasonCode);
		}
		logger.info("[checkActuals] Ends");
		return response[2];
	}

	/**
	 * This method is used to validate details of Test scenario, Decision Message, Reason code for Merchant Initiated transactions
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param reasonCodeXpath The xpath of the element containing Reason code of current transaction
	 * @param pnReferenceNumberXpath The xpath of element containing PN reference number of the current transaction
	 * @param ReasonCode The string that defines Reason code of current transaction
	 * @param DecisionMsg The string that defines the decision message
	 * @param TestScenario The string that defines the current test scenario
	 * @return String that contains PN reference number of the transaction
	 */
	public String checkMITActuals(WebDriver driver, String PaymentEvent, String PaymentType, String decisionXpath,
			String reasonCodeXpath, String pnReferenceNumberXpath, String ReasonCode, String DecisionMsg,
			String TestScenario) {
		logger.info("[checkActuals] Starts");
		logger.info("[checkActuals] from excel TestScenario=" + TestScenario);
		logger.info("[checkActuals] from excel DecisionMsg=" + DecisionMsg);
		logger.info("[checkActuals] from excel ReasonCode=" + ReasonCode);

		String response[] = getMITResponseFieldValue(driver, PaymentEvent, PaymentType, decisionXpath, reasonCodeXpath,
				pnReferenceNumberXpath);
		logger.info("[checkActuals] web decision=" + response[0]);
		logger.info("[checkActuals] xls DecisionMsg=" + DecisionMsg);
		Assert.assertEquals(response[0], DecisionMsg);

		logger.info("[checkActuals] web reasonCode=" + response[1]);
		logger.info("[checkActuals] xls ReasonCode=" + ReasonCode);
		Assert.assertEquals(response[1], ReasonCode);
		logger.info("[checkActuals] Ends");
		return response[2];
	}

	/**
	 * This method is used to validate details of Test scenario, Decision Message, Reason code for Level 2,3 data of processor
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param ProcessorName The string that defines Name of the current Processor
	 * @param ProcessorLevel The string that defines Processor level of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param reasonCodeXpath The xpath of the element containing Reason code of current transaction
	 * @param pnReferenceNumberXpath The xpath of element containing PN reference number of the current transaction
	 * @param ReasonCode The string that defines Reason code of current transaction
	 * @param DecisionMsg The string that defines the decision message
	 * @param TestScenario The string that defines the current test scenario
	 * @return String that contains PN reference number of the transaction
	 */
	public String checkActuals(WebDriver driver, String PaymentEvent, String ProcessorName, String ProcessorLevel,
			String decisionXpath, String reasonCodeXpath, String pnReferenceNumberXpath, String ReasonCode,
			String DecisionMsg, String TestScenario) {
		logger.info("[checkActuals] Starts");
		logger.info("[checkActuals] from excel TestScenario=" + TestScenario);
		logger.info("[checkActuals] from excel DecisionMsg=" + DecisionMsg);
		logger.info("[checkActuals] from excel ReasonCode=" + ReasonCode);

		String response[] = getResponseFieldValue(driver, PaymentEvent, ProcessorName, ProcessorLevel, decisionXpath,
				reasonCodeXpath, pnReferenceNumberXpath);
		logger.info("[checkActuals] web decision=" + response[0]);
		logger.info("[checkActuals] xls DecisionMsg=" + DecisionMsg);
		Assert.assertEquals(response[0], DecisionMsg);

		logger.info("[checkActuals] web reasonCode=" + response[1]);
		logger.info("[checkActuals] xls ReasonCode=" + ReasonCode);
		Assert.assertEquals(response[1], ReasonCode);
		logger.info("[checkActuals] Ends");
		return response[2];
	}

	/**
	 * This method is used to validate details of Test scenario, Decision Message, Reason code for Token service
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param ProcessorName The string that defines Name of the current Processor
	 * @param ProcessorLevel The string that defines Processor level of current transaction
	 * @param TokenService The string that defines Token service status of transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param reasonCodeXpath The xpath of the element containing Reason code of current transaction
	 * @param pnReferenceNumberXpath The xpath of element containing PN reference number of the current transaction
	 * @param ReasonCode The string that defines Reason code of current transaction
	 * @param DecisionMsg The string that defines the decision message
	 * @param TestScenario The string that defines the current test scenario
	 * @return String array that contains details of the transaction including PN reference number
	 */
	public String[] checkActuals(WebDriver driver, String PaymentEvent, String ProcessorName, String ProcessorLevel,
			String TokenService, String decisionXpath, String reasonCodeXpath, String pnReferenceNumberXpath,
			String ReasonCode, String DecisionMsg, String TestScenario) {
		logger.info("[checkActuals] Starts");
		logger.info("[checkActuals] from excel TestScenario=" + TestScenario);
		logger.info("[checkActuals] from excel DecisionMsg=" + DecisionMsg);
		logger.info("[checkActuals] from excel ReasonCode=" + ReasonCode);

		String response[] = getResponseFieldValues(driver, PaymentEvent, ProcessorName, ProcessorLevel, TokenService,
				decisionXpath, reasonCodeXpath, pnReferenceNumberXpath);
		logger.info("[checkActuals] web decision=" + response[0]);
		logger.info("[checkActuals] xls DecisionMsg=" + DecisionMsg);
		Assert.assertEquals(response[0], DecisionMsg);

		logger.info("[checkActuals] web reasonCode=" + response[1]);
		logger.info("[checkActuals] xls ReasonCode=" + ReasonCode);
		Assert.assertEquals(response[1], ReasonCode);
		logger.info("[checkActuals] Ends");
		return response;
	}

	/**
	 * This method is used to validate details of Test scenario, Decision Message, Reason code
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param TokenService The string that defines Token service status of transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param reasonCodeXpath The xpath of the element containing Reason code of current transaction
	 * @param pnReferenceNumberXpath The xpath of element containing PN reference number of the current transaction
	 * @param ReasonCode The string that defines Reason code of current transaction
	 * @param DecisionMsg The string that defines the decision message
	 * @param TestScenario The string that defines the current test scenario
	 * @return String array that contains details of the transaction including PN reference number
	 */
	public String[] checkActuals(WebDriver driver, String PaymentEvent, String TokenService, String decisionXpath,
			String reasonCodeXpath, String pnReferenceNumberXpath, String ReasonCode, String DecisionMsg,
			String TestScenario) {
		logger.info("[checkActuals] Starts");
		logger.info("[checkActuals] from excel TestScenario=" + TestScenario);
		logger.info("[checkActuals] from excel DecisionMsg=" + DecisionMsg);
		logger.info("[checkActuals] from excel ReasonCode=" + ReasonCode);

		String response[] = getResponseFieldValue(driver, PaymentEvent, TokenService, decisionXpath, reasonCodeXpath,
				pnReferenceNumberXpath);
		logger.info("[checkActuals] web decision=" + response[0]);
		logger.info("[checkActuals] xls DecisionMsg=" + DecisionMsg);
		Assert.assertEquals(response[0], DecisionMsg);

		logger.info("[checkActuals] web reasonCode=" + response[1]);
		logger.info("[checkActuals] xls ReasonCode=" + ReasonCode);
		Assert.assertEquals(response[1], ReasonCode);
		logger.info("[checkActuals] Ends");
		return response;
	}

	/**
	 * This method is used to fetch values of Response field
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param reasonCodeXpath The xpath of the element containing Reason code of current transaction
	 * @param pnReferenceNumberXpath The xpath of element containing PN reference number of the current transaction
	 * @return String array that contains Response field values
	 */
	private String[] getResponseFieldValue(WebDriver driver, String PaymentEvent, String decisionXpath,
			String reasonCodeXpath, String pnReferenceNumberXpath) {
		logger.info("[getResponseFieldValue] Starts");
		clickPaymentEvent(driver, PaymentEvent);
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		String fieldValue[] = new String[3];
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		executor = (JavascriptExecutor) driver;
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				logger.info("[getResponseFieldValue] Switched to child window");
				switchToWindow(driver, childWindow);
				clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
				WebElement element = findElementByXPath(driver, prop, Constants.RAW_RESPONSE_TEXT_FIELD);
				String[] lines = element.getText().trim().split("\\R");
				for (int i = 0; i < lines.length; i++) {
					if ((lines[i].contains(prop.getProperty(Constants.SOAP_DECISION_MESSAGE)))
							|| (lines[i].contains(prop.getProperty(Constants.REST_STATUS_VALUE)))) {
						if (fieldValue[0] == null) {
							fieldValue[0] = lines[i].trim();
						}
					}
					if ((lines[i].contains(prop.getProperty(Constants.SOAP_REASON_CODE)))) {
						if (fieldValue[1] == null) {
							fieldValue[1] = lines[i].trim();
						}
					}
				}
				fieldValue[2] = driver.findElement(By.xpath(pnReferenceNumberXpath)).getText();
				closeCurrentWindow(driver);
				switchToWindow(driver, mainWindow);
			}
		}
		logger.info("[getResponseFieldValue] Ends");
		return fieldValue;
	}

	/**
	 * This method is used to fetch values of Response field for Merchant initiated transactions
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param PaymentType The string that defines payment type of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param reasonCodeXpath The xpath of the element containing Reason code of current transaction
	 * @param pnReferenceNumberXpath The xpath of element containing PN reference number of the current transaction
	 * @return String array that contains Response field values
	 */
	private String[] getMITResponseFieldValue(WebDriver driver, String PaymentEvent, String PaymentType,
			String decisionXpath, String reasonCodeXpath, String pnReferenceNumberXpath) {
		logger.info("[getMITResponseFieldValue] Starts");
		clickPaymentEvent(driver, PaymentEvent);
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		List<WebElement> request = null;
		String[] requestLines = null;
		String fieldValue[] = new String[3];
		String subsequentReason = null;
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		executor = (JavascriptExecutor) driver;
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				logger.info("[getMITResponseFieldValue] Switched to child window");
				switchToWindow(driver, childWindow);
				// Checking MIT Fields
				if (Constants.AUTHORIZATION.equalsIgnoreCase(PaymentEvent)) {
					request = driver.findElements(By.xpath(prop.getProperty(Constants.SOAP_AUTH_REQUEST_XPATH)));
					requestLines = request.get(1).getText().trim().split("\\R");
				} else if (Constants.STRING_SALE.equalsIgnoreCase(PaymentEvent)) {
					request = driver.findElements(By.xpath(prop.getProperty(Constants.SOAP_SALE_REQUEST_XPATH)));
					requestLines = request.get(0).getText().trim().split("\\R");
				}
				logger.info("[getMITResponseFieldValue] Request Lines Length: " + requestLines.length);
				if (!Constants.MOTO.equalsIgnoreCase(PaymentType)) {
					Reporter.log("MIT Fields:");
				}
				for (int i = 0; i < requestLines.length; i++) {
					if (requestLines[i].contains(prop.getProperty(Constants.SOAP_MIT_PARSER_SUBSEQUENT_AUTH))) {
						logger.info("[getMITResponseFieldValue] Expected: "
								+ prop.getProperty(Constants.SOAP_MIT_SUBSEQUENT_AUTH));
						logger.info("[getMITResponseFieldValue] Actual: " + requestLines[i].trim());
						Assert.assertEquals(requestLines[i].trim(),
								prop.getProperty(Constants.SOAP_MIT_SUBSEQUENT_AUTH));
						Reporter.log(requestLines[i].trim());
					}
					if (!Constants.PAYMENT_TYPE_UNSCHEDULED.equalsIgnoreCase(PaymentType)) {
						if (requestLines[i]
								.contains(prop.getProperty(Constants.SOAP_MIT_PARSER_SUBSEQUENT_AUTH_REASON))) {
							if (Constants.PAYMENT_TYPE_RESUBMISSION.equalsIgnoreCase(PaymentType)) {
								subsequentReason = prop.getProperty(Constants.SOAP_MIT_SUBSEQUENT_AUTH_REASON_ONE);
							} else if (Constants.PAYMENT_TYPE_DELAYED_CHARGE.equalsIgnoreCase(PaymentType)) {
								subsequentReason = prop.getProperty(Constants.SOAP_MIT_SUBSEQUENT_AUTH_REASON_TWO);
							} else if (Constants.PAYMENT_TYPE_REAUTHORIZATION.equalsIgnoreCase(PaymentType)) {
								subsequentReason = prop.getProperty(Constants.SOAP_MIT_SUBSEQUENT_AUTH_REASON_THREE);
							} else if (Constants.PAYMENT_TYPE_NO_SHOW_CHARGE.equalsIgnoreCase(PaymentType)) {
								subsequentReason = prop.getProperty(Constants.SOAP_MIT_SUBSEQUENT_AUTH_REASON_FOUR);
							}
							logger.info("[getMITResponseFieldValue] Expected: " + subsequentReason);
							logger.info("[getMITResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(), subsequentReason);
							Reporter.log(requestLines[i].trim());
						}
					}

					if (requestLines[i]
							.contains(prop.getProperty(Constants.SOAP_MIT_PARSER_SUBSEQUENT_AUTH_STORED_CREDENTIAL))) {
						logger.info("[getMITResponseFieldValue] Expected: "
								+ prop.getProperty(Constants.SOAP_MIT_SUBSEQUENT_AUTH_STORED_CREDENTIAL));
						logger.info("[getMITResponseFieldValue] Actual: " + requestLines[i].trim());
						Assert.assertEquals(requestLines[i].trim(),
								prop.getProperty(Constants.SOAP_MIT_SUBSEQUENT_AUTH_STORED_CREDENTIAL));
						Reporter.log(requestLines[i].trim());
					}
					if (requestLines[i]
							.contains(prop.getProperty(Constants.SOAP_MIT_PARSER_SUBSEQUENT_AUTH_TRANSACTION_ID))) {
						logger.info(
								"[getMITResponseFieldValue] Transaction ID field  value: " + requestLines[i].trim());
						Reporter.log(requestLines[i].trim());
					}
					if (Constants.PAYMENT_TYPE_UNSCHEDULED.equalsIgnoreCase(PaymentType)) {
						if (requestLines[i].contains(prop.getProperty(Constants.SOAP_MIT_PARSER_COMMERCE_INDICATOR))) {
							logger.info("[getMITResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.SOAP_MIT_COMMERCE_INDICATOR));
							logger.info("[getMITResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.SOAP_MIT_COMMERCE_INDICATOR));
							Reporter.log(requestLines[i].trim());
						}
					}
				}
				clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
				fieldValue[0] = driver.findElement(By.xpath(decisionXpath)).getText();
				fieldValue[1] = driver.findElement(By.xpath(reasonCodeXpath)).getText();
				logger.info("[getMITResponseFieldValue] pnref field xpath: " + pnReferenceNumberXpath);
				fieldValue[2] = driver.findElement(By.xpath(pnReferenceNumberXpath)).getText();
				closeCurrentWindow(driver);
				switchToWindow(driver, mainWindow);
			}
		}
		logger.info("[getMITResponseFieldValue] Ends");
		return fieldValue;

	}

	/**
	 * This method is used to fetch values of Response field for Level 2,3 Processor level
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param ProcessorName The string that defines Processor Name of current transaction
	 * @param ProcessorLevel The string that defines Processor level of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param reasonCodeXpath The xpath of the element containing Reason code of current transaction
	 * @param pnReferenceNumberXpath The xpath of element containing PN reference number of the current transaction
	 * @return String array that contains Response field values
	 */
	private String[] getResponseFieldValue(WebDriver driver, String PaymentEvent, String ProcessorName,
			String ProcessorLevel, String decisionXpath, String reasonCodeXpath, String pnReferenceNumberXpath) {
		logger.info("[getResponseFieldValue] Starts");
		clickPaymentEvent(driver, PaymentEvent);
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		String fieldValue[] = new String[3];
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		executor = (JavascriptExecutor) driver;
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				logger.info("[getResponseFieldValue] Switched to child window");
				switchToWindow(driver, childWindow);
				clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
				WebElement element = findElementByXPath(driver, prop, Constants.RAW_RESPONSE_TEXT_FIELD);
				String[] lines = element.getText().trim().split("\\R");
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].contains(prop.getProperty(Constants.SOAP_DECISION_MESSAGE))) {
						if (fieldValue[0] == null) {
							fieldValue[0] = lines[i].trim();
						}
					}
					if (lines[i].contains(prop.getProperty(Constants.SOAP_REASON_CODE))) {
						if (fieldValue[1] == null) {
							fieldValue[1] = lines[i].trim();
						}
					}
				}
				fieldValue[2] = driver.findElement(By.xpath(pnReferenceNumberXpath)).getText();
				closeCurrentWindow(driver);
				switchToWindow(driver, mainWindow);
			}
		}
		logger.info("[getResponseFieldValue] Ends");
		return fieldValue;
	}

	/**
	 * This method is used to fetch values of Response field for Token services
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param ProcessorName The string that defines Processor Name of current transaction
	 * @param ProcessorLevel The string that defines Processor level of current transaction
	 * @param TokenService The string that defines Token service details of the transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param reasonCodeXpath The xpath of the element containing Reason code of current transaction
	 * @param pnReferenceNumberXpath The xpath of element containing PN reference number of the current transaction
	 * @return String array that contains Response field values
	 */
	private String[] getResponseFieldValues(WebDriver driver, String PaymentEvent, String ProcessorName,
			String ProcessorLevel, String TokenService, String decisionXpath, String reasonCodeXpath,
			String pnReferenceNumberXpath) {
		logger.info("[getResponseFieldValues] Starts");
		clickPaymentEvent(driver, PaymentEvent);
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		String TokenValue;
		String[] TokenValues;
		String fieldValue[] = new String[4];
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		executor = (JavascriptExecutor) driver;
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				switchToWindow(driver, childWindow);
				WebElement requestElement = findElementByXPath(driver, prop, Constants.RAW_REQUEST_TEXT_FIELD);
				String[] requestLines = requestElement.getText().trim().split("\\R");
				if (Constants.YES.equalsIgnoreCase(TokenService)) {
					for (int i = 0; i < requestLines.length; i++) {
						if (requestLines[i].contains(prop.getProperty(Constants.SOAP_TOKEN_SUBSCRIPTION_ID))) {
							if (fieldValue[3] == null) {
								TokenValue = requestLines[i].trim();
								TokenValues = TokenValue.split(" ");
								fieldValue[3] = TokenValues[1];
								logger.info("[getResponseFieldValues] field3: " + fieldValue[3]);
								break;
							}
						}
					}
				}
				// checking level 2,3 fields
				if ((Constants.PROCESSOR_LEVEL_2.equalsIgnoreCase(ProcessorLevel))
						|| (Constants.PROCESSOR_LEVEL_3.equalsIgnoreCase(ProcessorLevel))) {
					checkDataLevelFields(driver, prop, PaymentEvent, ProcessorName, ProcessorLevel, requestLines);
				}
				clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
				WebElement responseElement = findElementByXPath(driver, prop, Constants.RAW_RESPONSE_TEXT_FIELD);
				String[] responseLines = responseElement.getText().trim().split("\\R");
				for (int i = 0; i < responseLines.length; i++) {
					if (responseLines[i].contains(prop.getProperty(Constants.SOAP_DECISION_MESSAGE))) {
						if (fieldValue[0] == null) {
							fieldValue[0] = responseLines[i].trim();
						}
					}
					if (responseLines[i].contains(prop.getProperty(Constants.SOAP_REASON_CODE))) {
						if (fieldValue[1] == null) {
							fieldValue[1] = responseLines[i].trim();
						}
					}
					if (Constants.YES.equalsIgnoreCase(TokenService)) {
						if (responseLines[i].contains(prop.getProperty(Constants.SOAP_TOKEN_SUBSCRIPTION_ID))) {
							if (fieldValue[3] == null) {
								TokenValue = responseLines[i].trim();
								TokenValues = TokenValue.split(" ");
								fieldValue[3] = TokenValues[1];
								logger.info("[getResponseFieldValues] field3: " + fieldValue[3]);
							}
						}
					}
				}
				fieldValue[2] = driver.findElement(By.xpath(pnReferenceNumberXpath)).getText();
				closeCurrentWindow(driver);
				switchToWindow(driver, mainWindow);
			}
		}
		logger.info("[getResponseFieldValues] Ends");
		return fieldValue;
	}

	/**
	 * This method is used to fetch values of Response field
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param TokenService The string that defines Token service details of the transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param reasonCodeXpath The xpath of the element containing Reason code of current transaction
	 * @param pnReferenceNumberXpath The xpath of element containing PN reference number of the current transaction
	 * @return String array that contains Response field values
	 */
	private String[] getResponseFieldValue(WebDriver driver, String PaymentEvent, String TokenService,
			String decisionXpath, String reasonCodeXpath, String pnReferenceNumberXpath) {
		logger.info("[getResponseFieldValue] Starts");
		clickPaymentEvent(driver, PaymentEvent);
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		String TokenValue = null;
		String[] TokenValues;
		String fieldValue[] = new String[4];
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		executor = (JavascriptExecutor) driver;
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				switchToWindow(driver, childWindow);
				WebElement requestElement = findElementByXPath(driver, prop, Constants.RAW_REQUEST_TEXT_FIELD);
				String[] requestLines = requestElement.getText().trim().split("\\R");
				if (Constants.YES.equalsIgnoreCase(TokenService)) {
					if (fieldValue[3] == null) {
						for (int i = 0; i < requestLines.length; i++) {
							if (requestLines[i].contains(prop.getProperty(Constants.SOAP_TOKEN_SUBSCRIPTION_ID))) {
								TokenValue = requestLines[i].trim();
								TokenValues = TokenValue.split(" ");
								fieldValue[3] = TokenValues[1];
								logger.info("[getResponseFieldValue] field3: " + fieldValue[3]);
								break;
							}
						}
					}
				}
				clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
				WebElement responseElement = findElementByXPath(driver, prop, Constants.RAW_RESPONSE_TEXT_FIELD);
				String[] responseLines = responseElement.getText().trim().split("\\R");
				for (int i = 0; i < responseLines.length; i++) {
					if (responseLines[i].contains(prop.getProperty(Constants.SOAP_DECISION_MESSAGE))) {
						if (fieldValue[0] == null) {
							fieldValue[0] = responseLines[i].trim();
						}
					}
					if (responseLines[i].contains(prop.getProperty(Constants.SOAP_REASON_CODE))) {
						if (fieldValue[1] == null) {
							fieldValue[1] = responseLines[i].trim();
						}
					}
					if (Constants.YES.equalsIgnoreCase(TokenService)) {
						if (fieldValue[3] == null) {
							if (responseLines[i].contains(prop.getProperty(Constants.SOAP_TOKEN_SUBSCRIPTION_ID))) {
								TokenValue = responseLines[i].trim();
								TokenValues = TokenValue.split(" ");
								fieldValue[3] = TokenValues[1];
								logger.info("[getResponseFieldValues] field3: " + fieldValue[3]);
							}
						}
					}
				}
				fieldValue[2] = driver.findElement(By.xpath(pnReferenceNumberXpath)).getText();
				closeCurrentWindow(driver);
				switchToWindow(driver, mainWindow);
			}
		}
		logger.info("[getResponseFieldValue] Ends");
		return fieldValue;

	}

	/**
	 * This method is used to validate data level fields of a transaction for Processor level 2,3
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param ProcessorName The string that defines processor name of current transaction
	 * @param ProcessorLevel The string that defines processor level of current transaction
	 * @param requestLines The string array that have request lines that contain data fields
	 */
	public void checkDataLevelFields(WebDriver driver, Properties prop, String PaymentEvent, String ProcessorName,
			String ProcessorLevel, String[] requestLines) {
		logger.info("[checkDataLevelFields] Starts");
		String[] datafields = null;
		if (Constants.PROCESSOR_LEVEL_2.equalsIgnoreCase(ProcessorLevel)) {
			if (Constants.PROCESSOR_OMNI_PAY_DIRECT.equalsIgnoreCase(ProcessorName)) {
				String[] datafield = { "merchantVATRegistrationNumber", "purchaserOrderDate",
						"purchaserVATRegistrationNumber", "summaryCommodityCode", "supplierOrderReference", "userPO",
						"freightAmount", "grandTotalAmount", "taxAmount", "postalCode", "alternateTaxRate",
						"commodityCode", "productCode", "totalAmount", "taxRate", "totalAmount", "unitPrice" };
				datafields = datafield;
			} else if (Constants.PROCESSOR_CHASE_PAYMENTECH.equalsIgnoreCase(ProcessorName)) {
				String[] datafield = { "userPO", "invoiceNumber", "taxAmount", "unitPrice" };
				datafields = datafield;
				logger.info("[checkDataLevelFields] datafields value: " + Arrays.toString(datafield));
			} else if (Constants.PROCESSOR_GLOBAL_PAYMENTS.equalsIgnoreCase(ProcessorName)) {
				String[] datafield = { "userPO", "taxAmount", "unitPrice" };
				datafields = datafield;
			} else if (Constants.PROCESSOR_FDC_NASHVILLE.equalsIgnoreCase(ProcessorName)) {
				String[] datafield = { "purchaserVATRegistrationNumber", "userPO", "vatInvoiceReferenceNumber",
						"discountAmount", "dutyAmount", "postalCode", "postalCode", "invoiceNumber", "nationalTax",
						"taxAmount", "unitPrice" };
				datafields = datafield;
			} else if (Constants.PROCESSOR_TOTAL_SYSTEM_SERVICES.equalsIgnoreCase(ProcessorName)) {
				String[] datafield = { "userPO", "taxAmount" };
				datafields = datafield;
			} else if (Constants.PROCESSOR_CREDIT_MUTUEL_CIC.equalsIgnoreCase(ProcessorName)) {
				String[] datafield = { "unitPrice", "quantity", "productCode", "productName", "productSKU",
						"discountAmount", "taxAmount", "grandTotalAmount" };
				datafields = datafield;
			}
		} else if (Constants.PROCESSOR_LEVEL_3.equalsIgnoreCase(ProcessorLevel)) {
			if (Constants.PROCESSOR_OMNI_PAY_DIRECT.equalsIgnoreCase(ProcessorName)) {
				String[] datafield = { "purchasingLevel", "merchantVATRegistrationNumber", "purchaserOrderDate",
						"purchaserVATRegistrationNumber", "summaryCommodityCode", "supplierOrderReference", "userPO",
						"discountAmount", "freightAmount", "taxAmount", "postalCode", "country", "postalCode",
						"alternateTaxRate", "discountAmount", "productCode", "productName", "quantity", "taxAmount",
						"taxRate", "totalAmount", "unitOfMeasure", "unitPrice" };
				datafields = datafield;
			} else if (Constants.PROCESSOR_CHASE_PAYMENTECH.equalsIgnoreCase(ProcessorName)) {
				String[] datafield = { "purchasingLevel", "userPO", "commodityCode", "discountAmount", "invoiceNumber",
						"productCode", "productName", "quantity", "taxAmount", "taxRate", "totalAmount",
						"unitOfMeasure", "unitPrice", "discountAmount", "dutyAmount", "freightAmount",
						"grandTotalAmount", "postalCode", "country", "postalCode" };
				datafields = datafield;
				logger.info("[checkDataLevelFields] datafields value: " + Arrays.toString(datafield));
			} else if (Constants.PROCESSOR_GLOBAL_PAYMENTS.equalsIgnoreCase(ProcessorName)) {
				String[] datafield = { "purchasingLevel", "merchantVATRegistrationNumber", "purchaserOrderDate",
						"purchaserVATRegistrationNumber", "supplierOrderReference", "userPO", "discountAmount",
						"freightAmount", "postalCode", "country", "postalCode", "state", "commodityCode",
						"discountAmount", "invoiceNumber", "productCode", "quantity", "taxAmount", "unitOfMeasure",
						"unitPrice" };
				datafields = datafield;
			} else if (Constants.PROCESSOR_FDC_NASHVILLE.equalsIgnoreCase(ProcessorName)) {
				String[] datafield = { "purchasingLevel", "supplierOrderReference", "userPO", "discountAmount",
						"freightAmount", "grandTotalAmount", "postalCode", "country", "postalCode", "state",
						"commodityCode", "discountAmount", "invoiceNumber", "productCode", "productName", "productSKU",
						"quantity", "unitOfMeasure", "unitPrice" };
				datafields = datafield;
			} else if (Constants.PROCESSOR_TOTAL_SYSTEM_SERVICES.equalsIgnoreCase(ProcessorName)) {
				String[] datafield = { "purchasingLevel", "merchantVATRegistrationNumber", "purchaserOrderDate",
						"purchaserVATRegistrationNumber", "summaryCommodityCode", "userPO", "discountAmount",
						"freightAmount", "grandTotalAmount", "postalCode", "country", "postalCode", "state",
						"commodityCode", "invoiceNumber", "productCode", "productName", "quantity", "taxAmount",
						"taxRate", "totalAmount", "unitOfMeasure", "unitPrice" };
				datafields = datafield;
			} else if (Constants.PROCESSOR_CREDIT_MUTUEL_CIC.equalsIgnoreCase(ProcessorName)) {
				String[] datafield = { "unitPrice", "quantity", "productCode", "productName", "productSKU",
						"discountAmount", "taxAmount", "grandTotalAmount" };
				datafields = datafield;
			}
		}
		logger.info("[checkDataLevelFields] datafields value: " + Arrays.toString(datafields));
		logger.info("[checkDataLevelFields] datafields length value: " + datafields.length);
		Reporter.log(PaymentEvent + " - " + ProcessorLevel + " " + ProcessorName + " Supported Fields:");
		for (int i = 0; i < datafields.length; i++) {
			int dataFlag = 0;
			int k = i + 1;
			logger.info("[checkDataLevelFields] field value: " + datafields[i]);
			for (int j = 0; j < requestLines.length; j++) {
				if (requestLines[j].contains(datafields[i])) {
					dataFlag = 1;
				}
			}
			if (dataFlag != 0) {
				logger.info("[checkDataLevelFields] field present");
				Reporter.log("Field " + k + ": " + datafields[i]);
			} else {
				logger.info("[checkDataLevelFields] field is missing");
				Reporter.log("Field " + k + ": " + datafields[i] + " is missing");
			}
		}
	}

	/**
	 * This method is used to click the selected payment event
	 * @param driver Webdriver object which will be used for operations
	 * @param paymentEventName The string that contains Payment Event 
	 */
	public void clickPaymentEvent(WebDriver driver, String paymentEventName) {
		logger.info("[clickPaymentEvent] Starts");
		WebElement table = findElementById(driver, prop, Constants.PAYMENT_EVENT_TABLE_ID);
		List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
		String eventName = null;
		List<WebElement> cells = null;
		for (WebElement row : allRows) {
			cells = row.findElements(By.tagName(Constants.TD));
			eventName = cells.get(2).getText();
			if (eventName.equals(paymentEventName)) {
				cells.get(8).findElement(By.id(prop.getProperty(Constants.PAYMENT_EVENT_TRANSACTION_COLUMN_ID)))
						.click();
				logger.info("[clickPaymentEvent] Element Clicked");
				break;
			}
		}
		logger.info("[clickPaymentEvent] Ends");
	}

	/**
	 * This method is used obtain order Id of a transaction
	 * @param driver Webdriver object which will be used for operations
	 * @param paymentEventName The string that contains Payment Event
	 * @return String containing order Id of transaction
	 */
	public String getOrderId(WebDriver driver, String paymentEventName) {
		logger.info("[getOrderId] Starts");
		WebElement table = findElementById(driver, prop, Constants.PAYMENT_EVENT_TABLE_ID);
		List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
		String orderId = null;
		List<WebElement> cells = null;
		String eventName = null;
		for (WebElement row : allRows) {
			cells = null;
			cells = row.findElements(By.tagName(Constants.TD));
			if (cells.size() > 1) {
				eventName = cells.get(2).getText();
				if (eventName.equals(paymentEventName)) {
					orderId = StringUtils.substringAfterLast(cells.get(1)
							.findElement(By.id(prop.getProperty(Constants.PAYMENT_EVENT_TRANSACTION_COLUMN_ID)))
							.getText(), Constants.HASH);
					break;
				}
			}
		}
		logger.info("[getOrderId] Ends");
		return orderId;
	}

	/**
	 * This method is used to single click the payment event
	 * @param driver Webdriver object which will be used for operations
	 * @param Event String containing payment event of transaction
	 * @param Transaction String containing transaction Id of current transaction
	 * @param Result String containing payment result
	 */
	public void clickPaymentEvent(WebDriver driver, String Event, String Transaction, String Result) {
		logger.info("[ClickPaymentEvent] Starts");
		WebElement table = findElementById(driver, prop, Constants.PAYMENT_EVENT_TABLE_ID);
		List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
		List<WebElement> cells = null;
		for (WebElement row : allRows) {
			cells = row.findElements(By.tagName(Constants.TD));
			String paymentEvent = cells.get(2).getText();
			String paymentTransaction = StringUtils.substringBefore(cells.get(1).getText().trim(), Constants.HASH);
			String paymentResult = cells.get(5).getText();
			if (paymentEvent.trim().equalsIgnoreCase(Event.trim())
					&& Transaction.trim().equalsIgnoreCase(paymentTransaction.trim())
					&& (paymentResult.trim().equalsIgnoreCase(Result.trim()))) {
				cells.get(8).findElement(By.id(prop.getProperty(Constants.PAYMENT_EVENT_TRANSACTION_COLUMN_ID)))
						.click();
				logger.info("[ClickPaymentEvent] Element is clicked");
				break;
			}
		}
	}

	/**
	 * This method is used to set the payment method
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentMethod String containing payment method to be selected
	 */
	public void setPaymentMethod(WebDriver driver, String PaymentMethod) {
		logger.info("[setPaymentMethod] Ends");
		List<WebElement> paymentMethods = driver.findElements(By.name(Constants.S_PAY_METH));
		List<WebElement> cells = null;
		for (WebElement paymentMethod : paymentMethods) {
			cells = null;
			cells = paymentMethod.findElements(By.xpath(".."));
			String paymentMethodName = cells.get(0).getText();
			if (paymentMethodName.trim().equals(PaymentMethod)) {
				paymentMethod.click();
				logger.info("[setPaymentMethod] Element is clicked");
				break;
			}
		}
	}

	/**
	 * This method is used to set the Shipping method of the order
	 * @param driver Webdriver object which will be used for operations
	 * @param ShippingMethod String containing shipping method to be selected
	 */
	public void setShippingMethod(WebDriver driver, String ShippingMethod) {
		logger.info("[setShippingMethod] Starts");
		String shippingMethodName = null;
		List<WebElement> shippingMethods = driver.findElements(By.name(Constants.S_SHIP_METH));
		List<WebElement> cells = null;
		for (WebElement shippingMethod : shippingMethods) {
			cells = null;
			cells = shippingMethod.findElements(By.xpath(".."));
			shippingMethodName = cells.get(0).getText();
			shippingMethodName = StringUtils.substringBefore(shippingMethodName, Constants.SPACE);
			if (shippingMethodName.equals(ShippingMethod)) {
				shippingMethod.click();
			}
		}
		logger.info("[setShippingMethod] Ends");
	}

	/**
	 * This method is used to input the url for current transaction
	 * @param transactionType String containing type of current transaction
	 * @param transactionUrl String containing transaction url to be provided as input
	 */
	public void writeCurrentTransactionUrl(String transactionType, String transactionUrl) {
		logger.info("[writeCurrentTransactionUrl] Starts");
		if (null == fileWriter) {
			try {
				fileWriter = new FileWriter(
						System.getProperty(Constants.USER_DIR) + "\\test-output\\transactionData.txt", true);
				bufferedWriter = new BufferedWriter(fileWriter);
				printWriter = new PrintWriter(bufferedWriter);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		printWriter.println(transactionType + " :" + transactionUrl);
		printWriter.flush();
		logger.info("[writeCurrentTransactionUrl] Ends");
	}

	/**
	 * This function is used to check if any alert is present in the window
	 * @param driver Webdriver object which will be used for operations
	 * @return Boolean as a response if alert is present in window
	 */
	public boolean isAlertPresent(WebDriver driver) {
		logger.info("[isAlertPresent] Starts");
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException Ex) {
			return false;
		}
	}

	/**
	 * This method is used to set Shipping method in SCA
	 * @param driver Webdriver object which will be used for operations
	 * @param ShippingMethod String containing Shipping method to be selected
	 */
	public void setShippingMethodSCA(WebDriver driver, String ShippingMethod) {
		logger.info("[setShippingMethodSCA] Starts");
		String shippingMethodName = null;
		List<WebElement> shippingMethods = findElementsByClassName(driver,
				Constants.SUITE_COMMERCE_ADVANCED_SHIPPING_METHOD_CLASS_NAME);
		List<WebElement> cells = null;
		for (WebElement shippingMethod : shippingMethods) {
			cells = null;
			cells = shippingMethod.findElements(By.xpath("."));
			shippingMethodName = cells.get(0).getText();
			logger.info("[setShippingMethodSCA] shipping method:" + shippingMethodName);
			if (shippingMethodName.startsWith(ShippingMethod)) {
				shippingMethod.click();
				logger.info("[setShippingMethodSCA] Element is clicked");
			}
		}
	}

	/**
	 * This function is used to find a webElement in a page using unique id
	 * @param driver Webdriver object which will be used for operations
	 * @param prop Properties file which is configured for all xpath, user related data
	 * @param Id String containing ID of webelement to be found
	 * @return WebElement containing given Id
	 */
	public WebElement findElementById(WebDriver driver, Properties prop, String Id) {
		logger.info("[findElementById] Starts");
		String idInProp = null;
		WebElement element = null;
		logger.info("[findElementById] element Id=" + Id);
		if (null != Id) {
			logger.info("[findElementById] Id is not null.");
			idInProp = prop.getProperty(Id);
			element = driver.findElement(By.id(idInProp));
		} else {
			logger.info("[findElementById] Id is null.");
		}
		logger.info("[findElementById] Ends");
		return element;
	}

	/**
	 * This method is used single click a webelement with a unique Id
	 * @param driver Webdriver object which will be used for operations
	 * @param Idd String containing ID of webelement to be clicked
	 * @return WebElement containing given Id
	 */
	public WebElement clickElementById(WebDriver driver, String Id) {
		logger.info("[clickElementById] Starts");
		WebElement element = null;
		String idInProp = null;
		if (null != Id) {
			idInProp = prop.getProperty(Id);
			logger.info("[clickElementById] element Id=" + idInProp);
			if (null != idInProp) {
				element = driver.findElement(By.id(idInProp));
				element.click();
			} else {
				logger.info("[clickElementById] Id is null.");
			}
		}
		logger.info("[clickElementById] Ends");
		return element;
	}

	/**
	 * This method is used to find group of webelements having given class
	 * @param driver Webdriver object which will be used for operations
	 * @param className String containing class of elements to be found
	 * @return List of Webelements containing given class
	 */
	public List<WebElement> findElementsByClassName(WebDriver driver, String className) {
		logger.info("[findElementsByClassName] Starts");
		logger.info("[findElementsByClassName] element className=" + className);
		List<WebElement> elements = null;
		String classNameInProp = null;
		if (null != className) {
			classNameInProp = prop.getProperty(className);
			elements = driver.findElements(By.className(classNameInProp));
		} else {
			logger.info("[findElementsByClassName] className is null.");
		}
		logger.info("[findElementsByClassName] Ends");
		return elements;
	}

	/**
	 * This method is used to click the given card type
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentCardType String containing Payment card type to be clicked
	 */
	public void clickPaymentCard(WebDriver driver, String PaymentCardType) {
		logger.info("[clickPaymentCard] Starts");
		String paymentCardId = null;
		if (Constants.VISA.equalsIgnoreCase(PaymentCardType)) {
			paymentCardId = Constants.CARD_TYPE_001;
		} else if (Constants.MASTERCARD.equalsIgnoreCase(PaymentCardType)) {
			paymentCardId = Constants.CARD_TYPE_002;
		} else if (Constants.AMEX.equalsIgnoreCase(PaymentCardType)) {
			paymentCardId = Constants.CARD_TYPE_003;
		}
		clickElementById(driver, paymentCardId);
		logger.info("[clickPaymentCard] Ends");
	}

	/**
	 * This method is used to click the given account type
	 * @param driver Webdriver object which will be used for operations
	 * @param prop Properties file which is configured for all xpath, user related data
	 * @param AccType String containing Account type to be clicked
	 */
	public void clickAccountType(WebDriver driver, Properties prop, String AccType) {
		logger.info("[clickAccountType] Starts");
		executor = (JavascriptExecutor) driver;
		String AccountType = null;
		if (Constants.CHECKING.equalsIgnoreCase(AccType)) {
			AccountType = Constants.ECHECK_ACCOUNT_TYPE_CHECKING;
		} else if (Constants.CORPORATE_CHECKING.equalsIgnoreCase(AccType)) {
			AccountType = Constants.ECHECK_ACCOUNT_TYPE_CORPORATE_CHECKING;
		} else if (Constants.GENERAL_LEDGER.equalsIgnoreCase(AccType)) {
			AccountType = Constants.ECHECK_ACCOUNT_TYPE_GENERAL_LEDGER;
		} else if (Constants.SAVINGS.equalsIgnoreCase(AccType)) {
			AccountType = Constants.ECHECK_ACCOUNT_TYPE_SAVINGS;
		}
		executor.executeScript(prop.getProperty(AccountType));
		logger.info("[clickAccountType] Ends");
	}

	/**
	 * This method is used to get details of the item to be ordered
	 * @param Item String containing details of the item
	 * @return Object containing details of the item
	 */
	public Object[][] getItemDetails(String Item) {
		logger.info("[getItemDetails] Starts");
		Object ItemDetails[][] = null;
		/*
		 * String itemScenario = null;
		 * 
		 * if (Constants.POSITIVE_ITEM_DATA.equals(Item)) { itemScenario =
		 * Constants.POSITIVE_ITEM; logger.info("[getItemDetails] itemScenario:" +
		 * itemScenario); } else if (Constants.NEGATIVE_ITEM_DATA.equals(Item)) {
		 * itemScenario = Constants.NEGATIVE_ITEM;
		 * logger.info("[getItemDetails] itemScenario:" + itemScenario); } else {
		 * itemScenario = Constants.NULL; logger.info("[getItemDetails] itemScenario:" +
		 * itemScenario); }
		 */
		logger.info("[getItemDetails] Sheet Name: " + Item);
		ItemDetails = FileUtil.getSheetData(XLSX_FILE_PATH, Item);
		return ItemDetails;
	}

	/**
	 * This method is used to get details of the webstore item to be ordered
	 * @param Item Item String containing details of the item
	 * @return Object containing details of the item
	 */
	public Object[][] getWSItemDetails(String Item) {
		logger.info("[getWSItemDetails] Starts");
		Object ItemDetails[][] = null;
		String itemScenario = null;
		if (Constants.POSITIVE_WS_ITEM_DATA.equals(Item)) {
			itemScenario = Constants.POSITIVE_ITEM;
			logger.info("[getWSItemDetails] itemScenario:" + itemScenario);
		} else if (Constants.NEGATIVE_WS_ITEM_DATA.equals(Item)) {
			itemScenario = Constants.NEGATIVE_ITEM;
			logger.info("[getWSItemDetails] itemScenario:" + itemScenario);
		} else {
			itemScenario = Constants.NULL;
			logger.info("[getWSItemDetails] itemScenario:" + itemScenario);
		}
		logger.info("[getWSItemDetails] Item Scenario:" + (prop.getProperty(itemScenario + Constants._SHEET_NAME)));
		ItemDetails = FileUtil.getSheetData(XLSX_FILE_PATH, (prop.getProperty(itemScenario + Constants._SHEET_NAME)));
		logger.info("[getWSItemDetails] Ends");
		return ItemDetails;
	}

	/**
	 * This method is used to fetch Authorization code of the order
	 * @param driver Webdriver object which will be used for operations
	 * @param authCodeXPath xpath of the webelement containing authorization code
	 * @return String containing authorization code
	 */
	public String getAuthCode(WebDriver driver, String authCodeXPath) {
		logger.info("[getAuthCode] Starts");
		String authCode = null;
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		executor = (JavascriptExecutor) driver;
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				switchToWindow(driver, childWindow);
				authCode = driver.findElement(By.xpath(authCodeXPath)).getText();
				closeCurrentWindow(driver);
				switchToWindow(driver, mainWindow);

			}
		}
		logger.info("[getAuthCode] Ends");
		return authCode;
	}

	/**
	 * This method is used to fetch Authorization code and PN reference number of the order
	 * @param driver Webdriver object which will be used for operations
	 * @param authCodeXPath xpath of the webelement containing authorization code
	 * @param pnrefXPath xpath of the webelement containing PN reference number
	 * @return String array containing Authorization code and PN reference code
	 */
	public String[] getAuthCode(WebDriver driver, String authCodeXPath, String pnrefXPath) {
		logger.info("[getAuthCode] Starts");
		String[] authCode = new String[2];
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		executor = (JavascriptExecutor) driver;
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				switchToWindow(driver, childWindow);
				authCode[0] = driver.findElement(By.xpath(authCodeXPath)).getText();
				authCode[1] = driver.findElement(By.xpath(pnrefXPath)).getText();
				closeCurrentWindow(driver);
				switchToWindow(driver, mainWindow);
			}
		}
		logger.info("[getAuthCode] Ends");
		return authCode;
	}

	/**
	 * This method is used to fetch PN reference number of the order
	 * @param driver Webdriver object which will be used for operations
	 * @param pnReferenceNumberXpath xpath of the webelement containing PN reference number
	 * @return String containing PN reference code
	 */
	public String getPNReferenceNumber(WebDriver driver, String pnReferenceNumberXpath) {
		logger.info("[getPNReferenceNumber] Starts");
		String pnReferenceNumber = null;
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		executor = (JavascriptExecutor) driver;
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				switchToWindow(driver, childWindow);
				pnReferenceNumber = driver.findElement(By.xpath(pnReferenceNumberXpath)).getText();
				closeCurrentWindow(driver);
				switchToWindow(driver, mainWindow);
			}
		}
		logger.info("[getPNReferenceNumber] Ends");
		return pnReferenceNumber;
	}

	/**
	 * This method is used to handle payment instrument for the transaction
	 * @param driver Webdriver object which will be used for operations
	 * @param control String containing setup control for payment instrument
	 */
	public void handlePaymentInstrument(WebDriver driver, String control) {
		logger.info("[HandlePaymentInstrument] Starts");
		executor = (JavascriptExecutor) driver;
		driver = setupDriver(driver, prop, Constants.PAYMENT_INSTRUMENT_URL, null);
		clickElementByXPath(driver, prop, Constants.ENABLE_FEATURES_TRANSACTIONS_TAB);
		if (control.equalsIgnoreCase(Constants.DISABLE)) {
			executor.executeScript(
					"document.getElementById('paymentinstruments_fs').setAttribute('class', 'checkbox_unck')");
		} else if (control.equalsIgnoreCase(Constants.ENABLE)) {
			executor.executeScript(
					"document.getElementById('paymentinstruments_fs').setAttribute('class', 'checkbox_ck')");
		}

		/*
		 * if (control.equalsIgnoreCase(Constants.DISABLE)) { if
		 * (driver.findElement(By.xpath(prop.getProperty(Constants.
		 * PAYMENT_INSTRUMENT_CHECKBOX))).isSelected()) { logger.info(
		 * "[PaymentInstrument][checkbox] disabling");
		 * driver.findElement(By.xpath(prop.getProperty(Constants.
		 * PAYMENT_INSTRUMENT_CHECKBOX))).click(); } } else if
		 * (control.equalsIgnoreCase(Constants.ENABLE)) { if
		 * (!driver.findElement(By.xpath(prop.getProperty(Constants.
		 * PAYMENT_INSTRUMENT_CHECKBOX))).isSelected()) { logger.info(
		 * "[PaymentInstrument][checkbox] enabling");
		 * driver.findElement(By.xpath(prop.getProperty(Constants.
		 * PAYMENT_INSTRUMENT_CHECKBOX))).click(); } }
		 */

		clickElementByXPath(driver, prop, Constants.PAYMENT_INSTRUMENT_SAVE_BUTTON);
		logger.info("[HandlePaymentInstrument] Ends");
	}

	/**
	 * This method is used to reload webpage if element with given xpath is not found
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param xPath xpath of the webelement to be searched for
	 */
	public void doRefreshIfNoElementByXPath(WebDriver driver, Properties prop, String xPath) {
		boolean isNeedRefresh = true;
		int refreshAttemptCount = 0;
		WebElement element = null;
		element = findElementByXPath(driver, prop, xPath);
		do {
			refreshAttemptCount++;
			if (null == element) {
				driver.navigate().refresh();
			} else {
				isNeedRefresh = (refreshAttemptCount > 3) ? false : true;
			}
		} while (isNeedRefresh);
	}

	/**
	 * This method is used to check if given string is Numeric or not
	 * @param str String which contains text to be checked
	 * @return Boolean which contains status after checking string
	 */
	public boolean isNumeric(String str) {
		try {
			Long.parseLong(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * This method is used add title and description to TestNG invoicing report
	 * @param title String containing title to be added to report
	 * @param description String containing description to be added to report
	 */
	public void addLogToTestNGReportInvoicing(String title, String description) {
		Reporter.log(title + " : " + description);
	}

	/**
	 * This method is used add service and PN reference to TestNG report
	 * @param serviceName String containing name of service that is used for transaction
	 * @param pnReferenceNumber String containing PN reference of the transaction
	 */
	public void addLogToTestNGReport(String serviceName, String pnReferenceNumber) {
		Reporter.log(serviceName + Constants.PN_REFERENCE_NUMBER + Constants.A_HREF_EQUAL
				+ prop.getProperty(Constants.PREIX_EBC_TRANSACTIONS_URL) + pnReferenceNumber
				+ prop.getProperty(Constants.SUFFIX_EBC_TRANSACTIONS_URL) + Constants.SINGLE_QUOTE_CLOSING_TAG
				+ pnReferenceNumber + Constants.CLOSING_A_TAG);
	}

	/**
	 * This method is used add service name and PN service Id to TestNG report 
	 * @param serviceName String containing name of service that is used for transaction
	 * @param url String containing url 
	 * @param serviceId String containing Service id of the transaction
	 */
	public void addLogToTestNGReport(String serviceName, String url, String serviceId) {
		Reporter.log(serviceName + Constants.ORDER_ID_COLON + Constants.A_HREF_EQUAL + url
				+ Constants.SINGLE_QUOTE_CLOSING_TAG + serviceId + Constants.CLOSING_A_TAG);
	}

	/**
	 * This method is used to add screenshot to TestNGReport
	 * @param destDir The string array containing directory path where screenshot is saved
	 */
	public void addScreenshotToTestNGReport(File destDir) {
		String[] files = destDir.list();
		File dFile = null;
		for (String file : files) {
			dFile = new File(file);
			Reporter.log(StringUtils.substringBefore(file, Constants.DOT) + ": 		" + Constants.A_HREF_EQUAL
					+ (destDir + Constants.DOUBLE_BACKWARD_SLASH + dFile) + "'> " + Constants.IMG_SRC_EQUAL
					+ (destDir + Constants.DOUBLE_BACKWARD_SLASH + dFile) + "' "
					+ prop.getProperty(Constants.SCREENSHOT_HEIGHT_WIDTH) + "/> </a>");
		}
	}

	/**
	 * This method is used to set details of item to be ordered
	 * @param driver Webdriver object which will be used for operations
	 * @param ItemDetails Contains details of all items that is to be ordered
	 */
	public void setItemDetails(WebDriver driver, Object[][] ItemDetails) {
		executor = (JavascriptExecutor) driver;
		for (int i = 0; i < ItemDetails.length; i++) {
			int j = 0;
			executor.executeScript(prop.getProperty(Constants.NLAPI_SELECT_NEW_LINE_ITEM));
			setCurrentLineItemText(driver, Constants.NLAPI_CURRENT_LINE_ITEM_PREFIX, ItemDetails[i][j].toString(),
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			j++;
			if (isAlertPresent(driver)) {
				acceptAlert(driver);
			}
			setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_QUANTITY_PREFIX,
					ItemDetails[i][j].toString(), Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			j++;

			setCurrentLineItemValue(driver, Constants.NLAPI_CURRENT_LINE_ITEM_RATE_PREFIX, ItemDetails[i][j].toString(),
					Constants.NLAPI_CURRENT_LINE_ITEM_SUFFIX);
			j++;
			executor.executeScript(prop.getProperty(Constants.NLAPI_COMMIT_LINE_ITEM));
		}
	}

	/**
	 * This method is used to clear value of an element defined by an xpath
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param xPath xpath of webElement whose value is to be cleared
	 */
	public void clearElementByXPath(WebDriver driver, Properties prop, String xPath) {
		WebElement element = null;
		logger.info("[clickElementByXPath] xPath=" + xPath);
		if (null != xPath) {
			element = findElementByXPath(driver, prop, xPath);
			if (null != element) {
				logger.info("[clickElementByXPath] before click xPath=" + xPath);
				try {
					element.clear();
				} catch (Exception e) {
					e.printStackTrace();
				}
				logger.info("[clickElementByXPath] after click xPath=" + xPath);
			} else {
				logger.info("[clickElementByXPath] Not able to set the value element is Null for xPath=" + xPath);
			}
		} else {
			logger.info("[clickElementByXPath] xPath is Null for given xPath:" + xPath);
		}
	}
 
	/**
	 * This method is used to set text of current line item
	 * @param driver Webdriver object which will be used for operations
	 * @param currentLineItemTextPrefix String containing prefix text of line item
	 * @param itemDetailsValue String containing text of item detail
	 * @param currentLineItemSuffix String containing prefix text  of line item
	 */
	public void setCurrentLineItemText(WebDriver driver, String currentLineItemTextPrefix, String itemDetailsValue,
			String currentLineItemSuffix) {
		executor = (JavascriptExecutor) driver;
		logger.info("[setCurrentLineItemText] line item text: " + prop.getProperty(currentLineItemTextPrefix)
				+ Constants.SINGLE_QUOTATION + itemDetailsValue + Constants.SINGLE_QUOTATION
				+ prop.getProperty(currentLineItemSuffix));
		executor.executeScript(prop.getProperty(currentLineItemTextPrefix) + Constants.SINGLE_QUOTATION
				+ itemDetailsValue + Constants.SINGLE_QUOTATION + prop.getProperty(currentLineItemSuffix));
	}

	/**
	 * This method is used to set value of current line item
	 * @param driver Webdriver object which will be used for operations
	 * @param currentLineItemValuePrefix String containing prefix text of line item
	 * @param itemDetailsValue String containing value of item detail
	 * @param currentLineItemSuffix String containing prefix text  of line item
	 */
	public void setCurrentLineItemValue(WebDriver driver, String currentLineItemValuePrefix, String itemDetailsValue,
			String currentLineItemSuffix) {
		executor = (JavascriptExecutor) driver;
		logger.info("[setCurrentLineItemValue] line item value: " + prop.getProperty(currentLineItemValuePrefix)
				+ Constants.SINGLE_QUOTATION + itemDetailsValue + Constants.SINGLE_QUOTATION
				+ prop.getProperty(currentLineItemSuffix));
		executor.executeScript(prop.getProperty(currentLineItemValuePrefix) + Constants.SINGLE_QUOTATION
				+ itemDetailsValue + Constants.SINGLE_QUOTATION + prop.getProperty(currentLineItemSuffix));
	}

	/**
	 * This method is used to set value of line item
	 * @param driver Webdriver object which will be used for operations
	 * @param lineItemValuePrefix String containing prefix text of line item
	 * @param itemPosition Integer containing position of line item
	 * @param lineItemValueSuffix String containing prefix text  of line item
	 */
	public void setLineItemValue(WebDriver driver, String lineItemValuePrefix, int itemPosition,
			String lineItemValueSuffix) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		logger.info("[setLineItemValue] line item value: " + prop.getProperty(lineItemValuePrefix)
				+ Constants.SINGLE_QUOTATION + itemPosition + Constants.SINGLE_QUOTATION
				+ prop.getProperty(lineItemValueSuffix));
		executor.executeScript(prop.getProperty(lineItemValuePrefix) + Constants.SINGLE_QUOTATION + itemPosition
				+ Constants.SINGLE_QUOTATION + prop.getProperty(lineItemValueSuffix));
	}

	/**
	 * This method is used to set value to a field
	 * @param driver Webdriver object which will be used for operations
	 * @param fieldTextPrefix String containing prefix of text field
	 * @param fieldValue String containing value to be entered for text
	 * @param fieldTextSuffix  String containing suffix of text field
	 */
	public void setFieldText(WebDriver driver, String fieldTextPrefix, String fieldValue, String fieldTextSuffix) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		logger.info("[setFieldText] field text: " + prop.getProperty(fieldTextPrefix) + Constants.SINGLE_QUOTATION
				+ fieldValue + Constants.SINGLE_QUOTATION + prop.getProperty(fieldTextSuffix));
		executor.executeScript(prop.getProperty(fieldTextPrefix) + Constants.SINGLE_QUOTATION + fieldValue
				+ Constants.SINGLE_QUOTATION + prop.getProperty(fieldTextSuffix));
	}

	/**
	 * This method is used to select the employee details
	 * @param driver Webdriver object which will be used for operations
	 * @param EmployeeName Sting containing name of the employee
	 * @param EmployeeEmail Sting containing email id of the employee
	 */
	public void clickEmployee(WebDriver driver, String EmployeeName, String EmployeeEmail) {
		logger.info("[clickEmployee] Starts");
		WebElement table = findElementById(driver, prop, Constants.PAYMENT_PROCESSING_TABLE_ID);
		List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
		String employeeName = null;
		String employeeEmail = null;
		List<WebElement> cells = null;
		for (WebElement row : allRows) {
			cells = row.findElements(By.tagName(Constants.TD));
			employeeName = cells.get(2).getText();
			employeeEmail = cells.get(9).getText();
			if (EmployeeName.equals(employeeName) && EmployeeEmail.equals(employeeEmail)) {
				cells.get(0).findElement(By.linkText(Constants.EDIT)).click();
				break;
			}
		}
	}

	/**
	 * This method is used to select the role of the user
	 * @param driver Webdriver object which will be used for operations
	 * @param RoleName The string containing role of the user
	 * @param EnvironmentName The string containing name of the environment used by the user
	 */
	public void clickRole(WebDriver driver, String RoleName, String EnvironmentName) {
		logger.info("[clickRole] Starts");
		WebElement table = findElementById(driver, prop, Constants.PAYMENT_PROCESSING_TABLE_ID);
		List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
		String roleName = null;
		String environmentName = null;
		List<WebElement> cells = null;
		for (WebElement row : allRows) {
			cells = row.findElements(By.tagName(Constants.TD));
			roleName = cells.get(1).getText();
			environmentName = cells.get(2).getText();
			if (RoleName.equals(roleName) && EnvironmentName.equals(environmentName)) {
				cells.get(1).findElement(By.linkText(Constants.NETSUITE_AUTOMATION_TESTING)).click();
				break;
			}
		}
	}

	/**
	 * This method is used to add profile
	 * @param driver Webdriver object which will be used for operations
	 * @param ScriptId String containing script id of the profile
	 */
	public void clickAddProfile(WebDriver driver, String ScriptId) {
		logger.info("[clickAddProfile] Starts");
		WebElement table = findElementById(driver, prop, Constants._TAB);
		List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
		String scriptId = null;
		List<WebElement> cells = null;
		for (WebElement row : allRows) {
			cells = row.findElements(By.tagName(Constants.TD));
			scriptId = cells.get(2).getText();
			if (ScriptId.equals(scriptId)) {
				cells.get(0).findElement(By.linkText(Constants.ADD_PROFILE)).click();
				break;
			}
		}
	}

	/**
	 * This method is used to validate the EBC profile
	 * @param driver Webdriver object which will be used for operations
	 * @param ProfileName String containing profile name of EBC portal user
	 */
	public void checkEBCProfile(WebDriver driver, String ProfileName) {
		logger.info("[checkEBCProfile] Starts");
		WebElement profileTable = findElementByXPath(driver, prop, Constants.PROFILE_TABLE);
		String profileTableContent = profileTable.getAttribute(Constants.INNERHTML);
		int start = 0;
		int end = 0;
		String CheckboxId = null;
		StringBuffer buf = null;
		if (profileTableContent.contains(ProfileName)) {
			logger.info(profileTableContent.lastIndexOf(ProfileName));
			buf = new StringBuffer(profileTableContent);
			end = profileTableContent.lastIndexOf(ProfileName);
			profileTableContent = buf.replace(start, end, Constants.EMPTY_STRING).toString();
			buf = new StringBuffer(profileTableContent);
			start = profileTableContent.indexOf(Constants.CHECKBOX_HIPHEN_HIPHEN);
			profileTableContent = buf.replace(0, start, Constants.EMPTY_STRING).toString();
			CheckboxId = StringUtils.substringBefore(profileTableContent, "\"");
			clickElementById(driver, CheckboxId);
		}
		logger.info("[checkEBCProfile] Ends");
	}

	/**
	 * This method is used to rename folder as per service name and order id
	 * @param screenShotPath The string containing directory of screenshot path
	 * @param ServiceName String containing name of service used for transaction
	 * @param OrderId String containing order Id of the transaction completed
	 */
	public void renameFolder(String screenShotPath, String ServiceName, String OrderId) {
		logger.info("[renameFolder] Starts");
		File srcDir = new File(screenShotPath);
		File destDir = null;
		if (null != OrderId) {
			destDir = new File(
					srcDir.getParent() + Constants.DOUBLE_BACKWARD_SLASH + ServiceName + Constants.HIPHEN + OrderId);
		} else {
			destDir = new File(srcDir.getParent() + Constants.DOUBLE_BACKWARD_SLASH + ServiceName + Constants.HIPHEN
					+ Constants.ERROR);
		}
		try {
			FileUtils.copyDirectory(srcDir, destDir);
			deleteFilesInFolder(srcDir);
			addScreenshotToTestNGReport(destDir);
			srcDir.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("[renameFolder] Ends");
	}

	/**
	 * This method is used to delete files in a directory
	 * @param srcDir The file directory from which delete operation to be done
	 */
	public void deleteFilesInFolder(File srcDir) {
		logger.info("[deleteFilesInFolder] Starts");
		String[] files = srcDir.list();
		for (String file : files) {
			File currentFile = new File(srcDir.getPath(), file);
			currentFile.delete();
		}
		logger.info("[deleteFilesInFolder] Ends");
	}

	/**
	 * This method is used to scroll to the bottom of the present webpage
	 * @param driver Webdriver object which will be used for operations
	 */
	public void scrollToBottom(WebDriver driver) {
		logger.info("[scrollToBottom] Starts");
		executor = (JavascriptExecutor) driver;
		executor.executeScript(Constants.SCROLL_TO_BOTTOM_OF_THE_PAGE);
		logger.info("[scrollToBottom] Ends");
	}

	/**
	 * This method is used to scroll in the present webpage to view a value
	 * @param driver Webdriver object which will be used for operations
	 * @param prop properties file which is configured for all xpath, user related data
	 * @param value String value which defines where to scroll to in a webpage
	 */
	public void scrollIntoView(WebDriver driver, Properties prop, String value) {
		logger.info("[scrollIntoView] Starts");
		WebElement element = findElementByXPath(driver, prop, value);
		executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].scrollIntoView(true);", element);
		logger.info("[scrollIntoView] Ends");
	}

	/**
	 * This method is used to take screenshot of the current web page
	 * @param driver Webdriver object which will be used for operations
	 * @param screenShotPath String containing path directory of the folder to store screenshots
	 * @param screenshotName String containing name of the screenshot file
	 * @throws Exception
	 */
	public void takeScreenShot(WebDriver driver, String screenShotPath, String screenshotName) throws Exception {
		logger.info("[takeScreenShot] Starts");
		if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.SCREEN_SHOT_REQUIRED))) {
			logger.info("[takeScreenShot] ScreenShot is Required " + screenShotPath + Constants.DOUBLE_BACKWARD_SLASH
					+ screenshotName + Constants._JPG);
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			logger.info("[takeScreenShot] 0 ScreenShot saved in " + screenShotPath + Constants.DOUBLE_BACKWARD_SLASH
					+ screenshotName + Constants._JPG);

			// The below method will save the screen shot in d drive with name
			File destFile = new File(
					screenShotPath + Constants.DOUBLE_BACKWARD_SLASH + screenshotName + Constants._JPG);
			logger.info("[takeScreenShot] 1 ScreenShot saved in " + screenShotPath + Constants.DOUBLE_BACKWARD_SLASH
					+ screenshotName + Constants._JPG);

			FileUtils.copyFile(scrFile, destFile);
			logger.info("[takeScreenShot] 2 ScreenShot saved in " + screenShotPath + Constants.DOUBLE_BACKWARD_SLASH
					+ screenshotName + Constants._JPG);
		} else {
			logger.info("[takeScreenShot] ScreenShot is Not Required");
		}
		logger.info("[takeScreenShot] Ends");
	}

	/**
	 * This method is used to refresh the current page
	 * @param driver Webdriver object which will be used for operations
	 */
	public void refreshCurrentPage(WebDriver driver) {
		logger.info("[refreshCurrentPage] Starts");
		driver.navigate().refresh();
		logger.info("[refreshCurrentPage] Ends");
	}

	/**
	 * This method is used to close the current window
	 * @param driver Webdriver object which will be used for operations
	 */
	public void closeCurrentWindow(WebDriver driver) {
		logger.info("[closeCurrentWindow] Starts");
		driver.close();
		logger.info("[closeCurrentWindow] Ends");
	}

	/**
	 * This method is used to switch to new frame
	 * @param driver Webdriver object which will be used for operations
	 * @param frameId Integer which contains id of the frame to which driver is to be switched
	 */
	public void switchToFrame(WebDriver driver, int frameId) {
		logger.info("[switchToFrame] Starts");
		driver.switchTo().frame(frameId);
		logger.info("[switchToFrame] Ends");
	}

	/**
	 * This method is used to switch to new window
	 * @param driver Webdriver object which will be used for operations
	 * @param window Integer which contains id of the window to which driver is to be switched
	 */
	public void switchToWindow(WebDriver driver, String window) {
		logger.info("[switchToWindow] Starts");
		driver.switchTo().window(window);
		logger.info("[switchToWindow] Ends");
	}

	/**
	 * This method is used to switch driver to the main window
	 * @param driver Webdriver object which will be used for operations
	 * @return Webdriver element which is in the main browser
	 */
	public WebDriver switchToMainWindow(WebDriver driver) {
		logger.info("[switchToMainWindow] Starts");
		Iterator<String> itr = null;
		String childWindow = null;
		executor = (JavascriptExecutor) driver;
		String mainWindow = driver.getWindowHandle();
		Set<String> allWindows = driver.getWindowHandles();
		logger.info("[executeAuth] window size: " + allWindows.size());
		itr = allWindows.iterator();
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				closeCurrentWindow(driver);
				switchToWindow(driver, childWindow);
			}
		}
		logger.info("[switchToMainWindow] Ends");
		return driver;
	}

	/**
	 * This method is used to get the url of page in which driver is currently active
	 * @param driver Webdriver object which will be used for operations
	 * @return String containing url of the current web page
	 */
	public String getCurrentUrl(WebDriver driver) {
		logger.info("[getCurrentUrl] Starts");
		return driver.getCurrentUrl();
	}

	/**
	 * This method is used to accept an alert present in a webpage
	 * @param driver Webdriver object which will be used for operations
	 */
	public void acceptAlert(WebDriver driver) {
		logger.info("[acceptAlert] Starts");
		driver.switchTo().alert().accept();
	}

	/**
	 * This method is used to maximize the current browser window
	 * @param driver Webdriver object which will be used for operations
	 */
	public void maximizeWindow(WebDriver driver) {
		logger.info("[maximizeWindow] Starts");
		driver.manage().window().maximize();
	}

	/**
	 * This method is used to get the path of directory where screenshots are saved
	 * @param screenShotModulePath String containing path of Screenshot module
	 * @param ServiceName String containing name of service used for transaction
	 * @return String containing path where screenshots are saved
	 * @throws IOException Handles exceptions occurred
	 */
	public String getScreenshotsPath(String screenShotModulePath, String ServiceName) throws IOException {
		logger.info("[getScreenshotsPath] Starts");
		String screenShotPath = null;
		File dir = new File(screenShotModulePath + Constants.DOUBLE_BACKWARD_SLASH + ServiceName);
		dir.mkdir();
		screenShotPath = screenShotModulePath + Constants.DOUBLE_BACKWARD_SLASH + ServiceName;
		return screenShotPath;
	}

	/**
	 * This method is used to get path of screenshot modules folder
	 * @param moduleName String containing name of module folder
	 * @return String containing path of screenshot module folder
	 * @throws IOException Handles exceptions occurred
	 */
	public String getScreenshotsModulePath(String moduleName) throws IOException {
		logger.info("[getScreenshotsModulePath] Starts");
		String screenShotModulePath = null;
		String basePath = System.getProperty(Constants.USER_DIR)
				+ prop.getProperty(Constants.BASE_PATH_MULTISCREENSHOT_DIRECTORY);
		screenShotModulePath = getScreenshotsPath(basePath, moduleName);
		logger.info("[getScreenshotsModulePath] Ends");
		return screenShotModulePath;
	}

	/**
	 * This method is used to select a website preview
	 * @param driver Webdriver object which will be used for operations
	 * @param WebSite String containing website to be selected
	 */
	public void clickWebSitePreview(WebDriver driver, String WebSite) {
		logger.info("[clickWebSitePreview] Starts");
		WebElement div = findElementById(driver, prop, Constants.WEBSITES_TABLE_ID);
		logger.info("[clickWebSitePreview] div: " + div);
		WebElement table = div.findElement(By.xpath(".//*"));
		List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
		List<WebElement> cells = null;
		String WebSiteName = null;
		logger.info("[clickWebSitePreview] WebSite:" + WebSite);
		for (WebElement row : allRows) {
			cells = row.findElements(By.tagName(Constants.TD));
			WebSiteName = cells.get(1).getText();
			if (WebSiteName.equals(WebSite)) {
				logger.info("[clickWebSitePreview] Clicking on Preview Profile: " + WebSite);
				cells.get(4).click();
				break;
			}
		}

	}

	/**
	 * This method is used to edit a payment processing profile
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentProcessingProfile String containing Payment processing profile to be edited
	 */
	public void clickEditPaymentProcessingProfile(WebDriver driver, String PaymentProcessingProfile) {
		logger.info("[clickEditPaymentProcessingProfile] Starts");
		WebElement div = findElementById(driver, prop, Constants.PAYMENT_PROCESSING_TABLE_ID);
		logger.info("[clickEditPaymentProcessingProfile] div: " + div);
		WebElement table = div.findElement(By.xpath(".//*"));
		List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
		List<WebElement> cells = null;
		String PaymentProcessingProfileName = null;
		logger.info("[clickEditPaymentProcessingProfile] PaymentProcessingProfile:" + PaymentProcessingProfile);
		for (WebElement row : allRows) {
			cells = row.findElements(By.tagName(Constants.TD));
			PaymentProcessingProfileName = cells.get(1).getText();
			if (PaymentProcessingProfileName.equals(PaymentProcessingProfile)) {
				logger.info(
						"[clickEditPaymentProcessingProfile] Clicking on Edit Profile: " + PaymentProcessingProfile);
				cells.get(0).findElement(By.linkText(Constants.EDIT)).click();
				break;
			}
		}

	}

	/**
	 * This method is used to create a new Payment processing profile
	 * @param driver Webdriver object which will be used for operations
	 * @param PluginName String containing name of plugin to which payment processing profile to be created
	 */
	public void createNewPaymentProcessingProfile(WebDriver driver, String PluginName) {
		logger.info("[CreateNewPaymentProcessingProfile] Starts");
		WebElement div = findElementById(driver, prop, Constants.NEW_PAYMENT_PROCESSING_TABLE_ID);
		logger.info("[CreateNewPaymentProcessingProfile] div: " + div);
		WebElement table = div.findElement(By.xpath(".//tbody"));
		List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
		List<WebElement> cells = null;
		String PPPPluginName = null;
		logger.info("[CreateNewPaymentProcessingProfile] Plugin Name:" + PluginName);
		for (WebElement row : allRows) {
			cells = row.findElements(By.tagName(Constants.TD));
			PPPPluginName = cells.get(1).getText();
			logger.info(PPPPluginName);
			if (PPPPluginName.equals(PluginName)) {
				logger.info("[CreateNewPaymentProcessingProfile] Clicking on Add Profile: " + PluginName);
				cells.get(0).findElement(By.linkText(Constants.ADDPROFILE)).click();
				break;
			}
		}

	}

	/**
	 * This method is used to configure AVS Settings
	 * @param driver Webdriver object which will be used for operations
	 * @param AVSandCVNConfiguration String containing AVS and CVN configurations to be set
	 * @param DeclineAVSFlag String containing setup status of decline avs flag
	 * @param NoAVSMatch String containing setup of no AVS match
	 * @param AVSServiceNotAvailable String containing setup of AVS service unavailability
	 * @param PartialAVSMatch String containing setup of partial AVS match
	 */
	public void setAVSConfiguration(WebDriver driver, String AVSandCVNConfiguration, String DeclineAVSFlag,
			String NoAVSMatch, String AVSServiceNotAvailable, String PartialAVSMatch) {
		logger.info("[SetAVSConfiguration] Starts");
		clearElementByXPath(driver, prop, Constants.NEW_PPP_DECINE_AVS_FLAG);
		setElementValueByXPath(driver, prop, Constants.NEW_PPP_DECINE_AVS_FLAG, DeclineAVSFlag);
		logger.info("[SetAVSConfiguration] Element attribute value: "
				+ findElementByXPath(driver, prop, Constants.NEW_PPP_IGNOREAVSRESPONSE_CHECKBOX).getAttribute("class"));
		if (Constants.NO.equals(AVSandCVNConfiguration.toUpperCase())) {
			if (findElementByXPath(driver, prop, Constants.NEW_PPP_IGNOREAVSRESPONSE_CHECKBOX).getAttribute("class")
					.contains(Constants.CHECKBOX_UNCK)) {
				clickElementByXPath(driver, prop, Constants.NEW_PPP_IGNOREAVSRESPONSE_CHECKBOX);
			}
		}
		if (Constants.YES.equals(AVSandCVNConfiguration.toUpperCase())) {
			if (findElementByXPath(driver, prop, Constants.NEW_PPP_IGNOREAVSRESPONSE_CHECKBOX).getAttribute("class")
					.contains(Constants.CHECKBOX_CK)) {
				clickElementByXPath(driver, prop, Constants.NEW_PPP_IGNOREAVSRESPONSE_CHECKBOX);
			}
		}
		setElementValueByXPath(driver, prop, Constants.NEW_PPP_NO_AVS_MATCH, NoAVSMatch);
		setElementValueByXPath(driver, prop, Constants.NEW_PPP_AVS_SERVICE_NOT_AVAILABLE, AVSServiceNotAvailable);
		setElementValueByXPath(driver, prop, Constants.NEW_PPP_PARTIAL_AVS_MATCH, PartialAVSMatch);
		logger.info("[SetAVSConfiguration] Ends");
	}

	/**
	 * This method is used to configure CVN Settings
	 * @param driver Webdriver object which will be used for operations
	 * @param AVSandCVNConfiguration String containing AVS and CVN configurations to be set
	 * @param CSCNotSubmitted String containing setup status of CSC not submitted
	 * @param CSCNotSubmittedByCardholderBank String containing setup status of CSC not submitted by cardholder bank
	 * @param CSCServiceNotAvailable String containing setup status of CSC service unavailable
	 * @param CSCCheckFailed String containing setup status of CSC check failed
	 * @param NoCSCMatch String containing setup status of CSC not matched
	 */
	public void setCVNConfiguration(WebDriver driver, String AVSandCVNConfiguration, String CSCNotSubmitted,
			String CSCNotSubmittedByCardholderBank, String CSCServiceNotAvailable, String CSCCheckFailed,
			String NoCSCMatch) {
		logger.info("[SetCVNConfiguration]Starts");
		if (Constants.NO.equals(AVSandCVNConfiguration.toUpperCase())) {
			logger.info(findElementByXPath(driver, prop, Constants.NEW_PPP_IGNORECVNRESPONSE_CHECKBOX)
					.getAttribute("class"));
			if (findElementByXPath(driver, prop, Constants.NEW_PPP_IGNORECVNRESPONSE_CHECKBOX).getAttribute("class")
					.contains(Constants.CHECKBOX_UNCK)) {
				clickElementByXPath(driver, prop, Constants.NEW_PPP_IGNORECVNRESPONSE_CHECKBOX);
			}
		}
		if (Constants.YES.equals(AVSandCVNConfiguration.toUpperCase())) {
			if (findElementByXPath(driver, prop, Constants.NEW_PPP_IGNORECVNRESPONSE_CHECKBOX).getAttribute("class")
					.contains(Constants.CHECKBOX_CK)) {
				clickElementByXPath(driver, prop, Constants.NEW_PPP_IGNORECVNRESPONSE_CHECKBOX);
			}
		}
		setElementValueByXPath(driver, prop, Constants.NEW_PPP_CSC_NOT_SUBMITTED, CSCNotSubmitted);
		setElementValueByXPath(driver, prop, Constants.NEW_PPP_CSC_NOT_SUBMITTED_BY_CARDHOLDER_BANK,
				CSCNotSubmittedByCardholderBank);
		setElementValueByXPath(driver, prop, Constants.NEW_PPP_CSC_SERVICE_NOT_AVAILABLE, CSCServiceNotAvailable);
		setElementValueByXPath(driver, prop, Constants.NEW_PPP_CSC_CHECK_FAILED, CSCCheckFailed);
		setElementValueByXPath(driver, prop, Constants.NEW_PPP_NO_CSC_MATCH, NoCSCMatch);
		logger.info("[SetCVNConfiguration] Ends");
	}

	/**
	 * This method is used to configure Secure Acceptance Settings
	 * @param driver Webdriver object which will be used for operations
	 * @param ProfileID String containing profile Id to be setted up
	 * @param AcccessKey String containing access key to be setted up
	 * @param SecertKey String containing secret key to be setted up
	 * @param SuiteletUrl String containing suitelet url to be setted up
	 * @param WebstoreURL String containing webstore url to be used
	 */
	public void setSecureAcceptanceConfiguration(WebDriver driver, String ProfileID, String AcccessKey,
			String SecertKey, String SuiteletUrl, String WebstoreURL) {
		logger.info("[SetSecureAcceptanceConfiguration]Starts");
		setElementValueByXPath(driver, prop, Constants.NEW_PPP_SA_PROFILE_ID, ProfileID);
		sleep(SLEEP_TIMER_2);
		setElementValueByXPath(driver, prop, Constants.NEW_PPP_SA_ACCESS_KEY, AcccessKey);
		sleep(SLEEP_TIMER_2);
		setElementValueByXPath(driver, prop, Constants.NEW_PPP_SA_SECRET_KEY, SecertKey);
		sleep(SLEEP_TIMER_2);
		setElementValueByXPath(driver, prop, Constants.NEW_PPP_SA_SUITELET_URL, SuiteletUrl);
		sleep(SLEEP_TIMER_2);
		setElementValueByXPath(driver, prop, Constants.NEW_PPP_SA_WEBSTORE_URL, WebstoreURL);
		sleep(SLEEP_TIMER_2);
		logger.info("[SetSecureAcceptanceConfiguration]Ends");
	}

	/**
	 * This method is used to get the Payment methods that can be used to complete future transaction
	 * @param paymentMethods String containing payment method to be used
	 * @return Object containing payment methods used for transaction
	 */
	public Object[][] getPaymentMethods(String paymentMethods) {
		logger.info("[getPaymentMethods]Starts");
		Object PaymentMethods[][] = null;
		String Scenario = null;
		if (Constants.PAYMENT_METHOD_DATA.equals(paymentMethods)) {
			Scenario = Constants.PAYMENT_METHOD_NAME;
			logger.info("[getPaymentMethods] Scenario:" + Scenario);
		}
		logger.info("[getPaymentMethods] Scenario:" + (prop.getProperty(Scenario + Constants._SHEET_NAME)));
		PaymentMethods = FileUtil.getSheetData(XLSX_FILE_PATH, (prop.getProperty(Scenario + Constants._SHEET_NAME)));
		logger.info("[getPaymentMethods]Ends");
		return PaymentMethods;
	}

	/**
	 * This method is used to set the payment method for a transaction
	 * @param driver Webdriver object which will be used for operations
	 * @param paymentMethod Object containing all payment methods available
	 */
	public void setPaymentMethods(WebDriver driver, Object[][] paymentMethod) {
		logger.info("[setPaymentMethods] Starts");
		executor = (JavascriptExecutor) driver;
		int j = 0;
		int k;
		Actions actions = null;
		for (int i = 0; i < paymentMethod.length; i++) {
			k = -1;
			WebElement div = findElementById(driver, prop, Constants.NEW_PPP_PAYEMNT_METHOD);
			String div1 = findElementById(driver, prop, Constants.NEW_PPP_PAYEMNT_METHOD)
					.getAttribute("nlmultidropdown");
			logger.info("[setPaymentMethods] div1: " + div1);
			logger.info("[setPaymentMethods] div: " + div);
			WebElement table = div.findElement(By.xpath(".//tbody"));
			List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
			List<WebElement> cells = null;
			WebElement e = null;
			String Payment_Method, s = null;
			for (WebElement row : allRows) {
				cells = row.findElements(By.tagName(Constants.TD));
				Payment_Method = cells.get(0).getText();
				k = k + 1;
				if (paymentMethod[i][j].equals(Payment_Method)) {
					s = "row_" + div1 + "_" + k;
					e = row.findElement(By.id(s));
					actions = new Actions(driver);
					actions.keyDown(Keys.CONTROL).click(e).build().perform();
					break;
				}
			}
			actions.keyUp(Keys.CONTROL).build().perform();
			logger.info("[setPaymentMethods]Ends");
		}
	}

	/**
	 * This method is used to validate the selected payment method
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentMethodName String containing the payment method used for transaction
	 * @param Type String containing payment method type used for transaction
	 * @param Deposit String containing title of payment method deposit account
	 * @return
	 */
	public int checkPaymentMethod(WebDriver driver, String PaymentMethodName, String Type, String Deposit) {
		logger.info("[checkPaymentMethod]Starts");
		WebElement div = findElementById(driver, prop, Constants.PAYMENT_METHOD_TABLE_ID);
		logger.info("[checkPaymentMethod] div: " + div);
		WebElement table = div.findElement(By.xpath(".//tbody"));
		List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
		List<WebElement> cells = null;
		String PaymentMethod_name = null;
		int count = 0;
		logger.info("[checkPaymentMethod] PaymentMethod Name:" + PaymentMethodName);
		for (WebElement row : allRows) {
			cells = row.findElements(By.tagName(Constants.TD));
			PaymentMethod_name = cells.get(1).getText();
			logger.info(PaymentMethod_name);
			if (null != PaymentMethod_name) {
				if (PaymentMethod_name.equals(PaymentMethodName)) {
					logger.info("[checkPaymentMethod] clicking on add profile: " + PaymentMethodName);
					cells.get(0).findElement(By.linkText(Constants.EDIT)).click();
					logger.info("[checkPaymentMethod] element title: "
							+ findElementByXPath(driver, prop, Constants.PAYMENT_METHOD_TYPE).getAttribute("title"));
					sleep(SLEEP_TIMER_1);
					if (!((findElementByXPath(driver, prop, Constants.PAYMENT_METHOD_TYPE).getAttribute("title"))
							.equals(Type))) {
						sleep(SLEEP_TIMER_5);
						clearElementByXPath(driver, prop, Constants.PAYMENT_METHOD_TYPE);
						sleep(SLEEP_TIMER_5);
						setElementValueByXPath(driver, prop, Constants.PAYMENT_METHOD_TYPE, Type);
					}
					logger.info("[checkPaymentMethod] element class: "
							+ findElementByXPath(driver, prop, Constants.PAYMENT_METHOD_REQUIRELINELEVELDATA_CHECKBOX)
									.getAttribute("class"));
					if (findElementByXPath(driver, prop, Constants.PAYMENT_METHOD_REQUIRELINELEVELDATA_CHECKBOX)
							.getAttribute("class").contains(Constants.CHECKBOX_UNCK)) {
						clickElementByXPath(driver, prop, Constants.PAYMENT_METHOD_REQUIRELINELEVELDATA_CHECKBOX);
					}
					logger.info("[checkPaymentMethod] element class: "
							+ findElementByXPath(driver, prop, Constants.PAYMENT_METHOD_DISPLAYINWEBSITE_CHECKBOX)
									.getAttribute("class"));
					if (findElementByXPath(driver, prop, Constants.PAYMENT_METHOD_DISPLAYINWEBSITE_CHECKBOX)
							.getAttribute("class").contains(Constants.CHECKBOX_UNCK)) {
						clickElementByXPath(driver, prop, Constants.PAYMENT_METHOD_DISPLAYINWEBSITE_CHECKBOX);
					}
					if (!((findElementByXPath(driver, prop, Constants.PAYMENT_METHOD_DEPOSITACCOUNT)
							.getAttribute("title")).equals(Deposit))) {
						clearElementByXPath(driver, prop, Constants.PAYMENT_METHOD_DEPOSITACCOUNT);
						setElementValueByXPath(driver, prop, Constants.PAYMENT_METHOD_DEPOSITACCOUNT, Deposit);
					}
					sleep(SLEEP_TIMER_3);
					clickElementByXPath(driver, prop, Constants.PAYMENT_METHOD_SAVE);
					if (isAlertPresent(driver)) {
						driver.switchTo().alert().accept();
					}
					count = 1;
					break;
				}
			}
		}
		logger.info("[CheckPaymentMethod] Ends");
		return count;
	}

	/**
	 * This method is used to get all the OM and PM mapping methods
	 * @param OMPMMapping String containing data of OM PM mapping
	 * @return Object containing OMPM mapping methods
	 */
	public Object[][] getOMPMMappingMethod(String OMPMMapping) {
		logger.info("[getOMPMMappingMethod] Starts");
		Object OMPMMappingmethod[][] = null;
		String Scenario = null;
		if (Constants.OM_PM_MAPPING_DATA.equals(OMPMMapping)) {
			Scenario = Constants.OM_PM_MAPPING_NAME;
			logger.info("[getPaymentMethods] Scenario:" + Scenario);
		}
		logger.info("[getOMPMMappingMethod] Scenario:" + (prop.getProperty(Scenario + Constants._SHEET_NAME)));
		OMPMMappingmethod = FileUtil.getSheetData(XLSX_FILE_PATH, (prop.getProperty(Scenario + Constants._SHEET_NAME)));
		logger.info("[getOMPMMappingMethod] Ends");
		return OMPMMappingmethod;
	}

	/**
	 * This method is used to set the OM and PM mapping method
	 * @param driver Webdriver object which will be used for operations
	 * @param OMPMMappingmethod Object containing all OMPM mapping methods
	 */
	public void setOMPMMappingMethod(WebDriver driver, Object[][] OMPMMappingmethod) {
		logger.info("[setOMPMMappingMethod] Starts");
		executor = (JavascriptExecutor) driver;
		int j;
		for (int i = 0; i < OMPMMappingmethod.length; i++) {
			j = 0;
			clickElementByXPath(driver, prop, Constants.PAYMENT_METHOD_MAPPING_NEW);
			clickElementByXPath(driver, prop, Constants.PAYMENT_METHOD_MAPPING_PAYMENTMETHOD);
			setElementValueByXPath(driver, prop, Constants.PAYMENT_METHOD_MAPPING_PAYMENTMETHOD,
					OMPMMappingmethod[i][j].toString());
			clickElementByXPath(driver, prop, Constants.PAYMENT_METHOD_MAPPING_PAYMENTNAME);
			setElementValueByXPath(driver, prop, Constants.PAYMENT_METHOD_MAPPING_PAYMENTNAME,
					OMPMMappingmethod[i][++j].toString());
			clickElementByXPath(driver, prop, Constants.PAYMENT_METHOD_MAPPING_SAVE);
			clickElementByXPath(driver, prop, Constants.OM_PM_MAPPING_VIEW_RECORDS);
			logger.info("[setOMPMMappingMethod] Ends");
		}
	}

	/**
	 * This method is used to get all available payment cards
	 * @param paymentCards String containing data of all payment cards
	 * @return Object containing payment cards
	 */
	public Object[][] getpaymentCards(String paymentCards) {
		logger.info("[getpaymentCards] Starts");
		Object PaymentCards[][] = null;
		String Scenario = null;
		if (Constants.PAYMENT_CARDS_DATA.equals(paymentCards)) {
			Scenario = Constants.PAYMENT_CARD_NAME;
			logger.info("[getpaymentCards] Scenario:" + Scenario);
		}
		logger.info("[getpaymentCards] Scenario:" + (prop.getProperty(Scenario + Constants._SHEET_NAME)));
		PaymentCards = FileUtil.getSheetData(XLSX_FILE_PATH, (prop.getProperty(Scenario + Constants._SHEET_NAME)));
		logger.info("[getpaymentCards] Ends");
		return PaymentCards;
	}

	/**
	 * This method is used to select a payment method
	 * @param driver  Webdriver object which will be used for operations
	 * @param paymentDetails Object containing data of payment card 
	 */
	public void setPaymentCards(WebDriver driver, Object[][] paymentDetails) {
		logger.info("[setPaymentCards] Starts");
		executor = (JavascriptExecutor) driver;
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		Set<String> allWindows = driver.getWindowHandles();
		int j;
		Iterator<String> itr = allWindows.iterator();
		for (int i = 0; i < paymentDetails.length; i++) {
			j = 0;
			clickElementByXPath(driver, prop, Constants.CUSTOMER_PAYMENT_INSTRUMENT_NEW);
			mainWindow = driver.getWindowHandle();
			allWindows = driver.getWindowHandles();
			itr = allWindows.iterator();
			while (itr.hasNext()) {
				childWindow = itr.next();
				if (!mainWindow.equals(childWindow)) {
					switchToWindow(driver, childWindow);
					sleep(SLEEP_TIMER_1);
					setElementValueByXPath(driver, prop, Constants.PAYMENT_INSTRUMENT_NEW_TYPE_PAYMENT_METHOD,
							paymentDetails[i][j].toString());
					setElementValueByXPath(driver, prop, Constants.PAYMENT_INSTRUMENT_NEW_TYPE_PAYMENT_CARD_NUMBER,
							paymentDetails[i][++j].toString());
					setElementValueByXPath(driver, prop,
							Constants.PAYMENT_INSTRUMENT_NEW_TYPE_PAYMENT_CARD_EXPIRATIONDATE,
							paymentDetails[i][++j].toString());
					setElementValueByXPath(driver, prop, Constants.PAYMENT_INSTRUMENT_NEW_TYPE_PAYMENT_CARD_CARDBRAND,
							paymentDetails[i][++j].toString());
					clearElementByXPath(driver, prop,
							Constants.PAYMENT_INSTRUMENT_NEW_TYPE_PAYMENT_CARD_CARDHOLDERNAME);
					setElementValueByXPath(driver, prop,
							Constants.PAYMENT_INSTRUMENT_NEW_TYPE_PAYMENT_CARD_CARDHOLDERNAME,
							paymentDetails[i][++j].toString());
					scrollToBottom(driver);
					clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
					sleep(SLEEP_TIMER_5);
				}
			}
			switchToWindow(driver, mainWindow);
		}
		logger.info("[setPaymentCards] Ends");
	}

	/**
	 * This method is used to select the required payment processing profile
	 * @param driver Webdriver object which will be used for operations
	 * @param userIndex String containing index to specify user logging in to the instance
	 * @param PaymentProcessingProfile String containing payment processing profile to be selected to proceed further
	 * @param MerchantID String containing details of the merchant id for the profile
	 * @param ProcessorKey String containing details of the processor key for the profile
	 * @param TokenPaymentMethod Strinf to select token payment method setup
	 */
	public void setPaymentProcessingProfile(WebDriver driver, String userIndex, String PaymentProcessingProfile,
			String MerchantID, String ProcessorKey, String TokenPaymentMethod) {
		logger.info("[SetPaymentProcessingProfile] Starts");
		driver = setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, userIndex);
		clickEditPaymentProcessingProfile(driver, PaymentProcessingProfile);

		clearElementByXPath(driver, prop, Constants.PPP_MERCHANT_ID);
		setElementValueByXPath(driver, prop, Constants.PPP_MERCHANT_ID, MerchantID);
		clearElementByXPath(driver, prop, Constants.PPP_SECURITY_KEY);
		setElementValueByXPath(driver, prop, Constants.PPP_SECURITY_KEY, ProcessorKey);
		sleep(SLEEP_TIMER_3);
		findElementByXPath(driver, prop, Constants.PPP_SECURITY_KEY).sendKeys(Keys.DOWN, Keys.ENTER);
		logger.info("[SetPaymentProcessingProfile] checkbox selected: "
				+ findElementByXPath(driver, prop, Constants.REPLACE_PAYMENT_CARD_BY_TOKEN_STATE).isSelected());
		if (!findElementByXPath(driver, prop, Constants.REPLACE_PAYMENT_CARD_BY_TOKEN_STATE).isSelected()) {
			findElementByXPath(driver, prop, Constants.REPLACE_PAYMENT_CARD_BY_TOKEN).click();
		}
		clearElementByXPath(driver, prop, Constants.PAYMENT_CARD_TOKEN_PAYMENT_METHOD);
		setElementValueByXPath(driver, prop, Constants.PAYMENT_CARD_TOKEN_PAYMENT_METHOD, TokenPaymentMethod);
		clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		logger.info("[SetPaymentProcessingProfile] Ends");
	}

	/**
	 * This method is used to select the required payment processing profile for level 2,3 transactions
	 * @param driver Webdriver object which will be used for operations
	 * @param userIndex String containing index of user accessing the instance
	 * @param PaymentProcessingProfile String containing payment processing profile to be selected to proceed further
	 * @param MerchantID String containing details of the merchant id for the profile
	 * @param ProcessorKey String containing details of the processor key for the transaction
	 * @param ProcessorName String containing name of the processor used for transaction
	 * @param ProcessorLevel String containing level of transaction to occur
	 */
	public void setPaymentProcessingProfile(WebDriver driver, String userIndex, String PaymentProcessingProfile,
			String MerchantID, String ProcessorKey, String ProcessorName, String ProcessorLevel) {
		logger.info("[SetPaymentProcessingProfile] Starts");
		driver = setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, null);
		clickEditPaymentProcessingProfile(driver, PaymentProcessingProfile);
		clearElementByXPath(driver, prop, Constants.PPP_MERCHANT_ID);
		setElementValueByXPath(driver, prop, Constants.PPP_MERCHANT_ID, MerchantID);
		clearElementByXPath(driver, prop, Constants.PPP_SECURITY_KEY);
		setElementValueByXPath(driver, prop, Constants.PPP_SECURITY_KEY, ProcessorKey);
		sleep(SLEEP_TIMER_2);
		findElementByXPath(driver, prop, Constants.PPP_SECURITY_KEY).sendKeys(Keys.DOWN, Keys.ENTER);
		clearElementByXPath(driver, prop, Constants.PPP_PROCESSOR_NAME);
		setElementValueByXPath(driver, prop, Constants.PPP_PROCESSOR_NAME, ProcessorName);
		clickElementByXPath(driver, prop, Constants.PPP_PROCESSOR_LEVEL);
		if (Constants.PROCESSOR_LEVEL_2.equalsIgnoreCase(ProcessorLevel)) {
			clickElementByXPath(driver, prop, Constants.PPP_PROCESSOR_LEVEL_2);
		} else if (Constants.PROCESSOR_LEVEL_3.equalsIgnoreCase(ProcessorLevel)) {
			clickElementByXPath(driver, prop, Constants.PPP_PROCESSOR_LEVEL_3);
		}
		clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		logger.info("[SetPaymentProcessingProfile] Ends");
	}

	/**
	 * This method is used to select the required payment processing profile for fraud management and DM reject
	 * @param driver Webdriver object which will be used for operations
	 * @param userIndex String containing index of user accessing the instance
	 * @param PaymentProcessingProfile String containing payment processing profile to be selected to proceed further
	 * @param MerchantID String containing details of the merchant id for the profile
	 * @param ProcessorKey String containing details of the processor key for the transaction
	 * @param ProcessorName String containing name of the processor used for transaction
	 * @param ProcessorLevel String containing level of transaction to occur
	 * @param EnableFraudManagement String to enable fraud management setting
	 * @param DecisionManagerReject String to set DM scenario as reject
	 */
	public void setPaymentProcessingProfile(WebDriver driver, String userIndex, String PaymentProcessingProfile,
			String MerchantID, String ProcessorKey, String ProcessorLevel, String EnableFraudManagement,
			String DecisionManagerReject) {
		logger.info("[SetPaymentProcessingProfile] Starts");
		driver = setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, null);
		clickEditPaymentProcessingProfile(driver, PaymentProcessingProfile);
		clearElementByXPath(driver, prop, Constants.PPP_MERCHANT_ID);
		setElementValueByXPath(driver, prop, Constants.PPP_MERCHANT_ID, MerchantID);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("nlapiSetFieldText('custrecord_cs_pymt_ext_checkout_sec_key','" + ProcessorKey + "');");
		if (Constants.YES.equalsIgnoreCase(EnableFraudManagement)) {
			if (!findElementByXPath(driver, prop, Constants.PPP_FRAUD_MANAGEMENT).isSelected()) {
				js.executeScript("nlapiSetFieldValue('custrecord_cs_pymt_enable_fm','T');");
			}
		} else {
			if (findElementByXPath(driver, prop, Constants.PPP_FRAUD_MANAGEMENT).isSelected()) {
				js.executeScript("nlapiSetFieldValue('custrecord_cs_pymt_enable_fm','F');");
			}
		}
		clearElementByXPath(driver, prop, Constants.PPP_DECISION_MANAGER_REJECT);
		setElementValueByXPath(driver, prop, Constants.PPP_DECISION_MANAGER_REJECT, DecisionManagerReject);
		clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		logger.info("[SetPaymentProcessingProfile] Ends");
	}

	/**
	 * This method is used to select the required payment processing profile for t
	 * @param driver Webdriver object which will be used for operations
	 * @param userIndex String containing index of user accessing the instance
	 * @param PaymentProcessingProfile String containing payment processing profile to be selected to proceed further
	 * @param MerchantID String containing details of the merchant id for the profile
	 * @param ProcessorKey String containing details of the processor key for the transaction
	 * @param ProcessorName String containing name of the processor used for transaction
	 * @param ProcessorLevel String containing level of transaction to occur
	 * @param Token String to decide if token scenarios are to be handled
	 * @param TokenPaymentMethod String to set the token payment method
	 */
	public void setPaymentProcessingProfile(WebDriver driver, String userIndex, String PaymentProcessingProfile,
			String MerchantID, String ProcessorKey, String ProcessorName, String ProcessorLevel, String Token,
			String TokenPaymentMethod) {
		logger.info("[setPaymentProcessingProfile] Starts");
		driver = setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, null);
		clickEditPaymentProcessingProfile(driver, PaymentProcessingProfile);
		// Handling MID Details
		clearElementByXPath(driver, prop, Constants.PPP_MERCHANT_ID);
		setElementValueByXPath(driver, prop, Constants.PPP_MERCHANT_ID, MerchantID);
		// Handling Processor Name
		setElementValueByXPath(driver, prop, Constants.PPP_PROCESSOR_NAME, ProcessorName);
		// Handling Processor Level
		clickElementByXPath(driver, prop, Constants.PPP_PROCESSOR_LEVEL);
		if (Constants.PROCESSOR_LEVEL_2.equalsIgnoreCase(ProcessorLevel)) {
			clickElementByXPath(driver, prop, Constants.PPP_PROCESSOR_LEVEL_2);
		} else if (Constants.PROCESSOR_LEVEL_3.equalsIgnoreCase(ProcessorLevel)) {
			clickElementByXPath(driver, prop, Constants.PPP_PROCESSOR_LEVEL_3);
		}
		// Handling Security Key
		clearElementByXPath(driver, prop, Constants.PPP_SECURITY_KEY);
		setElementValueByXPath(driver, prop, Constants.PPP_SECURITY_KEY, ProcessorKey);
		sleep(SLEEP_TIMER_3);
		findElementByXPath(driver, prop, Constants.PPP_SECURITY_KEY).sendKeys(Keys.DOWN, Keys.ENTER);

		// Handling Token
		if (Constants.YES.equalsIgnoreCase(Token)) {
			if (!findElementByXPath(driver, prop, Constants.REPLACE_PAYMENT_CARD_BY_TOKEN_STATE).isSelected()) {
				findElementByXPath(driver, prop, Constants.REPLACE_PAYMENT_CARD_BY_TOKEN).click();
			}
			clearElementByXPath(driver, prop, Constants.PAYMENT_CARD_TOKEN_PAYMENT_METHOD);
			setElementValueByXPath(driver, prop, Constants.PAYMENT_CARD_TOKEN_PAYMENT_METHOD, TokenPaymentMethod);
		} else {
			if (findElementByXPath(driver, prop, Constants.REPLACE_PAYMENT_CARD_BY_TOKEN_STATE).isSelected()) {
				findElementByXPath(driver, prop, Constants.REPLACE_PAYMENT_CARD_BY_TOKEN).click();
			}
		}
		clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		logger.info("[setPaymentProcessingProfile] Ends");
	}

	/**
	 * This method is used to execute sleep time as per user
	 * @param userIndex String containing index to specify user accessing instance
	 */
	public void executionSleepTimer(String userIndex) {
		logger.info("[executionSleepTimer] Starts");
		int sleepTimer = Integer.parseInt(userIndex);
		logger.info("[executionSleepTimer] sleepTimer: " + sleepTimer + "s");
		for (int i = 0; i < sleepTimer; i++) {
			sleep(SLEEP_TIMER_1);
			sleep(SLEEP_TIMER_1);
		}
		logger.info("[executionSleepTimer] Ends");
	}

	// Common ERP Functions

	/**
	 * This common ERP method is used to execute capture service
	 * @param driver Webdriver object which will be used for operations
	 * @param Items String containing details of items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param RequireEBCCheck String to set if validation from EBC portal is required of not
	 * @param ReasonCode String containing reason code of the transaction happened
	 * @param DecisionMsg String containing decision manager message of the transaction
	 * @param TestScenario String to specify the scenario of the transaction happening
	 * @param screenShotPath String containing path directory where screenshot is to be saved
	 * @param screenShotModulePath String containing path of screenshot module folder
	 * @throws Exception Handles any exception that may happen
	 */
	public void executeCapture(WebDriver driver, String Items, String HasMultipleItem, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario, String screenShotPath,
			String screenShotModulePath) throws Exception {
		logger.info("[executeCapture] Starts");
		// findElementByXPath(driver, prop,
		// Constants.SEARCH_BAR).sendKeys(authOrderId, Keys.ENTER);
		String captureId = null;
		screenShotPath = getScreenshotsPath(screenShotModulePath, Constants.CAPTURE);
		executor = (JavascriptExecutor) driver;

		// SALES_ORDER_SECONDARY_APPROVE
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_APPROVE);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_FULFILL);
		takeScreenShot(driver, screenShotPath, Constants.ORDER_FULLFIL);
		setElementValueByXPath(driver, prop, Constants.ITEM_FULFILLMENT_STATUS, Constants.ORDER_STATUS_SHIPPED);
		Object itemDetails[][] = null;
		itemDetails = getItemDetails(Items);
		if (Constants.YES.equals(HasMultipleItem.toUpperCase())) {
			for (int i = 0; i < itemDetails.length; i++) {
				setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, (i + 1),
						Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
			}
		} else {
			setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, 1,
					Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
		}
		takeScreenShot(driver, screenShotPath, Constants.ORDER_FULLFIL);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_BILL);
		sleep(SLEEP_TIMER_5);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		sleep(SLEEP_TIMER_2);
		takeScreenShot(driver, screenShotPath, Constants.ORDER_CAPTURE);
		// writeCurrentTransactionUrl(Constants.CAPTURE_ORDERID_URL,
		// getCurrentUrl(driver));
		scrollToBottom(driver);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		captureId = getOrderId(driver, Constants.CAPTURE_AUTHORIZATION);
		renameFolder(screenShotPath, Constants.CAPTURE, captureId);

		if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.PROCESS_TYPE_REST))) {
			pnReferenceNumber = checkRestActuals(driver, Constants.CAPTURE_AUTHORIZATION, DecisionMsg, TestScenario);
		} else {
			pnReferenceNumber = checkActuals(driver, Constants.CAPTURE_AUTHORIZATION,
					prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		}

		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			checkTransactionInEBC(driver, pnReferenceNumber);
		}
		addLogToTestNGReport(Constants.CAPTURE, getCurrentUrl(driver), captureId);
		addLogToTestNGReport(Constants.CAPTURE, pnReferenceNumber);
		logger.info("[executeCapture] Ends");
	}

	/**
	 * This common ERP method is used to execute refund service
	 * @param driver Webdriver object which will be used for operations
	 * @param Location String containing Sales order location which is to be refunded
	 * @param RequireEBCCheck String to set if validation from EBC portal is required of not
	 * @param ReasonCode String containing reason code of the transaction happened
	 * @param DecisionMsg String containing decision manager message of the transaction
	 * @param TestScenario String to specify the scenario of the transaction happening
	 * @param screenShotPath String containing path directory where screenshot is to be saved
	 * @param screenShotModulePath String containing path of screenshot module folder
	 * @throws Exception Handles any exception that may happen
	 */
	public void executeRefund(WebDriver driver, String Location, String RequireEBCCheck, String ReasonCode,
			String DecisionMsg, String TestScenario, String screenShotPath, String screenShotModulePath)
			throws Exception {
		logger.info("[executeRefund] Starts");
		screenShotPath = getScreenshotsPath(screenShotModulePath, Constants.REFUND);
		String refundId = null;

		clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
		sleep(SLEEP_TIMER_2);
		setElementValueByXPath(driver, prop, Constants.SALES_ORDER_LOCATION, Location);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		sleep(SLEEP_TIMER_2);

		scrollToBottom(driver);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		refundId = getOrderId(driver, Constants.REFUND);
		renameFolder(screenShotPath, Constants.REFUND, refundId);

		if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.PROCESS_TYPE_REST))) {
			pnReferenceNumber = checkRestActuals(driver, Constants.REFUND, DecisionMsg, TestScenario);
		} else {
			pnReferenceNumber = checkActuals(driver, Constants.REFUND, prop.getProperty(Constants.DECISION_MESSAGE),
					prop.getProperty(Constants.REASON_CODE), prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH),
					ReasonCode, DecisionMsg, TestScenario);
		}
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			checkTransactionInEBC(driver, pnReferenceNumber);
		}
		addLogToTestNGReport(Constants.REFUND, getCurrentUrl(driver), refundId);
		addLogToTestNGReport(Constants.REFUND, pnReferenceNumber);
		logger.info("[executeRefund] Ends");
	}

	/**
	 * This common ERP method is used to execute customer refund service
	 * @param driver Webdriver object which will be used for operations
	 * @param Items String containing details of items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param Location String containing Sales order location which is to be refunded
	 * @param RequireEBCCheck String to set if validation from EBC portal is required of not
	 * @param ReasonCode String containing reason code of the transaction happened
	 * @param DecisionMsg String containing decision manager message of the transaction
	 * @param TestScenario String to specify the scenario of the transaction happening
	 * @param screenShotPath String containing path directory where screenshot is to be saved
	 * @param screenShotModulePath String containing path of screenshot module folder
	 * @throws Exception Handles any exception that may happen
	 */
	public void executeCustomerRefund(WebDriver driver, String Items, String HasMultipleItem, String Location,
			String RequireEBCCheck, String ReasonCode, String DecisionMsg, String TestScenario, String screenShotPath,
			String screenShotModulePath) throws Exception {
		logger.info("[executeCustomerRefund] Starts");
		screenShotPath = getScreenshotsPath(screenShotModulePath, Constants.CUSTOMER_REF);
		int i = 0;
		String refundId = null;
		Object itemDetails[][] = null;

		executor = (JavascriptExecutor) driver;
		clickElementByXPath(driver, prop, Constants.CASH_SALE_AUTHORIZE_RETURN);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		clickElementByXPath(driver, prop, Constants.CASH_SALE_APPROVE_RETURN);
		clickElementByXPath(driver, prop, Constants.CASH_SALE_RECEIVE_RETURN);
		itemDetails = getItemDetails(Items);
		if (Constants.YES.equals(HasMultipleItem.toUpperCase())) {
			for (i = 0; i < itemDetails.length; i++) {
				setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, (i + 1),
						Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
			}
		} else {
			setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, 1,
					Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
		}
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		checkUrlTitle(driver, prop, Constants.ITEM_RECEIPT_PAGE_TITLE);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.REFUND_CREATED_FROM);

		clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
		setElementValueByXPath(driver, prop, Constants.SALES_ORDER_LOCATION, Location);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.CASH_SALE_REFUND);
		checkUrlTitle(driver, prop, Constants.CUSTOMER_REFUND_PAGE_TITLE);
		sleep(SLEEP_TIMER_10);
		if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.VIRTUAL_MACHINE_RUN))) {
			sleep(SLEEP_TIMER_1);
			sleep(SLEEP_TIMER_1);
		}
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.TRANSACTIONS_REFUND_METHOD_TAB);

		scrollToBottom(driver);
		takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		refundId = getOrderId(driver, Constants.REFUND);
		renameFolder(screenShotPath, Constants.CUSTOMER_REF, refundId);

		if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.PROCESS_TYPE_REST))) {
			pnReferenceNumber = checkRestActuals(driver, Constants.REFUND, DecisionMsg, TestScenario);
		} else {
			pnReferenceNumber = checkActuals(driver, Constants.REFUND, prop.getProperty(Constants.DECISION_MESSAGE),
					prop.getProperty(Constants.REASON_CODE), prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH),
					ReasonCode, DecisionMsg, TestScenario);
		}
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			checkTransactionInEBC(driver, pnReferenceNumber);
		}
		addLogToTestNGReport(Constants.REFUND, getCurrentUrl(driver), refundId);
		addLogToTestNGReport(Constants.REFUND, pnReferenceNumber);
		logger.info("[executeCustomerRefund] Ends");
	}

	/**
	 * This common ERP method is used to execute void service
	 * @param driver Webdriver object which will be used for operations
	 * @param RequireEBCCheck String to set if validation from EBC portal is required of not
	 * @param ReasonCode String containing reason code of the transaction happened
	 * @param DecisionMsg String containing decision manager message of the transaction
	 * @param TestScenario String to specify the scenario of the transaction happening
	 * @param screenShotPath String containing path directory where screenshot is to be saved
	 * @param screenShotModulePath String containing path of screenshot module folder
	 * @throws Exception Handles any exception that may happen
	 */
	public void executeVoid(WebDriver driver, String RequireEBCCheck, String ReasonCode, String DecisionMsg,
			String TestScenario, String screenShotPath, String screenShotModulePath) throws Exception {
		logger.info("[executeVoid] Starts");
		String voidId = null;
		screenShotPath = getScreenshotsPath(screenShotModulePath, Constants.VOID);

		clickElementByXPath(driver, prop, Constants.SALES_ORDER_CANCEL_ORDER);
		switchToFrame(driver, Constants.THREE);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_CANCEL_YES_BUTTON);
		sleep(SLEEP_TIMER_2);

		scrollToBottom(driver);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		scrollToBottom(driver);
		takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		voidId = getOrderId(driver, Constants.VOID_AUTHORIZATION);
		renameFolder(screenShotPath, Constants.VOID, voidId);

		if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.PROCESS_TYPE_REST))) {
			pnReferenceNumber = checkRestActuals(driver, Constants.VOID_AUTHORIZATION, DecisionMsg, TestScenario);
		} else {
			pnReferenceNumber = checkActuals(driver, Constants.VOID_AUTHORIZATION,
					prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		}
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			checkTransactionInEBC(driver, pnReferenceNumber);
		}
		addLogToTestNGReport(Constants.VOID, getCurrentUrl(driver), voidId);
		addLogToTestNGReport(Constants.VOID, pnReferenceNumber);
		logger.info("[executeVoid] Ends");
	}

	/**
	 * This common ERP method is used to execute delayed shipment order
	 * @param driver Webdriver object which will be used for operations
	 * @param Items String containing details of items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param RequireEBCCheck String to set if validation from EBC portal is required of not
	 * @param ReasonCode String containing reason code of the transaction happened
	 * @param DecisionMsg String containing decision manager message of the transaction
	 * @param TestScenario String to specify the scenario of the transaction happening
	 * @param screenShotPath String containing path directory where screenshot is to be saved
	 * @param screenShotModulePath String containing path of screenshot module folder
	 * @throws Exception Handles any exception that may happen
	 */
	public void executeDelayedShipment(WebDriver driver, String Items, String HasMultipleItem, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario, String screenShotPath,
			String screenShotModulePath) throws Exception {
		logger.info("[executeDelayedShipment] Starts");
		screenShotPath = getScreenshotsPath(screenShotModulePath, Constants.DELAY_SHIPMENT);
		String saleId = null;

		executor = (JavascriptExecutor) driver;
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_APPROVE);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_FULFILL);
		takeScreenShot(driver, screenShotPath, Constants.ORDER_FULLFIL);
		setElementValueByXPath(driver, prop, Constants.ITEM_FULFILLMENT_STATUS, Constants.ORDER_STATUS_SHIPPED);
		Object itemDetails[][] = null;
		itemDetails = getItemDetails(Items);
		if (Constants.YES.equals(HasMultipleItem.toUpperCase())) {
			for (int i = 0; i < itemDetails.length; i++) {
				setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, (i + 1),
						Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
			}
		} else {
			setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, 1,
					Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
		}
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_BILL);
		sleep(SLEEP_TIMER_2);
		// Cash Sale Setting Date
		clickElementByXPath(driver, prop, Constants.CASH_SALE_DATE_FIELD);
		clickElementByXPath(driver, prop, Constants.CASH_SALE_DATE_FIELD_RIGHT, 3);
		clickElementByXPath(driver, prop, Constants.CASH_SALE_DATE_FIELD_SET);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);

		scrollToBottom(driver);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		scrollToBottom(driver);
		takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		saleId = getOrderId(driver, Constants.STRING_SALE);
		renameFolder(screenShotPath, Constants.DELAY_SHIPMENT, saleId);

		if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.PROCESS_TYPE_REST))) {
			pnReferenceNumber = checkRestActuals(driver, Constants.STRING_SALE, ReasonCode, TestScenario);
		} else {
			pnReferenceNumber = checkActuals(driver, Constants.STRING_SALE,
					prop.getProperty(Constants.SALE_DECISION_MESSAGE), prop.getProperty(Constants.SALE_REASON_CODE),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		}
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			checkTransactionInEBC(driver, pnReferenceNumber);
		}
		addLogToTestNGReport(Constants.STRING_SALE, getCurrentUrl(driver), saleId);
		addLogToTestNGReport(Constants.STRING_SALE, pnReferenceNumber);
		logger.info("[executeDelayedShipment] Ends");
	}

	/**
	 * This common ERP method is used to execute multiple capture service
	 * @param driver Webdriver object which will be used for operations
	 * @param Items String containing details of items to be ordered
	 * @param HasMultipleItem String to check if order contains multiple items
	 * @param RequireEBCCheck String to set if validation from EBC portal is required of not
	 * @param ReasonCode String containing reason code of the transaction happened
	 * @param DecisionMsg String containing decision manager message of the transaction
	 * @param TestScenario String to specify the scenario of the transaction happening
	 * @param screenShotPath String containing path directory where screenshot is to be saved
	 * @param screenShotModulePath String containing path of screenshot module folder
	 * @throws Exception Handles any exception that may happen
	 */
	public void executeMultipleCapture(WebDriver driver, String Items, String HasMultipleItem, String RequireEBCCheck,
			String ReasonCode, String DecisionMsg, String TestScenario, String screenShotPath,
			String screenShotModulePath) throws Exception {
		logger.info("[executeMultipleCapture] Starts");
		screenShotPath = getScreenshotsPath(screenShotModulePath, Constants.MULTI_CAPTURE);
		String captureId = null;
		String captureId2 = null;
		Object itemDetails[][] = null;
		Reporter.log("Multiple Item data: " + HasMultipleItem);
		executor = (JavascriptExecutor) driver;
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_APPROVE);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_FULFILL);
		scrollToBottom(driver);
		takeScreenShot(driver, screenShotPath, Constants.ORDER_FULLFIL);
		setElementValueByXPath(driver, prop, Constants.ITEM_FULFILLMENT_STATUS, Constants.ORDER_STATUS_SHIPPED);
		scrollToBottom(driver);
		itemDetails = getItemDetails(Items);
		if (Constants.YES.equals(HasMultipleItem.toUpperCase())) {
			setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, 1,
					Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
		}
		for (int i = 0; i < itemDetails.length - 1; i++) {
			clickElementByXPath(driver, prop, Constants.SALES_ORDER_FULFILL_CHECKBOX_PREFIX, (i + 2),
					Constants.SALES_ORDER_FULFILL_CHECKBOX_SUFFIX);
		}
		takeScreenShot(driver, screenShotPath, Constants.ORDER_FULLFIL);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_BILL);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);

		scrollToBottom(driver);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		scrollToBottom(driver);
		takeScreenShot(driver, screenShotPath, Constants.ORDER_CAPTURE);
		captureId = getOrderId(driver, Constants.CAPTURE_AUTHORIZATION);

		if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.PROCESS_TYPE_REST))) {
			pnReferenceNumber = checkRestActuals(driver, Constants.CAPTURE_AUTHORIZATION, DecisionMsg, TestScenario);
		} else {
			pnReferenceNumber = checkActuals(driver, Constants.CAPTURE_AUTHORIZATION,
					prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		}
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			checkTransactionInEBC(driver, pnReferenceNumber);
		}
		addLogToTestNGReport(Constants.CAPTURE, getCurrentUrl(driver), captureId);
		addLogToTestNGReport(Constants.CAPTURE, pnReferenceNumber);
		sleep(SLEEP_TIMER_2);
		// follow-on capture
		executor.executeScript(prop.getProperty(Constants.SALES_ORDER_CREATED_FROM));
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_FULFILL);
		takeScreenShot(driver, screenShotPath, Constants.ORDER_FULLFIL);
		setElementValueByXPath(driver, prop, Constants.ITEM_FULFILLMENT_STATUS, Constants.ORDER_STATUS_SHIPPED);
		for (int i = 0; i < itemDetails.length - 1; i++) {
			setLineItemValue(driver, Constants.NLAPI_LINE_ITEM_LOCATION_PREFIX, (i + 1),
					Constants.NLAPI_LINE_ITEM_LOCATION_SUFFIX);
		}
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_BILL);
		sleep(SLEEP_TIMER_5);
		clickElementByXPath(driver, prop, Constants.SALES_ORDER_SAVE_BUTTON);
		sleep(SLEEP_TIMER_2);

		scrollToBottom(driver);
		clickElementByXPath(driver, prop, Constants.TRANSACTIONS_BILLING_TAB);
		sleep(SLEEP_TIMER_2);
		clickElementByXPath(driver, prop, Constants.TRANSACTIONS_PAYMENT_EVENTS_TAB);

		scrollToBottom(driver);
		takeScreenShot(driver, screenShotPath, Constants.TRANSACTION_DETAIL);
		captureId2 = getOrderId(driver, Constants.CAPTURE_AUTHORIZATION);
		renameFolder(screenShotPath, Constants.MULTI_CAPTURE, captureId2);

		if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.PROCESS_TYPE_REST))) {
			pnReferenceNumber = checkRestActuals(driver, Constants.CAPTURE_AUTHORIZATION, DecisionMsg, TestScenario);
		} else {
			pnReferenceNumber = checkActuals(driver, Constants.CAPTURE_AUTHORIZATION,
					prop.getProperty(Constants.DECISION_MESSAGE), prop.getProperty(Constants.REASON_CODE),
					prop.getProperty(Constants.PNREFERENCE_NUMBER_XPATH), ReasonCode, DecisionMsg, TestScenario);
		}
		if (Constants.YES.equals(RequireEBCCheck.toUpperCase())) {
			checkTransactionInEBC(driver, pnReferenceNumber);
		}
		addLogToTestNGReport(Constants.CAPTURE, getCurrentUrl(driver), captureId2);
		addLogToTestNGReport(Constants.CAPTURE, pnReferenceNumber);
		logger.info("[executeMultipleCapture] Ends");
	}

	/**
	 * This method is used to check if any alert is present in webpage and accept if any
	 * @param driver Webdriver object which will be used for operations
	 */
	public void ifAlertPresentAccept(WebDriver driver) {
		logger.info("[ifAlertPresentAccept] Starts");
		if (isAlertPresent(driver)) {
			acceptAlert(driver);
		}
		logger.info("[ifAlertPresentAccept] Ends");
	}

	/**
	 * This method is used to check if value of a string is null or empty
	 * @param value String containing value of string to be checked
	 * @return Boolean which returns value based on string that is checked
	 */
	public boolean isValueNotNull(String value) {
		boolean flag = false;
		if (value != null && value != "") {
			flag = true;
		}
		return flag;
	}

	/**
	 * This method is used to validate if a given webelement exists or not
	 * @param element Webelemet which is to be validated
	 * @return Boolean that returns status after validating the passed webelement
	 */
	public boolean isElementNotNull(WebElement element) {
		boolean flag = false;
		if (element != null) {
			flag = true;
		}
		return flag;
	}

	/**
	 * This method is used to get path of failed testcases
	 * @return String containing path of failed testcases
	 */
	public String getFaildTestCasePath() {
		String faildTestCasePath = System.getProperty(Constants.USER_DIR)
				+ prop.getProperty(Constants.BASE_PATH_FAILEDTESTCASE_XML_DIRECTORY);
		return faildTestCasePath;
	}

	/**
	 * This method is used to set value to a textbox
	 * @param driver Webdriver object which will be used for operations
	 * @param prop Properties file which is configured for all xpath, user related data
	 * @param xPath xpath of the textbox webElement
	 * @param value String containing value to be set to textbox
	 */
	public void setTextBoxFieldValue(WebDriver driver, Properties prop, String xPath, String value) {
		WebElement element = null;
		logger.info("[setTextBoxFieldValue] xPath=" + xPath);
		if (isValueNotNull(xPath)) {
			element = findElementByXPath(driver, prop, xPath);
			if (null != element) {
				element.clear();
				element.sendKeys(value);
			} else {
				logger.info("[setTextBoxFieldValue] Not able to set the value, element is null for xPath=" + xPath);
			}
		} else {
			logger.info("[setTextBoxFieldValue] value is null for given xPath:" + xPath);
		}
	}

	/**
	 * THis method is used to clear items in cart in web store
	 * @param driver Webdriver object which will be used for operations
	 * @throws InterruptedException This method is used to handle any exceptions that may happen
	 */
	public void clearCart(WebDriver driver) throws InterruptedException {
		logger.info("[clearCart] begins");
		sleep(SLEEP_TIMER_2);
		String numberOfItemsInCart = findElementByClassName(driver, prop, Constants.SA_CART_ITEMS_COUNT).getText();
		if (numberOfItemsInCart.equals("0") == false) {
			clickElementByXPath(driver, prop, Constants.SA_CART);
			sleep(SLEEP_TIMER_1);
			clickElementByXPath(driver, prop, Constants.SA_CART_VIEW_CART);
			sleep(SLEEP_TIMER_10);
			List<WebElement> cartItems = findElementsByClassName(driver, Constants.SA_CART_REMOVE_ITEM_BUTTON);
			int i = cartItems.size() / 2;
			logger.info("[clearCart] Cart items Recorded");
			while (i > 0) {
				sleep(SLEEP_TIMER_1);
				try {
					clickElementByClassName(driver, prop, Constants.SA_CART_REMOVE_ITEM_BUTTON);
				} catch (Exception e) {
				}
				i--;
			}
			logger.info("[clearCart] Cart cleared successfully");
		} else {
			logger.info("[clearCart] Cart is already empty");
		}
		logger.info("[clearCart] Ends");
	}

	/**
	 * This method is used to find a webElement using class name
	 * @param driver Webdriver object which will be used for operations
	 * @param prop Properties file which is configured for all xpath, user related data
	 * @param className String containing name of class used to fetch the webElement
	 * @return Webelement found using the classname
	 */
	public WebElement findElementByClassName(WebDriver driver, Properties prop, String className) {
		logger.info("[findElementByClassName] Starts");
		WebElement element = null;
		String classNameInProp = null;
		if (null != className) {
			classNameInProp = prop.getProperty(className);
			logger.info("[findElementByClassName] element Id=" + classNameInProp);
			if (null != classNameInProp) {
				element = driver.findElement(By.className(classNameInProp));
			} else {
				logger.info("[findElementByClassName] Id is null.");
			}
		}
		logger.info("[findElementByClassName] Ends");
		return element;
	}

	/**
	 * This method is used to click a webElement using class name
	 * @param driver Webdriver object which will be used for operations
	 * @param prop Properties file which is configured for all xpath, user related data
	 * @param className String containing name of class used to click the webElement
	 */
	public void clickElementByClassName(WebDriver driver, Properties prop, String className) {
		logger.info("[clickElementByClassName] Starts");
		WebElement element = null;
		logger.info("[clickElementByClassName] className=" + className);
		if (null != className) {
			element = findElementByClassName(driver, prop, className);
			if (null != element) {
				logger.info("[clickElementByClassName] before click className=" + className);
				element.click();
				logger.info("[clickElementByClassName] after click className=" + className);
			} else {
				logger.info("[clickElementByClassName] Not able to set the value element is Null for className="
						+ className);
			}
		} else {
			logger.info("[clickElementByXPath] xPath is Null for given className:" + className);
		}
		logger.info("[clickElementByClassName] Ends");
	}

	/**
	 * This method is used to get order id from payment events
	 * @param driver Webdriver object which will be used for operations
	 * @param paymentProcessingProfile String containing Payment processing profile configured for transaction
	 * @return String containing order id of transaction
	 */
	public String getOrderIdFromPaymentEvents(WebDriver driver, String paymentProcessingProfile) {
		logger.info("[getOrderIdFromPaymentEvents] Starts");
		WebElement div = findElementById(driver, prop, Constants.PAYMENT_EVENTS_TABLE_ID);
		logger.info("[getOrderIdFromPaymentEvents] div: " + div);
		WebElement table = div.findElement(By.xpath(".//*"));
		List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
		List<WebElement> cells = null;
		String paymentProcessingProfileName = null;
		String paymentReason = null;
		String orderId = null;
		for (WebElement row : allRows) {
			cells = row.findElements(By.tagName(Constants.TD));
			paymentProcessingProfileName = cells.get(5).getText();
			paymentReason = cells.get(9).getText();
			if ((paymentProcessingProfileName.equalsIgnoreCase(paymentProcessingProfile))
					&& ((Constants.STRING_VERIFICATION_REJECTION.equalsIgnoreCase(paymentReason))
							|| (Constants.STRING_GENERAL_REJECT.equalsIgnoreCase(paymentReason))
							|| (Constants.STRING_EXTERNAL_FRAUD_REJECTION.equalsIgnoreCase(paymentReason)))) {
				orderId = cells.get(3).getText();
				orderId = StringUtils.substringAfterLast(orderId, Constants.HASH);
				break;
			}
		}
		logger.info("[getOrderIdFromPaymentEvents] Ends");
		return orderId;
	}

	/**
	 * This method is used to get result of transaction happened
	 * @param driver Webdriver object which will be used for operations
	 * @param paymentEventName String containing payment event name 
	 * @return String containing result of transaction
	 */
	public String getTransactionResult(WebDriver driver, String paymentEventName) {
		logger.info("[getTransactionResult] Starts");
		WebElement table = findElementById(driver, prop, Constants.PAYMENT_EVENT_TABLE_ID);
		List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
		String TransactionResult = null;
		List<WebElement> cells = null;
		String eventName = null;
		for (WebElement row : allRows) {
			cells = null;
			cells = row.findElements(By.tagName(Constants.TD));
			logger.info(cells.size());
			if (cells.size() > 1) {
				eventName = cells.get(2).getText();
				logger.info(cells.get(5).getText());
				if (eventName.equals(paymentEventName)) {
					if (!cells.get(5).getText().equals(Constants.PENDING)) {
						TransactionResult = cells.get(5).getText();
						logger.info(TransactionResult);
						break;
					}
				}
			}
		}
		logger.info("[getTransactionResult] Ends");
		return TransactionResult;
	}

	/**
	 * This method is used to get reason code of transaction happened
	 * @param driver Webdriver object which will be used for operations
	 * @param paymentEventName String containing payment event name 
	 * @return String containing reason code of transaction
	 */
	public String getTransactionReason(WebDriver driver, String paymentEventName) {
		logger.info("[getTransactionReason] Starts");
		WebElement table = findElementById(driver, prop, Constants.PAYMENT_EVENT_TABLE_ID);
		List<WebElement> allRows = table.findElements(By.tagName(Constants.TR));
		String TransactionReason = null;
		List<WebElement> cells = null;
		String eventName = null;
		for (WebElement row : allRows) {
			cells = null;
			cells = row.findElements(By.tagName(Constants.TD));
			if (cells.size() > 1) {
				eventName = cells.get(2).getText();
				if (eventName.equals(paymentEventName)) {
					if (!cells.get(5).getText().equals(Constants.PENDING)) {
						TransactionReason = cells.get(6).getText();
						break;
					}
				}
			}
		}
		logger.info("[getTransactionReason] Ends");
		return TransactionReason;
	}

	/**
	 * This method is used validate AVS code of transaction
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param reasonCodeXpath The xpath of the element containing Reason code of current transaction
	 * @param avccvnCodeXpath The xpath of the element containing AVS and CVN code of current transaction
	 * @param pnReferenceNumberXpath The xpath of element containing PN reference number of the current transaction
	 * @param ReasonCode String containing reason code of the transaction
	 * @param DecisionMsg String containing decision message of the transaction
	 * @param TestScenario String containing scenario of the transaction
	 * @param AVSandCVNCode String containing AVS and CVN code of the transaction
	 * @return String contain PN reference number of the transaction
	 */
	public String checkAvsActuals(WebDriver driver, String PaymentEvent, String decisionXpath, String reasonCodeXpath,
			String avccvnCodeXpath, String pnReferenceNumberXpath, String ReasonCode, String DecisionMsg,
			String TestScenario, String AVSandCVNCode) {
		logger.info("[checkAvsActuals] Starts");
		logger.info("[checkAvsActuals] from excel TestScenario=" + TestScenario);
		logger.info("[checkAvsActuals] from excel DecisionMsg=" + DecisionMsg);
		logger.info("[checkAvsActuals] from excel ReasonCode=" + ReasonCode);
		logger.info("[checkAvsActuals] from excel AVS/CVN code=" + AVSandCVNCode);
		String response[] = getResponseFieldValues(driver, PaymentEvent, decisionXpath, reasonCodeXpath,
				avccvnCodeXpath, pnReferenceNumberXpath, Constants.STRING_AVS);
		logger.info("[checkAvsActuals] web decision=" + response[0]);
		logger.info("[checkAvsActuals] xls DecisionMsg=" + DecisionMsg);
		Assert.assertEquals(response[0], DecisionMsg);
		logger.info("Generated Decision Message after assertion=" + " " + response[0]);
		logger.info("[checkAvsActuals] web reasonCode=" + response[1]);
		logger.info("[checkAvsActuals] xls ReasonCode=" + ReasonCode);
		Assert.assertEquals(response[1], ReasonCode);
		logger.info("Generated Reason Code after assertion=" + " " + response[1]);
		logger.info("[checkAvsActuals] web decision=" + response[2]);
		logger.info("[checkAvsActuals] xls DecisionMsg=" + AVSandCVNCode);
		Assert.assertEquals(response[2], AVSandCVNCode);
		logger.info("Generated Decision Message after assertion=" + " " + response[2]);
		String[] AVSCode = response[2].split(Constants.COLON);
		Reporter.log("AVS Code" + ": " + AVSCode[AVSCode.length - 1]);
		logger.info("[checkAvsActuals] Ends");
		return response[3];
	}

	/**
	* This method is used validate CVN code of transaction
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param reasonCodeXpath The xpath of the element containing Reason code of current transaction
	 * @param avccvnCodeXpath The xpath of the element containing AVS and CVN code of current transaction
	 * @param pnReferenceNumberXpath The xpath of element containing PN reference number of the current transaction
	 * @param ReasonCode String containing reason code of the transaction
	 * @param DecisionMsg String containing decision message of the transaction
	 * @param TestScenario String containing scenario of the transaction
	 * @param AVSandCVNCode String containing AVS and CVN code of the transaction
	 * @return String contain PN reference number of the transaction
	 */
	public String checkCvnActuals(WebDriver driver, String PaymentEvent, String decisionXpath, String reasonCodeXpath,
			String avccvnCodeXpath, String pnReferenceNumberXpath, String ReasonCode, String DecisionMsg,
			String TestScenario, String AVSorCVNCode) {
		logger.info("[checkCvnActuals] Starts");
		logger.info("[checkCvnActuals] from excel TestScenario=" + TestScenario);
		logger.info("[checkCvnActuals] from excel DecisionMsg=" + DecisionMsg);
		logger.info("[checkCvnActuals] from excel ReasonCode=" + ReasonCode);
		logger.info("[checkCvnActuals] from excel ReasonCode=" + AVSorCVNCode);

		String response[] = getResponseFieldValues(driver, PaymentEvent, decisionXpath, reasonCodeXpath,
				avccvnCodeXpath, pnReferenceNumberXpath, Constants.STRING_CVN);
		logger.info("[checkCvnActuals] web decision=" + response[0]);
		logger.info("[checkCvnActuals] xls DecisionMsg=" + DecisionMsg);
		Assert.assertEquals(response[0], DecisionMsg);

		logger.info("[checkCvnActuals] web reasonCode=" + response[1]);
		logger.info("[checkCvnActuals] xls ReasonCode=" + ReasonCode);
		Assert.assertEquals(response[1], ReasonCode);

		logger.info("[checkCvnActuals] web decision=" + response[2]);
		logger.info("[checkCvnActuals] xls DecisionMsg=" + AVSorCVNCode);
		Assert.assertEquals(response[2], AVSorCVNCode);

		String[] CVNCode = response[2].split(Constants.COLON);
		Reporter.log("CVN Code: " + CVNCode[CVNCode.length - 1]);
		logger.info("[checkCvnActuals] Ends");
		return response[3];
	}

	/**
	 * This method is used to fetch values from response fields after a transaction
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param reasonCodeXpath The xpath of the element containing Reason code of current transaction
	 * @param avccvnCodeXpath The xpath of the element containing AVS and CVN code of current transaction
	 * @param pnReferenceNumberXpath The xpath of element containing PN reference number of the current transaction
	 * @param avsOrCvn String containing AVS or CVN codes of a transaction
	 * @return
	 */
	private String[] getResponseFieldValues(WebDriver driver, String paymentEvent, String decisionXpath,
			String reasonCodeXpath, String avccvnCodeXpath, String pnReferenceNumberXpath, String avsOrCvn) {
		logger.info("[getResponseFieldValue] Started");
		sleep(SLEEP_TIMER_5);
		if (paymentEvent.equals(Constants.STRING_SALE)) {
			clickPaymentEvent(driver, Constants.STRING_SALE, Constants.ORDER, Constants.TRANSACTION_STATUS_ACCEPT);
			clickPaymentEvent(driver, Constants.STRING_SALE, Constants.ORDER,
					Constants.TRANSACTION_STATUS_PAYMENT_HOLD);
			clickPaymentEvent(driver, Constants.STRING_SALE, Constants.ORDER, Constants.TRANSACTION_STATUS_REJECT);
			clickPaymentEvent(driver, Constants.STRING_SALE, Constants.CUSTOMER_DEPOSIT,
					Constants.TRANSACTION_STATUS_ACCEPT);
			clickPaymentEvent(driver, Constants.STRING_SALE, Constants.STRING_CASH_SALE,
					Constants.TRANSACTION_STATUS_ACCEPT);
		} else {
			clickPaymentEvent(driver, Constants.AUTHORIZATION, Constants.ORDER, Constants.TRANSACTION_STATUS_ACCEPT);
			clickPaymentEvent(driver, Constants.AUTHORIZATION, Constants.ORDER,
					Constants.TRANSACTION_STATUS_PAYMENT_HOLD);
			clickPaymentEvent(driver, Constants.AUTHORIZATION, Constants.ORDER, Constants.TRANSACTION_STATUS_REJECT);
		}
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		String fieldValue[] = new String[4];
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		executor = (JavascriptExecutor) driver;
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				switchToWindow(driver, childWindow);
				sleep(SLEEP_TIMER_2);
				clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
				if (!Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.PROCESS_TYPE_REST))) {
					WebElement element = findElementByXPath(driver, prop, Constants.RAW_RESPONSE_TEXT_FIELD);
					String[] lines = element.getText().trim().split("\\R");
					logger.info("[getResponseFieldValue] response length: " + lines.length);
					for (int i = 0; i < lines.length; i++) {
						if ((lines[i].contains(prop.getProperty(Constants.SOAP_DECISION_MESSAGE)))
								|| (lines[i].contains(prop.getProperty(Constants.SOAP_SA_DECISION_MESSAGE)))) {
							fieldValue[0] = lines[i].trim();
						}
						if ((lines[i].contains(prop.getProperty(Constants.SOAP_REASON_CODE)))
								|| (lines[i].contains(prop.getProperty(Constants.SOAP_SA_REASON_CODE)))) {
							fieldValue[1] = lines[i].trim();
						}
						if (Constants.STRING_AVS.equalsIgnoreCase(avsOrCvn)) {
							if ((lines[i].contains(prop.getProperty(Constants.SOAP_AVS_CODE)))
									|| (lines[i].contains(prop.getProperty(Constants.SOAP_SA_AVS_CODE)))) {
								fieldValue[2] = lines[i].trim();
							}
						}
						if (Constants.STRING_CVN.equalsIgnoreCase(avsOrCvn)) {
							if ((lines[i].contains(prop.getProperty(Constants.SOAP_CVN_CODE)))
									|| (lines[i].contains(prop.getProperty(Constants.SOAP_SA_CVN_CODE)))) {
								fieldValue[2] = lines[i].trim();
							}
						}
					}
					fieldValue[3] = driver.findElement(By.xpath(pnReferenceNumberXpath)).getText();
				}
				closeCurrentWindow(driver);
				switchToWindow(driver, mainWindow);
			}
		}

		logger.info("[getResponseFieldValue] field length: " + fieldValue.length);
		logger.info("[getResponseFieldValue] Ends");
		return fieldValue;
	}

	/**
	 * This method is used to set payment processing profile for a transaction including CVN
	 * @param driver Webdriver object which will be used for operations
	 * @param userIndex String containing index to specify user accessing instance
	 * @param PPP String containing Payment processing profile used for transaction
	 * @param AVSandCVNConfiguration String containing data about AVS and CVN configuration
	 * @param CSCNotSubmitted String to configure setup for CSC not submitted
	 * @param CSCNotSubmittedByCardholderBank String to configure setup for CSC not submitted by cardholder bank
	 * @param CSCServiceNotAvailable String to configure setup for CSC not available
	 * @param CSCCheckFailed  String to configure setup for CSC not submitted
	 * @param NoCSCMatch String to configure setup for CSC not matching
	 */
	public void setPaymentProcessingProfileCVN(WebDriver driver, String userIndex, String PPP,
			String AVSandCVNConfiguration, String CSCNotSubmitted, String CSCNotSubmittedByCardholderBank,
			String CSCServiceNotAvailable, String CSCCheckFailed, String NoCSCMatch) {
		logger.info("[SetPaymentProcessingProfileCVN] Starts");
		driver = setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, userIndex);
		clickEditPaymentProcessingProfile(driver, PPP);
		sleep(SLEEP_TIMER_3);
		setCVNConfiguration(driver, AVSandCVNConfiguration, CSCNotSubmitted, CSCNotSubmittedByCardholderBank,
				CSCServiceNotAvailable, CSCCheckFailed, NoCSCMatch);
		sleep(SLEEP_TIMER_3);
		scrollToBottom(driver);
		clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		logger.info("[SetPaymentProcessingProfileCVN] Ends");

	}

	/**
	 * This method is used to update logs to TestNG report
	 * @param serviceName String containing service used for transaction
	 * @param Constant String containing constant to be added to report
	 * @param serviceId String containing service id to be added to report
	 */
	public void addLogsToTestNGReport(String serviceName, String Constant, String serviceId) {
		logger.info("[addLogsToTestNGReport] Starts");
		Reporter.log(serviceName + Constants.SPACE + Constant + serviceId);
	}

	/**
	 * This method is used to set payment processing profile for a transaction including Decision Manager
	 * @param driver Webdriver object which will be used for operations
	 * @param userIndex String containing index to specify user accessing instance
	 * @param PaymentProcessingProfile String containing Payment processing profile used for transaction
	 * @param MerchantID String containing merchant id of the profile used for transaction
	 * @param ProcessorKey String containing processor key of the profile used for transaction
	 * @param ProcessorLevel String containing level of transaction
	 * @param DecisionManagerReject String used to set DM rule to reject
	 */
	public void setPaymentProcessingProfileDM(WebDriver driver, String userIndex, String PaymentProcessingProfile,
			String MerchantID, String ProcessorKey, String ProcessorLevel, String DecisionManagerReject) {
		logger.info("[SetPaymentProcessingProfileDM] Starts");
		driver = setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, userIndex);
		clickEditPaymentProcessingProfile(driver, PaymentProcessingProfile);
		logger.info("[SetPaymentProcessingProfileDM] element class: "
				+ findElementByXPath(driver, prop, Constants.PPP_FRAUD_MANAGEMENT).getAttribute("class"));
		if (findElementByXPath(driver, prop, Constants.PPP_FRAUD_MANAGEMENT).getAttribute("class")
				.contains(Constants.CHECKBOX_UNCK)) {
			clickElementByXPath(driver, prop, Constants.PPP_FRAUD_MANAGEMENT);
		}
		clearElementByXPath(driver, prop, Constants.PPP_DECISION_MANAGER_REJECT);
		setElementValueByXPath(driver, prop, Constants.PPP_DECISION_MANAGER_REJECT, DecisionManagerReject);
		clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		logger.info("[SetPaymentProcessingProfileDM] Ends");
	}

	/**
	 * This method is used to validate DM results for tests
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param pnReferenceNumberXpath The xpath of element containing PN reference number of the current transaction
	 * @param dmDecision String to set rule for DM
	 * @param DecisionMsg String containing decision message of the transaction
	 * @param TestScenario String containing scenario of the transaction
	 * @return String containing PN reference number of transaction
	 */
	public String checkDmActuals(WebDriver driver, String PaymentEvent, String decisionXpath,
			String pnReferenceNumberXpath, String dmDecision, String DecisionMsg, String TestScenario) {
		logger.info("[checkDmActuals] Starts");
		logger.info("[checkDmActuals] from excel TestScenario=" + TestScenario);
		logger.info("[checkDmActuals] from excel DecisionMsg=" + DecisionMsg);
		String response[] = getResponseFieldValues(driver, PaymentEvent, decisionXpath, pnReferenceNumberXpath,
				dmDecision, "DM");
		logger.info("[checkDmActuals] web decision=" + response[0]);
		logger.info("[checkDmActuals] xls DecisionMsg=" + DecisionMsg);
		Assert.assertEquals(response[0], DecisionMsg);

		Reporter.log("DecisionMsg " + ": " + response[0]);
		logger.info("[checkDmActuals] Ends");
		return response[1];
	}

	/**
	* This method is used to get response field values of a transaction including DM
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param pnReferenceNumberXpath The xpath of element containing PN reference number of the current transaction
	 * @param dmDecision String to set rule for DM
	 * @param isDM String to validate if DM is active
	 * @return String array containing field values
	 */
	private String[] getResponseFieldValues(WebDriver driver, String paymentEvent, String decisionXpath,
			String pnReferenceNumberXpath, String dmDecision, String isDM) {
		logger.info("[getResponseFieldValues] Starts");
		sleep(SLEEP_TIMER_5);
		clickPaymentEvent(driver, Constants.AUTHORIZATION, Constants.ORDER, Constants.PENDING);
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		String fieldValue[] = new String[4];
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		executor = (JavascriptExecutor) driver;
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				switchToWindow(driver, childWindow);
				clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
				WebElement element = findElementByXPath(driver, prop, Constants.RAW_RESPONSE_TEXT_FIELD);
				String[] lines = element.getText().trim().split("\\R");
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].contains(prop.getProperty(Constants.SOAP_DECISION_MESSAGE))) {
						fieldValue[0] = lines[i].trim();
					}
					if (lines[i].contains(prop.getProperty(Constants.SOAP_REASON_CODE))) {
						fieldValue[1] = lines[i].trim();
					}
				}
				String pnref = prop.getProperty(Constants.PN_REF_NUMBER_SECURE_ACCEPTANCE);
				fieldValue[1] = driver.findElement(By.xpath(pnref)).getText();
				logger.info(fieldValue[1]);
			}
		}
		closeCurrentWindow(driver);
		switchToWindow(driver, mainWindow);

		logger.info("[getResponseFieldValue] Ends");
		return fieldValue;
	}

	/**
	 * This method is used to set payment processing profile for echeck transaction
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentProcessingProfile String containing payment processing profile which is to be setted for transaction
	 */
	public void setPaymentProcessingProfileECheck(WebDriver driver, String PaymentProcessingProfile) {
		logger.info("[SetPaymentProcessingProfileECheck] Starts");
		driver = setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, null);
		Actions action = new Actions(driver);
		clickEditPaymentProcessingProfile(driver, PaymentProcessingProfile);
		driver.findElement(By.xpath("//*[@id='row_supportedpaymentoperations40_3']/a")).click();
		action.keyDown(Keys.CONTROL).build().perform();
		driver.findElement(By.xpath("//*[@id='row_supportedpaymentoperations40_4']/a")).click();
		action.keyUp(Keys.CONTROL).build().perform();
		clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		logger.info("[SetPaymentProcessingProfileECheck] Ends");
	}

	/**
	 * This method is used to check if a xpath exist or not
	 * @param driver Webdriver object which will be used for operations
	 * @param xpath xpath which is to be checked for existence
	 * @param value String containing value attribute of xpath
	 * @return Boolean containing details about existence of xpath
	 */
	public boolean isExists(WebDriver driver, String xpath, String value) {
		logger.info("[isExists] Starts");
		boolean isExists = false;
		try {
			WebElement isXPath = findElementByXPath(driver, prop, xpath);
			String isValue = isXPath.getAttribute("value").toString();
			if (isValue.equals(value) == true) {
				isExists = true;
			}
		} catch (NoSuchElementException e) {

		}
		logger.info("[isExists] Ends");
		return isExists;
	}

	/**
	 * This method is used to reset payment processing profile for echeck transaction
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentProcessingProfile String containing payment processing profile which is to be set for transaction
	 */
	public void resetPaymentProcessingProfileECheck(WebDriver driver, String PaymentProcessingProfile) {
		logger.info("[resetPaymentProcessingProfileECheck] Starts");
		driver = setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, null);
		Actions action = new Actions(driver);
		clickEditPaymentProcessingProfile(driver, PaymentProcessingProfile);
		List<WebElement> paymentMethods = driver.findElements(By.xpath("//*[contains(@id,'row_paymentmethods39')]"));
		int lastPaymentMethod = paymentMethods.size() - 1;
		driver.findElement(By.xpath("//*[@id='row_paymentmethods39_0']/a")).click();
		action.keyDown(Keys.SHIFT).build().perform();
		driver.findElement(By.xpath("//*[@id='row_paymentmethods39_" + lastPaymentMethod + "']/a")).click();
		action.keyUp(Keys.SHIFT).build().perform();
		action.keyDown(Keys.CONTROL).build().perform();
		clickElementByLinkText(driver, "Secure Acceptance eCheck");
		action.keyUp(Keys.CONTROL).build().perform();
		driver.findElement(By.xpath("//*[@id='row_supportedpaymentoperations40_0']/a")).click();
		action.keyDown(Keys.SHIFT).build().perform();
		driver.findElement(By.xpath("//*[@id='row_supportedpaymentoperations40_5']/a")).click();
		action.keyUp(Keys.SHIFT).build().perform();
		clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		logger.info("resetPaymentProcessingProfileECheck] Ends");
	}

	/**
	 * This method is used to set payment method for transaction
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentMethodName String containing payment method of transaction
	 * @param Type String containing payment type
	 * @param Deposit String containing deposit data
	 */
	public void setPaymentMethod(WebDriver driver, String PaymentMethodName, String Type, String Deposit) {
		logger.info("[SetPaymentMethod] Starts");
		if (!((findElementByXPath(driver, prop, Constants.NEW_PAYMENT_METHOD_NAME).getAttribute("title"))
				.equals(Type))) {
			sleep(SLEEP_TIMER_5);
			clearElementByXPath(driver, prop, Constants.NEW_PAYMENT_METHOD_NAME);
			sleep(SLEEP_TIMER_5);
			setElementValueByXPath(driver, prop, Constants.NEW_PAYMENT_METHOD_NAME, PaymentMethodName);
		}
		if (!((findElementByXPath(driver, prop, Constants.PAYMENT_METHOD_TYPE).getAttribute("title")).equals(Type))) {
			sleep(SLEEP_TIMER_5);
			clearElementByXPath(driver, prop, Constants.PAYMENT_METHOD_TYPE);
			sleep(SLEEP_TIMER_5);
			setElementValueByXPath(driver, prop, Constants.PAYMENT_METHOD_TYPE, Type);
		}
		if (findElementByXPath(driver, prop, Constants.PAYMENT_METHOD_REQUIRELINELEVELDATA_CHECKBOX)
				.getAttribute("class").contains(Constants.CHECKBOX_UNCK)) {
			clickElementByXPath(driver, prop, Constants.PAYMENT_METHOD_REQUIRELINELEVELDATA_CHECKBOX);
		}
		if (findElementByXPath(driver, prop, Constants.PAYMENT_METHOD_DISPLAYINWEBSITE_CHECKBOX).getAttribute("class")
				.contains(Constants.CHECKBOX_UNCK)) {
			clickElementByXPath(driver, prop, Constants.PAYMENT_METHOD_DISPLAYINWEBSITE_CHECKBOX);
		}
		sleep(SLEEP_TIMER_3);
		clickElementByXPath(driver, prop, Constants.PAYMENT_METHOD_SAVE);
		if (isAlertPresent(driver)) {
			driver.switchTo().alert().accept();
		}
		logger.info("[SetPaymentMethod] Ends");
	}

	/**
	 * This method is used to switch to default content
	 * @param driver Webdriver object which will be used for operations
	 */
	public void switchToDefaultContent(WebDriver driver) {
		logger.info("[switchToDefaultContent] Starts");
		driver.switchTo().defaultContent();
	}

	/**
	 * This method is used to scroll up the window by 100 pixels
	 * @param driver Webdriver object which will be used for operations
	 */
	public void scroll100Up(WebDriver driver) {
		logger.info("[scroll100Up] Starts");
		executor = (JavascriptExecutor) driver;
		executor.executeScript("window.scrollBy(0, -100)");
	}

	/**
	 * This method is used to set payment processing profile including Secure acceptance details
	 * @param driver Webdriver object which will be used for operations
	 * @param userIndex String containing index to specify user accessing instance
	 * @param PPP String containing Payment processing profile used for transaction
	 * @param MID String containing merchant id of the profile used for transaction
	 * @param ProcessorName String containing name of processor used for transaction
	 * @param ProcessorKey String containing processor key used for transaction
	 * @param ProcessorSecretKey String containing secret key of processor used for transaction
	 * @param AVSandCVNConfiguration String containing AVS and CVN configuration setup
	 * @param DeclineAVSFlag String containing configuration setup of AVS flag decline
	 * @param NoAVSMatch String containing configuration setup of AVS not matching
	 * @param AVSServiceNotAvailable String containing configuration setup of AVS not available
	 * @param PartialAVSMatch String containing configuration setup of AVS partial match
	 * @param CSCNotSubmitted String containing configuration setup of CSC not submitted
	 * @param CSCNotSubmittedByCardholderBank String containing configuration setup of CSC not submitted by cardholder bank
	 * @param CSCServiceNotAvailable String containing configuration setup of CSC service not available
	 * @param CSCCheckFailed String containing configuration setup of CSC check fail
	 * @param NoCSCMatch String containing configuration setup of CSC not matching
	 * @param SAProfileID String containing details of Secure acceptance profile id
	 * @param SAAccessKey String containing details of Secure acceptance access key
	 * @param SASecretKey String containing details of Secure acceptance secret key
	 */
	public void setPaymentProcessingProfile(WebDriver driver, String userIndex, String PPP, String MID,
			String ProcessorName, String ProcessorKey, String ProcessorSecretKey, String AVSandCVNConfiguration,
			String DeclineAVSFlag, String NoAVSMatch, String AVSServiceNotAvailable, String PartialAVSMatch,
			String CSCNotSubmitted, String CSCNotSubmittedByCardholderBank, String CSCServiceNotAvailable,
			String CSCCheckFailed, String NoCSCMatch, String SAProfileID, String SAAccessKey, String SASecretKey) {
		logger.info("[setPaymentProcessingProfile] Starts");
		driver = setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, userIndex);
		sleep(SLEEP_TIMER_2);
		clickEditPaymentProcessingProfile(driver, PPP);
		sleep(SLEEP_TIMER_2);
		clearElementByXPath(driver, prop, Constants.PPP_MERCHANT_ID);
		setElementValueByXPath(driver, prop, Constants.PPP_MERCHANT_ID, MID);
		setElementValueByXPath(driver, prop, Constants.PPP_PROCESSOR_NAME, ProcessorName);

		if (Constants.YES.equalsIgnoreCase(prop.getProperty(Constants.PROCESS_TYPE_REST))) {
			clearElementByXPath(driver, prop, Constants.PPP_REST_SECURITY_KEY);
			setElementValueByXPath(driver, prop, Constants.PPP_REST_SECURITY_KEY, ProcessorKey);
			clearElementByXPath(driver, prop, Constants.PPP_REST_SECRET_KEY);
			setElementValueByXPath(driver, prop, Constants.PPP_REST_SECRET_KEY, ProcessorSecretKey);
		} else {
			clearElementByXPath(driver, prop, Constants.PPP_SECURITY_KEY);
			setElementValueByXPath(driver, prop, Constants.PPP_SECURITY_KEY, ProcessorKey);
			sleep(SLEEP_TIMER_2);
			findElementByXPath(driver, prop, Constants.PPP_SECURITY_KEY).sendKeys(Keys.DOWN, Keys.ENTER);
		}
		setAVSConfiguration(driver, AVSandCVNConfiguration, DeclineAVSFlag, NoAVSMatch, AVSServiceNotAvailable,
				PartialAVSMatch);
		setCVNConfiguration(driver, AVSandCVNConfiguration, CSCNotSubmitted, CSCNotSubmittedByCardholderBank,
				CSCServiceNotAvailable, CSCCheckFailed, NoCSCMatch);
		if (PPP.contains(Constants.STRING_WEBSTORE)) {
			clearElementByXPath(driver, prop, Constants.NEW_PPP_SA_PROFILE_ID);
			setElementValueByXPath(driver, prop, Constants.NEW_PPP_SA_PROFILE_ID, SAProfileID);
			clearElementByXPath(driver, prop, Constants.NEW_PPP_SA_ACCESS_KEY);
			setElementValueByXPath(driver, prop, Constants.NEW_PPP_SA_ACCESS_KEY, SAAccessKey);
			clearElementByXPath(driver, prop, Constants.NEW_PPP_SA_SECRET_KEY);
			setElementValueByXPath(driver, prop, Constants.NEW_PPP_SA_SECRET_KEY, SASecretKey);
		}
		scrollToBottom(driver);
		clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		logger.info("[setPaymentProcessingProfile] Ends");
	}

	/**
	 * This method is used to set payment processing profile including AVS
	 * @param driver Webdriver object which will be used for operations
	 * @param userIndex String containing index to specify user accessing instance
	 * @param PPP String containing Payment processing profile used for transaction
	 * @param AVSandCVNConfiguration String containing AVS and CVN configuration setup
	 * @param DeclineAVSFlag String containing configuration setup of AVS flag decline
	 * @param NoAVSMatch String containing configuration setup of AVS not matching
	 * @param AVSServiceNotAvailable String containing configuration setup of AVS not available
	 * @param PartialAVSMatch String containing configuration setup of AVS partial match
	 */
	public void setPaymentProcessingProfileAVS(WebDriver driver, String userIndex, String PPP,
			String AVSandCVNConfiguration, String DeclineAVSFlag, String NoAVSMatch, String AVSServiceNotAvailable,
			String PartialAVSMatch) {
		logger.info("[setPaymentProcessingProfileAVS] Starts");
		driver = setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, userIndex);
		clickEditPaymentProcessingProfile(driver, PPP);
		sleep(SLEEP_TIMER_3);
		setAVSConfiguration(driver, AVSandCVNConfiguration, DeclineAVSFlag, NoAVSMatch, AVSServiceNotAvailable,
				PartialAVSMatch);
		scrollToBottom(driver);
		clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		logger.info("[setPaymentProcessingProfileAVS] Ends");
	}

	/**
	 * This method is used to validate REST setup values
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param ReasonCode The string that contains reason code of current transaction
	 * @param TestScenario The string that contains test scenario of current transaction
	 * @return String containing PN reference number of a transaction
	 */
	public String checkRestActuals(WebDriver driver, String PaymentEvent, String ReasonCode, String TestScenario) {
		logger.info("[checkRestActuals] Starts");
		logger.info("[checkRestActuals] from excel TestScenario=" + TestScenario);
		logger.info("[checkRestActuals] from excel ReasonCode=" + ReasonCode);

		String response[] = getRestResponseFieldValue(driver, PaymentEvent);
		logger.info("[checkRestActuals] web reasonCode=" + response[0]);
		logger.info("[checkRestActuals] xls ReasonCode=" + ReasonCode);
		Assert.assertEquals(response[0], ReasonCode);

		logger.info("[checkRestActuals] Ends");
		return response[1];
	}

	/**
	 * This method is used to validate REST setup values including payment type
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param PaymentType The string that contains payment type used for current transaction
	 * @param ReasonCode The string that contains reason code of current transaction
	 * @param TestScenario The string that contains test scenario of current transaction
	 * @return String containing PN reference number of a transaction
	 */
	public String checkRestActuals(WebDriver driver, String PaymentEvent, String PaymentType, String ReasonCode,
			String TestScenario) {
		logger.info("[checkRestActuals] Starts");
		logger.info("[checkRestActuals] from excel TestScenario=" + TestScenario);
		logger.info("[checkRestActuals] from excel ReasonCode=" + ReasonCode);

		String response[] = getRestResponseFieldValue(driver, PaymentEvent, PaymentType);
		logger.info("[checkRestActuals] web reasonCode=" + response[0]);
		logger.info("[checkRestActuals] xls ReasonCode=" + ReasonCode);
		Assert.assertEquals(response[0], ReasonCode);

		logger.info("[checkRestActuals] Ends");
		return response[1];
	}

	/**
	 * This method is used to validate REST setup values including AVS and CVN
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param avccvnCodeXpath The xpath of the element containing AVS and CVN codes
	 * @param pnReferenceNumberXpath The xpath of the element containing PN reference number
	 * @param DecisionMsg String containing decision message of the transaction
	 * @param TestScenario String containing scenario of the transaction
	 * @param AVSandCVNCode String containing AVS and CVN code of the transaction
	 * @return String containing PN reference number of a transaction
	 */
	public String checkRESTavsActuals(WebDriver driver, String PaymentEvent, String decisionXpath,
			String avccvnCodeXpath, String pnReferenceNumberXpath, String DecisionMsg, String TestScenario,
			String AVSandCVNCode) {
		logger.info("[checkRESTavsActuals] Starts");
		logger.info("[checkRESTavsActuals] from excel TestScenario=" + TestScenario);
		logger.info("[checkRESTavsActuals] from excel DecisionMsg=" + DecisionMsg);
		logger.info("[checkRESTavsActuals] from excel ReasonCode=" + AVSandCVNCode);

		String response[] = getAVSRestResponseFieldValues(driver, PaymentEvent, decisionXpath, avccvnCodeXpath,
				pnReferenceNumberXpath, "AVS");
		logger.info("[checkRESTavsActuals] web decision=" + response[0]);
		logger.info("[checkRESTavsActuals] xls DecisionMsg=" + DecisionMsg);
		Assert.assertEquals(response[0], DecisionMsg);

		logger.info("[checkRESTavsActuals] web decision=" + response[1]);
		logger.info("[checkRESTavsActuals] xls DecisionMsg=" + AVSandCVNCode);
		Assert.assertEquals(response[1], AVSandCVNCode);
		Reporter.log("AVS Code" + ": " + response[1]);
		logger.info("[checkRESTavsActuals] Ends");
		return response[2];
	}

	/**
	 * This method is used to get response field values for a REST transaction including AVS
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param avccvnCodeXpath The xpath of the element containing AVS and CVN codes
	 * @param pnReferenceNumberXpath The xpath of the element containing PN reference number
	 * @param avsOrCvn String containing AVS or CVN code of transaction
	 * @return String array containing response field values
	 */
	private String[] getAVSRestResponseFieldValues(WebDriver driver, String paymentEvent, String decisionXpath,
			String avccvnCodeXpath, String pnReferenceNumberXpath, String avsOrCvn) {
		logger.info("[getAVSRestResponseFieldValues] Starts");
		sleep(SLEEP_TIMER_5);
		if (paymentEvent.equals(Constants.STRING_SALE)) {
			clickPaymentEvent(driver, Constants.STRING_SALE, Constants.STRING_CASH_SALE, Constants.PENDING);
		} else {
			clickPaymentEvent(driver, Constants.AUTHORIZATION, Constants.ORDER, Constants.PENDING);
		}
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		String fieldValue[] = new String[3];
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		executor = (JavascriptExecutor) driver;
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				logger.info("[getAVSRestResponseFieldValues] Switched to child window");
				switchToWindow(driver, childWindow);
				clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
				WebElement element = findElementByXPath(driver, prop, Constants.RAW_RESPONSE_TEXT_FIELD);
				String[] lines = element.getText().trim().split("\\R");
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].contains(prop.getProperty(Constants.REST_STATUS))) {
						fieldValue[0] = lines[i].trim();
					}
					if (lines[i].contains(prop.getProperty(Constants.REST_AVS))) {
						fieldValue[1] = lines[i + 1].trim();
					}
					if (lines[i].contains(prop.getProperty(Constants.REST_PN_REF))) {
						fieldValue[2] = lines[i].split(":")[1].trim().replace("\"\"", "");
					}
				}
				closeCurrentWindow(driver);
				switchToWindow(driver, mainWindow);
			}
		}
		logger.info("[getAVSRestResponseFieldValues] Ends");
		return fieldValue;
	}

	/**
	 * This method is used to validate REST setup values including CVN codes
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param avccvnCodeXpath The xpath of the element containing AVS and CVN codes
	 * @param pnReferenceNumberXpath The xpath of the element containing PN reference number
	 * @param DecisionMsg String containing decision message of the transaction
	 * @param TestScenario String containing scenario of the transaction
	 * @param AVSandCVNCode String containing AVS and CVN code of the transaction
	 * @return String containing PN reference number of a transaction
	 */
	public String checkRESTcvnActuals(WebDriver driver, String PaymentEvent, String decisionXpath,
			String avccvnCodeXpath, String pnReferenceNumberXpath, String DecisionMsg, String TestScenario,
			String AVSandCVNCode) {
		logger.info("[checkRESTcvnActuals] Starts");
		logger.info("[checkRESTcvnActuals] from excel TestScenario=" + TestScenario);
		logger.info("[checkRESTcvnActuals] from excel DecisionMsg=" + DecisionMsg);
		logger.info("[checkRESTcvnActuals] from excel ReasonCode=" + AVSandCVNCode);

		String response[] = getCVNRestResponseFieldValues(driver, PaymentEvent, decisionXpath, avccvnCodeXpath,
				pnReferenceNumberXpath, "CVN");
		logger.info("[checkRESTcvnActuals] web decision=" + response[0]);
		logger.info("[checkRESTcvnActuals] xls DecisionMsg=" + DecisionMsg);
		Assert.assertEquals(response[0], DecisionMsg);

		Reporter.log("DecisionMsg " + ": " + response[0]);
		logger.info("[checkRESTcvnActuals] web decision=" + response[1]);
		logger.info("[checkRESTcvnActuals] xls DecisionMsg=" + AVSandCVNCode);
		Assert.assertEquals(response[1], AVSandCVNCode);

		Reporter.log("AVS Code" + ": " + response[1]);
		logger.info("[checkRESTcvnActuals] Ends");
		return response[2];
	}

	/**
	 * This method is used to get response field values for a REST transaction including CVN
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param avccvnCodeXpath The xpath of the element containing AVS and CVN codes
	 * @param pnReferenceNumberXpath The xpath of the element containing PN reference number
	 * @param avsOrCvn String containing AVS or CVN code of transaction
	 * @return String array containing response field values
	 */
	private String[] getCVNRestResponseFieldValues(WebDriver driver, String paymentEvent, String decisionXpath,
			String avccvnCodeXpath, String pnReferenceNumberXpath, String avsOrCvn) {
		logger.info("[getCVNRestResponseFieldValues] Starts");
		sleep(SLEEP_TIMER_5);
		if (paymentEvent.equals(Constants.STRING_SALE)) {
			clickPaymentEvent(driver, Constants.STRING_SALE, Constants.STRING_CASH_SALE, Constants.PENDING);
		} else {
			clickPaymentEvent(driver, Constants.AUTHORIZATION, Constants.ORDER, Constants.PENDING);
		}
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		String fieldValue[] = new String[3];
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		executor = (JavascriptExecutor) driver;
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				logger.info("[getCVNRestResponseFieldValues] Switched to child window");
				switchToWindow(driver, childWindow);
				clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
				WebElement element = findElementByXPath(driver, prop, Constants.RAW_RESPONSE_TEXT_FIELD);
				String[] lines = element.getText().trim().split("\\R");
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].contains(prop.getProperty(Constants.REST_STATUS))) {
						fieldValue[0] = lines[i].trim();
					}
					if (lines[i].contains(prop.getProperty(Constants.REST_CVN))) {
						fieldValue[1] = lines[i].trim();
					}
					if (lines[i].contains(prop.getProperty(Constants.REST_PN_REF))) {
						fieldValue[2] = lines[i].split(":")[1].trim().replace("\"\"", "");
					}
				}
				closeCurrentWindow(driver);
				switchToWindow(driver, mainWindow);
			}
		}
		logger.info("[getCVNRestResponseFieldValues] Ends");
		return fieldValue;
	}

	/**
	 * This method is used to get response field values for a REST transaction
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @return String array containing response field values
	 */
	private String[] getRestResponseFieldValue(WebDriver driver, String PaymentEvent) {
		logger.info("[getRestResponseFieldValue] Starts");
		clickPaymentEvent(driver, PaymentEvent);
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		String fieldValue[] = new String[3];
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		executor = (JavascriptExecutor) driver;
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				logger.info("[getRestResponseFieldValue] Switched to child window");
				switchToWindow(driver, childWindow);
				clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
				List<WebElement> pres = driver.findElements(By.xpath(prop.getProperty(Constants.REST_RESPONSE_XPATH)));
				String[] lines = pres.get(3).getText().trim().split("\\R");
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].contains(prop.getProperty(Constants.REST_STATUS))) {
						fieldValue[0] = lines[i].trim();
					}
					if (lines[i].contains(prop.getProperty(Constants.REST_PN_REF))) {
						fieldValue[1] = lines[i].split(":")[1].trim().replace("\"", "");
					}
				}
				closeCurrentWindow(driver);
				switchToWindow(driver, mainWindow);
			}
		}
		logger.info("[getRestResponseFieldValue] Ends");
		return fieldValue;
	}

	/**
	 * This method is used to select credit card to complete a transaction
	 * @param driver Webdriver object which will be used for operations
	 * @param cardEnding String containing last 4 digits of the credit card
	 * @param cardExpiry String containing expiry date details of the credit card
	 * @param cardType String containing credit card type used for transaction
	 */
	public void selectCard(WebDriver driver, String cardEnding, String cardExpiry, String cardType) {
		logger.info("[selectCard] Starts");
		List<WebElement> cards = driver.findElements(By.className("backbone-collection-view-cell-span4"));
		for (WebElement row : cards) {
			String alt = row.findElement(By.tagName("img")).getAttribute("alt").toString();
			logger.info("[selectCard] Payment Data: " + alt);
			if ((null != cardEnding) && (null != cardExpiry)) {
				String cardNumber = row.findElement(By.className("paymentinstrument-creditcard-number")).getText();
				String cardExpire = row.findElement(By.className("paymentinstrument-creditcard-expdate")).getText();
				logger.info("[selectCard] Card Data: " + cardNumber + ", " + cardExpire);
				if ((alt.equalsIgnoreCase(cardType)) && (cardNumber.contains(cardEnding))
						&& (cardExpire.contains(cardExpiry))) {
					logger.info("[selectCard] Selecting Card");
					row.findElement(By.tagName("a")).click();
					break;
				}
			} else {
				if (alt.equalsIgnoreCase(cardType)) {
					logger.info("[selectCard] Selecting Secure Payment");
					row.findElement(By.tagName("a")).click();
					break;
				}
			}
		}
		logger.info("[selectCard] Ends");
	}

	/**
	 * This method is used to select credit card typr from the dropdown
	 * @param driver Webdriver object which will be used for operations
	 * @param paymentCardType String containing credit card type used for transaction
	 */
	public void clickPaymentCardDropDown(WebDriver driver, String paymentCardType) {
		logger.info("[clickPaymentCardDropDown] - Starts");
		boolean isCard = false;
		List<WebElement> cards = new ArrayList<WebElement>();
		clickElementByXPath(driver, prop, Constants.SA_CARD_TYPE_DROPDOWN);
		cards = driver.findElements(By.xpath("//*[@id='card_type']/option"));
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).getText().toString().toUpperCase().equals(paymentCardType.toUpperCase())) {
				cards.get(i).click();
				isCard = true;
				break;
			}
		}
		if (isCard == false) {
			logger.info("[clickPaymentCardDropDown] - Card not found!");
		}
		logger.info("[clickPaymentCardDropDown] - Ends");
	}

	/**
	 * This method is used to select credit card type from the available options
	 * @param driver Webdriver object which will be used for operations
	 * @param paymentCardType String containing credit card type used for transaction
	 */
	public void selectPaymentCard(WebDriver driver, String paymentCardType) {
		logger.info("[clickPaymentCard] - Starts");
		boolean isCard = false;
		List<WebElement> cards = new ArrayList<WebElement>();
		cards = driver.findElements(By.xpath(prop.getProperty(Constants.SA_CARD_TYPE_SELECTION)));
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).getText().toString().toUpperCase().equals(paymentCardType.toUpperCase())) {
				String xpath = "//*[@id='card_type_selection']/div[" + (i + 1) + "]/input";
				driver.findElement(By.xpath(xpath)).click();
				// driver.findElement(By.xpath("//*[@id='card_type_00"+(i+1)+"']")).click();
				isCard = true;
				break;
			}
		}
		if (isCard == false) {
			logger.info("[clickPaymentCard] - Card not found!");
		}
		logger.info("[clickPaymentCard] - Ends");
	}

	/**
	 * This method is used to fetch response field values for a REST transaction
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param PaymentType The string that defines payment type used for current transaction
	 * @return String array containing response field values
	 */
	private String[] getRestResponseFieldValue(WebDriver driver, String PaymentEvent, String PaymentType) {
		logger.info("[getRestResponseFieldValue] Starts");
		clickPaymentEvent(driver, PaymentEvent);
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		String fieldValue[] = new String[3];
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		executor = (JavascriptExecutor) driver;
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				logger.info("[getRestResponseFieldValue] Switched to child window");
				switchToWindow(driver, childWindow);
				// Checking MIT Fields
				List<WebElement> request = driver
						.findElements(By.xpath(prop.getProperty(Constants.REST_RESPONSE_XPATH)));
				String[] requestLines = request.get(1).getText().trim().split("\\R");
				if (PaymentType.equals(Constants.PAYMENT_TYPE_RESUBMISSION)) {
					for (int i = 0; i < requestLines.length; i++) {
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_TYPE))) {
							logger.info("Expected: " + prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							logger.info("Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_REASON))) {
							logger.info("Expected: " + prop.getProperty(Constants.REST_MIT_REASON_ONE));
							logger.info("Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_REASON_ONE));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_STORED_CREDENTAILS_USED))) {
							logger.info(
									"Expected: " + prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							logger.info("Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_PREVIOUS_TRANSACTION_ID))) {
							logger.info("Previous Transaction ID field: " + requestLines[i].trim());
							Reporter.log(requestLines[i].trim());
						}
					}
				} else if (PaymentType.equals(Constants.PAYMENT_TYPE_DELAYED_CHARGE)) {
					for (int i = 0; i < requestLines.length; i++) {
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_TYPE))) {
							logger.info("Expected: " + prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							logger.info("Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_REASON))) {
							logger.info("Expected: " + prop.getProperty(Constants.REST_MIT_REASON_TWO));
							logger.info("Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_REASON_TWO));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_STORED_CREDENTAILS_USED))) {
							logger.info(
									"Expected: " + prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							logger.info("Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_PREVIOUS_TRANSACTION_ID))) {
							logger.info("Previous Transaction ID field: " + requestLines[i].trim());
							Reporter.log(requestLines[i].trim());
						}
					}
				} else if (PaymentType.equals(Constants.PAYMENT_TYPE_REAUTHORIZATION)) {
					for (int i = 0; i < requestLines.length; i++) {
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_TYPE))) {
							logger.info("Expected: " + prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							logger.info("Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_REASON))) {
							logger.info("Expected: " + prop.getProperty(Constants.REST_MIT_REASON_THREE));
							logger.info("Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_REASON_THREE));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_STORED_CREDENTAILS_USED))) {
							logger.info(
									"Expected: " + prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							logger.info("Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_PREVIOUS_TRANSACTION_ID))) {
							logger.info("Previous Transaction ID field: " + requestLines[i].trim());
							Reporter.log(requestLines[i].trim());
						}
					}
				} else if (PaymentType.equals(Constants.PAYMENT_TYPE_NO_SHOW_CHARGE)) {
					for (int i = 0; i < requestLines.length; i++) {
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_TYPE))) {
							logger.info("Expected: " + prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							logger.info("Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_REASON))) {
							logger.info("Expected: " + prop.getProperty(Constants.REST_MIT_REASON_FOUR));
							logger.info("Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_REASON_FOUR));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_STORED_CREDENTAILS_USED))) {
							logger.info(
									"Expected: " + prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							logger.info("Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_PREVIOUS_TRANSACTION_ID))) {
							logger.info("Previous Transaction ID field: " + requestLines[i].trim());
							Reporter.log(requestLines[i].trim());
						}
					}
				} else if (PaymentType.equals(Constants.PAYMENT_TYPE_UNSCHEDULED)) {
					for (int i = 0; i < requestLines.length; i++) {
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_TYPE))) {
							logger.info("Expected: " + prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							logger.info("Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_COMMERCE_INDICATOR))) {
							logger.info(
									"Expected: " + prop.getProperty(Constants.REST_MIT_COMMERCE_INDICATOR_INTERNET));
							logger.info("Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_COMMERCE_INDICATOR_INTERNET));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_STORED_CREDENTAILS_USED))) {
							logger.info(
									"Expected: " + prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							logger.info("Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_PREVIOUS_TRANSACTION_ID))) {
							logger.info("Previous Transaction ID field: " + requestLines[i].trim());
							Reporter.log(requestLines[i].trim());
						}
					}
				} else {
					logger.info("[getRestResponseFieldValue] - Payment Type not matching!");
					Reporter.log("Input Data Issue: Payment Type not matching");
				}
				clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
				List<WebElement> pres = driver.findElements(By.xpath(prop.getProperty(Constants.REST_RESPONSE_XPATH)));
				String[] lines = pres.get(3).getText().trim().split("\\R");
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].contains(prop.getProperty(Constants.REST_STATUS))) {
						fieldValue[0] = lines[i].trim();
					}
					if (lines[i].contains(prop.getProperty(Constants.REST_PN_REF))) {
						fieldValue[1] = lines[i].split(":")[1].trim().replace("\"", "");
					}
				}
				closeCurrentWindow(driver);
				switchToWindow(driver, mainWindow);
			}
		}
		logger.info("[getRestResponseFieldValue] Ends");
		return fieldValue;
	}

	/**
	 * This method is used to validate Merchant initiated REST transactions
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param statusMsgXpath xpath of webelement containing status message
	 * @param pnReferenceNumberXpath xpath of webelement containing PN reference number
	 * @param StatusMsg String containing details of the transaction status message
	 * @param TestScenario String containing scenario of the transaction
	 * @param PaymentType String containing payment type used for the transaction
	 * @return String containing PN reference number of a transaction
	 */
	public String checkRestMITActuals(WebDriver driver, String PaymentEvent, String statusMsgXpath,
			String pnReferenceNumberXpath, String StatusMsg, String TestScenario, String PaymentType) {
		logger.info("[checkRestMITActuals] Starts");
		logger.info("[checkRestMITActuals] from excel TestScenario=" + TestScenario);
		logger.info("[checkRestMITActuals] from excel ReasonCode=" + StatusMsg);

		String response[] = getRestResponseFieldValue(driver, PaymentEvent, statusMsgXpath, pnReferenceNumberXpath,
				PaymentType);
		logger.info("[checkRestMITActuals] web reasonCode=" + response[0]);
		logger.info("[checkRestMITActuals] xls ReasonCode=" + StatusMsg);
		Assert.assertEquals(response[0], StatusMsg);
		logger.info("Generated Reason Code after assertion=" + " " + response[0]);
		logger.info("[checkRestMITActuals] Ends");
		return response[1];
	}

	/**
	 * This method is used to get response field values for REST transaction
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param statusMsgXpath xpath of webelement containing status message
	 * @param pnReferenceNumberXpath xpath of webelement containing PN reference number
	 * @param PaymentType String containing payment type used for the transaction
	 * @return String array containing response field values
	 */
	private String[] getRestResponseFieldValue(WebDriver driver, String PaymentEvent, String statusMsgXpath,
			String pnReferenceNumberXpath, String PaymentType) {
		logger.info("[getRestResponseFieldValue] Starts");
		clickPaymentEvent(driver, PaymentEvent);
		String mainWindow = driver.getWindowHandle();
		String childWindow = null;
		String fieldValue[] = new String[3];
		Set<String> allWindows = driver.getWindowHandles();
		Iterator<String> itr = allWindows.iterator();
		executor = (JavascriptExecutor) driver;
		while (itr.hasNext()) {
			childWindow = itr.next();
			if (!mainWindow.equals(childWindow)) {
				logger.info("[getRestResponseFieldValue] Switched to child window");
				switchToWindow(driver, childWindow);
				// Checking MIT Fields
				List<WebElement> request = driver
						.findElements(By.xpath(prop.getProperty(Constants.REST_RESPONSE_XPATH)));
				String[] requestLines = request.get(1).getText().trim().split("\\R");
				if (PaymentType.equals(Constants.PAYMENT_TYPE_RESUBMISSION)) {
					for (int i = 0; i < requestLines.length; i++) {
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_TYPE))) {
							logger.info("[getRestResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							logger.info("[getRestResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_REASON))) {
							logger.info("[getRestResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.REST_MIT_REASON_ONE));
							logger.info("[getRestResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_REASON_ONE));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_STORED_CREDENTAILS_USED))) {
							logger.info("[getRestResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							logger.info("[getRestResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_PREVIOUS_TRANSACTION_ID))) {
							logger.info("[getRestResponseFieldValue] Previous Transaction ID field: "
									+ requestLines[i].trim());
							Reporter.log(requestLines[i].trim());
						}
					}
				} else if (PaymentType.equals(Constants.PAYMENT_TYPE_DELAYED_CHARGE)) {
					for (int i = 0; i < requestLines.length; i++) {
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_TYPE))) {
							logger.info("[getRestResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							logger.info("[getRestResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_REASON))) {
							logger.info("[getRestResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.REST_MIT_REASON_TWO));
							logger.info("[getRestResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_REASON_TWO));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_STORED_CREDENTAILS_USED))) {
							logger.info("[getRestResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							logger.info("[getRestResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_PREVIOUS_TRANSACTION_ID))) {
							logger.info("[getRestResponseFieldValue] Previous Transaction ID field: "
									+ requestLines[i].trim());
							Reporter.log(requestLines[i].trim());
						}
					}
				} else if (PaymentType.equals(Constants.PAYMENT_TYPE_REAUTHORIZATION)) {
					for (int i = 0; i < requestLines.length; i++) {
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_TYPE))) {
							logger.info("[getRestResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							logger.info("[getRestResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_REASON))) {
							logger.info("[getRestResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.REST_MIT_REASON_THREE));
							logger.info("[getRestResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_REASON_THREE));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_STORED_CREDENTAILS_USED))) {
							logger.info("[getRestResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							logger.info("[getRestResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_PREVIOUS_TRANSACTION_ID))) {
							logger.info("[getRestResponseFieldValue] Previous Transaction ID field: "
									+ requestLines[i].trim());
							Reporter.log(requestLines[i].trim());
						}
					}
				} else if (PaymentType.equals(Constants.PAYMENT_TYPE_NO_SHOW_CHARGE)) {
					for (int i = 0; i < requestLines.length; i++) {
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_TYPE))) {
							logger.info("[getRestResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							logger.info("[getRestResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_REASON))) {
							logger.info("[getRestResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.REST_MIT_REASON_FOUR));
							logger.info("[getRestResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_REASON_FOUR));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_STORED_CREDENTAILS_USED))) {
							logger.info("[getRestResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							logger.info("[getRestResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_PREVIOUS_TRANSACTION_ID))) {
							logger.info("[getRestResponseFieldValue] Previous Transaction ID field: "
									+ requestLines[i].trim());
							Reporter.log(requestLines[i].trim());
						}
					}
				} else if (PaymentType.equals(Constants.PAYMENT_TYPE_UNSCHEDULED)) {
					for (int i = 0; i < requestLines.length; i++) {
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_TYPE))) {
							logger.info("[getRestResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							logger.info("[getRestResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_TYPE_MERCHANT));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i].contains(prop.getProperty(Constants.REST_MIT_PARSER_COMMERCE_INDICATOR))) {
							logger.info("[getRestResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.REST_MIT_COMMERCE_INDICATOR_INTERNET));
							logger.info("[getRestResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_COMMERCE_INDICATOR_INTERNET));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_STORED_CREDENTAILS_USED))) {
							logger.info("[getRestResponseFieldValue] Expected: "
									+ prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							logger.info("[getRestResponseFieldValue] Actual: " + requestLines[i].trim());
							Assert.assertEquals(requestLines[i].trim(),
									prop.getProperty(Constants.REST_MIT_STORED_CREDENTAILS_USED_TRUE));
							Reporter.log(requestLines[i].trim());
						}
						if (requestLines[i]
								.contains(prop.getProperty(Constants.REST_MIT_PARSER_PREVIOUS_TRANSACTION_ID))) {
							logger.info("[getRestResponseFieldValue] Previous Transaction ID field: "
									+ requestLines[i].trim());
							Reporter.log(requestLines[i].trim());
						}
					}
				} else {
					logger.info("[getRestResponseFieldValue] ERROR - Payment Type not matching!");
					Reporter.log("Input Data Issue: Payment Type not matching");
				}
				clickElementByXPath(driver, prop, Constants.SALES_ORDER_RAW_RESPONSE_TAB);
				List<WebElement> pres = driver.findElements(By.xpath(prop.getProperty(Constants.REST_RESPONSE_XPATH)));
				String[] lines = pres.get(3).getText().trim().split("\\R");
				for (int i = 0; i < lines.length; i++) {
					if (lines[i].contains(prop.getProperty(Constants.REST_STATUS))) {
						fieldValue[0] = lines[i].trim();
					}
					if (lines[i].contains(prop.getProperty(Constants.REST_PN_REF))) {
						fieldValue[1] = lines[i].split(":")[1].trim().replace("\"", "");
					}
				}
				closeCurrentWindow(driver);
				switchToWindow(driver, mainWindow);
			}
		}
		logger.info("[getRestResponseFieldValue] Ends");
		return fieldValue;
	}

	/**
	 * This method is used to validate details of Secure acceptance transactions
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentEvent The string that defines payment event of current transaction
	 * @param decisionXpath The xpath of the element containing Decision message
	 * @param reasonCodeXpath The xpath of the element containing Reason code of transaction
	 * @param avccvnCodeXpath The xpath of the element containing AVS and CVN code of transaction
	 * @param pnReferenceNumberXpath The xpath of the element containing PN reference number of transaction
	 * @param ReasonCode The string that defines Reason code of current transaction
	 * @param DecisionMsg The string that defines the decision message
	 * @param TestScenario The string that defines the current test scenario
	 * @return String that contains PN reference number of the transaction
	 */
	public String checkSAActuals(WebDriver driver, String PaymentEvent, String decisionXpath, String reasonCodeXpath,
			String avccvnCodeXpath, String pnReferenceNumberXpath, String ReasonCode, String DecisionMsg,
			String TestScenario) {
		logger.info("[checkSAActuals] Starts");
		logger.info("[checkSAActuals] from excel TestScenario=" + TestScenario);
		logger.info("[checkSAActuals] from excel DecisionMsg=" + DecisionMsg);
		logger.info("[checkSAActuals] from excel ReasonCode=" + ReasonCode);

		String response[] = getResponseFieldValues(driver, PaymentEvent, decisionXpath, reasonCodeXpath,
				avccvnCodeXpath, pnReferenceNumberXpath, "SA");
		logger.info("[checkActuals] web decision=" + response[0]);
		logger.info("[checkActuals] xls DecisionMsg=" + DecisionMsg);
		Assert.assertEquals(response[0], DecisionMsg);
		Reporter.log("DecisionMsg " + ": " + response[0]);

		logger.info("[checkActuals] web reasonCode=" + response[1]);
		logger.info("[checkActuals] xls ReasonCode=" + ReasonCode);
		Assert.assertEquals(response[1], ReasonCode);
		Reporter.log("Reason Code: " + response[1]);

		logger.info("[checkSAActuals] Ends");
		return response[2];
	}

	/**
	 * This method is used to check if credit option is selected or not
	 * @param webElement Webelement that is checked to select credit option
	 * @return Boolean returning status of credit selection
	 */
	public boolean isCreditChecked(WebElement webElement) {
		logger.info("[isCreditChecked] Starts");
		Boolean result = false;
		try {
			String value = webElement.getAttribute(prop.getProperty(Constants.WEBSTORE_INVOICES_CHECKED));
			if (value != null) {
				result = true;
			}
		} catch (Exception e) {
		}
		logger.info("[isCreditChecked] Ends");
		return result;
	}

	/**
	 * This method to cancel an alert present in the web browser
	 * @param driver Webdriver object which will be used for operations
	 */
	public void cancelAlert(WebDriver driver) {
		logger.info("[cancelAlert] Starts");
		driver.switchTo().alert().dismiss();
	}

	/**
	 * This method is used to select payment processing profile profile for level 2,3 transaction
	 * @param driver Webdriver object which will be used for operations
	 * @param PaymentProcessingProfile String containing name of payment processing profile used for transaction
	 * @param ProcessorLevel This method is used to select processor level of transaction
	 */
	public void setPaymentProcessingProfile(WebDriver driver, String PaymentProcessingProfile, String ProcessorLevel) {
		logger.info("[setPaymentProcessingProfile] Starts");
		driver = setupDriver(driver, prop, Constants.PAYMENT_PROCESSING_PROFILE_URL, null);
		clickEditPaymentProcessingProfile(driver, PaymentProcessingProfile);
		clickElementByXPath(driver, prop, Constants.PPP_PROCESSOR_LEVEL);
		if (Constants.PROCESSOR_LEVEL_2.equalsIgnoreCase(ProcessorLevel)) {
			clickElementByXPath(driver, prop, Constants.PPP_PROCESSOR_LEVEL_2);
		} else if (Constants.PROCESSOR_LEVEL_3.equalsIgnoreCase(ProcessorLevel)) {
			clickElementByXPath(driver, prop, Constants.PPP_PROCESSOR_LEVEL_3);
		}
		sleep(SLEEP_TIMER_3);
		clickElementByXPath(driver, prop, Constants.PAYMENT_PROCESSING_PROFILES_SAVE_BUTTON);
		logger.info("[setPaymentProcessingProfile] Ends");
	}

	/**
	 * This method is used to validate AVS status of a payment
	 * @param driver Webdriver object which will be used for operations
	 * @param NoAVSMatch String to configure setup for ANS not matching for transaction
	 * @param AVSServiceNotAvailable String to configure setup for ANS service unavailable for transaction
	 * @param PartialAVSMatch String to configure setup for ANS partial matching for transaction
	 * @param TransactionReason String that contains reason code of the transaction
	 * @param TransactionResult String that contains results of transaction
	 */
	public void checkPaymentStatusAVS(WebDriver driver, String NoAVSMatch, String AVSServiceNotAvailable,
			String PartialAVSMatch, String TransactionReason, String TransactionResult) {
		logger.info("[checkPaymentStatusAVS] Starts");
		if (NoAVSMatch.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& AVSServiceNotAvailable.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& PartialAVSMatch.equals(Constants.TRANSACTION_STATUS_ACCEPT)) {
			Assert.assertEquals(TransactionReason, prop.getProperty(Constants.REASON_SUCCESS));
			Assert.assertEquals(TransactionResult, prop.getProperty(Constants.RESULT_SUCCESS));
		}
		if (AVSServiceNotAvailable.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& PartialAVSMatch.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& !NoAVSMatch.equals(Constants.TRANSACTION_STATUS_ACCEPT)) {
			if (NoAVSMatch.equals(Constants.VERIFICATION_REVIEW)) {
				Assert.assertEquals(TransactionReason, prop.getProperty(Constants.REASON_VERIFICATION));
				Assert.assertEquals(TransactionResult, prop.getProperty(Constants.RESULT_VERIFICATION));
			} else {
				Assert.assertEquals(TransactionReason, prop.getProperty(Constants.REASON_REJECT));
				Assert.assertEquals(TransactionResult, prop.getProperty(Constants.RESULT_REJECT));
			}
		}
		if (NoAVSMatch.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& PartialAVSMatch.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& !AVSServiceNotAvailable.equals(Constants.TRANSACTION_STATUS_ACCEPT)) {
			if (AVSServiceNotAvailable.equals(Constants.VERIFICATION_REVIEW)) {
				Assert.assertEquals(TransactionReason, prop.getProperty(Constants.REASON_VERIFICATION));
				Assert.assertEquals(TransactionResult, prop.getProperty(Constants.RESULT_VERIFICATION));
			} else {
				Assert.assertEquals(TransactionReason, prop.getProperty(Constants.REASON_REJECT));
				Assert.assertEquals(TransactionResult, prop.getProperty(Constants.RESULT_REJECT));
			}
		}
		if (NoAVSMatch.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& AVSServiceNotAvailable.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& !PartialAVSMatch.equals(Constants.TRANSACTION_STATUS_ACCEPT)) {
			if (PartialAVSMatch.equals(Constants.VERIFICATION_REVIEW)) {
				Assert.assertEquals(TransactionReason, prop.getProperty(Constants.REASON_VERIFICATION));
				Assert.assertEquals(TransactionResult, prop.getProperty(Constants.RESULT_VERIFICATION));
			} else {
				Assert.assertEquals(TransactionReason, prop.getProperty(Constants.REASON_REJECT));
				Assert.assertEquals(TransactionResult, prop.getProperty(Constants.RESULT_REJECT));
			}
		}
		logger.info("[checkPaymentStatusAVS] Ends");
	}

	/**
	 * This method is used to validate CSC status of a payment
	 * @param driver Webdriver object which will be used for operations
	 * @param CSCNotSubmitted String to configure setup for CSC not submitted for transaction
	 * @param CSCNotSubmittedByCardholderBank String to configure setup for CSC not submitted by cardholder bank for transaction
	 * @param CSCCheckFailed String to configure setup for CSC validation failed for transaction
	 * @param NoCSCMatch String to configure setup for CSC not matching for transaction
	 * @param TransactionReason String that contains reason code of the transaction
	 * @param TransactionResult String that contains results of transaction
	 * @param CVNCode String containing CVN code of transaction
	 */
	public void checkPaymentStatusCVN(WebDriver driver, String CSCNotSubmitted, String CSCNotSubmittedByCardholderBank,
			String CSCCheckFailed, String NoCSCMatch, String TransactionReason, String TransactionResult,
			String CVNCode) {
		logger.info("[checkPaymentStatusCVN] Starts");
		String cvCode = CVNCode.split(":")[CVNCode.split(":").length - 1].trim().replace("\"", "").trim();
		logger.info("[checkPaymentStatusCVN] CVN Code: " + cvCode);
		if (CSCNotSubmitted.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& CSCNotSubmittedByCardholderBank.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& CSCCheckFailed.equals(Constants.TRANSACTION_STATUS_ACCEPT) && !cvCode.equals("N")) {
			Assert.assertEquals(TransactionReason, prop.getProperty(Constants.REASON_SUCCESS));
			Assert.assertEquals(TransactionResult, prop.getProperty(Constants.RESULT_SUCCESS));
		}
		if (!CSCNotSubmitted.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& CSCNotSubmittedByCardholderBank.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& CSCCheckFailed.equals(Constants.TRANSACTION_STATUS_ACCEPT) && !cvCode.equals("N")) {
			if (CSCNotSubmitted.equals(Constants.VERIFICATION_REVIEW)) {
				Assert.assertEquals(TransactionReason, prop.getProperty(Constants.REASON_VERIFICATION));
				Assert.assertEquals(TransactionResult, prop.getProperty(Constants.RESULT_VERIFICATION));
			} else {
				Assert.assertEquals(TransactionReason, prop.getProperty(Constants.REASON_REJECT));
				Assert.assertEquals(TransactionResult, prop.getProperty(Constants.RESULT_REJECT));
			}
		}
		if (CSCNotSubmitted.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& !CSCNotSubmittedByCardholderBank.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& CSCCheckFailed.equals(Constants.TRANSACTION_STATUS_ACCEPT) && !cvCode.equals("N")) {
			if (CSCNotSubmittedByCardholderBank.equals(Constants.VERIFICATION_REVIEW)) {
				Assert.assertEquals(TransactionReason, prop.getProperty(Constants.REASON_VERIFICATION));
				Assert.assertEquals(TransactionResult, prop.getProperty(Constants.RESULT_VERIFICATION));
			} else {
				Assert.assertEquals(TransactionReason, prop.getProperty(Constants.REASON_REJECT));
				Assert.assertEquals(TransactionResult, prop.getProperty(Constants.RESULT_REJECT));
			}
		}
		if (CSCNotSubmitted.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& CSCNotSubmittedByCardholderBank.equals(Constants.TRANSACTION_STATUS_ACCEPT)
				&& !CSCCheckFailed.equals(Constants.TRANSACTION_STATUS_ACCEPT) && !cvCode.equals("N")) {
			if (CSCCheckFailed.equals(Constants.VERIFICATION_REVIEW)) {
				Assert.assertEquals(TransactionReason, prop.getProperty(Constants.REASON_VERIFICATION));
				Assert.assertEquals(TransactionResult, prop.getProperty(Constants.RESULT_VERIFICATION));
			} else {
				Assert.assertEquals(TransactionReason, prop.getProperty(Constants.REASON_REJECT));
				Assert.assertEquals(TransactionResult, prop.getProperty(Constants.RESULT_REJECT));
			}
		}
		if (cvCode.equals("N")) {
			Assert.assertEquals(TransactionReason, prop.getProperty(Constants.REASON_GENERAL_REJECT));
			Assert.assertEquals(TransactionResult, prop.getProperty(Constants.RESULT_REJECT));
		}
		logger.info("[checkPaymentStatusCVN] Ends");
	}

	/**
	 * This method is used to clear cache of active web browser
	 * @param driver Webdriver object which will be used for operations
	 * @param userIndex Index number of user to specify the user of the instance
	 * @return Webdriver object after clearing browser cache which will be used for operations
	 */
	public WebDriver clearBrowserCache(WebDriver driver, String userIndex) {
		logger.info("[clearBrowserCache] Starts");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		driver = setupDriver(driver, prop, Constants.CLEAR_BROWSER_DATA_URL, userIndex);
		sleep(2000);
		WebElement clearBtn = (WebElement) js.executeScript(
				"return document.querySelector(\"body > settings-ui\").shadowRoot.querySelector(\"#main\").shadowRoot.querySelector(\"settings-basic-page\").shadowRoot.querySelector(\"#basicPage > settings-section:nth-child(9) > settings-privacy-page\").shadowRoot.querySelector(\"settings-clear-browsing-data-dialog\").shadowRoot.querySelector(\"#clearBrowsingDataConfirm\")");
		sleep(2000);
		clearBtn.click();
		logger.info("[clearBrowserCache] Ends");
		return driver;
	}
}