package model.statements;
import model.state.PrgState;
import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import exceptions.StatementException;

import java.io.IOException;

public interface IStatement {
    PrgState execute(PrgState state) throws StatementException, ExpressionExceptions, IOException, ADTException;
    IStatement deepCopy();
}
