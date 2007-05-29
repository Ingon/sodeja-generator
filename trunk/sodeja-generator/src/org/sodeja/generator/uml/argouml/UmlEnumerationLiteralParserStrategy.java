package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlEnumeration;
import org.sodeja.generator.uml.UmlEnumerationLiteral;
import org.xml.sax.Attributes;

public class UmlEnumerationLiteralParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlEnumeration parent = (UmlEnumeration) context.peek();
		
		UmlEnumerationLiteral element = new UmlEnumerationLiteral();
		fill(element, attributes);
		parent.getLiterals().add(element);
		
		context.push(element);
	}
}
