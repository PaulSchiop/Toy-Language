package model.adt;

import exceptions.KeyNotFoundExc;

import java.util.Map;
import java.util.Set;

public interface IMyMap<K,V> {
    void insert(K key, V value);
    void remove(K key) throws KeyNotFoundExc;
    V getValue(K key) throws KeyNotFoundExc;
    boolean containsKey(K key);
    Set<K> getKeys();
    Map<K,V> getContent();
    Set<Map.Entry<K, V>> entrySet();

}
