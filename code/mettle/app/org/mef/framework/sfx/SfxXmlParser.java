package org.mef.framework.sfx;

import java.io.FileReader;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.xpath.XPathExpression;
import org.xml.sax.InputSource;


public class SfxXmlParser {
	private Document _doc;
	
	public SfxXmlParser(Document doc)
	{
		_doc = doc;
	}
	
	public Document getDoc()
	{
		return _doc;
	}
	
	public Element getFirstByName(Element parent, String tagName)
	{
		return this.getIthByName(parent, tagName, 0);
	}
	public Element getIthByName(Element parent, String tagName, int index)
	{
		NodeList list = parent.getElementsByTagName(tagName);
		for(int i = 0; i < list.getLength(); i++)
		{
			Node node = list.item(i);
			Element el = (Element)node;
			if (i == index)
			{
				return el;
			}
		}
		return null;
	}

	public Element getByAttribute(Document doc, String tagName, String attrName, String attrValue)
	{
		
		Element root = doc.getDocumentElement();
		NodeList list = root.getElementsByTagName(tagName);
		for(int i = 0; i < list.getLength(); i++)
		{
			Node node = list.item(i);
			Element el = (Element)node;
			String id = el.getAttribute(attrName);
			if (id.equals(attrValue))
			{
				return el;
			}
		}
		return null;
	}

	public int getAttrInt(Element el, String attrName)
	{
		String id = el.getAttribute(attrName);
		Integer nId = Integer.parseInt(id);
		return nId;
	}
	
	public Element getFirstChildEl(Element el)
	{
		Node child = el.getFirstChild();
		while(child != null)
		{
			if (child.getNodeType() == Node.ELEMENT_NODE)
			{
				return (Element)child;
			}
			child = child.getNextSibling();
		}
		return null;
	}
	public Element getNextSiblingEl(Element el)
	{
		Node child = el.getNextSibling();
		while (child != null)
		{
			if (child.getNodeType() == Node.ELEMENT_NODE)
			{
				return (Element)child;
			}
			child = child.getNextSibling();
		}
		return null;
	}
	
	public void replaceNode(Element doomed, Element newOne, boolean needsImport)
	{
		Node copy;
		if (needsImport)
		{
			copy = _doc.importNode(newOne, true);
		}
		else
		{
			copy = (Node)newOne;
		}
		
		Node parent = doomed.getParentNode();
		parent.insertBefore(copy, doomed);
		parent.removeChild(doomed);
	}
	
	public Element getByXPath(Document doc, String xpathStr) throws Exception
	{
		Element result = null;
		try
		{
			XPath xpath = XPathFactory.newInstance().newXPath();
			javax.xml.xpath.XPathExpression expr = xpath.compile(xpathStr);
			
			Object obj = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList)obj;
			
			for (int i = 0; i < nodes.getLength(); i++) 
			{
				Element el = (Element)nodes.item(i);
				if (el != null)
				{
					result = el;
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return result;
	}
	public ArrayList<Element> getAllByXPath(Document doc, String xpathStr) throws Exception
	{
		ArrayList<Element> resultL = new ArrayList<Element>();
		
		try
		{
			XPath xpath = XPathFactory.newInstance().newXPath();
			javax.xml.xpath.XPathExpression expr = xpath.compile(xpathStr);
			
			Object obj = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList)obj;
			
			for (int i = 0; i < nodes.getLength(); i++) 
			{
				Element el = (Element)nodes.item(i);
				if (el != null)
				{
					resultL.add(el);
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return resultL;
	}
}	

