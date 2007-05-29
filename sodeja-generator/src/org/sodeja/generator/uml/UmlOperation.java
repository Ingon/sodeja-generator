package org.sodeja.generator.uml;

import java.util.ArrayList;
import java.util.List;

public class UmlOperation extends UmlElement implements UmlTaggableElement {
	private UmlVisibility visibility;
	private UmlOwnerScope scope;
	
	private UmlParameter result;
	private List<UmlParameter> parameters;
	
	private List<UmlTagValue> tags;
	
	public UmlOperation() {
		this.parameters = new ArrayList<UmlParameter>();
		this.tags = new ArrayList<UmlTagValue>();
	}
	
	public List<UmlParameter> getParameters() {
		return parameters;
	}
	
	public UmlParameter getResult() {
		return result;
	}

	public void setResult(UmlParameter returnType) {
		this.result = returnType;
	}
	
	public List<UmlTagValue> getTags() {
		return tags;
	}

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
}
