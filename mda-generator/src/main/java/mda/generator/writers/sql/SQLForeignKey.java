package mda.generator.writers.sql;

import mda.generator.beans.UmlAssociation;
import mda.generator.writers.java.NamesComputingUtil;

/**
 * 
 * @author Fabien Crapart
 *
 */
public class SQLForeignKey {
	private final String name;
	private final String sourceName;
	private final String targetName;
	
	private final String fkValue;
	private final String pkValue;

	/**
	 * 
	 * @param umlAssociation
	 */
	public SQLForeignKey(UmlAssociation umlAssociation) {
		name = umlAssociation.getName();
		sourceName = umlAssociation.getSource().getName();
		targetName = umlAssociation.getTarget().getName();
				
		fkValue = NamesComputingUtil.computeFKValue(umlAssociation);
		pkValue = NamesComputingUtil.computePKValue(umlAssociation.getTarget());
	}
	
	/**
	 * 
	 * @param name
	 * @param sourceName
	 * @param targetName
	 * @param fkValue
	 * @param pkValue
	 */
	public SQLForeignKey(String name, String sourceName, String targetName, String fkValue, String pkValue) {
		this.name = name;
		this.sourceName = sourceName;
		this.targetName = targetName;
		this.fkValue = fkValue;
		this.pkValue = pkValue;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the sourceName
	 */
	public String getSourceName() {
		return sourceName;
	}

	/**
	 * @return the targetName
	 */
	public String getTargetName() {
		return targetName;
	}

	/**
	 * @return the fkValue
	 */
	public String getFkValue() {
		return fkValue;
	}

	/**
	 * @return the pkValue
	 */
	public String getPkValue() {
		return pkValue;
	}
}
