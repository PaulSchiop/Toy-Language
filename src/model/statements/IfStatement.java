package model.statements;


import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import exceptions.StatementException;
import model.adt.IMyDict;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.BoolType;
import model.values.BoolValue;
import model.values.IValue;

public class IfStatement implements IStatement{
    private IExpression exp;
    private IStatement thenS;
    private IStatement elseS;

    public IfStatement(IExpression exp, IStatement thenS, IStatement elseS) {
        this.exp = exp;
        this.thenS = thenS;
        this.elseS = elseS;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, ADTException {
        IValue val = this.exp.evaluate(state.getSymTable(), state.getHeap());
        if (!val.getType().equals(new BoolType())) {
            throw new StatementException("Condition is not a boolean");
        }
        if (((BoolValue)val).getVal()) {
            state.getExeStack().push(this.thenS);
        } else {
            state.getExeStack().push(this.elseS);
        }
        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new IfStatement(this.exp.deepCopy(), this.thenS.deepCopy(), this.elseS.deepCopy());
    }

    @Override
    public String toString() {
        return "if (" + this.exp.toString() + ") then (" + this.thenS.toString() + ") else (" + this.elseS.toString() + ")";
    }

    @Override
    public IMyDict<String, model.types.IType> typeCheck(IMyDict<String, model.types.IType> typeEnv) throws StatementException {
        model.types.IType typeExp = this.exp.typeCheck(typeEnv);
        if (!typeExp.equals(new BoolType())) {
            throw new StatementException("Condition is not a boolean");
        }
        this.thenS.typeCheck(typeEnv.deepCopy());
        this.elseS.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }
}
