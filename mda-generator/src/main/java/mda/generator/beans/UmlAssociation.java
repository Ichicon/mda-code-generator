package mda.generator.beans;

/**
 * Correspond à connector dans le XMI /!\
 * @author Fabien
 *
 */
public class UmlAssociation {
	private String id;
	private String name;
		
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
	
	public String toString() {
		return name;
	}
	
	
}
