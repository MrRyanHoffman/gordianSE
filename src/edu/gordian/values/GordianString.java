package edu.gordian.values;

import edu.gordian.value.Value;
import java.util.Objects;

public final class GordianString implements Value {

    private final String val;

    public GordianString(String val) {
        this.val = val;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GordianString) {
            return ((GordianString) obj).val.equals(val);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.val);
        return hash;
    }

    @Override
    public String toString() {
        return val;
    }
}
