package model.values;

import model.types.IType;
import model.types.RefType;
import model.values.IValue;

public class RefValue implements IValue{
    private int address;
    private IType locationType;

    public RefValue(int address, IType locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress() {
        return this.address;
    }

    public IType getLocationType() {
        return this.locationType;
    }

    @Override
    public String toString() {
        return "(" + this.address + ", " + this.locationType.toString() + ")";
    }

    @Override
    public IType getType() {
        return new RefType(this.locationType);
    }

    @Override
    public boolean equals(IValue other) {
        if (this == other) return true;
        if (!(other instanceof RefValue refValue)) return false;
        return this.address == refValue.address && this.locationType.equals(refValue.locationType);
    }
}
