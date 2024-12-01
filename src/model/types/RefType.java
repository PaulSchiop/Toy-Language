package model.types;

import model.values.IValue;
import model.values.RefValue;

public class RefType implements IType{
    IType inner;

    public RefType(IType inner) {
        this.inner = inner;
    }

    public RefType() {
        this.inner = null;
    }

    public IType getInner() {
        return this.inner;
    }

    @Override
    public String toString() {
        return "Ref(" + this.inner.toString() + ")";
    }

    @Override
    public boolean equals(IType obj) {
        if (obj instanceof RefType) {
            return this.inner.equals(((RefType) obj).getInner());
        }
        return false;
    }

    @Override
    public IValue defaultValue() {
        return new RefValue(0, this.inner);
    }
}
