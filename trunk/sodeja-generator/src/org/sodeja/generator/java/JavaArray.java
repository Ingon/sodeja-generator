package org.sodeja.generator.java;

public class JavaArray implements JavaType {
	private JavaType type;
	
	public JavaArray(JavaType type) {
		this.type = type;
	}

	public JavaType getType() {
		return type;
	}
}
