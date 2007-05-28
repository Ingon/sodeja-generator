package org.sodeja.generator.uml;

import java.util.List;

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
		if(clazz == UmlStereotype.class) {
			cached = findById(model.getStereotypes(), id);
		} else if(clazz == UmlGeneralization.class) {
			cached = findById(model.getGeneratlizations(), id);
		} else if(clazz == UmlTagDefinition.class) {
			cached = findById(model.getTags(), id);
		} else if(clazz == UmlDataType.class) {
			cached = findById(model.getTypes(), id);
		} else if(clazz == UmlClass.class) {
			cached = findById(model.getClasses(), id);
		} else if(clazz == UmlInterface.class) {
			cached = findById(model.getInterfaces(), id);
		} else {
			throw new UnsupportedOperationException();
		}
		
		return (T) cached;
	}

	private static <R extends UmlElement> R findById(List<R> elements, String id) {
		for(R element : elements) {
			if(element.getId().equals(id)) {
				return element;
			}
		}
		
		throw new RuntimeException("No element with id " + id + " found ");
	}
}
