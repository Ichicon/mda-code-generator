package mda.basics.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import mda.basics.persistence.hibernate.HibernateUtil;

/**
 * Lancement du contexte hibernate au démarrage
 */
public class HibernateListener implements ServletContextListener {

    /**
     * Initialisation du context servlet
     * @param event Evenement
     */
    @Override
    public void contextInitialized(final ServletContextEvent event) {
        // Appel de la methode static d'initialisation
        HibernateUtil.getSessionFactory(getHibernateKey());
    }

    /**
     * Destruction du context servlet
     * @param event Evenement
     */
    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        HibernateUtil.getSessionFactory(getHibernateKey()).close();
    }

    /**
     * @return la clé de la configuration hibernate ("" par défaut)
     */
    protected String getHibernateKey() {
        return "";
    }

}
