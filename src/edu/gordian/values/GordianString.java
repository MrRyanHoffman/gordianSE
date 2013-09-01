package edu.gordian.values;

import edu.gordian.value.Value;

public final class GordianString implements Value {

    private final String val;

    public GordianString(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
