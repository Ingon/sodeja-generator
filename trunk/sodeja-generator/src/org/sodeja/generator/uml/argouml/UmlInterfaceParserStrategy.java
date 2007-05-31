package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlAssociationEnd;
import org.sodeja.generator.uml.UmlAttribute;
import org.sodeja.generator.uml.UmlDependency;
import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlGeneralization;
import org.sodeja.generator.uml.UmlInterface;
import org.sodeja.generator.uml.UmlNamespace;
import org.sodeja.generator.uml.UmlParameter;
import org.sodeja.generator.uml.UmlReference;
import org.xml.sax.Attributes;

public class UmlInterfaceParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(parent instanceof UmlNamespace) {
			UmlInterface element = new UmlInterface((UmlNamespace) parent);
			fill(element, attributes);
			context.push(element);
			
			((UmlNamespace) parent).getInterfaces().add(element);
		} else if(parent instanceof UmlAttribute) {
			UmlReference<UmlInterface> reference = createReference(context, UmlInterface.class, attributes);
			((UmlAttribute) parent).setType(reference);
		} else if(parent instanceof UmlAssociationEnd) {
			UmlReference<UmlInterface> reference = createReference(context, UmlInterface.class, attributes);
			((UmlAssociationEnd) parent).setReferent(reference);
		} else if(parent instanceof UmlGeneralization) {
			UmlReference<UmlInterface> reference = createReference(context, UmlInterface.class, attributes);
			String xmlParent = context.getParentTagName();
			if(xmlParent.equals("UML:Generalization.child")) {
				((UmlGeneralization) parent).setChild(reference);
			} else {
				((UmlGeneralization) parent).setParent(reference);
			}
		} else if(parent instanceof UmlParameter) {
			UmlReference<UmlInterface> reference = createReference(context, UmlInterface.class, attributes);
			((UmlParameter) parent).setType(reference);
		} else if(parent instanceof UmlDependency) {
			UmlReference<UmlInterface> reference = createReference(context, UmlInterface.class, attributes);
			String xmlParent = context.getParentTagName();
			if(xmlParent.equals("UML:Dependency.client")) {
				((UmlDependency) parent).setClient(reference);
			} else {
				((UmlDependency) parent).setSupplier(reference);
			}
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public void end(XmiParser context, String content) {
		if(context.peek() instanceof UmlInterface) {
			super.end(context, content);
		}
	}
}
