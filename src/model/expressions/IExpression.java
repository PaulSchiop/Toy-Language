package model.expressions;
import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;
import model.adt.IMyDict;
import model.adt.IMyHeap;

public interface IExpression {
    IValue evaluate(IMyDict<String, IValue> tbl, IMyHeap hp) throws ExpressionExceptions, ADTException;
    IExpression deepCopy();
    IType typeCheck(IMyDict<String, IType> typeEnv) throws ExpressionExceptions;
}
