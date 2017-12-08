package mda.generator.beans;

public class UmlDomain {
	private String name;
	
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
		return "\n\t" + name;
	}
}
