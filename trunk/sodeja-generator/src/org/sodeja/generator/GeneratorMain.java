package org.sodeja.generator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.sodeja.generator.impl.HibernateClassGenerator;
import org.sodeja.generator.impl.HibernateConfigGenerator;
import org.sodeja.generator.impl.HibernateDaoGenerator;
import org.sodeja.generator.impl.SpringDaoConfigurationGenerator;
import org.sodeja.generator.uml.UmlModel;
import org.sodeja.generator.uml.argouml.XmiParser;

public class GeneratorMain {
	public static void main(String[] args) throws Exception {
		GeneratorContext ctx = createContext();
		
		UmlModel model = null;
		if(ctx.getModelFile().getName().endsWith(".xmi")) {
			model = XmiParser.parseXmi(ctx.getModelFile());
		} else {
			model = XmiParser.parseZargo(ctx.getModelFile());
		}
		for(Generator generator : loadGenerators()) {
			generator.generate(ctx, model);
		}
	}

	private static List<Generator> loadGenerators() {
		List<Generator> result = new ArrayList<Generator>();
		result.add(new HibernateClassGenerator());
		result.add(new HibernateConfigGenerator());
		
		result.add(new HibernateDaoGenerator());
		result.add(new SpringDaoConfigurationGenerator());
		return result;
	}
	
	private static GeneratorContext createContext() throws IOException {
		GeneratorContext ctx = new GeneratorContext();
		
		File propsFile = new File("generator.properties");
		if(propsFile.exists()) {
			Properties props = new Properties();
			props.load(new FileReader(propsFile));
			
			ctx.setBaseFolder(props.getProperty("generator.base"));
			ctx.setModelFile(props.getProperty("generator.model"));
			ctx.setSourceFolder(props.getProperty("generator.source"));
			ctx.setConfigFolder(props.getProperty("generator.config"));
		} else {
			ctx.setBaseFolder("d:/Temp/izpit/");
//			ctx.setModelFile("izpit.bg.xmi");
			ctx.setModelFile("izpit.bg.zargo");
			ctx.setSourceFolder("src");
			ctx.setConfigFolder("web/WEB-INF/conf");
		}
		return ctx;
	}
}
