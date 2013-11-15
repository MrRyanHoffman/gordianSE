package org.gordian.value;

import api.gordian.Class;
import api.gordian.Object;
import org.gordian.GordianPrimitive;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public class GordianBoolean extends GordianPrimitive {

    public static final Class CLASS = Parent.CLASS;
    public static final GordianBoolean TRUE = new GordianBoolean(true);
    public static final GordianBoolean FALSE = new GordianBoolean(false);
    private final boolean val;

    public GordianBoolean(boolean val) {
        this.val = val;
    }

    public boolean getValue() {
        return val;
    }

    public boolean equals(Object object) {
        if (object instanceof GordianBoolean) {
            return ((GordianBoolean) object).val == val;
        } else {
            return false;
        }
    }

    public Class parentClass() {
        return CLASS;
    }

    public Object parent() {
        return null;
    }

    public String toString() {
        return String.valueOf(val);
    }

    private static final class Parent extends PrimitiveClass {

        private static final Class CLASS = new Parent();
    }
}
