package org.sodeja.generator;

import java.io.File;

public class GeneratorContext {
	private File baseFolder;
	private File configFolder;
	private File sourceFolder;
	private File modelFile;

	protected void setBaseFolder(String path) {
		this.baseFolder = new File(path);
	}
	
	protected void setConfigFolder(String path) {
		this.configFolder = new File(baseFolder, path);
	}
	
	protected void setSourceFolder(String path) {
		this.sourceFolder = new File(baseFolder, path);
	}

	protected void setModelFile(String path) {
		modelFile = new File(baseFolder, path);
	}
	
	public File getBaseFolder() {
		return baseFolder;
	}
	
	public File getSourceFolder() {
		return sourceFolder;
	}
	
	public File getConfigFolder() {
		return configFolder;
	}
	
	public File getModelFile() {
		return modelFile;
	}
}
