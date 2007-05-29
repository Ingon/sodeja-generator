package org.sodeja.generator.uml;

public class UmlPackage extends UmlNamespace {
	public UmlPackage(UmlNamespace parent) {
		super(parent);
	}
	
	public String getFullName() {
		if(parent instanceof UmlModel) {
			return getName();
		}
		return String.format("%s.%s", ((UmlPackage) parent).getFullName(), getName());
	}
}
