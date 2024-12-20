package model.statements;
import model.adt.IMyDict;
import model.state.PrgState;
import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import exceptions.StatementException;
import model.types.IType;

import java.io.IOException;

public interface IStatement {
    PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, IOException, ADTException;
    IStatement deepCopy();
    IMyDict<String, IType> typeCheck(IMyDict<String, IType> typeEnv) throws StatementException;
}
