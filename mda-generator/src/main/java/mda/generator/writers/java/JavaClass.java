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

	private List<JavaAttribute> attributesList = new ArrayList<>();
	private List<JavaMethod> methodsList = new ArrayList<>();

	private JavaClass pkClass;

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

		// PKs
		managePKs(umlClass, converter);

		// Attributes, getter, setter
		manageAttributes(umlClass, converter);
	
		// Associations vers d'autres classes
		manageAssociations(umlClass, converter);
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

	/**
	 * @return the pkClass
	 */
	public JavaClass getPkClass() {
		return pkClass;
	}
	

	/**
	 * Generation des attributs et methodes (et classe si besoin) liées aux PKs
	 * @param umlClass
	 * @param converter
	 */
	private void managePKs(UmlClass umlClass, ConverterInterface converter) {

		if(umlClass.getPKs().size() > 1) {
			// FIXME multiple pks !
			// create class
			// use it as attribute
			// make getters/setters
		} else if(umlClass.getPKs().size() > 0){
			UmlAttribute umlPK = umlClass.getPKs().get(0);
			JavaAttribute javaAttribute = new JavaAttribute(umlPK, converter, importManager);
			attributesList.add(javaAttribute);

			// Generate getter/setter			
			JavaMethod getterPK = generateGetter(javaAttribute);
			String seqName = "\"" + JavaWriter.SEQUENCE_PREFIX + umlClass.getName().toUpperCase() + "\"";
		
			// Id annotation on PK field
			getterPK.addAnnotations(new JavaAnnotation(importManager.getFinalName("javax.persistence.Id")));
			// Annotation Sequence generator
			getterPK.addAnnotations(new JavaAnnotation(
					importManager.getFinalName("javax.persistence.SequenceGenerator"), 
					new JavaAnnotationProperty("name",seqName),
					new JavaAnnotationProperty("sequenceName",seqName),
					new JavaAnnotationProperty("allocationSize","20")
			));
		// Annotation on PK field to use generator
			getterPK.addAnnotations(new JavaAnnotation(
					importManager.getFinalName("javax.persistence.GeneratedValue"),
					new JavaAnnotationProperty("strategy",importManager.getFinalName("javax.persistence.GenerationType")+".SEQUENCE"),
					new JavaAnnotationProperty("generator",seqName)
			));	
			// Annotation pour le nom de la colonne
			getterPK.addAnnotations(new JavaAnnotation(
					importManager.getFinalName("javax.persistence.Column"),
					new JavaAnnotationProperty("name","\""+javaAttribute.getColumnName() +"\""),
					new JavaAnnotationProperty("nullable",javaAttribute.isNotNull()?"false":"true")
			));
			methodsList.add(getterPK);
			methodsList.add(generateSetter(javaAttribute));
		}
	}
	
	/**
	 * Attributs (no PLS) managements to create java attributes and getter/setters
	 * @param umlClass
	 * @param converter
	 */
	private void manageAttributes(UmlClass umlClass, ConverterInterface converter) {
		for(UmlAttribute umlAttribute : umlClass.getAttributes()) {
			// PKs managed before
			if(!umlAttribute.isPK()) {
				JavaAttribute javaAttribute = new JavaAttribute(umlAttribute, converter, importManager);
				attributesList.add(javaAttribute);

				// Generate getter/setter
				JavaMethod getter = generateGetter(javaAttribute);
				// Annotation pour le nom de la colonne
				getter.addAnnotations(new JavaAnnotation(
						importManager.getFinalName("javax.persistence.Column"),
						new JavaAnnotationProperty("name","\""+javaAttribute.getColumnName() +"\""),
						new JavaAnnotationProperty("nullable",javaAttribute.isNotNull()?"false":"true")
				));

				methodsList.add(getter);
				methodsList.add(generateSetter(javaAttribute));
			}
		}
	}
	
	/**
	 * Management of umlAssociation to create attributes and getter/setters
	 * @param umlClass
	 * @param converter
	 */
	private void manageAssociations(UmlClass umlClass, ConverterInterface converter) {
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
