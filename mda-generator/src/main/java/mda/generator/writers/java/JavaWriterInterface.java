package mda.generator.writers.java;

/**
 * Class to write java packages, entities and daos
 * 
 * @author Fabien Crapart
 *
 */
public interface JavaWriterInterface {
	/**
	 * Write java code using provided configuration.
	 * 
	 * @param config Java writer configuration.
	 */
	void writeSourceCode(JavaWriterConfig config);

}
