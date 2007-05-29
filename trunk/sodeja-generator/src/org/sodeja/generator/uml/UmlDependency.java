package org.sodeja.generator.uml;

public class UmlDependency extends UmlElement {
	private UmlReference<? extends UmlClass> client;
	private UmlReference<? extends UmlClass> supplier;
	
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
