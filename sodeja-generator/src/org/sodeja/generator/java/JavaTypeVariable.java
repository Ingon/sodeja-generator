package org.sodeja.generator.java;

import java.util.ArrayList;
import java.util.List;

public class JavaTypeVariable {
	private String name;
	private JavaClass bound;
	private List<JavaInterface> additionalBounds;
	
	public JavaTypeVariable(String name) {
		this.name = name;
		
		additionalBounds = new ArrayList<JavaInterface>();
	}
	
	public JavaTypeVariable(String name, JavaClass bound) {
		this(name);
		this.bound = bound;
	}

	public String getName() {
		return name;
	}

	public JavaClass getBound() {
		return bound;
	}

	public List<JavaInterface> getAdditionalBounds() {
		return additionalBounds;
	}
}
