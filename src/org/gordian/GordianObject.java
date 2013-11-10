package org.gordian;

import org.gordian.storage.GordianVariables;
import org.gordian.storage.GordianMethods;
import api.gordian.Object;
import api.gordian.methods.Method;
import api.gordian.storage.InternalNotFoundException;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public abstract class GordianObject implements Object {

    private final GordianMethods methods = new GordianMethods(true);
    private final GordianVariables variables = new GordianVariables(true);

    {
        Object parent = parent();
        if (parent != null) {
            variables.put("super", parent);
        }
    }

    protected GordianMethods getMethods() {
        return methods;
    }

    protected GordianVariables getVariables() {
        return variables;
    }

    public Method getMethod(String name) throws InternalNotFoundException {
        return methods.get(name);
    }

    public Object getVariable(String name) throws InternalNotFoundException {
        return variables.get(name);
    }

}
