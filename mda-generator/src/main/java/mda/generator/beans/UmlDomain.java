package mda.generator.beans;

/**
 * Domain representing a type in the model file
 * 
 * @author Fabien Crapart
 */
public class UmlDomain {
	private String name;
	private int minLength;
	private int maxLength;
	private int precision;
	
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
	 * @return the minLength
	 */
	public int getMinLength() {
		return minLength;
	}
	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
	/**
	 * @return the maxLength
	 */
	public int getMaxLength() {
		return maxLength;
	}
	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	/**
	 * @return the precision
	 */
	public int getPrecision() {
		return precision;
	}
	/**
	 * @param precision the precision to set
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public String toString() {
		return "\n\t" + name;
	}
}
