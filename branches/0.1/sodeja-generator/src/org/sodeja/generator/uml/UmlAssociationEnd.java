package org.sodeja.generator.uml;

public class UmlAssociationEnd extends UmlElement {
	private boolean navigateale;
	private UmlAggregationType type;
	private UmlOrdering ordering;
	
	private UmlMultiplicityRange range;
	private UmlReference<? extends UmlClass> referent;
	
	public boolean isNavigateale() {
		return navigateale;
	}
	public void setNavigateale(boolean navigateale) {
		this.navigateale = navigateale;
	}
	
	public UmlMultiplicityRange getRange() {
		return range;
	}
	public void setRange(UmlMultiplicityRange range) {
		this.range = range;
	}
	
	public UmlReference<? extends UmlClass> getReferent() {
		return referent;
	}
	public void setReferent(UmlReference<? extends UmlClass> referent) {
		this.referent = referent;
	}
	
	public UmlAggregationType getType() {
		return type;
	}
	public void setType(UmlAggregationType type) {
		this.type = type;
	}
	
	public UmlOrdering getOrdering() {
		return ordering;
	}
	public void setOrdering(UmlOrdering ordering) {
		this.ordering = ordering;
	}
	
	public boolean isRefersTo(UmlClass clazz) {
		return clazz.isReferent(referent);
	}
}
