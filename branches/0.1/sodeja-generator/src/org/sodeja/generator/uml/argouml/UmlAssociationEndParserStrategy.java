package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlAggregationType;
import org.sodeja.generator.uml.UmlAssociation;
import org.sodeja.generator.uml.UmlAssociationEnd;
import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlOrdering;
import org.xml.sax.Attributes;

public class UmlAssociationEndParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(! (parent instanceof UmlAssociation)) {
			throw new RuntimeException();
		}
		
		UmlAssociationEnd element = new UmlAssociationEnd();
		fill(element, attributes);
		fillAdditional(element, attributes);
		context.push(element);
		
		UmlAssociation association = (UmlAssociation) parent;
		if(association.getFirst() == null) {
			association.setFirst(element);
		} else {
			association.setSecond(element);
		}
	}

	private void fillAdditional(UmlAssociationEnd element, Attributes attributes) {
		element.setNavigateale(Boolean.parseBoolean(attributes.getValue("isNavigable")));
		element.setType(UmlAggregationType.valueOf(attributes.getValue("aggregation").toUpperCase()));
		element.setOrdering(UmlOrdering.valueOf(attributes.getValue("ordering").toUpperCase()));
	}
}
