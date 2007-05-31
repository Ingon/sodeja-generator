package org.sodeja.generator.java;


public class JavaWildcardType implements JavaType {
	private JavaType lowerBounds;
	private JavaType upperBounds;
	
	public JavaWildcardType() {
	}

	public JavaType getLowerBounds() {
		return lowerBounds;
	}

	public void setLowerBounds(JavaType type) {
		if(upperBounds != null) {
			throw new IllegalArgumentException("Not possible to have simultainiously lower/upper bounds");
		}
		lowerBounds = type;
	}
	
	public JavaType getUpperBounds() {
		return upperBounds;
	}

	public void setUpperBounds(JavaType type) {
		if(lowerBounds != null) {
			throw new IllegalArgumentException("Not possible to have simultainiously lower/upper bounds");
		}
		upperBounds = type;
	}
}
