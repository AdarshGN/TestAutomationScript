package com.cybersource.test.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * FileUtil class contains all the utilities related to files used for the project
 * @author AD20243779
 *
 */
public class FileUtil {
	static Properties prop = null;
	// static Xls_Reader xls = null;
	private static XlsReader xlsNetSuite = null;
	static final Logger logger = Logger.getLogger(FileUtil.class);

	/**
	 * This method is used to load configuration for the technology
	 * @param tech String containing technology for which test is running
	 * @return properties file which is configured for all xpath, user related data
	 * @throws IOException Used to handle any exceptions that may happen
	 */
	public static Properties loadConfig(String tech) throws IOException {
		prop = new Properties();
		String techStr = (null != tech) ? tech + Constants.UNDERSCORE : Constants.EMPTY_STRING;
		logger.info(Constants.CONFIG_FILE_EQUAL + Constants.RESOURCES_FORWARD_SLASH + techStr
				+ Constants.CONFIG_PROPERTIES);
		FileInputStream fs = new FileInputStream(
				Constants.RESOURCES_FORWARD_SLASH + techStr + Constants.CONFIG_PROPERTIES);
		prop.load(fs);
		return prop;

	}

	/**
	 * This method is used to get details from excel sheet
	 * @param fileName String containing file name of the excel
	 * @param sheetName String containing sheet name from which data is to be fetched
	 * @return Object which contains data stored in the worksheet
	 */
	public static Object[][] getSheetData(String fileName, String sheetName) {
		logger.info("[FileUtil][getSheetData]  NETSUITE?:" + fileName.contains(Constants.TECH_NETSUITE));
		if (null != fileName) {
			if (fileName.contains(Constants.TECH_NETSUITE)) {
				if (null == xlsNetSuite) {
					logger.info("Loading NetSuite specific XLSX for one time");
					logger.info("[FileUtil][getSheetData] fileName = " + fileName);
					xlsNetSuite = new XlsReader(fileName);
				}
				logger.info(Constants.ASSIGNED_NETSUITE_SPECIFIC_XLSX);
				// xls = xlsNetSuite;
			}
		}
		logger.info(Constants.FILENAME_EQUAL + fileName);
		logger.info(Constants.SHEETNAME_EQUAL + sheetName);

		int cols = xlsNetSuite.getColumnCount(sheetName);
		int rows = xlsNetSuite.getRowCount(sheetName);
		logger.info(Constants.ROW_COUNT_EQUAL + rows);
		logger.info(Constants.COL_COUNT_EQUAL + cols);
		Object data[][] = new Object[rows - 1][cols];
		for (int rNum = 2; rNum <= rows; rNum++) {
			for (int cNum = 0; cNum < cols; cNum++) {
				data[rNum - 2][cNum] = xlsNetSuite.getCellData(sheetName, cNum, rNum);
			}
		}
		return data;
	}

	/**
	 * This method is used to write data to an excel file
	 * @param path String containing path of excel file
	 * @param sheetame String containing sheetname in the excel to write
	 * @param profileData String containing data to write to the file
	 */
	public static void writeDataToSheet(String path, String sheetame, Object profileData[][]) {
		try {
			FileInputStream fis = new FileInputStream(path);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			FileOutputStream fos = new FileOutputStream(path);
			Sheet sheet = workbook.getSheet(sheetame);
			XSSFRow row = null;
			XSSFCell cell = null;
			int cellNumber = 6;
			row = (XSSFRow) sheet.getRow(1);
			for (Object[] aProfile : profileData) {
				cellNumber = 6;
				for (Object field : aProfile) {
					cell = row.getCell(cellNumber);
					if (cell == null) {
						cell = row.createCell(cellNumber);
					}
					cell.setCellValue(field.toString());
					cellNumber++;
				}
			}
			workbook.write(fos);
		} catch (IOException e) {
			logger.info(Constants.IOEXCEPTION_OCCURRED);
			e.printStackTrace();
		}
	}

}
