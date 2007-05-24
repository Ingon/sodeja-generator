package org.sodeja.generator.java;

import java.util.List;

public class JavaPackage implements Annotateable {
	private String name;
	private JavaPackage parent;
	private List<JavaAnnotation> annotations;
	
	public JavaPackage(String name) {
		this(null, name);
	}
	
	public JavaPackage(JavaPackage parent, String name) {
		this.parent = parent;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFullName() {
		if(parent == null) {
			return name;
		}
		return String.format("%s.%s", parent.getFullName(), name);
	}
	
	public List<JavaAnnotation> getAnnotations() {
		return annotations;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(! (obj instanceof JavaPackage)) {
			return false;
		}
		
		JavaPackage other = (JavaPackage) obj;
		if(parent == other.parent) {
			return name.equals(other.name);
		}
		if(parent != null && other.parent != null) {
			return name.equals(other.name) && parent.equals(other.parent);
		}
		return false;
	}

	public static JavaPackage createFromDots(String pck) {
		String[] strPackages = pck.split("\\.");
		JavaPackage[] packages = new JavaPackage[strPackages.length];
		packages[0] = new JavaPackage(strPackages[0]);
		for(int i = 1, n = strPackages.length;i < n;i++) {
			packages[i] = new JavaPackage(packages[i - 1], strPackages[i]);
		}
		return packages[packages.length - 1];
	}
}
