package mda.generator.readers;

import java.util.Map;

import mda.generator.beans.UmlClass;
import mda.generator.beans.UmlDomain;
import mda.generator.beans.UmlPackage;

/**
 * Interface for the model file Reader. Provides the generator with data to build Packages, Entities and DAO classes.
 * 
 * @author Fabien Crapart
 *
 */
public interface ModelFileReaderInterface {
	/**
	 * Main method to extract data from model file
	 * @param pathToModelFile Path to model file to read
	 * @param pathToModelFile Path to metadata file to read
	 */
	void extractObjects(String pathToModelFile, String pathToMetadataFile);
	
	/**
	 * @return Map by name of domains (type) in the file 
	 */
	Map<String, UmlDomain> getDomainsMap();	
	/**
	 * @return Map by name of packages in the file 
	 */
	Map<String, UmlPackage> getPackagesMap();	
	/**
	 * @return Map by id of classes in the file 
	 */
	Map<String, UmlClass> getClassesMap();
}
