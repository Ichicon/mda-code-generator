package mda.generator.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class UmlClass {	
	private String eaId;
	private String name;
	private String rawName;
	private String comment;
	
	private UmlPackage xmiPackage;
	private List<UmlAttribute> attributs = new ArrayList<>();
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
		// On modifie le nom
		this.rawName = name;
		this.name = StringUtils.remove(WordUtils.capitalizeFully(name, '_'), "_");
	}
	
	/**
	 * @return the rawName
	 */
	public String getRawName() {
		return rawName;
	}
	
	/**
	 * @return the pack
	 */
	public UmlPackage getXmiPackage() {
		return xmiPackage;
	}
	/**
	 * @param pack the pack to set
	 */
	public void setXmiPackage(UmlPackage pack) {
		this.xmiPackage = pack;
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
	 * @return the attribut
	 */
	public List<UmlAttribute> getAttributs() {
		return attributs;
	}
	/**
	 * @param attribut the attribut to set
	 */
	public void setAttribut(List<UmlAttribute> attribut) {
		this.attributs = attribut;
	}	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\t. ").append(name).append(" (").append(rawName).append(")").append(" : ");
		
		for(UmlAttribute attribut : attributs) {
			sb.append("\n\t\t- ").append(attribut);
		}
		
		return sb.toString(); 
	}
}
