package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlAttribute;
import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlEnumeration;
import org.sodeja.generator.uml.UmlGeneralization;
import org.sodeja.generator.uml.UmlNamespace;
import org.sodeja.generator.uml.UmlParameter;
import org.sodeja.generator.uml.UmlReference;
import org.xml.sax.Attributes;

public class UmlEnumerationParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(parent instanceof UmlNamespace) {
			UmlEnumeration element = new UmlEnumeration((UmlNamespace) parent);
			fill(element, attributes);
			context.push(element);
			
			((UmlNamespace) parent).getEnumerations().add(element);
		} else if(parent instanceof UmlAttribute) {
			UmlReference<UmlEnumeration> reference = createReference(context, UmlEnumeration.class, attributes);
			((UmlAttribute) parent).setType(reference);
		} else if(parent instanceof UmlGeneralization) {
			UmlReference<UmlEnumeration> reference = createReference(context, UmlEnumeration.class, attributes);
			String xmlParent = context.getParentTagName();
			if(xmlParent.equals("UML:Generalization.child")) {
				((UmlGeneralization) parent).setChild(reference);
			} else {
				((UmlGeneralization) parent).setParent(reference);
			}
		} else if(parent instanceof UmlParameter) {
			UmlReference<UmlEnumeration> reference = createReference(context, UmlEnumeration.class, attributes);
			((UmlParameter) parent).setType(reference);
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public void end(XmiParser context, String content) {
		if(context.peek() instanceof UmlEnumeration) {
			super.end(context, content);
		}
	}
}
