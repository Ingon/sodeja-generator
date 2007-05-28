package org.sodeja.generator.xml.spring;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.sodeja.collections.ListUtils;
import org.sodeja.functional.Predicate1;

public class SpringConfiguration {
	private List<SpringBean> beans;

	public SpringConfiguration() {
		beans = new ArrayList<SpringBean>();
	}
	
	public void add(SpringBean bean) {
		beans.add(bean);
	}
	
	public SpringBean current() {
		if(beans.isEmpty()) {
			throw new IllegalStateException("No current bean!");
		}
		return ListUtils.last(beans);
	}
	
	public SpringBean findByName(final String name) {
		return ListUtils.find(beans, new Predicate1<SpringBean>() {
			public Boolean execute(SpringBean bean) {
				return bean.getName().equals(name);
			}});
	}
	
	public void write(PrintWriter writer) {
		writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
		writer.append("<beans\r\n"); 
		writer.append("    xmlns=\"http://www.springframework.org/schema/beans\"\r\n");
		writer.append("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n");
		writer.append("    xsi:schemaLocation=\"http://www.springframework.org/schema/beans " +
				"http://www.springframework.org/schema/beans/spring-beans-2.0.xsd\">\r\n\r\n");
		
		for(SpringBean bean : beans) {
			bean.write(writer);
		}
		
		writer.append("</beans>\r\n");
	}
}
