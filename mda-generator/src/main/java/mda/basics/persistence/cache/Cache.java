package mda.basics.persistence.cache;

/**
 * Interface des implémentations de cache. <br/>
 * @param <K> Type des clés
 * @param <V> Type des valeurs
 */
public interface Cache<K, V> {

    /**
     * Récupération d'un objet mis en cache
     * @param key clé de l'objet
     * @return objet mis en cache, null si l'objet n'est pas en cache
     */
    Object get(final K key);

    /**
     * Mise en cache d'un objet identifié par sa clé.
     * @param key clé de l'objet
     * @param value objet mis en cache
     */
    void put(final K key, final V value);

    /**
     * Suppression d'un objet du cache
     * @param key clé de l'objet
     */
    void remove(final K key);

    /**
     * Purge du cache
     */
    void clear();

    /**
     * @return Taille du cache en nombre de valeurs.
     */
    int size();
}
