package mda.generator.writers.java;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Import manager for a class
 * @author Fabien Crapart
 *
 */
public class ImportManager {
	/** Imports name mapped by class simple name */
	private Map<String, String> imports = new HashMap<>();
	
	/**
	 * 
	 * @param fullName
	 * @return the name to use (could be full name if name already use)
	 */
	public String addTypeAndGetFinalName(String fullName) {
		String simpleName = StringUtils.substringAfterLast(fullName, ".");
		if(StringUtils.isEmpty(simpleName)) {
			return fullName;
		}
		
		// Already exists, if it's the same could use simple name otherwise use full name
		String importFullName =imports.get(simpleName);
		if( importFullName != null ) {
			if(importFullName.equals(fullName)) {
				return simpleName;
			} else {
				return fullName;
			}
		}else { // New import to add, use simple name
			imports.put(simpleName, fullName);
			return simpleName;
		}
	}

	/**
	 * All imports values
	 * @return All imports values
	 */
	public Collection<String> getAllImports(){
		return imports.values();
	}
}
