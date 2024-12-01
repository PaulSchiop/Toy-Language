package model.adt;

import exceptions.KeyNotFoundExc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyDictionary<K,V> implements IMyDict<K,V> {
    private Map<K,V> dict;

    public MyDictionary(){
        this.dict = new HashMap<>();
    }

    @Override
    public void insert(K key, V value) {
        this.dict.put(key, value);
    }

    @Override
    public V getValue(K key) throws KeyNotFoundExc {
        if(!this.dict.containsKey(key))
            throw new KeyNotFoundExc("Key not found in dictionary");
        return this.dict.get(key);
    }

    @Override
    public void remove(K key) throws KeyNotFoundExc {
        if(!this.dict.containsKey(key))
            throw new KeyNotFoundExc("Key not found in dictionary");
        this.dict.remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        return this.dict.containsKey(key);
    }

    @Override
    public Set<K> getKeys() {
        return this.dict.keySet();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (K key : this.dict.keySet()) {
            Object value = this.dict.get(key);
            str.append(key.toString()).append(" -> ")
                    .append(value != null ? value.toString() : "null") // Handle null values
                    .append("\n");
        }
        return "My dictionary: " + str;
    }

    Map<K,V> getDict() {
        return this.dict;
    }

    @Override
    public Collection<V> getValues() {
        return this.dict.values();
    }
}
