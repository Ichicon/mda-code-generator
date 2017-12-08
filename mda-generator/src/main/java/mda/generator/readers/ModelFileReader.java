package mda.generator.readers;

import java.util.Map;

import mda.generator.beans.UmlAssociation;
import mda.generator.beans.UmlClass;
import mda.generator.beans.UmlDomain;
import mda.generator.beans.UmlPackage;

public interface ModelFileReader {
	void extractObjects(String pathToModelFile);
	
	Map<String, UmlDomain> getDomainsMap();	
	Map<String, UmlPackage> getPackagesMap();	
	Map<String, UmlClass> getClassesMap();
	Map<String, UmlAssociation> getAssociationsMap();
}
