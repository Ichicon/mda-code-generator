package mda.generator.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class UmlClass {	
	private String id;
	private String camelCaseName;
	private String name;
	private String comment;

	private UmlPackage xmiPackage;
	private List<UmlAttribute> attributs = new ArrayList<>();
	private List<UmlAssociation> associations = new ArrayList<>();

	/**
	 * @return the eaId
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param eaId the eaId to set
	 */
	public void setId(String eaId) {
		this.id = eaId;
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
		// On modifie le nom
		this.name = name;
		this.camelCaseName = StringUtils.remove(WordUtils.capitalizeFully(name, '_'), "_");
	}

	/**
	 * @return the rawName
	 */
	public String getName() {
		return name;
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
	public List<UmlAttribute> getAttributes() {
		return attributs;
	}
	
	/**
	 * Renvoie la liste des attributs PKs
	 * @return liste des attributs PKs
	 */
	public List<UmlAttribute> getPKs(){
		List<UmlAttribute> pks = new ArrayList<>();
		for(UmlAttribute attr : attributs) {
			if(attr.isPK()) {
				pks.add(attr);
			}
		}
		
		return pks;
	}

	/**
	 * @return the associations
	 */
	public List<UmlAssociation> getAssociations() {
		return associations;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\t. ").append(camelCaseName).append(" (").append(name).append(")").append(" : ");

		for(UmlAttribute attribut : attributs) {
			sb.append("\n\t\t- ").append(attribut);
		}
		
		for(UmlAssociation association : associations) {
			sb.append("\n\t\t> ").append(association);
		}

		return sb.toString(); 
	}
}
