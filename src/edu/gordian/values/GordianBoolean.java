package edu.gordian.values;

import edu.gordian.value.Value;

public final class GordianBoolean implements Value {

    private final boolean val;

    public GordianBoolean(boolean val) {
        this.val = val;
    }

    public boolean get() {
        return val;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GordianBoolean) {
            return ((GordianBoolean) obj).val == val;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.val ? 1 : 0);
        return hash;
    }
}
