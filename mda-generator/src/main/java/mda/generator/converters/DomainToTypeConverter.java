package mda.generator.converters;

import mda.generator.beans.UmlDomain;

/**
 * Interface pour spécifier une classe qui fourni le noms des types java  et bdd pour chaque nom de domaine
 * @author Fabien
 *
 */
public interface DomainToTypeConverter {
	/**
	 * Fournit un nom de type java pour un nom de domaine
	 * @param domainName nom du domaine
	 * @return Nom  de la classe java correspondante
	 */
	public String getJavaType(String domainName);
	
	/**
	 * Fournit un nom de type bdd pour un nom de domaine
	 * @param domainName  nom du domaine
	 * @return nom du type bdd correspondant
	 */
	public String getDataBaseType(String domainName);
}
