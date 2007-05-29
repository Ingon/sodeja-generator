package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlAssociation;
import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlNamespace;
import org.xml.sax.Attributes;

public class UmlAssociationParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(! (parent instanceof UmlNamespace)) {
			throw new RuntimeException();
		}
		
		UmlAssociation element = new UmlAssociation();
		fill(element, attributes);
		context.push(element);
		
		((UmlNamespace) parent).getAssociations().add(element);
	}
}
