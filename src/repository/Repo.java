package repository;

import model.state.PrgState;

import java.util.LinkedList;
import java.util.List;

public class Repo implements IRepo {
    private List<PrgState> programs;
    private int currentProgram;

    public Repo() {
        this.programs = new LinkedList<>();
        this.currentProgram = 0;
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
}
