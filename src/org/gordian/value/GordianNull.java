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
public class GordianNull extends GordianObject {

    public static final Class CLASS = DummyParent.INSTANCE;

    public boolean equals(Object object) {
        return object instanceof GordianNull;
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
