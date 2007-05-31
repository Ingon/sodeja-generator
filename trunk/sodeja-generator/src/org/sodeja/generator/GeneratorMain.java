package org.sodeja.generator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.sodeja.collections.ArrayUtils;
import org.sodeja.generator.uml.UmlModel;
import org.sodeja.generator.uml.argouml.XmiParser;
import org.sodeja.lang.StringUtils;
import org.sodeja.lang.reflect.ReflectUtils;

public class GeneratorMain {
	public static void main(String[] args) throws Exception {
		if(ArrayUtils.isEmpty(args) || args.length > 1) {
			throw new IllegalArgumentException("Should contain only the path to the generator properties");
		}
		File generatorPropertiesFile = new File(args[0]);
		if(! generatorPropertiesFile.exists()) {
			throw new IllegalArgumentException("Given generator properties does not exists");
		}
		GeneratorContext ctx = createContext(generatorPropertiesFile);
		
		UmlModel model = null;
		if(ctx.getModelFile().getName().endsWith(".xmi")) {
			model = XmiParser.parseXmi(ctx.getModelFile());
		} else {
			model = XmiParser.parseZargo(ctx.getModelFile());
		}
		for(Generator generator : ctx.getGenerators()) {
			generator.generate(ctx, model);
		}
	}

	private static GeneratorContext createContext(File generatorPropertiesFile) throws IOException {
		GeneratorContext ctx = new GeneratorContext();
		
		Properties generatorProperties = new Properties();
		generatorProperties.load(new FileReader(generatorPropertiesFile));
		
		ctx.setModelFile(new File(generatorProperties.getProperty("generator.model")));
		ctx.setGenerators(loadGenerators(generatorProperties));
		
		return ctx;
	}
	
	@SuppressWarnings("unchecked")
	private static List<Generator> loadGenerators(Properties generatorProperties) {
		String generatorsLine = generatorProperties.getProperty("generator.generators");
		if(StringUtils.isTrimmedEmpty(generatorsLine)) {
			throw new IllegalArgumentException("There should be some generators defined");
		}
		
		List<Generator> result = new ArrayList<Generator>();
		String[] generatorsList = generatorsLine.split(";");
		
		for(String generator : generatorsList) {
			try {
				result.add(loadGenerator(generatorProperties, generator));
			} catch (Exception e) {
				throw new RuntimeException("Problem while configuring generators", e);
			}
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	private static Generator loadGenerator(Properties generatorProperties, String generatorName) throws Exception {
		String generatorClassStr = generatorProperties.getProperty("generator." + generatorName);
		if(StringUtils.isTrimmedEmpty(generatorClassStr)) {
			throw new IllegalArgumentException("For every generator with <name> there should be a " +
					"property 'generator.<name>' defined with real generator class in it");
		}
		
		Class<? extends Generator> generatorClass = (Class<? extends Generator>) Class.forName(generatorClassStr);
		Generator generator = generatorClass.newInstance();
		
		for(String generatorProperty : generatorProperties.stringPropertyNames()) {
			if(! generatorProperty.startsWith("generator." + generatorName + ".")) {
				continue;
			}
			
			String property = generatorProperty.substring(generatorProperty.lastIndexOf(".") + 1);
			String propertyValue = generatorProperties.getProperty(generatorProperty);
			
			Object convertedValue = getConvertedValue(generator, property, propertyValue);
			ReflectUtils.setFieldValue(generator, property, convertedValue);
			
//			Field field = ReflectUtils.findFieldInHierarchy(generator, property);
//			if(field.getType() == Boolean.TYPE) {
//				ReflectUtils.setFieldValue(generator, property, Boolean.parseBoolean(propertyValue));
//			} else {
//				ReflectUtils.setFieldValue(generator, property, propertyValue);
//			}
		}
		
		return generator;
	}
	
	private static Object getConvertedValue(Generator generator, String property, String propertyValue) {
		Field field = ReflectUtils.findFieldInHierarchy(generator, property);
		if(field.getType() == Boolean.TYPE) {
			return Boolean.parseBoolean(propertyValue);
		} else {
			return propertyValue;
		}
	}
}
