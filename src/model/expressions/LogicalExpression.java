package model.expressions;

import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import model.adt.IMyDict;
import model.adt.IMyHeap;
import model.types.BoolType;
import model.values.BoolValue;
import model.values.IValue;

public class LogicalExpression implements IExpression{
    private IExpression exp1;
    private IExpression exp2;
    private LogicalOperator op;

    public LogicalExpression(IExpression exp1, IExpression exp2, LogicalOperator op) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.op = op;
    }

    @Override
    public BoolValue evaluate(IMyDict<String, IValue> tbl, IMyHeap heap) throws ExpressionExceptions, ADTException {
        IValue val1 = this.exp1.evaluate(tbl, heap);
        IValue val2 = this.exp2.evaluate(tbl, heap);

        if (!val1.getType().equals(new BoolType())) {
            throw new ExpressionExceptions("First operand is not a boolean");
        }
        if (!val2.getType().equals(new BoolType())) {
            throw new ExpressionExceptions("Second operand is not a boolean");
        }

        switch (this.op) {
            case AND:
                return new BoolValue(((BoolValue) val1).getVal() && ((BoolValue) val2).getVal());
            case OR:
                return new BoolValue(((BoolValue) val1).getVal() || ((BoolValue) val2).getVal());
            default:
                throw new ExpressionExceptions("Invalid operator");
        }
    }

    @Override
    public IExpression deepCopy() {
        return new LogicalExpression(this.exp1.deepCopy(), this.exp2.deepCopy(), this.op);
    }

    @Override
    public String toString() {
        return this.exp1.toString() + " " + this.op + " " + this.exp2.toString();
    }
}
