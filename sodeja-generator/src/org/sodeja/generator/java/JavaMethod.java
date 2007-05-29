package org.sodeja.generator.java;

import java.util.ArrayList;
import java.util.List;

public class JavaMethod extends JavaField {
	private boolean _abstract;
	
	private List<JavaMethodParameter> parameters;
	private List<JavaClass> exceptions;
	
	private String customId;
	private boolean customContent;
	private String content;
	
	public JavaMethod(JavaType type, String name) {
		this(type, name, new ArrayList<JavaMethodParameter>());
	}

	public JavaMethod(JavaType type, String name, List<JavaMethodParameter> parameters) {
		super(type, name);
		this.accessModifier = JavaAccessModifier.PUBLIC;
		this.parameters = parameters;
		this.exceptions = new ArrayList<JavaClass>();
		this.content = "";
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

	public boolean isAbstract() {
		return _abstract;
	}
	
	public void setAbstract(boolean _abstract) {
		this._abstract = _abstract;
	}
}
