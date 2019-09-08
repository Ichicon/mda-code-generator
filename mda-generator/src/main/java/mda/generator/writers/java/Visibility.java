package mda.generator.writers.java;

/**
 * Java scope of code (class, method, attribute)
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
	 * @return the javaCode for the scope
	 */
	public String toString() {
		return javaCode;
	}	
}
