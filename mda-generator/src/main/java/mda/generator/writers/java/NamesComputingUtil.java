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
	public static String SEQUENCE_PREFIX ="SEQ_";
	
	/**
	 * Compute sequence name for a table with unique PK
	 * @param umlClass
	 * @return Sequence name
	 */
	public static String computeSequenceName(UmlClass umlClass) {
		return SEQUENCE_PREFIX + umlClass.getName().toUpperCase();
	}
	
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
			return StringUtils.uncapitalize(umlClass.getName()+"Id");
		}

		return pkName;		
	}
	
	public static String computeFKValue(UmlAssociation umlAssociation) {
		String fkValue = null;
		
		// We can use fk name defined in model if it's a single pk
		if(umlAssociation.getTarget().getPKs().size() == 1) {
			fkValue = umlAssociation.getFkName();		
		}	
		
		// If no fk name defined or multiple pk, we use pk names from target table
		if(StringUtils.isEmpty(fkValue)) {
			fkValue = computePKValue(umlAssociation.getTarget());
		}

		return fkValue;
	}

	
	public static String computePKValue(UmlClass umlClass) {
		StringBuilder pkValue = new StringBuilder();
		for(UmlAttribute pk : umlClass.getPKs()) {
			if(pkValue.length() > 0) {
				pkValue.append(", ");
			}
			pkValue.append(pk.getName());
		}
		
		return pkValue.toString();
	}

}
