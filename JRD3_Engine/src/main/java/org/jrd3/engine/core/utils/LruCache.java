/*
 * File created on 10/12/2015 by Ray1184
 * Last modification on 10/12/2015 by Ray1184
 */
package org.jrd3.engine.core.utils;

import org.jrd3.engine.core.loaders.Cacheable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Generic cache using the least recently used (LRU) algorithm.
 *
 * @author Ray1184
 * @version 1.0
 */
public class LruCache<K, V extends Cacheable> {

    /**
     * The load factor.
     */
    private static final float LOAD_FACTOR = 0.75f;

    /**
     * The load factor.
     */
    private static final int INITIAL_SLOTS = 100;

    /**
     * The cache itself.
     */
    private final LinkedHashMap<K, V> map;

    /**
     * The cache max size (in bytes).
     */
    private final long maxSize;

    /**
     * The cache current size (in bytes).
     */
    private long currentSize;

    /**
     * Default constructor.
     *
     * @param maxSize The cache max size.
     */
    public LruCache(long maxSize) {
        this.maxSize = maxSize;
        map = new LinkedHashMap<K, V>(INITIAL_SLOTS, LOAD_FACTOR, true) {

            private static final long serialVersionUID = 9004389287485515308L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                if (currentSize > LruCache.this.maxSize) {
                    if (!eldest.getValue().isCurrentlyUsed()) {
                        currentSize -= eldest.getValue().getSize();
                        eldest.getValue().cleanup();
                        return true;
                    }
                }
                return false;
            }


        };
    }

    /**
     * Gets an element from the cache.
     *
     * @param key The element key.
     * @return The element.
     */
    public synchronized V retrieve(K key) {
        return map.get(key);
    }

    /**
     * Checks whether cache has key.
     *
     * @param key The key.
     * @return <code>true</code> if key exists, <code>false</code> otherwise.
     */
    public boolean hasKey(K key) {
        return map.containsKey(key);
    }

    /**
     * Adds an element to the cache.
     *
     * @param key   The element key.
     * @param value The element.
     */
    public synchronized void attach(K key, V value) {
        if ((key == null) || (value == null)) {
            return;
        }
        currentSize += value.getSize();
        map.put(key, value);
    }

    /**
     * Removes all from the cache.
     */
    public synchronized void clear() {
        map.forEach((k, v) -> v.cleanup());
        map.clear();
    }

    /**
     * Gets a set of all internal keys.
     *
     * @return The key set.
     */
    public synchronized Set<K> getKeySet() {
        return map.keySet();
    }

    /**
     * Getter for property 'currentSize'.
     *
     * @return Value for property 'currentSize'.
     */
    public long getCurrentSize() {
        return currentSize;
    }
}
