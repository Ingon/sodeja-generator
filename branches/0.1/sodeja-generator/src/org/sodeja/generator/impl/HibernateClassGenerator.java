package org.sodeja.generator.impl;

import org.sodeja.generator.java.JavaClass;
import org.sodeja.generator.java.JavaField;
import org.sodeja.generator.java.JavaMethod;
import org.sodeja.generator.java.JavaPackage;
import org.sodeja.generator.java.JavaType;
import org.sodeja.generator.uml.UmlAggregationType;
import org.sodeja.generator.uml.UmlAssociation;
import org.sodeja.generator.uml.UmlAssociationEnd;
import org.sodeja.generator.uml.UmlAttribute;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlModel;
import org.sodeja.generator.uml.UmlOperation;
import org.sodeja.generator.uml.UmlType;

public class HibernateClassGenerator extends ClassGenerator {
	
	private static final JavaClass LONG = new JavaClass(null, "Long");
	
	private static final JavaPackage PERSISTANCE_PACKAGE = JavaPackage.createFromDots("javax.persistence");
	private static final JavaClass PERSISTANCE_BASIC = new JavaClass(PERSISTANCE_PACKAGE, "Basic");
	private static final JavaClass PERSISTANCE_CASCADE_TYPE = new JavaClass(PERSISTANCE_PACKAGE, "CascadeType");
	private static final JavaClass PERSISTANCE_EMBEDDED = new JavaClass(PERSISTANCE_PACKAGE, "Embedded");
	private static final JavaClass PERSISTANCE_EMBEDDABLE = new JavaClass(PERSISTANCE_PACKAGE, "Embeddable");
	private static final JavaClass PERSISTANCE_ENTITY = new JavaClass(PERSISTANCE_PACKAGE, "Entity");
	private static final JavaClass PERSISTANCE_ENUMERATED = new JavaClass(PERSISTANCE_PACKAGE, "Enumerated");
	private static final JavaClass PERSISTANCE_ENUM_TYPE = new JavaClass(PERSISTANCE_PACKAGE, "EnumType");
//	private static final JavaClass PERSISTANCE_FETCH_TYPE = new JavaClass(PERSISTANCE_PACKAGE, "FetchType");
	private static final JavaClass PERSISTANCE_GENERATED_VALUE = new JavaClass(PERSISTANCE_PACKAGE, "GeneratedValue");
	private static final JavaClass PERSISTANCE_GENERATION_TYPE = new JavaClass(PERSISTANCE_PACKAGE, "GenerationType");
	private static final JavaClass PERSISTANCE_ID = new JavaClass(PERSISTANCE_PACKAGE, "Id");
	private static final JavaClass PERSISTANCE_INHERITANCE = new JavaClass(PERSISTANCE_PACKAGE, "Inheritance");
	private static final JavaClass PERSISTANCE_INHERITANCE_TYPE = new JavaClass(PERSISTANCE_PACKAGE, "InheritanceType");
	private static final JavaClass PERSISTANCE_MAPPED_SUPERCLASS = new JavaClass(PERSISTANCE_PACKAGE, "MappedSuperclass");
	private static final JavaClass PERSISTANCE_ONE_TO_MANY = new JavaClass(PERSISTANCE_PACKAGE, "OneToMany");
	private static final JavaClass PERSISTANCE_ONE_TO_ONE = new JavaClass(PERSISTANCE_PACKAGE, "OneToOne");
	private static final JavaClass PERSISTANCE_MANY_TO_MANY = new JavaClass(PERSISTANCE_PACKAGE, "ManyToMany");
	private static final JavaClass PERSISTANCE_MANY_TO_ONE = new JavaClass(PERSISTANCE_PACKAGE, "ManyToOne");
	private static final JavaClass PERSISTANCE_SEQUENCE_GENERATOR = new JavaClass(PERSISTANCE_PACKAGE, "SequenceGenerator");
	private static final JavaClass PERSISTANCE_TABLE = new JavaClass(PERSISTANCE_PACKAGE, "Table");
	
	private static final JavaPackage HIBERNATE_PACKAGE = JavaPackage.createFromDots("org.hibernate.annotations");
	private static final JavaClass HIBERNATE_ACCESS_TYPE = new JavaClass(HIBERNATE_PACKAGE, "AccessType");
	private static final JavaClass HIBERNATE_CASCADE = new JavaClass(HIBERNATE_PACKAGE, "Cascade");
//	private static final JavaClass HIBERNATE_COLLECTION_OF_ELEMENTS = new JavaClass(HIBERNATE_PACKAGE, "CollectionOfElements");
	private static final JavaClass HIBERNATE_INDEX_COLUMN = new JavaClass(HIBERNATE_PACKAGE, "IndexColumn");

	@Override
	protected JavaClass createJavaClass(JavaPackage domainPackage, UmlModel model, UmlClass modelClass) {
		JavaClass clazz = super.createJavaClass(domainPackage, model, modelClass);
		if(GeneratorUtils.isEmbedded(modelClass)) {
			addEmbeddedAnnotations(clazz);
		} else {
			addDomainAnnotations(model, modelClass, clazz);
		}
		return clazz;
	}
	
	@Override
	protected void createAttributes(JavaClass domainClass, UmlModel model, UmlClass modelClass) {
		if(! GeneratorUtils.isEmbedded(modelClass) && ! GeneratorUtils.isChild(modelClass)) {
			addDomainId(domainClass);
		}
		
		super.createAttributes(domainClass, model, modelClass);
	}

