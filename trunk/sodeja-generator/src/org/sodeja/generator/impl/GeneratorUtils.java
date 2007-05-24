package org.sodeja.generator.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.sodeja.generator.GeneratorContext;
import org.sodeja.generator.java.DefaultJavaClassWriter;
import org.sodeja.generator.java.JavaClass;
import org.sodeja.generator.java.MergingJavaFile;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlOperation;
import org.sodeja.generator.uml.UmlTagDefinition;
import org.sodeja.generator.uml.UmlTagValue;
import org.sodeja.generator.uml.UmlTaggableElement;

public class GeneratorUtils {
	private GeneratorUtils() {
	}

	protected static void writeClass(GeneratorContext ctx, JavaClass clazz) {
		DefaultJavaClassWriter writer = new DefaultJavaClassWriter(clazz);
		
		try {
			File file = createFile(ctx, clazz);
			MergingJavaFile mergedContents = new MergingJavaFile();
			if(file.exists()) {
				FileReader fileReader = new FileReader(file);
				MergingJavaFile.loadCustom(fileReader, mergedContents);
				fileReader.close();
			}
			
			FileWriter fileWriter = new FileWriter(file);
			writer.write(fileWriter, mergedContents);
			fileWriter.close();
		} catch(Exception exc) {
			throw new RuntimeException(exc);
		}
	}
	
	private static File createFile(GeneratorContext ctx, JavaClass clazz) {
		String packageName = clazz.getPackage().getFullName();
		File sourceFolder = ctx.getSourceFolder();
		
		File classFolder = new File(sourceFolder, packageName.replace(".", "/"));
		classFolder.mkdirs();
		
		return new File(classFolder, clazz.getName() + ".java");
	}
	
	public static boolean isEnum(UmlClass modelClass) {
		return isSet(modelClass, "enum");
	}
	
	public static boolean isEmbedded(UmlClass modelClass) {
		return isSet(modelClass, "embedded");
	}
	
	public static boolean isEnumerated(UmlClass modelClass) {
		return isSet(modelClass, "enum");
	}
	
	public static boolean isCrud(UmlClass modelClass) {
		return isSet(modelClass, "crud");
	}

	public static boolean isDao(UmlOperation modelOperation) {
		return isSet(modelOperation, "dao");
	}
	
	public static boolean isChild(UmlClass modelClass) {
		if(modelClass.getParent() == null) {
			return false;
		}
		return isDomainObject(modelClass.getParent().getReferent().getParent().getReferent());
	}
	
	private static boolean isDomainObject(UmlClass modelClass) {
		return modelClass.getStereotype().getReferent().getName().equals(SimpleDomainGenerator.DOMAIN_STEREOTYPE);
	}

	public static boolean isSet(UmlTaggableElement taggable, String tagName) {
		for(UmlTagValue tagValue : taggable.getTags()) {
			UmlTagDefinition tag = tagValue.getTag().getReferent();
			if(tag.getName().equals(tagName) && Boolean.parseBoolean(tagValue.getValue())) {
				return true;
			}
		}
		return false;
	}
}
