package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlAssociation;
import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlPackage;
import org.xml.sax.Attributes;

public class UmlAssociationParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(! (parent instanceof UmlPackage)) {
			throw new RuntimeException();
		}
		
		UmlAssociation element = new UmlAssociation();
		fill(element, attributes);
		context.push(element);
		
		context.getModel().getAssociations().add(element);
	}
}
