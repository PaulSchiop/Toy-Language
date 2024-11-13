package model.types;

import model.values.IValue;
import model.values.IntValue;

public class IntType implements IType {
    @Override
    public boolean equals(IType obj) {
        return obj instanceof IntType;
    }

    public String toString() {
        return "int";
    }

    @Override
    public IValue defaultValue() {
        return new IntValue(0);
    }
}
