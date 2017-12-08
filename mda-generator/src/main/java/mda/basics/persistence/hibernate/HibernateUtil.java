// --------------------------------------------------------------
// Projet : imdv

// Client : Mediavision
// Auteur : BULL
// Copyright © BULL Mediavision - 2008
// --------------------------------------------------------------
// $Id: $File $Revision $DateZ $Author $
// --------------------------------------------------------------

package mda.basics.persistence.hibernate;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import mda.basics.Constantes;
import mda.basics.exceptions.DataAccessException;



/**
 * Description de la classe : Fonctions génériques permettant de facilité, l'utilisation d'hibernate.
 * <hr/>
 * @author BULL - $Author: cvs_cr $
 * @version $Revision: 1.10 $ - $Date: 2005/04/26 12:21:45 $
 */
public final class HibernateUtil {

    /**
     * Log des messages applicatifs.
     */
    private static final Logger LOG = LogManager.getLogger(HibernateUtil.class);

    /** Les instances d'HibernateUtil */
    private static final Map<String, HibernateUtil> INSTANCES = new HashMap<String, HibernateUtil>();

    /**
     * Fabrique de session.
     */
    private SessionFactory sessionFactory;

    /**
     * configuration
     */
    private Configuration config;

    /**
     * Thread de gestion de la session.
     */
    private final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();

    /** Le mapper entre interface et entité */
    private EntityClassMapper entityClassMapper;

    /**
     * Initialisation de l'instance par défaut d'HibernateUtil (si une seule configuration Hibernate)
     */
    public static void init() {
        final String hibernateConfigFile = System.getProperty("hibernate.configuration.file", "hibernate.cfg.xml");
        init("", hibernateConfigFile);
    }

    /**
     * Initialisation d'HibernateUtil
     * @param key la clé pour retrouver l'instance d'HibernateUtil
     * @param hibernateConfigFile le fichier de configuration à utiliser pour hibernate
     */
    public static void init(final String key, final String hibernateConfigFile) {
        try {
            final HibernateUtil hUtil = new HibernateUtil();
            INSTANCES.put(key, hUtil);
            
            // Crée la SessionFactory
            hUtil.config = new Configuration();
            URL urlHibernateCfg = HibernateUtil.class.getClassLoader().getResource(hibernateConfigFile);
            if (urlHibernateCfg == null) {
                urlHibernateCfg = new File(hibernateConfigFile).toURI().toURL();
            }
            hUtil.config = hUtil.config.configure(urlHibernateCfg);
            // Construction d'un ServiceRegistry pour éviter le deprecated du
            // changement de version d'Hibernate.
            // S'assurer que ce ServiceRegistry convient pour la construction de
            // la Session
//            final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
//                            hUtil.config.getProperties()).build();

            hUtil.sessionFactory = hUtil.config.buildSessionFactory();
            final String uriEntityClassMapper = hUtil.config.getProperty("bull.entityClassMapper");
            hUtil.entityClassMapper = (EntityClassMapper) Class.forName(uriEntityClassMapper).newInstance();

            // Chargement du cache
            final String uriCacheLoader = hUtil.config.getProperty("bull.cacheLoader");
            if (uriCacheLoader != null) {
                final CacheLoader cacheLoader = (CacheLoader) Class.forName(uriCacheLoader).newInstance();
                final Thread cacheLoaderThread = new Thread() {

                    @Override
                    public void run() {
                        try {
                            cacheLoader.loadCache();
                        } catch (final DataAccessException e) {
                            LOG.warn(e.toString(), e);
                        }
                    }
                };
                cacheLoaderThread.setDaemon(true);
                cacheLoaderThread.start();
            }

        } catch (final Exception ex) {
            final String msg = Constantes.MSG_ERR_SESSION_FACTORY;
            LOG.error(msg, ex);
            throw new HibernateException(msg, ex);
        }
    }

    /**
     * Protection de l'instanciation d'une classe statique
     */
    private HibernateUtil() {
        // Rien à faire
    }

    /**
     * Récupère l'instance d'HibernateUtil pour la clé en paramètre
     * @param key la clé hibernate (facultatif)
     * @return l'instance d'HibernateUtil
     */
    private static HibernateUtil getInstance(final String... key) {
        if (key.length > 1) {
            throw new IllegalArgumentException("Appeler la méthode getInstance avec une seule clé : " + key.length);
        }
        final String key1;
        if (key.length == 1) {
            key1 = key[0];
        } else {
            key1 = "";
        }
        if (!INSTANCES.containsKey(key1)) {
            if (key1 != null && key1.isEmpty()) {
                init();
            } else {
                throw new IllegalArgumentException("HibernateUtil inconnu pour la configuration : " + key1);
            }
        }
        return INSTANCES.get(key1);
    }

    /**
     * Renvoie la session courante.
     * @param key la clé hibernate à utiliser (facultatif)
     * @return Session Retourne la session
     * @throws DataAccessException si impossible d'accéder à la BdD
     */
    public static Session currentSession(final String... key) throws DataAccessException {
        final HibernateUtil hUtil = getInstance(key);
        Session session = hUtil.threadLocal.get();
        // Ouvre une nouvelle Session, si ce Thread n'en a aucune
        if (session == null || !session.isOpen()) {
            try {
                session = hUtil.sessionFactory.openSession();
                session.setFlushMode(FlushMode.COMMIT);
            } catch (final HibernateException hex) {
                throw new DataAccessException(Constantes.MSG_ERR_SESSION_FACTORY, hex);
            }
            hUtil.threadLocal.set(session);
        }
        return session;
    }

    /**
     * Ferme la session.
     * @param key la clé hibernate à utiliser (facultatif)
     */
    public static void closeSession(final String... key) {
        final HibernateUtil hUtil = getInstance(key);
        // fermeture de session
        final Session s = hUtil.threadLocal.get();
        try {
            if (s != null && s.isOpen()) {
                s.close();
            }
        } finally {
            hUtil.threadLocal.remove();
        }
    }

    /**
     * obtention de l'objet PersistentClass a partir de la classe bean hibernate
     * @param entityClass class bean entité
     * @param key la clé hibernate à utiliser (facultatif)
     * @return objet PersistentClass
     */
//    public static PersistentClass getClassMapping(final Class<?> entityClass, final String... key) {
//        final HibernateUtil hUtil = getInstance(key);
//        return hUtil.config.getClassMapping(entityClass.getName());
//    }

    /**
     * Accesseur a la configuration hibernate
     * @param key la clé hibernate à utiliser (facultatif)
     * @return objet de configuration hibernate
     */
    public static Configuration getConfiguration(final String... key) {
        final HibernateUtil hUtil = getInstance(key);
        return hUtil.config;
    }

    /**
     * Accesseur a la factory de session hibernate
     * @param key la clé hibernate à utiliser (facultatif)
     * @return objet de fabrique de session hibernate
     */
    public static SessionFactory getSessionFactory(final String... key) {
        final HibernateUtil hUtil = getInstance(key);
        return hUtil.sessionFactory;
    }

    /**
     * Récupère la valeur du EntityClassMapper
     * @param key la clé hibernate à utiliser (facultatif)
     * @return the entityClassMapper
     */
    public static EntityClassMapper getEntityClassMapper(final String... key) {
        final HibernateUtil hUtil = getInstance(key);
        return hUtil.entityClassMapper;
    }

}
