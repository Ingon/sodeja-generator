package org.sodeja.generator.java;

import java.util.ArrayList;
import java.util.List;

public class JavaConstructor extends JavaMember implements JavaGenericDeclaration, JavaContent {
	private List<JavaTypeVariable> typeParameters;
	
	private List<JavaMethodParameter> parameters;
	private List<JavaClass> exceptions;
	
	private String customId;
	private boolean customContent;
	private String content;
	
	public JavaConstructor() {
		this.typeParameters = new ArrayList<JavaTypeVariable>();
		this.parameters = new ArrayList<JavaMethodParameter>();
		this.exceptions = new ArrayList<JavaClass>();
	}

	public List<JavaTypeVariable> getTypeParameters() {
		return typeParameters;
	}

	public void addTypeParameter(JavaTypeVariable parameter) {
		typeParameters.add(parameter);
	}
	
	public List<JavaMethodParameter> getParameters() {
		return parameters;
	}
	
	public List<JavaClass> getExceptions() {
		return exceptions;
	}

	public void addParameter(JavaMethodParameter parameter) {
		parameters.add(parameter);
	}

	@Override
	public String getContent() {
		return content;
	}
	
	@Override
	public String getCustomId() {
		return customId;
	}
	
	@Override
	public boolean isCustomContent() {
		return customContent;
	}
	
	@Override
	public void setCustom(String customId) {
		this.customContent = true;
		this.customId = customId;
		this.content = null;
	}

	@Override
	public void setContent(String content) {
		this.customContent = false;
		this.customId = null;
		this.content = content;
	}
}
