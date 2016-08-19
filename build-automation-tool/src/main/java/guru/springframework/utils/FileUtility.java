package guru.springframework.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtility {
	
	/*public static void main(String[] args) {
		
		File[] file = listf("D:\\Ashrujit\\Workspace\\AddressBookMessageSet");
		
	}*/
	
	public static File[] listf(String directoryName) {

	    // .............list file
	    File directory = new File(directoryName);

	    // get all the files from a directory
	    File[] fList = directory.listFiles();
	    
	    List<String> fileNames = new ArrayList<String>();

	    for (File file : fList) {
	        if (file.isFile()) {
	            System.out.println(file.getAbsolutePath());
	            fileNames.add(file.getName());
	        } else if (file.isDirectory()) {
	            listf(file.getAbsolutePath());
	        }
	    }
	    return fList;
	}  
	
	
	public static String readFile(String path) throws IOException{
		
		String sCurrentLine = null;
        StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(path))){		
		  
			while ((sCurrentLine = br.readLine()) != null) {
		        sb.append(sCurrentLine);
		    }
		
		}
		
		      return sb.toString();
		}

}
