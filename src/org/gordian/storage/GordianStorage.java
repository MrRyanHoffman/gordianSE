package org.gordian.storage;

import api.gordian.storage.InternalNotFoundException;
import edu.first.util.list.ArrayList;
import edu.first.util.list.Iterator;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
final class GordianStorage {

    private final ArrayList list = new ArrayList();
    private final ArrayList reserved = new ArrayList();

    public GordianStorage() {
    }

    public GordianStorage(GordianStorage s) {
        list.addAll(s.list);
        reserved.addAll(s.reserved);
    }

    protected void reserve(String name, Object value) {
        removeAll(name);
        set(name, value);
        reserved.add(name);
    }

    public Object get(String name) throws InternalNotFoundException {
        Iterator i = list.iterator();
        while (i.hasNext()) {
            Node n = (Node) i.next();
            if (n.name.equals(name)) {
                return n.object;
            }
        }
        throw new InternalNotFoundException(name);
    }

    public Object put(String name, Object object) {
        if (reserved.contains(name)) {
            throw new IllegalStateException("Cannot set final element - \"" + name + "\"");
        }
        if (object == null) {
            throw new NullPointerException("Tried to set " + name + " as null.");
        }
        list.add(0, new Node(name, object));
        return object;
    }

    public Object set(String name, Object object) {
        if (reserved.contains(name)) {
            throw new IllegalStateException("Cannot set final element - \"" + name + "\"");
        }
        if (object == null) {
            throw new NullPointerException("Tried to set " + name + " as null.");
        }
        for (int x = 0; x < list.size(); x++) {
            if (((Node) list.get(x)).name.equals(name)) {
                // operate on node to affect container scopes
                ((Node)list.get(x)).object = object;
                return object;
            }
        }
        put(name, object);
        return object;
    }

    public void remove(String name) throws InternalNotFoundException {
        Iterator i = list.iterator();
        while (i.hasNext()) {
            Node n = (Node) i.next();
            if (n.name.equals(name)) {
                list.remove(n);
                return;
            }
        }
        throw new InternalNotFoundException(name);
    }

    public void removeAll(String name) {
        for (int x = 0; x < list.size(); x++) {
            if (((Node) list.get(x)).name.equals(name)) {
                // goes backwards to check next element
                list.remove(x--);
            }
        }
    }

    public boolean contains(String name) {
        Iterator i = list.iterator();
        while (i.hasNext()) {
            Node n = (Node) i.next();
            if (n.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private final class Node {

        private final String name;
        private Object object;

        public Node(String name, Object object) {
            this.name = name;
            this.object = object;
        }
    }

}
