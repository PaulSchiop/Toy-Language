package repository;

import model.state.PrgState;

public interface IRepo {
    PrgState getCurrentProgram();
    void addProgram(PrgState program);
    void addPrgState(PrgState prg);
}
