package mda.generator.exceptions;


/**
 * Exception occuring during generation
 * 
 * @author Fabien Crapart
 *
 */
public class MdaGeneratorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public MdaGeneratorException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	/**
	 * 
	 * @param msg
	 */
	public MdaGeneratorException(String msg ) {
		super(msg);
	}
}
