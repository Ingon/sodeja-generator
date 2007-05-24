package org.sodeja.generator.uml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UmlPackage extends UmlElement {
	private UmlElement parent;
	
	private List<UmlPackage> subpackages;
	private List<UmlClass> classes;
	
	public UmlPackage(UmlElement parent) {
		this.parent = parent;
		
		this.subpackages = new ArrayList<UmlPackage>();
		this.classes = new ArrayList<UmlClass>();
	}
	
	public List<UmlClass> getClasses() {
		return Collections.unmodifiableList(classes);
	}
	
	public void addClass(UmlClass clazz) {
		classes.add(clazz);
		getRoot().getClasses().add(clazz);
	}
	
	public List<UmlPackage> getSubpackages() {
		return subpackages;
	}
	
	public String getFullName() {
		if(parent instanceof UmlModel) {
			return getName();
		}
		return String.format("%s.%s", ((UmlPackage) parent).getFullName(), getName());
	}

	public UmlPackage getParent() {
		if(parent instanceof UmlModel) {
			throw new IllegalArgumentException();
		}
		return (UmlPackage) parent;
	}
	
	private UmlModel getRoot() {
		if(parent instanceof UmlModel) {
			return (UmlModel) parent;
		}
		return ((UmlPackage) parent).getRoot();
	}
}
