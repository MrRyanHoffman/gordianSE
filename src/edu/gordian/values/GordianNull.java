package edu.gordian.values;

import edu.gordian.value.Value;

public final class GordianNull implements Value {

    private static final GordianNull NULL = new GordianNull();

    public static GordianNull get() {
        return NULL;
    }

    private GordianNull() {
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GordianNull;
    }

    @Override
    public String toString() {
        return "null";
    }
}
