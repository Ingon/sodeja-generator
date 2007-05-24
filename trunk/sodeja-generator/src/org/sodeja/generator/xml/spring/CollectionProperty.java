package org.sodeja.generator.xml.spring;

import java.io.PrintWriter;
import java.util.Collection;

public class CollectionProperty implements SpringProperty {
	private Collection<?> collection;

	public CollectionProperty(Collection<?> collection) {
		this.collection = collection;
	}

	public Collection<?> getCollection() {
		return collection;
	}

	@Override
	public void write(String name, PrintWriter writer) {
		throw new UnsupportedOperationException();
	}
}
