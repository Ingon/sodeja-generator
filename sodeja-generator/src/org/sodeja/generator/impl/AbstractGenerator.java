package org.sodeja.generator.impl;

import org.sodeja.generator.Generator;
import org.sodeja.generator.GeneratorContext;
import org.sodeja.generator.uml.UmlModel;
import org.sodeja.lang.StringUtils;

public abstract class AbstractGenerator implements Generator {

	private String stereotype;
	
	@Override
	public void generate(GeneratorContext ctx, UmlModel model) {
		if(StringUtils.isTrimmedEmpty(stereotype)) {
			throw new IllegalArgumentException();
		}
	}

	public final String getStereotype() {
		return stereotype;
	}

	public final void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}
}
