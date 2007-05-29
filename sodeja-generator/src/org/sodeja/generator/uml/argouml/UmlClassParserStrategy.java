package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlAssociationEnd;
import org.sodeja.generator.uml.UmlAttribute;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlDependency;
import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlGeneralization;
import org.sodeja.generator.uml.UmlPackage;
import org.sodeja.generator.uml.UmlParameter;
import org.sodeja.generator.uml.UmlReference;
import org.xml.sax.Attributes;

public class UmlClassParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(parent instanceof UmlPackage) {
			UmlClass element = new UmlClass((UmlPackage) parent);
			fill(element, attributes);
			context.push(element);
			
			((UmlPackage) parent).addClass(element);
		} else if(parent instanceof UmlAttribute) {
			UmlReference<UmlClass> reference = createReference(context, UmlClass.class, attributes);
			((UmlAttribute) parent).setType(reference);
		} else if(parent instanceof UmlAssociationEnd) {
			UmlReference<UmlClass> reference = createReference(context, UmlClass.class, attributes);
			((UmlAssociationEnd) parent).setReferent(reference);
		} else if(parent instanceof UmlGeneralization) {
			UmlReference<UmlClass> reference = createReference(context, UmlClass.class, attributes);
			String xmlParent = context.getParentTagName();
			if(xmlParent.equals("UML:Generalization.child")) {
				((UmlGeneralization) parent).setChild(reference);
			} else {
				((UmlGeneralization) parent).setParent(reference);
			}
		} else if(parent instanceof UmlParameter) {
			UmlReference<UmlClass> reference = createReference(context, UmlClass.class, attributes);
			((UmlParameter) parent).setType(reference);
		} else if(parent instanceof UmlDependency) {
			UmlReference<UmlClass> reference = createReference(context, UmlClass.class, attributes);
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
		if(context.peek() instanceof UmlClass) {
			super.end(context, content);
		}
	}
}
