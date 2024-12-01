package repository;

import model.expressions.IExpression;
import model.state.PrgState;
import exceptions.RepoException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.io.FileWriter;
import model.state.PrgState;

public class Repo implements IRepo {
    private List<PrgState> programs;
    private PrgState currentProgram;
    private String logFileName;

    public Repo(String logFileName) {
        this.programs = new LinkedList<>();
        this.logFileName = logFileName;
    }

    @Override
    public PrgState getCurrentProgram() {
        return programs.get(programs.size() - 1);
    }

    @Override
    public void addProgram(PrgState program) {
        this.programs.add(program);
    }

    @Override
    public void addPrgState(PrgState prg) {
        this.programs.add(prg);
    }

    @Override
    public void logPrgStateExec() {
        try{
            PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(this.logFileName, true)));
            logFile.println(this.getCurrentProgram().toString());
            logFile.close();
        } catch (IOException e) {
            throw new RepoException("Could not open log file");
        }
    }
}
