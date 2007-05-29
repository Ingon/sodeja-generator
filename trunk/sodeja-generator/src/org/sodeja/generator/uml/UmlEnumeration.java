package org.sodeja.generator.uml;

import java.util.ArrayList;
import java.util.List;

public class UmlEnumeration extends UmlType {
	private List<UmlEnumerationLiteral> literals;
	
	public UmlEnumeration(UmlNamespace parentNamespace) {
		super(parentNamespace);
		
		literals = new ArrayList<UmlEnumerationLiteral>();
	}

	public List<UmlEnumerationLiteral> getLiterals() {
		return literals;
	}
}
