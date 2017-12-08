package mda.basics.persistence.hibernate;

import mda.basics.exceptions.DataAccessException;

/**
 * Interface pour la classe dédiée au chargement de la classe (à paramétrer dans hibernate.cfg.xml : propriété bull.cacheLoader).
 */
public interface CacheLoader {

    /**
     * Méthode de chargement de cache : monDao.findAll();
     * @throws DataAccessException erreur d'accès aux données
     */
    void loadCache() throws DataAccessException;

}
