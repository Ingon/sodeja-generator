package org.sodeja.generator.impl;

import java.util.List;

import org.sodeja.generator.GeneratorContext;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlModel;
import org.sodeja.lang.StringUtils;

public abstract class AbstractPerClassGenerator extends AbstractGenerator {
	@Override
	public void generate(GeneratorContext ctx, UmlModel model) {
		String stereotype = getStereotype();
		if(StringUtils.isTrimmedEmpty(stereotype)) {
			throw new IllegalArgumentException("Stereotype cannot be empty");
		}
		
		List<UmlClass> modelClasses = model.findClassesByStereotype(stereotype);
		for(UmlClass modelClass : modelClasses) {
			generate(ctx, model, modelClass);
		}
	}

	protected abstract void generate(GeneratorContext ctx, UmlModel model, UmlClass modelClass);
}
