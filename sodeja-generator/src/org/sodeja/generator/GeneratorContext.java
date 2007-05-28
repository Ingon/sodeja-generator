package org.sodeja.generator;

import java.io.File;
import java.util.List;

public class GeneratorContext {
	private File modelFile;
	private List<Generator> generators;
	
	protected GeneratorContext() {
//		generators = new ArrayList<Generator>();
//		generators.add(new HibernateClassGenerator());
//		generators.add(new HibernateConfigGenerator());
//		
//		generators.add(new HibernateDaoGenerator());
//		generators.add(new SpringDaoConfigurationGenerator());
	}
	
	protected void setModelFile(File modelFile) {
		this.modelFile = modelFile;
	}
	
	public File getModelFile() {
		return modelFile;
	}

	public List<Generator> getGenerators() {
		return generators;
	}

	public void setGenerators(List<Generator> generators) {
		this.generators = generators;
	}
}
