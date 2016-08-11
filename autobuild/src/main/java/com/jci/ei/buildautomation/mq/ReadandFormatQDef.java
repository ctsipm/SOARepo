package com.jci.ei.buildautomation.mq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

public class ReadandFormatQDef {

	private static Logger logger = Logger.getLogger("JCIBuildAutomation");
	
	/**
	 * Reads MQ Definition
	 * @param filenameWithPath
	 * @return String[] qdef
	 */	
	public String[] readQDef(String filenameWithPath) {
		
		File file = new File(filenameWithPath);
		BufferedReader inBuf = null;
		String line = "";
		String str = "";
		String[] mqscComm = null; 
		
		try {
			inBuf = new BufferedReader(new FileReader(file));
			while ((line = inBuf.readLine()) != null) {
				if (!line.trim().equals("") && line.trim().endsWith("+")) {
					line = line.trim();
					str = str + line.substring(0, line.trim().length()-1).trim() + " ";
				} else {
					str = str + line.trim();
					str = str.trim() + "\n";
				}
			}
			
			mqscComm = str.split("\n");
			
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (inBuf != null)
			{
				try {
					inBuf.close();
				} catch (IOException e) {
				}
			}
		}
		
		return mqscComm;
	}
}