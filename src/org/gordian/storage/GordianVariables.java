package org.gordian.storage;

import api.gordian.Object;
import api.gordian.storage.InternalNotFoundException;
import api.gordian.storage.Variables;
import org.gordian.value.GordianBoolean;
import org.gordian.value.GordianNull;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public class GordianVariables implements Variables {

    private final GordianStorage storage = new GordianStorage();

    {
        storage.reserve("null", new GordianNull());
        storage.reserve("true", GordianBoolean.TRUE);
        storage.reserve("false", GordianBoolean.FALSE);
    }
    
    public Object get(String name) throws InternalNotFoundException {
        return (Object) storage.get(name);
    }

    public void put(String name, Object object) {
        storage.put(name, object);
    }

    public void set(String name, Object object) {
        storage.set(name, object);
    }

    public void remove(String name) throws InternalNotFoundException {
        storage.remove(name);
    }

    public void removeAll(String name) {
        storage.removeAll(name);
    }

    public boolean contains(String name) {
        return storage.contains(name);
    }
}
