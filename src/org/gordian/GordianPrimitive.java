package org.gordian;

import api.gordian.Arguments;
import org.gordian.storage.GordianVariables;
import org.gordian.storage.GordianMethods;
import api.gordian.Object;
import api.gordian.Signature;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public abstract class GordianPrimitive implements Object {

    private final GordianMethods methods = new GordianMethods(true);
    private final GordianVariables variables = new GordianVariables(true);

    public GordianMethods methods() {
        return methods;
    }

    public GordianVariables variables() {
        return variables;
    }

    protected static class PrimitiveClass extends GordianClass {

        public PrimitiveClass() {
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
