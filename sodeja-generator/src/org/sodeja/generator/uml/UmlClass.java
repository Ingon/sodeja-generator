package org.sodeja.generator.uml;

import java.util.ArrayList;
import java.util.List;

public class UmlClass extends UmlType {
	private List<UmlReference<UmlAbstraction>> abstractions;
	private List<UmlAttribute> attributes;
	
	public UmlClass(UmlNamespace parentNamespace) {
		super(parentNamespace);
		
		this.abstractions = new ArrayList<UmlReference<UmlAbstraction>>();
		
		this.attributes = new ArrayList<UmlAttribute>();
	}
	
	public List<UmlAttribute> getAttributes() {
		return attributes;
	}
	
	public List<UmlReference<UmlAbstraction>> getAbstractions() {
		return abstractions;
	}
}
