package model.statements;

import model.adt.IMyDict;
import model.state.PrgState;
import exceptions.StatementException;
import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import model.statements.IStatement;
import model.expressions.IExpression;
import model.types.IType;
import model.types.IntType;
import model.values.IValue;
import model.values.IntValue;
import model.types.StringType;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStatement implements IStatement{
    private IExpression expression;
    private String varName;

    public ReadFileStatement(IExpression expression, String varName) {
        this.expression = expression;
        this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, IOException, ADTException {
        var table = state.getSymTable();

        if (!table.contains(this.varName)) {
            throw new StatementException("Variable not declared");
        }

        if (!table.getValue(this.varName).getType().equals(new IntType())) {
            throw new StatementException("Variable is not of type int");
        }

        IValue result = this.expression.evaluate(table, state.getHeap());

        if (!result.getType().equals(new StringType())) {
            throw new StatementException("Expression is not of type string");
        }

        BufferedReader reader = state.getFileTable().getValue((StringValue) result);

        String read = reader.readLine();

        if (read.isEmpty()){
            read = "0";
        }

        int parser = Integer.parseInt(read);

        table.insert(this.varName, new IntValue(parser));

        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new ReadFileStatement(this.expression, this.varName);
    }

    @Override
    public String toString() {
        return "ReadFile(" + this.expression.toString() + ", " + this.varName + ")";
    }

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        this.expression.typeCheck(typeEnv);
        return typeEnv;
    }
}
