package model.statements;

import model.expressions.IExpression;
import model.state.PrgState;
import exceptions.*;
import model.values.IValue;
import model.values.RefValue;

import java.io.IOException;

public class HeapAllocStatement implements IStatement{
    private String varName;
    private IExpression exp;

    public HeapAllocStatement(String varName, IExpression exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, IOException, ADTException {
        if (state.getSymTable().containsKey(this.varName)) {
            throw new StatementException("Variable already defined in the symbol table");
        }

        IValue val = state.getSymTable().getValue(this.varName);
        if(!(val instanceof RefValue)) {
            throw new StatementException("Variable must be RefType");
        }

        IValue expVal = exp.evaluate(state.getSymTable(), state.getHeap());
        if (!val.getType().equals(expVal.getType())) {
            throw new StatementException("The type of the expression must be the same as the location type");
        }

        int address = state.getHeap().allocate(expVal);
        state.getSymTable().insert(this.varName, new RefValue(address, expVal.getType()));
        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new HeapAllocStatement(this.varName, this.exp.deepCopy());
    }

    @Override
    public String toString() {
        return "new(" + this.varName + ", " + this.exp.toString() + ")";
    }
}
