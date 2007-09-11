package org.sodeja.generator.impl;

import java.io.PrintWriter;

import org.sodeja.generator.GeneratorContext;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlModel;

public class HibernateConfigGenerator extends ConfigurationFileDomainGenerator {
	
	private String dialect;
	
	public String getDialect() {
		return dialect;
	}
	
	@Override
	protected void generateBegin(GeneratorContext ctx, PrintWriter writer, UmlModel model) {
		writer.append("<!DOCTYPE hibernate-configuration PUBLIC\r\n");
		writer.append(" \"-//Hibernate/Hibernate Configuration DTD 3.0//EN\"\r\n");
		writer.append(" \"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd\">\r\n\r\n");
		
		writer.append("<hibernate-configuration>\r\n");
		writer.append("    <session-factory>\r\n");
		writer.format("        <property name=\"dialect\">%s</property>\r\n", dialect);
	}

	@Override
	protected void generateContent(GeneratorContext ctx, PrintWriter writer, UmlClass modelClass) {
		if(GeneratorUtils.isEmbedded(modelClass) || GeneratorUtils.isEnum(modelClass)) {
			return;
		}
		
		writer.format("        <mapping class=\"%s\"/>\r\n", modelClass.getFullName());
	}

	@Override
	protected void generateEnd(GeneratorContext ctx, PrintWriter writer, UmlModel model) {
		writer.append("    </session-factory>\r\n");
		writer.append("</hibernate-configuration>\r\n");
	}
}
