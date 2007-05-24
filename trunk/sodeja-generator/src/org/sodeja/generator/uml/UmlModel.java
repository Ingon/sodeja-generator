package org.sodeja.generator.uml;

import java.util.ArrayList;
import java.util.List;

public class UmlModel extends UmlElement {
	private List<UmlPackage> packages;
	private List<UmlClass> classes;
	private List<UmlDataType> types;
	private List<UmlTagDefinition> tags;
	private List<UmlStereotype> stereotypes;
	private List<UmlAssociation> associations;
	private List<UmlGeneralization> generatlizations;
	
	public UmlModel() {
		this.types = new ArrayList<UmlDataType>();
		this.packages = new ArrayList<UmlPackage>();
		this.classes = new ArrayList<UmlClass>();
		this.tags = new ArrayList<UmlTagDefinition>();
		this.stereotypes = new ArrayList<UmlStereotype>();
		this.associations = new ArrayList<UmlAssociation>();
		this.generatlizations = new ArrayList<UmlGeneralization>();
	}
	
	public List<UmlPackage> getPackages() {
		return packages;
	}
	
	protected List<UmlClass> getClasses() {
		return classes;
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

	public List<UmlClass> findClassesByStereotype(String name) {
		List<UmlClass> result = new ArrayList<UmlClass>();
		
		for(UmlClass clazz : this.classes) {
			if(clazz.getStereotype() == null) {
				continue;
			}
			
			if(clazz.getStereotype().getReferent().getName().equals(name)) {
				result.add(clazz);
			}
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
		return result;
	}
	
	public List<UmlGeneralization> findPerentGeneralizations(UmlClass clazz) {
		List<UmlGeneralization> result = new ArrayList<UmlGeneralization>();
		for(UmlGeneralization generalization : generatlizations) {
			if(clazz.isReferent(generalization.getParent())) {
				result.add(generalization);
			}
		}
		return result;
	}
}
