package model.statements;

import model.adt.IMyDict;
import model.state.PrgState;
import model.types.IType;

public class NopStatement implements IStatement {
    @Override
    public PrgState execute(PrgState state) {
        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new NopStatement();
    }

    @Override
    public String toString() {
        return "nop";
    }

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) {
        return typeEnv;
    }
}
