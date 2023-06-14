package com.cybersource.test.util;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Helper class which is used help the automation test run run smoothly by waiting till webelements are ready to be interactable
 * @author AD20243779
 *
 */
public class Helper {

	/**
	 * This method is used to wait till an element will be interactable before giving a click
	 * @param driver Webdriver object which will be used for further operations
	 * @param element Webelement that is to be clicked
	 */
	public static void waitForElementToBeClickable(WebDriver driver, WebElement element) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	/**
	 * This method is used to wait till a webelemet is loaded properly till it is visible
	 * @param driver Webdriver object which will be used for further operations
	 * @param element Webelement that is to be displayed
	 */
	public static void waitForElementToBeVisible(WebDriver driver, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.visibilityOf(element));

	}

}
