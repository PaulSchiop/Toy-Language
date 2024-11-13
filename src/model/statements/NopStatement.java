package model.statements;

import model.state.PrgState;

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
}
