package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlAbstraction;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlNamespace;
import org.sodeja.generator.uml.UmlReference;
import org.xml.sax.Attributes;

public class UmlAbstractionParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(parent instanceof UmlNamespace) {
			UmlAbstraction element = new UmlAbstraction();
			fill(element, attributes);
			context.push(element);
			
			((UmlNamespace) parent).getAbstractions().add(element);
		} else if(parent instanceof UmlClass) {
			UmlReference<UmlAbstraction> reference = createReference(context, UmlAbstraction.class, attributes);
			((UmlClass) parent).getAbstractions().add(reference);
		} else {
			throw new RuntimeException();
		}
	}
}
