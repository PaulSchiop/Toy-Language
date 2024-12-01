package model.adt;
import exceptions.KeyNotFoundExc;

import java.util.Collection;
import java.util.Set;

public interface IMyDict<Key, Value>{
    void insert(Key key, Value value);
    Value getValue(Key key) throws KeyNotFoundExc;
    void remove(Key key) throws KeyNotFoundExc;
    boolean containsKey(Key key);
    Set<Key> getKeys();
    Collection<Value> getValues();
}
