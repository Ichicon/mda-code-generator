package mda.generator.beans;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class UmlAttribute {
	private boolean isPK;
	private String rawName;
	private String name;
	private UmlDomain domain;
	private String comment;
	private Boolean isNotNull;
	private UmlAssociation association;
	
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
	 * @return the association
	 */
	public UmlAssociation getAssociation() {
		return association;
	}
	
	/**
	 * @param association the association to set
	 */
	public void setAssociation(UmlAssociation association) {
		this.association = association;
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
		this.rawName = name;
		this.name =  WordUtils.uncapitalize(StringUtils.remove(WordUtils.capitalizeFully(name, '_'), "_"));
	}
	
	/**
	 * @return the rawName
	 */
	public String getRawName() {
		return rawName;
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

	@Override
	public String toString() {
		return name + " => " + rawName + " " + (isPK?"(PK)":"") + " " +domain.getName()+" " + (isNotNull?"not null":"nullable") + " " + (association!=null?association.toString():"");
	}
}
