package mda.generator.writers.java.utils;

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
	/** Pcakage name of the class using ImportManager */
	private final String currentPackageName;
	
	/**
	 * Create an import manager with the currentPackageName because we don't need import if the class is in the same package
	 * @param currentPackageName
	 */
	public ImportManager(String currentPackageName) {
		this.currentPackageName = currentPackageName;
	}
	
	/**
	 * Add type to imports if not already present, compute final name for class inside code
	 * @param fullName
	 * @return the name to use (could be full name if name already use)
	 */
	public String getFinalName(String fullName) {
		String simpleName = StringUtils.substringAfterLast(fullName, ".");
		
		// If simple name it means there is no package so return only the class name
		if(StringUtils.isEmpty(simpleName)) {
			return fullName;
		}
		
		// If package is the same return simple name and no need to add import
		String packageName = StringUtils.substringBeforeLast(fullName, ".");
		if(packageName.equals(currentPackageName)) {
			return simpleName;
		}
		
		// Already exists, if it's the same could use simple name otherwise use full name
		String importFullName = imports.get(simpleName);
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
