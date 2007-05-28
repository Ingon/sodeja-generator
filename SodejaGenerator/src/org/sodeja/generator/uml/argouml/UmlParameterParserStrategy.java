package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlOperation;
import org.sodeja.generator.uml.UmlParameter;
import org.xml.sax.Attributes;

public class UmlParameterParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(! (parent instanceof UmlOperation)) {
			throw new IllegalArgumentException();
		}
		
		UmlParameter element = new UmlParameter();
		fill(element, attributes);
		context.push(element);
		
		UmlOperation operation = (UmlOperation) parent;
		String attribute = attributes.getValue("kind");
		if(attribute == null) {
			operation.getParameters().add(element);
		} else if(attribute.equals("return")) {
			operation.setResult(element);
		} else {
			throw new RuntimeException();
		}
	}
}
