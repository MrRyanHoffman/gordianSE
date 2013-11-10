package org.gordian.value;

import api.gordian.Arguments;
import api.gordian.Class;
import api.gordian.Object;
import api.gordian.Signature;
import org.gordian.GordianClass;
import org.gordian.GordianObject;
import org.gordian.method.GordianMethod;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public class GordianBoolean extends GordianObject {

    public static final Class CLASS = DummyParent.INSTANCE;
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
        return DummyParent.INSTANCE;
    }

    public Object parent() {
        return null;
    }

    public String toString() {
        return String.valueOf(val);
    }

    private static final class DummyParent extends GordianClass {

        private static final DummyParent INSTANCE = new DummyParent();

        public DummyParent() {
            super(null);
        }

        public Object contruct(Arguments arguments) {
            return null;
        }

        public Signature[] contructors() {
            return null;
        }

    }
}
