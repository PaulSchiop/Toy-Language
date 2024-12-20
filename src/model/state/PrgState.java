package model.state;
import java.io.BufferedReader;
import java.io.IOException;

import exceptions.EmptyStackExc;
import model.statements.IStatement;
import model.adt.IMyDict;
import model.adt.IMyStack;
import model.adt.IMyList;
import model.values.IValue;
import model.values.StringValue;
import model.adt.*;

public class PrgState {
    private IMyDict<String, IValue> symTable;
    private IMyStack<IStatement> exeStack;
    private IMyList<String> out;
    private IStatement originalProgram;
    private IMyDict<StringValue, BufferedReader> fileTable;
    private IMyHeap heap;

    private final int id;
    private static int currentId = 0;

    public PrgState(IStatement st){
        this.exeStack = new MyStack<>();
        this.symTable = new MyDictionary<>();
        this.out = new MyList<>();
        this.fileTable = new MyDictionary<>();
        this.heap = new MyHeap();
        this.originalProgram = st.deepCopy();
        exeStack.push(st);
        this.id = getNewId();
    }

    public PrgState(IMyStack<IStatement> exeStack, IMyDict<String, IValue> symTable, IMyList<String> out, IMyHeap heap, IStatement originalProgram, IMyDict<StringValue, BufferedReader> fileTable) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.originalProgram = originalProgram.deepCopy();
        exeStack.push(originalProgram);
        this.fileTable = fileTable;
        this.heap = heap;
        this.id = getNewId();
    }

    public synchronized PrgState oneStep() throws IOException {
        if (exeStack.isEmpty()) {
            throw new EmptyStackExc("Program state stack is emmmmpty");
        }
        IStatement currentStatement = exeStack.pop();
        return currentStatement.execute(this);
    }

    public Boolean isNotCompleted() {
        return !this.exeStack.isEmpty();
    }

    public IMyDict<String, IValue> getSymTable() {
        return symTable;
    }

    public IMyDict<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public IMyList<String> getOut() {
        return this.out;
    }

    public IMyHeap getHeap() {
        return this.heap;
    }

    public String fileTableToString() {
        StringBuilder result = new StringBuilder();
        result.append("FileTable:\n");
        for(StringValue key : fileTable.getKeys()) {
            result.append(key).append(" -> ").append(fileTable.getValue(key)).append("\n");
        }
        return result.toString();
    }

    public void setExeStack(IMyStack<IStatement> exeStack) {
        this.exeStack = exeStack;
    }

    public IMyStack<IStatement> getExeStack() {
        return this.exeStack;
    }

    public String toString() {
        return "Program state id: " + id + "\n" +
                "ExeStack: " + exeStack.toString() + "\n" +
                "SymTable: " + symTable.toString() + "\n" +
                "Out: " + out.toString() + "\n" +
                "FileTable: " + fileTable.toString() + "\n"+
                "Heap: " + heap.toString() + "\n";
    }

    private static synchronized int getNewId() {
        currentId++;
        //System.out.println("***************************New id: " + currentId);
        return currentId;
    }

}
