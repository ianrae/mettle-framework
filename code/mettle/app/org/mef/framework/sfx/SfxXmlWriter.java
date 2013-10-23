package org.mef.framework.sfx;

import org.w3c.dom.Document;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;

public class SfxXmlWriter {
	public SfxXmlWriter()
	{
	}

	public String writeXMLToString(Document doc) throws Exception
	{
		//write it out
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();

		DOMSource source = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);
		transformer.transform(source, result);

		return writer.toString();
	}
	
	public void writeXml(Document doc, String path) throws Exception 
	{
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    DOMSource source = new DOMSource(doc);
        File ofile = new File(path);
        Result result = new StreamResult(ofile);
		transformer.transform(source, result);
	}
		
}
