package model.statements;

import exceptions.*;
import model.adt.IMyDict;
import model.state.PrgState;
import model.expressions.IExpression;
import model.types.IType;
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
        if (!state.getSymTable().contains(this.varName)) {
            throw new StatementException("Variable not declared");
        }

        if (!(state.getSymTable().getValue(this.varName).getType() instanceof RefType)) {
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

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        IType typeVar = typeEnv.getValue(this.varName);
        IType typeExp = this.exp.typeCheck(typeEnv);
        if (!typeVar.equals(new RefType(typeExp))) {
            throw new StatementException("Type mismatch");
        }
        return typeEnv;
    }
}
