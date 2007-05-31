package org.sodeja.generator.impl;

import java.util.List;

import org.sodeja.collections.CollectionUtils;
import org.sodeja.collections.ListUtils;
import org.sodeja.generator.GeneratorContext;
import org.sodeja.generator.java.JavaAccessModifier;
import org.sodeja.generator.java.JavaClass;
import org.sodeja.generator.java.JavaEnum;
import org.sodeja.generator.java.JavaField;
import org.sodeja.generator.java.JavaMethod;
import org.sodeja.generator.java.JavaMethodParameter;
import org.sodeja.generator.java.JavaPackage;
import org.sodeja.generator.java.JavaParameterizedType;
import org.sodeja.generator.java.JavaType;
import org.sodeja.generator.java.JavaTypeVariable;
import org.sodeja.generator.java.JavaTypeVariableReference;
import org.sodeja.generator.uml.UmlAssociation;
import org.sodeja.generator.uml.UmlAssociationEnd;
import org.sodeja.generator.uml.UmlAttribute;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlDependency;
import org.sodeja.generator.uml.UmlDependencyType;
import org.sodeja.generator.uml.UmlEnumeration;
import org.sodeja.generator.uml.UmlEnumerationLiteral;
import org.sodeja.generator.uml.UmlGeneralization;
import org.sodeja.generator.uml.UmlModel;
import org.sodeja.generator.uml.UmlMultiplicityRange;
import org.sodeja.generator.uml.UmlOperation;
import org.sodeja.generator.uml.UmlOrdering;
import org.sodeja.generator.uml.UmlReference;
import org.sodeja.generator.uml.UmlType;

public class ClassGenerator extends AbstractClassGenerator {
	
	protected static final JavaClass VOID_CLASS = new JavaClass(null, "void");
	
	protected static final JavaPackage UTIL_PACKAGE = JavaPackage.createFromDots("java.util");
	protected static final JavaClass COLLECTION_CLASS = new JavaClass(UTIL_PACKAGE, "Collection");
	protected static final JavaClass LIST_CLASS = new JavaClass(UTIL_PACKAGE, "List");
	protected static final JavaClass SET_CLASS = new JavaClass(UTIL_PACKAGE, "Set");
	
	@Override
	public void generate(GeneratorContext ctx, UmlModel model) {
		super.generate(ctx, model);
		
		List<UmlEnumeration> modelEnumerations = model.findEnumerationsByStereotype(getStereotype());
		for(UmlEnumeration modelEnumeration : modelEnumerations) {
			generate(ctx, model, modelEnumeration);
		}
		
		List<UmlClass> modelClasses = model.findClassesByStereotype(getStereotype());
		for(UmlClass modelClass : modelClasses) {
			generate(ctx, model, modelClass);
		}
	}

	protected void generate(GeneratorContext ctx, UmlModel model, UmlEnumeration modelEnumeration) {
		JavaPackage domainPackage = JavaPackage.createFromDots(modelEnumeration.getParentNamespace().getFullName());
		JavaEnum domainClass = createJavaEnum(domainPackage, model, modelEnumeration);
		writeClass(domainClass);
	}
	
	protected JavaEnum createJavaEnum(JavaPackage domainPackage, UmlModel model, UmlEnumeration modelEnumeration) {
		JavaEnum javaEnum = new JavaEnum(domainPackage, modelEnumeration.getName());
		for(UmlEnumerationLiteral attribute : modelEnumeration.getLiterals()) {
			javaEnum.addValue(attribute.getName());
		}
		return javaEnum;
	}
	
	protected void generate(GeneratorContext ctx, UmlModel model, UmlClass modelClass) {
		JavaPackage domainPackage = JavaPackage.createFromDots(modelClass.getParentNamespace().getFullName());
		JavaClass domainClass = createClass(domainPackage, model, modelClass);
		writeClass(domainClass);
	}
	
	protected JavaClass createClass(JavaPackage domainPackage, UmlModel model, UmlClass modelClass) {
		if(GeneratorUtils.isEnum(modelClass)) {
			throw new IllegalArgumentException("Implementation changed to use a UmlEnumeration type");
		}
		
		JavaClass domainClass = createJavaClass(domainPackage, model, modelClass);
		createAttributes(domainClass, model, modelClass);
		createAssociations(domainClass, model, modelClass);
		createOperations(domainClass, model, modelClass);
		return domainClass;
	}
	
	protected JavaClass createJavaClass(JavaPackage domainPackage, UmlModel model, UmlClass modelClass) {
		JavaClass domainClass = new JavaClass(domainPackage, modelClass.getName());
		if(CollectionUtils.isEmpty(modelClass.getGeneralizations())) {
			return domainClass;
		}

		if(modelClass.getGeneralizations().size() > 1) {
			throw new IllegalArgumentException("It is not possible to implement more than one class");
		}
		
		createParent(model, modelClass, domainClass);
		
		return domainClass;
	}

