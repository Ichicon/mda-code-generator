package mda.basics.persistence.cache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestion de cache en mémoire. <br/>
 * Les références en cache sont conservées en fonction de la place mémoire disponible.
 * @param <K> Type des clés
 * @param <V> Type des valeurs
 */
public class CacheSoft<K, V> implements Cache<K, V> {

    /** The internal Map that will hold the SoftReference. */
    private final Map<K, SoftValue<K, V>> map = new ConcurrentHashMap<K, SoftValue<K, V>>();

    /** Reference queue for cleared SoftReference objects. */
    private final ReferenceQueue<V> queue = new ReferenceQueue<V>();

    @Override
    public Object get(final K key) {
        final Object result;
        // We get the SoftReference represented by that key
        final SoftReference<V> softRef = map.get(key);
        if (softRef != null) {
            // From the SoftReference we get the value, which can be
            // null if it was not in the map, or it was removed in
            // the processQueue() method defined below
            result = softRef.get();
            if (result == null) {
                // If the value has been garbage collected, remove the
                // entry from the HashMap.
                map.remove(key);
            }
        } else {
            result = null;
        }
        return result;
    }

    /**
     * We define our own subclass of SoftReference which contains not only the value but also the key to make it easier to find the entry in the Map after it's been garbage collected.
     */
    private static final class SoftValue<K, V> extends SoftReference<V> {

        /**
         * Key.
         */
        private final K key;

        /**
         * Constructor.
         * @param value V
         * @param key K
         * @param queue ReferenceQueue
         */
        SoftValue(final V value, final K key, final ReferenceQueue<V> queue) {
            super(value, queue);
            this.key = key;
        }
    }

    /**
     * Here we go through the ReferenceQueue and remove garbage collected SoftValue objects from the Map by looking them up using the SoftValue.key data member.
     */
    @SuppressWarnings("unchecked")
    private void processQueue() {
        SoftValue<K, V> softValue = (SoftValue<K, V>) queue.poll();
        while (softValue != null) {
            map.remove(softValue.key); // we can access private data!
            softValue = (SoftValue<K, V>) queue.poll();
        }
    }

    @Override
    public void put(final K key, final V value) {
        processQueue(); // throw out garbage collected values first
        map.put(key, new SoftValue<K, V>(value, key, queue));
    }

    @Override
    public void remove(final K key) {
        processQueue(); // throw out garbage collected values first
        map.remove(key);
    }

    @Override
    public void clear() {
        processQueue(); // throw out garbage collected values
        map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }
}
