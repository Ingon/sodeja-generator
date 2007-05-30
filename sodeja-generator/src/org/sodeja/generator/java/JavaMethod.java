package org.sodeja.generator.java;

import java.util.ArrayList;
import java.util.List;

public class JavaMethod extends JavaMember {
	// Access flags
	private boolean isStatic;
	private boolean isAbstract;
	
	private JavaType returnType;
	private String name;
	
	private List<JavaMethodParameter> parameters;
	private List<JavaClass> exceptions;
	
	private String customId;
	private boolean customContent;
	private String content;
	
	public JavaMethod(JavaType returnType, String name) {
		this(returnType, name, new ArrayList<JavaMethodParameter>());
	}

	public JavaMethod(JavaType returnType, String name, List<JavaMethodParameter> parameters) {
		this.returnType = returnType;
		this.name = name;
		this.parameters = parameters;
		this.exceptions = new ArrayList<JavaClass>();
		this.content = "";
	}
	
	public JavaType getReturnType() {
		return returnType;
	}

	public List<JavaMethodParameter> getParameters() {
		return parameters;
	}
	
	public List<JavaClass> getExceptions() {
		return exceptions;
	}

	public void setCustom(String customId) {
		this.customId = customId;
		this.customContent = true;
	}
	
	public String getCustomId() {
		return customId;
	}

	public boolean isCustomContent() {
		return customContent;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void addParameter(JavaMethodParameter parameter) {
		parameters.add(parameter);
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public boolean isAbstract() {
		return isAbstract;
	}
	
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public String getName() {
		return name;
	}
}
