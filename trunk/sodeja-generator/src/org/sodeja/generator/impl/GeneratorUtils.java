package org.sodeja.generator.impl;

import org.sodeja.collections.CollectionUtils;
import org.sodeja.collections.ListUtils;
import org.sodeja.generator.uml.UmlAttribute;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlGeneralization;
import org.sodeja.generator.uml.UmlModel;
import org.sodeja.generator.uml.UmlOperation;
import org.sodeja.generator.uml.UmlParameter;
import org.sodeja.generator.uml.UmlTagDefinition;
import org.sodeja.generator.uml.UmlTagValue;
import org.sodeja.generator.uml.UmlTaggableElement;

public class GeneratorUtils {
	private GeneratorUtils() {
	}
	
	public static boolean isEnum(UmlClass modelClass) {
		return isSet(modelClass, "enum");
	}
	
	public static boolean isEmbedded(UmlClass modelClass) {
		return isSet(modelClass, "embedded");
	}
	
	public static boolean isCrud(UmlClass modelClass) {
		return isSet(modelClass, "crud");
	}

	public static boolean isDao(UmlOperation modelOperation) {
		return isSet(modelOperation, "dao");
	}

	public static String getMulty(UmlParameter modelParameter) {
		return getSetValue(modelParameter, "multy");
	}

	public static String getMulty(UmlAttribute modelAttribute) {
		return getSetValue(modelAttribute, "multy");
	}
	
	public static boolean isChild(UmlClass modelClass, String stereotype) {
		if(CollectionUtils.isEmpty(modelClass.getGeneralizations())) {
			return false;
		}
		if(modelClass.getGeneralizations().size() > 1) {
			throw new IllegalArgumentException();
		}
		UmlGeneralization generalization = ListUtils.first(modelClass.getGeneralizations()).getReferent();
		UmlClass parentClass = (UmlClass) generalization.getParent().getReferent();
		return isDomainObject(parentClass, stereotype);
	}
	
	private static boolean isDomainObject(UmlClass modelClass, String stereotype) {
		return modelClass.hasStereotype(stereotype);
	}
	
	public static boolean isParent(UmlModel model, UmlClass modelClass) {
		return ! model.findPerentGeneralizations(modelClass).isEmpty();
	}

	public static boolean isNotRootParent(UmlModel model, UmlClass modelClass) {
		return isParent(model, modelClass) && ! (CollectionUtils.isEmpty(modelClass.getGeneralizations())); //modelClass.getParent() != null
	}
	
	public static boolean isSet(UmlTaggableElement taggable, String tagName) {
		for(UmlTagValue tagValue : taggable.getTags()) {
			UmlTagDefinition tag = tagValue.getTag().getReferent();
			if(tag.getName().equals(tagName) && Boolean.parseBoolean(tagValue.getValue())) {
				return true;
			}
		}
		return false;
	}

	protected static String getSetValue(UmlTaggableElement taggable, String tagName) {
		for(UmlTagValue tagValue : taggable.getTags()) {
			UmlTagDefinition tag = tagValue.getTag().getReferent();
			if(tag.getName().equals(tagName)) {
				return tagValue.getValue();
			}
		}
		return null;
	}
}
