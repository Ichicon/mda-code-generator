package mda.basics.persistence.hibernate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;

import mda.basics.exceptions.DataAccessException;


/**
 * Handler qui permet de faire des opérations en amont ou en aval des méthodes d'accès aux données Il permet de gérer les exceptions de type SQL et Hibernate
 */
public class DAOInvocationHandler implements InvocationHandler {

    /** L'implementation de l'objet pour le proxy */
    private final Object implementation;

    /** Le logger */
    private final Logger log; // NOPMD

    /**
     * Constructeur
     * @param newImplementation l'objet Ã  utiliser pour les invocations
     */
    public DAOInvocationHandler(final Object newImplementation) {
        this.implementation = newImplementation;
        this.log = LogManager.getLogger(newImplementation.getClass());

    }

    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final long start = System.nanoTime();
        final boolean debugEnabled = log.isDebugEnabled();
        if (debugEnabled) {
            log.debug("Entrée dans la methode " + method.getName());
        }
        try {
            final Object result = method.invoke(implementation, args);
            return result;
        } catch (final InvocationTargetException ex) {
            final Throwable targetException = ex.getTargetException();
            if (targetException instanceof SQLException || targetException instanceof HibernateException ) {
                log.error("Access error", targetException);
                throw new DataAccessException("Access error", targetException);
            }
            throw targetException;
        } finally {
            if (debugEnabled) {
                log.debug("Sortie de la methode " + method.getName() + " - durée de l'execution : "
                                + (System.nanoTime() - start) / 1000000 + "ms");
            }
        }
    }
}
