package org.sodeja.generator.java;

public class JavaTypeVariableReference implements JavaType {
	private String name;
	
	public JavaTypeVariableReference(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
