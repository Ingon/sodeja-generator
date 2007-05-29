package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlDependency;
import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlPackage;
import org.sodeja.generator.uml.UmlReference;
import org.xml.sax.Attributes;

public class UmlDependencyParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(parent instanceof UmlPackage) {
			UmlDependency element = new UmlDependency();
			fill(element, attributes);
			context.push(element);
			
			context.getModel().getDependencies().add(element);
		} else if(parent instanceof UmlClass) {
			UmlReference<UmlDependency> reference = createReference(context, UmlDependency.class, attributes);
			((UmlClass) parent).setDependency(reference);
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
