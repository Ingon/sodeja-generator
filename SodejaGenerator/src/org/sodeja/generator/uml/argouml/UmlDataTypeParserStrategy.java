package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlAttribute;
import org.sodeja.generator.uml.UmlDataType;
import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlModel;
import org.sodeja.generator.uml.UmlParameter;
import org.sodeja.generator.uml.UmlReference;
import org.xml.sax.Attributes;

public class UmlDataTypeParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlElement parent = context.peek();
		if(parent instanceof UmlModel) {
			UmlDataType element = new UmlDataType();
			fill(element, attributes);
			((UmlModel) parent).getTypes().add(element);
			
			context.push(element);
		} else if(parent instanceof UmlAttribute) {
			UmlReference<UmlDataType> reference = createReference(context, UmlDataType.class, attributes);
			((UmlAttribute) parent).setType(reference);
		} else if(parent instanceof UmlParameter) {
			UmlReference<UmlDataType> reference = createReference(context, UmlDataType.class, attributes);
			((UmlParameter) parent).setType(reference);
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public void end(XmiParser context, String content) {
		if(context.peek() instanceof UmlDataType) {
			super.end(context, content);
		}
	}
}
