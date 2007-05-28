package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlGeneralization;
import org.sodeja.generator.uml.UmlPackage;
import org.sodeja.generator.uml.UmlReference;
import org.xml.sax.Attributes;

public class UmlGeneralizationParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(parent instanceof UmlPackage) {
			UmlGeneralization element = new UmlGeneralization();
			fill(element, attributes);
			context.push(element);
			
			context.getModel().getGeneratlizations().add(element);
		} else if(parent instanceof UmlClass) {
			UmlReference<UmlGeneralization> reference = createReference(context, UmlGeneralization.class, attributes);
			((UmlClass) parent).setParent(reference);
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
