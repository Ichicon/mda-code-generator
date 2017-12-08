package mda.generator.beans;

import java.util.ArrayList;
import java.util.List;

public class UmlPackage {
	private String eaId;
	private String name;
	private String comment;
	private List<UmlClass> classes = new ArrayList<>();
	/**
	 * @return the eaId
	 */
	public String getEaId() {
		return eaId;
	}
	/**
	 * @param eaId the eaId to set
	 */
	public void setEaId(String eaId) {
		this.eaId = eaId;
	}
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
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return the classes
	 */
	public List<UmlClass> getClasses() {
		return classes;
	}
	/**
	 * @param classes the classes to set
	 */
	public void setClasses(List<UmlClass> classes) {
		this.classes = classes;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n").append(name);
		for(UmlClass xmiClass : classes) {
			sb.append(xmiClass);
		}	
		
		return sb.toString();
	}
	
}
