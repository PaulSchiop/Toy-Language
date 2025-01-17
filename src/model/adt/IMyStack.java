package model.adt;
import exceptions.EmptyStackExc;
import java.util.Stack;

public interface IMyStack<T> {
    public void push(T elem);
    public T pop() throws EmptyStackExc;
    public int size();
    public boolean isEmpty();
    public T peek() throws EmptyStackExc;
    public Stack<T> getStack();
}
