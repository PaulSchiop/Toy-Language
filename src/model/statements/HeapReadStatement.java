package model.statements;

import model.adt.IMyDict;
import model.state.PrgState;
import exceptions.*;
import model.expressions.IExpression;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

import java.io.IOException;

public class HeapReadStatement implements IStatement{
    private IExpression exp;

    public HeapReadStatement(String varName, IExpression exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, IOException, ADTException {
        IValue val = this.exp.evaluate(state.getSymTable(), state.getHeap());
        if (!val.getType().equals(new RefType())) {
            throw new StatementException("The expression must be of RefType");
        }

        int address = ((RefValue) val).getAddress();
        if(!state.getHeap().containsKey(address)) {
            throw new StatementException("The address is not a key in the heap table");
        }
        state.getHeap().getValue(address);

        return state;
    }

    @Override
    public IStatement deepCopy() {
        return null;
    }

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        IType typeExp = this.exp.typeCheck(typeEnv);
        if (!typeExp.equals(new RefType())) {
            throw new StatementException("The expression must be of RefType");
        }
        return typeEnv;
    }
}
