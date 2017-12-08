package mda.basics.exceptions;

/**
 * Description de la classe : Exception spécifique à la couche DAO
 */
public class DataAccessException extends Exception {

    /**
     * Numéro d'identification de la version de la classe.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de la classe
     */
    public DataAccessException() {
        super();
    }

    /**
     * Constructeur de la classe avec un message et une exception source.
     * @param msg Message de l'exeption
     */
    public DataAccessException(final String msg) {
        super(msg);
    }

    /**
     * Constructeur de la classe
     * @param ex Exception source
     */
    public DataAccessException(final Throwable ex) {
        super("Erreur DataAccessException", ex);
    }

    /**
     * Constructeur de la classe avec un message et une exception source.
     * @param msg Message de l'exeption
     * @param ex Exception source
     */
    public DataAccessException(final String msg, final Throwable ex) {
        super(msg, ex);
    }
}
