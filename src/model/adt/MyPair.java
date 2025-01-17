package model.adt;

public class MyPair<T,v> {
    private T first;
    private v second;

    public MyPair(T first, v second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public v getSecond() {
        return second;
    }

    public void setSecond(v second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + ", " + second.toString() + ")";
    }
}
