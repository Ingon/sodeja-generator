package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlAttribute;
import org.sodeja.generator.uml.UmlClass;
import org.xml.sax.Attributes;

public class UmlAttributeParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlClass parent = (UmlClass) context.peek();
		
		UmlAttribute element = new UmlAttribute();
		fill(element, attributes);
		
		parent.getAttributes().add(element);
		context.push(element);
	}
}
