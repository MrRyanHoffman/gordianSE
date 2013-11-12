package org.gordian.value;

import api.gordian.Arguments;
import api.gordian.Class;
import api.gordian.Object;
import api.gordian.Signature;
import edu.first.util.list.Collections;
import edu.first.util.list.List;
import org.gordian.GordianClass;
import org.gordian.GordianObject;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public class GordianList extends GordianObject {

    public static final Class CLASS = DummyParent.INSTANCE;
    private final List list;

    public GordianList(Object[] values) {
        list = Collections.asList(values);
    }

    public Object get(int index) {
        return (Object) list.get(index);
    }

    public boolean equals(Object object) {
        if (object instanceof GordianList) {
            return list.equals(((GordianList) object).list);
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
        return list.toString();
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
