package org.sodeja.generator.java;

import java.util.ArrayList;
import java.util.List;

public class JavaClass implements JavaAnnotatedElement, JavaAccessModifiable {
	private JavaPackage _package;
	private String name;
	
	private List<JavaClass> imports;

	private List<JavaAnnotation> annotations;

	private JavaAccessModifier accessModifier = JavaAccessModifier.PUBLIC;
	private JavaObjectType parent;
	private List<JavaObjectType> interfaces;
	
	private List<JavaField> fields;
	private List<JavaMethod> methods;
	
	public JavaClass(JavaPackage _package, String name) {
		this._package = _package;
		this.name = name;
		
		imports = new ArrayList<JavaClass>();
		annotations = new ArrayList<JavaAnnotation>();
		interfaces = new ArrayList<JavaObjectType>();
		fields = new ArrayList<JavaField>();
		methods = new ArrayList<JavaMethod>();
	}
	
	public JavaPackage getPackage() {
		return _package;
	}

	public List<JavaClass> getImports() {
		return imports;
	}

	public void addImport(JavaClass javaClass) {
		autoImport(javaClass);
	}

	public JavaAccessModifier getAccessModifier() {
		return accessModifier;
	}

	public String getName() {
		return name;
	}

	public JavaObjectType getParent() {
		return parent;
	}

	public void setParent(JavaObjectType parent) {
		autoImport(parent);
		this.parent = parent;
	}
	
	public List<JavaObjectType> getInterfaces() {
		return interfaces;
	}

	public List<JavaAnnotation> getAnnotations() {
		return annotations;
	}

	public void addAnnotation(JavaAnnotation annotation) {
		autoImport(annotation.getType());
		annotations.add(annotation);
	}

	public void addAnnotation(JavaClass clazz) {
		addAnnotation(new JavaAnnotation(clazz));
	}
	
	public void addAnnotation(JavaClass clazz, String value) {
		addAnnotation(new JavaAnnotation(clazz, value));
	}
	
	public List<JavaField> getFields() {
		return fields;
	}

	public void addField(JavaField field) {
		autoImport(field);
		fields.add(field);
	}
	
	public List<JavaMethod> getMethods() {
		return methods;
	}
	
	public void addMethod(JavaMethod method) {
		autoImport(method);
		for(JavaMethodParameter param : method.getParameters()) {
			if(param.getType() instanceof JavaObjectType) {
				autoImport((JavaObjectType) param.getType());
			}
		}
		methods.add(method);
	}

	public boolean isSystem() {
		return _package.getFullName().equals("java.lang");
	}
	
	public void addInterface(JavaObjectType inter) {
		if(! (inter.getBase() instanceof JavaInterface)) {
			throw new IllegalArgumentException();
		}
		autoImport(inter);
		interfaces.add(inter);
	}
	
	public String getFullName() {
		return String.format("%s.%s", getPackage().getFullName(), getName());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(! (obj instanceof JavaClass)) {
			return false;
		}
		
		JavaClass other = (JavaClass) obj;
		return name.equals(other.name) && _package.equals(other._package);
	}

	private void autoImport(JavaField field) {
		for(JavaAnnotation annotation : field.getAnnotations()) {
			autoImport(annotation.getType());
		}
		if(field.getType() instanceof JavaObjectType) {
			autoImport((JavaObjectType) field.getType());
		}
	}
	
	private void autoImport(JavaObjectType type) {
		autoImport(type.getBase());
		for(JavaClass param : type.getParams()) {
			autoImport(param);
		}
	}
	
	private void autoImport(JavaClass clazz) {
		if(clazz._package == null) {
			return;
		}
		
		if(this._package.getFullName().equals(clazz._package.getFullName())) {
			return;
		}
		
		for(JavaClass javaImport : imports) {
			if(javaImport.name == clazz.name) {
				return;
			}
		}
		imports.add(clazz);
	}
}
