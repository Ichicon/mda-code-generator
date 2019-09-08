package mda.generator.writers.sql;

import mda.generator.beans.UmlAttribute;
import mda.generator.beans.UmlDomain;
import mda.generator.converters.ConverterInterface;

/**
 * SQl column in sql table for sql generation.
 * 
 * @author Fabien Crapart
 */
public class SQLColumn {
	private final String name;
	private final String type;
	private final boolean isNotNull;
	private final String comment;
	
	public SQLColumn(UmlAttribute umlAttribute, ConverterInterface converter) {
		this.name = umlAttribute.getName();
		this.type = converter.getDataBaseType(umlAttribute.getDomain());
		this.isNotNull = umlAttribute.getIsNotNull();
		this.comment = umlAttribute.getComment();
	}
	
	public SQLColumn(String name, UmlDomain domain, boolean isNotNull,String comment, ConverterInterface converter) {
		this.name = name;
		this.type = converter.getDataBaseType(domain);
		this.isNotNull = isNotNull;
		this.comment = comment;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the isNotNull
	 */
	public boolean isNotNull() {
		return isNotNull;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	
	
}
