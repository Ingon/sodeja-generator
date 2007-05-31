package org.sodeja.generator.java;

public interface JavaContent {
	public String getContent();
	
	public String getCustomId();

	public boolean isCustomContent();

	public void setCustom(String customId);
	
	public void setContent(String content);
}
