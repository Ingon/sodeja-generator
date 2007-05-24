package org.sodeja.generator.impl;

import java.util.List;

import org.sodeja.generator.Generator;
import org.sodeja.generator.GeneratorContext;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlModel;

public abstract class SimpleDomainGenerator implements Generator {
	
	protected static final String DOMAIN_STEREOTYPE = "DomainObject";
	
	public void generate(GeneratorContext ctx, UmlModel model) {
		List<UmlClass> modelClasses = model.findClassesByStereotype(DOMAIN_STEREOTYPE);
		for(UmlClass modelClass : modelClasses) {
			generate(ctx, model, modelClass);
		}
	}

	protected abstract void generate(GeneratorContext ctx, UmlModel model, UmlClass modelClass);
}
