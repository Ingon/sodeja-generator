package org.sodeja.generator.impl;

import java.io.PrintWriter;
import java.util.List;

import org.sodeja.generator.GeneratorContext;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlModel;

public abstract class ConfigurationFileDomainGenerator extends AbstractConfigurationGenerator {
	public void generate(GeneratorContext ctx, UmlModel model) {
		List<UmlClass> modelClasses = model.findClassesByStereotype(getStereotype());
		try {
			PrintWriter writer = new PrintWriter(getFileWriter());
			generateBegin(ctx, writer, model);
			
			for(UmlClass modelClass : modelClasses) {
				generateContent(ctx, writer, modelClass);
			}
			
			generateEnd(ctx, writer, model);
			writer.close();
		} catch(Exception exc) {
			throw new RuntimeException(exc);
		}
	}

	protected abstract void generateBegin(GeneratorContext ctx, PrintWriter writer, UmlModel model);

	protected abstract void generateContent(GeneratorContext ctx, PrintWriter writer, UmlClass modelClass);
	
	protected abstract void generateEnd(GeneratorContext ctx, PrintWriter writer, UmlModel model);
}
