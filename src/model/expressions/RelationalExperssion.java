package model.expressions;

import model.adt.IMyDict;
import model.adt.IMyHeap;
import model.values.BoolValue;
import model.values.IValue;
import exceptions.ExpressionExceptions;
import exceptions.ADTException;
import model.values.IntValue;
import model.types.IntType;

public class RelationalExperssion implements IExpression{
    private IExpression exp1;
    private IExpression exp2;
    private RelationalOperator op;

    public RelationalExperssion(IExpression exp1, IExpression exp2, RelationalOperator op) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.op = op;
    }

    @Override
    public IValue evaluate(IMyDict<String, IValue> symTbl, IMyHeap heap) throws ExpressionExceptions, ADTException {
        IValue val1 = this.exp1.evaluate(symTbl, heap);
        IValue val2 = this.exp2.evaluate(symTbl, heap);

        if (!(val1.getType() instanceof IntType)) {
            throw new ExpressionExceptions("First operand is not an integer");
        }
        if (!(val2.getType() instanceof IntType)) {
            throw new ExpressionExceptions("Second operand is not an integer");
        }

        int intVal1 = ((IntValue) val1).getValue();
        int intVal2 = ((IntValue) val2).getValue();

        boolean result;
        switch (this.op) {
            case SMALLER:
                result = intVal1 < intVal2;
                break;
            case SMALLER_OR_EQUAL:
                result = intVal1 <= intVal2;
                break;
            case GREATER:
                result = intVal1 > intVal2;
                break;
            case GREATER_OR_EQUAL:
                result = intVal1 >= intVal2;
                break;
            case EQUAL:
                result = intVal1 == intVal2;
                break;
            case NOT_EQUAL:
                result = intVal1 != intVal2;
                break;
            default:
                throw new ExpressionExceptions("Invalid relational operator");
        }

        return new BoolValue(result); // Return BoolValue instead of IntValue
    }



    @Override
    public String toString() {
        return this.exp1.toString() + this.op + this.exp2.toString();
    }

    @Override
    public IExpression deepCopy() {
        return new RelationalExperssion(this.exp1.deepCopy(), this.exp2.deepCopy(), this.op);
    }
}
