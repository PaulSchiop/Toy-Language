package model.statements;

import model.adt.IMyDict;
import model.adt.IMyHeap;
import model.expressions.IExpression;
import model.state.PrgState;
import exceptions.*;
import model.types.IType;
import model.types.StringType;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CloseReadFile implements IStatement{
    private final IExpression expression;

    public CloseReadFile(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, IOException, ADTException {
        var table = state.getSymTable();
        IMyHeap heap = state.getHeap();

        var result = this.expression.evaluate(table, heap);

        if (!result.getType().equals(new StringType())) {
            throw new StatementException("Expression is not of type string");
        }

        BufferedReader reader = state.getFileTable().getValue((StringValue) result);

        reader.close();

        state.getFileTable().remove((StringValue) result);

        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new CloseReadFile(this.expression.deepCopy());
    }

    @Override
    public String toString() {
        return "closeRFile(" + this.expression.toString() + ")";
    }

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        this.expression.typeCheck(typeEnv);
        return typeEnv;
    }
}
