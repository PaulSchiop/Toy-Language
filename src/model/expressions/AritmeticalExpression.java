package model.expressions;

import model.adt.IMyDict;
import model.values.IValue;
import model.values.IntValue;
import model.types.IntType;
import exceptions.ExpressionExceptions;
import exceptions.ADTException;

public class AritmeticalExpression implements IExpression {
    private IExpression exp1;
    private IExpression exp2;
    private AritmeticalOperator op;

    public AritmeticalExpression(IExpression exp1, IExpression exp2, AritmeticalOperator op) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.op = op;
    }

    public IValue evaluate(IMyDict<String, IValue> symTbl) throws ExpressionExceptions, ADTException {
        IValue val1 = this.exp1.evaluate(symTbl);
        IValue val2 = this.exp2.evaluate(symTbl);

        if (!val1.getType().equals(new IntType())) {
            throw new ExpressionExceptions("First operand is not an integer");
        }
        if (!val2.getType().equals(new IntType())) {
            throw new ExpressionExceptions("Second operand is not an integer");
        }

        IntValue intVal1 = (IntValue) val1;
        IntValue intVal2 = (IntValue) val2;

        switch (this.op) {
            case ADD:
                return new IntValue(intVal1.getValue() + intVal2.getValue());
            case SUB:
                return new IntValue(intVal1.getValue() - intVal2.getValue());
            case MUL:
                return new IntValue(intVal1.getValue() * intVal2.getValue());
            case DIV:
                if (intVal2.getValue() == 0) {
                    throw new ExpressionExceptions("Division by zero");
                }
                return new IntValue(intVal1.getValue() / intVal2.getValue());
            default:
                throw new ExpressionExceptions("Invalid operator");
        }
    }

    @Override
    public String toString() {
        return this.exp1.toString() + this.op + this.exp2.toString();
    }

    @Override
    public IExpression deepCopy() {
        return new AritmeticalExpression(this.exp1.deepCopy(), this.exp2.deepCopy(), this.op);
    }
}
