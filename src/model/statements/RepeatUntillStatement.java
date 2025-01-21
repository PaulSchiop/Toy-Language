package model.statements;

import exceptions.ADTException;
import exceptions.StatementException;
import model.adt.IMyDict;
import model.adt.IMyStack;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.BoolType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;

import java.io.IOException;

public class RepeatUntillStatement implements IStatement{
    public IStatement statement;
    public IExpression cond;

    public RepeatUntillStatement(IStatement statement, IExpression expression) {
        this.statement = statement;
        this.cond = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws ADTException, IOException, StatementException {
        IMyStack<IStatement> stack = state.getExeStack();
        IValue condValue = cond.evaluate(state.getSymTable(), state.getHeap());

        BoolValue boolCond = (BoolValue) condValue;
        if (!boolCond.getVal()) {
            stack.push(this);
            stack.push(statement);
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new RepeatUntillStatement(statement.deepCopy(), cond.deepCopy());
    }

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        IType typeCond = cond.typeCheck(typeEnv);
        if (typeCond.equals(new BoolType())) {
            statement.typeCheck(typeEnv);
            return typeEnv;
        }
        throw new StatementException("Condition expression is not a boolean");
    }

    @Override
    public String toString() {
        return "repeat {" + statement.toString() + "} until (" + cond.toString() + ")";
    }
}