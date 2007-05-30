package org.sodeja.generator.java_old;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergingJavaFile {
	private List<JavaClass> imports;
	private Map<String, String> functions;
	
	public MergingJavaFile() {
		imports = new ArrayList<JavaClass>();
		functions = new HashMap<String, String>();
	}
	
	public List<JavaClass> getImports() {
		return imports;
	}
	
	public void addImportLine(String importValue) {
		String trimedLine = importValue.trim();
		if(! trimedLine.endsWith(";")) {
			throw new RuntimeException();
		}
		
		String noLineEnd = trimedLine.substring(0, trimedLine.length() - 1);

		String[] lineData = noLineEnd.split(" ");
		if(lineData.length != 2) {
			throw new RuntimeException();
		}
		String classPortion = lineData[1];
		
		int lastDotIndex = classPortion.lastIndexOf('.');
		
		String packageName = classPortion.substring(0, lastDotIndex);
		JavaPackage pack = JavaPackage.createFromDots(packageName);
		
		String className = classPortion.substring(lastDotIndex + 1);
		if(className.equals("*")) {
			throw new IllegalArgumentException("No multy import is supported, please optimize imports first!");
		}
		
		JavaClass clazz = new JavaClass(pack, className);
		imports.add(clazz);
	}

	public Map<String, String> getFunctions() {
		return functions;
	}
	
	public String getFunctionContent(String id) {
		String value = functions.get(id);
		if(value == null) {
			value = DefaultJavaClassWriter.CUSTOM_CODE;
		}
		return value;
	}

	public static void loadCustom(Reader reader, MergingJavaFile merged) throws Exception {
		boolean collectingLines = false;
		StringBuilder collector = new StringBuilder();
		String customId = null;
		
		BufferedReader br = new BufferedReader(reader);
		for(String line = br.readLine();line != null;line = br.readLine()) {
			if(line.startsWith(DefaultJavaClassWriter.START_PREFIX)) {
				customId = line.substring(line.indexOf('<') + 1, line.indexOf('>'));
				collectingLines = true;
			} else if(line.startsWith(DefaultJavaClassWriter.END_PREFIX)) {
				merged.getFunctions().put(customId, collector.toString());
				customId = null;
				collector.setLength(0);
				collectingLines = false;
			} else if(collectingLines) {
				collector.append(line);
				collector.append("\r\n");
			} else if(line.startsWith("import")) {
				merged.addImportLine(line);
			}
		}
	}
}
