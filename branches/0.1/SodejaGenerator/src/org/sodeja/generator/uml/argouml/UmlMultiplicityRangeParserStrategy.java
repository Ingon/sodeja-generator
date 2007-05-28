package org.sodeja.generator.uml.argouml;

import org.sodeja.generator.uml.UmlAssociationEnd;
import org.sodeja.generator.uml.UmlAttribute;
import org.sodeja.generator.uml.UmlElement;
import org.sodeja.generator.uml.UmlMultiplicityRange;
import org.xml.sax.Attributes;

public class UmlMultiplicityRangeParserStrategy extends XmiParserStrategy {
	@Override
	public void begin(XmiParser context, Attributes attributes) {
		UmlMultiplicityRange range = null;
		String lower = attributes.getValue("lower");
		String upper = attributes.getValue("upper");
		if(lower.equals("0") && upper.equals("1")) {
			range = UmlMultiplicityRange.ZERO_ONE;
		} else if(lower.equals("0") && upper.equals("-1")) {
			range = UmlMultiplicityRange.ZERO_MANY;
		} else if(lower.equals("1") && upper.equals("1")) {
			range = UmlMultiplicityRange.ONE_ONE;
		} else if(lower.equals("1") && upper.equals("-1")) {
			range = UmlMultiplicityRange.ONE_MANY;
		} else {
			throw new RuntimeException();
		}
		
		UmlElement parent = context.peek();
		if(parent instanceof UmlAttribute) {
			((UmlAttribute) parent).setRange(range);
		} else if(parent instanceof UmlAssociationEnd) {
			((UmlAssociationEnd) parent).setRange(range);
		}
	}

	@Override
	public void end(XmiParser context, String content) {
	}
}
