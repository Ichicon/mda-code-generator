package mda.generator.writers.java;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import mda.generator.MdaGeneratorBuilder;
import mda.generator.beans.UmlPackage;
import mda.generator.exceptions.MdaGeneratorException;

/**
 * Class to write java packages and classes from Uml objects
 * 
 * @author Fabien Crapart
 */
public class JavaWriter implements JavaWriterInterface {
	/** Logger */
	private static final Logger LOG = LogManager.getLogger();
	
	/** Comment to use in the file to not regenerate it at all */
	public static String ONE_TIME_GENERATION ="// NO GENERATION";

	/** Constant use to mark the end of generated code in a file, the rest of file can be edited without being erased */
	public static String END_OF_GENERATED ="// END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE";

	/** Sequence prefix */
	public static String SEQUENCE_PREFIX = "SEQ_";	
	
	/** Line break */
	public static String BREAK = "\n";
	
	/** Commment to add to specify that the file has been generated */
	public static String GENERATED_COMMENT = "This file has been automatically generated.";
	/** Comment to insert when no comment found in diagram */
	public static String NO_COMMENT_FOUND = "No comment found in model diagram";

	/** Writer config */
	private JavaWriterConfig config;

	@Override
	public void initWriterConfig(JavaWriterConfig config) {
		this.config = config;
	}

	@Override
	public void writeSourceCode() {		
		if(config == null) {
			throw new MdaGeneratorException("No config initialized for java writer,use initWriterConfig before calling writeSourceCode");
		}

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
	 * 
	 * @param srcRoot
	 * @param umlPackage
	 */
	protected void createPackage(Path srcRoot, UmlPackage umlPackage) {
		JavaPackage javaPackage = new JavaPackage(srcRoot, umlPackage, config.getConverter());

		// Create package directory
		try {
			Files.createDirectories(javaPackage.getPackagePath());
		} catch (IOException e) {
			throw new MdaGeneratorException("Error while creating directory "  + javaPackage.getPackagePath() + " for package " + javaPackage.getPackageName(), e);
		}

		// Creating package-info (erasing already existing one)
		VelocityContext context = new VelocityContext();
		context.put("javaPackage", javaPackage);
		
		// Writing package-info file
		Path packageInfoPath = javaPackage.getPackagePath().resolve("package-info.java");
		try {
			writeFileFromTemplate(packageInfoPath, config.getPathToPackageInfoTemplate(), context);
		}catch (IOException e) {
			throw new MdaGeneratorException("Error while creating package-info "  + packageInfoPath + " for package " + javaPackage.getPackageName(), e);		
		}

		// Writing classes
		for(JavaClass javaClass : javaPackage.getClasses()) {
			try {
				writeClass(javaClass);
			} catch (IOException e) {
				throw new MdaGeneratorException("Error while creating class "  + javaClass.getClassPath(), e);		
			}
		}
	}
	
	/**
	 * 
	 * @param javaClass
	 * @throws IOException
	 */
	protected void writeClass(JavaClass javaClass) throws IOException {
		// Test if file already existe
		Path classPath = javaClass.getClassPath();
		
		StringBuilder contentToKeep = new StringBuilder();
		boolean doNotRegenerate = false;
		boolean keepContent = false;
		if(Files.exists(classPath)) {
			for(String line : Files.readAllLines(classPath)) {
				// No generation for this one
				if(line.contains(ONE_TIME_GENERATION)) {
					doNotRegenerate =true;
					break;
				}
				// We keep user edited content
				if(keepContent) {
					if(contentToKeep.length()>0) {
						contentToKeep.append(BREAK);
					}
					contentToKeep.append(line);
				}	
				
				// Comment to detect user content (after this line)
				if(line.contains(END_OF_GENERATED)) {
					keepContent = true;
				}		
			}
		}
		
		// Class generation only if allowed
		if(!doNotRegenerate) {
			writeClassContentWithTemplate(classPath, javaClass, keepContent, contentToKeep);
			//writeClassContent(classPath, javaClass, keepContent, oldContent, contentToKeep);			
		} else {
			LOG.debug(classPath + " will not be overwritten because '" + ONE_TIME_GENERATION + "' is present");
		}
	}
	
	protected void writeClassContentWithTemplate(Path classPath, JavaClass javaClass, boolean keepContent, StringBuilder contentToKeep) throws IOException {
		VelocityContext context = new VelocityContext();

		context.put( "javaClass", javaClass);
		context.put( "keep_content", keepContent);
		context.put( "content_to_keep", contentToKeep);
		context.put( "generated_comment", GENERATED_COMMENT);
		context.put( "end_of_generated", END_OF_GENERATED);
	
		writeFileFromTemplate(classPath,  config.getPathToEntitiesTemplate(), context);
	}

	protected void writeFileFromTemplate(Path filePath, Path templatePath, VelocityContext context ) throws IOException {
		Properties prop = new Properties();
		prop.setProperty("file.resource.loader.path", templatePath.getParent().toString());
		Velocity.init(prop);
		
		Template template = null;
		try{
			template = Velocity.getTemplate(templatePath.getFileName().toString());
		}catch( Exception e ){ 
			throw new MdaGeneratorException("Error while writing from template " + templatePath,e);
		}
		StringWriter sw = new StringWriter();
		template.merge(context,sw);		
		LOG.debug("Creating " + filePath);				
		Files.write(filePath, sw.toString().getBytes(config.getCharset()));	
	}
}
