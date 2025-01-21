package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import exceptions.StatementException;
import model.adt.IMyDict;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.BoolType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;

import java.io.IOException;

public class CondStatement implements IStatement{
    //v=exp1 ? exp2 : exp3
    private final String varName;
    private final IExpression exp1;
    private final IExpression exp2;
    private final IExpression exp3;

    public CondStatement(String varName, IExpression exp1, IExpression exp2, IExpression exp3) {
        this.varName = varName;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, IOException, ADTException {
        System.out.println("CondStatement: " + this.varName + " = (" + this.exp1.toString() + ") ? " + this.exp2.toString() + " : " + this.exp3.toString());
        IValue val1 = this.exp1.evaluate(state.getSymTable(), state.getHeap());
        IValue val2 = this.exp2.evaluate(state.getSymTable(), state.getHeap());
        IValue val3 = this.exp3.evaluate(state.getSymTable(), state.getHeap());

        System.out.println("val1: " + val1);
        System.out.println("val2: " + val2);
        System.out.println("val3: " + val3);

        if (val1.equals(new BoolValue(true))) {
            state.getSymTable().insert(this.varName, val2);
            System.out.println("\n\n\nSwitched to stmt1\n\n\n");
        } else {
            state.getSymTable().insert(this.varName, val3);
            System.out.println("\n\n\nSwitched to stmt2\n\n\n");
        }

        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new CondStatement(this.varName, this.exp1.deepCopy(), this.exp2.deepCopy(), this.exp3.deepCopy());
    }

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        IType typeVar = typeEnv.getValue(this.varName);
        IType typeExp1 = this.exp1.typeCheck(typeEnv);
        IType typeExp2 = this.exp2.typeCheck(typeEnv);
        IType typeExp3 = this.exp3.typeCheck(typeEnv);

        if (!typeExp1.equals(new BoolType())) {
            throw new StatementException("The first expression is not of type Bool");
        }

        if (!typeExp2.equals(typeExp3)) {
            throw new StatementException("The second and third expressions are not of the same type");
        }

        if (!typeVar.equals(typeExp2)) {
            throw new StatementException("The variable " + this.varName + " is not of the same type as the second and third expressions");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return this.varName + " = (" + this.exp1.toString() + ") ? " + this.exp2.toString() + " : " + this.exp3.toString();
    }
}
