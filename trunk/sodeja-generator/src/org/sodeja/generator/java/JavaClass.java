package org.sodeja.generator.java;

import java.util.ArrayList;
import java.util.List;

public class JavaClass implements JavaType, JavaAnnotatedElement, JavaAccessModifiable {
	private JavaPackage _package;
	
	private List<JavaClass> imports;

	private List<JavaAnnotation> annotations;

	private JavaAccessModifier accessModifier = JavaAccessModifier.PUBLIC;
	private String name;
	
	private JavaClass parent;
	private List<JavaInterface> interfaces;
	
	private List<JavaMember> members;
//	private List<JavaField> fields;
//	private List<JavaMethod> methods;
	
	public JavaClass(JavaPackage _package, String name) {
		this._package = _package;
		this.name = name;
		
		imports = new ArrayList<JavaClass>();
		annotations = new ArrayList<JavaAnnotation>();
		interfaces = new ArrayList<JavaInterface>();
		members = new ArrayList<JavaMember>();
//		fields = new ArrayList<JavaField>();
//		methods = new ArrayList<JavaMethod>();
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

	public JavaClass getParent() {
		return parent;
	}

	public void setParent(JavaClass parent) {
		autoImport(parent);
		this.parent = parent;
	}
	
	public List<JavaInterface> getInterfaces() {
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
		List<JavaField> result = new ArrayList<JavaField>();
		for(JavaMember member : members) {
			if(member instanceof JavaField) {
				result.add((JavaField) member);
			}
		}
		
		return result;
//		return Collections.unmodifiableList(fields);
	}

	public void addField(JavaField field) {
		autoImport(field);
		members.add(field);
	}
	
	public List<JavaMethod> getMethods() {
		List<JavaMethod> result = new ArrayList<JavaMethod>();
		for(JavaMember member : members) {
			if(member instanceof JavaMethod) {
				result.add((JavaMethod) member);
			}
		}
		
		return result;
//		return Collections.unmodifiableList(methods);
	}
	
	public void addMethod(JavaMethod method) {
		autoImport(method);
		members.add(method);
	}

	public List<JavaConstructor> getConstructors() {
		List<JavaConstructor> result = new ArrayList<JavaConstructor>();
		for(JavaMember member : members) {
			if(member instanceof JavaConstructor) {
				result.add((JavaConstructor) member);
			}
		}
		
		return result;
	}
	
	public void addConstructor(JavaConstructor constructor) {
		autoImport(constructor);
		members.add(constructor);
	}
	
	public void addInterface(JavaInterface inter) {
		autoImport(inter);
		interfaces.add(inter);
	}
	
	public boolean isSystem() {
		return _package.getFullName().equals("java.lang");
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

	private void autoImport(JavaMethod method) {
		autoImportMember(method);
		if(method.getReturnType() instanceof JavaClass) {
			autoImport((JavaClass) method.getReturnType());
		}
		for(JavaMethodParameter param : method.getParameters()) {
			if(param.getType() instanceof JavaClass) {
				autoImport((JavaClass) param.getType());
			}
		}
	}

	private void autoImport(JavaField field) {
		autoImportMember(field);
		if(field.getType() instanceof JavaClass) {
			autoImport((JavaClass) field.getType());
		}
	}
	
	private void autoImport(JavaConstructor constructor) {
		autoImportMember(constructor);
		for(JavaMethodParameter param : constructor.getParameters()) {
			if(param.getType() instanceof JavaClass) {
				autoImport((JavaClass) param.getType());
			}
		}
	}
	
	private void autoImportMember(JavaMember member) {
		for(JavaAnnotation annotation : member.getAnnotations()) {
			autoImport(annotation.getType());
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
