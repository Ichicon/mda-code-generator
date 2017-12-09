package mda.generator.writers.java;

/**
 * Visibilité d'un élément de code (class, méthode, attribut)
 * @author Fabien
 *
 */
public enum Visibility {
	PUBLIC("public"),
	PRIVATE("private"),
	PROTECTED("protected"),
	PACKAGE("");
	
	private final String javaCode;
	private Visibility(String javaCode) {
		this.javaCode = javaCode;
	}
	/**
	 * @return the javaCode
	 */
	public String getJavaCode() {
		return javaCode;
	}	
}
