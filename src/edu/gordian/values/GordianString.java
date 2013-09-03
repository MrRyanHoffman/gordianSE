package edu.gordian.values;

import language.value.Value;

public final class GordianString implements Value {

    private final String val;

    public GordianString(String val) {
        this.val = val
                .replaceAll("\\\\\"", "\"")
                .replaceAll("\\\\t", "\t")
                .replaceAll("\\\\n", "\n");
    }

    public boolean equals(Object obj) {
        if (obj instanceof GordianString) {
            return ((GordianString) obj).val.equals(val);
        } else {
            return false;
        }
    }

    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.val.hashCode();
        return hash;
    }

    public String toString() {
        return val;
    }
}
