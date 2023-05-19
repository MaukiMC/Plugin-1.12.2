package net.mauki.maukiseasonpl.caches.handler;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

/**
 * Caches for anything (key - value)
 * @param <K> The key of the element
 * @param <V> The value of the element
 */
public class Cache<K, V> {

    /**
     * The actual size of the cache (for cache-internal processes)
     */
    private int SIZE;

    /**
     * {@link HashMap} for the keys (actual index - key)
     */
    private final HashMap<Integer, K> keys = new HashMap<>();
    /**
     * {@link HashMap} for the values (actual index - value)
     */
    private final HashMap<Integer, V> values = new HashMap<>();

    /**
     * Add an element to the cache
     * @param key The unique key of the element
     * @param value The value/element of the key
     */
    public void add(K key, V value) {
        if(contains(key)) throw new IllegalArgumentException("Key is already in use");
        keys.put(SIZE, key);
        values.put(SIZE, value);
        SIZE++;
    }

    /**
     * Get the value of the index
     * @param i The index of the value in the cache
     * @return The value of the index
     */
    public V get(int i) {
        if(i >= SIZE) throw new IndexOutOfBoundsException();
        return values.get(i);
    }

    /**
     * Get the value of the key
     * @param key The key of the value in the cache
     * @return The value of the key
     */
    public V get(K key) {
        if(keys.containsKey(key)) throw new NullPointerException("Key was not found");
        AtomicReference<Integer> needed_index = new AtomicReference<>();
        keys.forEach((index, target_key) -> {
           if(target_key == key) needed_index.set(index);
        });
        if(needed_index.get() == null) throw new NullPointerException("Key was not found");
        return values.get(needed_index.get());
    }

    /**
     * Check if the key is already in use
     * @param key The key you want to check
     * @return If the key is already in use
     */
    public boolean contains(K key) {
        if(keys.containsKey(key)) throw new NullPointerException("Key was not found");
        AtomicReference<Integer> needed_index = new AtomicReference<>();
        keys.forEach((index, target_key) -> {
            if(target_key == key) needed_index.set(index);
        });
        return needed_index.get() != null;
    }

    /**
     * Get the index of a key
     * @param key The unique key of a value
     * @return The index of the key
     */
    public int indexOf(K key) {
        AtomicReference<Integer> needed_index = new AtomicReference<>();
        keys.forEach((index, target_key) -> {
            if(target_key == key) needed_index.set(index);
        });
        return needed_index.get();
    }

    /**
     * Remove an element from the cache
     * @param key The unique key of the element you want to remove
     */
    public void remove(K key) {
        if(!keys.containsKey(key)) throw new IllegalArgumentException("Key was not found");
        int i = indexOf(key);
        values.remove(i);
        keys.remove(i);
    }

    /**
     * Loop through all cache elements
     * @param action The action you want to perform with the keys and values
     */
    public void forEach(BiConsumer<? super K, ? super V> action) {
        if(action == null) throw new NullPointerException();
        if(size() <= 0) return;
        for(int i = 0; i < size(); i++) {
            action.accept(keys.get(i), values.get(i));
        }
    }

    /**
     * Get the size of the cache
     * @return The size
     */
    public int size() {
        return values.size();
    }

}
