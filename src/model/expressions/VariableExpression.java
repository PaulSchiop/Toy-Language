package model.expressions;

import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import model.adt.IMyHeap;
import model.values.BoolValue;
import model.values.IValue;
import model.adt.IMyDict;

public class VariableExpression implements IExpression{
    private String id;

    public VariableExpression(String id) {
        this.id = id;
    }

    @Override
    public IValue evaluate(IMyDict<String, IValue> tbl, IMyHeap heap) throws ExpressionExceptions, ADTException {
        return tbl.getValue(id);
    }

    @Override
    public IExpression deepCopy() {
        return new VariableExpression(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
