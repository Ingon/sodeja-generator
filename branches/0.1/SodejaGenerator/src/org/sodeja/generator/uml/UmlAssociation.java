package org.sodeja.generator.uml;

public class UmlAssociation extends UmlElement {
	private UmlAssociationEnd first;
	private UmlAssociationEnd second;
	
	public UmlAssociationEnd getFirst() {
		return first;
	}
	public void setFirst(UmlAssociationEnd first) {
		this.first = first;
	}
	
	public UmlAssociationEnd getSecond() {
		return second;
	}
	public void setSecond(UmlAssociationEnd second) {
		this.second = second;
	}
	
	public boolean isParticipant(UmlClass clazz) {
		return first.isRefersTo(clazz) || second.isRefersTo(clazz);
	}
	
	public UmlAssociationEnd getThisEnd(UmlClass modelClass) {
		if(first.isRefersTo(modelClass)) {
			return first;
		} else if(second.isRefersTo(modelClass)) {
			return second;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public UmlAssociationEnd getOtherEnd(UmlClass modelClass) {
		if(first.isRefersTo(modelClass)) {
			return second;
		} else if(second.isRefersTo(modelClass)) {
			return first;
		} else {
			throw new IllegalArgumentException();
		}
	}
}
