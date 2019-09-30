package mda.generator.converters.type;

import mda.generator.beans.UmlDomain;

/**
 * Interface to convert type from diagram to JAVA Types and SQL types.
 *
 * @author Fabien Crapart
 */
public interface TypeConverterInterface {
	/**
	 * Fournit un nom de type java pour un nom de domaine
	 * @param domain domaine
	 * @return Nom  de la classe java correspondante
	 */
	public String getJavaType(UmlDomain domain);

	/**
	 * Fournit un nom de type bdd pour un nom de domaine
	 * @param domain  domaine
	 * @return nom du type bdd correspondant
	 */
	public String getDataBaseType(UmlDomain domain);
}