	@Override
	protected JavaField createField(JavaClass domainClass, UmlModel model, UmlAttribute attribute) {
		JavaField field = super.createField(domainClass, model, attribute);
		
		UmlType otherType = attribute.getType().getReferent();
		if(otherType instanceof UmlClass) {
			UmlClass otherClass = (UmlClass) otherType;
			if(GeneratorUtils.isEmbedded(otherClass)) { 
				field.addAnnotation(PERSISTANCE_EMBEDDED);
			} else {
				field.addAnnotation(PERSISTANCE_BASIC);
			}
		} else {
			field.addAnnotation(PERSISTANCE_BASIC);
		}
		
		return field;
	}

	@Override
	protected JavaField createField(JavaClass domainClass, UmlModel model, UmlClass modelClass, UmlAssociation modelAssociation) {
		JavaField field = super.createField(domainClass, model, modelClass, modelAssociation);
		if(field == null) {
			return null;
		}
		
		UmlAssociationEnd thisModelEnd = modelAssociation.getThisEnd(modelClass);
		UmlAssociationEnd otherModelEnd = modelAssociation.getOtherEnd(modelClass);
		UmlClass otherModelClass = otherModelEnd.getReferent().getReferent();
		if(GeneratorUtils.isEmbedded(otherModelClass)) {
			field.addAnnotation(PERSISTANCE_EMBEDDED);
		} else if(GeneratorUtils.isEnum(otherModelClass)) {
			domainClass.addImport(PERSISTANCE_ENUM_TYPE);
			field.addAnnotation(PERSISTANCE_ENUMERATED, "EnumType.STRING");
		} else if(otherModelEnd.getRange().isMulty()) {
			if(thisModelEnd.getType() == UmlAggregationType.COMPOSITE) {
				field.addAnnotation(PERSISTANCE_ONE_TO_MANY);
				field.addAnnotation(HIBERNATE_INDEX_COLUMN, "name=\"number_in_row\"");
				field.addAnnotation(HIBERNATE_CASCADE, "value={org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN}");
			} else {
				field.addAnnotation(PERSISTANCE_MANY_TO_MANY);
				field.addAnnotation(HIBERNATE_INDEX_COLUMN, "name=\"number_in_row\"");
			}
		} else if(thisModelEnd.getType() == UmlAggregationType.COMPOSITE) {
			domainClass.addImport(PERSISTANCE_CASCADE_TYPE);
			field.addAnnotation(PERSISTANCE_ONE_TO_ONE, "cascade=CascadeType.ALL");
		} else if(thisModelEnd.getRange().isMulty()) {
			field.addAnnotation(PERSISTANCE_MANY_TO_ONE);
		} else {
			field.addAnnotation(PERSISTANCE_ONE_TO_ONE);
		}
		return field;
	}

	@Override
	protected JavaMethod createMethod(JavaClass domainClass, UmlModel model, UmlOperation modelOperation) {
		if(GeneratorUtils.isDao(modelOperation)) {
			return null;
		}
		
		return super.createMethod(domainClass, model, modelOperation);
	}

	private void addEmbeddedAnnotations(JavaClass clazz) {
		clazz.addAnnotation(PERSISTANCE_EMBEDDABLE);
		clazz.addAnnotation(HIBERNATE_ACCESS_TYPE, "\"field\"");
	}
	
	private void addDomainAnnotations(UmlModel model, UmlClass modelClass, JavaClass clazz) {
		if(isNotRootParent(model, modelClass)) {
			clazz.addAnnotation(PERSISTANCE_MAPPED_SUPERCLASS);
			return;
		}
		
		addDomainAnnotations(clazz);
		
		if(isParent(model, modelClass)) {
			clazz.addImport(PERSISTANCE_INHERITANCE_TYPE);
			clazz.addAnnotation(PERSISTANCE_INHERITANCE, "strategy=InheritanceType.JOINED");
		}
	}
	
	private boolean isParent(UmlModel model, UmlClass modelClass) {
		return ! model.findPerentGeneralizations(modelClass).isEmpty();
	}

	private boolean isNotRootParent(UmlModel model, UmlClass modelClass) {
		return isParent(model, modelClass) && modelClass.getParent() != null;
	}
	
	private void addDomainAnnotations(JavaClass clazz) {
		String mappingName = NamingUtils.toTableName(clazz.getName());
		
		clazz.addAnnotation(PERSISTANCE_ENTITY);
		clazz.addAnnotation(HIBERNATE_ACCESS_TYPE, "\"field\"");
		clazz.addAnnotation(PERSISTANCE_TABLE, "name=\"t_" + mappingName + "\"");
		clazz.addAnnotation(PERSISTANCE_SEQUENCE_GENERATOR, "name=\"SEQ_GENERATOR\", sequenceName=\"t_" + mappingName + "_id_seq\"");
	}

	private void addDomainId(JavaClass clazz) {
		clazz.addImport(PERSISTANCE_GENERATION_TYPE);
		
		JavaField idField = new JavaField(new JavaType(LONG), "id");
		idField.addAnnotation(PERSISTANCE_ID);
		idField.addAnnotation(PERSISTANCE_GENERATED_VALUE, "strategy=GenerationType.SEQUENCE, generator=\"SEQ_GENERATOR\"");
		clazz.addField(idField);
		
		clazz.addMethod(createGetter("id", idField.getType()));
	}
}
