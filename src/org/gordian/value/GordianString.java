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
public class GordianString extends GordianObject {

    public static final Class CLASS = DummyParent.INSTANCE;
    private final String val;

    public GordianString(String val) {
        this.val = val;
    }

    public String getValue() {
        return val;
    }

    public boolean equals(Object object) {
        if (object instanceof GordianString) {
            return ((GordianString) object).val.equals(val);
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
