package mda.generator.converters;

import mda.generator.beans.UmlDomain;

/**
 * Interface pour spécifier une classe qui fourni le noms des types java  et bdd pour chaque nom de domaine
 * @author Fabien
 *
 */
public interface ConverterInterface {
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
