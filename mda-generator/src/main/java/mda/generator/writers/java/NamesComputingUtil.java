package mda.generator.writers.java;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import mda.generator.beans.UmlAssociation;
import mda.generator.beans.UmlAttribute;
import mda.generator.beans.UmlClass;
import mda.generator.exceptions.MdaGeneratorException;

/**
 * Util to compute FK and PK names.
 * 
 * @author Fabien Crapart
 *
 */
public class NamesComputingUtil {
	/** Default prefix for sql sequence name */
	public static final String DEFAULT_SEQUENCE_PREFIX ="SEQ_";
	/** Actual sql sequence name prefix */
	private static String sequencePrefix = DEFAULT_SEQUENCE_PREFIX;
	
	/** Pattern to find underscore in a string */
	private static Pattern FIND_UNDERSCORE = Pattern.compile("_");
	
	
	/**
	 * Compute sequence name for a table with unique PK
	 * @param umlClass
	 * @return Sequence name
	 */
	public static String computeSequenceName(UmlClass umlClass) {
		return sequencePrefix + umlClass.getName().toUpperCase();
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
	 * Compute final name for FK, use name defined in association as a priority or pk name instead.
	 * 
	 * @param umlAssociation
	 * @return
	 */
	public static String computeJavaFkName(UmlAssociation umlAssociation) {
		String fkName = umlAssociation.getFkName();
		
		if(StringUtils.isEmpty(fkName)) {
			fkName = computeJavaPkName(umlAssociation.getTarget());
		}
		
		return fkName;
	}

	/**
	 * 
	 * @param umlClass
	 * @return
	 */
	public static String computeJavaPkName(UmlClass umlClass) {
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
	
	/**
	 * Compute the FK name for SQL.
	 * 
	 * @param umlAssociation
	 * @return
	 */
	public static String computeFkSqlName(UmlAssociation umlAssociation) {
		String fkValue = null;
		
		// We can use fk name defined in model if it's a single pk
		if(umlAssociation.getTarget().getPKs().size() == 1) {
			fkValue = umlAssociation.getFkName();		
		}	
		
		// If no fk name defined or multiple pk, we use pk names from target table
		if(StringUtils.isEmpty(fkValue)) {
			fkValue = computePkSqlName(umlAssociation.getTarget());
		}

		return fkValue;
	}

	
	/**
	 * Compute PK name for SQL.
	 * 
	 * @param umlClass
	 * @return
	 */
	public static String computePkSqlName(UmlClass umlClass) {
		StringBuilder pkValue = new StringBuilder();
		for(UmlAttribute pk : umlClass.getPKs()) {
			if(pkValue.length() > 0) {
				pkValue.append(", ");
			}
			pkValue.append(pk.getName());
		}
		
		return pkValue.toString();
	}
	
	/**
	 * Change sequence prefix, default prefix is SEQ_
	 * @param newPrefix new value for prefix
	 * @return 
	 */
	public static void changeSequencePrefix(String newPrefix) {
		sequencePrefix = newPrefix;
	}
	
	
	/**
	 * Convert a "snake case" name in "camel case name".
	 * If there is no "_" in the original name, we just capitalize.
	 * 
	 * @param originalName Orignal name to convert to camelCase
	 * @return name in camel case.
	 */
	public static String computeCamelCaseName(String originalName) {
		if(FIND_UNDERSCORE.matcher(originalName).matches()){		
			return StringUtils.remove(WordUtils.capitalizeFully(originalName, '_'), "_");
		}
		
		// No underscore, we juste capitalize
		return StringUtils.capitalize(originalName);
	}
}
