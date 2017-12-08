package mda.generator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mda.generator.beans.UmlDomain;
import mda.generator.beans.UmlPackage;
import mda.generator.converters.DomainToTypeConverter;
import mda.generator.exceptions.MdaGeneratorException;
import mda.generator.readers.ModelFileReader;

public class MdaGenerator {
	private static final Logger LOG = LogManager.getLogger(MdaGenerator.class);
	
	private String pathToModelFile;
	private Class<? extends ModelFileReader> readerClass;
	private Class<? extends DomainToTypeConverter> converterClass;	
	
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
	 * @param converterClass the converterClass to set
	 */
	public void setConverterClass(Class<? extends DomainToTypeConverter> converterClass) {
		this.converterClass = converterClass;
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
		
		// instanciation du converter
		DomainToTypeConverter converter;
		try {
			converter = converterClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOG.error("Impossible d'instancier le converter" , e);		
			throw new MdaGeneratorException("Impossible d'instancier le converter", e);
		}
		
		// Logs de ce qui a été extrait
		StringBuilder sbXmiObjects = new StringBuilder();
		
		sbXmiObjects.append("\n\nDOMAINS (JAVA / DATABASE):\n-------------------------------------");
		for(UmlDomain xmiDomain : reader.getDomainsMap().values()) {
			sbXmiObjects.append(xmiDomain);
			sbXmiObjects.append(" ").append(converter.getJavaType(xmiDomain.getName()));
			sbXmiObjects.append(" / ").append(converter.getDataBaseType(xmiDomain.getName()));
		}
		
		sbXmiObjects.append("\n\nPACKAGES & CLASSES :\n-------------------------------------");		
		for(UmlPackage xmiPackage : reader.getPackagesMap().values()) {
			sbXmiObjects.append(xmiPackage);
		}

		LOG.info(sbXmiObjects);
		
		// Generation 
		
		
	}
	
}
