package mda.generator.writers;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import mda.generator.exceptions.MdaGeneratorException;

/**
 * 
 * @author Fabien Crapart
 *
 */
public class VelocityUtils {
	private static Logger LOG = LogManager.getLogger();

	/**
	 * Analyse already existing file and keep_content and content_to_keep inside velocity context
	 * 
	 * @return false if the file shouldn't be regenerated
	 * @throws IOException 
	 */
	public static boolean analyseFileAndCompleteContext(Path filePath,String OneTimeGeneration, String endOfGenerated, VelocityContext context ) throws IOException {
		StringBuilder contentToKeep = new StringBuilder();
		boolean doNotRegenerate = false;
		boolean keepContent = false;
		boolean lineAdded = false;
		if(Files.exists(filePath)) {
			for(String line : Files.readAllLines(filePath)) {
				// No generation for this one
				if(OneTimeGeneration!= null && line.contains(OneTimeGeneration)) {
					doNotRegenerate =true;
					break;
				}
				// We keep user edited content
				if(keepContent) {
					if(lineAdded) {
						contentToKeep.append("\n");
					}else {
						lineAdded=true;
					}
					contentToKeep.append(line);
				}	

				// Comment to detect user content (after this line)
				if(endOfGenerated!=null && line.contains(endOfGenerated)) {
					keepContent = true;
				}		
			}
		}			
		
		context.put( "keep_content", keepContent);
		context.put( "content_to_keep", contentToKeep.toString());
		
		return !doNotRegenerate;
	}

	/**
	 * Ecriture d'un contenu avec un template velocity
	 * @param filePath
	 * @param templatePath
	 * @param context
	 * @throws IOException
	 */
	public static void writeFileFromTemplate(Path filePath, Path templatePath, VelocityContext context, Charset charset) throws IOException {
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
		Files.write(filePath, sw.toString().getBytes(charset));	
	}
}
