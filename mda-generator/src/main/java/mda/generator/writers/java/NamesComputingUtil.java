package mda.generator.writers.java;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import mda.generator.beans.UmlAssociation;
import mda.generator.beans.UmlAttribute;
import mda.generator.beans.UmlClass;
import mda.generator.exceptions.MdaGeneratorException;

/**
 * Util to compute FK and PK anmes
 * @author Fabien Crapart
 *
 */
public class NamesComputingUtil {
	/**
	 * Calcul le nom de l'objet dans un relation
	 * @param association
	 * @return nom calculé
	 */	
	public static String computeFkObjectName(UmlAssociation association) {
		String roleName = association.getFkObjectName();
		
		if(StringUtils.isEmpty(roleName)) {
			roleName = StringUtils.uncapitalize(association.getTarget().getCamelCaseName());
		}
		
		return roleName;
	}

	/**
	 * Compute final name for FK, use name defined in association as a priority or pk name instead
	 * @param umlAssociation
	 * @return
	 */
	public static String computeFKName(UmlAssociation umlAssociation) {
		String fkName = umlAssociation.getFkName();
		
		if(StringUtils.isEmpty(fkName)) {
			fkName = computePKName(umlAssociation.getTarget());
		}
		
		return fkName;
	}

	/**
	 * 
	 * @param umlClass
	 * @return
	 */
	public static String computePKName(UmlClass umlClass) {
		String pkName;
		List<UmlAttribute> pks = umlClass.getPKs();
		if(pks.isEmpty()) {
			throw new MdaGeneratorException("Cannot find a primary key for class " + umlClass.getName());
		} else if(pks.size()==1) {
			pkName = pks.get(0).getName();
		} else {
			// FIXME gérer cas pk multiple
			return "compositeKeyNotImplemented";
		}

		return pkName;		
	}

}
