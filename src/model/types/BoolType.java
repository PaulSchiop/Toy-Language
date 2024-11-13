package model.types;

import model.values.IValue;
import model.values.IValue;

public class BoolType implements IType{
    @Override
    public boolean equals(IType obj) {
        return obj instanceof BoolType;
    }

    @Override
    public IValue defaultValue() {
        return null;
    }

    @Override
    public String toString() {
        return "bool";
    }
}