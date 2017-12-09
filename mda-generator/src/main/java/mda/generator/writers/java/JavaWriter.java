package mda.generator.writers.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mda.generator.beans.UmlPackage;
import mda.generator.exceptions.MdaGeneratorException;

/**
 * Class to write java packages and classes
 * 
 * @author Fabien Crapart
 *
 */
public class JavaWriter implements JavaWriterInterface {
	/** Logger */
	private static final Logger LOG = LogManager.getLogger();
	
	/** Comment to use in the file to not regenerate it at all */
	public static String ONE_TIME_GENERATION ="// NO GENERATION";

	/** Constant use to mark the end of generated code in a file, the rest of file can be edited without being erased */
	public static String END_OF_GENERATED ="// END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE";

	

	// Constants for code
	private static String BREAK = "\n";
	private static String BRACKET_START = "{\n";
	private static String BRACKET_END = "}\n";
	private static String PAKAGE = "package ";
	private static String CLAZZ = " class ";
	private static String END = ";";
	private static String END_BREAK = ";\n";
	
	// Constants for comments 
	private static String START_COMMENT = "/**\n";
	private static String END_COMMENT = " */\n";
	private static String CONTINUE_COMMENT = " * ";
	private static String GENERATED_COMMENT = CONTINUE_COMMENT + "This file has been automatically generated." + BREAK + BREAK;
	
	private static String NO_COMMENT_FOUND = " * No comment found in model diagram \n";

	
	private JavaWriterConfig config;
	
	private int nbClassesIdentic = 0;
	private int nbClassesGenerated =0;

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

		// Iterate dans create packages and classes inside
		for(UmlPackage umlPackage : config.getUmlPackages()) {
			createPackage(config.getJavaOutputDirectory(), umlPackage);
		}
		
		// Log creations
		LOG.info("Created classes: " + nbClassesGenerated);
		LOG.info("Identic classes: " + nbClassesIdentic);
	}

	/**
	 * 
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
		Path packageInfoPath = javaPackage.getPackagePath().resolve("package-info.java");
		LOG.info("Creating " + packageInfoPath);
		StringBuilder content = new StringBuilder();
		content.append(START_COMMENT);
		if(javaPackage.getPackageComment() != null) {
			for(String commentLine : javaPackage.getPackageComment().split(BREAK)) {
				content.append(CONTINUE_COMMENT).append(commentLine).append(BREAK);
			}
		}else {
			content.append(NO_COMMENT_FOUND);
		}
		content.append(END_COMMENT);
		content.append(PAKAGE).append(javaPackage.getPackageName()).append(END);
		try {
			Files.write(packageInfoPath, content.toString().getBytes(config.getCharset()));
		} catch (IOException e) {
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
	
	
	protected void writeClass(JavaClass javaClass) throws IOException {
		// Test if file already existe
		Path classPath = javaClass.getClassPath();
		
		StringBuilder oldContent = new StringBuilder();
		StringBuilder contentToKeep = new StringBuilder();
		boolean doNotRegenerate = false;
		boolean keepContent = false;
		if(Files.exists(classPath)) {
			for(String line : Files.readAllLines(classPath)) {
				oldContent.append(line).append(BREAK);
				// No generation for this one
				if(line.contains(ONE_TIME_GENERATION)) {
					doNotRegenerate =true;
					break;
				}
				// We keep user edited content
				if(keepContent) {
					contentToKeep.append(line).append(BREAK);
				}	
				
				// Comment to detect user content (after this line)
				if(line.contains(END_OF_GENERATED)) {
					keepContent = true;
				}
				
				
			}
		}
		
		// Class generation only if allowed
		if(!doNotRegenerate) {
			writeClassFile(classPath, javaClass, keepContent, oldContent, contentToKeep);			
		} else {
			LOG.debug(classPath + " will not be overwritten because '" + ONE_TIME_GENERATION + "' is present");
		}
	}
	
	private void writeClassFile(Path classPath, JavaClass javaClass, boolean keepContent, StringBuilder oldContent, StringBuilder contentToKeep) throws IOException {
		StringBuilder finalContent = new StringBuilder();
		

		// Package
		finalContent.append(PAKAGE).append(javaClass.getPackageName()).append(END_BREAK).append(BREAK);
		
		// class comments
		finalContent.append(START_COMMENT);
		// Comment for auto generation
		finalContent.append(GENERATED_COMMENT);
		if(javaClass.getComment() == null){
			finalContent.append(NO_COMMENT_FOUND);
		}else {
			for(String commentLine : javaClass.getComment() .split(BREAK)) {
				finalContent.append(CONTINUE_COMMENT).append(commentLine).append(BREAK);
			}
		}
		finalContent.append(END_COMMENT);
		
		
		// Class
		finalContent
			.append(javaClass.getVisibilite().getJavaCode())
			.append(CLAZZ)
			.append(javaClass.getName())
			.append(BRACKET_START);
		
		// Attributes
		
		// Methods
		
		
		// End of generated code
		finalContent.append(BREAK).append(END_OF_GENERATED).append(BREAK);
		
		// Add old content (end class is inside)
		if(keepContent) {
			finalContent.append(contentToKeep);
		} else { // or end class
			finalContent.append(BRACKET_END);
		}
		
		// Ecriture du fichier de classe
		if(oldContent.toString().equals(finalContent.toString())){
			nbClassesIdentic++;
		}else {
			LOG.debug("Creating " + classPath + (keepContent?" with old content inside":""));				
			Files.write(classPath, finalContent.toString().getBytes(config.getCharset()));
			nbClassesGenerated++;
		}
	}

}