	protected void createParent(UmlModel model, UmlClass modelClass, JavaClass domainClass) {
		UmlReference<UmlGeneralization> generalizationRef = ListUtils.first(modelClass.getGeneralizations());
		UmlGeneralization generalization = generalizationRef.getReferent();
		UmlClass modelParentClass = (UmlClass) generalization.getParent().getReferent();
		domainClass.setParent(ClassGeneratorUtils.getJavaClass(modelParentClass));
		
		createGenerics(model, modelClass, domainClass, modelParentClass);
	}

	protected void createGenerics(UmlModel model, UmlClass modelClass, JavaClass domainClass, UmlClass modelParentClass) {
		for(UmlReference<UmlDependency> dependencyRef : modelClass.getDependencies()) {
			UmlDependency dependency = dependencyRef.getReferent();
			UmlDependency parentDependency = findNamedDependency(modelParentClass, dependency);
			if(parentDependency == null) {
				continue;
			}
			
			JavaClass parentClass = (JavaClass) domainClass.getParent();
			
			// TODO Check if it is not a leaf and add generic declaration!!!
			JavaParameterizedType realType = new JavaParameterizedType(parentClass);
			realType.getTypeArguments().add(createJavaClass(domainClass.getPackage(), model, dependency.getSupplier().getReferent()));
			domainClass.setParent(realType);
		}
	}
	
	protected UmlDependency findNamedDependency(UmlClass modelParentClass, UmlDependency childDependency) {
		for(UmlReference<UmlDependency> dependencyRef : modelParentClass.getDependencies()) {
			UmlDependency dependency = dependencyRef.getReferent();
			if(dependency.getName().equals(childDependency.getName())) {
				return dependency;
			}
		}
		
		return null;
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
		JavaType type = ClassGeneratorUtils.getJavaClass(attribute.getType());
		return new JavaField(type, attribute.getName());
	}

	protected JavaMethod createGetter(JavaField field) {
		return createGetter(field.getName(), field.getType());
	}

	protected JavaMethod createGetter(String name, JavaType type) {
		JavaMethod getter = new JavaMethod(type, "get" + NamingUtils.firstUpper(name));
		getter.setAccessModifier(JavaAccessModifier.PUBLIC);
		getter.setContent(String.format("return this.%s;", name));
		return getter;
	}

	protected JavaMethod createSetter(JavaField field) {
		JavaMethod setter = new JavaMethod(VOID_CLASS, "set" + NamingUtils.firstUpper(field.getName()));
		setter.setAccessModifier(JavaAccessModifier.PUBLIC);
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
		
		if(! otherEnd.isNavigateale()) {
			return null;
		}
		
		UmlType otherModelType = otherEnd.getReferent().getReferent();
		JavaType type = createType(domainClass, otherEnd, otherModelType, getDependency(modelClass, otherModelType));
		return new JavaField(type, otherEnd.getName());
	}

	protected UmlDependency getDependency(UmlClass modelClass, UmlType otherModelType) {
		for(UmlReference<UmlDependency> dependencyRef : modelClass.getDependencies()) {
			UmlDependency dependency = dependencyRef.getReferent();
			if(otherModelType.isReferent(dependency.getSupplier()) && dependency.getType() == UmlDependencyType.USAGE) {
				return dependency;
			}
		}
		return null;
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

	protected JavaType createType(JavaClass domainClass, UmlAssociationEnd otherEnd, UmlType otherModelType, UmlDependency dependency) {
		if(! otherEnd.getRange().isMulty()) {
			return createSingleType(domainClass, otherEnd, otherModelType, dependency);
		}
		
		return createMultyType(domainClass, otherEnd, otherModelType, dependency);
	}

	protected JavaType createSingleType(JavaClass domainClass, UmlAssociationEnd otherEnd, UmlType otherModelType, UmlDependency dependency) {
		JavaClass javaClass = ClassGeneratorUtils.getJavaClass(otherModelType);
		return createReferenceType(domainClass, otherEnd, dependency, javaClass);
	}

	protected JavaType createMultyType(JavaClass domainClass, UmlAssociationEnd otherEnd, UmlType otherModelType, UmlDependency dependency) {
		JavaClass baseClass = getJavaClass(otherEnd.getOrdering());
		JavaParameterizedType type = new JavaParameterizedType(baseClass);

		JavaClass javaClass = ClassGeneratorUtils.getJavaClass(otherModelType);
		JavaType refType = createReferenceType(domainClass, otherEnd, dependency, javaClass);
		type.getTypeArguments().add(refType);

		return type;
	}

	protected JavaType createReferenceType(JavaClass domainClass, UmlAssociationEnd otherEnd, UmlDependency dependency, JavaClass javaClass) {
		if(dependency == null) {
			return javaClass;
		}
		
		String typeVariableName = dependency.getName();
		domainClass.addTypeParameter(new JavaTypeVariable(typeVariableName, javaClass));
		return new JavaTypeVariableReference(typeVariableName);
	}
	
	protected JavaClass getJavaClass(UmlOrdering ordering) {
		if(ordering == UmlOrdering.ORDERED) {
			return LIST_CLASS;
		} else {
			return SET_CLASS;
		}
	}
}
