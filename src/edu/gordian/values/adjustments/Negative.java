package edu.gordian.values.adjustments;

import edu.gordian.scopes.Scope;
import edu.gordian.values.gordian.GordianNumber;

public final class Negative extends GordianNumber {

    public static boolean is(Scope s, String v) {
        return v.startsWith("-") && (s.toValue(v.substring(1)) instanceof GordianNumber);
    }

    public static Negative valueOf(Scope s, String v) {
        return new Negative(-((GordianNumber) s.toValue(v.substring(1))).doubleValue());
    }

    public Negative(double value) {
        super(value);
    }
}