package org.sodeja.generator.impl;

import java.io.PrintWriter;

import org.sodeja.generator.GeneratorContext;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlModel;

public class HibernateConfigGenerator extends ConfigurationFileDomainGenerator {
	@Override
	protected void generateBegin(GeneratorContext ctx, PrintWriter writer, UmlModel model) {
		writer.append("<!DOCTYPE hibernate-configuration PUBLIC\r\n");
		writer.append(" \"-//Hibernate/Hibernate Configuration DTD 3.0//EN\"\r\n");
		writer.append(" \"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd\">\r\n\r\n");
		
		writer.append("<hibernate-configuration>\r\n");
		writer.append("    <session-factory>\r\n");
		writer.append("        <property name=\"dialect\">org.hibernate.dialect.PostgreSQLDialect</property>\r\n");
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
