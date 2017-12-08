package mda.generator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.common.reflection.java.JavaReflectionManager;

import mda.generator.beans.UmlDomain;
import mda.generator.beans.UmlPackage;
import mda.generator.exceptions.MdaGeneratorException;
import mda.generator.readers.ModelFileReader;
import mda.generator.readers.XmiReader;

public class MdaGenerator {
	private static final Logger LOG = LogManager.getLogger(MdaGenerator.class);
	
	private String pathToModelFile;
	private Class<? extends ModelFileReader> readerClass;
	
	
	/**
	 * @param pathToModelFile the pathToModelFile to set
	 */
	public void setPathToModelFile(String pathToModelFile) {
		this.pathToModelFile = pathToModelFile;
	}

	/**
	 * @param readerClass the readerClass to set
	 */
	public void setReaderClass(Class<? extends ModelFileReader> readerClass) {
		this.readerClass = readerClass;
	}


	/**
	 * 
	 * @param pathToModelFile
	 * @param readerClass
	 */
	public void generate() {
		ModelFileReader reader;
		try {
			reader = readerClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOG.error("Impossible d'instancier le reader" , e);		
			throw new MdaGeneratorException("Impossible d'instancier le reader", e);
		}
		
		// Lecture du xmi
		reader.extractObjects(pathToModelFile);
		
		// Logs de ce qui a été extrait
		StringBuilder sbXmiObjects = new StringBuilder();
		
		sbXmiObjects.append("\n\nDOMAINS :\n-------------------------------------");
		for(UmlDomain xmiDomain : reader.getDomainsMap().values()) {
			sbXmiObjects.append(xmiDomain);
		}
		
		sbXmiObjects.append("\n\nPACKAGES & CLASSES :\n-------------------------------------");		
		for(UmlPackage xmiPackage : reader.getPackagesMap().values()) {
			sbXmiObjects.append(xmiPackage);
		}
		
		sbXmiObjects.append("\n\nAssociations :\n-------------------------------------");		
		
		LOG.info(sbXmiObjects);
		// Generation 
	}
	
}
