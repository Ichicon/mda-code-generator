package mda.generator.converters.java;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import mda.generator.beans.UmlAssociation;

/**
 * Convert names in diagram to name in java classes.
 * If contains a "_" convert to camel case. Just capitalize otherwise.
 *
 * @author Fabien Crapart
 *
 */
public class JavaSnakeToCamelNameConverter implements JavaNameConverterInterface {
	/**
	 * Convert a classe Name extracted from the diagram to a java class name for the final classes.
	 * @param className Class name in diagram.
	 * @return Class name in final classes.
	 */
	@Override
	public String convertClassName(String className) {
		return computeCamelCaseName(className);
	}

	/**
	 * Convert an attribute name from the diagram to a java attribute name in the final classes.
	 * @param attributeName Name in diagram.
	 * @return Attribut name in java classes.
	 */
	@Override
	public String convertAttributeName(String attributeName) {
		return StringUtils.uncapitalize(computeCamelCaseName(attributeName));
	}

	/**
	 * Convert FK name from role in association or, if not defined, pointed classe name in camel case.
	 * @param association
	 * @return name of fk attribute
	 */
	@Override
	public String computeFkAttributeName(UmlAssociation association){
		String roleName = association.getFkObjectName();

		// Compute name from pointed class if role not defined
		if(StringUtils.isEmpty(roleName)) {
			roleName = StringUtils.uncapitalize(convertClassName(association.getTarget().getName()));
		}

		// Adding List at the end if association is multiple
		if(association.isTargetMultiple()) {
			roleName += "List";
		}

		return roleName;
	}

	/** Convert a "snake case" name in "camel case name".
	 * If there is no "_" in the original name, we just capitalize.
	 *
	 * @param originalName Orignal name to convert to camelCase
	 * @return name in camel case.
	 */
	private static String computeCamelCaseName(String originalName) {
		// If null or empty, no process to do
		if(StringUtils.isEmpty(originalName)) {
			return originalName;
		}

		// If contains at least one "_", we convert it from "snake case" to "camel case"
		if(originalName.indexOf('_') != -1){
			return StringUtils.remove(WordUtils.capitalizeFully(originalName, '_'), "_");
		}

		// No underscore, we juste capitalize
		return StringUtils.capitalize(originalName);
	}
}
