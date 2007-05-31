package org.sodeja.generator.impl;

import org.sodeja.generator.java.JavaAccessModifier;
import org.sodeja.generator.java.JavaClass;
import org.sodeja.generator.java.JavaEnum;
import org.sodeja.generator.java.JavaInterface;
import org.sodeja.generator.java.JavaMethod;
import org.sodeja.generator.java.JavaMethodParameter;
import org.sodeja.generator.java.JavaPackage;
import org.sodeja.generator.java.JavaParameterizedType;
import org.sodeja.generator.java.JavaType;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlDataType;
import org.sodeja.generator.uml.UmlEnumeration;
import org.sodeja.generator.uml.UmlInterface;
import org.sodeja.generator.uml.UmlNamespace;
import org.sodeja.generator.uml.UmlOperation;
import org.sodeja.generator.uml.UmlOwnerScope;
import org.sodeja.generator.uml.UmlParameter;
import org.sodeja.generator.uml.UmlReference;
import org.sodeja.generator.uml.UmlType;

public class ClassGeneratorUtils {
	protected static JavaMethod createMethod(JavaClass domainClass, UmlOperation modelOperation) {
		JavaType resultType = getParameterType(modelOperation.getResult());
		JavaMethod method = new JavaMethod(resultType, modelOperation.getName());
		method.setAccessModifier(JavaAccessModifier.PUBLIC);
		method.setCustom(modelOperation.getId());
		method.setStatic(modelOperation.getScope() == UmlOwnerScope.CLASSIFIER);
		
		for(UmlParameter modelParam : modelOperation.getParameters()) {
			JavaType paramType = getParameterType(modelParam);
			method.addParameter(new JavaMethodParameter(paramType, modelParam.getName()));
		}
		
		return method;
	}

	protected static JavaType getParameterType(UmlParameter parameter) {
		JavaClass baseClass = getJavaClass(parameter.getType());
		
		String value = GeneratorUtils.getMulty(parameter);
		if(value == null) {
			return baseClass;
		}
		
		JavaParameterizedType type = new JavaParameterizedType(getMultyType(value));
		type.getTypeArguments().add(baseClass);
		return type;
	}
	
	protected static JavaClass getMultyType(String value) {
		if(value.equals("list")) {
			return ClassGenerator.LIST_CLASS;
		} else if(value.equals("collection")) {
			return ClassGenerator.COLLECTION_CLASS;
		} else if(value.equals("set")) {
			return ClassGenerator.SET_CLASS;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	protected static JavaClass getJavaClass(UmlReference<? extends UmlType> modelReference) {
		return getJavaClass(modelReference.getReferent());
	}
	
	protected static JavaClass getJavaClass(UmlType modelType) {
		if(modelType instanceof UmlDataType) {
			return new JavaClass(null, modelType.getName());
		} else if(modelType instanceof UmlInterface) {
			return getJavaClass((UmlInterface) modelType);
		} else if(modelType instanceof UmlClass) {
			return getJavaClass((UmlClass) modelType);
		} else if(modelType instanceof UmlEnumeration) {
			return getJavaClass((UmlEnumeration) modelType);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	protected static JavaClass getJavaClass(UmlClass modelClass) {
		JavaPackage pack = getJavaPackage(modelClass.getParentNamespace());
		return new JavaClass(pack, modelClass.getName());
	}

	protected static JavaInterface getJavaClass(UmlInterface modelInterface) {
		JavaPackage pack = getJavaPackage(modelInterface.getParentNamespace());
		return new JavaInterface(pack, modelInterface.getName());
	}
	
	protected static JavaEnum getJavaClass(UmlEnumeration modelEnumeration) {
		JavaPackage pack = getJavaPackage(modelEnumeration.getParentNamespace());
		return new JavaEnum(pack, modelEnumeration.getName());
	}
	
	protected static JavaPackage getJavaPackage(UmlNamespace namespace) {
		return JavaPackage.createFromDots(namespace.getFullName());
	}
}
