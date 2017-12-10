package mda.generator.writers.java;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import mda.generator.beans.UmlAssociation;
import mda.generator.beans.UmlAttribute;
import mda.generator.beans.UmlClass;
import mda.generator.converters.ConverterInterface;

public class JavaClass {
	private final Path classPath;
	
	private final String packageName;
	
	private ImportManager importManager = new ImportManager();
	
	private final List<String> comments  = new ArrayList<>();	
	private final List<JavaAnnotation> annotationsList = new ArrayList<>();
	
	private Visibility visibilite = Visibility.PUBLIC;
	private String name;	

	// TODO final ? abstract ?
//  TODO private List<String> extendsList;
//	TODO private List<String> implementsList;
	
	private List<JavaAttribute> attributesList = new ArrayList<>();
	private List<JavaMethod> methodsList = new ArrayList<>();
	
	/**
	 * 
	 * @param javaPackage
	 * @param umlClass
	 * @param converter Converter de domain -> type java/sql
	 */
	public JavaClass(JavaPackage javaPackage, UmlClass umlClass, ConverterInterface converter) {
		this.classPath = javaPackage.getPackagePath().resolve(umlClass.getCamelCaseName() + ".java");
		this.name = umlClass.getCamelCaseName() ;
		
		if(umlClass.getComment() != null) {
			comments.addAll(Arrays.asList(umlClass.getComment().split("\n")));
		} 

		this.packageName = javaPackage.getPackageName();
		
		for(UmlAttribute umlAttribute : umlClass.getAttributes()) {
			JavaAttribute javaAttribut = new JavaAttribute(umlAttribute, converter, importManager);
			attributesList.add(javaAttribut);
			
			// Generate getter/setter
			methodsList.add(generateGetter(javaAttribut.getJavaType(), javaAttribut.getName(),javaAttribut.getComments()));
			methodsList.add(generateSetter(javaAttribut.getJavaType(), javaAttribut.getName(), javaAttribut.getComments()));
			
		}
		
		// FIXME gérer associations java
		for(UmlAssociation association : umlClass.getAssociations()) {
			//attributes, methods ..
		}
	}

	/**
	 * @return the classPath
	 */
	public Path getClassPath() {
		return classPath;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @return the importsList
	 */
	public Collection<String> getImportsList() {
		return importManager.getAllImports();
	}

	/**
	 * @return the comment
	 */
	public List<String> getComments() {
		return new ArrayList<>(comments);
	}

	/**
	 * @return the annotationsList
	 */
	public List<JavaAnnotation> getAnnotationsList() {
		return annotationsList;
	}

	/**
	 * @return the visibilite
	 */
	public Visibility getVisibilite() {
		return visibilite;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the attributesList
	 */
	public List<JavaAttribute> getAttributesList() {
		return attributesList;
	}

	/**
	 * @return the methodsList
	 */
	public List<JavaMethod> getMethodsList() {
		return methodsList;
	}

	
	
	private static JavaMethod generateGetter(String type, String attrName, List<String> comments) {
		JavaMethod getter = new JavaMethod(Visibility.PUBLIC, type, "get" + StringUtils.capitalize(attrName));
		
		// Comment from model
		for(String commentLine : comments) {
			getter.addCommentLine(commentLine);
		}
		// Comment for return
		getter.addCommentLine("@return value of " + attrName);
		
		getter.addContentLine("return " + attrName);
		
		return getter;
		
	}
	
	private static JavaMethod generateSetter(String type, String attrName, List<String> comments) {
		List<String> args = new ArrayList<>();
		args.add("final " + type + " " + attrName);
		
		JavaMethod setter = new JavaMethod(Visibility.PUBLIC, "void", "set" + StringUtils.capitalize(attrName), args);
		
		// Comment from model
		for(String commentLine : comments) {
			setter.addCommentLine(commentLine);
		}
		// Comment for param
		setter.addCommentLine("@param " + attrName + " new value to give to "  + attrName);
		
		// Content for setter
		setter.addContentLine("this." + attrName + " = " + attrName);

		return setter;
	}
}
