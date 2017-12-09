package mda.generator.writers.java;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Class representing a java annotation
 * @author Fabien Crapart
 *
 */
public class JavaAnnotation {
	/** Full annotation name */
	private final String name;
	/** Pacakge name computed for imports resolution */
	private final String packageName;

	/** Properties of annotation if needed, ex : \@Annotation(properties) */
	private List<JavaAnnotationProperty> properties = new ArrayList<>();
		
	/**
	 * Constructor of annotation
	 * @param name Full name of annotation (with package)
	 */
	public JavaAnnotation(String name) {
		this.name = name;
		this.packageName = StringUtils.substringBeforeLast(name, ".");
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @return the properties
	 */
	public List<JavaAnnotationProperty> getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(List<JavaAnnotationProperty> properties) {
		this.properties = properties;
	}
}
