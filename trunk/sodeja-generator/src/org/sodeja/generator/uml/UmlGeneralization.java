package org.sodeja.generator.uml;

public class UmlGeneralization extends UmlElement {
	private UmlReference<UmlClass> parent;
	private UmlReference<UmlClass> child;
	
	public UmlReference<UmlClass> getChild() {
		return child;
	}
	public void setChild(UmlReference<UmlClass> child) {
		this.child = child;
	}
	
	public UmlReference<UmlClass> getParent() {
		return parent;
	}
	public void setParent(UmlReference<UmlClass> parent) {
		this.parent = parent;
	}
}
