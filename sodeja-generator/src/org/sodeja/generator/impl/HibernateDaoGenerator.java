package org.sodeja.generator.impl;

import java.util.ArrayList;
import java.util.List;

import org.sodeja.collections.CollectionUtils;
import org.sodeja.generator.GeneratorContext;
import org.sodeja.generator.java.JavaClass;
import org.sodeja.generator.java.JavaInterface;
import org.sodeja.generator.java.JavaMethod;
import org.sodeja.generator.java.JavaPackage;
import org.sodeja.generator.java.JavaType;
import org.sodeja.generator.uml.UmlClass;
import org.sodeja.generator.uml.UmlModel;
import org.sodeja.generator.uml.UmlOperation;
import org.sodeja.generator.uml.UmlPackage;

public class HibernateDaoGenerator extends SimpleDomainGenerator {
	
	private static final JavaPackage JAVA_LANG_PACKAGE = new JavaPackage("java.lang");
	private static final JavaClass LONG = new JavaClass(JAVA_LANG_PACKAGE, "Long");
	
	private static final JavaPackage PARENT_DAO_PACKAGE = JavaPackage.createFromDots("org.sodeja.hibernate");
	protected static final JavaClass PARENT_DAO = new JavaInterface(PARENT_DAO_PACKAGE, "GenericDao");
	protected static final JavaClass PARENT_DAO_IMPL = new JavaClass(PARENT_DAO_PACKAGE, "GenericDaoImpl");
	
	@Override
	protected void generate(GeneratorContext ctx, UmlModel model, UmlClass modelClass) {
		if(! GeneratorUtils.isCrud(modelClass)) {
			return;
		}
		
		List<UmlOperation> modelDaoOperations = getDaoOperations(modelClass);
		if(CollectionUtils.isEmpty(modelDaoOperations)) {
			return;
		}
		
		UmlPackage modelRootPackage = modelClass.getParentPackage().getParent();
		UmlPackage modelDaoPackage = new UmlPackage(modelRootPackage);
		modelDaoPackage.setName("dao");
		
		JavaPackage daoPackage = JavaPackage.createFromDots(modelDaoPackage.getFullName());
		
		JavaInterface parentInterface = createInterface(ctx, modelClass, modelDaoOperations, daoPackage);
		createImpl(ctx, modelClass, modelDaoOperations, parentInterface);
	}

	private JavaInterface createInterface(GeneratorContext ctx, UmlClass modelClass, List<UmlOperation> modelDaoOperations, JavaPackage daoPackage) {
		JavaInterface daoInterface = new JavaInterface(daoPackage, modelClass.getName() + "Dao");
		daoInterface.addInterface(createParentType(modelClass));

		for(UmlOperation modelOperation : modelDaoOperations) {
			JavaMethod method = ClassGeneratorUtils.createMethod(daoInterface, modelOperation);
			daoInterface.addMethod(method);
		}
		
		GeneratorUtils.writeClass(ctx, daoInterface);
		return daoInterface;
	}

	private void createImpl(GeneratorContext ctx, UmlClass modelClass, List<UmlOperation> modelDaoOperations, JavaInterface parentInterface) {
		JavaClass daoClass = new JavaClass(parentInterface.getPackage(), modelClass.getName() + "DaoImpl");
		daoClass.setParent(createParentTypeImpl(modelClass));
		daoClass.addInterface(new JavaType(parentInterface));

		for(UmlOperation modelOperation : modelDaoOperations) {
			JavaMethod method = ClassGeneratorUtils.createMethod(daoClass, modelOperation);
			daoClass.addMethod(method);
		}
		
		GeneratorUtils.writeClass(ctx, daoClass);
	}

	private List<UmlOperation> getDaoOperations(UmlClass modelClass) {
		List<UmlOperation> result = new ArrayList<UmlOperation>();
		for(UmlOperation modelOperation : modelClass.getOperations()) {
			if(GeneratorUtils.isDao(modelOperation)) {
				result.add(modelOperation);
			}
		}
		return result;
	}
	
	private JavaType createParentType(UmlClass modelClass) {
		JavaType parent = new JavaType(getDaoInterface());
		parent.addParameter(ClassGeneratorUtils.getJavaClass(modelClass));
		parent.addParameter(LONG);
		return parent;
	}

	private JavaType createParentTypeImpl(UmlClass modelClass) {
		JavaType parent = new JavaType(getDaoImplementation());
		parent.addParameter(ClassGeneratorUtils.getJavaClass(modelClass));
		parent.addParameter(LONG);
		return parent;
	}
	
	protected JavaClass getDaoInterface() {
		return PARENT_DAO;
	}
	
	protected JavaClass getDaoImplementation() {
		return PARENT_DAO_IMPL;
	}
}
