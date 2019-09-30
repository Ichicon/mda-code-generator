package mda.generator.writers.sql;

import mda.generator.beans.UmlAttribute;
import mda.generator.beans.UmlDomain;
import mda.generator.converters.type.TypeConverterInterface;

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

	public SQLColumn(UmlAttribute umlAttribute, TypeConverterInterface converter) {
		name = umlAttribute.getName();
		type = converter.getDataBaseType(umlAttribute.getDomain());
		isNotNull = umlAttribute.getIsNotNull();
		comment = umlAttribute.getComment();
	}

	public SQLColumn(String name, UmlDomain domain, boolean isNotNull,String comment, TypeConverterInterface converter) {
		this.name = name;
		type = converter.getDataBaseType(domain);
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
