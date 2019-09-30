package mda.generator.converters.java;

import mda.generator.beans.UmlAssociation;

/**
 * Convert names in diagram to name in java classes.
 *
 * @author Fabien Crapart
 *
 */
public interface JavaNameConverterInterface {
	/**
	 * Convert a classe Name extracted from the diagram to a java class name for the final classes.
	 * @param className Class name in diagram.
	 * @return Class name in final classes.
	 */
	String convertClassName(String className);

	/**
	 * Convert an attribute name from the diagram to a java attribute name in the final classes.
	 * @param attributeName Name in diagram.
	 * @return Attribut name in java classes.
	 */
	String convertAttributeName(String attributeName);

	/**
	 * Conpute the name of pointed objet in FK
	 * @param association UmlAssociation defining the fk
	 * @return Fk object name
	 */
	String computeFkAttributeName(UmlAssociation association);
}
