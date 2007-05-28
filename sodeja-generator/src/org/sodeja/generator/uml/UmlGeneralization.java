package org.sodeja.generator.uml;

public class UmlGeneralization extends UmlElement {
	private UmlReference<? extends UmlClass> parent;
	private UmlReference<? extends UmlClass> child;
	
	public UmlReference<? extends UmlClass> getChild() {
		return child;
	}
	public void setChild(UmlReference<? extends UmlClass> child) {
		this.child = child;
	}
	
	public UmlReference<? extends UmlClass> getParent() {
		return parent;
	}
	public void setParent(UmlReference<? extends UmlClass> parent) {
		this.parent = parent;
	}
}
