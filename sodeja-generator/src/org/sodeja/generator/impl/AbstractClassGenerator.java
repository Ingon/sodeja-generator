package org.sodeja.generator.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.sodeja.generator.GeneratorContext;
import org.sodeja.generator.java.DefaultJavaClassWriter;
import org.sodeja.generator.java.JavaClass;
import org.sodeja.generator.java.MergingJavaFile;
import org.sodeja.generator.uml.UmlModel;

public abstract class AbstractClassGenerator extends AbstractGenerator {
	private String sourceFolder;
	
	public File getSourceFolder() {
		return new File(sourceFolder);
	}
	
	@Override
	public void generate(GeneratorContext ctx, UmlModel model) {
		super.generate(ctx, model);
		if(sourceFolder == null) {
			throw new IllegalArgumentException();
		}
	}
	
	protected void writeClass(JavaClass clazz) {
		writeClass(getSourceFolder(), clazz);
	}
	
	protected static void writeClass(File sourceFolder, JavaClass clazz) {
		DefaultJavaClassWriter writer = new DefaultJavaClassWriter(clazz);
		
		try {
			File file = createFile(sourceFolder, clazz);
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
	
	private static File createFile(File sourceFolder, JavaClass clazz) {
		String packageName = clazz.getPackage().getFullName();
		
		File classFolder = new File(sourceFolder, packageName.replace(".", "/"));
		classFolder.mkdirs();
		
		return new File(classFolder, clazz.getName() + ".java");
	}
}
