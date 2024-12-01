package controller;

import exceptions.EmptyStackExc;
import model.adt.*;
import model.statements.IStatement;
import model.state.PrgState;
import model.values.IValue;
import model.values.RefValue;
import model.values.StringValue;
import repository.IRepo;
import repository.Repo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Controller {
    private final IRepo repo;
    private boolean displayFlag;

    public Controller(IRepo repo, boolean flag){
        this.repo = repo;
        this.displayFlag = flag;
    }

    Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddr, Map<Integer, IValue> heap){
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues){
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {RefValue v1 = (RefValue) v; return v1.getAddress();})
                .collect(Collectors.toList());
    }

    void performGarbageCollection(PrgState state) {
        IMyHeap heap = state.getHeap();
        List<Integer> symTableAddresses = getAddrFromSymTable(state.getSymTable().getValues());
        //move heap.getMap to a map
        IMyMap<Integer, IValue> heapMap = heap.getMap();
        Map<Integer, IValue> heapContent = heapMap.getContent();
        Map<Integer, IValue> newHeapContent = unsafeGarbageCollector(symTableAddresses, heap.getMap().getContent());
        heap.setContent(newHeapContent);
    }

    public PrgState executeOneStep(PrgState state) throws EmptyStackExc, IOException {
        IMyStack<IStatement> stack = state.getExeStack();
        if (stack.isEmpty()){
            throw new EmptyStackExc("The execution stack is empty");
        }

        IStatement curr = stack.pop();
        curr.execute(state);

        if (displayFlag){
            displayCurrentState(state);
        }
        return state;
    }

    public void displayCurrentState(PrgState state){
        System.out.println(state.toString() + '\n');
    }

    public void executeAllSteps() throws EmptyStackExc, IOException {
        PrgState curr = this.repo.getCurrentProgram();
        repo.logPrgStateExec();
        while(!curr.getExeStack().isEmpty()) {
            executeOneStep(curr);
            repo.logPrgStateExec();
            performGarbageCollection(curr);
            repo.logPrgStateExec();
        }
    }

    public void addProgram(IStatement st){
        this.repo.addProgram(new PrgState(st));
    }

    public Repo getRepo(){
        return (Repo) this.repo;
    }
}
