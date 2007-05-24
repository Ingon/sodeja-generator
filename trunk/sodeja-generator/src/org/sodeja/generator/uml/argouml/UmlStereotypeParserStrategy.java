package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlModel;
import org.sodeja.generator.uml.UmlReference;
import org.sodeja.generator.uml.UmlStereotype;
import org.xml.sax.Attributes;

public class UmlStereotypeParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(parent instanceof UmlModel) {
			UmlStereotype element = new UmlStereotype();
			fill(element, attributes);
			context.push(element);
			
			((UmlModel) parent).getStereotypes().add(element);
		} else if(parent instanceof UmlClass) {
			UmlReference<UmlStereotype> reference = createReference(context, UmlStereotype.class, attributes);
			((UmlClass) parent).setStereotype(reference);
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public void end(XmiParser context, String content) {
		if(context.peek() instanceof UmlStereotype) {
			super.end(context, content);
		}
	}
}
