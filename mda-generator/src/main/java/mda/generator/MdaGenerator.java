package mda.generator;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mda.generator.beans.UmlDomain;
import mda.generator.beans.UmlPackage;
import mda.generator.converters.ConverterInterface;
import mda.generator.exceptions.MdaGeneratorException;
import mda.generator.readers.ModelFileReaderInterface;
import mda.generator.writers.java.JavaWriterConfig;
import mda.generator.writers.java.JavaWriterInterface;
import mda.generator.writers.sql.SQLWriterConfig;
import mda.generator.writers.sql.SQLWriterInterface;

/**
 * Java classes and SQL generator from model file
 * 
 * With standard JavaWriter add a comment line containing // NO GENERATION to prevent class from being erased.
 * FIXME implements composite pk
 * TODO Get attributes default value from model
 * TODO Add a properties file to configure
 * 
 * @author Fabien Crapart
 *
 */
public class MdaGenerator {
	private static final Logger LOG = LogManager.getLogger(MdaGenerator.class);

	/** Input models and reader */
	private Path pathToModelFile;
	private Path pathToMetadataFile;
	private Class<? extends ModelFileReaderInterface> readerClass;
	
	private Class<? extends ConverterInterface> converterClass;	
	
	/** Java generation */
	private Path javaOutputDirectory = null;
	private Class<? extends JavaWriterInterface> javaWriterClass;	
	private Path pathToPackageInfoTemplate;
	private Path pathToEntitiesTemplate;
	private Path pathToDaosTemplate;
	/** Nom dans les packages qui correspond à l'emplacement des entities, permet de conserver un path équivalent pour les daos */
	private String entitiesPackagePartName = "entities";
	private String daosPackagePartName = "daos";

	/** Emplacement du fichier SQL généré en sortie */
	private Path sqlOutputDirectory = null;
	private Class<? extends SQLWriterInterface> sqlWriterClass;	
	private Path pathToCreateSQLTemplate;
	private Path pathToDropSQLTemplate;
	private List<String> excludedPrefixes;
	
	/** Charset */
	private Charset charset;
	
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
		reader.extractObjects(pathToModelFile.toString(), pathToMetadataFile.toString());

		// Logs de ce qui a été extrait
		StringBuilder sbUmlObjects = new StringBuilder();

		sbUmlObjects.append("\n\nDOMAINS (JAVA / DATABASE):\n-------------------------------------");
		for(UmlDomain umlDomain : reader.getDomainsMap().values()) {
			sbUmlObjects.append(umlDomain);
			sbUmlObjects.append(" ").append(converter.getJavaType(umlDomain));
			sbUmlObjects.append(" / ").append(converter.getDataBaseType(umlDomain));
		}

		sbUmlObjects.append("\n\nPACKAGES & CLASSES :\n-------------------------------------");		
		for(UmlPackage xmiPackage : reader.getPackagesMap().values()) {
			sbUmlObjects.append(xmiPackage);
		}

		LOG.info(sbUmlObjects);

		// Generation du code java	
		JavaWriterConfig javaConfig = new JavaWriterConfig();
		javaConfig.setJavaOutputDirectory(javaOutputDirectory);
		javaConfig.setUmlPackages(reader.getPackagesMap().values());
		javaConfig.setConverter(converter);
		javaConfig.setEntities(entitiesPackagePartName);
		javaConfig.setDaos(daosPackagePartName);
		javaConfig.setPathToPackageInfoTemplate(pathToPackageInfoTemplate);
		javaConfig.setPathToEntitiesTemplate(pathToEntitiesTemplate);
		javaConfig.setPathToDaosTemplate(pathToDaosTemplate);
		javaConfig.setCharset(charset);		
		javaWriter.writeSourceCode(javaConfig);
		
		// Generation du sql
		SQLWriterConfig sqlConfig = new SQLWriterConfig();
		sqlConfig.setPackagesList(reader.getPackagesMap().values());
		sqlConfig.setSqlOutputDirectory(sqlOutputDirectory);
		sqlConfig.setCreateSqlTemplatePath(pathToCreateSQLTemplate);
		sqlConfig.setDropSqlTemplatePath(pathToDropSQLTemplate);
		sqlConfig.setConverter(converter);
		sqlConfig.setCharset(charset);
		sqlConfig.setExcludesClassesPrefixes(excludedPrefixes);

		sqlWriter.writeSql(sqlConfig);
	}

	
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
	 * @param pathToMetadataFile the pathToMetadataFile to set
	 */
	public void setPathToMetadataFile(Path pathToMetadataFile) {
		this.pathToMetadataFile = pathToMetadataFile;
	}

	/**
	 * @param pathToEntitiesTemplate the pathToEntitiesTemplate to set
	 */
	public void setPathToEntitiesTemplate(Path pathToEntitiesTemplate) {
		this.pathToEntitiesTemplate = pathToEntitiesTemplate;
	}

	/**
	 * @param pathToDaosTemplate the pathToDaosTemplate to set
	 */
	public void setPathToDaosTemplate(Path pathToDaosTemplate) {
		this.pathToDaosTemplate = pathToDaosTemplate;
	}
	
	/**
	 * @param pathToCreateSQLTemplate the pathToCreateSQLTemplate to set
	 */
	public void setPathToCreateSQLTemplate(Path pathToCreateSQLTemplate) {
		this.pathToCreateSQLTemplate = pathToCreateSQLTemplate;
	}
	
	/**
	 * @param pathToPackageInfoTemplate the pathToPacakgeInfoTemplate to set
	 */
	public void setPathToPackageInfoTemplate(Path pathToPackageInfoTemplate) {
		this.pathToPackageInfoTemplate = pathToPackageInfoTemplate;
	}

	/**
	 * @param pathToDropSQLTemplate the pathToDropSQLTemplate to set
	 */
	public void setPathToDropSQLTemplate(Path pathToDropSQLTemplate) {
		this.pathToDropSQLTemplate = pathToDropSQLTemplate;
	}

	/**
	 * @param charset the charset to set
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}
	
	/**
	 * @param excludedPrefixes the excludedPrefixes to set
	 */
	public void setExcludedPrefixes(List<String> excludedPrefixes) {
		this.excludedPrefixes = excludedPrefixes;
	}


	private void logConfiguration() {
		StringBuilder msgConfig = new StringBuilder();
		msgConfig.append("\nMDA GENERATOR CONFIGURATION :");
		msgConfig.append("\n - Model file ").append(pathToModelFile).append(" will be parsed with ").append(readerClass.getName());
		msgConfig.append("\n - Metadata file ").append(pathToMetadataFile).append(" will be parsed with ").append(readerClass.getName());
		msgConfig.append("\n - Domains types will be converted with ").append(converterClass.getName());
		msgConfig.append("\n - JAVA sources will be written with "+ javaWriterClass.getName() +" in ").append(javaOutputDirectory)
			.append( " with '").append( entitiesPackagePartName ).append( "' as entities package part and '" )
			.append( daosPackagePartName ).append( "' as daos package part");
		msgConfig.append("\n - CREATE SQL will be written with '" + sqlWriterClass.getName() +"' and '" + pathToCreateSQLTemplate + "' template in " + sqlOutputDirectory);	
		msgConfig.append("\n - DROP SQL will be written with '" + sqlWriterClass.getName() +"' and '" + pathToDropSQLTemplate + "' template in " + sqlOutputDirectory);	
		msgConfig.append("\n\n");

		LOG.info(msgConfig.toString());
	}
}
