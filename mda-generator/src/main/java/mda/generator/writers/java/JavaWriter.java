package mda.generator.writers.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.VelocityContext;

import mda.generator.beans.UmlPackage;
import mda.generator.exceptions.MdaGeneratorException;
import mda.generator.writers.VelocityUtils;
import mda.generator.writers.java.codepart.JavaClass;
import mda.generator.writers.java.codepart.JavaPackage;

/**
 * Class to write java packages and classes from Uml objects.
 *
 * @author Fabien Crapart
 */
public class JavaWriter implements JavaWriterInterface {
	/** Logger */
	private static final Logger LOG = LogManager.getLogger(JavaWriter.class);

	/** Comment to use in the file to not regenerate it at all */
	public static String STOP_GENERATION ="// STOP GENERATION";

	/** Constant use to mark the end of generated code in a file, the rest of file can be edited without being erased */
	public static String END_OF_GENERATED ="// END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE";

	/** Line break */
	public static String BREAK = "\n";

	/** Comment to insert when no comment found in diagram */
	public static String NO_COMMENT_FOUND = "No comment found in model diagram";

	/** Writer config */
	private JavaWriterConfig config;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeSourceCode(JavaWriterConfig config) {
		if(config == null) {
			throw new MdaGeneratorException("Null config given for java writer");
		}

		this.config = config;

		// Create the root directory for sources if doesn't exists
		try {
			Files.createDirectories(config.getJavaOutputDirectory());
		} catch (IOException e) {
			throw new MdaGeneratorException("Error while creating source root path", e);
		}

		// Iterate to create packages and classes inside
		for(UmlPackage umlPackage : config.getUmlPackages()) {
			createPackage(config.getJavaOutputDirectory(), umlPackage);
		}
	}

	/**
	 * Creating java package (which create java classes)
	 * @param srcRoot root path to create java classes
	 * @param umlPackage UML Object with package information
	 */
	protected void createPackage(Path srcRoot, UmlPackage umlPackage) {
		JavaPackage javaPackage = new JavaPackage(srcRoot, umlPackage, config.getConverter(), config.getJavaNameConverter());

		// Writing package-info file for entities
		Path entitiesPackagePath = writeEntitiesPackageInfo(javaPackage);

		// Writing package-info file for daos (replace entities part name by daos part name)
		Path daosPackagePath = writeDaosPackageInfo(javaPackage);

		// Writing classes and daos
		for(JavaClass javaClass : javaPackage.getClasses()) {
			try {
				// Add user defined annotations if provided
				javaClass.setUserDefinedAnnotations(config.getAnnotationsForClasses().get(javaClass.getName()));

				writeClass(entitiesPackagePath, javaClass);
				// Class without pk fields are embeddable => no dao
				if(javaClass.getPkField() != null) {
					writeDao(daosPackagePath, javaClass);
				} else {
					LOG.warn(javaClass.getName() + " is a composite key, it's bad, not sure it will work well.");
				}
			} catch (IOException e) {
				throw new MdaGeneratorException("Error while generating class "  + javaClass.getName(), e);
			}
		}
	}


	/**
	 * Creation of package path for entities and the package info file inside.
	 *
	 * @param javaPackage Object describing the java package
	 * @return path of the directory representing the package for entities
	 */
	protected Path writeEntitiesPackageInfo(JavaPackage javaPackage) {
		VelocityContext context = new VelocityContext();
		context.put("commentsList", javaPackage.getCommentsList());
		context.put("packageName", javaPackage.getPackageName());

		Path entitiesPackagePath =  javaPackage.getPackagePath();
		Path entitiesPackageInfoPath = entitiesPackagePath.resolve("package-info.java");
		try {
			Files.createDirectories(entitiesPackagePath);
			VelocityUtils.writeFileFromTemplate(entitiesPackageInfoPath, config.getPathToPackageInfoTemplate(), context, config.getCharset());
		}catch (IOException e) {
			throw new MdaGeneratorException("Error while creating entities package-info "  + entitiesPackageInfoPath + " for package " + javaPackage.getPackageName(), e);
		}

		return entitiesPackagePath;
	}


