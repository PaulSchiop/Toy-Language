package model.statements;

import model.state.PrgState;
import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import exceptions.StatementException;
import model.values.IValue;
import model.expressions.IExpression;

import java.io.IOException;

public class CompStatement implements IStatement {
    private IStatement first;
    private IStatement second;

    public CompStatement(IStatement first, IStatement second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, IOException, ADTException {
        state.getExeStack().push(this.second);
        state.getExeStack().push(this.first);
        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new CompStatement(this.first.deepCopy(), this.second.deepCopy());
    }

    @Override
    public String toString() {
        return this.first.toString() + "; " + this.second.toString();
    }
}