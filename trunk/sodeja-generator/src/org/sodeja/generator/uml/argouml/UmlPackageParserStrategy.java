package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlNamespace;
import org.sodeja.generator.uml.UmlPackage;
import org.xml.sax.Attributes;

public class UmlPackageParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlNamespace parent = (UmlNamespace) context.peek();
		
		UmlPackage element = new UmlPackage(parent);
		fill(element, attributes);
		parent.getChildren().add(element);
		
		context.push(element);
	}
}
