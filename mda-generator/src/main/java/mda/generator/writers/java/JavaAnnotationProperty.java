package mda.generator.writers.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mda.generator.exceptions.MdaGeneratorException;

/**
 * Property of an annotation ( ex: @ManyToMany(name = value) )
 * The value can be another annotation.
 * 
 * @author Fabien Crapart
 */
public class JavaAnnotationProperty {	
	private final String name;
	
	/** True if value is annotation */
	private final boolean isAnnotation;
	
	private final String value;
	private final List<JavaAnnotation> annotationValues = new ArrayList<>();
	
	/**
	 * Simple property for annotation
	 * @param name
	 * @param value
	 */
	public JavaAnnotationProperty(String name, String value) {
		this.name =name;
		this.value = value;		
		this.isAnnotation =false;
	}
	
	/**
	 * Property wich is another annotation
	 * @param name
	 * @param value
	 */
	public JavaAnnotationProperty(String name, JavaAnnotation... values) {
		this.name =name;
		this.value = null;
		if(values.length <= 0) {
			throw new MdaGeneratorException("Annotation property " + name + " must have at least one value");
		}
		
		this.annotationValues.addAll(Arrays.asList(values));
		this.isAnnotation = true;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the annotationValues
	 */
	public List<JavaAnnotation> getAnnotationValue() {
		return annotationValues;
	}

	/**
	 * @return the isAnnotation
	 */
	public boolean isAnnotation() {
		return isAnnotation;
	}

}
