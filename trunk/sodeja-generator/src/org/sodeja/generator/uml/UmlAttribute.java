package org.sodeja.generator.uml;

public class UmlAttribute extends UmlElement {
	private UmlVisibility visibility;
	private UmlOwnerScope scope;
	
	private UmlMultiplicityRange range;
	private UmlReference<? extends UmlType> type;
	
	public UmlVisibility getVisibility() {
		return visibility;
	}
	public void setVisibility(UmlVisibility visibility) {
		this.visibility = visibility;
	}
	
	public UmlOwnerScope getScope() {
		return scope;
	}
	public void setScope(UmlOwnerScope scope) {
		this.scope = scope;
	}
	
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
