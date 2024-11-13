package model.expressions;
import model.statements.IStatement;
import model.state.PrgState;
import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import model.values.IValue;
import model.adt.IMyDict;

public interface IExpression {
    IValue evaluate(IMyDict<String, IValue> tbl) throws ExpressionExceptions, ADTException;
    IExpression deepCopy();
}
