package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlDependency;
import org.sodeja.generator.uml.UmlDependencyType;
import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlNamespace;
import org.sodeja.generator.uml.UmlReference;
import org.sodeja.generator.uml.UmlType;
import org.xml.sax.Attributes;

public class UmlDependencyParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(parent instanceof UmlNamespace) {
			UmlDependency element = new UmlDependency();
			fill(element, attributes);
			context.push(element);
			
			String tag = context.getTagName();
			if(tag.equals("UML:Dependency")) {
				element.setType(UmlDependencyType.NONE);
			} else if(tag.equals("UML:Permission")) {
				element.setType(UmlDependencyType.PERMISSION);
			} else if(tag.equals("UML:Usage")) {
				element.setType(UmlDependencyType.USAGE);
			} else {
				throw new IllegalArgumentException();
			}
			
			((UmlNamespace) parent).getDependencies().add(element);
		} else if(parent instanceof UmlType) {
			UmlReference<UmlDependency> reference = createReference(context, UmlDependency.class, attributes);
			((UmlType) parent).getDependencies().add(reference);
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public void end(XmiParser context, String content) {
		if(context.peek() instanceof UmlDependency) {
			super.end(context, content);
		}
	}
}
