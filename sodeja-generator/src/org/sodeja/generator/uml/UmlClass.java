package org.sodeja.generator.uml;

import java.util.ArrayList;
import java.util.List;

public class UmlClass extends UmlType implements UmlTaggableElement {
	private UmlPackage parentPackage;
	
	private UmlReference<UmlStereotype> stereotype;
	private UmlReference<UmlGeneralization> parent;
	
	private List<UmlTagValue> tags;
	
	private List<UmlAttribute> attributes;
	private List<UmlOperation> operations;
	
	public UmlClass(UmlPackage parentPackage) {
		this.parentPackage = parentPackage;
		
		this.tags = new ArrayList<UmlTagValue>();
		this.attributes = new ArrayList<UmlAttribute>();
		this.operations = new ArrayList<UmlOperation>();
	}
	
	public List<UmlAttribute> getAttributes() {
		return attributes;
	}
	public UmlReference<UmlGeneralization> getParent() {
		return parent;
	}
	public void setParent(UmlReference<UmlGeneralization> parent) {
		this.parent = parent;
	}
	
	public UmlReference<UmlStereotype> getStereotype() {
		return stereotype;
	}
	public void setStereotype(UmlReference<UmlStereotype> stereotype) {
		this.stereotype = stereotype;
	}

	public List<UmlTagValue> getTags() {
		return tags;
	}

	public UmlPackage getParentPackage() {
		return parentPackage;
	}

	public List<UmlOperation> getOperations() {
		return operations;
	}
	
	public String getFullName() {
		return String.format("%s.%s", parentPackage.getFullName(), getName());
	}
}
