package model.adt;

import model.values.IValue;

import java.util.Map;

public class MyHeap implements IMyHeap {
    private IMyMap<Integer, IValue> heap;
    private int freeLocation;

    public MyHeap() {
        this.heap = new MyMap<Integer, IValue>();
        this.freeLocation = 1;
    }

    @Override
    public int allocate(IValue value) {
        this.heap.insert(this.freeLocation, value);
        return this.freeLocation++;
    }

    @Override
    public IValue getValue(int key) {
        return this.heap.getValue(key);
    }

    @Override
    public void setValue(int key, IValue value) {
        this.heap.insert(key, value);
    }

    @Override
    public IMyMap<Integer, IValue> getMap() {
        return this.heap;
    }

    @Override
    public boolean containsKey(int key) {
        return this.heap.containsKey(key);
    }

    @Override
    public String toString() {
        return "Heap: " + this.heap.toString();
    }

    @Override
    public void setContent(Map<Integer, IValue> newHeapContent) {
        this.heap = new MyMap<>();
        newHeapContent.forEach((key, value) -> this.heap.insert(key, value));
    }
}
