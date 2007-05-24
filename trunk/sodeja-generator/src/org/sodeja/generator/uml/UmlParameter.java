package org.sodeja.generator.uml;

public class UmlParameter extends UmlElement {
	private UmlReference<? extends UmlType> type;

	public UmlReference<? extends UmlType> getType() {
		return type;
	}
	public void setType(UmlReference<? extends UmlType> type) {
		this.type = type;
	}
}
