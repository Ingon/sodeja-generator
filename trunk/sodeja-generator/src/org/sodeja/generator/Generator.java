package org.sodeja.generator;

import org.sodeja.generator.uml.UmlModel;

public interface Generator {
	public void generate(GeneratorContext ctx, UmlModel model);
}
