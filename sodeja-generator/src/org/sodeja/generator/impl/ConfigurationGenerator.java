package org.sodeja.generator.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.sodeja.generator.Generator;
import org.sodeja.generator.GeneratorContext;

public abstract class ConfigurationGenerator implements Generator {
	protected abstract String getConfigFilename();
	
	protected abstract String getConfigSubfolder();
		
	protected Writer getFileWriter(GeneratorContext ctx) throws IOException {
		File hibernateConfigFolder = new File(ctx.getConfigFolder(), getConfigSubfolder());
		hibernateConfigFolder.mkdirs();
		
		File configFile = new File(hibernateConfigFolder, getConfigFilename());
		Writer writer = new FileWriter(configFile);
		return writer;
	}
}
