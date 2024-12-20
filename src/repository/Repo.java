package repository;

import model.state.PrgState;
import exceptions.RepoException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.io.FileWriter;

public class Repo implements IRepo {
    private List<PrgState> programs;
    private PrgState currentProgram;
    private String logFileName;

    public Repo(String logFileName) {
        this.programs = Collections.synchronizedList(new LinkedList<>());
        this.logFileName = logFileName;
    }

    @Override
    public void addProgram(PrgState program) {
        this.programs.add(program);
    }

    @Override
    public List<PrgState> getPrgList() {
        return this.programs;
    }

    @Override
    public void setPrgList(List<PrgState> prgList) {
        this.programs.clear();
        this.programs = prgList;
    }

    @Override
    public void logPrgStateExec(PrgState prg) {
        try{
            PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(this.logFileName, true)));
            logFile.println(prg.toString());
            logFile.close();
        } catch (IOException e) {
            throw new RepoException("Could not open log file");
        }
    }
}
