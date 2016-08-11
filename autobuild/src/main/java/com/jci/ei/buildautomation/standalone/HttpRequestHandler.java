package com.jci.ei.buildautomation.standalone;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import com.jci.ei.buildautomation.services.BuildAutomationServiceImpl;
import com.jci.ei.buildautomation.services.PropertyHolder;
import com.jci.ei.buildautomation.webservice.BuildAndDeployServiceResponse;
import com.jci.ei.buildautomation.webservice.BuildServiceResponse;
import com.jci.ei.buildautomation.webservice.DeployServiceResponse;
import com.jci.jftt.http.HTTPHelper;
import com.jci.jftt.http.HTTPResult;
import com.jci.jftt.http.HTTPTracePoint;
import com.jci.jftt.http.RemoteRequestException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HttpRequestHandler implements HttpHandler 
{
	public static final String taskName="AutomatedBuildHttpHandler";
	public static BuildAutomationServiceImpl service = null;
	public static PropertyHolder propHolder = null;

	public static String initialize(String configPath)
	{
		service = new BuildAutomationServiceImpl();
		propHolder = new PropertyHolder();
		
		String res = propHolder.loadProperties(configPath);
		if (res == null)
		{
			service.setPropertyHolder(propHolder);
		}
		return res;
	}
	
	public void handle(HttpExchange he) throws IOException 
	{
		//Logger.log(LogSeverity.DEBUG, taskName, 0, "Received HTTP request");
		String URL = he.getRequestURI().toString();
		Headers responseHeaders = he.getResponseHeaders();
		OutputStream responseBody = he.getResponseBody();
		
		if (URL.startsWith("/favicon.ico"))
		{
			he.sendResponseHeaders(404, 0);
			he.close();
			return;
		}
		
		//Logger.log(LogSeverity.DEBUG, taskName, 0, "Got HTTP request from "+
		//		he.getRemoteAddress().getAddress().getHostAddress().toString());
		//Logger.log(LogSeverity.DEBUG, taskName, 0, "URL: "+URL);
		
		HTTPResult r = HTTPHelper.parse(taskName, he);
		System.out.println(r.toXML(HTTPTracePoint.ENTRY,taskName,getHttpIdentity()));

		
		if (r.isQueryValid == false)
		{
			responseHeaders.set("Content-Type", "text/xml");
			he.sendResponseHeaders(400, 0);
			
			responseBody.write(r.toXML(HTTPTracePoint.ENTRY,taskName,getHttpIdentity()).getBytes());
			
			responseBody.flush();
			responseBody.close();
			return;
		}

		
		String op = r.parameters.getProperty("operation");

		if (op != null)
		{
			try
			{
				if (op.equals("Build"))
				{
					http_build(r, he, responseHeaders, responseBody);
					return;
				}
						
				if (op.equals("Deploy"))
				{
					http_deploy(r, he, responseHeaders, responseBody);
					return;
				}
				
				if (op.equals("Build And Deploy"))
				{
					http_buildAndDeploy(r, he, responseHeaders, responseBody);
					return;
				}			
			} catch (RemoteRequestException rre)
			{
				responseHeaders.set("Content-Type", "text/xml");
				he.sendResponseHeaders(500, 0);

				System.err.println(Arrays.toString(rre.getStackTrace()));
				
				r.operationResult = rre.reason;
				r.operationCallSuccessful = false;
				
				responseBody.write(r.toXML(HTTPTracePoint.EXIT,taskName,getHttpIdentity()).getBytes());
				
				responseBody.flush();
				responseBody.close();
			}			
		}
		
				
		responseHeaders.set("Content-Type", "text/html");
		he.sendResponseHeaders(200, 0);
		
		responseBody.write("<html><head><title>Automated Build Standalone</title></head><body><form action=\"submit.request\" method=\"GET\">".getBytes());
		responseBody.write("<p>Operation<select name=\"operation\"><option id=\"build\">Build</option><option id=\"deploy\">Deploy</option><option id=\"buildAndDeploy\">Build And Deploy</option></select></p>".getBytes());
		responseBody.write("<p>PCA # <input type=\"text\" name=\"pca\"/></p>".getBytes());
		responseBody.write("<p>Environment <select name=\"env\"><option>DEV</option><option>QA</option><option>PROD</option></select></p>".getBytes());
		responseBody.write("<p><input type=\"submit\" value=\"Submit\"/></p>".getBytes());
		responseBody.write("</form></body></html>".getBytes());
		
		responseBody.flush();
		responseBody.close();
		
	}
	
	private void http_build(HTTPResult r, HttpExchange he,
			Headers responseHeaders, OutputStream responseBody) throws IOException, RemoteRequestException 
	{
		
		r.isQueryValid = true;
		r.processingDescription = null;
		
		r.checkMandatoryParameter("pca");
		r.checkMandatoryParameter("env");
		
		if (service == null)
		{
			r.operationCallSuccessful = false;
			r.operationResult = "Service not initialized";
		} else {
			BuildServiceResponse res = null;
			try {
				res = service.build(r.parameters.getProperty("pca"),r.parameters.getProperty("env"));
			} catch (RuntimeException re)
			{
				System.err.println(Arrays.toString(re.getStackTrace()));
				throw new RemoteRequestException("RuntimeException on build: "+re.getMessage());
			} catch (Exception e)
			{
				System.err.println(Arrays.toString(e.getStackTrace()));
				throw new RemoteRequestException("Exception on build: "+e.getMessage());
			}
			
			r.operationResult = res.getResponseMessage();
		}
		
		
		r.operationCallSuccessful = true;
		//r.operationResult = "";
		
		String out = r.toXML(HTTPTracePoint.EXIT,taskName,getHttpIdentity());
		//Logger.log(LogSeverity.INFO, taskName, 0, out);
		
		responseHeaders.set("Content-Type", "text/xml");
		he.sendResponseHeaders(200, 0);
					
		responseBody.write(out.getBytes());
		
		responseBody.flush();
		responseBody.close();

		return;
		
	}

	private void http_deploy(HTTPResult r, HttpExchange he,
			Headers responseHeaders, OutputStream responseBody) throws IOException, RemoteRequestException 
	{
		
		r.isQueryValid = true;
		r.processingDescription = null;
		
		r.checkMandatoryParameter("pca");
		r.checkMandatoryParameter("env");
		
		if (service == null)
		{
			r.operationCallSuccessful = false;
			r.operationResult = "Service not initialized";
		} else {
			DeployServiceResponse res = null;
			try {
				res = service.deploy(r.parameters.getProperty("pca"),r.parameters.getProperty("env"));
			} catch (RuntimeException re)
			{
				System.err.println(Arrays.toString(re.getStackTrace()));
				throw new RemoteRequestException("RuntimeException on build: "+re.getMessage());
			} catch (Exception e)
			{
				System.err.println(Arrays.toString(e.getStackTrace()));
				throw new RemoteRequestException("Exception on build: "+e.getMessage());
			}
			
			r.operationResult = res.getResponseMessage();
		}
		
		
		r.operationCallSuccessful = true;
		//r.operationResult = "";
		
		String out = r.toXML(HTTPTracePoint.EXIT,taskName,getHttpIdentity());
		
		responseHeaders.set("Content-Type", "text/xml");
		he.sendResponseHeaders(200, 0);
					
		responseBody.write(out.getBytes());
		
		responseBody.flush();
		responseBody.close();

		return;
		
	}

	private void http_buildAndDeploy(HTTPResult r, HttpExchange he,
			Headers responseHeaders, OutputStream responseBody) throws IOException, RemoteRequestException 
	{
		
		r.isQueryValid = true;
		r.processingDescription = null;

		r.checkMandatoryParameter("pca");
		r.checkMandatoryParameter("env");
		
		if (service == null)
		{
			r.operationCallSuccessful = false;
			r.operationResult = "Service not initialized";
		} else {
			BuildAndDeployServiceResponse res = null;
			try {
				res = service.buildAndDeploy(r.parameters.getProperty("pca"),r.parameters.getProperty("env"));
			} catch (RuntimeException re)
			{
				System.err.println(Arrays.toString(re.getStackTrace()));
				throw new RemoteRequestException("RuntimeException on build: "+re.getMessage());
			} catch (Exception e)
			{
				System.err.println(Arrays.toString(e.getStackTrace()));
				throw new RemoteRequestException("Exception on build: "+e.getMessage());
			}
			
			r.operationResult = res.getResponseMessage();
		}
		
		
		r.operationCallSuccessful = true;
		//r.operationResult = "";
		
		String out = r.toXML(HTTPTracePoint.EXIT,taskName,getHttpIdentity());
		
		responseHeaders.set("Content-Type", "text/xml");
		he.sendResponseHeaders(200, 0);
					
		responseBody.write(out.getBytes());
		
		responseBody.flush();
		responseBody.close();

		return;
		
	}

	private String getHttpIdentity() 
	{
		return "automatedBuildStandalone=\"true\"";
	}
	
}
