package org.gordian;

import org.gordian.storage.GordianVariables;
import org.gordian.storage.GordianMethods;
import api.gordian.Object;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public abstract class GordianObject implements Object {

    private final GordianMethods methods = new GordianMethods(true);
    private final GordianVariables variables = new GordianVariables(true);

    public GordianMethods methods() {
        return methods;
    }

    public GordianVariables variables() {
        return variables;
    }

}
