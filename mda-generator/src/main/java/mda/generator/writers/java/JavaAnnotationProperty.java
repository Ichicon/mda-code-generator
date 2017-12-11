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
	private final String value;
	
	/**
	 * Simple property for annotation
	 * @param name
	 * @param value
	 */
	public JavaAnnotationProperty(String name, String value) {
		this.name =name;
		this.value = value;		
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
}
