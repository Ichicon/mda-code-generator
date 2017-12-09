package mda.generator.writers.java;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import mda.generator.beans.UmlAssociation;
import mda.generator.beans.UmlAttribute;
import mda.generator.beans.UmlClass;
import mda.generator.converters.ConverterInterface;

public class JavaClass {
	private Path classPath;
	
	private String packageName;
	
	/** Import list computed or/and manually setted */
	private List<String> importsList = new ArrayList<>();
	
	private String comment;
	
	private List<JavaAnnotation> annotationsList;
	private Visibility visibilite = Visibility.PUBLIC;
	private String name;	

	// TODO final ? abstract ?
//  TODO private List<String> extendsList;
//	TODO private List<String> implementsList;
	
	private List<JavaAttribute> attributesList;
	private List<JavaMethod> methodsList;
	
	/**
	 * 
	 * @param javaPackage
	 * @param umlClass
	 * @param converter Converter de domain -> type java/sql
	 */
	public JavaClass(JavaPackage javaPackage, UmlClass umlClass, ConverterInterface converter) {
		this.classPath = javaPackage.getPackagePath().resolve(umlClass.getCamelCaseName() + ".java");
		this.name = umlClass.getCamelCaseName() ;
		this.comment = umlClass.getComment();
		this.packageName = javaPackage.getPackageName();
		
		for(UmlAttribute umlAttribute : umlClass.getAttributes()) {
			JavaAttribute javaAttribut = new JavaAttribute(umlAttribute, converter);
			
			// Generate methods for attribute
			
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
	public List<String> getImportsList() {
		return importsList;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
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


}
