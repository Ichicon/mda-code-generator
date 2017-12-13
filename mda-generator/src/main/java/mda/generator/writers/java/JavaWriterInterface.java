package mda.generator.writers.java;

/**
 * Class to write java packages, entities and daos
 * 
 * @author Fabien Crapart
 *
 */
public interface JavaWriterInterface {
	/**
	 * Write code
	 */
	void writeSourceCode(JavaWriterConfig config);

}
