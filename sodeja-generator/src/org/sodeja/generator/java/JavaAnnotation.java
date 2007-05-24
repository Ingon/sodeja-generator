package org.sodeja.generator.java;

public class JavaAnnotation {
	private JavaClass type;
	private String text;
	
	public JavaAnnotation(JavaClass type) {
		this(type, null);
	}

	public JavaAnnotation(JavaClass type, String text) {
		this.type = type;
		this.text = text;
	}

	public JavaClass getType() {
		return type;
	}

	public String getText() {
		return text;
	}
}
