package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlGeneralization;
import org.sodeja.generator.uml.UmlNamespace;
import org.sodeja.generator.uml.UmlReference;
import org.sodeja.generator.uml.UmlType;
import org.xml.sax.Attributes;

public class UmlGeneralizationParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(parent instanceof UmlNamespace) {
			UmlGeneralization element = new UmlGeneralization();
			fill(element, attributes);
			context.push(element);
			
			((UmlNamespace) parent).getGeneratlizations().add(element);
		} else if(parent instanceof UmlType) {
			UmlReference<UmlGeneralization> reference = createReference(context, UmlGeneralization.class, attributes);
			((UmlType) parent).getGeneralizations().add(reference);
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public void end(XmiParser context, String content) {
		if(context.peek() instanceof UmlGeneralization) {
			super.end(context, content);
		}
	}
}
