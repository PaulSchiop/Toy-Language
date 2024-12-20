package model.statements;

import model.adt.IMyDict;
import model.expressions.IExpression;
import model.state.PrgState;
import exceptions.*;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;
import model.types.IType;

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
        /*if (state.getSymTable().containsKey(this.varName)) {
            throw new StatementException("Variable already defined in the symbol table");
        }*/

        IValue val = state.getSymTable().getValue(this.varName);
        if(!(val instanceof RefValue)) {
            throw new StatementException("Variable must be RefType");
        }

        IValue expVal = exp.evaluate(state.getSymTable(), state.getHeap());
        IType LocationType = ((RefValue)state.getSymTable().getValue(varName)).getLocationType();
        if(!expVal.getType().equals(LocationType)) {
            throw new StatementException("The type of the variable must be the same as the location type");
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

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        System.out.println("HeapAllocStatement a: " + varName);
        IType typeVar = typeEnv.getValue(varName);
        IType typeExp = exp.typeCheck(typeEnv);
        if (!typeVar.equals(new RefType(typeExp))) {
            throw new StatementException("Heap: right hand side and left hand side have different types");
        }
        return typeEnv;
    }
}
