package mda.generator.writers.java;

/**
 * Class to write java packages and classes
 * 
 * @author Fabien Crapart
 *
 */
public interface JavaWriterInterface {
	/**
	 * Initialize writer
	 * @param config Config object
	 */
	void initWriterConfig(JavaWriterConfig config);
	
	/**
	 * Write code
	 */
	void writeSourceCode();

}
