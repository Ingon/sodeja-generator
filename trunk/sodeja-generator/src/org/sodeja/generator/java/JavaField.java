package org.sodeja.generator.java;

public class JavaField extends JavaMember {
	// Access flags
	private boolean isStatic;
	
	private JavaType type;
	private String name;
	
	public JavaField(JavaType type, String name) {
		this.type = type;
		this.name = name;
	}

	public JavaType getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
}
