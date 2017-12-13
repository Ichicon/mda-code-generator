package mda.generator.writers.java;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a java method
 * @author Fabien Crapart
 *
 */
public class JavaMethod {
	private List<JavaAnnotation> annotationsList = new ArrayList<>();
	
	private final Visibility visibility;
	private final String name;
	private final String returnType;
	private final List<String> args;
	
	private final List<String> contentLines = new ArrayList<>();
	private final List<String> commentLines = new ArrayList<>();
	
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
	 * Method without args
	 * @param visibility
	 * @param returnType
	 * @param name
	 */
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
	 * @return the annotations (copy)
	 */
	public List<JavaAnnotation> getAnnotationsList() {
		return new ArrayList<>(annotationsList);
	}

	/**
	 * Add annotation to method
	 * @param annotation new annotation to add
	 */
	public void addAnnotations(JavaAnnotation annotation) {
		 annotationsList.add(annotation);
	}


	/**
	 * @return the content lines (copy)
	 */
	public List<String> getContentLines() {
		return new ArrayList<>(contentLines);
	}
	
	/**
	 * Add line to content
	 * @param contentLine new line to add
	 */
	public void addContentLine(String contentLine) {
		contentLines.add(contentLine);
	}
	

	/**
	 * @return the comments lines (copy)
	 */
	public List<String> getCommentsList() {
		return new ArrayList<>(commentLines);
	}
	
	/**
	 * Add line to comment
	 * @param commentLine new line to add
	 */
	public void addCommentLine(String commentLine) {
		commentLines.add(commentLine);
	}
	
	/**
	 * Display args as a list
	 * @return
	 */
	public String getDisplayArgs() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(String arg : getArgs()) {
			if(first) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(arg);
		}
		
		return sb.toString();
	}
}