	/**
	 * Creation of package path for DAOs and the package info file inside.
	 *
	 * @param javaPackage Object describing the java package.
	 * @return path of the directory representing the package for DAOs.
	 */
	protected Path writeDaosPackageInfo(JavaPackage javaPackage) {
		VelocityContext context = new VelocityContext();
		context.put("commentsList", javaPackage.getCommentsList());
		context.put("packageName", replaceEntitiesWithDaos(javaPackage.getPackageName()));

		Path daosPackagePath = replaceEntitiesWithDaos(javaPackage.getPackagePath());
		Path daosPackageInfoPath = daosPackagePath.resolve("package-info.java");
		try {
			Files.createDirectories(daosPackagePath);
			VelocityUtils.writeFileFromTemplate(daosPackageInfoPath, config.getPathToPackageInfoTemplate(), context, config.getCharset());
		}catch (IOException e) {
			throw new MdaGeneratorException("Error while creating daos package-info "  + daosPackageInfoPath + " for package " + javaPackage.getPackageName(), e);
		}

		return daosPackagePath;
	}


	/**
	 * Write a java class into the given package path.
	 * @param packageEntitiesPath Path where to create the java class.
	 * @param javaClass Object with data to create the java class.
	 * @throws IOException Error while writing the java class file.
	 */
	protected void writeClass(Path packageEntitiesPath, JavaClass javaClass) throws IOException {
		// Analyse file and existing content, add values to context to use in template
		Path entityPath = packageEntitiesPath.resolve(javaClass.getName()+".java");
		VelocityContext context = new VelocityContext();
		if(VelocityUtils.analyseFileAndCompleteContext(entityPath, STOP_GENERATION, END_OF_GENERATED, context)) {
			context.put( "javaClass", javaClass);
			context.put( "end_of_generated", END_OF_GENERATED);

			VelocityUtils.writeFileFromTemplate(entityPath,  config.getPathToEntitiesTemplate(), context, config.getCharset());
		} else {
			LOG.debug(entityPath + " will not be overwritten because '" + STOP_GENERATION + "' is present");
		}
	}

	/**
	 * Write the dao file corresponding to the given java class.
	 *
	 * @param packageDaosPath Package associated with the class.
	 * @param javaClass Object with data about the java class for which we generate a DAO.
	 * @throws IOException Error while writing the DAO file.
	 */
	protected void writeDao(Path packageDaosPath, JavaClass javaClass) throws IOException {
		// Test if file already exists
		Path daoPath = packageDaosPath.resolve(javaClass.getName() + "DAO.java");

		// Analyse file and existing content, add values to context to use in template
		VelocityContext context = new VelocityContext();
		if(VelocityUtils.analyseFileAndCompleteContext(daoPath, STOP_GENERATION, END_OF_GENERATED, context)) {
			context.put("daoPackageName", replaceEntitiesWithDaos(javaClass.getPackageName()));
			context.put("javaClass", javaClass);
			context.put("end_of_generated", END_OF_GENERATED);

			VelocityUtils.writeFileFromTemplate(daoPath,  config.getPathToDaosTemplate(), context, config.getCharset());
		} else {
			LOG.debug(daoPath + " will not be overwritten because '" + STOP_GENERATION + "' is present");
		}
	}

	/**
	 * Change in the package name, the "entities" part with "daos" (depending on configurated names).
	 * @param entitiesPackage Actual path for the package with entites
	 * @return path for package with DAOs.
	 */
	private Path replaceEntitiesWithDaos(Path entitiesPackagePath) {
		String nomPackageDAO = replaceEntitiesWithDaos(entitiesPackagePath.toString());
		return Paths.get(StringUtils.replaceChars(nomPackageDAO,'.', '/'));
	}

	/**
	 * Change in the package name, the "entities" part with "daos" (depending on configurated names).
	 * @param entitiesPackage Package name for entities
	 * @return package name for DAOs
	 */
	private String replaceEntitiesWithDaos(String entitiesPackage) {
		return entitiesPackage.replaceAll(config.getEntities(),config.getDaos());
	}
}
