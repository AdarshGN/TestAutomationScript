package com.cybersource.test.util;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.cybersource.test.CommonTest;

/**
 * Listeners class is used to implement event handling and perform actions based on specific events occuring during test execution
 * @author AD20243779
 *
 */
public class Listeners extends CommonTest implements ITestListener {
	CommonUtil commonUtil = new CommonUtil();
	final static Logger logger = Logger.getLogger(Listeners.class);
	String userIndex = "1";

	/**
	 * This method is used to perform any action when a test is finished
	 */
	public void onFinish(ITestContext arg0) {
		Reporter.log(arg0.getName() + Constants.FINISHED);
	}

	/**
	 * This method is used to perform any action when a test is failed 
	 */
	public void onTestFailure(ITestResult arg0) {
		logger.info("[onTestFailure] Starts");
		WebDriver driver = commonUtil.getDriver(arg0.getTestContext().getCurrentXmlTest().getParameter(Constants.USER_INDEX));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date dateNow = new Date();
		String date = dateFormat.format(dateNow);
		logger.info("[onTestFailure] Date Time: " + date);
		driver = commonUtil.configureDriver(driver, prop, userIndex);
		String methodName = arg0.getName();
		logger.info("[onTestFailure] Method Failed: " + methodName);
		if (!arg0.isSuccess()) {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			try {
				logger.info(
						"[onTestFailure] Stored File Path: " + prop.getProperty(Constants.FAILED_SCREENSHOTS_DIRECTORY)
								+ methodName + Constants.UNDERSCORE + date + Constants._JPG);
				File destFile = new File(prop.getProperty(Constants.FAILED_SCREENSHOTS_DIRECTORY) + methodName
						+ Constants.UNDERSCORE + date + Constants._JPG);
				FileUtils.copyFile(scrFile, destFile);
				Reporter.log(Constants.FAILED_SCREENSHOT + Constants.A_HREF_EQUAL + destFile.getAbsolutePath()
						+ Constants.SINGLE_QUOTE_CLOSING_TAG + Constants.IMG_SRC_EQUAL + destFile.getAbsolutePath()
						+ prop.getProperty(Constants.SCREENSHOT_HEIGHT_WIDTH) + Constants.FORWARD_SLASH_CLOSING_TAG
						+ Constants.CLOSING_A_TAG);
			} catch (IOException e) {
				Reporter.log("[onTestFailure] Error in screenshot");
			}
		}
		logger.info("[onTestFailure] Ends");
	}

	/**
	 * This method is used to perform any action when a test is started
	 */
	public void onTestStart(ITestResult arg0) {
		Reporter.log(arg0.getName() + Constants.STARTS);
	}

	/**
	 * This method is used to perform any action when a test is success
	 */
	public void onTestSuccess(ITestResult arg0) {
		Reporter.log(arg0.getName() + Constants.ENDS);
	}

	/**
	 * This method is used to perform any action when a test is skipped
	 */
	public void onTestSkipped(ITestResult result) {
	}

	/**
	 * This method is used to perform any action when a test is failed, but is within success percentage defined
	 */
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

	/**
	 * This method is used to perform any action when a test is started
	 */
	public void onStart(ITestContext context) {
	}
}