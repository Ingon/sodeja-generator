package org.sodeja.generator.uml;

import java.util.ArrayList;
import java.util.List;

public class UmlClass extends UmlType {
	private UmlReference<UmlDependency> dependency;
	
	private List<UmlAttribute> attributes;
	
	public UmlClass(UmlNamespace parentNamespace) {
		super(parentNamespace);
		
		this.attributes = new ArrayList<UmlAttribute>();
	}
	
	public List<UmlAttribute> getAttributes() {
		return attributes;
	}
	
	public UmlReference<UmlDependency> getDependency() {
		return dependency;
	}

	public void setDependency(UmlReference<UmlDependency> dependency) {
		this.dependency = dependency;
	}
}
