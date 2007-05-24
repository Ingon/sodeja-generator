package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlModel;
import org.sodeja.generator.uml.UmlReference;
import org.sodeja.generator.uml.UmlTagDefinition;
import org.sodeja.generator.uml.UmlTagValue;
import org.xml.sax.Attributes;

public class UmlTagDefinitionParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(parent instanceof UmlModel) {
			UmlTagDefinition element = new UmlTagDefinition();
			fill(element, attributes);
			context.push(element);
			
			((UmlModel) parent).getTags().add(element);
		} else if(parent instanceof UmlTagValue) {
			UmlReference<UmlTagDefinition> reference = createReference(context, UmlTagDefinition.class, attributes);
			((UmlTagValue) parent).setTag(reference);
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public void end(XmiParser context, String content) {
		if(context.peek() instanceof UmlTagDefinition) {
			super.end(context, content);
		}
	}
}
