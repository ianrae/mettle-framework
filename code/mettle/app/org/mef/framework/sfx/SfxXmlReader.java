package org.mef.framework.sfx;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.Charset;

public class SfxXmlReader {

	public SfxXmlReader()
	{
	}
	
	public Document read(String xmlContentString) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		StringReader sr = new StringReader(xmlContentString);
		InputSource inputSource = new InputSource(sr);
		Document doc = builder.parse(inputSource);
		return doc;
	}	
	
	public Document readFromFile(String path) throws Exception
	{
		return read(new File(path));
	}

	public Document read(File path) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		FileInputStream fis = new FileInputStream(path);
//		writer = new OutputStreamWriter(outputStream, Charset.forName("UTF-8"));
		InputStreamReader sr = new InputStreamReader(new BufferedInputStream(fis), Charset.forName("UTF-8"));
		
		InputSource inputSource = new InputSource(sr);
		Document doc = builder.parse(inputSource);
		return doc;
	}	

	public Document read(InputStream inputStream) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		InputStreamReader sr = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

		InputSource inputSource = new InputSource(sr);
		Document doc = builder.parse(inputSource);
		return doc;
	}
}
