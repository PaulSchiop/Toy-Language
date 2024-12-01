package model.statements;

import exceptions.*;
import model.state.PrgState;
import model.expressions.IExpression;
import model.values.BoolValue;
import model.values.IValue;
import model.types.*;

import java.io.IOException;

public class WhileStatement implements IStatement{
    private IExpression exp;
    private IStatement stmt;

    public WhileStatement(IExpression exp, IStatement stmt) {
        this.exp = exp;
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, IOException, ADTException {
        IValue val = this.exp.evaluate(state.getSymTable(), state.getHeap());
        if (!val.getType().equals(new BoolType())) {
            throw new StatementException("Condition is not a boolean");
        }
        if (((BoolValue)val).getVal()) {
            state.getExeStack().push(stmt);
            execute(state);
        }
        else{
            state.getExeStack().pop();
        }
        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new WhileStatement(this.exp.deepCopy(), this.stmt.deepCopy());
    }
}