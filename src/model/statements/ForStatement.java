package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import exceptions.StatementException;
import model.adt.IMyDict;
import model.expressions.*;
import model.state.PrgState;
import model.types.BoolType;
import model.types.IType;
import model.types.IntType;
import model.values.IntValue;
import model.values.StringValue;

import java.io.IOException;

public class ForStatement implements IStatement{
    private final IExpression exp;
    private final IStatement stmt;
    private final IExpression start;
    private final IExpression end;
    private final String varName;

    public ForStatement(String varname ,IExpression start, IExpression exp, IExpression end, IStatement stmt) {
        this.varName = varname;
        this.exp = exp;
        this.stmt = stmt;
        this.start = start;
        this.end = end;
    }

    @Override
    public PrgState execute(PrgState state) throws ADTException, IOException, StatementException {
        IStatement newStatement = new CompStatement(
                new VariableDeclarationStatement(varName, new IntType()),
                new CompStatement(
                        new AssignStatement(varName, start),
                        new WhileStatement(
                                exp,
                                new CompStatement(
                                        stmt,
                                        new AssignStatement(varName, end)
                                )
                        )
                )
        );

        state.getExeStack().push(newStatement);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new ForStatement(this.varName, this.start.deepCopy(), this.exp.deepCopy(), this.end.deepCopy(), this.stmt.deepCopy());
    }

    @Override
    public IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException {
        typeEnv.insert(this.varName, new IntType());

        if(!start.typeCheck(typeEnv).equals(new IntType())
                || !end.typeCheck(typeEnv).equals(new IntType())
                || !exp.typeCheck(typeEnv).equals(new BoolType())){
            throw new StatementException("All expressions must be int");
        }

        stmt.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "for(" + varName + "=" + start.toString() + ";" + exp.toString() + ";" + varName + "=" + end.toString() + "){" + stmt.toString() + "}";
    }
}
