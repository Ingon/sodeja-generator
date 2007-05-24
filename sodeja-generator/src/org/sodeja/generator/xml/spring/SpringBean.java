package org.sodeja.generator.xml.spring;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SpringBean {
	private String name;
	private String clazz;
	
	private boolean _abstract;
	private SpringBean parent;
	
	private Map<String, SpringProperty> properties;
	
	public SpringBean(String name, String clazz) {
		this.name = name;
		this.clazz = clazz;
		
		this.properties = new HashMap<String, SpringProperty>();
	}
	
	public boolean isAbstract() {
		return _abstract;
	}
	
	public void setAbstract(boolean _abstract) {
		this._abstract = _abstract;
	}

	public String getName() {
		return name;
	}

	public String getClazz() {
		return clazz;
	}

	public SpringBean getParent() {
		return parent;
	}

	public void setParent(SpringBean parent) {
		this.parent = parent;
	}
	
	public void addReference(String name, SpringBean value) {
		properties.put(name, new ReferenceProperty(value));
	}
	
	public void addValue(String name, Object value) {
		properties.put(name, new ValueProperty(String.valueOf(value)));
	}
	
	public void addCollection(String name, Collection<?> value) {
		properties.put(name, new CollectionProperty(value));
	}
	
	public void addMap(String name, Map<?, ?> value) {
		properties.put(name, new MapProperty(value));
	}

	protected void write(PrintWriter writer) {
		writer.format("    <bean name=\"%s\" class=\"%s\"%s%s>\r\n", name, clazz, genAbstract(), genParent());
		for(Map.Entry<String, SpringProperty> entry : properties.entrySet()) {
			entry.getValue().write(entry.getKey(), writer);
		}
		writer.append("    </bean>\r\n\r\n");
	}

	private String genAbstract() {
		if(! _abstract) {
			return "";
		}
		return " abstract=\"true\"";
	}

	private String genParent() {
		if(parent == null) {
			return "";
		}
		return String.format(" parent=\"%s\"", parent.getName());
	}
}
