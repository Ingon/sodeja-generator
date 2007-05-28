package org.sodeja.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.sodeja.generator.impl.HibernateClassGenerator;
import org.sodeja.generator.impl.HibernateConfigGenerator;
import org.sodeja.generator.impl.HibernateDaoGenerator;
import org.sodeja.generator.impl.SpringDaoConfigurationGenerator;
import org.sodeja.lang.StringUtils;

public class GeneratorContext {
	private File baseFolder;
	private File configFolder;
	private File sourceFolder;
	private File modelFile;
	private List<Generator> generators;
	
	protected GeneratorContext() {
		generators = new ArrayList<Generator>();
		generators.add(new HibernateClassGenerator());
		generators.add(new HibernateConfigGenerator());
		
		generators.add(new HibernateDaoGenerator());
		generators.add(new SpringDaoConfigurationGenerator());
	}
	
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
	
	@SuppressWarnings("unchecked")
	protected void setGenerators(String generatorsLine) {
		if(StringUtils.isTrimmedEmpty(generatorsLine)) {
			return;
		}
		String[] generatorsList = generatorsLine.split(";");
		generators.clear();
		
		for(String generator : generatorsList) {
			try {
				Class<? extends Generator> generatorClass = (Class<? extends Generator>) Class.forName(generator);
				generators.add(generatorClass.newInstance());
			} catch (Exception e) {
				throw new RuntimeException("Exception while creating generatos", e);
			} 
		}
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

	public List<Generator> getGenerators() {
		return generators;
	}
}
