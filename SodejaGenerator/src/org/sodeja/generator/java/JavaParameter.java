package org.sodeja.generator.java;

public class JavaParameter {
	private JavaType type;
	private String name;
	
	public JavaParameter(JavaType type, String name) {
		this.type = type;
		this.name = name;
	}

	public JavaType getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
}
