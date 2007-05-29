package org.sodeja.generator.impl;

import java.util.List;

import org.sodeja.generator.GeneratorContext;
import org.sodeja.generator.java.JavaClass;
import org.sodeja.generator.java.JavaEnum;
import org.sodeja.generator.java.JavaField;
import org.sodeja.generator.java.JavaMethod;
import org.sodeja.generator.java.JavaPackage;
import org.sodeja.generator.java.JavaMethodParameter;
import org.sodeja.generator.java.JavaType;
import org.sodeja.generator.uml.UmlAssociation;
import org.sodeja.generator.uml.UmlAssociationEnd;
import org.sodeja.generator.uml.UmlAttribute;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlGeneralization;
import org.sodeja.generator.uml.UmlModel;
import org.sodeja.generator.uml.UmlMultiplicityRange;
import org.sodeja.generator.uml.UmlOperation;
import org.sodeja.generator.uml.UmlOrdering;

public class ClassGenerator extends AbstractClassGenerator {
	
	protected static final JavaClass VOID_CLASS = new JavaClass(null, "void");
	
	protected static final JavaPackage UTIL_PACKAGE = JavaPackage.createFromDots("java.util");
	protected static final JavaClass COLLECTION_CLASS = new JavaClass(UTIL_PACKAGE, "Collection");
	protected static final JavaClass LIST_CLASS = new JavaClass(UTIL_PACKAGE, "List");
	protected static final JavaClass SET_CLASS = new JavaClass(UTIL_PACKAGE, "Set");
	
	@Override
	public void generate(GeneratorContext ctx, UmlModel model) {
		super.generate(ctx, model);
		
		List<UmlClass> modelClasses = model.findClassesByStereotype(getStereotype());
		for(UmlClass modelClass : modelClasses) {
			generate(ctx, model, modelClass);
		}
	}

	protected void generate(GeneratorContext ctx, UmlModel model, UmlClass modelClass) {
		JavaPackage domainPackage = JavaPackage.createFromDots(modelClass.getParentPackage().getFullName());
		JavaClass domainClass = createClass(domainPackage, model, modelClass);
		writeClass(domainClass);
	}
	
	protected JavaClass createClass(JavaPackage domainPackage, UmlModel model, UmlClass modelClass) {
		if(GeneratorUtils.isEnum(modelClass)) {
			return createJavaEnum(domainPackage, model, modelClass);
		}
		
		JavaClass domainClass = createJavaClass(domainPackage, model, modelClass);
		createAttributes(domainClass, model, modelClass);
		createAssociations(domainClass, model, modelClass);
		createOperations(domainClass, model, modelClass);
		return domainClass;
	}
	
	protected JavaClass createJavaEnum(JavaPackage domainPackage, UmlModel model, UmlClass modelClass) {
		JavaEnum javaEnum = new JavaEnum(domainPackage, modelClass.getName());
		for(UmlAttribute attribute : modelClass.getAttributes()) {
			javaEnum.addValue(attribute.getName());
		}
		return javaEnum;
	}
	
	protected JavaClass createJavaClass(JavaPackage domainPackage, UmlModel model, UmlClass modelClass) {
		JavaClass domainClass = new JavaClass(domainPackage, modelClass.getName());
		if(modelClass.getParent() != null) {
			UmlGeneralization modelGeneralization = modelClass.getParent().getReferent();
			UmlClass modelParentClass = modelGeneralization.getParent().getReferent();
			domainClass.setParent(ClassGeneratorUtils.getJavaType(modelParentClass));
		}
		
		return domainClass;
	}

	protected void createAttributes(JavaClass domainClass, UmlModel model, UmlClass modelClass) {
		for(UmlAttribute attribute : modelClass.getAttributes()) {
			if(attribute.getRange() != UmlMultiplicityRange.ONE_ONE) {
				throw new IllegalArgumentException();
			}
			
			JavaField field = createField(domainClass, model, attribute);
			domainClass.addField(field);
			domainClass.addMethod(createGetter(field));
			domainClass.addMethod(createSetter(field));
		}
	}

	protected JavaField createField(JavaClass domainClass, UmlModel model, UmlAttribute attribute) {
		JavaType type = ClassGeneratorUtils.getJavaType(attribute.getType());
		return new JavaField(type, attribute.getName());
	}

	protected JavaMethod createGetter(JavaField field) {
		return createGetter(field.getName(), field.getType());
	}

	protected JavaMethod createGetter(String name, JavaType type) {
		JavaMethod getter = new JavaMethod(type, "get" + NamingUtils.firstUpper(name));
		getter.setContent(String.format("return this.%s;", name));
		return getter;
	}

	protected JavaMethod createSetter(JavaField field) {
		JavaMethod setter = new JavaMethod(new JavaType(VOID_CLASS), "set" + NamingUtils.firstUpper(field.getName()));
		setter.addParameter(new JavaMethodParameter(field.getType(), field.getName()));
		setter.setContent(String.format("this.%s = %s;", field.getName(), field.getName()));
		return setter;
	}

	protected void createAssociations(JavaClass domainClass, UmlModel model, UmlClass modelClass) {
		List<UmlAssociation> associations = model.findAssociations(modelClass);
		for(UmlAssociation association : associations) {
			JavaField field = createField(domainClass, model, modelClass, association);
			if(field == null) {
				continue;
			}
			domainClass.addField(field);
			domainClass.addMethod(createGetter(field));
			domainClass.addMethod(createSetter(field));
		}
	}

	protected JavaField createField(JavaClass domainClass, UmlModel model, UmlClass modelClass, UmlAssociation modelAssociation) {
		UmlAssociationEnd otherEnd = modelAssociation.getOtherEnd(modelClass);
		UmlClass otherModelClass = otherEnd.getReferent().getReferent();
		
		if(! otherEnd.isNavigateale()) {
			return null;
		}
		
		JavaType type = createType(otherEnd, otherModelClass);
		return new JavaField(type, otherEnd.getName());
	}
	
	protected void createOperations(JavaClass domainClass, UmlModel model, UmlClass modelClass) {
		for(UmlOperation modelOperation : modelClass.getOperations()) {
			JavaMethod method = createMethod(domainClass, model, modelOperation);
			if(method != null) {
				domainClass.addMethod(method);
			}
		}
	}

	protected JavaMethod createMethod(JavaClass domainClass, UmlModel model, UmlOperation modelOperation) {
		return ClassGeneratorUtils.createMethod(domainClass, modelOperation);
	}

	protected JavaType createType(UmlAssociationEnd otherEnd, UmlClass otherModelClass) {
		JavaClass otherClass = ClassGeneratorUtils.getJavaClass(otherModelClass);
		if(otherEnd.getRange().isMulty()) {
			JavaType type = new JavaType(getJavaClass(otherEnd.getOrdering()));
			type.addParameter(otherClass);
			return type;
		} else {
			return new JavaType(otherClass);
		}
	}
	
	protected JavaClass getJavaClass(UmlOrdering ordering) {
		if(ordering == UmlOrdering.ORDERED) {
			return LIST_CLASS;
		} else {
			return SET_CLASS;
		}
	}
}
