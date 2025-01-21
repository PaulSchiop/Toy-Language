package model.values;

import model.types.IType;
import model.types.StringType;

public class StringValue implements IValue{
    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(IValue val) {
        return val.getType().equals(new StringType()) && ((StringValue) val).getValue().equals(this.value) ;
    }
}
