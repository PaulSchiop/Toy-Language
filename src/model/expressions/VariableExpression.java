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
    public IValue evaluate(IMyDict<String, IValue> symTbl, IMyHeap heap) throws ExpressionExceptions, ADTException {
        if (!symTbl.containsKey(this.id)) {
            throw new ExpressionExceptions("Variable '" + this.id + "' is not defined");
        }

        IValue value = symTbl.getValue(this.id);
        if (value == null) {
            throw new ExpressionExceptions("Variable '" + this.id + "' has not been initialized");
        }

        return value;
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
