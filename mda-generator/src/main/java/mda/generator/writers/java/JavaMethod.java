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
	
	private final List<String> content = new ArrayList<>();
	private final List<String> comments = new ArrayList<>();
	
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
	
	public JavaMethod(Visibility visibility, String returnType, String name) {
		this.visibility = visibility;
		this.returnType = returnType;
		this.name = name;
		this.args = new ArrayList<>();
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
	 * @return the content lines (copy)
	 */
	public List<String> getContent() {
		return new ArrayList<>(content);
	}
	
	/**
	 * Add line to content
	 * @param contentLine new line to add
	 */
	public void addContentLine(String contentLine) {
		content.add(contentLine);
	}
	

	/**
	 * @return the comments lines (copy)
	 */
	public List<String> getComments() {
		return new ArrayList<>(comments);
	}
	
	/**
	 * Add line to comment
	 * @param commentLine new line to add
	 */
	public void addCommentLine(String commentLine) {
		comments.add(commentLine);
	}
}
