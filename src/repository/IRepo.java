package repository;

import exceptions.RepoException;
import model.state.PrgState;

import java.util.List;

public interface IRepo {
    void addProgram(PrgState program);
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> prgList);
    void logPrgStateExec(PrgState prg) throws RepoException;
}
