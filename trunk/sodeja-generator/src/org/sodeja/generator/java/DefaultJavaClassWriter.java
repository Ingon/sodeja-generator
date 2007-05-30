package org.sodeja.generator.java;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.sodeja.collections.CollectionUtils;
import org.sodeja.collections.ListUtils;
import org.sodeja.functional.Predicate1;

public class DefaultJavaClassWriter {
	
	protected static final String START_PREFIX = "// Start Custom Content ";
	protected static final String CUSTOM_CODE = "// TODO: Custom code here!\r\n";
	protected static final String END_PREFIX = "// End Custom Content ";
	
	private JavaClass clazz;	
	private int level;
	private String levelString;
	
	public DefaultJavaClassWriter(JavaClass clazz) {
		this.clazz = clazz;
		this.levelString = "";
	}
	
	public void write(Writer writer, MergingJavaFile mergedContents) {
		PrintWriter out = new PrintWriter(writer);
		level = 0;
		
		writePackage(clazz.getPackage(), out);
		writeImports(clazz.getImports(), mergedContents, out);
		
		writeRootStart(out);
		
		level = 4;
		if(clazz instanceof JavaEnum) {
			writeValues(((JavaEnum) clazz).getValues(), out);
		}
		writeFields(clazz.getFields(), out);
		writeMethods(clazz.getMethods(), mergedContents, out);
		level = 0;
		
		writeRootEnd(out);
	}

	private void writeRootStart(PrintWriter out) {
		writeAnnotations(clazz, out);
		if(clazz instanceof JavaEnum) {
			out.format("%s enum %s {", getAccess(clazz), clazz.getName());
		} else if(clazz instanceof JavaInterface) {
			out.format("%s interface %s%s {", getAccess(clazz), clazz.getName(), getInterfaceExtends());
		} else if(clazz instanceof JavaClass) {
			out.format("%s class %s%s%s {", getAccess(clazz), clazz.getName(), getExtends(), getImplements());
		} else {
			throw new IllegalArgumentException();
		}
		out.println();
	}
	
	private void writeRootEnd(PrintWriter out) {
		out.println("}");
	}

	private void writePackage(JavaPackage pckg, PrintWriter out) {
		writeAnnotations(pckg, out);
		
		out.format("package %s;", pckg.getFullName());
		out.println();
		out.println();
	}
	
	private void writeAnnotations(Annotateable annotatable, PrintWriter out) {
		if(CollectionUtils.isEmpty(annotatable.getAnnotations())) {
			return;
		}
		
		for(JavaAnnotation annotation : annotatable.getAnnotations()) {
			writeAnnotation(annotation, out);
		}
	}

	private void writeAnnotation(JavaAnnotation annotation, PrintWriter out) {
//		out.format("%s@%s(%s)", getLevelPrefix(), annotation.getType().getName(), annotation.getText());
		out.format("%s@%s%s", getLevelPrefix(), getTypeText(annotation.getType()), getAnnotationText(annotation));
		out.println();
	}
	
	private String getAnnotationText(JavaAnnotation annotation) {
		if(annotation.getText() == null) {
			return "";
		}
		return String.format("(%s)", annotation.getText());
	}
	
	private void writeImports(List<JavaClass> imports, MergingJavaFile mergedContents, PrintWriter out) {
		List<JavaClass> realImports = new ArrayList<JavaClass>();
		realImports.addAll(mergedContents.getImports());
		
		if(! CollectionUtils.isEmpty(imports)) {
			for(Iterator<JavaClass> ite = imports.iterator();ite.hasNext();) {
				JavaClass importClass = ite.next();
				if(realImports.contains(importClass)) {
					continue;
				}
				
				if(containsSameName(realImports, importClass)) {
					continue;
				}
				
				realImports.add(importClass);
			}
		}
		
		Collections.sort(realImports, new Comparator<JavaClass>() {
			public int compare(JavaClass c1, JavaClass c2) {
				String p1 = c1.getPackage().getFullName();
				String p2 = c2.getPackage().getFullName();
				int comp = p1.compareTo(p2);
				if(comp != 0) {
					return comp;
				}
				
				return c1.getName().compareTo(c2.getName());
			}});
		for(JavaClass clazz : realImports) {
			if(clazz.getPackage().getFullName().equals("java.lang")) {
				continue;
			}
			writeImport(clazz, out);
		}
		out.println();
	}

	private boolean containsSameName(List<JavaClass> realImports, final JavaClass importClass) {
		return ListUtils.elem(realImports, new Predicate1<JavaClass>() {
			public Boolean execute(JavaClass realImportClass) {
				return realImportClass.getName().equals(importClass.getName());
			}});
	}

	private void writeImport(JavaClass clazz, PrintWriter out) {
		out.format("import %s.%s;", clazz.getPackage().getFullName(), clazz.getName());
		out.println();
	}

	private void writeFields(List<JavaField> fields, PrintWriter out) {
		if(CollectionUtils.isEmpty(fields)) {
			return;
		}

		for(JavaField field : fields) {
			writeField(field, out);
		}
	}
	
	private void writeField(JavaField field, PrintWriter out) {
		writeAnnotations(field, out);
		out.format("%s%s%s %s %s;", getLevelPrefix(), getAccess(field), getStatic(field), getTypeText(field.getType()), field.getName());
		out.println();
		out.println();
	}
	
	private void writeMethods(List<JavaMethod> methods, MergingJavaFile mergedContents, PrintWriter out) {
		if(CollectionUtils.isEmpty(methods)) {
			return;
		}

		for(JavaMethod method : methods) {
			writeMethod(method, mergedContents, out);
		}
	}

