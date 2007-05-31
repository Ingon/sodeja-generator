package org.sodeja.generator.uml;


public class UmlReference<T extends UmlElement> {
	private String id;
	
	private Class<T> clazz;
	private UmlElement cached;
	private UmlModel model;
	
	public UmlReference(String id, Class<T> clazz, UmlModel model) {
		this.id = id;
		this.clazz = clazz;
		this.model = model;
	}
	
	public String getId() {
		return id;
	}

	@SuppressWarnings("unchecked")
	public T getReferent() {
		cached = model.findById(clazz, id);
		if(cached == null) {
			throw new RuntimeException("Unable to find element with id " + id);
		}
		return (T) cached;
	}
}
