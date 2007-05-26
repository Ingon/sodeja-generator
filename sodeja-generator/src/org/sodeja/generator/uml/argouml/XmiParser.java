package org.sodeja.generator.uml.argouml;

import java.io.File;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlModel;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmiParser extends DefaultHandler {
	
	public static UmlModel parseXmi(File file) {
		return parse(new InputSource(file.toURI().toASCIIString()));
	}
	
	public static UmlModel parseZargo(File file) {
		try {
			String name = file.getName();
			String modelName = name.substring(0, name.lastIndexOf('.'));
			System.out.println("ModelName: " + modelName);
			
			ZipFile zip = new ZipFile(file);
			ZipEntry entry = zip.getEntry(modelName + ".xmi");
			
			return parse(new InputSource(zip.getInputStream(entry)));
		} catch(Exception exc) {
			throw new RuntimeException(exc);
		}
	}
	
	private static UmlModel parse(InputSource is) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			
			XmiParser xmiParser = new XmiParser();
			parser.parse(is, xmiParser);
			return (UmlModel) xmiParser.getResult();
		} catch(Exception exc) {
			throw new RuntimeException(exc);
		}
	}
	
	private Map<String, XmiParserStrategy> mapping = new HashMap<String, XmiParserStrategy>();
	private StringBuilder content = new StringBuilder();
	
	private Deque<UmlElement> stack = new LinkedList<UmlElement>();
	private Deque<String> debugStack = new LinkedList<String>();
	
	private UmlElement result;
	
	public XmiParser() {
		mapping.put("UML:MultiplicityRange", new UmlMultiplicityRangeParserStrategy());
		
		mapping.put("UML:Model", new UmlModelParserStrategy());
		mapping.put("UML:DataType", new UmlDataTypeParserStrategy());
		mapping.put("UML:Stereotype", new UmlStereotypeParserStrategy());
		
		mapping.put("UML:Package", new UmlPackageParserStrategy());
		mapping.put("UML:Class", new UmlClassParserStrategy());
		mapping.put("UML:Interface", new UmlInterfaceParserStrategy());
		
		mapping.put("UML:TagDefinition", new UmlTagDefinitionParserStrategy());
		mapping.put("UML:TaggedValue", new UmlTagValueParserStrategy());
		mapping.put("UML:TaggedValue.dataValue", new UmlTagValueValueParserStrategy());
		
		mapping.put("UML:Attribute", new UmlAttributeParserStrategy());
		
		mapping.put("UML:Operation", new UmlOperationParserStrategy());
		mapping.put("UML:Parameter", new UmlParameterParserStrategy());
		
		mapping.put("UML:Association", new UmlAssociationParserStrategy());
		mapping.put("UML:AssociationEnd", new UmlAssociationEndParserStrategy());
		
		mapping.put("UML:Generalization", new UmlGeneralizationParserStrategy());
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		debugStack.push(qName);
		XmiParserStrategy strategy = mapping.get(qName);
		if(strategy == null) {
			return;
		}
		strategy.begin(this, attributes);
		content.setLength(0);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		content.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		XmiParserStrategy strategy = mapping.get(qName);
		if(strategy == null) {
			return;
		}
		strategy.end(this, content.toString());
		debugStack.pop();
	}

	protected void push(UmlElement element) {
		if(result != null) {
			throw new IllegalStateException();
		}
		stack.addFirst(element);
	}
	
	protected UmlElement pop() {
		UmlElement element = stack.removeFirst();
		if(stack.isEmpty()) {
			result = element;
		}
		return element;
	}
	
	protected UmlElement peek() {
		return stack.peekFirst();
	}
	
	protected String getParentTagName() {
		String head = debugStack.pop();
		String parent = debugStack.peek();
		debugStack.push(head);
		return parent;
	}
	
	protected UmlModel getModel() {
		return (UmlModel) stack.getLast();
	}
	
	public UmlElement getResult() {
		return result;
	}
}
