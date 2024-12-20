package model.adt;
import exceptions.KeyNotFoundExc;
import model.values.IValue;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface IMyDict<Key, Value>{
    void insert(Key key, Value value);
    Value getValue(Key key) throws KeyNotFoundExc;
    void remove(Key key) throws KeyNotFoundExc;
    boolean contains(Key key);
    Set<Key> getKeys();
    Collection<Value> getValues();
    public IMyDict<Key, Value> deepCopy();

    Map<IValue, IValue> getContent();
}
