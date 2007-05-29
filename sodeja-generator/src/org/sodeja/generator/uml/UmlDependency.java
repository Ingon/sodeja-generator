package org.sodeja.generator.uml;

import java.util.ArrayList;
import java.util.List;

public class UmlDependency extends UmlElement {
	private UmlDependencyType type;
	
	private List<UmlReference<UmlStereotype>> stereotypes;
	private UmlReference<? extends UmlClass> client;
	private UmlReference<? extends UmlClass> supplier;
	
	public UmlDependency() {
		stereotypes = new ArrayList<UmlReference<UmlStereotype>>();
	}
	
	public UmlDependencyType getType() {
		return type;
	}
	public void setType(UmlDependencyType type) {
		this.type = type;
	}
	
	public List<UmlReference<UmlStereotype>> getStereotypes() {
		return stereotypes;
	}

	public UmlReference<? extends UmlClass> getClient() {
		return client;
	}
	public void setClient(UmlReference<? extends UmlClass> client) {
		this.client = client;
	}
	
	public UmlReference<? extends UmlClass> getSupplier() {
		return supplier;
	}
	public void setSupplier(UmlReference<? extends UmlClass> supplier) {
		this.supplier = supplier;
	}
}
