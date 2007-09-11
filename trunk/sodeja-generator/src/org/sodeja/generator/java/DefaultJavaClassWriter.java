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
import org.sodeja.lang.StringUtils;

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
		writeConstructors(clazz.getConstructors(), mergedContents, out);
		writeMethods(clazz.getMethods(), mergedContents, out);
		level = 0;
		
		writeRootEnd(out);
	}

	private void writeRootStart(PrintWriter out) {
		writeAnnotations(clazz, out);
		if(clazz instanceof JavaEnum) {
			out.format("%s enum %s {", getAccess(clazz), clazz.getName());
		} else if(clazz instanceof JavaInterface) {
			out.format("%s interface %s%s%s {", getAccess(clazz), clazz.getName(), 
					getGenericDeclaration(clazz), getInterfaceExtends());
		} else if(clazz instanceof JavaClass) {
			out.format("%s class %s%s%s%s {", getAccess(clazz), clazz.getName(), 
					getGenericDeclaration(clazz), getExtends(), getImplements());
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
	
	private void writeAnnotations(JavaAnnotatedElement annotatable, PrintWriter out) {
		if(CollectionUtils.isEmpty(annotatable.getAnnotations())) {
			return;
		}
		
		for(JavaAnnotation annotation : annotatable.getAnnotations()) {
			writeAnnotation(annotation, out);
		}
	}

	private void writeAnnotation(JavaAnnotation annotation, PrintWriter out) {
		out.format("%s@%s%s", getLevelPrefix(), getTypeText(annotation.getType()), getAnnotationText(annotation));
		out.println();
	}
	
	private String getAnnotationText(JavaAnnotation annotation) {
		if(StringUtils.isTrimmedEmpty(annotation.getText())) {
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
		out.format("%s%s%s%s %s %s;", getLevelPrefix(), getAccess(field), getStatic(field), getFinal(field), getTypeText(field.getType()), field.getName());
		out.println();
		out.println();
	}
	
	private void writeConstructors(List<JavaConstructor> constructors, MergingJavaFile mergedContents, PrintWriter out) {
		if(CollectionUtils.isEmpty(constructors)) {
			return;
		}

		for(JavaConstructor constructor : constructors) {
			writeConstructor(constructor, mergedContents, out);
		}
	}

	private void writeConstructor(JavaConstructor constructor, MergingJavaFile mergedContents, PrintWriter out) {
		writeAnnotations(constructor, out);
		
		out.format("%s%s%s %s(%s)%s {", getLevelPrefix(), getAccess(constructor), 
				getConstrcutorGenericDeclaration(constructor), clazz.getName(), 
				getParameters(constructor), getThrows(constructor));
		
		out.println();
		
		writeCustomContent(constructor, mergedContents, out);

		out.println();
		out.format("%s}", getLevelPrefix());
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
			if(clazz instanceof JavaInterface) {
				out.format("%s%s%s %s %s(%s)%s;", getLevelPrefix(), getAccess(method), 
						getMethodGenericDeclaration(method), getTypeText(method.getReturnType()), 
						method.getName(), getParameters(method), getThrows(method));
			} else {
				out.format("%s%s abstract%s %s %s(%s)%s;", getLevelPrefix(), getAccess(method), 
						getMethodGenericDeclaration(method), getTypeText(method.getReturnType()), 
						method.getName(), getParameters(method), getThrows(method));
			}
			out.println();
			return;
		}
		
		out.format("%s%s%s%s %s %s(%s)%s {", getLevelPrefix(), getAccess(method), getStatic(method), 
				getMethodGenericDeclaration(method), getTypeText(method.getReturnType()), 
				method.getName(), getParameters(method), getThrows(method));
		out.println();
		
		writeCustomContent(method, mergedContents, out);
		
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

	private void writeCustomContent(JavaContent customContent, MergingJavaFile mergedContents, PrintWriter out) {
		if(customContent.isCustomContent()) {
			out.format(START_PREFIX + "<%s>\r\n", customContent.getCustomId());
			out.append(mergedContents.getFunctionContent(customContent.getCustomId()));
			out.format(END_PREFIX + "<%s>", customContent.getCustomId());
		} else {
			level = 8;
			String[] content = customContent.getContent().split("\r\n");
			for(int i = 0, n = content.length;i < n;i++) {
				String contentLine = content[i];
				out.format("%s%s", getLevelPrefix(), contentLine);
				if(i + 1 < n) {
					out.println();
				}
			}
			level = 4;
		}
	}

	private String getConstrcutorGenericDeclaration(JavaConstructor constructor) {
		String result = getGenericDeclaration(constructor);
		if(StringUtils.isTrimmedEmpty(result)) {
			return result;
		}
		
		return " " + result;
	}
	
	private String getMethodGenericDeclaration(JavaMethod method) {
		String result = getGenericDeclaration(method);
		if(StringUtils.isTrimmedEmpty(result)) {
			return result;
		}
		
		return " " + result;
	}
	
	private String getGenericDeclaration(JavaGenericDeclaration declaration) {
		if(CollectionUtils.isEmpty(declaration.getTypeParameters())) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		for(Iterator<JavaTypeVariable> ite = declaration.getTypeParameters().iterator();ite.hasNext();) {
			JavaTypeVariable parameter = ite.next();
			writeTypeVariable(parameter, sb);
			
			if(ite.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append(">");
		
		return sb.toString();
	}
	
	private void writeTypeVariable(JavaTypeVariable parameter, StringBuilder sb) {
		sb.append(parameter.getName());
		
		if(parameter.getBound() == null) {
			if(! CollectionUtils.isEmpty(parameter.getAdditionalBounds())) {
				throw new RuntimeException("Type bound is obligatory if you have additional bounds");
			}
			return;
		}
		
		sb.append(String.format(" extends %s", getTypeText(parameter.getBound())));
		
		if(CollectionUtils.isEmpty(parameter.getAdditionalBounds())) {
			return;
		}
		
		for(JavaInterface bound : parameter.getAdditionalBounds()) {
			sb.append(String.format(" & %s", getTypeText(bound)));
		}
	}
	
	private String getParameters(JavaConstructor constructor) {
		return getParametersString(constructor.getParameters());
	}
	
	private String getParameters(JavaMethod method) {
		return getParametersString(method.getParameters());
	}

	private String getParametersString(List<JavaMethodParameter> parameters) {
		if(CollectionUtils.isEmpty(parameters)) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		for(Iterator<JavaMethodParameter> ite = parameters.iterator();ite.hasNext();) {
			JavaMethodParameter param = ite.next();
			sb.append(String.format("%s %s", getTypeText(param.getType()), param.getName()));
			if(ite.hasNext()) {
				sb.append(", ");
			}
		}
		
		return sb.toString();
	}

	private String getThrows(JavaConstructor constructor) {
		return getThrows(constructor.getExceptions());
	}
	
	private String getThrows(JavaMethod method) {
		return getThrows(method.getExceptions());
	}
	
	private String getThrows(List<JavaClass> exceptions) {
		if(CollectionUtils.isEmpty(exceptions)) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(" throws ");
		for(Iterator<JavaClass> ite = exceptions.iterator();ite.hasNext();) {
			JavaClass param = ite.next();
			sb.append(getTypeText(param));
			if(ite.hasNext()) {
				sb.append(", ");
			}
		}
		
		return sb.toString();
	}

	private String getAccess(JavaAccessModifiable modifiable) {
		JavaAccessModifier modifier = modifiable.getAccessModifier();
		if(modifier == JavaAccessModifier.PACKAGE) {
			return "";
		}
		
		return modifier.name().toLowerCase();
	}
	
	private String getStatic(JavaField field) {
		return field.isStatic() ? " static" : "";
	}

	private String getFinal(JavaField field) {
		return field.isFinal() ? " final" : "";
	}

	private String getStatic(JavaMethod method) {
		return method.isStatic() ? " static" : "";
	}
	
	private String getExtends() {
		if(clazz.getParent() == null) {
			return "";
		}
		return String.format(" extends %s", getTypeText(clazz.getParent()));
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
	
	private void addWithSeparators(StringBuilder sb, List<JavaType> types) {
		for(Iterator<JavaType> ite = types.iterator();ite.hasNext();) {
			sb.append(getTypeText(ite.next()));
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
		if(type instanceof JavaClass) {
			return getTypeText((JavaClass) type);
		} else if(type instanceof JavaPrimitiveType) {
			return ((JavaPrimitiveType) type).name().toLowerCase();
		} else if(type instanceof JavaTypeVariableReference) {
			return ((JavaTypeVariableReference) type).getName();
		} else if(type instanceof JavaWildcardType) {
			return getTypeText((JavaWildcardType) type);
		} else if(type instanceof JavaParameterizedType) {
			return getTypeText((JavaParameterizedType) type);
		} else if(type instanceof JavaArray) {
			return getTypeText((JavaArray) type);
		} else {
			throw new IllegalArgumentException();
		}
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
	
	private String getTypeText(JavaWildcardType type) {
		if(type.getLowerBounds() != null) {
			return String.format("? super %s", getTypeText(type.getLowerBounds()));
		} else if(type.getUpperBounds() != null) {
			return String.format("? extends %s", getTypeText(type.getUpperBounds()));
		} else {
			return "?";
		}
	}
	
	private String getTypeText(JavaParameterizedType type) {
		if(CollectionUtils.isEmpty(type.getTypeArguments())) {
			return getTypeText(type.getType());
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(getTypeText(type.getType()));
		sb.append("<");
		for(Iterator<JavaType> ite = type.getTypeArguments().iterator();ite.hasNext();) {
			JavaType clazz = ite.next();
			sb.append(getTypeText(clazz));
			if(ite.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append(">");
		return sb.toString();
	}
	
	private String getTypeText(JavaArray type) {
		return getTypeText(type.getType()) + "[]";
	}
}
