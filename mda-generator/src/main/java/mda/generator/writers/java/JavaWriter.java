package mda.generator.writers.java;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
 * Class to write java packages and classes
 * TODO voir pour utiliser des templates
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

	// Sequence prefix
	public static String SEQUENCE_PREFIX = "SEQ_";
	
	
	// Constants for code
	private static String PAKAGE = "package ";
	private static String END = ";";
	private static String EMPTY = "";
	private static String BREAK = "\n";
	
	// Constants for comments 
	private static String START_COMMENT = "/**\n";
	private static String END_COMMENT = " */\n";
	private static String CONTINUE_COMMENT = " * ";
	private static String GENERATED_COMMENT = "This file has been automatically generated.";
	
	public static String NO_COMMENT_FOUND = "No comment found in model diagram";

	// Writer config
	private JavaWriterConfig config;
	
	// Counters for generation
	private int nbClassesIdentic = 0;
	private int nbClassesGenerated =0;

	@Override
	public void initWriterConfig(JavaWriterConfig config) {
		this.config = config;
	}

	@Override
	public void writeSourceCode() {
		Properties prop = new Properties();
		prop.setProperty("file.resource.loader.path", MdaGeneratorBuilder.getApplicationPath().toString());
		Velocity.init(prop);
		
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
			writeFileFromTemplate(packageInfoPath, "package-info.vm", context);
		}catch (IOException e) {
			throw new MdaGeneratorException("Error while creating package-info "  + packageInfoPath + " for package " + javaPackage.getPackageName(), e);		
		}
		
//		
//		
//		
//	
//		LOG.info("Creating " + packageInfoPath);
//		
//		StringBuilder content = new StringBuilder();
//
//		// Comments
//		generateCommentLinesFromComments(content, javaPackage.getCommentsList(), EMPTY);	
//
//		// Package line
//		content.append(PAKAGE).append(javaPackage.getPackageName()).append(END);
//		
//		// Ecriture du fichier
//		try {
//			Files.write(packageInfoPath, content.toString().getBytes(config.getCharset()));
//		} catch (IOException e) {
//			throw new MdaGeneratorException("Error while creating package-info "  + packageInfoPath + " for package " + javaPackage.getPackageName(), e);		
//		}
//		
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
	
		writeFileFromTemplate(classPath, "entity.vm", context);
	}

	protected void writeFileFromTemplate(Path filePath, String templatePath, VelocityContext context ) throws IOException {
		Template template = null;
		try{
			template = Velocity.getTemplate(templatePath);
		}catch( Exception e ){ 
			throw new MdaGeneratorException("Error while writing from template " + templatePath,e);
		}
		StringWriter sw = new StringWriter();
		template.merge(context,sw);		
		LOG.debug("Creating " + filePath);				
		Files.write(filePath, sw.toString().getBytes(config.getCharset()));	
	}

	/**
	 * 
	 * @param content
	 * @param comments
	 */
	protected void generateCommentLinesFromComments(StringBuilder content, List<String> comments , String indent) {
		content.append(indent).append(START_COMMENT);
		if(!comments.isEmpty()){
			for(String commentLine : comments) {
				content.append(indent).append(CONTINUE_COMMENT).append(commentLine).append(BREAK);
			}
		}
		content.append(indent).append(END_COMMENT);
	}

}
