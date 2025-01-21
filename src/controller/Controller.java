package controller;

import exceptions.ControllerException;
import exceptions.EmptyStackExc;
import model.adt.*;
import model.statements.IStatement;
import model.state.PrgState;
import model.types.IType;
import model.values.IValue;
import model.values.RefValue;
import model.values.StringValue;
import repository.IRepo;
import repository.Repo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private final IRepo repo;
    private boolean displayFlag;
    private ExecutorService executor;

    public Controller(IRepo repo, boolean flag) {
        this.repo = repo;
        this.displayFlag = flag;
        this.executor = Executors.newFixedThreadPool(2);
    }

    public ExecutorService getExecutor() {
        return this.executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public void oneStepForAllPrg(List<PrgState> prgList) throws EmptyStackExc, IOException, InterruptedException{
        List<PrgState> prgStates = removeCompletedPrg(prgList);

        if(prgList.isEmpty()){
            throw new ControllerException("No more programs to execute");
        }

        prgStates.forEach(repo::logPrgStateExec);

        List<Callable<PrgState>> callList = prgStates.stream().filter(p -> !p.getExeStack().isEmpty())
                .map((PrgState p) -> (Callable<PrgState>) (p::oneStep))
                .toList();



        List<PrgState> newPrgList;
        try{
            newPrgList = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (ExecutionException | InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .toList();
        } catch (InterruptedException e) {
            throw new ControllerException(e.getMessage());
        }

        for (PrgState newState : newPrgList) {
            if(!prgStates.contains(newState)){
                prgStates.add(newState);
            }
        }

        prgStates.forEach(repo::logPrgStateExec);

        repo.setPrgList(prgStates);
    }


    public void allSteps() throws EmptyStackExc, IOException, InterruptedException {
        IMyDict<String, IType> table = new MyDictionary<>();
        for (PrgState prg : repo.getPrgList()) {

            //print the list
            System.out.println(repo.getPrgList());

            if (!prg.getExeStack().isEmpty()) {
                System.out.println("Type environment before type check: " + table);
                table = prg.getExeStack().peek().typeCheck(table);
                System.out.println("Type environment after type check: " + table);
            }
        }

        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());

        if (prgList.isEmpty()) {
            System.out.println("Program state stack is empty");
            executor.shutdownNow();
            return;
        }
        prgList.forEach(repo::logPrgStateExec);

        try {
            while (!prgList.isEmpty()) {
                this.performGarbageCollection(prgList.getFirst());
                oneStepForAllPrg(prgList);
                prgList.forEach(System.out::println);
                prgList = removeCompletedPrg(repo.getPrgList());
            }
        } catch (ControllerException e) {
            System.out.println("Program finished");
        }
        executor.shutdownNow();

        repo.setPrgList(prgList);
    }

    public void performGarbageCollection(PrgState state) {
        List<Integer> symTableAddresses = GarbageCollector.getAddrFromSymTable(state.getSymTable().getContent().values());
        List<Integer> reachableAddresses = GarbageCollector.getTransitiveAddresses(symTableAddresses, state.getHeap().getMap().getContent());
        Map<Integer, IValue> cleanedHeap = GarbageCollector.unsafeGarbageCollector(reachableAddresses, state.getHeap().getMap().getContent());
        state.getHeap().setContent(cleanedHeap);
    }

    List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());
    }

    public void addProgram(IStatement st) {
        this.repo.addProgram(new PrgState(st));
    }

    public Repo getRepo() {
        return (Repo) this.repo;
    }

    public List<PrgState> getProgramStateList() {
        return this.repo.getPrgList();
    }
}
