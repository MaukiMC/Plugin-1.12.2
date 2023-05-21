package net.mauki.maukiseasonpl.caches.handler;

import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
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
     * Add an element or update if it already exists
     * @param key The key of the element you want to add
     * @param value The value of the element you want to add
     */
    public void addOrUpdate(K key, V value) {
        if(!contains(key)) {
            add(key, value);
            return;
        }
        int realIndex = realIndexOf(key);
        values.put(realIndex, value);
    }

    /**
     * Add an element and remove it after a specific period of time
     * @param key The key of the element
     * @param value The value of the element
     * @param millis The time in milliseconds
     * @return A {@link CompletionStage} object which will be triggered after the element got removed or an error occurred
     */
    public CompletionStage<JSONObject> addAndRemove(K key, V value, long millis) {
        AtomicReference<CompletableFuture<JSONObject>> futureAtomicReference = new AtomicReference<>();
        add(key, value);
        int index = realIndexOf(key);
        new Thread(() -> {
            try {
                Thread.sleep(millis);
                if(!this.contains(key)) {
                    CompletableFuture<JSONObject> future = new CompletableFuture<>();
                    future.complete(new JSONObject()
                            .put("success", false)
                            .put("reason", "Element was not longer in cache")
                            .put("realIndex", index));
                    futureAtomicReference.set(future);
                    return;
                }
                remove(key);
                CompletableFuture<JSONObject> future = new CompletableFuture<>();
                future.complete(new JSONObject().put("success", true));
                futureAtomicReference.set(future);
            } catch (InterruptedException e) {
                CompletableFuture<JSONObject> future = new CompletableFuture<>();
                future.complete(new JSONObject()
                        .put("success", false)
                        .put("reason", e.getMessage())
                        .put("realIndex", index));
                futureAtomicReference.set(future);
                e.printStackTrace();
            }
        }).start();
        return futureAtomicReference.get();
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
     * Get a value or a default value if the key is not known
     * @param key The key of the value you want to get
     * @param defaultValue The default object that will be returned if the key is not found
     * @return The value or the default value
     */
    public V getOrDefault(K key, V defaultValue) {
        try {
            V v = get(key);
            if(v == null) return defaultValue;
            return v;
        } catch(NullPointerException ex) {
            return defaultValue;
        }
    }

    /**
     * Get all keys of a value
     * @param value The value of
     * @return A {@link Collection} with all keys
     */
    public Collection<K> getKeysOfValue(V value) {
        Collection<K> keySet = new ArrayList<>();
        values.forEach((index, v) -> {
            if(value == v) keySet.add(keys.get(index));
        });
        return keySet;
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
     * Get the real index of a key
     * @param key The key of the element you want the real index of
     * @return The real index of the element
     */
    public int realIndexOf(K key) {
        AtomicReference<Integer> i = new AtomicReference<>();
        keys.forEach((index, target_key) -> {
            if(target_key == key) i.set(index);
        });
        return i.get();
    }

    /**
     * Remove an element from the cache
     * @param key The unique key of the element you want to remove
     */
    public void remove(K key) {
        if(!keys.containsKey(key)) throw new IllegalArgumentException("Key was not found");
        int i = realIndexOf(key);
        values.remove(i);
        keys.remove(i);
    }

    /**
     * Clears the entire cache
     */
    public void clear() {
        values.clear();
        keys.clear();
        SIZE = 0;
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
     * Get a {@link Collection} with all keys
     * @return The collection
     */
    public Collection<K> keys() {
        return new ArrayList<>(keys.values());
    }

    /**
     * Get a {@link Collection} with all values
     * @return The collection
     */
    public Collection<V> values() {
        return new ArrayList<>(values.values());
    }

    /**
     * Get the size of the cache
     * @return The size
     */
    public int size() {
        return values.size();
    }

}
