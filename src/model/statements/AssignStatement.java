package model.statements;

import model.adt.IMyDict;
import model.state.PrgState;
import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import exceptions.StatementException;
import model.types.IType;
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
        if (!state.getSymTable().contains(id)) {
            throw new StatementException("Variable " + id + " is not defined");
        }
        IValue val = state.getSymTable().getValue(this.id);
        IValue eval = this.exp.evaluate(state.getSymTable(), state.getHeap());
        System.out.println("AssignStatement: " + this.id + " = " + eval);
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

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        IType typeVar = typeEnv.getValue(id);
        IType typeExp = exp.typeCheck(typeEnv);
        if (!typeVar.equals(typeExp)) {
            throw new StatementException("Assignment: right hand side and left hand side have different types");
        }
        return typeEnv;
    }
}
