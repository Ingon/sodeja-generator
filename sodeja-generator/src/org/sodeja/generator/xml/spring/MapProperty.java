package org.sodeja.generator.xml.spring;

import java.io.PrintWriter;
import java.util.Map;

class MapProperty implements SpringProperty {
	private Map<?, ?> value;

	public MapProperty(Map<?, ?> value) {
		this.value = value;
	}

	public Map<?, ?> getValue() {
		return value;
	}

	@Override
	public void write(String name, PrintWriter writer) {
		throw new UnsupportedOperationException();
	}
}
