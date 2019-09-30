package mda.generator.beans;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class inside a package (containing attributes and associations with other classes)
 *
 * @author Fabien Crapart
 *
 */
public class UmlClass {
	private String id;
	private String name;
	private String comment;

	private UmlPackage xmiPackage;
	private final Map<String,UmlAttribute> attributs = new LinkedHashMap<>();
	private final List<UmlAssociation> associations = new ArrayList<>();

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
		id = eaId;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		// On modifie le nom
		this.name = name;
	}


	public void addAttribute(UmlAttribute attribute) {
		attributs.put(attribute.getId(), attribute);
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
		xmiPackage = pack;
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
		return new ArrayList<>(attributs.values());
	}

	/**
	 * Récupération d'un attribut par son id unique
	 * @param id
	 * @return
	 */
	public UmlAttribute getAttributeById(String id) {
		return attributs.get(id);
	}

	/**
	 * Renvoie la liste des attributs PKs
	 * @return liste des attributs PKs
	 */
	public List<UmlAttribute> getPKs(){
		List<UmlAttribute> pks = new ArrayList<>();
		for(UmlAttribute attr : attributs.values()) {
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\t. ").append(name).append(" : ");

		for(UmlAttribute attribut : attributs.values()) {
			sb.append("\n\t\t- ").append(attribut);
		}

		for(UmlAssociation association : associations) {
			sb.append("\n\t\t> ").append(association);
		}

		return sb.toString();
	}


}
