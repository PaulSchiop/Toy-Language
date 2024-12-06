package model.values;

import model.types.IType;
import model.types.BoolType;

public class BoolValue implements IValue{
    private boolean val;

    public BoolValue(boolean v){
        this.val = v;
    }

    public boolean getVal(){
        return this.val;
    }

    public boolean equals(IValue other){
        return other instanceof BoolValue && ((BoolValue) other).getVal() == this.val;
    }

    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public String toString() {
        return "bool";
    }

}
