package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlReference;
import org.xml.sax.Attributes;

public abstract class XmiParserStrategy {
	public abstract void begin(XmiParser context, Attributes attributes);
	
	public void end(XmiParser context, String content) {
		context.pop();
	}
	
	protected void fill(UmlElement element, Attributes attributes) {
		element.setId(attributes.getValue("xmi.id"));
		element.setName(attributes.getValue("name"));
	}

	protected <T extends UmlElement> UmlReference<T> createReference(XmiParser context, Class<T> clazz, Attributes attributes) {
		String id = attributes.getValue("xmi.idref");
		if(id == null) {
			throw new IllegalArgumentException();
		}
		return new UmlReference<T>(id, clazz, context.getModel());
	}
}
