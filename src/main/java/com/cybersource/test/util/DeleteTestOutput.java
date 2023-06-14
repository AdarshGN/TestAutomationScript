package com.cybersource.test.util;

import java.io.File;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeSuite;

/**
 * DeleteTestOutput which is used clear the existing data from output directory
 * @author AD20243779
 *
 */
public class DeleteTestOutput {
	final static Logger logger = Logger.getLogger(DeleteTestOutput.class);
	
	 @BeforeSuite
	 /**
	  * This method is used to clear the test output directory before a suite is run
	  */
	 public static void deleteTestOutPutDirectory() {
		 logger.info(Constants.TEST_OUTPUT_DELETE_IS_CALLED);
		 boolean isDeleted = deleteTestOutput(new File(System.getProperty(Constants.USER_DIR)+Constants.TEST_OUTPUT_DIRECTORY));
		 if(isDeleted) {
			 logger.info(Constants.TEST_OUTPUT_DELETED);
		 }
		 else {
			 logger.info(Constants.ERROR_DELETING_TEST_OUTPUT);
		 }		 
	 }
	
	 /**
	  * This method is used to delete the test output file
	  * @param dir File directory where test output file is stored
	  * @return Boolean that returns status of delete operation
	  */
	 public static boolean deleteTestOutput(File dir) {
		 boolean success = false;
		 String[] children = null;
	      if (dir.isDirectory()) {
	         children = dir.list();
	         for (int i = 0; i < children.length; i++) {
	            success = deleteTestOutput (new File(dir, children[i]));	            
	            if (!success) {
	               return false;
	            }
	         }
	      }
	      return dir.delete();
	   }
}
