package mda.generator.writers.java;

/**
 * Property of an annotation ( ex: @ManyToMany(name = value) )
 * The value can be another annotation.
 * 
 * @author Fabien Crapart
 */
public class JavaAnnotationProperty {	
	private String name;
	
	/** True if value is annotation */
	private boolean isAnnotation;
	
	private String value;
	private JavaAnnotation annotationValue;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the annotationValue
	 */
	public JavaAnnotation getAnnotationValue() {
		return annotationValue;
	}
	/**
	 * @param annotationValue the annotationValue to set
	 */
	public void setAnnotationValue(JavaAnnotation annotationValue) {
		this.annotationValue = annotationValue;
		if(annotationValue != null) {
			isAnnotation = true;
		} else {
			isAnnotation = false;
		}
	}
	/**
	 * @return the isAnnotation
	 */
	public boolean isAnnotation() {
		return isAnnotation;
	}

}
