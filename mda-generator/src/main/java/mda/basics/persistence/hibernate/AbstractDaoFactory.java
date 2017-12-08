package mda.basics.persistence.hibernate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;


/**
 * Factory qui permet de créer les DAO
 */
public abstract class AbstractDaoFactory {

    /**
     * Méthode qui renvoie un proxy vers la DAO passé en paramètre, et la renvoie dans le type du second argument
     * @param instanceImpl l'instance de l'implémentation à utiliser
     * @param interfaceDAO l'objet est renvoyé sous ce type là
     * @param <T> generic de interfaceService
     * @return le proxy
     */
    @SuppressWarnings("unchecked")
    protected static <T> T getProxyPourDAO(final T instanceImpl, final Class<T> interfaceDAO) {
        final InvocationHandler handler = new DAOInvocationHandler(instanceImpl);
        final Class<?>[] interfaces = new Class[] {interfaceDAO };
        final ClassLoader loader = AbstractDaoFactory.class.getClassLoader();
        return (T) Proxy.newProxyInstance(loader, interfaces, handler);
    }

}
