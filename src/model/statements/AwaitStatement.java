package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import exceptions.StatementException;
import javafx.util.Pair;
import model.adt.IMyBarrier;
import model.adt.IMyDict;
import model.state.PrgState;
import model.types.IType;
import model.types.IntType;
import model.values.IValue;
import model.values.IntValue;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitStatement implements IStatement{
    private String var;
    private static final Lock lock = new ReentrantLock();

    public AwaitStatement(String var) {
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, IOException, ADTException {
        lock.lock();

        IMyDict<String, IValue> symTable = state.getSymTable();
        IMyBarrier barrierTable = state.getBarrier();

        if (symTable.contains(var)) {
            IntValue index = (IntValue) symTable.getValue(var);
            int foundIndex = index.getValue();

            if (barrierTable.containsKey(index.getValue())) {

                Pair<Integer, List<Integer>> currentBarriers = barrierTable.get(foundIndex);
                List<Integer> list = currentBarriers.getValue();
                ArrayList<Integer> arrayList = new ArrayList<>(list);

                int length = currentBarriers.getValue().size();
                int currentKey = currentBarriers.getKey();

                if (length < currentKey) {
                    if (arrayList.contains(state.getId())) {
                        state.getExeStack().push(this);
                    }
                    else {
                        arrayList.add(state.getId());
                        barrierTable.put(foundIndex, new Pair<>(currentKey, arrayList));
                        state.setBarrier(barrierTable);
                    }
                }
                else {
                    throw new StatementException("Await address not in barrier");
                }
            }
            else {
                throw new StatementException("Await variable not in barrier");
            }
        }

        lock.unlock();
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new AwaitStatement(this.var);
    }

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        if (typeEnv.getValue(var).equals(new IntType()))
            return typeEnv;
        else
            throw new StatementException("Var is not of type int!");
    }

    @Override
    public String toString() {
        return "await(" + this.var + ")";
    }
}
