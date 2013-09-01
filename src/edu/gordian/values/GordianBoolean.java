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
}
