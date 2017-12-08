package mda.generator.beans;

import org.apache.commons.lang3.StringUtils;

/**
 * Association unidirectionnelle entre deux classes, contient une référence vers son opposée.
 * Fournit les informations de nullabilité, multiplicité et navigabilité.
 * 
 * @author Fabien
 *
 */
public class UmlAssociation {
	private String id;
	private String name;
	private boolean owner; // pour les manyToMany on a besoin d'un côté principal
	
	private UmlAssociation opposite;
		
	private boolean targetMultiple;
	private boolean targetNullable;
	private boolean targetNavigable;
	
	 // si pas défini, on utilise le nom de la classe target comme nom
	private String roleName;
	
	private UmlClass source;
	private UmlClass target;
		
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the eaId to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the owner
	 */
	public UmlClass getSource() {
		return source;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setSource(UmlClass owner) {
		this.source = owner;
	}

	/**
	 * @return the target
	 */
	public UmlClass getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(UmlClass target) {
		this.target = target;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		if(StringUtils.isEmpty(roleName)) {
			return target.getCamelCaseName();
		}
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	/**
	 * @return the owner
	 */
	public boolean isOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(boolean owner) {
		this.owner = owner;
	}

	/**
	 * @return association inverse
	 */
	public UmlAssociation getOpposite() {
		return opposite;
	}

	/**
	 * @param opposite the opposite to set
	 */
	public void setOpposite(UmlAssociation opposite) {
		this.opposite = opposite;
	}

	/**
	 * @return the targetIsMultiple
	 */
	public boolean isTargetMultiple() {
		return targetMultiple;
	}

	/**
	 * @param targetIsMultiple the targetIsMultiple to set
	 */
	public void setTargetMultiple(boolean targetIsMultiple) {
		this.targetMultiple = targetIsMultiple;
	}

	/**
	 * @return the targetIsNullable
	 */
	public boolean isTargetNullable() {
		return targetNullable;
	}

	/**
	 * @param targetIsNullable the targetIsNullable to set
	 */
	public void setTargetNullable(boolean targetIsNullable) {
		this.targetNullable = targetIsNullable;
	}

	/**
	 * @return the targetIsNavigable
	 */
	public boolean isTargetNavigable() {
		return targetNavigable;
	}

	/**
	 * @param targetIsNavigable the targetIsNavigable to set
	 */
	public void setTargetNavigable(boolean targetIsNavigable) {
		this.targetNavigable = targetIsNavigable;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		
		sb.append(name).append(" ");
		
		sb.append(" [");
		sb.append(opposite.isTargetNullable()?"0":"1");
		sb.append("..");
		sb.append(opposite.isTargetMultiple()?"*":"1");
		sb.append("]");
		
		sb.append(opposite.isTargetNavigable()?" <":" ");
		sb.append("---");
		sb.append(targetNavigable?"> ":" ");

		sb.append(target.getCamelCaseName());
		if(!StringUtils.isEmpty(roleName)) {
			sb.append(" alias " + roleName);
		}
		
		sb.append(" [");
		sb.append(targetNullable?"0":"1");
		sb.append("..");
		sb.append(targetMultiple?"*":"1");
		sb.append("]");

		return sb.toString();
	}
}
