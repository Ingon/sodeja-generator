package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlNamespace;
import org.sodeja.generator.uml.UmlReference;
import org.sodeja.generator.uml.UmlStereotype;
import org.sodeja.generator.uml.UmlType;
import org.xml.sax.Attributes;

public class UmlStereotypeParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(parent instanceof UmlNamespace) {
			UmlStereotype element = new UmlStereotype();
			fill(element, attributes);
			context.push(element);
			
			((UmlNamespace) parent).getStereotypes().add(element);
		} else if(parent instanceof UmlType) {
			UmlReference<UmlStereotype> reference = createReference(context, UmlStereotype.class, attributes);
			((UmlType) parent).getStereotypes().add(reference);
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
