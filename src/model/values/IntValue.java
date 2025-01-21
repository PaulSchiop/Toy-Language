package model.values;

import model.types.IType;
import model.types.IntType;

public class IntValue implements IValue {
    private int value;

    public IntValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public IType getType() {
        return new IntType();
    }

    @Override
    public boolean equals(IValue other) {
        if (this == other) return true;
        if (!(other instanceof IntValue intValue)) return false;
        return this.value == intValue.value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
