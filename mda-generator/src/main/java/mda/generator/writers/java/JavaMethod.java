package mda.generator.writers.java;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a java method
 * @author Fabien Crapart
 *
 */
public class JavaMethod {
	private List<JavaAnnotation> annotations = new ArrayList<>();
	
	// TODO static, final ?

	private final Visibility visibility;
	private final String name;
	private final String returnType;
	private final List<String> args;
	
	private String content;
	
	/**
	 * Constructeur d'un attribut de classe java
	 * @param visibility
	 * @param returnType
	 * @param name
	 * @param args
	 */
	public JavaMethod(Visibility visibility, String returnType, String name, List<String> args) {
		this.visibility = visibility;
		this.returnType = returnType;
		this.name = name;
		this.args = new ArrayList<>(args);
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
	 * @return the returnType
	 */
	public String getReturnType() {
		return returnType;
	}

	/**
	 * @return the args
	 */
	public List<String> getArgs() {
		return new ArrayList<>(args);
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

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
