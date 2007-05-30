package org.sodeja.generator.java;

import java.util.ArrayList;
import java.util.List;

public class JavaObjectType implements JavaType {
	private JavaClass base;
	private List<JavaClass> parameters;
	
	public JavaObjectType(JavaClass base) {
		this.base = base;
		this.parameters = new ArrayList<JavaClass>();
	}
	
	public JavaClass getBase() {
		return base;
	}
	
	public List<JavaClass> getParams() {
		return parameters;
	}

	public void addParameter(JavaClass simpleClass) {
		parameters.add(simpleClass);
	}
}
