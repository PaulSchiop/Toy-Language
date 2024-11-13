package controller;

import exceptions.EmptyStackExc;
import model.adt.*;
import model.statements.IStatement;
import model.state.PrgState;
import model.values.IValue;
import model.values.StringValue;
import repository.IRepo;
import repository.Repo;

import java.io.BufferedReader;
import java.io.IOException;

public class Controller {
    private final IRepo repo;
    private boolean displayFlag;

    public Controller(IRepo repo, boolean flag){
        this.repo = repo;
        this.displayFlag = flag;
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
        while(!curr.getExeStack().isEmpty())
            executeOneStep(curr);
    }

    public void addProgram(IStatement st){
        this.repo.addProgram(new PrgState(st));
    }

    public Repo getRepo(){
        return (Repo) this.repo;
    }
}
