package model.statements;

import model.state.PrgState;
import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import exceptions.StatementException;
import model.values.IValue;
import model.expressions.IExpression;

import java.io.IOException;

public class AssignStatement implements IStatement{
    private String id;
    private IExpression exp;
    public AssignStatement(String id, IExpression exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, IOException, ADTException {
        if (!state.getSymTable().containsKey(id)) {
            throw new StatementException("Variable " + id + " is not defined");
        }
        IValue val = state.getSymTable().getValue(this.id);
        IValue eval = this.exp.evaluate(state.getSymTable(), state.getHeap());
        if (!val.getType().equals(eval.getType())) {
            throw new StatementException("Types do not match");
        }
        state.getSymTable().insert(this.id, eval);
        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new AssignStatement(this.id, this.exp.deepCopy());
    }

    @Override
    public String toString() {
        return this.id + " = " + this.exp.toString();
    }
}
