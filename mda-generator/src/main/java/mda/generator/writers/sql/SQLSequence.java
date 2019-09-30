package mda.generator.writers.sql;

import mda.generator.beans.UmlClass;
import mda.generator.writers.NamesComputingUtil;


/**
 * Class which represents an SQL sequence. 
 * 
 * @author Fabien Crapart
 *
 */
public class SQLSequence {
	private final String name;
	
	public SQLSequence(UmlClass umlClass) {
		name = NamesComputingUtil.computeSequenceName(umlClass);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	
}
