package model.statements;

import model.adt.IMyDict;
import model.expressions.IExpression;
import model.state.PrgState;
import exceptions.*;
import model.types.IType;
import model.types.StringType;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OpenRFileStatement implements IStatement {
    private final IExpression expression;

    public OpenRFileStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) {

        IValue result = this.expression.evaluate(state.getSymTable(), state.getHeap());
        if(!result.getType().equals(new StringType())) {
            throw new StatementException("Expression is not of type string");
        }

        StringValue fileName = (StringValue) result;
        if(state.getFileTable().contains(fileName)) {
            throw new StatementException("File already opened");
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName.getValue()));
            state.getFileTable().insert(fileName, reader);
        } catch (IOException e) {
            throw new StatementException("File not found");
        }

        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new OpenRFileStatement(this.expression.deepCopy());
    }

    @Override
    public String toString() {
        return "openRFile(" + this.expression.toString() + ")";
    }

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        this.expression.typeCheck(typeEnv);
        return typeEnv;
    }
}
