package model.statements;

import exceptions.ADTException;
import exceptions.StatementException;
import model.adt.IMyDict;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.IType;
import model.values.IntValue;

import java.io.IOException;

public class SleepStatement implements IStatement{
    public IExpression time;

    public SleepStatement(IExpression time) {
        this.time = time;
    }

    @Override
    public PrgState execute(PrgState state) throws ADTException, IOException, StatementException {
        if(time.evaluate(state.getSymTable(), state.getHeap()).getType().equals(new model.types.IntType())){
            IntValue val = (IntValue)time.evaluate(state.getSymTable(), state.getHeap());
            if(val.getValue() > 0){
                state.getExeStack().push(new SleepStatement(new model.expressions.ValueExpression(new model.values.IntValue(val.getValue() - 1))));
            }
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new SleepStatement(time);
    }

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        if(!time.typeCheck(typeEnv).equals(new model.types.IntType())){
            throw new StatementException("Sleep statement: time expression is not of type int");
        }
        return typeEnv;
    }

    @Override
    public String toString() {
        return "Sleep(" + time.toString() + ")";
    }
}
