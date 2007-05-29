package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlAttribute;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlOwnerScope;
import org.sodeja.generator.uml.UmlVisibility;
import org.xml.sax.Attributes;

public class UmlAttributeParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlClass parent = (UmlClass) context.peek();
		
		UmlAttribute element = new UmlAttribute();
		fill(element, attributes);
		
		element.setScope(UmlOwnerScope.valueOf(attributes.getValue("ownerScope").toUpperCase()));
		element.setVisibility(UmlVisibility.valueOf(attributes.getValue("visibility").toUpperCase()));
		
		parent.getAttributes().add(element);
		context.push(element);
	}
}