	private void writeMethod(JavaMethod method, MergingJavaFile mergedContents, PrintWriter out) {
		writeAnnotations(method, out);
		if(method.isAbstract()) {
			out.format("%s%s abstract %s %s(%s)%s;", getLevelPrefix(), getAccess(method), 
					getTypeText(method.getType()), method.getName(), getParameters(method), getThrows(method));
			out.println();
			return;
		}
		
		out.format("%s%s%s %s %s(%s)%s {", getLevelPrefix(), getAccess(method), getStatic(method), getTypeText(method.getType()), 
				method.getName(), getParameters(method), getThrows(method));
		out.println();
		
		if(method.isCustomContent()) {
			out.format(START_PREFIX + "<%s>\r\n", method.getCustomId());
			out.append(mergedContents.getFunctionContent(method.getCustomId()));
			out.format(END_PREFIX + "<%s>", method.getCustomId());
		} else {
			level = 8;
			String[] content = method.getContent().split("\r\n");
			for(int i = 0, n = content.length;i < n;i++) {
				String contentLine = content[i];
				out.format("%s%s", getLevelPrefix(), contentLine);
				if(i + 1 < n) {
					out.println();
				}
			}
			level = 4;
		}
		
		out.println();
		out.format("%s}", getLevelPrefix());
		out.println();
		out.println();
	}

	private void writeValues(List<String> values, PrintWriter out) {
		for(Iterator<String> ite = values.iterator();ite.hasNext();) {
			out.format("%s%s", getLevelPrefix(), ite.next());
			if(ite.hasNext()) {
				out.println(",");
			} else {
				out.println(";");
			}
		}
	}

	private String getParameters(JavaMethod method) {
		if(CollectionUtils.isEmpty(method.getParameters())) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		for(Iterator<JavaMethodParameter> ite = method.getParameters().iterator();ite.hasNext();) {
			JavaMethodParameter param = ite.next();
			sb.append(String.format("%s %s", getTypeText(param.getType()), param.getName()));
			if(ite.hasNext()) {
				sb.append(", ");
			}
		}
		
		return sb.toString();
	}

	private String getThrows(JavaMethod method) {
		if(CollectionUtils.isEmpty(method.getExceptions())) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(" throws ");
		for(Iterator<JavaClass> ite = method.getExceptions().iterator();ite.hasNext();) {
			JavaClass param = ite.next();
			sb.append(getTypeText(param));
			if(ite.hasNext()) {
				sb.append(", ");
			}
		}
		
		return sb.toString();
	}

	private String getAccess(AccessModifiable modifiable) {
		JavaAccessModifier modifier = modifiable.getAccessModifier();
		if(modifier == JavaAccessModifier.PACKAGE) {
			return "";
		}
		
		return modifier.name().toLowerCase();
	}
	
	private Object getStatic(JavaField field) {
		return field.isStatic() ? " static" : "";
	}

	private String getExtends() {
		if(clazz.getParent() == null) {
			return "";
		}
		return String.format(" extends %s", getObjectTypeText(clazz.getParent()));
	}

	private String getImplements() {
		if(CollectionUtils.isEmpty(clazz.getInterfaces())) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(" implements ");
		addWithSeparators(sb, clazz.getInterfaces());
		return sb.toString();
	}
	
	private String getInterfaceExtends() {
		if(CollectionUtils.isEmpty(clazz.getInterfaces())) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(" extends ");
		addWithSeparators(sb, clazz.getInterfaces());
		return sb.toString();
	}
	
	private void addWithSeparators(StringBuilder sb, List<JavaObjectType> classes) {
		for(Iterator<JavaObjectType> ite = classes.iterator();ite.hasNext();) {
			sb.append(getObjectTypeText(ite.next()));
			if(ite.hasNext()) {
				sb.append(", ");
			}
		}
	}
	
	private String getLevelPrefix() {
		if(levelString.length() == level) {
			return levelString;
		}
		
		StringBuilder sb = new StringBuilder();
		while(sb.length() < level) {
			sb.append(' ');
		}
		levelString = sb.toString();
		return levelString;
	}
	
	private String getTypeText(JavaType type) {
		if(type instanceof JavaObjectType) {
			return getObjectTypeText((JavaObjectType) type);
		} else if(type instanceof JavaPrimitive) {
			return ((JavaPrimitive) type).name().toLowerCase();
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	private String getObjectTypeText(JavaObjectType type) {
		if(CollectionUtils.isEmpty(type.getParams())) {
			return getTypeText(type.getBase());
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(getTypeText(type.getBase()));
		sb.append("<");
		for(Iterator<JavaClass> ite = type.getParams().iterator();ite.hasNext();) {
			JavaClass clazz = ite.next();
			sb.append(getTypeText(clazz));
			if(ite.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append(">");
		return sb.toString();
	}
	
	private String getTypeText(JavaClass type) {
		if(type.getPackage() == null) {
			return type.getName();
		}
		
		if(clazz.getPackage().getFullName().equals(type.getPackage().getFullName())) {
			return type.getName();
		}
		
		if(type.isSystem()) {
			return type.getName();
		}
		
		for(JavaClass importClazz : clazz.getImports()) {
			if(importClazz.getPackage().getFullName().equals(type.getPackage().getFullName()) 
					&& importClazz.getName().equals(type.getName())) {
				return type.getName();
			}
		}
		
		return String.format("%s.%s", type.getPackage().getFullName(), type.getName());
	}
}
