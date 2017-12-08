package mda.basics.persistence.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * Gestion de cache utilisant la librarie Ehcache, paramétrable dans le fichier ehcache.xml. <br/>
 * @param <K> Type des clés
 * @param <V> Type des valeurs
 */
public class EhCache<K, V> implements Cache<K, V> {

    /**
     * Gestionnaire Ehcache static.
     */
    private static final CacheManager CACHE_MANAGER = CacheManager.create();

    /**
     * Instance de ce cache.
     */
    private final Ehcache cache;

    /**
     * Constructeur.
     * @param cacheName Nom unique du cache
     */
    public EhCache(final String cacheName) {
        super();
        // la configuration du cache sera déduite du fichier ehcache.xml présent à côté des classes
        // et selon son nom si paramétré dans ehcache.xml ou selon la configuration par défaut paramétrée dans ehcache.xml
        cache = CACHE_MANAGER.addCacheIfAbsent(cacheName);
    }

    /**
     * Purge les caches.
     */
    public static void clearAll() {
        CACHE_MANAGER.clearAll();
    }

    /**
     * Arrête définitivement tous les caches et les ressources associées.
     */
    public static void shutdown() {
        CACHE_MANAGER.shutdown();
    }

    @Override
    public Object get(final K key) {
        final Element element = cache.get(key);
        if (element != null) {
            return element.getObjectValue();
        }
        return null;
    }

    @Override
    public void put(final K key, final V value) {
        cache.put(new Element(key, value));
    }

    @Override
    public void remove(final K key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.removeAll();
    }

    @Override
    public int size() {
        return cache.getSize();
    }
}
