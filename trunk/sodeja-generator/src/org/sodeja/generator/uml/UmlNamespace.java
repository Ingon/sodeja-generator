package org.sodeja.generator.uml;

import java.util.ArrayList;
import java.util.List;

import org.sodeja.collections.CollectionUtils;

public abstract class UmlNamespace extends UmlElement {
	protected UmlNamespace parent;
	protected List<UmlPackage> children;
	
	protected List<UmlClass> classes;
	protected List<UmlInterface> interfaces;
	protected List<UmlEnumeration> enumerations;
	protected List<UmlDataType> types;

	protected List<UmlTagDefinition> tags;
	protected List<UmlStereotype> stereotypes;
	protected List<UmlAssociation> associations;
	protected List<UmlGeneralization> generatlizations;
	protected List<UmlDependency> dependencies;
	protected List<UmlAbstraction> abstractions;
	
	public UmlNamespace(UmlNamespace parent) {
		this.parent = parent;
		this.children = new ArrayList<UmlPackage>();
		
		this.classes = new ArrayList<UmlClass>();
		this.interfaces = new ArrayList<UmlInterface>();
		this.enumerations = new ArrayList<UmlEnumeration>();
		this.types = new ArrayList<UmlDataType>();
		
		this.tags = new ArrayList<UmlTagDefinition>();
		this.stereotypes = new ArrayList<UmlStereotype>();
		this.associations = new ArrayList<UmlAssociation>();
		this.generatlizations = new ArrayList<UmlGeneralization>();
		this.dependencies = new ArrayList<UmlDependency>();
		this.abstractions = new ArrayList<UmlAbstraction>();
	}

	public UmlNamespace getParent() {
		return parent;
	}

	public List<UmlPackage> getChildren() {
		return children;
	}

	public List<UmlClass> getClasses() {
		return classes;
	}
	
	public List<UmlInterface> getInterfaces() {
		return interfaces;
	}

	public List<UmlEnumeration> getEnumerations() {
		return enumerations;
	}

	public List<UmlStereotype> getStereotypes() {
		return stereotypes;
	}
	
	public List<UmlTagDefinition> getTags() {
		return tags;
	}
	
	public List<UmlDataType> getTypes() {
		return types;
	}
	
	public List<UmlAssociation> getAssociations() {
		return associations;
	}

	public List<UmlGeneralization> getGeneratlizations() {
		return generatlizations;
	}

	public List<UmlDependency> getDependencies() {
		return dependencies;
	}
	
	public List<UmlAbstraction> getAbstractions() {
		return abstractions;
	}

	public abstract String getFullName();
	
	public List<UmlClass> findClassesByStereotype(String name) {
		List<UmlClass> result = new ArrayList<UmlClass>();
		
		for(UmlClass clazz : this.classes) {
			if(CollectionUtils.isEmpty(clazz.getStereotypes())) {
				continue;
			}
			
			if(clazz.hasStereotype(name)) {
				result.add(clazz);
			}
		}
		
		for(UmlNamespace child : children) {
			result.addAll(child.findClassesByStereotype(name));
		}
		
		return result;
	}
	
	public List<UmlEnumeration> findEnumerationsByStereotype(String name) {
		List<UmlEnumeration> result = new ArrayList<UmlEnumeration>();
		
		for(UmlEnumeration clazz : this.enumerations) {
			if(CollectionUtils.isEmpty(clazz.getStereotypes())) {
				continue;
			}
			
			if(clazz.hasStereotype(name)) {
				result.add(clazz);
			}
		}
		
		for(UmlNamespace child : children) {
			result.addAll(child.findEnumerationsByStereotype(name));
		}
		
		return result;
	}
	
	public List<UmlAssociation> findAssociations(UmlClass clazz) {
		List<UmlAssociation> result = new ArrayList<UmlAssociation>();
		for(UmlAssociation association : associations) {
			if(association.isParticipant(clazz)) {
				result.add(association);
			}
		}
		
		for(UmlNamespace child : children) {
			result.addAll(child.findAssociations(clazz));
		}
		
		return result;
	}
	
	public List<UmlGeneralization> findPerentGeneralizations(UmlClass clazz) {
		List<UmlGeneralization> result = new ArrayList<UmlGeneralization>();
		for (UmlGeneralization generalization : generatlizations) {
			if (clazz.isReferent(generalization.getParent())) {
				result.add(generalization);
			}
		}
		
		for(UmlNamespace child : children) {
			result.addAll(child.findPerentGeneralizations(clazz));
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	protected <R extends UmlElement> R findById(Class<R> clazz, String id) {
		UmlElement result = null;
		if(clazz == UmlStereotype.class) {
			result = findById(getStereotypes(), id);
		} else if(clazz == UmlGeneralization.class) {
			result = findById(getGeneratlizations(), id);
		} else if(clazz == UmlTagDefinition.class) {
			result = findById(getTags(), id);
		} else if(clazz == UmlDataType.class) {
			result = findById(getTypes(), id);
		} else if(clazz == UmlClass.class) {
			result = findById(getClasses(), id);
		} else if(clazz == UmlInterface.class) {
			result = findById(getInterfaces(), id);
		} else if(clazz == UmlEnumeration.class) {
			result = findById(getEnumerations(), id);
		} else if(clazz == UmlDependency.class) {
			result = findById(getDependencies(), id);
		} else {
			throw new UnsupportedOperationException();
		}
		
		if(result == null) {
			for(UmlNamespace child : children) {
				result = child.findById(clazz, id);
				if(result != null) {
					break;
				}
			}
		}
		
		return (R) result;
	}
	
	protected <R extends UmlElement> R findById(List<R> elements, String id) {
		for(R element : elements) {
			if(element.getId().equals(id)) {
				return element;
			}
		}
		
		return null;
	}
}
