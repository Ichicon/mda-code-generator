package mda.generator.beans;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import mda.generator.writers.java.NamesComputingUtil;

/**
 * Attribute of a class
 * 
 * @author Fabien
 *
 */
public class UmlAttribute {
	/** Id in xml schema */
	private String id;
	
	private boolean isPK;
	private String name;
	private String camelCaseName;
	private UmlDomain domain;
	private String comment;
	private Boolean isNotNull;
	/** Atrtibute used for JPA updatable=false */
	private Boolean isReadonly = true;

	/**
	 * @return the isPK
	 */
	public boolean isPK() {
		return isPK;
	}

	/**
	 * @param isPK the isPK to set
	 */
	public void setPK(boolean isPK) {
		this.isPK = isPK;
	}

	/**
	 * @return the name
	 */
	public String getCamelCaseName() {
		return camelCaseName;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
		this.camelCaseName =  WordUtils.uncapitalize(NamesComputingUtil.computeCamelCaseName(name));
	}
	
	/**
	 * @return the rawName
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * @return the domain
	 */
	public UmlDomain getDomain() {
		return domain;
	}
	/**
	 * @param domain the domain to set
	 */
	public void setDomain(UmlDomain domain) {
		this.domain = domain;
	}
	/**
	 * @return the isNotNull
	 */
	public Boolean getIsNotNull() {
		return isNotNull;
	}
	/**
	 * @param isNotNull the isNotNull to set
	 */
	public void setIsNotNull(Boolean isNotNull) {
		this.isNotNull = isNotNull;
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
	 * @return the isReadonly
	 */
	public Boolean isReadonly() {
		return isReadonly;
	}

	/**
	 * @param isReadonly the isReadonly to set
	 */
	public void setReadonly(Boolean isReadonly) {
		this.isReadonly = isReadonly;
	}

	@Override
	public String toString() {
		return camelCaseName + " => " +
			name + " " + (isPK?"(PK)":"") + " " +
			domain.getName()+" " +
			(isNotNull?"not null":"nullable") +
			(isReadonly!=null && isReadonly?" NOT UPDATABLE":"");
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	
}
