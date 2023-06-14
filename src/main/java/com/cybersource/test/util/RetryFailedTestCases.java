package com.cybersource.test.util;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryFailedTestCases class contains method to handle failed cases by re running again 
 * @author AD20243779
 *
 */
public class RetryFailedTestCases implements IRetryAnalyzer {

	int counter = 1;
	// total retry attempt if a test case fails.
	int retryLimit = 3;

	/**
	 * This method is used to check if a failed testcase should be retried again
	 */
	public boolean retry(ITestResult result) {
		if (counter < retryLimit) {
			counter++;
			return true;
		}
		return false;
	}
}