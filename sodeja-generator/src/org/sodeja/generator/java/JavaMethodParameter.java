package org.sodeja.generator.java;

public class JavaMethodParameter {
	private JavaType type;
	private String name;
	
	public JavaMethodParameter(JavaType type, String name) {
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
