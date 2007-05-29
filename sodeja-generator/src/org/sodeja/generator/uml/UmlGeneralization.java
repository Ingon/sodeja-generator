package org.sodeja.generator.uml;

public class UmlGeneralization extends UmlElement {
	private UmlReference<? extends UmlType> parent;
	private UmlReference<? extends UmlType> child;
	
	public UmlReference<? extends UmlType> getChild() {
		return child;
	}
	public void setChild(UmlReference<? extends UmlType> child) {
		this.child = child;
	}
	
	public UmlReference<? extends UmlType> getParent() {
		return parent;
	}
	public void setParent(UmlReference<? extends UmlType> parent) {
		this.parent = parent;
	}
}
