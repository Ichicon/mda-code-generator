package mda.generator;

import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mda.generator.beans.UmlDomain;
import mda.generator.beans.UmlPackage;
import mda.generator.converters.ConverterInterface;
import mda.generator.exceptions.MdaGeneratorException;
import mda.generator.readers.ModelFileReaderInterface;
import mda.generator.writers.java.JavaWriterConfig;
import mda.generator.writers.java.JavaWriterInterface;
import mda.generator.writers.sql.SQLWriterInterface;

/**
 * Java classes and SQL generator from model file
 * 
 * With standard JavaWriter add a comment line containing // NO GENERATION to prevent class from being erased.
 * 
 * @author Fabien Crapart
 *
 */
public class MdaGenerator {
	private static final Logger LOG = LogManager.getLogger(MdaGenerator.class);

	private Path pathToModelFile;
	private Class<? extends ModelFileReaderInterface> readerClass;
	private Class<? extends ConverterInterface> converterClass;	
	private Class<? extends JavaWriterInterface> javaWriterClass;	
	private Class<? extends SQLWriterInterface> sqlWriterClass;	
	
	
	/** Emplacement de sortie des packages et fichiers générés */
	private Path javaOutputDirectory = null;
	/** Emplacement du fichier SQL généré en sortie */
	private Path sqlOutputDirectory = null;

	/** Nom dans les packages qui correspond à l'emplacement des entities, permet de faire le path équivalent pour les daos */
	private String entitiesPackagePartName = "entities";
	private String daosPackagePartName = "daos";

	/**
	 * @param pathToModelFile the pathToModelFile to set
	 */
	public void setPathToModelFile(Path pathToModelFile) {
		this.pathToModelFile = pathToModelFile;
	}

	/**
	 * @param readerClass the readerClass to set
	 */
	public void setReaderClass(Class<? extends ModelFileReaderInterface> readerClass) {
		this.readerClass = readerClass;
	}

	/**
	 * @param converterClass the converterClass to set
	 */
	public void setConverterClass(Class<? extends ConverterInterface> converterClass) {
		this.converterClass = converterClass;
	}

	/**
	 * @param javaWriterClass the javaWriterClass to set
	 */
	public void setJavaWriterClass(Class<? extends JavaWriterInterface> javaWriterClass) {
		this.javaWriterClass = javaWriterClass;
	}

	/**
	 * @param sqlWriterClass the sqlWriterClass to set
	 */
	public void setSqlWriterClass(Class<? extends SQLWriterInterface> sqlWriterClass) {
		this.sqlWriterClass = sqlWriterClass;
	}

	/**
	 * @param javaOutputDirectory the javaOutputDirectory to set
	 */
	public void setJavaOutputDirectory(Path javaOutputDirectory) {
		this.javaOutputDirectory = javaOutputDirectory;
	}

	/**
	 * @param sqlOutputDirectory the sqlOutputDirectory to set
	 */
	public void setSqlOutputDirectory(Path sqlOutputDirectory) {
		this.sqlOutputDirectory = sqlOutputDirectory;
	}

	/**
	 * @param entitiesPackagePartName the entitiesPackagePartName to set
	 */
	public void setEntitiesPackagePartName(String entitiesPackagePartName) {
		this.entitiesPackagePartName = entitiesPackagePartName;
	}

	/**
	 * @param daosPackagePartName the daosPackagePartName to set
	 */
	public void setDaosPackagePartName(String daosPackagePartName) {
		this.daosPackagePartName = daosPackagePartName;
	}

	/**
	 * 
	 * @param pathToModelFile
	 * @param readerClass
	 */
	public void generate() {
		// Instanciation du reader
		ModelFileReaderInterface reader;
		try {
			reader = readerClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOG.error("Impossible d'instancier le reader" , e);		
			throw new MdaGeneratorException("Impossible d'instancier le reader", e);
		}
		
		// Instanciation du converter
		ConverterInterface converter;
		try {
			converter = converterClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOG.error("Impossible d'instancier le converter" , e);		
			throw new MdaGeneratorException("Impossible d'instancier le converter", e);
		}
		
		// Instanciation du java writer
		JavaWriterInterface javaWriter;
		try {
			javaWriter = javaWriterClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOG.error("Impossible d'instancier le java writer" , e);		
			throw new MdaGeneratorException("Impossible d'instancier le java writer", e);
		}
		
		// Instanciation du sql writer
		SQLWriterInterface sqlWriter;
		try {
			sqlWriter = sqlWriterClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOG.error("Impossible d'instancier le sql writer" , e);		
			throw new MdaGeneratorException("Impossible d'instancier le sql writer", e);
		}
		
		
		// Display configuration in logs
		logConfiguration();
		
		// Lecture du xmi
		reader.extractObjects(pathToModelFile.toString());

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

		// Generation du code java	
		JavaWriterConfig javaConfig = new JavaWriterConfig();
		javaConfig.setJavaOutputDirectory(javaOutputDirectory);
		javaConfig.setUmlPackages(reader.getPackagesMap().values());
		javaConfig.setConverter(converter);
		javaConfig.setEntities(entitiesPackagePartName);
		javaConfig.setDaos(daosPackagePartName);
		
		javaWriter.initWriterConfig(javaConfig);
		javaWriter.writeSourceCode();

		
		// Generation du sql
		sqlWriter.writeSql();
	}

	private void logConfiguration() {
		StringBuilder msgConfig = new StringBuilder();
		msgConfig.append("\nMDA GENERATOR CONFIGURATION :");
		msgConfig.append("\n - Input file ").append(pathToModelFile).append(" will be parsed with ").append(readerClass.getName());
		msgConfig.append("\n - Domains types will be converted with ").append(converterClass.getName());
		msgConfig.append("\n - JAVA sources will be written with "+ javaWriterClass.getName() +" in ").append(javaOutputDirectory)
			.append( " with '").append( entitiesPackagePartName ).append( "' as entities package part and '" )
			.append( daosPackagePartName ).append( "' as daos package part");
		msgConfig.append("\n - SQL files will be written with " + sqlWriterClass.getName()  +" in " + sqlOutputDirectory);	
		msgConfig.append("\n\n");

		LOG.info(msgConfig.toString());
	}
}
