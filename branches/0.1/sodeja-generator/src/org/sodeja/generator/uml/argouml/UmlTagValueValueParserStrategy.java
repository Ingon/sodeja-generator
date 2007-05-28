package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlTagValue;
import org.xml.sax.Attributes;

public class UmlTagValueValueParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		if(! (context.peek() instanceof UmlTagValue)) {
			throw new RuntimeException();
		}
	}

	@Override
	public void end(XmiParser context, String content) {
		((UmlTagValue) context.peek()).setValue(content);
	}
}
