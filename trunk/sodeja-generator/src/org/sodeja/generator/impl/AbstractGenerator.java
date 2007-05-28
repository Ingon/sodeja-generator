package org.sodeja.generator.impl;

import org.sodeja.generator.Generator;

public abstract class AbstractGenerator implements Generator {
	private String stereotype;
	
	public AbstractGenerator() {
		stereotype = "DomainObject";
	}
	
	public String getStereotype() {
		return stereotype;
	}

	public void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}
}
