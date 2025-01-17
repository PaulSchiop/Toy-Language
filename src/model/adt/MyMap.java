package model.adt;

import exceptions.KeyNotFoundExc;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public class MyMap<K,V> implements IMyMap{
    private Map<K,V> map;

    public MyMap(){
        this.map = new HashMap<K,V>();
    }

    @Override
    public void insert(Object key, Object value) {
        this.map.put((K) key, (V) value);
    }

    @Override
    public void remove(Object key) throws KeyNotFoundExc {
        if (!this.map.containsKey(key))
            throw new KeyNotFoundExc("Key not found in dictionary");
        this.map.remove(key);
    }

    @Override
    public Object getValue(Object key) throws KeyNotFoundExc {
        if (!this.map.containsKey(key))
            throw new KeyNotFoundExc("Key not found in dictionary");
        return this.map.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public Set getKeys() {
        return this.map.keySet();
    }

    @Override
    public Map<K,V> getContent() {
        return this.map;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (K key : this.map.keySet()) {
            str.append(key).append(" -> ").append(this.map.get(key)).append("\n");
        }
        return "My map: " + str;
    }

    @Override
    public Set entrySet() {
        return this.map.entrySet();
    }
}
