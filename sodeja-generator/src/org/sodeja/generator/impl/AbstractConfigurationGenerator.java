package org.sodeja.generator.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.sodeja.generator.GeneratorContext;
import org.sodeja.generator.uml.UmlModel;

public abstract class AbstractConfigurationGenerator extends AbstractGenerator {
	private String configurationFile;

	public File getConfigurationFile() {
		return new File(configurationFile);
	}

	@Override
	public void generate(GeneratorContext ctx, UmlModel model) {
		super.generate(ctx, model);
		if(configurationFile == null) {
			throw new IllegalArgumentException();
		}
	}
	
	protected Writer getFileWriter() throws IOException {
		File parent = getConfigurationFile().getParentFile();
		parent.mkdirs();
		
		return new FileWriter(configurationFile);
	}
}
