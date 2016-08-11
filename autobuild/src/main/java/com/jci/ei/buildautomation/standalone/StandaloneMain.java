package com.jci.ei.buildautomation.standalone;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class StandaloneMain {

	public static void main(String[] args) 
	{
		System.out.println("This is EI automated build tool standalone mode.");
		//set up HTTP listener

		if (args.length == 0)	
		{
			System.err.println("Config path was not defined.");
			System.exit(-1);
		}
		
		String cfgPath = args[0];
		File f = new File(cfgPath);
		
		if (! f.exists())
		{
			System.err.println("Config file ["+f.getAbsolutePath()+"] does not exist.");
			System.exit(-2);
		}
		
		if (! f.canRead())
		{
			System.err.println("Config file ["+f.getAbsolutePath()+"] cannot be read.");
			System.exit(-3);
		}
		
		String res = HttpRequestHandler.initialize(cfgPath);
		if (res != null)
		{
			System.err.println("Error initializing property holder: "+res);
			System.exit(-4);
		}
		
		InetSocketAddress ia = new InetSocketAddress(10222);
		HttpServer s = null;
		try {
			s = HttpServer.create(ia, 0);
			s.createContext("/", new HttpRequestHandler());
			//s.createContext("/submit", new HttpRequestHandler());
			s.start();
		} catch (IOException e) {
			//Logger.log(LogSeverity.ERROR,"SYSTEM",-1,"Error creating HTTP listener: "+e.getMessage());
			return;
		}
		
	}

}
