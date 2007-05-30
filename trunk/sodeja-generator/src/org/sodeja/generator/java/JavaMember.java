package org.sodeja.generator.java;

import java.util.ArrayList;
import java.util.List;

public class JavaMember implements JavaAnnotatedElement, JavaAccessModifiable {
	private JavaAccessModifier accessModifier = JavaAccessModifier.PRIVATE;
	private List<JavaAnnotation> annotations;
	
	protected JavaMember() {
		this.annotations = new ArrayList<JavaAnnotation>();
	}
	
	@Override
	public List<JavaAnnotation> getAnnotations() {
		return annotations;
	}

	public void addAnnotation(JavaAnnotation annotation) {
		annotations.add(annotation);
	}
	
	public void addAnnotation(JavaClass clazz) {
		addAnnotation(new JavaAnnotation(clazz));
	}
	
	public void addAnnotation(JavaClass clazz, String value) {
		addAnnotation(new JavaAnnotation(clazz, value));
	}

	public JavaAccessModifier getAccessModifier() {
		return accessModifier;
	}

	public void setAccessModifier(JavaAccessModifier accessModifier) {
		this.accessModifier = accessModifier;
	}
}
