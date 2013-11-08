package org.gordian.value;

import api.gordian.Arguments;
import api.gordian.Class;
import api.gordian.Object;
import api.gordian.Signature;
import org.gordian.GordianClass;
import org.gordian.GordianObject;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public class GordianNumber extends GordianObject {

    public static final Class CLASS = DummyParent.INSTANCE;
    private final double val;

    public GordianNumber(double val) {
        this.val = val;
    }

    public double getValue() {
        return val;
    }

    public int getInt() {
        return (int) val;
    }
    
    public long getLong() {
        return (long) val;
    }

    public boolean equals(Object object) {
        if (object instanceof GordianNumber) {
            return ((GordianNumber) object).val == val;
        } else {
            return false;
        }
    }

    public Class parentClass() {
        return DummyParent.INSTANCE;
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
