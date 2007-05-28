package org.sodeja.generator.java;

import java.util.ArrayList;
import java.util.List;

public class JavaEnum extends JavaClass {
	
	private List<String> values;
	
	public JavaEnum(JavaPackage _package, String name) {
		super(_package, name);
		values = new ArrayList<String>();
	}

	public List<String> getValues() {
		return values;
	}
	
	public void addValue(String value) {
		values.add(value);
	}
}
