package mda.generator.beans;

import mda.generator.exceptions.MdaGeneratorException;

/**
 * Domain representing a type in the model file
 * 
 * @author Fabien Crapart
 */
public class UmlDomain {
	private String name;
	private String typeName;
	private String maxLength;
	private String precision;
	
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
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}
	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		if(typeName == null) {
			throw new MdaGeneratorException("GenericType undefined for domain " + name);
		}
		this.typeName = typeName;
	}
	/**
	 * @return the maxLength
	 */
	public String getMaxLength() {
		return maxLength;
	}
	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}
	/**
	 * @return the precision
	 */
	public String getPrecision() {
		return precision;
	}
	/**
	 * @param precision the precision to set
	 */
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	public String toString() {
		return "\n\t" + name + "(" + typeName + ")" 
			+ (maxLength != null && !maxLength.equals("0")?" maxLength="+maxLength:""
			+ (precision != null && !precision.equals("0")?" precision="+precision:"")
		);
	}
}
