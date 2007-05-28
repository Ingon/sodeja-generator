package org.sodeja.generator.uml;

import java.util.ArrayList;
import java.util.List;

public class UmlParameter extends UmlElement implements UmlTaggableElement {
	private UmlReference<? extends UmlType> type;
	private List<UmlTagValue> tags;

	public UmlParameter() {
		tags = new ArrayList<UmlTagValue>();
	}
	
	public UmlReference<? extends UmlType> getType() {
		return type;
	}
	public void setType(UmlReference<? extends UmlType> type) {
		this.type = type;
	}

	public List<UmlTagValue> getTags() {
		return tags;
	}
}
