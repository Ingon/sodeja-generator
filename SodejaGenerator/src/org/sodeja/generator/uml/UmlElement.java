package org.sodeja.generator.uml;

public class UmlElement {
	private String id;
	private String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isReferent(UmlReference<? extends UmlElement> reference) {
		return reference != null && this.id.equals(reference.getId());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(! (obj instanceof UmlElement)) {
			return false;
		}
		
		return id.equals(((UmlElement) obj).getId());
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("%s[id=%s, name=%s]", this.getClass().getSimpleName(), id, name);
	}
}
