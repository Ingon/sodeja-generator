package org.sodeja.generator.java;

import java.util.ArrayList;
import java.util.List;

public class JavaConstructor extends JavaMember {
	private List<JavaMethodParameter> parameters;
	private List<JavaClass> exceptions;
	
	private String customId;
	private boolean customContent;
	private String content;
	
	public JavaConstructor() {
		parameters = new ArrayList<JavaMethodParameter>();
		exceptions = new ArrayList<JavaClass>();
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
}
