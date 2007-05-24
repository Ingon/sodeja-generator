package org.sodeja.generator.xml.spring;

import java.io.PrintWriter;

class ValueProperty implements SpringProperty {
	private String value;

	public ValueProperty(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public void write(String name, PrintWriter writer) {
		writer.format("        <property name=\"%s\" value=\"%s\"/>\r\n", name, value);
	}
}
