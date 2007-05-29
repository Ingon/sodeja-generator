package org.sodeja.generator.uml;

import java.util.ArrayList;
import java.util.List;

public class UmlAbstraction extends UmlElement {
	private List<UmlReference<UmlStereotype>> stereotypes;
	private UmlReference<UmlInterface> supplier;
	private UmlReference<UmlClass> client;
	
	public UmlAbstraction() {
		stereotypes = new ArrayList<UmlReference<UmlStereotype>>();
	}
	
	public UmlReference<UmlInterface> getSupplier() {
		return supplier;
	}
	public void setSupplier(UmlReference<UmlInterface> supplier) {
		this.supplier = supplier;
	}
	
	public UmlReference<UmlClass> getClient() {
		return client;
	}
	public void setClient(UmlReference<UmlClass> client) {
		this.client = client;
	}
	
	public List<UmlReference<UmlStereotype>> getStereotypes() {
		return stereotypes;
	}
}
