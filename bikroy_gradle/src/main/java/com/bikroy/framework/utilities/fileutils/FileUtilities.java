package com.bikroy.framework.utilities.fileutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.bikroy.framework.globals.Logger;

/**
 * Class to perform read operations on Xml files
 * 
 * @author Aishwarya Dwivedi
 * @version 1.0
 * @since 1.0
 */
public class FileUtilities {
	// Initialize private logger object
	private static Logger logger = new Logger().getLogger(Thread
			.currentThread().getStackTrace()[1].getClassName());

	/**
	 * Reads contents from given file and returns all data as a single string
	 * 
	 * @param fileNameWithPath
	 * @return
	 * @throws Exception
	 */
	public static String FileRead(String fileNameWithPath) throws Exception {
		StringBuilder sb = null;
		FileUtilities.logger.info(fileNameWithPath);
		try (InputStream in = new FileInputStream(fileNameWithPath);
				BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
			String str = null;
			sb = new StringBuilder(8192);
			while ((str = r.readLine()) != null) {
				sb.append(str);
			}
			FileUtilities.logger.info("data from InputStream as String : " + sb.toString());
		} catch (IOException ioe) {
			FileUtilities.logger.error(ioe);
		}

		if (sb != null) {
			return sb.toString();
		}

		return null;
	}

	public static boolean FileExists(String file) {
		boolean fileExists = false;

		try {
			File f = new File(file);
			fileExists = f.exists();
		} catch (Exception e) {
			// eat exception
		}

		return fileExists;
	}

	public static boolean FileWrite(String fileNameWithPath, String content)
			throws Exception {
		File file = new File(fileNameWithPath);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			FileUtilities.logger.info("Creating new file: " + fileNameWithPath);
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();

		return true;
	}

	public static boolean FileAppend(String fileNameWithPath, String content)
			throws Exception {
		File file = new File(fileNameWithPath);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.append(content);
		bw.close();

		return true;
	}
}
