package model.statements;

import exceptions.*;
import model.adt.IMyDict;
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
        if (((BoolValue)val).getVal()) {
            state.getExeStack().push(this);
            state.getExeStack().push(this.stmt);
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

    @Override
    public String toString() {
        return "while(" + this.exp.toString() + "){" + this.stmt.toString() + "}";
    }

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        IType typeExp = this.exp.typeCheck(typeEnv);
        if (!typeExp.equals(new BoolType())) {
            throw new StatementException("Condition is not a boolean");
        }
        this.stmt.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }
}
