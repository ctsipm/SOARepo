package com.jci.ei.buildautomation.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnzipUtil {

	public static void main(String[] args) {
        
		String zipFilePath = "E:/I1208.zip";
        String destDir = "C:/build/code/MessageSet";
        
        File zipfile = new File(zipFilePath);
        File directory = new File(destDir);
         
        try {
			unzip(zipfile, directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
    }
	
    private static void unzip(File zipfile, File directory) throws IOException {
        ZipFile zfile = new ZipFile(zipfile);
        Enumeration<? extends ZipEntry> entries = zfile.entries();
        while (entries.hasMoreElements()) {
          ZipEntry entry = entries.nextElement();
          File file = new File(directory, entry.getName());
          if (entry.isDirectory()) {
            file.mkdirs();
          } else {
            file.getParentFile().mkdirs();
            InputStream in = zfile.getInputStream(entry);
            try {
              copy(in, file);
            } finally {
              in.close();
            }
          }
        }
      }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
          int readCount = in.read(buffer);
          if (readCount < 0) {
            break;
          }
          out.write(buffer, 0, readCount);
        }
      }

      @SuppressWarnings("unused")
	private static void copy(File file, OutputStream out) throws IOException {
        InputStream in = new FileInputStream(file);
        try {
          copy(in, out);
        } finally {
          in.close();
        }
      }

    private static void copy(InputStream in, File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        try {
          copy(in, out);
        } finally {
          out.close();
        }
      }
}