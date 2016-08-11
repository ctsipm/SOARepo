package com.jci.ei.buildautomation.test;

import java.io.File;

public class BarFolderStructure {
	public static void main(String[] args) {
		
		File file = new File("MDBKRDB04Europe/");
		System.out.println(getBarFileLocation(file));
	
	}
	
	private static String getBarFileLocation(File resource){
		
		if(resource != null){
			File parentFile = resource.getParentFile();
			if(parentFile != null && "Deployment".equals(parentFile.getName())){
				return parentFile.getPath()+"\\Bar Files";
			}else{
				if (parentFile == null) {
					return "\\Bar Files";
				} else {
					return getBarFileLocation(parentFile);
				}
			}
		}
		return null;
	}	
}
