package mda.basics.persistence.hibernate;

/**
 * Exception lorsque HibernateUtil n'a pas encore été initialisé dans l'application.
 */
public class NotInitializedException extends RuntimeException {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur.
     */
    public NotInitializedException() {
        super("HibernateUtil n'a pas été initialisé");
    }

}
