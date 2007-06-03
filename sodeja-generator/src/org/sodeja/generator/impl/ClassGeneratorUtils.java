package org.sodeja.generator.impl;

import java.util.List;

import org.sodeja.generator.java.JavaAccessModifier;
import org.sodeja.generator.java.JavaArray;
import org.sodeja.generator.java.JavaClass;
import org.sodeja.generator.java.JavaEnum;
import org.sodeja.generator.java.JavaField;
import org.sodeja.generator.java.JavaInterface;
import org.sodeja.generator.java.JavaMethod;
import org.sodeja.generator.java.JavaMethodParameter;
import org.sodeja.generator.java.JavaPackage;
import org.sodeja.generator.java.JavaParameterizedType;
import org.sodeja.generator.java.JavaPrimitiveType;
import org.sodeja.generator.java.JavaType;
import org.sodeja.generator.uml.UmlAttribute;
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
	public static void addOperations(List<UmlOperation> modelDaoOperations, JavaClass type) {
		for(UmlOperation modelOperation : modelDaoOperations) {
			JavaMethod method = createMethod(type, modelOperation);
			type.addMethod(method);
		}
	}
	
	public static JavaMethod createMethod(JavaClass domainClass, UmlOperation modelOperation) {
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
		JavaType baseType = getJavaClass(parameter.getType());
		
		String value = GeneratorUtils.getMulty(parameter);
		if(value == null) {
			return baseType;
		}
		
		return getMultyType(baseType, value);
	}
	
	protected static JavaType getMultyType(JavaType baseType, String value) {
		if(value.equals("list")) {
			return getParameteriziedType(baseType, ClassGenerator.LIST_CLASS);
		} else if(value.equals("collection")) {
			return getParameteriziedType(baseType, ClassGenerator.COLLECTION_CLASS);
		} else if(value.equals("set")) {
			return getParameteriziedType(baseType, ClassGenerator.SET_CLASS);
		} else if(value.equals("array")) {
			return new JavaArray(baseType);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	protected static JavaParameterizedType getParameteriziedType(JavaType baseClass, JavaClass realClass) {
		JavaParameterizedType type = new JavaParameterizedType(realClass);
		type.getTypeArguments().add(baseClass);
		return type;
	}
	
	protected static JavaType getAttributeType(UmlAttribute attribute) {
		JavaType baseType = getJavaClass(attribute.getType());
		
		String value = GeneratorUtils.getMulty(attribute);
		if(value == null) {
			return baseType;
		}

		return getMultyType(baseType, value);
	}
	
	protected static JavaType getJavaClass(UmlReference<? extends UmlType> modelReference) {
		return getJavaClass(modelReference.getReferent());
	}
	
	protected static JavaType getJavaClass(UmlType modelType) {
		if(modelType instanceof UmlDataType) {
			try {
				return JavaPrimitiveType.valueOf(modelType.getName().toUpperCase());
			} catch(Exception exc) {
				return new JavaClass(null, modelType.getName());
			}
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
	
	public static JavaClass getJavaClass(UmlClass modelClass) {
		JavaPackage pack = getJavaPackage(modelClass.getParentNamespace());
		return new JavaClass(pack, modelClass.getName());
	}

	public static JavaInterface getJavaClass(UmlInterface modelInterface) {
		JavaPackage pack = getJavaPackage(modelInterface.getParentNamespace());
		return new JavaInterface(pack, modelInterface.getName());
	}
	
	public static JavaEnum getJavaClass(UmlEnumeration modelEnumeration) {
		JavaPackage pack = getJavaPackage(modelEnumeration.getParentNamespace());
		return new JavaEnum(pack, modelEnumeration.getName());
	}
	
	public static JavaPackage getJavaPackage(UmlNamespace namespace) {
		return JavaPackage.createFromDots(namespace.getFullName());
	}

	public static JavaMethod createGetter(JavaField field) {
		return createGetter(field.getName(), field.getType());
	}
	
	public static JavaMethod createGetter(String name, JavaType type) {
		JavaMethod getter = new JavaMethod(type, "get" + NamingUtils.firstUpper(name));
		getter.setAccessModifier(JavaAccessModifier.PUBLIC);
		getter.setContent(String.format("return this.%s;", name));
		return getter;
	}
	
	public static JavaMethod createSetter(JavaField field) {
		JavaMethod setter = new JavaMethod(ClassGenerator.VOID_TYPE, "set" + NamingUtils.firstUpper(field.getName()));
		setter.setAccessModifier(JavaAccessModifier.PUBLIC);
		setter.addParameter(new JavaMethodParameter(field.getType(), field.getName()));
		setter.setContent(String.format("this.%s = %s;", field.getName(), field.getName()));
		return setter;
	}
}
