package com.cybersource.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.TestNG;

/**
 * TestRunner class is used to replicate the TestNG suite running process in JAVA format
 * @author AD20243779
 *
 */
public class TestRunner {
	final static Logger logger = Logger.getLogger(TestRunner.class);

	/**
	 * This method is used to replicate the TestNG suite running process in JAVA format
	 * @param args Argument for main function containing xml file name
	 */
	public static void main(String[] args) {
		logger.info("[TestRunnner] [main] Starts");
		String xmlpathPrefix = "./resources/testcases/netsuite.test.xmls/positive.case/";
		logger.info("Argument Passed: " + args[0]);
		TestNG testng = new TestNG();
		List<String> suites = new ArrayList<String>();
		suites.add(xmlpathPrefix + args[0]);
		logger.info("Suite Path Passed: " + xmlpathPrefix + args[0]);
		testng.setTestSuites(suites);
		testng.run();
		logger.info("[TestRunner] [main] Ends");
	}

}
