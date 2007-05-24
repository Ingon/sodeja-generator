package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlModel;
import org.sodeja.generator.uml.UmlPackage;
import org.xml.sax.Attributes;

public class UmlPackageParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		
		UmlPackage element = new UmlPackage(parent);
		fill(element, attributes);
		
		if(parent instanceof UmlModel) {
			((UmlModel) parent).getPackages().add(element);
		} else if(parent instanceof UmlPackage) {
			((UmlPackage) parent).getSubpackages().add(element);
		} else {
			throw new RuntimeException();
		}
		
		context.push(element);
	}
}
