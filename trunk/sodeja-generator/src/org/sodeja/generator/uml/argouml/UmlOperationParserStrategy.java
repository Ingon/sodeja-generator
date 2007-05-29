package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlOperation;
import org.sodeja.generator.uml.UmlOwnerScope;
import org.sodeja.generator.uml.UmlType;
import org.xml.sax.Attributes;

public class UmlOperationParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(! (parent instanceof UmlType)) {
			throw new IllegalArgumentException();
		}
		
		UmlOperation element = new UmlOperation();
		fill(element, attributes);
		element.setScope(UmlOwnerScope.valueOf(attributes.getValue("ownerScope").toUpperCase()));
		context.push(element);
		
		((UmlType) parent).getOperations().add(element);
	}
}
