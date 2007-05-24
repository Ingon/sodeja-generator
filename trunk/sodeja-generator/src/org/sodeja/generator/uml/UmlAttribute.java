package org.sodeja.generator.uml;

public class UmlAttribute extends UmlElement {
	private UmlMultiplicityRange range;
	private UmlReference<? extends UmlType> type;
	
	public UmlMultiplicityRange getRange() {
		return range;
	}
	public void setRange(UmlMultiplicityRange range) {
		this.range = range;
	}
	
	public UmlReference<? extends UmlType> getType() {
		return type;
	}
	public void setType(UmlReference<? extends UmlType> type) {
		this.type = type;
	}
}
