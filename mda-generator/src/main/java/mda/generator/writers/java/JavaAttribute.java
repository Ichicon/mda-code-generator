package mda.generator.writers.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mda.generator.beans.UmlAssociation;
import mda.generator.beans.UmlAttribute;
import mda.generator.converters.ConverterInterface;

/**
 * Class representing a java class attribute
 * @author Fabien Crapart
 */
public class JavaAttribute {
private List<JavaAnnotation> annotations = new ArrayList<>();
	
	private final Visibility visibility = Visibility.PRIVATE;
	private final String name;
	private final List<String> comments = new ArrayList<>(); 
	private final String javaType;
	private final boolean isNotNull;
	
	
	
	/**
	 * Creation of java attribute
	 * @param umlAttribute Uml attribute definition
	 * @param converter typeConverter
	 */
	public JavaAttribute(UmlAttribute umlAttribute, ConverterInterface converter, ImportManager importManager) {
		this.javaType= importManager.getFinalName(converter.getJavaType(umlAttribute.getDomain().getName()));
		this.name = umlAttribute.getCamelCaseName();
		
		if(umlAttribute.getComment() != null) {
			this.comments.addAll(Arrays.asList(umlAttribute.getComment().split("\n")));
		} else {
			comments.add(JavaWriter.NO_COMMENT_FOUND);
		}
		
		this.isNotNull = umlAttribute.getIsNotNull();
		
		
		// Annotations ?
	}
	
	/**
	 * Attribute from association
	 * @param umlAssociation
	 * @param converter
	 * @param importManager
	 */
	public JavaAttribute(UmlAssociation umlAssociation, ConverterInterface converter, ImportManager importManager) {
		if(umlAssociation.isTargetMultiple()) {
			this.name = umlAssociation.getRoleName()+ "List";
		} else {
			this.name = umlAssociation.getRoleName();
		}
		
		String javaTypeName = umlAssociation.getTarget().getXmiPackage().getName() + "." + umlAssociation.getTarget().getCamelCaseName(); 
		this.javaType = importManager.getFinalName(javaTypeName);
		this.isNotNull = umlAssociation.isTargetNullable();
		
		this.comments.add("Association " + umlAssociation.getName() + " to " + umlAssociation.getTarget().getCamelCaseName());
	}

	/**
	 * @return the visibility
	 */
	public Visibility getVisibility() {
		return visibility;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * @return the javaType
	 */
	public String getJavaType() {
		return javaType;
	}
	
	/**
	 * @return the isNotNull
	 */
	public boolean isNotNull() {
		return isNotNull;
	}

	/**
	 * @return the comment
	 */
	public List<String> getComments() {
		return comments;
	}

	/**
	 * @return the annotations
	 */
	public List<JavaAnnotation> getAnnotations() {
		return annotations;
	}

	/**
	 * @param annotations the annotations to set
	 */
	public void setAnnotations(List<JavaAnnotation> annotations) {
		this.annotations = annotations;
	}
	
}
