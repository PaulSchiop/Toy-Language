package model.expressions;

import model.adt.IMyDict;
import model.adt.IMyHeap;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;

public class ValueExpression implements IExpression{
    private IValue value;

    public ValueExpression(IValue value) {
        this.value = value;
    }

    @Override
    public IValue evaluate(IMyDict<String, IValue> tbl, IMyHeap heap) {
        return value;
    }

    @Override
    public IExpression deepCopy() {
        return new ValueExpression(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public IType typeCheck(IMyDict<String, IType> typeEnv) {
        return value.getType();
    }
}
