package model.statements;

import model.state.PrgState;
import exceptions.*;
import model.expressions.*;
import model.adt.*;
import model.values.*;
import model.types.*;

import java.io.IOException;

public class ForkStatement implements IStatement{
    private IStatement statement;

    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, IOException, ADTException {
        IMyStack<IStatement> newStack = new MyStack<>();
        return new PrgState(newStack, state.getSymTable().deepCopy(), state.getOut(), state.getHeap(), this.statement, state.getFileTable());
    }

    @Override
    public IStatement deepCopy() {
        return new ForkStatement(this.statement.deepCopy());
    }

    @Override
    public String toString() {
        return "fork(" + this.statement.toString() + ")";
    }

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        this.statement.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }
}
