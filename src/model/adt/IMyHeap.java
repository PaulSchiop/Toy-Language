package model.adt;

import model.values.IValue;

import java.util.Collection;
import java.util.Map;

public interface IMyHeap {
    public int allocate(IValue value);
    public IValue getValue(int key);
    public void setValue(int key, IValue value);
    public IMyMap<Integer, IValue> getMap();
    public boolean containsKey(int key);
    void setContent(Map<Integer, IValue> newHeapContent);
}
