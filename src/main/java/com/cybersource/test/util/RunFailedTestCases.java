package com.cybersource.test.util;

import java.util.ArrayList;

import java.util.List;

import org.testng.TestNG;

/**
 * RunFailedTestCases class contains methods that helps to rerun failed test cases
 * @author AD20243779
 *
 */
public class RunFailedTestCases {
	CommonUtil commonUtil = new CommonUtil();
	static int runLimit = 3;

	/**
	 * This method is used to get path of failed testcases to run them again
	 * @param args Arguments of main function
	 */
	public void main(String[] args) {
		String faildTestCasePath = commonUtil.getFaildTestCasePath();
		if (commonUtil.isValueNotNull(faildTestCasePath)) {
			for (int i = 0; i < runLimit; i++) {
				TestNG runner = new TestNG();
				List<String> list = new ArrayList<String>();
				list.add(faildTestCasePath);
				runner.setTestSuites(list);
				runner.run();
			}
		}
	}

}
