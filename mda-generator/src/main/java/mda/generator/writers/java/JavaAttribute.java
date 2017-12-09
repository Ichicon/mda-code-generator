package mda.generator.writers.java;

import java.util.ArrayList;
import java.util.List;

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
	private final String comment;
	private final String javaType;
	private final boolean isNotNull;
	
	/**
	 * Creation of java attribute
	 * @param umlAttribute Uml attribute definition
	 * @param converter typeConverter
	 */
	public JavaAttribute(UmlAttribute umlAttribute, ConverterInterface converter) {
		this.javaType= converter.getJavaType(umlAttribute.getDomain().getName());
		this.name = umlAttribute.getCamelCaseName();
		this.comment = umlAttribute.getComment();
		this.isNotNull = umlAttribute.getIsNotNull();
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
	public String getComment() {
		return comment;
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
