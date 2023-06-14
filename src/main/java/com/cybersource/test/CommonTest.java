package com.cybersource.test;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.ITestContext;

import com.cybersource.test.netsuite.ConfigurePaymentProcessingProfile;
import com.cybersource.test.util.Constants;
import com.cybersource.test.util.FileUtil;

/**
 * CommonTest class have all the functionality used to load common configuration setups for all test cases
 * @author AD20243779
 *
 */
public class CommonTest {
	static final String FILE_PATH_PREFIX = "resources\\testdata\\";
	protected static String XLSX_FILE_PATH;
	static String tech = null;
	protected static Properties prop = null;
	private static Properties propNetSuite = null;
	static int count = 0;
	protected static String prefix = null;
	final static Logger logger = Logger.getLogger(ConfigurePaymentProcessingProfile.class);
	static {
		count++;
		logger.info(count + " [CommonTest][static]  tech:" + tech);
	}

	/**
	 * This method is used to load the common configuration setup for the testcases
	 * @param context ITestContest object which contains all information for a given test run and their environment
	 * @throws IOException Used to handle the exceptions that may happen
	 */
	public void loadCommonConfig(ITestContext context) throws IOException {
		count++;
		String techMasterStr = context.getCurrentXmlTest().getParameter("techMaster");
		String techStr = context.getCurrentXmlTest().getParameter("tech");
		prefix = context.getCurrentXmlTest().getParameter(Constants.PREFIX);
		tech = (null == techMasterStr) ? techStr : techMasterStr;
		logger.info(
				count + " [CommonTest][loadCommonConfig]  XML:" + context.getCurrentXmlTest().getSuite().getFileName());
		logger.info(count + " [CommonTest][loadCommonConfig]  tech:" + tech + ", Suite="
				+ context.getCurrentXmlTest().getSuite() + ",context.getName()=" + context.getName() + ", prop="
				+ prop);
		// If technology netsuite not there exit the system
		if (null == tech || !Constants.TECH_NETSUITE.equals(tech)) {
			logger.info("Since no technology configured, system is exiting, please configure in xml tech as netsuite");
			System.exit(0);
		}
		if (Constants.TECH_NETSUITE.equals(tech)) {
			if (null == propNetSuite) {
				logger.info("Loading NetSuite specific properties for one time");
				propNetSuite = FileUtil.loadConfig(tech);
			}
			prop = propNetSuite;
			logger.info("Assigned NetSuite specific properties");
		}

		if (null != prop) {
			XLSX_FILE_PATH = FILE_PATH_PREFIX + tech + Constants.UNDERSCORE + prop.getProperty("xls.filepath");
		}
		logger.info(count + " [CommonTest][loadCommonConfig]  XLSX_FILE_PATH:" + XLSX_FILE_PATH);
		logger.info(count + " [CommonTest][loadCommonConfig]  tech:" + tech + ", prop=" + prop);
	}

}
