package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import exceptions.StatementException;
import model.adt.IMyDict;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.IType;
import model.values.IValue;

import java.io.IOException;

public class SwitchStatement implements IStatement{
    private IStatement stmt1;
    private IStatement stmt2;
    private IStatement stmt3;
    private IExpression exp1;
    private IExpression exp2;
    private IExpression exp3;

    public SwitchStatement(IExpression exp1, IStatement stmt1, IExpression exp2, IStatement stmt2, IExpression exp3, IStatement stmt3) {
        this.exp1 = exp1;
        this.stmt1 = stmt1;
        this.exp2 = exp2;
        this.stmt2 = stmt2;
        this.exp3 = exp3;
        this.stmt3 = stmt3;
    }


    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, IOException, ADTException {
        IValue val1 = this.exp1.evaluate(state.getSymTable(), state.getHeap());
        IValue val2 = this.exp2.evaluate(state.getSymTable(), state.getHeap());
        IValue val3 = this.exp3.evaluate(state.getSymTable(), state.getHeap());

        System.out.println("val1: " + val1);
        System.out.println("val2: " + val2);
        System.out.println("val3: " + val3);

        System.out.println("val1.equals(val2): " + val1.equals(val2));
        System.out.println("val1.equals(val3): " + val1.equals(val3));

        if (val1.equals(val2)) {
            state.getExeStack().push(this.stmt1);
            System.out.println("\n\n\nSwitched to stmt1\n\n\n");
        } else if (val1.equals(val3)) {
            state.getExeStack().push(this.stmt2);
            System.out.println("\n\n\nSwitched to stmt2\n\n\n");
        } else {
            state.getExeStack().push(this.stmt3);
            System.out.println("\n\n\nSwitched to stmt3\n\n\n");
        }
        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new SwitchStatement(this.exp1.deepCopy(), this.stmt1.deepCopy(), this.exp2.deepCopy(), this.stmt2.deepCopy(), this.exp3.deepCopy(), this.stmt3.deepCopy());
    }

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        IType typeExp1 = this.exp1.typeCheck(typeEnv);
        IType typeExp2 = this.exp2.typeCheck(typeEnv);
        IType typeExp3 = this.exp3.typeCheck(typeEnv);
        if (!typeExp1.equals(typeExp2) || !typeExp1.equals(typeExp3)) {
            throw new StatementException("Types do not match");
        }
        this.stmt1.typeCheck(typeEnv.deepCopy());
        this.stmt2.typeCheck(typeEnv.deepCopy());
        this.stmt3.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "switch (" + this.exp1.toString() + ") case (" + this.exp2.toString() + ") : (" + this.stmt1.toString() + ") case (" + this.exp3.toString() + ") : (" + this.stmt2.toString() + ") default : (" + this.stmt3.toString() + ")";
    }
}
