package model.statements;

import exceptions.StatementException;
import model.state.PrgState;
import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import model.types.IType;
import model.values.IValue;
import model.expressions.IExpression;

import java.io.IOException;

public class VariableDeclarationStatement implements IStatement{
    private String name;
    private IType type;

    public VariableDeclarationStatement(String name, IType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, IOException, ADTException {
        if (state.getSymTable().containsKey(this.name)){
            throw new StatementException("Variable already declared");
        }
        state.getSymTable().insert(this.name, this.type.defaultValue());
        return state;
    }

    @Override
    public IStatement deepCopy() {
        return null;
    }

    @Override
    public String toString() {
        return this.type.toString() + " " + this.name;
    }
}