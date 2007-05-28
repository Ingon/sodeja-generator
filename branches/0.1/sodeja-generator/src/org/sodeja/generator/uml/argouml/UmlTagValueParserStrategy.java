package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlTagValue;
import org.sodeja.generator.uml.UmlTaggableElement;
import org.xml.sax.Attributes;

public class UmlTagValueParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(! (parent instanceof UmlTaggableElement)) {
			throw new RuntimeException();
		}
		
		UmlTagValue element = new UmlTagValue();
		fill(element, attributes);
		context.push(element);
		
		((UmlTaggableElement) parent).getTags().add(element);
	}
}
