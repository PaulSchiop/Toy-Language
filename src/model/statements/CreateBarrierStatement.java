package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import exceptions.StatementException;
import javafx.util.Pair;
import model.adt.IMyBarrier;
import model.adt.IMyDict;
import model.adt.IMyHeap;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.IType;
import model.types.IntType;
import model.values.IValue;
import model.values.IntValue;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CreateBarrierStatement implements IStatement{
    private IExpression expression;
    private String variable;
    private static Lock lock = new ReentrantLock();

    public CreateBarrierStatement(String variable, IExpression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    public static Lock getLock() {
        return lock;
    }

    public static void setLock(Lock lock) {
        CreateBarrierStatement.lock = lock;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, IOException, ADTException {
        lock.lock();
        IMyDict<String, IValue> symTable = state.getSymTable();
        IMyHeap heap = state.getHeap();
        IMyBarrier barrierTable = state.getBarrier();

        IntValue waitingThreads = (IntValue) expression.evaluate(symTable, heap);

        int nr = waitingThreads.getValue();
        int freeAddress = barrierTable.getFreeAddress();

        barrierTable.put(freeAddress, new Pair<>(nr, new java.util.LinkedList<>()));

        symTable.insert(variable, new IntValue(freeAddress));
        System.out.println("Barrier created at location: " + freeAddress + " with value: " + nr);

        lock.unlock();
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new CreateBarrierStatement(variable, expression.deepCopy());
    }

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        if (typeEnv.getValue(variable).equals(new IntType()))
            if (expression.typeCheck(typeEnv).equals(new IntType()))
                return typeEnv;
            else
                throw new StatementException("Expression is not of type int");
        else
            throw new StatementException("Variable is not of type int!");
    }

    @Override
    public String toString() {
        return "CreateBarrierStatement(" + variable + ", " + expression.toString() + ")";
    }
}
