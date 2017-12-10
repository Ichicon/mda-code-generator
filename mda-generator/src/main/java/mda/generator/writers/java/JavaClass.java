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
	 * @param converter
	 */
	public JavaClass(JavaPackage javaPackage, UmlClass umlClass, ConverterInterface converter) {
		this.classPath = javaPackage.getPackagePath().resolve(umlClass.getCamelCaseName() + ".java");
		this.name = umlClass.getCamelCaseName() ;
		
		if(umlClass.getComment() != null) {
			comments.addAll(Arrays.asList(umlClass.getComment().split("\n")));
		} else {
			comments.add(JavaWriter.NO_COMMENT_FOUND);
		}

		this.packageName = javaPackage.getPackageName();
		
		// Annotation entity
		annotationsList.add(new JavaAnnotation(importManager.getFinalName("javax.persistence.Entity")));
		// Annotation table
		annotationsList.add(
				new JavaAnnotation(
						importManager.getFinalName("javax.persistence.Table"),
						new JavaAnnotationProperty("name","\"" + umlClass.getName() + "\"")
				)
		);
		
		
		// Attributes, getter, setter
		for(UmlAttribute umlAttribute : umlClass.getAttributes()) {
			JavaAttribute javaAttribute = new JavaAttribute(umlAttribute, converter, importManager);
			attributesList.add(javaAttribute);
			
			// Generate getter/setter
			methodsList.add(generateGetter(javaAttribute));
			methodsList.add(generateSetter(javaAttribute));		
		}
		
		// Associations vers d'autres classes
		for(UmlAssociation association : umlClass.getAssociations()) {
			if(association.isTargetNavigable()) {
				JavaAttribute javaAttribute = new JavaAttribute(association, converter, importManager);
				attributesList.add(javaAttribute);
				
				// Generate getter/setter 
				// TODO +  JPA annotations for getter setters
				methodsList.add(generateGetter(javaAttribute));
				methodsList.add(generateSetter(javaAttribute));	
			}
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

	
	
	private static JavaMethod generateGetter(JavaAttribute attribute) {
		JavaMethod getter = new JavaMethod(Visibility.PUBLIC, attribute.getJavaType(), "get" + StringUtils.capitalize(attribute.getName()));
		
		// Comment from model
		for(String commentLine : attribute.getComments()) {
			getter.addCommentLine(commentLine);
		}
		// Comment for return
		getter.addCommentLine("@return value of " + attribute.getName());
		
		getter.addContentLine("return " + attribute.getName());
		
		return getter;		
	}
	
	private static JavaMethod generateSetter(JavaAttribute attribute) {
		List<String> args = new ArrayList<>();
		args.add("final " + attribute.getJavaType() + " " + attribute.getName());
		
		JavaMethod setter = new JavaMethod(Visibility.PUBLIC, "void", "set" + StringUtils.capitalize(attribute.getName()), args);
		
		// Comment from model
		for(String commentLine : attribute.getComments()) {
			setter.addCommentLine(commentLine);
		}
		// Comment for param
		setter.addCommentLine("@param " + attribute.getName() + " new value to give to "  + attribute.getName());
		
		// Content for setter
		setter.addContentLine("this." + attribute.getName() + " = " + attribute.getName());

		return setter;
	}
}
