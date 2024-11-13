package model.expressions;

import model.adt.IMyDict;
import model.values.IValue;

public class ValueExpression implements IExpression{
    private IValue value;

    public ValueExpression(IValue value) {
        this.value = value;
    }

    @Override
    public IValue evaluate(IMyDict<String, IValue> tbl) {
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
}
