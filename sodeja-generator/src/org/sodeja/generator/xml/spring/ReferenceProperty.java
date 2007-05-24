package org.sodeja.generator.xml.spring;

import java.io.PrintWriter;

class ReferenceProperty implements SpringProperty {
	private SpringBean reference;

	public ReferenceProperty(SpringBean reference) {
		this.reference = reference;
	}

	public SpringBean getReference() {
		return reference;
	}

	@Override
	public void write(String name, PrintWriter writer) {
		writer.format("        <property name=\"%s\" ref=\"%s\"/>\r\n", name, reference.getName());
	}
}
