package org.sodeja.generator.uml;

import java.util.ArrayList;
import java.util.List;

public class UmlType extends UmlElement implements UmlTaggableElement {
	private UmlNamespace parentNamespace;
	
	private UmlVisibility visibility;
	
	private List<UmlReference<UmlDependency>> dependencies;
	private List<UmlReference<UmlGeneralization>> generalizations;
	private List<UmlOperation> operations;
	
	private List<UmlReference<UmlStereotype>> stereotypes;
	private List<UmlTagValue> tags;

	public UmlType(UmlNamespace parentNamespace) {
		this.parentNamespace = parentNamespace;
		
		this.dependencies = new ArrayList<UmlReference<UmlDependency>>();
		this.generalizations = new ArrayList<UmlReference<UmlGeneralization>>();
		this.operations = new ArrayList<UmlOperation>();
		
		this.stereotypes = new ArrayList<UmlReference<UmlStereotype>>();
		this.tags = new ArrayList<UmlTagValue>();
	}
	
	public UmlNamespace getParentNamespace() {
		return parentNamespace;
	}
	
	public UmlVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(UmlVisibility visibility) {
		this.visibility = visibility;
	}

	public List<UmlReference<UmlDependency>> getDependencies() {
		return dependencies;
	}

	public List<UmlReference<UmlGeneralization>> getGeneralizations() {
		return generalizations;
	}

	public List<UmlOperation> getOperations() {
		return operations;
	}

	public List<UmlReference<UmlStereotype>> getStereotypes() {
		return stereotypes;
	}

	public List<UmlTagValue> getTags() {
		return tags;
	}
	
	public String getFullName() {
		return String.format("%s.%s", parentNamespace.getFullName(), getName());
	}
	
	public boolean hasStereotype(String name) {
		for(UmlReference<UmlStereotype> stereotypeRef : stereotypes) {
			if(stereotypeRef.getReferent().getName().equals(name)) {
				return true;
			}
		}
		
		return false;
	}
}
