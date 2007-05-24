package org.sodeja.generator.uml;

import java.util.ArrayList;
import java.util.List;

public class UmlOperation extends UmlElement implements UmlTaggableElement {
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
}
