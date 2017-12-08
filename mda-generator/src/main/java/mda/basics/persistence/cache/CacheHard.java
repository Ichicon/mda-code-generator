package mda.basics.persistence.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestion de cache en mémoire. <br/>
 * Les références en cache sont conservées quelle que soit la mémoire disponible.
 * @param <K> Type des clés
 * @param <V> Type des valeurs
 */
public class CacheHard<K, V> implements Cache<K, V> {

    /** The internal Map. */
    private final Map<K, V> map = new ConcurrentHashMap<K, V>();

    @Override
    public Object get(final K key) {
        return map.get(key);
    }

    @Override
    public void put(final K key, final V value) {
        map.put(key, value);
    }

    @Override
    public void remove(final K key) {
        map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }
}
