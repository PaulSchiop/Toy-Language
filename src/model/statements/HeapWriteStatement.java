package model.statements;

import exceptions.*;
import model.state.PrgState;
import model.expressions.IExpression;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class HeapWriteStatement implements IStatement {
    private String varName;
    private IExpression exp;

    public HeapWriteStatement(String varName, IExpression exp) {
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, ADTException {
        if (!state.getSymTable().containsKey(this.varName)) {
            throw new StatementException("Variable not declared");
        }
        if (!state.getSymTable().getValue(this.varName).getType().equals(new RefType())) {
            throw new StatementException("Variable is not of RefType");
        }

        IValue val = state.getSymTable().getValue(this.varName);
        int address = ((RefValue) val).getAddress();
        if (!state.getHeap().containsKey(address)) {
            throw new StatementException("Address not in heap");
        }
        IValue expVal = this.exp.evaluate(state.getSymTable(), state.getHeap());
        if (!expVal.getType().equals(((RefValue) val).getLocationType())) {
            throw new StatementException("Type mismatch");
        }

        state.getHeap().setValue(address, expVal);

        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new HeapWriteStatement(this.varName, this.exp.deepCopy());
    }

    @Override
    public String toString() {
        return "writeHeap(" + this.varName + ", " + this.exp.toString() + ")";
    }
}
