package model.statements;

import model.state.PrgState;
import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import model.values.IValue;
import model.expressions.IExpression;

public class PrintStatement implements IStatement{
    private IExpression exp;

    public PrintStatement(IExpression exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws ExpressionExceptions, ADTException {
        IValue val = this.exp.evaluate(state.getSymTable(), state.getHeap());
        state.getOut().add(val.toString());
        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new PrintStatement(this.exp.deepCopy());
    }

    @Override
    public String toString() {
        return "print(" + this.exp.toString() + ")";
    }
}
