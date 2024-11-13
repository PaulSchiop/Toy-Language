package model.adt;

import exceptions.EmptyStackExc;

import java.util.Stack;

public class MyStack<T> implements IMyStack<T> {
    private Stack<T> stack;

    public MyStack() {
        this.stack = new Stack<>();
    }

    @Override
    public void push(T elem) {
        this.stack.push(elem);
    }

    @Override
    public T pop() throws EmptyStackExc {
        if (this.stack.isEmpty()) {
            throw new EmptyStackExc("Stack is empty!");
        }
        return this.stack.pop();
    }

    @Override
    public int size() {
        return this.stack.size();
    }

    @Override
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (T elem : this.stack) {
            str.append(elem.toString()).append("\n");
        }
        return "My stack: " + str;
    }
}
