package org.sodeja.generator.java;

public class JavaInterface extends JavaClass {
	public JavaInterface(JavaPackage _package, String name) {
		super(_package, name);
	}

	@Override
	public void setParent(JavaClass parent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addMethod(JavaMethod method) {
		method.setAbstract(true);
		super.addMethod(method);
	}
}
