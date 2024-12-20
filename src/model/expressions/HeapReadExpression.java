package model.expressions;

import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import model.adt.IMyDict;
import model.adt.IMyHeap;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class HeapReadExpression implements IExpression{
    private IExpression expression;

    public HeapReadExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IValue evaluate(IMyDict<String, IValue> symTable, IMyHeap heap) throws ExpressionExceptions, ADTException {
        IValue value = expression.evaluate(symTable, heap);
        if(!(value instanceof RefValue refValue)) {
            throw new ExpressionExceptions("Expression does not evaluate to a reference");
        }
        return heap.getValue(refValue.getAddress());
    }

    @Override
    public IExpression deepCopy() {
        return new HeapReadExpression(expression.deepCopy());
    }

    @Override
    public String toString() {
        return "rH(" + expression.toString() + ")";
    }

    @Override
    public IType typeCheck(IMyDict<String, IType> typeEnv) throws ExpressionExceptions {
        IType type = expression.typeCheck(typeEnv);
        if(type instanceof RefType refType) {
            return refType.getInner();
        }
        throw new ExpressionExceptions("Expression does not evaluate to a reference");
    }
}